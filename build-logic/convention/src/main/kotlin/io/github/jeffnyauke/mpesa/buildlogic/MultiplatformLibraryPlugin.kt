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

import io.github.jeffnyauke.mpesa.buildlogic.util.KotlinTargetDetails
import io.github.jeffnyauke.mpesa.buildlogic.util.buildHost
import io.github.jeffnyauke.mpesa.buildlogic.util.configureAndroid
import io.github.jeffnyauke.mpesa.buildlogic.util.nativeTargetGroup
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.konan.target.KonanTarget

class MultiplatformLibraryPlugin : Plugin<Project> {
    override fun apply(multiplatformTarget: Project) {
        with(multiplatformTarget) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("build-logic.common")
            }

            configureMultiplatformLibrary()
            configureAndroid()

            tasks {
                withType<CInteropProcess> { onlyIf { konanTarget.buildHost == HostManager.host.family } }

                withType<AbstractKotlinNativeCompile<*, *>> {
                    val konanTarget = KonanTarget.Companion.predefinedTargets[target]!!
                    onlyIf { konanTarget.buildHost == HostManager.host.family }
                }
            }
        }
    }

    private fun Project.configureMultiplatformLibrary() {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets {
                val commonMain by getting
                val commonTest by getting { dependencies { implementation(project(":test")) } }
                create("nativeMain") { dependsOn(commonMain) }
                create("nativeTest") { dependsOn(commonTest) }
                all {
                    languageSettings.apply {
                        optIn("kotlin.js.ExperimentalJsExport")
                    }
                }
            }

            explicitApi()
            android()
            jvm()
            js(IR) {
                binaries.library()
                useCommonJs()
                nodejs {
                    testTask {
                        useMocha {
                            timeout = "10000"
                        }
                    }
                }
                browser {
                    commonWebpackConfig {
                        cssSupport {
                            enabled.set(true)
                        }
                    }
                    testTask {
                        useKarma {
                            // useFirefox()
                            useChrome()
                            // useSafari()
                        }
                    }
                }
                generateTypeScriptDefinitions()
            }

//        nativeTargetGroup(
//            "androidNdk",
//            androidNativeArm32(),
//            androidNativeArm64(),
//        )

            nativeTargetGroup(
                "linux",
                linuxX64(),
            )

            nativeTargetGroup(
                "ios",
                iosArm64(),
                iosX64(),
                iosSimulatorArm64(),
            )

            nativeTargetGroup(
                "watchos",
                watchosArm32(),
                watchosArm64(),
                watchosX64(),
                watchosSimulatorArm64(),
            )

            nativeTargetGroup(
                "tvos",
                tvosArm64(),
                tvosX64(),
                tvosSimulatorArm64(),
            )

            nativeTargetGroup(
                "macos",
                macosX64(),
                macosArm64(),
            )

            nativeTargetGroup(
                "mingw",
                mingwX64(),
            )

            val targetsWithCoroutines =
                KotlinTargetDetails.values().filter(KotlinTargetDetails::hasCoroutines)
                    .map(KotlinTargetDetails::presetName)

            targets.filter { it.preset?.name in targetsWithCoroutines }.forEach {
                (
                    it.compilations.findByName("main")?.defaultSourceSet
                        ?: sourceSets.findByName("${it.name}Main")
                    )?.apply {
                    dependencies { api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4") }
                }
            }
        }
    }
}
