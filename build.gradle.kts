plugins {
    idea
    id("java-library")
    id("fabric-loom")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
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

    flatDir {
        dirs("libs")
    }

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

    maven("https://maven.createmod.net") {
        name = "CreateMod"
    }

    maven("https://maven.ryanhcode.dev/releases") {
        name = "RyanHCode"
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
    add("ksp", project(":ksp"))
    compileOnly(project(":ksp"))

    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${project.property("parchment_minecraft_version")}:${project.property("parchment_mappings_version")}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("fabric_kotlin_version")}")
    modImplementation("software.bernie.geckolib:geckolib-fabric-1.21.1:4.7.5")
    modImplementation("dev.emi:trinkets:${project.property("trinkets_version")}")
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:15.0.140")

    include(modImplementation(":simplebedrockmodel-fabric:2.5.7+mc1.21.1")!!)
    include(modImplementation("com.github.Nova-Committee:ModernKeyBinding:17bf4f794ae3ce31aee90e0df67e2757c3533d10")!!)
    modImplementation("net.createmod.ponder:Ponder-Fabric-${project.property("parchment_minecraft_version")}:${project.property("ponder_version")}")

    modCompileOnly("maven.modrinth:sable:e11C0I1A") {
        isTransitive = false
    }
    compileOnly("dev.ryanhcode.sable-companion:sable-companion-common-${project.property("parchment_minecraft_version")}:${project.property("sable_companion_version")}")
    implementation("org.ywzj:rhino:1.8.1-SNAPSHOT")
    include("org.ywzj:rhino:1.8.1-SNAPSHOT")

    modCompileOnly("mezz.jei:jei-1.21.1-fabric-api:${project.property("jei_version")}")
    modRuntimeOnly("mezz.jei:jei-1.21.1-fabric:${project.property("jei_version")}")

    modImplementation("curse.maven:jade-324717:6291517")

    modCompileOnly("vazkii.patchouli:Patchouli:1.21.1-93-FABRIC")
    modRuntimeOnly("vazkii.patchouli:Patchouli:1.21.1-93-FABRIC")

    modCompileOnly("com.github.thedeathlycow:thermoo:v4.8.0")
    modRuntimeOnly("com.github.thedeathlycow:thermoo:v4.8.0")
    modCompileOnly("maven.modrinth:1j76DVHU:sqMweCpe")
    modRuntimeOnly("maven.modrinth:1j76DVHU:sqMweCpe")
    runtimeOnly("org.apache.commons:commons-math3:3.6.1")
    runtimeOnly("com.github.FiguraMC.luaj:luaj-core:3.0.8-figura")
    runtimeOnly("com.github.FiguraMC.luaj:luaj-jse:3.0.8-figura")
    runtimeOnly("org.apache.bcel:bcel:6.6.1")
    modRuntimeOnly("maven.modrinth:touhoulittlemaid-orihime:0.6.2-neo1.5.0")
    runtimeOnly("org.openjdk.nashorn:nashorn-core:15.4")

    include(modImplementation("fuzs.extensibleenums:extensibleenums-fabric:${project.property("extensibleenums_version")}")!!)
    include(modImplementation("org.ladysnake.cardinal-components-api:cardinal-components-base:${property("cardinal_components_version")}")!!)
    include(modImplementation("org.ladysnake.cardinal-components-api:cardinal-components-entity:${property("cardinal_components_version")}")!!)
    include(modApi("teamreborn:energy:4.1.0")!!)
    modImplementation("curse.maven:better-combat-by-daedelus-639842:6532547")
    modImplementation("maven.modrinth:playeranimator:2.0.4+1.21.1-fabric")
    modCompileOnly("curse.maven:real-camera-851574:${project.property("real_camera_id")}")
    modImplementation("curse.maven:net-music-978569:6838604")
    modApi("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:21.1.6")

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    runtimeOnly("com.google.code.findbugs:jsr305:3.0.2")

    implementation("com.maydaymemory:mae:1.1.4") {
        exclude("com.google.code.gson", "gson")
        exclude("com.google.code.findbugs", "jsr305")
        exclude("it.unimi.dsi", "fastutil")
        exclude("org.joml", "joml")
    }
    include("com.maydaymemory:mae:1.1.4")
}

fabricApi {
    configureDataGeneration {
        outputDirectory = file("src/generated/resources")
    }
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.WARN
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

kotlin {
    jvmToolchain(21)
    sourceSets {
        named("main") {
            kotlin.srcDirs("src/main/kotlin", "src/main/java")
        }
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
