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

package io.github.jeffnyauke.mpesa.buildlogic.util

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import java.io.ByteArrayOutputStream

/**
 * Android build
 */
fun Project.configureAndroid() {
    configure<LibraryExtension> {
        compileSdk = libs.version("gradle-android-compile-sdk").toInt()
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        defaultConfig {
            minSdk = libs.version("gradle-android-min-sdk").toInt()
        }
        publishing.singleVariant("release") { withSourcesJar() }
    }
}

/**
 * android library
 */
fun Project.configureAndroidLibrary() {
    extensions.configure<LibraryExtension> {
        buildFeatures {
            buildConfig = false
        }
        sourceSets.configureEach {
            setRoot("src/android/$name")
            java.srcDirs("src/android/$name/kotlin")
        }
    }
}

/**
 * VersionCatalog
 */
val Project.libs: VersionCatalog
    get() {
        return extensions.getByType<VersionCatalogsExtension>().named("libs")
    }

/**
 * VersionCatalog version
 */
fun VersionCatalog.version(name: String): String {
    return findVersion(name).get().requiredVersion
}

/**
 * VersionCatalog pluginId
 */
fun VersionCatalog.pluginId(name: String): String {
    return findPlugin(name).get().get().pluginId
}

/**
 * Execute shell command
 */
fun Project.execute(vararg commands: String): String {
    val out = ByteArrayOutputStream()
    exec {
        commandLine = listOf("sh", "-c") + commands
        standardOutput = out
        isIgnoreExitValue = true
    }
    return out.toString().trim()
}
