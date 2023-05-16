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

import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.nativeTargetGroup(
    name: String,
    vararg targets: KotlinNativeTarget,
): Array<out KotlinNativeTarget> {
    sourceSets {
        val (main, test) = if (targets.size > 1) {
            val nativeMain = getByName("nativeMain")
            val nativeTest = getByName("nativeTest")
            val main = create("${name}Main") {
                dependsOn(nativeMain)
            }
            val test = create("${name}Test") {
                dependsOn(nativeTest)
            }
            main to test
        } else {
            (null to null)
        }

        targets.forEach { target ->
            main?.let {
                target.compilations["main"].defaultSourceSet {
                    dependsOn(main)
                }
            }
            test?.let {
                target.compilations["test"].defaultSourceSet {
                    dependsOn(test)
                }
            }
        }
    }
    return targets
}
