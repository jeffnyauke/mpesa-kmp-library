plugins {
  kotlin("jvm") version "1.6.10"
  application
}

repositories {
  mavenCentral()
  google()
  maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
}

description = "Local consumer sandbox"

application {
  mainClass.set("local.sandbox.MainKt")
}

dependencies {
  implementation("dev.jeffnyauke:mpesa-kmp-library")
  testImplementation("dev.jeffnyauke:test")
}
