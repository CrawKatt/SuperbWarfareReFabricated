package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.init.TestLoader.parseManifest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.full.createInstance

@Serializable
data class LoaderInfo(
    @SerialName("FormatVersion")
    val formatVersion: Int,
    @SerialName("Items")
    val items: Map<String, ItemRegisterInfo>,
)

@Serializable
data class ItemRegisterInfo(
    @SerialName("Rarity")
    val rarity: String = "common",
    @SerialName("MaxStackSize")
    val maxStackSize: Int = 64,
    @SerialName("FireResistant")
    val fireResistant: Boolean = false,
    @SerialName("Durability")
    val durability: Int? = null,
)

/**
 * 「由其他 mod 的 jar 提供注册描述、由 Superb Warfare 代为注册」机制的试验实现。
 *
 * 当前行为：
 *  1. 遍历所有 mod 文件，读取 `META-INF/sbw/registry.json`；
 *  2. 扫描 mod 内的 class 文件，找出被 [TestLoaderTarget] 标注的类并加载它们
 *     —— 只读 class annotation data，不预先加载整个 jar；
 *  3. 以「描述文件所属 mod id」为命名空间注册物品。
 *
 * 协议与设计约定见 `localmod/README.md`。跑通后再决定是否迁到独立的 plugin 包。
 *
 * 注意：所有运行期字符串（日志、异常）统一用英文 —— 中文在 Windows 的 GBK 控制台
 * 会被转义成乱码，而日志是给第三方看的。
 */
object TestLoader {

    /** 描述文件在 mod jar 内的位置。放 META-INF 是为了不被资源包/数据包看见或覆盖。 */
    private const val MANIFEST_PATH = "META-INF/sbw/registry.json"

    private const val FORMAT_VERSION = 1

    /**
     * ignoreUnknownKeys：协议要能向前演进，插件写了本版本不认识的字段不应该直接崩。
     * 缺字段 / 类型不对仍然会抛 SerializationException（含 MissingFieldException）。
     */
    private val JSON = Json {
        ignoreUnknownKeys = true
    }

    // TODO 正确在运行时读所有Rarity（含EnumExtensions）
    private val RARITIES = mapOf(
        "common" to Rarity.COMMON,
        "uncommon" to Rarity.UNCOMMON,
        "rare" to Rarity.RARE,
        "epic" to Rarity.EPIC
    )

    /** 命名空间 -> 该 mod 声明的物品。命名空间由 [ModContainer] 推导，不从 JSON 读。 */
    private val manifests = linkedMapOf<String, LoaderInfo>()

    /** 被 [TestLoaderTarget] 标注、且成功实例化的入口类，便于后续扩展时取用。 */
    @JvmField
    val loadedEntrypoints: MutableMap<String, MutableList<Any>> = linkedMapOf()

    fun register() {
        discover()
        registerItems()
    }

    private fun registerItems() {
        for ((namespace, info) in manifests) {
            for ((id, item) in info.items) {
                val location = ResourceLocation.fromNamespaceAndPath(namespace, id)

                // 所属 mod 自己注册了同名物品时让给它：我们只是"代注册"，不抢所有权。
                if (BuiltInRegistries.ITEM.containsKey(location)) {
                    Mod.LOGGER.error(
                        "[sbw-loader] {} already exists, skipping the declaration from mod {} ({})",
                        location, namespace, MANIFEST_PATH
                    )
                    continue
                }

                Registry.register(BuiltInRegistries.ITEM, location, item.create())

                Mod.LOGGER.info(
                    "[sbw-loader] registered {} (declared by mod {})",
                    location, namespace
                )
            }
        }
    }

    private fun discover() {
        for (container in FabricLoader.getInstance().allMods) {
            val namespace = container.metadata.id

            // 自己不用这套机制，自己的物品走 ModItems。
            if (namespace == Mod.MODID) continue

            val entrypoints = scanEntrypoints(container, namespace)

            val manifestPath = container.findPath(MANIFEST_PATH).orElse(null)
            if (manifestPath == null || !Files.isRegularFile(manifestPath)) {
                if (entrypoints.isNotEmpty()) {
                    Mod.LOGGER.warn(
                        "[sbw-loader] mod {} provides entrypoint classes but no {}",
                        namespace, MANIFEST_PATH
                    )
                }
                continue
            }

            val info = parseManifest(manifestPath, namespace)
            manifests[namespace] = info

            Mod.LOGGER.info(
                "[sbw-loader] discovered plugin {}: {} item declaration(s), {} entrypoint class(es)",
                namespace, info.items.size, entrypoints.size
            )
        }
    }

    private fun scanEntrypoints(container: ModContainer, namespace: String): List<String> {
        val annotationDescriptor = Type.getDescriptor(TestLoaderTarget::class.java)
        val names = linkedSetOf<String>()

        try {
            for (root in container.rootPaths) {
                Files.walk(root).use { paths ->
                    paths
                        .filter { path ->
                            Files.isRegularFile(path) &&
                                    path.fileName.toString().endsWith(".class")
                        }
                        .forEach { path ->
                            try {
                                val reader = Files.newInputStream(path).use { input ->
                                    ClassReader(input)
                                }

                                var annotated = false

                                reader.accept(
                                    object : ClassVisitor(Opcodes.ASM9) {
                                        override fun visitAnnotation(
                                            descriptor: String,
                                            visible: Boolean
                                        ): AnnotationVisitor? {
                                            if (descriptor == annotationDescriptor) {
                                                annotated = true
                                            }

                                            return null
                                        }
                                    },
                                    ClassReader.SKIP_CODE or
                                            ClassReader.SKIP_DEBUG or
                                            ClassReader.SKIP_FRAMES
                                )

                                if (annotated) {
                                    names.add(reader.className.replace('/', '.'))
                                }
                            } catch (t: Throwable) {
                                Mod.LOGGER.error(
                                    "[sbw-loader] failed to inspect class {} of mod {}",
                                    path, namespace, t
                                )
                            }
                        }
                }
            }
        } catch (t: Throwable) {
            Mod.LOGGER.error(
                "[sbw-loader] failed to scan annotations of mod {}",
                namespace, t
            )
            return emptyList()
        }

        val sortedNames = names.sorted()

        for (name in sortedNames) {
            loadEntrypoint(name, namespace)?.let { instance ->
                loadedEntrypoints
                    .getOrPut(namespace) { mutableListOf() }
                    .add(instance)
            }
        }

        return sortedNames
    }

    private fun loadEntrypoint(className: String, namespace: String): Any? {
        return try {
            val clazz = Class.forName(
                className,
                true,
                TestLoader::class.java.classLoader
            ).kotlin

            val instance = clazz.objectInstance ?: clazz.createInstance()

            Mod.LOGGER.info(
                "[sbw-loader] loaded entrypoint {} (mod={})",
                className, namespace
            )

            instance
        } catch (t: Throwable) {
            // 一个坏插件不应该炸掉整个启动。
            Mod.LOGGER.error(
                "[sbw-loader] failed to load entrypoint {} (mod={}), skipped",
                className, namespace, t
            )
            null
        }
    }

    /**
     * 解析 + 语义校验。校验放在这里是为了「插件写错了就在启动时报错」，
     * 而不是等到注册物品时才发现少了个字段。
     */
    private fun parseManifest(path: Path, namespace: String): LoaderInfo {
        fun fail(message: String): Nothing =
            throw IllegalStateException(
                "[sbw-loader] invalid $MANIFEST_PATH of mod $namespace: $message"
            )

        val text = try {
            Files.readString(path)
        } catch (t: Throwable) {
            fail("cannot be read: $t")
        }

        // 缺字段 / 类型不对 / 语法错误都会以 SerializationException（含 MissingFieldException）抛出。
        val info = try {
            JSON.decodeFromString(LoaderInfo.serializer(), text)
        } catch (t: Exception) {
            fail("cannot be decoded: ${t.message}")
        }

        if (info.formatVersion != FORMAT_VERSION) {
            fail(
                "unsupported FormatVersion=${info.formatVersion} " +
                        "(this build supports $FORMAT_VERSION)"
            )
        }

        // key 即物品 id：唯一性由 Map 保证，这里只校验它是不是合法的资源路径。
        for ((id, item) in info.items) {
            if (':' in id) {
                fail(
                    "Items['$id'] must not be namespaced: " +
                            "the namespace is derived from the owning mod"
                )
            }

            if (!ResourceLocation.isValidPath(id)) {
                fail(
                    "Items['$id'] is not a valid item path " +
                            "(lowercase a-z, 0-9, '_', '-', '.')"
                )
            }

            if (RARITIES[item.rarity.lowercase()] == null) {
                fail(
                    "Items['$id'].Rarity '${item.rarity}' is unknown " +
                            "(expected one of ${RARITIES.keys.joinToString()})"
                )
            }

            if (item.maxStackSize !in 1..99) {
                fail(
                    "Items['$id'].MaxStackSize=${item.maxStackSize} " +
                            "is out of range 1..99"
                )
            }

            if (item.durability != null && item.durability < 1) {
                fail("Items['$id'].Durability must be >= 1")
            }
        }

        return info
    }

    /** Rarity 已在 [parseManifest] 校验过，这里的 getValue 不会抛。 */
    private fun ItemRegisterInfo.create(): Item {
        val properties = Item.Properties()
            .stacksTo(maxStackSize)
            .rarity(RARITIES.getValue(rarity.lowercase()))

        if (fireResistant) {
            properties.fireResistant()
        }

        durability?.let {
            properties.durability(it)
        }

        return Item(properties)
    }
}