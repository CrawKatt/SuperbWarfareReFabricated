import java.time.Instant

plugins {
    eclipse
    idea
    id("fabric-loom") version "1.13.6"
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
}

fun getGitCommitHash(): String {
    return runCatching {
        providers.exec {
            commandLine("git", "rev-parse", "--short", "HEAD")
        }.standardOutput.asText.get().trim()
    }.getOrElse { "unknown" }
}

version = "${project.property("minecraft_version")}-${project.property("mod_version")}-${getGitCommitHash()}"
group = "com.atsushio.superbwarfare"

base {
    archivesName.set(project.property("mod_id").toString())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
}

loom {
    accessWidenerPath.set(file("src/main/resources/superbwarfare.accesswidener"))

    runs {
        named("client") {
            client()
            configName = "SuperbWarfare Fabric Client"
            runDir = "run"

            vmArg("-XX:+IgnoreUnrecognizedVMOptions")
            vmArg("-XX:+AllowEnhancedClassRedefinition")

            //property("geckolib.disable_examples", "true")
        }

        named("server") {
            server()
            configName = "SuperbWarfare Fabric Server"
            runDir = "run"

            //property("geckolib.disable_examples", "true")
        }

        create("data") {
            inherit(named("client").get())
            name("Data Generation")
            runDir("run")

            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.modid=${project.property("mod_id")}")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/generated/resources")}")
            vmArg("-Dfabric-api.datagen.strict-validation")
        }
    }
}

sourceSets.main {
    resources {
        srcDir("src/generated/resources")
        exclude(".cache/**")
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    flatDir {
        dirs("libs")
    }

    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
    }

    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }

    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/releases/")
    }

    maven {
        name = "Ladysnake"
        url = uri("https://maven.ladysnake.org/releases")
    }

    maven {
        name = "TechReborn"
        url = uri("https://maven.fabricmc.net/")
    }

    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    }

    maven {
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
    }

    maven {
        url = uri("https://maven.shedaniel.me/")
    }

    maven {
        url = uri("https://cursemaven.com")
    }

    maven {
        name = "JitPack"
        url = uri("https://jitpack.io")
    }

    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }

    maven {
        name = "SpongePowered"
        url = uri("https://repo.spongepowered.org/maven/")
    }

    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") {
        name = "Fuzs Mod Resources"
    }

    maven("https://mvn.devos.one/releases/") {
        name = "DevOS"
        content {
            includeGroup("io.github.fabricators_of_create.Porting-Lib")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.20.1:2023.09.03@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("fabric_kotlin_version")}")

    // Built from Sh1roCu/SimpleBedrockModel-Fabric@926992e (the upstream JitPack build is broken).
    include(modImplementation(":simplebedrockmodel-fabric:2.4.6+mc1.20.1")!!)

    include(modImplementation("maven.modrinth:modernkeybinding:1.20.X-1.2.0")!!)
    include(implementation("org.apache.commons:commons-math3:3.6.1")!!)
    include(implementation("com.github.FiguraMC.luaj:luaj-core:3.0.8-figura") {
        exclude("org.apache.commons", "commons-lang3")
    })!!

    include(implementation("com.github.FiguraMC.luaj:luaj-jse:3.0.8-figura") {
        exclude("org.apache.commons", "commons-lang3")
    })!!

    include(implementation("org.apache.bcel:bcel:6.6.1") {
        exclude("org.apache.commons", "commons-lang3")
    })!!

    include(implementation("com.maydaymemory:mae:1.1.4") {
        exclude("com.google.code.findbugs", "jsr305")
        exclude("it.unimi.dsi", "fastutil")
        exclude("org.joml", "joml")
    })!!

    include(modImplementation("fuzs.extensibleenums:extensibleenums-fabric:${project.property("extensibleenums_version")}")!!)

    // CuriosAPI -> Trinkets
    modImplementation("dev.emi:trinkets:3.7.1")

    // Runtime APIs used directly by the mod. Nest them so a release JAR has
    // the same built-in capabilities that Forge provides without requiring
    // users to discover undeclared libraries after a startup crash.
    include(modImplementation("teamreborn:energy:3.0.0")!!)
    include(modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:5.2.0")!!)
    include(modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:5.2.0")!!)

    // JSR-305 (javax.annotation @ParametersAreNonnullByDefault)
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    // GeckoLib
    modImplementation("software.bernie.geckolib:geckolib-fabric-1.20.1:4.4.6")
    implementation("com.eliotlash.mclib:mclib:20")

    // JEI Fabric
    modCompileOnly("mezz.jei:jei-${project.property("minecraft_version")}-fabric-api:${project.property("jei_version")}")
    modRuntimeOnly("mezz.jei:jei-${project.property("minecraft_version")}-fabric:${project.property("jei_version")}")

    // Patchouli Fabric
    modCompileOnly("vazkii.patchouli:Patchouli:1.20.1-84-FABRIC")
    modRuntimeOnly("vazkii.patchouli:Patchouli:1.20.1-84-FABRIC")

    // Cloth Config Fabric
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${project.property("cloth_config_version")}")

    // Optional integrations.
    modCompileOnly("maven.modrinth:nvQzSEkH:oJx1UoWN") // Jade 11.12.3 Fabric
    modCompileOnly("maven.modrinth:1j76DVHU:c6sOTqZb")
    modLocalRuntime("maven.modrinth:1j76DVHU:c6sOTqZb")
    modCompileOnly("maven.modrinth:lhGA9TYQ:WbL7MStR") // Architectury API 9.2.14 Fabric
    modCompileOnly("maven.modrinth:sk9knFPE:MLIu0Tct") // Rhino 2001.2.3-build.10 Fabric
    modCompileOnly("maven.modrinth:umyGl7zF:kPLHkyoJ") // KubeJS 2001.6.5-build.16 Fabric

    modApi("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:8.0.3")

    // Optional client compatibility, compiled against the Fabric artifact.
    modCompileOnly("maven.modrinth:fYYSAh4R:ncXw8tDz") // Real Camera 0.7.4 Fabric
}

tasks.named<ProcessResources>("processResources") {
    val legacyTagPathSegments = mapOf(
        "block" to "blocks",
        "entity_type" to "entity_types",
        "item" to "items"
    )
    val replaceProperties = mapOf(
        "version" to project.version.toString(),
        "minecraft_version" to project.property("minecraft_version"),
        "loader_version" to project.property("loader_version"),
        "fabric_api_version" to project.property("fabric_api_version"),
        "fabric_kotlin_version" to project.property("fabric_kotlin_version"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.property("mod_version"),
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )

    inputs.properties(replaceProperties)

    eachFile {
        val parts = path.split("/")
        if (parts.size > 3 && parts[0] == "data") {
            if (parts[2] == "tags") {
                val replacement = parts.getOrNull(3)?.let(legacyTagPathSegments::get)
                if (replacement != null) {
                    path = (parts.take(3) + replacement + parts.drop(4)).joinToString("/")
                }
            }
        }
    }

    filesMatching(listOf("fabric.mod.json", "pack.mcmeta")) {
        expand(replaceProperties + mapOf("project" to project))
    }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            "Specification-Title" to project.property("mod_id").toString(),
            "Specification-Vendor" to project.property("mod_authors").toString(),
            "Specification-Version" to "1",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version.toString(),
            "Implementation-Vendor" to project.property("mod_authors").toString(),
            "Implementation-Timestamp" to Instant.now().toString()
        )
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

kotlin {
    jvmToolchain(17)
    sourceSets.named("main") {
        kotlin.srcDirs("src/main/kotlin", "src/main/java")
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
