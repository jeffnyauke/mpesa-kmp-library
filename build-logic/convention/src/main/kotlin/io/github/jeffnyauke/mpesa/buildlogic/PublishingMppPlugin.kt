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
import io.github.jeffnyauke.mpesa.buildlogic.util.SANDBOX
import io.github.jeffnyauke.mpesa.buildlogic.util.buildHost
import io.github.jeffnyauke.mpesa.buildlogic.util.isMainHost
import org.gradle.api.Named
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.publish.tasks.GenerateModuleMetadata
import org.gradle.api.specs.Spec
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.HostManager

class PublishingMppPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("build-logic.publishing")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                fun Collection<KotlinTarget>.onlyBuildIf(enabled: Spec<in Task>) {
                    forEach {
                        it.compilations.all {
                            val compileKotlinTask = compileTaskProvider.get() as? KotlinCompile
                            compileKotlinTask?.onlyIf(enabled)
                        }
                    }
                }

                fun Collection<Named>.onlyPublishIf(enabled: Spec<in Task>) {
                    val publications: Collection<String> = map { it.name }
                    afterEvaluate {
                        configure<PublishingExtension> {
                            publications {
                                matching { it.name in publications }.all {
                                    val targetPublication = this@all
                                    tasks.withType<AbstractPublishToMaven>()
                                        .matching { it.publication == targetPublication }.configureEach {
                                            onlyIf(enabled)
                                        }
                                    tasks.withType<GenerateModuleMetadata>()
                                        .matching { it.publication.get() == targetPublication }.configureEach {
                                            onlyIf(enabled)
                                        }
                                }
                            }
                        }
                    }
                }

                val nativeTargets = targets.withType<KotlinNativeTarget>()
                val windowsHostTargets = nativeTargets.filter { it.konanTarget.buildHost == Family.MINGW }
                val linuxHostTargets = nativeTargets.filter { it.konanTarget.buildHost == Family.LINUX }
                val osxHostTargets = nativeTargets.filter { it.konanTarget.buildHost == Family.OSX }
                val androidTargets = targets.withType<KotlinAndroidTarget>()
                val mainHostTargets = targets.filter { it !in nativeTargets }
                logger.info("Linux host targets: $linuxHostTargets")
                logger.info("OSX host targets: $osxHostTargets")
                logger.info("Windows host targets: $windowsHostTargets")
                logger.info("Android targets: $androidTargets")
                logger.info("Main host targets: $mainHostTargets")

                linuxHostTargets.onlyBuildIf { !CI || HostManager.hostIsLinux }
                linuxHostTargets.onlyPublishIf { !CI || HostManager.hostIsLinux }

                osxHostTargets.onlyBuildIf { !CI || HostManager.hostIsMac }
                osxHostTargets.onlyPublishIf { !CI || HostManager.hostIsMac }

                windowsHostTargets.onlyBuildIf { !CI || HostManager.hostIsMingw }
                windowsHostTargets.onlyPublishIf { !CI || HostManager.hostIsMingw }

                androidTargets.forEach {
                    if (!CI || SANDBOX || isMainHost) {
                        it.publishLibraryVariants("release", "debug")
                    }
                }

                mainHostTargets.onlyBuildIf {
                    !CI || SANDBOX || isMainHost
                }
                (mainHostTargets + Named { "kotlinMultiplatform" }).onlyPublishIf {
                    !CI || SANDBOX || isMainHost
                }
            }
        }
    }
}
