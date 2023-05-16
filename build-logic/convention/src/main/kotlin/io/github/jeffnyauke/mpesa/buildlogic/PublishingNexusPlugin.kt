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

import io.github.gradlenexus.publishplugin.NexusPublishExtension
import io.github.jeffnyauke.mpesa.buildlogic.util.by
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class PublishingNexusPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.github.gradle-nexus.publish-plugin")
                apply("build-logic.common")
            }

            configure<NexusPublishExtension> {
                repositories {
                    sonatype {
                        nexusUrl by uri("https://s01.oss.sonatype.org/service/local/")
                        snapshotRepositoryUrl by uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    }
                }
            }
        }
    }
}
