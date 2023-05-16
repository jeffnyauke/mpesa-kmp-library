#!/usr/bin/env kotlin -Xplugin=/opt/homebrew/opt/kotlin/libexec/lib/kotlinx-serialization-compiler-plugin.jar

// usage: ./removeGhPackageVersion.main.kts template-kmp-library 1.1.3

@file:DependsOn("io.ktor:ktor-client-cio-jvm:2.2.2")
@file:DependsOn("io.ktor:ktor-client-content-negotiation-jvm:2.2.2")
@file:DependsOn("io.ktor:ktor-client-auth-jvm:2.2.2")
@file:DependsOn("io.ktor:ktor-serialization-kotlinx-json-jvm:2.2.2")
@file:Suppress("PLUGIN_IS_NOT_ENABLED")

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val targetRepo = args.getOrNull(0) ?: error("Target repository not specified")
val targetVersion = args.getOrNull(1) ?: error("Target version not specified")
println("TargetRepo[$targetRepo] TargetVersion[$targetVersion]")

val ghClient = HttpClient(CIO) {
    install(Auth) {
        basic {
            credentials {
                BasicAuthCredentials(
                    username = System.getenv("GITHUB_LOGIN"),
                    password = System.getenv("GITHUB_OAUTH")
                )
            }
            sendWithoutRequest { true }
        }
    }
    install(ContentNegotiation) {
        json(jsonLenient)
    }
    defaultRequest {
        host = "api.github.com"
        url {
            protocol = URLProtocol.HTTPS
        }
        header("Accept", "application/vnd.github.v3+json")
    }
}

private val jsonLenient = Json {
    isLenient = true
    ignoreUnknownKeys = true
    coerceInputValues = true
    prettyPrint = true
}

@Serializable
data class GHRepository(
    val id: String,
    @SerialName("node_id") val nodeId: String,
    val name: String,
    @SerialName("full_name") val fullName: String
)

@Serializable
data class GHPackage(
    val id: String,
    val name: String,
    @SerialName("package_type") val packageType: String,
    val repository: GHRepository
)

@Serializable
data class GHPackageVersion(val id: String, val name: String)

runBlocking {
    val packages = mutableListOf<GHPackage>().run {
        var i = 0
        while (true) {
            val page: List<GHPackage> = ghClient.get("/user/packages") {
                parameter("package_type", "maven")
                parameter("page", "${++i}")
            }.body()
            if (page.isEmpty()) {
                break
            } else {
                addAll(page.filter { it.repository.name == targetRepo })
            }
        }
        toList()
    }
    val versions = packages.map {
        it to mutableListOf<GHPackageVersion>().run {
            var i = 0
            while (true) {
                val page: List<GHPackageVersion> =
                    ghClient.get("/user/packages/${it.packageType}/${it.name}/versions") {
                        parameter("package_type", "maven")
                        parameter("page", "${++i}")
                    }.body()
                if (page.isEmpty()) {
                    break
                } else {
                    addAll(page)
                }
            }
            toList()
        }

    }
    versions.forEach { (pkg, ver) ->
        if (ver.size > 1) {
            ver.firstOrNull { it.name == targetVersion }?.let {
                println("Deleting ${pkg.name}@${it.name}")
                ghClient.delete("/user/packages/${pkg.packageType}/${pkg.name}/versions/${it.id}")
            }
        } else if (ver.any { it.name == targetVersion }) {
            println("Deleting ${pkg.name}")
            ghClient.delete("/user/packages/${pkg.packageType}/${pkg.name}")
        }
    }
}
