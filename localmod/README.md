# localmod/ — accesorio de prueba del loader

`sbwloadertest` es un mod Fabric de prueba que declara el objeto
`META-INF/sbw/registry.json`; Superb Warfare lo descubre y registra sus objetos.
Incluye dos clases anotadas con `@TestLoaderTarget`: una Java y una Kotlin.

El loader vive en
`src/main/kotlin/com/atsuishio/superbwarfare/init/LoaderTest.kt` y se activa con
`TestLoader.register()` desde `Mod.onInitialize()`.

## Estructura

```text
localmod/sbwloadertest/
├── java/com/sbwloadertest/LoaderTargetClass.java
├── kotlin/com/sbwloadertest/LoaderTargetKClass.kt
└── resources/
    ├── fabric.mod.json
    ├── META-INF/sbw/registry.json
    ├── assets/sbwloadertest/...
    └── data/sbwloadertest/recipes/test.json
```

La metadata es `fabric.mod.json`; no se usa `mods.toml` ni un entrypoint Fabric
adicional. El host escanea las clases anotadas después de que Fabric haya
cargado los mods.

## Configuración de desarrollo

El source set se conecta en el `build.gradle.kts` raíz:

```kotlin
val loaderTest: SourceSet by sourceSets.creating {
    java.srcDir("localmod/sbwloadertest/java")
    kotlin.srcDir("localmod/sbwloadertest/kotlin")
    resources.srcDir("localmod/sbwloadertest/resources")
}

loom {
    mods {
        create(project.property("mod_id").toString()) {
            sourceSet(sourceSets.main.get())
        }
        create("sbwloadertest") {
            sourceSet(loaderTest)
        }
    }
}

dependencies {
    add(loaderTest.compileOnlyConfigurationName, sourceSets.main.get().output)
    add("localRuntime", loaderTest.output)
}
```

`localRuntime` pone el output del addon en el classpath real de la instancia de
desarrollo. Las tareas `runClient`, `runServer` y `runData` dependen de compilar
el source set y procesar sus recursos, así que basta con ejecutar:

```text
./gradlew runClient
```

Durante el arranque deberían aparecer:

```text
[sbw-loader] loaded entrypoint com.sbwloadertest.LoaderTargetClass (mod=sbwloadertest)
[sbw-loader] loaded entrypoint com.sbwloadertest.LoaderTargetKClass (mod=sbwloadertest)
[sbw-loader] discovered plugin sbwloadertest: 1 item declaration(s), 2 entrypoint class(es)
[sbw-loader] registered sbwloadertest:test (declared by mod sbwloadertest)
```

El objeto se puede obtener con `/give @s sbwloadertest:test` o mediante la
receta de prueba incluida.

## Contrato de `registry.json`

El namespace se obtiene del id del mod Fabric. El JSON solo contiene rutas
locales:

```json
{
  "FormatVersion": 1,
  "Items": {
    "test": {
      "Rarity": "common"
    }
  }
}
```

Campos admitidos por esta versión:

| Campo | Obligatorio | Valor predeterminado |
| --- | --- | --- |
| `FormatVersion` | sí | `1` |
| `Items` | sí | — |
| `Items.<id>.Rarity` | no | `common` |
| `Items.<id>.MaxStackSize` | no | `64` |
| `Items.<id>.FireResistant` | no | `false` |
| `Items.<id>.Durability` | no | ninguno |

Los ids no pueden contener `:` y deben ser rutas válidas de Minecraft. Un
manifest inválido detiene el arranque; una clase de entrada que no pueda
instanciarse se registra y se omite. Si el mod propietario ya registró el
mismo objeto, el loader no lo reemplaza.

## Clases de entrada

```kotlin
package com.example.plugin

import com.atsuishio.superbwarfare.init.TestLoaderTarget

@TestLoaderTarget
object PluginEntry
```

Las clases se detectan leyendo sus bytecode con ASM en los `rootPaths` de cada
`ModContainer`, sin cargar todas las clases primero. Después se instancia un
Kotlin `object` o, como alternativa, un constructor sin argumentos. Las
instancias válidas quedan en `TestLoader.loadedEntrypoints` agrupadas por mod.

El plugin debe declarar una dependencia Fabric requerida de `superbwarfare`,
porque ahí viven la anotación y el loader. Para código Kotlin también debe
declarar `fabric-language-kotlin`.
