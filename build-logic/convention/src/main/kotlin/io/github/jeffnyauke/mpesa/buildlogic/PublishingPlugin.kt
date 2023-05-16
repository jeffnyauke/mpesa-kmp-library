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

import io.github.jeffnyauke.mpesa.buildlogic.util.Git
import io.github.jeffnyauke.mpesa.buildlogic.util.by
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import org.gradle.util.GradleVersion
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class PublishingPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("signing")
                apply("maven-publish")
                apply("build-logic.common")
                apply("build-logic.dokka")
            }
            configurePublishing()
        }
    }

    private fun Project.configurePublishing() {
        val publishing = extensions.getByType<PublishingExtension>()
        val dokkaHtml by tasks.getting(DokkaTask::class)
        val javadocJar by tasks.registering(Jar::class) {
            dependsOn(dokkaHtml)
            archiveClassifier.set("javadoc")
            from((tasks["dokkaHtml"] as DokkaTask).outputDirectory)
        }
        tasks {
            withType<Jar> {
                manifest {
                    attributes += sortedMapOf(
                        "Built-By" to System.getProperty("user.name"),
                        "Build-Jdk" to System.getProperty("java.version"),
                        "Implementation-Version" to project.version,
                        "Created-By" to "${GradleVersion.current()}",
                        "Created-From" to "${Git.headCommitHash}",
                    )
                }
            }
            val cleanMavenLocal by registering {
                group = "build"
                doLast {
                    val groupRepo = file(
                        "${System.getProperty("user.home")}/.m2/repository/${
                            project.group.toString().replace(".", "/")
                        }",
                    )
                    publishing.publications.filterIsInstance<MavenPublication>().forEach {
                        groupRepo.resolve(it.artifactId).deleteRecursively()
                    }
                }
            }
            named("clean") {
                dependsOn(cleanMavenLocal)
            }
            withType<KotlinCompile> {
                onlyIf
            }
        }

        configure<SigningExtension> {
            val signingKey: String? by project
            val signingPassword: String? by project
            if (signingKey != null) {
                useInMemoryPgpKeys(signingKey, signingPassword)
                sign(publishing.publications)
            }
        }

        configure<PublishingExtension> {
            publications {
                val ghOwnerId: String = project.properties["gh.owner.id"]!!.toString()
                val ghOwnerName: String = project.properties["gh.owner.name"]!!.toString()
                val ghOwnerEmail: String = project.properties["gh.owner.email"]!!.toString()
                repositories {
                    maven("https://maven.pkg.github.com/$ghOwnerId/${rootProject.name}") {
                        name = "GitHub"
                        credentials {
                            username = System.getenv("GH_USERNAME")
                            password = System.getenv("GH_PASSWORD")
                        }
                    }
                }
                create<MavenPublication>("Module") {
                    artifact(javadocJar)
                    pom {
                        name by project.name
                        url by "https://github.com/$ghOwnerId/${rootProject.name}"
                        description by project.description

                        licenses {
                            license {
                                name by "The Apache License, Version 2.0"
                                url by "https://www.apache.org/licenses/LICENSE-2.0.txt"
                            }
                        }

                        developers {
                            developer {
                                id by ghOwnerId
                                name by ghOwnerName
                                email by ghOwnerEmail
                            }
                        }

                        scm {
                            connection by "scm:git:git@github.com:$ghOwnerId/${rootProject.name}.git"
                            url by "https://github.com/$ghOwnerId/${rootProject.name}"
                            tag by Git.headCommitHash
                        }
                    }
                }
            }
        }
    }
}
