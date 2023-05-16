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

package io.github.jeffnyauke.mpesa.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinTest
import java.io.File
import java.util.Properties

class CommonPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("idea")
                apply("build-logic.spotless")
            }

            configureIdea()
            configureTests()
            configureKotlin()

            rootDir.resolve("local.properties").takeIf(File::exists)?.let {
                Properties().apply {
                    it.inputStream().use(::load)
                }.mapKeys { (k, _) -> k.toString() }
            }?.toList()?.forEach { (k, v) ->
                extra[k] = v
            }

            tasks {
                afterEvaluate {
                    if (tasks.findByName("compile") == null) {
                        register("compile") {
                            dependsOn(withType(AbstractKotlinCompile::class))
                            group = "build"
                        }
                    }
                    if (tasks.findByName("allTests") == null) {
                        register("allTests") {
                            dependsOn(withType(KotlinTest::class))
                            group = "verification"
                        }
                    }
                }
            }
        }
    }
}

private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
}

private fun Project.configureIdea() {
    configure<IdeaModel> {
        module {
            isDownloadSources = true
            isDownloadJavadoc = true
        }
    }
}

private fun Project.configureTests() {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
