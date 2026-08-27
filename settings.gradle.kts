pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
    }
    plugins {
        id("fabric-loom") version "1.16.3"
        id("org.jetbrains.kotlin.jvm") version "2.1.20"
        id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
        id("com.google.devtools.ksp") version "2.1.20-2.0.1"
    }
}

include(":ksp")
