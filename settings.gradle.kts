pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
    }
    plugins {
        id("fabric-loom") version "1.13.6"
        id("org.jetbrains.kotlin.jvm") version "2.2.0"
    }
}