# Superb Warfare ReFabricated 项目架构指南

## 概览

Superb Warfare ReFabricated 是 Superb Warfare NeoForge 版面向 Fabric 1.20.1 的移植。本仓库实现的是 Fabric 版本；NeoForge 版本仅用于参考原始行为。

- Mod ID：`superbwarfare`
- 主包名：`com.atsuishio.superbwarfare`
- Java 17，Kotlin/Java 混合开发
- Fabric Loom + Fabric API
- GeckoLib 与 Simple Bedrock Model 用于动画和渲染
- Trinkets 用于饰品栏位
- Cardinal Components 与 Team Reborn Energy 用于组件和能量系统
- Forge Config API Port 与 Cloth Config 用于配置
- Rhino/LuaJ 用于脚本系统

具体版本、映射和依赖始终以 `build.gradle.kts` 与 `gradle.properties` 为准。

## 移植原则

NeoForge 实现是行为规范，而不是目标平台。移植或修复功能时必须保留：

- 注册项与资源 ID。
- NBT、JSON 和存档数据格式。
- 初始化与执行顺序。
- 逻辑侧、线程和生命周期。
- 网络包的方向、内容和字段顺序。
- 事件的取消、可变结果及其他语义。
- 视觉结果和其他可观察行为。

优先使用最直接的 Fabric 或原版等价实现。如果 Fabric API 没有等价事件，则使用由小型 Mixin 触发的自定义回调。不得为了通过编译而删除机制、返回常量或留下空实现。

尽可能保留原有代码结构和语言。保持行为一致的移植不应顺带重组或重新设计子系统。

## 核心概念

### 属性修改链（PMC）

`GunData.get(prop)` 按以下顺序计算属性的最终值：

1. NBT 中保存的 JSON 属性覆写。
2. `GunItem` 的修改。
3. 当前开火模式的修改。
4. `AmmoConsumer` 的修改。
5. 所有已装备 Perk 的修改。
6. `GunProp.modifyProperty` 定义的内置上下限。

该顺序属于武器行为的一部分，不得随意调整。

### NBT 持久化

枪械的运行时状态——弹药、Perk、配件、开火模式、热量和换弹状态——保存在 `ItemStack` 的 NBT 中，并通过 `IntValue`、`DoubleValue`、`BooleanValue`、`StringEnumValue` 等类型化包装类访问。

### 数据驱动设计

枪械、资源和载具模板通过 `CustomData` 从 JSON 加载。`DefaultGunData`、`GunResource` 和 `VehicleData` 负责描述内容，不要将这一流程替换为硬编码数据。

```text
JSON 资源 → CustomData.load()
  ├─ 枪械数据 → DefaultGunData
  ├─ 枪械资源 → GunResource
  └─ 载具数据 → VehicleData

ItemStack NBT ↔ GunData.from(stack)
  ├─ get(prop) → 应用 PMC 链
  └─ save() → 持久化状态
```

## 项目结构

Gradle 会编译 `src/main/kotlin` 和 `src/main/java` 中的 Kotlin 代码。除非确有必要，不要在两个源码目录之间移动文件。

| 路径或包 | 职责 |
|---|---|
| `Mod.kt` | 通用入口、注册、生命周期与服务端 Tick |
| `ClientMod.java` | 客户端入口、渲染、模型、界面与按键 |
| `init/` | 物品、方块、实体、声音、配方、Perk 等内容的注册 |
| `data/` | 枪械、载具、容器数据与序列化 |
| `item/` | 武器、弹药、护甲、饰品及其他物品 |
| `entity/` | 载具、弹射物与生物实体 |
| `event/` | 事件处理器与回调注册 |
| `event/custom/` | 对 Fabric API 未提供事件的回调实现 |
| `client/` | 渲染器、模型、GUI、粒子、Overlay 与 Shader |
| `network/` | 网络注册、编解码与 C2S/S2C 消息 |
| `capability/` | 实体组件与能量适配器 |
| `compat/` | 其他 Mod 的可选兼容层 |
| `resource/` | 数据与模型的加载、重载 |
| `mixins/` | Minecraft Hook、Accessor 与 Invoker |
| `datagen/` | 模型、配方、战利品表、标签与进度的数据生成器 |
| `world/` | SavedData、物理与世界工具 |
| `script/` | 脚本引擎集成 |

## 实体与枪械

```text
Entity
  ├─ VehicleEntity
  │    ├─ GeoVehicleEntity
  │    └─ AutoAimableEntity
  │         └─ ArtilleryEntity
  ├─ Projectile
  │    └─ ProjectileEntity
  └─ Mod 生物实体
```

`VehicleEntity` 负责能量、武器、座位、库存、OBB 碰撞、残骸状态和引擎。`AutoAimableEntity` 额外提供拥有者、目标、启用状态、射击解算以及敌人/弹射物过滤。

`ProjectileEntity` 实现自定义射线追踪、OBB 碰撞、部位命中、穿透、爆炸、火焰、击退、碎片和曳光弹。

`GunItem` 是枪械基类，并参与 PMC 链。`GunGeoItem` 使用 GeckoLib 提供待机、开火、换弹、近战和冲刺等状态的动画。换弹与拉栓的定时回调属于武器行为的一部分。

## Fabric 集成

### 入口与注册

`fabric.mod.json` 是元数据、依赖、Mixin、Access Widener 和入口的唯一依据。NeoForge 遗留的 `mods.toml` 不定义 Fabric Mod。

- `ModInitializer`：通用初始化。
- `ClientModInitializer`：仅客户端初始化。
- `DataGeneratorEntrypoint`：资源生成。
- Cardinal Components 入口：实体组件注册。

内容注册通过 `init/` 中的持有对象调用原版/Fabric 注册 API；不要重新实现 `DeferredRegister` 或 `RegistryObject`。

### 事件与 Mixin

NeoForge 事件应替换为阶段与语义一致的 Fabric 回调。没有合适回调时：

1. 如果已有自定义回调的契约完全相同，则优先复用。
2. 为单一事件创建职责明确的回调。
3. 通过最小且稳定的 Java Mixin 注入点触发回调。
4. 业务逻辑放在监听器中，不要写进 Mixin。

所有 Mixin 都必须在 `mixins.superbwarfare.json` 中声明。客户端 Mixin 放在 `client` 段；通用 Mixin 必须能够在独立服务端加载。始终依据项目配置的映射核对目标、描述符、Ordinal 以及取消语义。

如果唯一障碍是访问级别，则使用 `superbwarfare.accesswidener` 或 Accessor/Invoker Mixin。

### 组件、能量与饰品

- NeoForge 实体 Capability → Cardinal Components API。
- 能量 Capability → Team Reborn Energy。
- Curios → Trinkets。
- Forge 配置 → Forge Config API Port。

Forge Config API Port 会保留部分 `net.minecraftforge.*` 包下的类。在确认每个类由哪个依赖提供之前，不要全局替换这些 import。

### 客户端与服务端分离

通用代码在独立服务端加载时不得解析 `net.minecraft.client`、渲染器或客户端处理器。渲染、模型、按键、Overlay、Shader 和客户端接收器应从客户端入口或客户端 Mixin 注册。

如果 JVM 在通用类的静态加载阶段已经解析了客户端引用，仅检查运行环境并不能避免服务端崩溃。

### 网络通信

`NetworkRegistry` 统一负责握手、注册、编解码和消息发送。

- 明确区分 C2S 与 S2C 方向。
- 保持所有序列化字段的顺序和类型。
- 在服务端线程执行 C2S 游戏逻辑，并验证发送消息的玩家。
- 在客户端执行 S2C Handler，同时避免服务端加载客户端类。
- 所有消息必须先注册再发送。
- 网络功能必须用连接到服务端的客户端测试，不能只测试单人游戏。

## 兼容与有意保留的差异

- Cold Sweat 没有适用于此 Fabric 移植的版本；对应兼容通过 Thermoo 实现。不要直接恢复 NeoForge 的 Cold Sweat 集成。
- `ThermalShaderHandler` 的注册保持禁用，因为该 Shader 会造成 Overlay 重复。解决效果被二次应用的问题之前不要启用。
- 可选兼容层在访问其他 Mod 的类之前，必须使用 `FabricLoader` 检查对应 Mod 是否存在。
- 除非任务明确要求调查，否则保留有意注释或禁用的代码块。

## 编码约定

- Kotlin 文件继续使用 Kotlin，Java 文件继续使用 Java，除非 API 限制确实要求改变。
- 使用现有的 `init/` 持有对象注册内容。
- 移植时保留类名、方法名、字段名、ID、序列化键和初始化顺序。
- 避免与任务无关的重构、重命名和格式化。
- 新建回调、工具或适配器之前，先复用已有实现。
- 所有纯客户端逻辑必须与通用代码隔离。
- 不能因为项目能够编译就认定已经实现行为一致；必须验证可观察行为。

## 常见任务指引

- **添加枪械：** 定义 JSON 数据、注册物品，并按需添加模型与渲染器。
- **添加载具：** 定义 `VehicleData`/资源、注册实体，并添加客户端模型与渲染器。
- **添加弹药：** 注册物品并配置对应的 `AmmoConsumer`。
- **添加 Perk：** 在 `ModPerks` 中注册，并实现 PMC 修改。
- **添加网络包：** 创建继承 `ServerPacketPayload` / `ClientPacketPayload` 的消息类并标注 `@RegisterPacket`，无需手动维护注册列表。
- **添加事件：** 先查找 Fabric 回调或已有自定义回调；只有缺少等价 Hook 时才使用 Mixin。
- **添加配置：** 使用现有配置类，需要界面时通过 Cloth Config 暴露。
- **添加可选兼容：** 隔离依赖引用，并通过 Mod 检测保护访问。

## 验证

根据改动范围使用以下命令：

```bash
./gradlew compileKotlin
./gradlew build
./gradlew runServer
./gradlew runClient
```

只有在确实需要修改生成资源时才运行数据生成，并始终检查生成后的差异。

功能改动至少需要验证受影响的逻辑侧、注册、持久化、事件和网络。渲染或 Shader 改动还必须进行视觉检查。单独出现 `BUILD SUCCESSFUL` 不能证明与 NeoForge 行为一致。
