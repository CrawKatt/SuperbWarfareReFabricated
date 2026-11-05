# Auditoría de paridad NeoForge → Fabric 1.20.1

Fecha: 2026-08-26  
Rama auditada: `merge/0.8.9.1-fabric`  
Fabric auditado antes de sanear el historial: `8d3a17ebe`  
HEAD equivalente después de sanearlo: `554eba698`  
Oráculo NeoForge: tag `1.20-0.8.9.1`, commit `74b64f8ea67dbb15532292466d7ef955572c2e8a`

## Alcance y criterio

La contraparte NeoForge indicada arriba se trató como especificación del comportamiento observable. Se compararon registro e inicialización, eventos y fases, estado y persistencia, red, recursos/datagen, recetas, render, shaders, menús e integraciones opcionales.

Los MD que ya existían en la rama no se usaron ni se leyeron como fuente, porque corresponden a 1.21.1. En particular se excluyeron `parity-differences-0.8.9.1.md`, `parity-resolution-plan-0.8.9.1.md` y `port-plan-0.8.9.1.md`.

Cold Sweat → Thermoo queda fuera de los defectos por indicación del mantenedor: Cold Sweat no existe para este objetivo Fabric. El `ThermalShaderHandler` desregistrado también se considera una decisión intencional temporal para impedir el overlay doble descrito por el mantenedor.

## Veredicto

La rama **todavía no tiene paridad 1:1**. Se encontraron:

- 2 bloqueantes confirmados.
- 4 diferencias de severidad alta.
- 6 diferencias de severidad media.
- 2 diferencias de severidad baja.
- 4 diferencias/adaptaciones explícitas que deben documentarse o decidirse.
- 3 riesgos de paridad que necesitan pruebas diferenciales antes de poder cerrarse.

El bloqueo principal es objetivo: `runServer` no alcanza un servidor funcional. `build` termina correctamente, pero eso no detecta el fallo de carga de clases que ocurre durante el arranque dedicado.

## Resumen de diferencias confirmadas

| ID | Severidad | Área | Diferencia observable |
|---|---|---|---|
| D-01 | Bloqueante | Servidor dedicado | El servidor se cae al registrar `HANDSOME_GOGGLES` porque una clase común inicializa una constante desde un renderer cliente. |
| D-02 | Bloqueante | Recetas/tags | 102 recetas referencian al menos un tag `forge:` que se confirmó ausente en Fabric. Muchas recetas e ingredientes de ensamblaje quedan vacíos. |
| D-03 | Alta | Disparo/render tick | El bucle de disparo se ejecuta dos veces por frame y, a ≤20 FPS, además una vez por tick; NeoForge lo ejecuta una sola vez por la ruta correspondiente. |
| D-04 | Alta | Mapa táctico | Se usan eventos de inicio/parada del cliente en vez de carga/descarga de mundo; puede sobrevivir estado de otro mundo o dimensión. |
| D-05 | Alta | Ponder | El plugin existe, pero nunca se añade a `PonderIndex`; sus diez escenas no se registran. |
| D-06 | Alta | Spawn/red | Los datos adicionales de vehículos, misiles y proyectiles llegan en otro paquete hasta un tick después del spawn, no atómicamente con él. |
| D-07 | Media | FOV | Fabric captura el FOV antes de aplicar las modificaciones; NeoForge lo captura al final por prioridad `LOWEST`. |
| D-08 | Media | HUD | La mira vanilla no se cancela en asientos `banHand` sin arma. NeoForge sí la cancela. |
| D-09 | Media | Partículas | El shader de partículas suaves está implementado, pero su callback de registro nunca se instala. |
| D-10 | Media | Menús | La apertura de menús de energía/FuMO se emula un tick tarde y puede omitirse si el menú se cierra antes. |
| D-11 | Media | Decoración de ítems | `VehicleKeyItemDecorator` no tiene ningún llamador; no aparece el indicador de color del equipo. |
| D-12 | Media | Alcance opcional | En el entorno con `reach-entity-attributes`, sus mixins ganan algunos puntos de inyección y omiten validaciones de alcance propias de SBW. |
| D-13 | Baja | Trinkets | `TacticalTerminalItem` implementa `Trinket`, pero no se registra mediante `TrinketsApi.registerTrinket`. |
| D-14 | Baja | Registro datapack | Fabric eliminó el registro dinámico y sincronizado `superbwarfare:sbw/perks` que NeoForge expone. |

## Detalle y evidencia

### D-01 — crash en servidor dedicado

En `HandsomeGogglesItem.kt:130-135`, el `companion object` común hace:

```kotlin
val TEXTURE = HandsomeGogglesRenderer.TEXTURE
```

Eso fuerza la carga de `HandsomeGogglesRenderer` durante la inicialización de `ModItems`, incluso en servidor. La ejecución de `./gradlew runServer --no-daemon --console=plain` falló con:

```text
RuntimeException: Cannot load class net.minecraft.client.model.HumanoidModel in environment SERVER
at HandsomeGogglesItem.<clinit>(HandsomeGogglesItem.kt:132)
```

NeoForge separa el lado cliente mediante su distribución/event bus y no carga ese renderer en dedicado. Es bloqueante para cualquier instalación servidor.

### D-02 — recetas generadas con namespace Forge obsoleto

El port ya define los tags comunes con namespace `c:` en `ModTags.kt:14-23` y `ModRecipeProvider.kt:59-67`. Sin embargo, los recursos generados comprometidos no fueron regenerados después de esa conversión:

- Hay 493 recetas generadas, igual que en el oráculo.
- 104 archivos de receta todavía contienen alguna referencia `forge:`.
- 102 de ellos usan tags que el runtime confirmó ausentes, entre ellos `forge:ingots/steel`, `forge:ingots/lead`, `forge:plates/steel`, `forge:plates/plastic`, `forge:storage_blocks/steel` y varios `forge:dusts/*`.
- El log del cliente informa que los agregados `forge:plates`, `forge:ingots`, `forge:raw_materials`, `forge:dusts` y `forge:ores` no pueden resolverse por faltar sus tags hijos.

Esto afecta tanto recetas vanilla (`Ingredient` con tag) como las cadenas de texto que consumen `VehicleAssemblingIngredient` y `ResearchingRecipe`.

Las carpetas fuente `tags/item`, `tags/block` y `tags/entity_type` **no son un defecto separado**: `build.gradle.kts:295-326` las convierte a `tags/items`, `tags/blocks` y `tags/entity_types`, y el JAR producido contiene las rutas correctas para 1.20.1.

Durante la prueba de cliente también apareció `Loaded 11 recipes`, pero esa cifra no se usa como prueba de este hallazgo porque el mundo abierto era un mapa 1.21+ con un datapack `pack_format: 48`. La ausencia de los tags sí está confirmada independientemente por recursos y log.

### D-03 — cadencia de disparo distinta

NeoForge procesa `handleWeaponFire(RenderTickEvent)` solo en fase `START` y solo si `fps > 20`; a `fps <= 20` lo procesa una vez desde el tick cliente `END`. El balanceo respiratorio sí recibe ambas fases de render.

`GameRendererMixin.java:38-53` llama en `HEAD` y `TAIL` a las tres rutinas: disparo de arma, disparo de vehículo y respiración. No hay guard de FPS en esas llamadas. `ClientEventHandler.kt:568-574` vuelve a ejecutar los dos disparos a ≤20 FPS.

Resultado:

- A más de 20 FPS: dos evaluaciones de disparo por frame en vez de una.
- A 20 FPS o menos: dos evaluaciones por frame más una por tick en vez de una por tick.

Puede cambiar temporizadores, frecuencia de paquetes C2S, consumo de munición, retroceso y sonido.

### D-04 — lifecycle incorrecto del mapa táctico

NeoForge llama `initForDimension` en `LevelEvent.Load` y `clear` en `LevelEvent.Unload` para cada `ClientLevel`.

`TacticalMapChunkListener.kt:27-36` usa `CLIENT_STARTED` y `CLIENT_STOPPING`, que representan la vida de la aplicación. Al iniciar el cliente normalmente aún no existe `client.level`, y al abandonar/cambiar mundo no se limpia el cache. `ensureInit` detecta cambios, pero `initForDimension` (`TacticalMapCache.kt:160-167`) no vacía imágenes, texturas, chunks dibujados ni colas antes de cargar la nueva dimensión.

Puede mezclar tiles y estado GPU entre mundos, servidores o dimensiones hasta reiniciar Minecraft.

### D-05 — escenas Ponder inaccesibles

NeoForge ejecuta `PonderIndex.addPlugin(SBWPonderPlugin)`. Fabric conserva `SBWPonderPlugin.kt`, que registra diez escenas, y empaqueta Ponder, pero no existe ninguna llamada a `PonderIndex.addPlugin` ni un mecanismo de autodetección equivalente en esta versión de Ponder.

El mensaje genérico de registro de Ponder visto al arrancar no demuestra registro de SBW; otros mods también aportan plugins. Las diez escenas de SBW quedan fuera del índice.

### D-06 — datos adicionales de spawn tardíos

En NeoForge, `FastThrowableProjectile`, `MissileProjectile` y `VehicleEntity` implementan `IEntityAdditionalSpawnData`, por lo que el estado adicional acompaña al spawn.

Fabric encola `EntitySpawnDataMessage` en `EntityTrackingEvents.START_TRACKING` y lo envía al final del tick de servidor (`NetworkRegistry.java:106-124`). Existe una ventana de hasta un tick con valores por defecto y un primer render potencialmente incorrecto. Si la entidad deja de existir o el tracking cambia antes del drenaje, el mensaje se descarta.

### D-07 — orden de captura del FOV invertido

NeoForge declara `captureFov` con prioridad `LOWEST`, después de `onFovUpdate`. `GameRendererMixin.java:55-66` crea un contexto y llama primero `captureFov`, luego `onFovUpdate`.

El FOV retornado se modifica, pero el valor global usado por otras animaciones conserva el FOV vanilla previo a zoom.

### D-08 — mira visible en asientos `banHand`

NeoForge cancela la mira si el vehículo devuelve `banHand(player)`, independientemente de que el asiento tenga arma. `ClientEventHandler.shouldCancelCrossHair` (`ClientEventHandler.kt:2932-2970`) solo cubre armas de mano, asientos con arma, indicador de artillería y monitor. La hotbar sí conserva su comprobación `banHand`.

### D-09 — shader de partículas suaves sin registrar

NeoForge conecta `ModParticleRenderTypes.onRegisterShaders` al evento de shaders. Fabric implementa `ModParticleRenderTypes.register()` con `CoreShaderRegistrationCallback`, pero no hay ninguna llamada a `register()`.

`CustomFlareParticle` usa el render type afectado; al quedar `softParticleShader == null`, se toma el shader vanilla de respaldo y se pierde la transparencia suave propia.

### D-10 — apertura de menú diferida

NeoForge usa `PlayerContainerEvent.Open` para registrar observadores de energía, enviar el estado completo y notificar la apertura de radar en el momento de abrir.

`ChargingStationMenu.kt:49-54` y `FuMO25Menu.kt:75-80` usan `queueServerWork(1)`. El primer estado llega un tick tarde; si el contenedor deja de ser el actual antes de la tarea, `onOpened` no ocurre.

### D-11 — decoración de llave de vehículo omitida

NeoForge registra tres decoradores. `GuiGraphicsMixin.java:23-30` solo invoca `ContainerItemDecorator` y `LuckyContainerItemDecorator`. `VehicleKeyItemDecorator.kt` existe, pero no tiene llamadores, así que no se dibuja el icono teñido con el color del equipo del vehículo enlazado.

### D-12 — conflicto de mixins de alcance

Los logs de cliente/servidor muestran que `reach-entity-attributes` gana sobre `ServerGamePacketListenerReachMixin` y sobre la comprobación de rotura de bloque. La rama mantiene atributos propios `superbwarfare:block_reach` y `superbwarfare:entity_reach`; objetos como la palanca/crowbar y `LungeMine` modifican esos atributos.

En una instalación que incluya `reach-entity-attributes`, parte de la validación servidor puede ignorar los atributos de SBW o usar bases distintas. El comportamiento base sin ese mod debe comprobarse aparte, porque `reach-entity-attributes` está añadido como `modRuntimeOnly` de desarrollo y no está anidado ni declarado como dependencia de release.

### D-13 — terminal táctico no registrado como Trinket

`TacticalTerminalItem` implementa `Trinket` y define `canEquip` para impedir duplicados. `ModItems.init()` registra paracaídas, dog tag, IFF y gafas térmicas, pero omite `TACTICAL_TERMINAL`. Trinkets puede tratarlo con su comportamiento predeterminado, pero no invoca la regla personalizada 1:1.

### D-14 — registro dinámico de perks ausente

NeoForge crea y sincroniza el registro datapack `superbwarfare:sbw/perks` con `PerkDescriptor`. Fabric eliminó `ModDatapackRegistries` y conserva la carga manual de JSON de perks, por lo que el gameplay base no mostró una pérdida directa. Sí cambia la API observable para datapacks/mods externos y desaparecen las semánticas de registro/default key/tags del oráculo.

## Diferencias intencionales o de plataforma

### A-01 — Cold Sweat → Thermoo

Aceptada por el mantenedor. Cold Sweat no forma parte del ecosistema objetivo Fabric; los comentarios/desactivación de esa integración no constituyen un defecto de paridad de esta rama. No requiere acción.

### A-02 — `ThermalShaderHandler.register()` desactivado

Aceptada temporalmente por el mantenedor para evitar un overlay extraño que aparenta aplicarse dos veces. La diferencia observable frente a NeoForge es que Fabric solo deja activo el efecto cargado por `GameRenderer` (`night_vision.json`) y no ejecuta la cadena `thermal.json` de `ThermalShaderHandler`.

No debe resolverse descomentando la línea sin más. `turnOnThermalImaging` solicita simultáneamente ambas cadenas; al registrar el handler, Fabric puede procesarlas como dos pasadas fullscreen en etapas distintas. Además, el port quitó la propagación de stencil al buffer térmico que sí tenía NeoForge. El plan conserva el handler desactivado hasta garantizar una sola composición visual y restaurar profundidad/stencil.

### A-03 — visibilidad de paracaídas Curios/Trinkets

NeoForge consulta `SlotContext.visible()`. Trinkets 3.7.1 no ofrece una propiedad/toggle equivalente y renderiza el trinket equipado. Por tanto, Fabric no puede ocultar el paracaídas con la misma UI. Es una limitación observable de la sustitución Curios → Trinkets; para paridad estricta haría falta estado/UI/sync propios.

### A-04 — contrato de distribución

NeoForge usa `jarJar` para varias bibliotecas; Fabric anida Ponder, SimpleBedrockModel, Rhino, Cardinal Components, Energy y utilidades, pero deja como dependencias externas Fabric API, Fabric Kotlin, GeckoLib, Trinkets, ExtensibleEnums y Forge Config API Port. No cambia necesariamente el gameplay una vez instalado todo, pero no es el mismo contrato de “un solo JAR”.

## Riesgos aún no demostrados como defecto

### R-01 — Rhino no equivalente

NeoForge usa un fork privado/relocalizado `org.ywzj:rhino:1.8.1-SNAPSHOT` con paquetes `org.mozillaa.javascript`. Fabric usa Rhino estándar 1.7.15 y cambió los imports a `org.mozilla.javascript`. Puede variar sintaxis, coerción, interoperabilidad y errores de scripts. Sin corpus diferencial no puede afirmarse paridad.

### R-02 — SimpleBedrockModel 2.4.6 frente a 2.5.1

NeoForge usa 2.5.1; Fabric anida una compilación local 2.4.6 del commit `926992e`. Animación, huesos, LOD, hitboxes o render pueden divergir. No se observó una escena concreta suficiente para elevarlo a defecto confirmado.

### R-03 — guard de versión TACZ

NeoForge solo activa compatibilidad con TACZ `>=1.1.4`. Fabric devuelve únicamente `isModLoaded("tacz")`. El artefacto Fabric probado publica una versión con esquema `0.7.0-forge1.1.8-hotfix`, por lo que copiar literalmente la comparación Forge tampoco sería correcto. Falta declarar y comprobar el rango Fabric realmente soportado.

## Paridad comprobada sin diferencia relevante

- Worldgen: seis menas y las apariciones de Senpai/Steel Coil mantienen biomas, pesos y conteos del oráculo.
- Loot: los conjuntos de tablas objetivo para common, rare, epic y ancient-city CPU coinciden; se conserva la gamerule.
- Trades: se conservaron 122 entradas de armory y dos del wandering trader.
- Red: están registrados los mismos 27 mensajes S2C y 50 C2S de gameplay; Fabric añade handshake/versionado y el paquete auxiliar de spawn.
- Estado del jugador: munición y térmica persisten, se copian en respawn y se sincronizan en login/respawn/cambio de dimensión mediante CCA.
- Fuego de fósforo: componente y persistencia están presentes.
- Serialización: están los cinco serializadores previstos, incluido `VEC3`.
- Config: 129 definiciones en ambos lados, con estructura/defaults equivalentes.
- Overlays: el orden relativo de la cadena de overlays coincide con NeoForge.
- Tick de jugador: el mixin `TAIL` reproduce la fase `END` de Forge.
- Yunque: se mantienen coste 10 y consumo de un material.
- Idiomas: no faltan claves que existan en NeoForge; los cambios detectados son traducciones adicionales o redacción.
- Compatibilidad JSON: las condiciones inspeccionadas de Create, CBC y Patchouli se convirtieron a condiciones Fabric equivalentes.
- KubeJS, Jade y JEI conservan sus mecanismos de registro.
- Las rutas fuente singulares de tags se transforman correctamente a rutas 1.20.1 en el JAR final.

## Validación ejecutada

| Prueba | Resultado |
|---|---|
| `./gradlew build --no-daemon --console=plain` | `BUILD SUCCESSFUL`; no existen tests automatizados (`NO-SOURCE`). |
| Inspección del JAR remapeado | Contiene 512 recetas SBW y las rutas de tags plurales correctas. |
| `./gradlew runServer --no-daemon --console=plain` | Fallo confirmado D-01 antes de completar el arranque. Gradle devuelve éxito pese al fallo del proceso Minecraft, por lo que hay que inspeccionar el log. |
| `./gradlew runClient --no-daemon --console=plain` | Cliente e integrado alcanzaron un mundo; se confirmaron tags Forge faltantes y conflictos de alcance. Se cerró manualmente después del smoke test. |

No se pudo ejecutar una matriz completa de gameplay porque el servidor dedicado está bloqueado por D-01 y no hay tests automatizados. Los riesgos R-01/R-03 requieren mods/matrices externas específicas; R-02 necesita escenas visuales comparables.
