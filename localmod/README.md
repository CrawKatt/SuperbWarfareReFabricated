# localmod/ — accesorio de prueba del loader

`sbwloadertest` es un mod Fabric de prueba que declara objetos, armas y accesorios
en `META-INF/sbw/registry.json`; Superb Warfare los descubre y registra.
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
[sbw-loader] discovered plugin sbwloadertest: 1 item(s), 1 gun(s), 1 attachment(s), 2 entrypoint class(es)
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
    "test": { "ItemTabs": ["superbwarfare:item"] }
  },
  "Guns": {
    "ak_74": { "ItemTabs": ["superbwarfare:guns"] }
  },
  "Attachments": {
    "charm_loader_senpai": {
      "Rarity": "superb",
      "ItemTabs": ["superbwarfare:attachment"]
    }
  }
}
```

Campos admitidos por esta versión:

| Campo | Obligatorio | Valor predeterminado |
| --- | --- | --- |
| `FormatVersion` | sí | `1` |
| `Items` / `Guns` / `Attachments` | no | `{}` |
| `<grupo>.<id>.Rarity` | no | `common` |
| `<grupo>.<id>.MaxStackSize` | no | `64` |
| `<grupo>.<id>.FireResistant` | no | `false` |
| `<grupo>.<id>.Durability` | no | ninguno |
| `<grupo>.<id>.ItemTabs` | no | `[]` |

Los tres grupos son opcionales y crean `Item`, `GeoGunItem` y `BasicAttachmentItem`,
respectivamente. Las rarezas admitidas son `common`, `uncommon`, `rare`, `epic`,
`legendary`, `superb`, `virtual` y `beast`.

`ItemTabs` acepta ids completos de pestañas existentes, por ejemplo
`superbwarfare:attachment`, `superbwarfare:guns`, `superbwarfare:item` o
`minecraft:ingredients`. Los ids repetidos se añaden una sola vez; omitir la lista
no impide registrar el objeto, pero no lo añade a ninguna pestaña. Solo se añaden
a las pestañas las declaraciones que se hayan registrado correctamente.

Los ids no pueden contener `:` y deben ser rutas válidas de Minecraft. No se
puede repetir el mismo id entre grupos. Se conserva el orden de declaración y se
ignoran los campos desconocidos. El manifiesto solo se lee del mod, nunca de un
datapack. Un manifest inválido detiene el arranque; una clase de entrada que no pueda
instanciarse se registra y se omite. Si el mod propietario ya registró el
mismo objeto, el loader no lo reemplaza.

## Accesorios de otros mods

El id de `Attachments` también identifica su `AttachmentDefinition`. El accesorio
incluido necesita estos recursos, además de sus nombres traducidos:

- `data/sbwloadertest/sbw/attachments/charm_loader_senpai.json`.
- `assets/sbwloadertest/models/item/charm_loader_senpai.json` y su icono.
- El modelo Bedrock y la textura referenciados por la definición.
- Los tags de slots o una declaración explícita en `AvailableAttachments` del arma.

El **Loader Test Senpai Charm** usa el namespace `sbwloadertest`, rareza `superb`
y `Level: 4`. `Slot`, `Level` e `ItemTabs` son independientes.

El tag `data/superbwarfare/tags/items/attachment/charm/superb.json` añade
`sbwloadertest:charm_loader_senpai` con `replace: false`. La cadena de tags del host
lo incluye en `#superbwarfare:attachment/charm`, por lo que se puede instalar en el
AK-47 sin modificar sus datos. Un tag propio no basta si el arma no lo referencia.

Para comprobarlo en el juego, obtén el accesorio desde su pestaña creativa o con
`/give @s sbwloadertest:charm_loader_senpai`, abre la modificación del AK-47 y
comprueba las opciones, instalación, retirada y renderizado en su hueso `charm_pos`.

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
