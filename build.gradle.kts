plugins {
    idea
    id("java-library")
    id("fabric-loom") version "1.13.6"
    `maven-publish`
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
}

version = "${project.property("minecraft_version")}-${project.property("mod_version")}"
group = "com.atsuishio.superbwarfare"

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://maven.fabricmc.net/")

    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
    }

    maven("https://maven.terraformersmc.com/") {
        name = "Terraformers"
    }

    maven("https://maven.shedaniel.me") {
        name = "Shedaniel"
    }

    maven("https://maven.ladysnake.org/releases") {
        name = "Ladysnake"
    }

    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") {
        name = "Fuzs Mod Resources"
    }

    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
    }

    maven("https://jitpack.io") {
        name = "JitPack"
    }

    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") {
        name = "GeckoLib"
        content {
            includeGroupByRegex("software\\.bernie.*")
            includeGroup("com.eliotlash.mclib")
        }
    }

    maven("https://maven.blamejared.com/") {
        name = "BlameJared"
        content {
            includeGroup("mezz.jei")
            includeGroup("vazkii.patchouli")
        }
    }

    maven("https://cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
}

base {
    archivesName.set(project.property("mod_id") as String)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
}

loom {
    accessWidenerPath = file("src/main/resources/superbwarfare.accesswidener")
    mods {
        create(project.property("mod_id") as String) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${project.property("parchment_minecraft_version")}:${project.property("parchment_mappings_version")}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    modImplementation("software.bernie.geckolib:geckolib-fabric-1.21.1:4.7.5")
    modImplementation("dev.emi:trinkets:${project.property("trinkets_version")}")
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:15.0.140")

    modCompileOnly("mezz.jei:jei-1.21.1-fabric-api:${project.property("jei_version")}")
    modRuntimeOnly("mezz.jei:jei-1.21.1-fabric:${project.property("jei_version")}")

    modImplementation("curse.maven:jade-324717:6291517")

    modCompileOnly("vazkii.patchouli:Patchouli:1.21.1-93-FABRIC")
    modRuntimeOnly("vazkii.patchouli:Patchouli:1.21.1-93-FABRIC")

    modCompileOnly("com.github.thedeathlycow:thermoo:v4.8.0")
    modRuntimeOnly("com.github.thedeathlycow:thermoo:v4.8.0")

    include(modImplementation("fuzs.extensibleenums:extensibleenums-fabric:${project.property("extensibleenums_version")}")!!)
    include(modApi("teamreborn:energy:4.1.0")!!)
    modImplementation("curse.maven:better-combat-by-daedelus-639842:6532547")
    modImplementation("maven.modrinth:playeranimator:2.0.4+1.21.1-fabric")
    modCompileOnly("curse.maven:real-camera-851574:${project.property("real_camera_id")}")
    modImplementation("curse.maven:net-music-978569:6838604")
    modApi("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:21.1.6")

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

tasks.withType<ProcessResources> {
    val properties = mapOf(
        "version" to project.version,
        "minecraft_version" to project.property("minecraft_version"),
        "loader_version" to project.property("loader_version"),
        "fabric_version" to project.property("fabric_version"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.property("mod_version"),
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )

    inputs.properties(properties)

    filesMatching("fabric.mod.json") {
        expand(properties)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/repo")
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
