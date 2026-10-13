package com.atsuishio.superbwarfare.capability.player

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.capability.CapabilityWrapper
import com.atsuishio.superbwarfare.capability.ModCapabilities
import com.atsuishio.superbwarfare.capability.player.PlayerVariableSerializer.descriptor
import com.atsuishio.superbwarfare.capability.sync.SyncTarget
import com.atsuishio.superbwarfare.data.gun.Ammo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraftforge.common.capabilities.AutoRegisterCapability
import java.util.*
import java.util.function.Consumer

/**
 * 玩家自身的弹药存量与热成像状态。
 *
 * 与其它 capability 不同，这份数据在服务端是**就地修改**的可变对象：弹药读写散落在 `Ammo`、
 * Perk 和一堆事件处理器里，`modify` 又依赖 [watch] / [changed] 做"没变就不发包"的差分。
 * 因此它没做成不可变的 data class，但格式相关的代码已经全部收进 [PlayerVariableSerializer]，
 * 落盘与同步的管道仍由 [CapabilityWrapper] 提供 —— 本类只剩数据与几个基础行为。
 */
class PlayerVariable {
    private var old: PlayerVariable? = null

    @JvmField
    var ammo: MutableMap<Ammo, Int> = EnumMap(Ammo::class.java)
    var activeThermalImaging: Boolean = false

    /** 记录当前状态，配合 [changed] 判断 `modify` 是否真的产生了变化 */
    fun watch(): PlayerVariable {
        this.old = this.copy()
        return this
    }

    /** 是否有未同步的变化。仅在 [watch] 之后调用有意义。 */
    fun changed(): Boolean = old != null && old != this

    fun copy(): PlayerVariable {
        val clone = PlayerVariable()

        for (type in Ammo.entries) {
            type.set(clone, type.get(this))
        }

        clone.activeThermalImaging = this.activeThermalImaging

        return clone
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PlayerVariable) return false

        for (type in Ammo.entries) {
            if (type.get(this) != type.get(other)) return false
        }

        return activeThermalImaging == other.activeThermalImaging
    }

    /** 与 [equals] 保持一致：只统计弹药数量与热成像状态，不掺进 [old] 之类的临时状态。 */
    override fun hashCode(): Int {
        var result = activeThermalImaging.hashCode()

        for (type in Ammo.entries) {
            result = 31 * result + type.get(this)
        }

        return result
    }

    companion object {
        var ID: ResourceLocation = loc("player_variables")

        @JvmStatic
        fun getOrDefault(entity: Entity): PlayerVariable = ModCapabilities.PLAYER_VARIABLE.get(entity)

        /**
         * 标记玩家变量已变化，由自动同步框架在本 tick 结束时发给该玩家。
         *
         * 数据是就地修改的，包装类没法靠比较新旧值发现变化，只能由调用点显式声明。
         */
        @JvmStatic
        fun markDirty(entity: Entity) {
            ModCapabilities.PLAYER_VARIABLE.markDirty(entity)
        }

        /**
         * 编辑并自动同步玩家变量
         *
         * 数据没有实际变化时不会发包。
         */
        @JvmStatic
        fun modify(entity: Entity, consumer: Consumer<PlayerVariable>) {
            if (entity.level().isClientSide) return
            val cap = getOrDefault(entity)

            cap.watch()
            consumer.accept(cap)

            if (!cap.changed()) return

            markDirty(entity)
        }
    }

    /** 只为了让 Forge 有一个唯一的 capability 实现类而存在，见 [CapabilityWrapper]。 */
    @AutoRegisterCapability
    class Holder : CapabilityWrapper<PlayerVariable>(
        PlayerVariableSerializer,
        PlayerVariable(),
        SyncTarget.SELF,
    )
}

/**
 * [PlayerVariable] 的存档与同步格式。
 *
 * NBT：每个弹种按 [Ammo.serializationName] 直接写在根上，加上 `ActiveThermalImaging`，
 * 与旧版本手写的 `writeToNBT` 完全一致，因此老存档可以直接读；反过来，因为它只按
 * [descriptor] 里声明的字段取值，NBT 里多出来的键会被忽略。
 *
 * 网络：按同一顺序写 boolean + 各弹种的 int。两端 mod 版本必然一致、弹种集合不会不同，
 * 因此不需要再额外写一个长度前缀。
 *
 * 这里直接读写 `ammo` 而**不**经过 `Ammo.set`：那是带服务端配置上限的"玩家操作"入口，
 * 而同步过来的是服务端的权威数据，不能再拿客户端本地的上限去校验（会被静默丢弃）。
 */
@OptIn(ExperimentalSerializationApi::class)
object PlayerVariableSerializer : KSerializer<PlayerVariable> {

    private const val ACTIVE_THERMAL_IMAGING = "ActiveThermalImaging"

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PlayerVariable") {
        element<Boolean>(ACTIVE_THERMAL_IMAGING)

        for (ammo in Ammo.entries) {
            element<Int>(ammo.serializationName)
        }
    }

    override fun serialize(encoder: Encoder, value: PlayerVariable) {
        val composite = encoder.beginStructure(descriptor)

        composite.encodeBooleanElement(descriptor, 0, value.activeThermalImaging)

        for (index in Ammo.entries.indices) {
            composite.encodeIntElement(descriptor, index + 1, Ammo.entries[index].get(value))
        }

        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): PlayerVariable {
        val composite = decoder.beginStructure(descriptor)
        val variable = PlayerVariable()

        // 网络格式是定长顺序读写；NBT 按名字逐个取，缺键即默认值
        if (composite.decodeSequentially()) {
            variable.activeThermalImaging = composite.decodeBooleanElement(descriptor, 0)

            for (index in Ammo.entries.indices) {
                variable.ammo[Ammo.entries[index]] =
                    composite.decodeIntElement(descriptor, index + 1).coerceAtLeast(0)
            }
        } else {
            var index = composite.decodeElementIndex(descriptor)

            while (index != CompositeDecoder.DECODE_DONE) {
                if (index == 0) {
                    variable.activeThermalImaging = composite.decodeBooleanElement(descriptor, index)
                } else {
                    Ammo.entries.getOrNull(index - 1)?.let {
                        variable.ammo[it] = composite.decodeIntElement(descriptor, index).coerceAtLeast(0)
                    }
                }

                index = composite.decodeElementIndex(descriptor)
            }
        }

        composite.endStructure(descriptor)
        return variable
    }
}
