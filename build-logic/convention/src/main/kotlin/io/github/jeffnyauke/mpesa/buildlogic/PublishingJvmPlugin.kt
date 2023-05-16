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

import io.github.jeffnyauke.mpesa.buildlogic.util.CI
import io.github.jeffnyauke.mpesa.buildlogic.util.isMainHost
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.publish.tasks.GenerateModuleMetadata
import org.gradle.api.specs.Spec
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class PublishingJvmPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("idea")
                apply("org.jetbrains.kotlin.jvm")
                apply("build-logic.publishing")
            }

            val mainHostSpec: Spec<in Task> = Spec { !CI || isMainHost }

            tasks {
                withType<KotlinCompile> {
                    onlyIf(mainHostSpec)
                    inputs.property("project.mainOS", project.property("project.mainOS"))
                }
            }

            afterEvaluate {
                configure<PublishingExtension> {
                    publications {
                        all {
                            val targetPublication = this@all
                            tasks.withType<AbstractPublishToMaven>()
                                .matching { it.publication == targetPublication }
                                .configureEach {
                                    onlyIf(mainHostSpec)
                                }
                            tasks.withType<GenerateModuleMetadata>()
                                .matching { it.publication.get() == targetPublication }
                                .configureEach {
                                    onlyIf(mainHostSpec)
                                }
                        }
                    }
                }
            }
        }
    }
}
