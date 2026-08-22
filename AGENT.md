# SuperbWarfare Fabric Port 项目指南

## 项目定位

本仓库是 Superb Warfare `0.8.9.1` 的 Minecraft Fabric 1.21.1 移植版。NeoForge
版本是行为规范，但这里的实现必须使用 Fabric 生命周期和 API，不能把 NeoForge 的注册器、事件总线或
Capability 代码直接复制进来。

- Mod ID：`superbwarfare`
- 包名：`com.atsuishio.superbwarfare`
- Java：21
- Kotlin：2.1.20
- 构建：Fabric Loom 1.13.6
- 映射：官方 Mojang mappings + Parchment `2024.11.17`
- 通用入口：`com.atsuishio.superbwarfare.Mod`（`ModInitializer`）
- 客户端入口：`com.atsuishio.superbwarfare.client.SuperbWarfareFabricClient`
- Fabric metadata：`src/main/resources/fabric.mod.json`

## 移植原则

1. NeoForge 0.8.9.1 决定可观察行为，Fabric 代码只替换平台接缝。
2. 保持注册 ID、NBT、Data Component、codec、网络方向、执行侧和渲染阶段不变。
3. 优先复用仓库中的 Fabric 适配器，不为单个调用重新实现一套抽象。
4. Java 文件继续使用 Java，Kotlin 文件继续使用 Kotlin。
5. 不得为了通过编译删除机制；没有平台等价物时要明确记录差异。

## Fabric 生命周期

`Mod.onInitialize()` 负责通用初始化：

- 通过 `Registry.register` 初始化物品、方块、实体、声音、粒子和配方类型。
- 注册 Fabric payload、服务端接收器、命令、reload listener 和事件 callback。
- 初始化 Cardinal Components、能量/库存 lookup、世界数据和兼容层。
- 注册 client-safe 的内置 Bedrock 模型资源；其它客户端注册只在客户端入口执行。

`SuperbWarfareFabricClient.onInitializeClient()` 负责：

- 实体、物品、护甲、粒子和 HUD renderer。
- key mapping、屏幕、模型 loader、shader 和客户端 reload listener。
- 客户端 payload receiver、客户端 tick 和客户端实体事件。

不要添加 `@Mod`、`IEventBus`、`NeoForge.EVENT_BUS`、`@EventBusSubscriber` 或
`@SubscribeEvent`。生命周期入口只能由 `fabric.mod.json` 声明。

## 注册与初始化

`init/` 下的 Kotlin `object` 保存注册对象。常见形式是：

```kotlin
val EXAMPLE: Item = Registry.register(
    BuiltInRegistries.ITEM,
    Mod.loc("example"),
    ExampleItem()
)
```

不要在 Fabric 代码中引入 `DeferredRegister`、`DeferredHolder`、
`RegisterEvent` 或 `NeoForgeRegistries`。需要确保某个 `object` 被初始化时，在其 `init()` 中显式触发。

## 配置

配置通过 Forge Config API Port 加载。因此以下 NeoForge 类型是有意保留的平台桥接，不应仅因为包名而删除：

- `net.neoforged.neoforge.common.ModConfigSpec`
- `net.neoforged.fml.config.ModConfig`
- `fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry`

客户端、通用和服务端 spec 分别由 `CLIENT_CONFIG`、`COMMON_CONFIG`、`SERVER_CONFIG`
提供，并在通用入口注册一次。客户端入口不得再次注册配置。

## 事件系统

优先使用 Fabric API callback：

- tick：`ServerTickEvents`、`ClientTickEvents`
- 实体生命周期：`ServerEntityEvents`、`ClientEntityEvents`
- 玩家交互：`UseEntityCallback`、`AttackEntityCallback`
- 命令：`CommandRegistrationCallback`
- reload：`ResourceManagerHelper`、`ServerLifecycleEvents`
- 渲染：`WorldRenderEvents` 和项目已有 renderer callback

Fabric 没有相同 hook 时，使用 `mixins/` 中的最小注入点，再委托给 `event/` 的业务 handler。
不要把 gameplay 逻辑复制进 mixin。

项目自定义事件位于 `api/event/`，由 `SuperbWarfareEvents` 等 Fabric `Event` 发布；它们不是
NeoForge bus event。

## 网络

网络注册集中在 `network/NetworkRegistry.kt`：

- payload 类型使用 `PayloadTypeRegistry` 注册。
- 客户端到服务端消息位于 `network/message/send/`。
- 服务端到客户端消息位于 `network/message/receive/`。
- 使用 `ClientPlayNetworking`、`ServerPlayNetworking` 或项目的
  `sendPacketToServer` / `sendPacketTo` helper。

不要使用 `PacketDistributor`、`RegisterPayloadHandlersEvent` 或 NeoForge `IPayloadContext`。
payload handler 必须在正确线程执行，并保持原版 wire shape。

## 组件、能量、库存与饰品

- 玩家和实体持久数据：Cardinal Components API，入口是 `ModComponents`。
- 物品、方块和实体能量：Team Reborn Energy API，通过 `ModCapabilities` 的 Fabric lookup 访问。
- 实体库存：项目的 Fabric inventory adapter，同样通过 `ModCapabilities` / `InventoryTool` 访问。
- 饰品：Trinkets；物品实现位于 `item/trinket/`，slot 数据位于
  `data/trinkets/slots`、`data/trinkets/entities` 和 `data/trinkets/tags/item`。

不要使用 NeoForge Attachments、`Capabilities`、`IItemHandler`、Curios 或其 JSON slot。
新增可装备物品时必须同时添加 Trinkets slot、entity assignment 和 item tag。

## 数据驱动与脚本

枪械、载具、Perk 和皮肤由 JSON/resource reload 驱动：

```text
JSON → DataLoader / CustomData → DefaultGunData / VehicleData / PerkDescriptor
ItemStack components/NBT ↔ GunData.from(stack) → PMC 属性计算链
```

PMC 顺序必须保持：

1. ItemStack 数据覆写
2. `GunItem` 修改
3. 开火模式修改
4. `AmmoConsumer` 修改
5. 已装备 Perk 修改
6. `GunProp` 上下限

内置 Rhino 脚本位于 `script/`，资源脚本位于 `assets/superbwarfare/scripts` 和
`data/superbwarfare/scripts`。它是本模组的数据脚本系统，不是完整的 KubeJS 替代品。

Fabric 1.21.1 没有可用的 KubeJS 版本，因此本 port 不注册 KubeJS plugin/event。不要恢复
`FORGE_BUS` 或提交一个只实现部分事件的伪兼容层。

## 实体与渲染

主要实体层级：

```text
Entity
├─ VehicleEntity
│  ├─ AutoAimableEntity
│  │  └─ ArtilleryEntity
│  └─ 遗留 GeoVehicleEntity（计划在 0.8.10 移除）
├─ ProjectileEntity / MissileProjectile / FastThrowableProjectile
└─ TargetEntity / DPSGeneratorEntity / SenpaiEntity
```

`VehicleEntity` 负责座位、武器、库存、能量、OBB、残骸、引擎、模型条目和同步。
高速弹体由 `ProjectileChunkSavedData` 手动 tick；不要恢复 NeoForge 的
`onAddedToLevel`/`onRemovedFromLevel` hook。

新载具优先使用 Simple Bedrock Model (`models/bedrock`) 和 `BasicVehicleRenderer`。
仍使用 GeckoLib 的枪械通过 `GunGeoItem.createGeoRenderer` 接入 Fabric。普通自定义物品 renderer
由 `BuiltinItemRendererRegistry` 在 `ClientRenderHandler` 注册。

客户端类不能从通用初始化路径直接加载。新增 renderer、shader、`Minecraft` 引用或
`@Environment(EnvType.CLIENT)` 类型时，先确认 dedicated server 不会解析它。

## 资源规则

- Fabric access 修改写入 `superbwarfare.accesswidener`，不要添加 access transformer。
- mixin 只由 `superbwarfare.mixins.json` 声明。
- 可选资源使用 `fabric:load_conditions`，不要使用 `neoforge:conditions`。
- Patchouli 等可选模组用 `fabric:all_mods_loaded`。
- convention tag 使用 `c:` namespace。
- 客户端 Bedrock 模型路径为 `models/bedrock`，动画路径为 `animations/bedrock`。

## 目录速查

| 路径 | 用途 |
|---|---|
| `init/` | 直接注册和初始化 |
| `data/gun/` | GunData、GunProp、Ammo、PMC 数据 |
| `data/vehicle/` | 载具属性和数据模型 |
| `entity/vehicle/` | 载具实体及工具扩展 |
| `entity/projectile/` | 弹体、导弹和爆炸行为 |
| `item/gun/` | 枪械基类和具体武器 |
| `item/trinket/` | Trinkets 饰品 |
| `event/` | Fabric callback 的业务处理器 |
| `network/` | payload 注册、发送和接收 |
| `client/` | renderer、GUI、overlay、shader、模型和粒子 |
| `capability/` | CCA 数据与 Fabric 存储适配 |
| `resource/` | 枪械、载具和模型 reload |
| `datagen/` | recipe、loot、tag、advancement、model 生成 |
| `mixins/` | Fabric/vanilla 缺失 hook 的最小桥接 |

仓库历史原因导致部分 `.kt` 位于 `src/main/java`；除非任务明确要求整理目录，不要顺便移动。

## 常见修改流程

- 新枪械：注册 item → gun/resource JSON → renderer/model → tag/recipe → payload（如需要）。
- 新载具：注册 entity → vehicle JSON → SBM model/texture/animation → renderer → recipe/loot/tag。
- 新弹体：注册 entity → renderer/model → 手动 tick/velocity update 策略 → 网络与伤害 tag。
- 新 Perk：注册 `ModPerks` → descriptor/script → Perk item → recipe/model/tag。
- 新 payload：定义 codec/type → 通用类型注册 → 正确侧 receiver → multiplayer 验证。
- 新饰品：`TrinketItem` → slot JSON → entity slot assignment → `trinkets` item tag。

## 验证

提交前至少执行：

```bash
./gradlew compileKotlin
./gradlew compileJava
./gradlew processResources
./gradlew build
```

涉及 mixin、网络或客户端渲染时还要验证 dedicated server 和客户端启动。编译成功不代表行为
1:1；载具、射击、重连、换维度、资源 reload、Trinkets 和可选依赖都需要对应的运行时检查。
