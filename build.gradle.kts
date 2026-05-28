import java.io.ByteArrayOutputStream
import java.time.Instant

plugins {
    eclipse
    idea
    id("fabric-loom") version "1.8.13"
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

            property("mixin.env.remapRefMap", "true")
            property("geckolib.disable_examples", "true")
        }

        named("server") {
            server()
            configName = "SuperbWarfare Fabric Server"
            runDir = "run"

            property("geckolib.disable_examples", "true")
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
    mavenLocal()

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
        name = "SpongePowered"
        url = uri("https://repo.spongepowered.org/maven/")
    }

    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") {
        name = "Fuzs Mod Resources"
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

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    // CuriosAPI -> Trinkets
    modImplementation("dev.emi:trinkets:3.7.1")

    // TechReborn Energy API
    modImplementation("teamreborn:energy:3.0.0")

    // Cardinal Components API
    modImplementation("com.github.OnyxStudios.Cardinal-Components-API:cardinal-components-api:5.2.3")

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

    // Jade Fabric: usa el archivo Fabric correspondiente en CurseMaven
    modImplementation("curse.maven:jade-324717:${project.property("jade_version")}")

    // Dependencias CurseMaven: verifica que estos IDs sean archivos Fabric.
    modImplementation("curse.maven:timeless-and-classics-zero-1028108:6518539")
    modImplementation("curse.maven:create-328085:6255513")
    modImplementation("curse.maven:mmmmmmmmmmmm-225738:6237015")
    modImplementation("curse.maven:selene-499980:6249659")
    modImplementation("curse.maven:better-combat-by-daedelus-639842:5625757")
    modImplementation("curse.maven:playeranimator-658587:4587214")

    modApi("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:8.0.3")

    // Opcionales
    modCompileOnly("curse.maven:real-camera-851574:${project.property("real_camera_id")}")
}

tasks.named<ProcessResources>("processResources") {
    val replaceProperties = mapOf(
        "version" to project.version.toString(),
        "minecraft_version" to project.property("minecraft_version"),
        "loader_version" to project.property("loader_version"),
        "fabric_api_version" to project.property("fabric_api_version"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.property("mod_version"),
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )

    inputs.properties(replaceProperties)

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

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
