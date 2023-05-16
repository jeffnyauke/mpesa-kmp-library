/*
 * Copyright 2023 Jeffrey Nyauke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jeffnyauke.mpesa.core.network

import io.github.jeffnyauke.mpesa.core.constant.Environment
import io.github.jeffnyauke.mpesa.core.error.MpesaException
import io.github.jeffnyauke.mpesa.core.response.ErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmSynthetic
import kotlin.time.Duration.Companion.seconds
import co.touchlab.kermit.Logger.Companion as KermitLogger

@JvmSynthetic
internal fun provideHttpClient(environment: Environment): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(jsonLenient)
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    KermitLogger.v("HTTP Client", null, message)
                }
            }
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10.seconds.inWholeMilliseconds
            connectTimeoutMillis = 10.seconds.inWholeMilliseconds
        }
        defaultRequest {
            url.protocol = URLProtocol.HTTPS
            url.host = environment.host
            header("cache-control", "no-cache")
            contentType(ContentType.Application.Json)
        }
        addDefaultResponseValidation()
    }.apply {
        sendPipeline.intercept(HttpSendPipeline.Before) {
            try {
                proceed()
            } catch (e: Throwable) {
                throw MpesaException(e)
            }
        }
    }
}

internal suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
    return if (status == HttpStatusCode.OK) {
        body()
    } else {
        val bodyText = bodyAsText()
        val bodyError = try {
            jsonLenient.decodeFromString<ErrorResponse>(bodyText)
        } catch (e: SerializationException) {
            null
        }
        throw MpesaException(bodyError)
    }
}

private val jsonLenient = Json {
    isLenient = true
    ignoreUnknownKeys = true
    coerceInputValues = true
    prettyPrint = true
}
