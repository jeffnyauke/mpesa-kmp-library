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
plugins { id(libs.plugins.buildlogic.multiplatform.library.get().pluginId) }

description = "Local test utilities"

android.namespace = "local.test"

kotlin {
    explicitApi = null
    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("test"))
                api(kotlin("test-annotations-common"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        named("androidMain") { dependencies { api(kotlin("test-junit5")) } }
        named("jvmMain") { dependencies { api(kotlin("test-junit5")) } }
        named("jsMain") { dependencies { api(kotlin("test-js")) } }
    }
}
