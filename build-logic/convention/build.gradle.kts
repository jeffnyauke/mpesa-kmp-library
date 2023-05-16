/*
 * Copyright 2022 Jeffrey Nyauke
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

plugins {
    idea
    signing
    `kotlin-dsl`
    `maven-publish`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.kotlin.serialization.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    compileOnly(libs.dokka.gradlePlugin)
    compileOnly(libs.maven.publish.gradlePlugin)
    compileOnly(libs.nexus.gradlePlugin)
    compileOnly(libs.git.hooks.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("common") {
            id = "build-logic.common"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.CommonPlugin"
        }
        register("kotlin.multiplatform") {
            id = "build-logic.multiplatform.library"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.MultiplatformLibraryPlugin"
        }
        register("android.application") {
            id = "build-logic.android.application"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.AndroidApplicationPlugin"
        }
        register("android.library") {
            id = "build-logic.android.library"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.AndroidLibraryPlugin"
        }
        register("git.hooks") {
            id = "build-logic.git.hooks"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.GitHooksPlugin"
        }
        register("dokka") {
            id = "build-logic.dokka"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.DokkaPlugin"
        }
        register("spotless") {
            id = "build-logic.spotless"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.SpotlessPlugin"
        }
        register("publishing") {
            id = "build-logic.publishing"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.PublishingPlugin"
        }
        register("publishing.jvm") {
            id = "build-logic.publishing.jvm"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.PublishingJvmPlugin"
        }
        register("publishing.mpp") {
            id = "build-logic.publishing.mpp"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.PublishingMppPlugin"
        }
        register("publishing.nexus") {
            id = "build-logic.publishing.nexus"
            implementationClass = "io.github.jeffnyauke.mpesa.buildlogic.PublishingNexusPlugin"
        }
    }
}
