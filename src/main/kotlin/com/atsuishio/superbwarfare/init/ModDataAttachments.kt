package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.capability.entity.InfiniteAmmoCapability
import com.atsuishio.superbwarfare.capability.living.PhosphorusFireCapability
import com.atsuishio.superbwarfare.capability.living.RadiationCapability
import com.atsuishio.superbwarfare.capability.player.PlayerChargeState
import com.atsuishio.superbwarfare.capability.player.PlayerDeployState
import com.atsuishio.superbwarfare.capability.player.PlayerVariable
import com.atsuishio.superbwarfare.tools.createMapCodec
import com.atsuishio.superbwarfare.tools.createStreamCodec
import com.mojang.serialization.Codec
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier
import kotlin.reflect.full.createInstance

object ModDataAttachments {
    val ATTACHMENT_TYPES: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Mod.MODID)

    /**
     * 玩家自身的弹药 / 热成像数据，只有玩家本人需要读取，同步只发给持有者自己。
     *
     * 注意：NeoForge 的初始同步不会检查 [net.neoforged.neoforge.attachment.AttachmentSyncHandler.sendToPlayer]，
     * 因此其他玩家在开始追踪该玩家时仍会收到一次该数据（客户端不会读取它）。
     */
    @JvmField
    val PLAYER_VARIABLE = register<PlayerVariable>("player_variable") {
        it.sync(PlayerVariable.SyncHandler)
    }

    @JvmField
    val PHOSPHORUS_FIRE = registerPersistent<PhosphorusFireCapability>("phosphorus_fire")

    @JvmField
    val RADIATION = registerPersistent<RadiationCapability>("radiation")

    @JvmField
    val INFINITE_AMMO = registerPersistent<InfiniteAmmoCapability>("infinite_ammo")

    @JvmField
    val PLAYER_DEPLOY_STATE = registerSynced<PlayerDeployState>("player_deploy_state")

    @JvmField
    val PLAYER_CHARGE_STATE = registerSynced<PlayerChargeState>("player_charge_state")

    private inline fun <reified T : INBTSerializable<CompoundTag>> register(
        name: String,
        noinline supplier: () -> T = { T::class.createInstance() },
        noinline sync: ((AttachmentType.Builder<T>) -> AttachmentType.Builder<T>)? = null,
    ): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
        return ATTACHMENT_TYPES.register(name, Supplier {
            val builder = AttachmentType.serializable(supplier)
            (sync?.invoke(builder) ?: builder).build()
        })
    }

    /**
     * 唯一的数据形态工厂，把"落盘 / 同步"两个正交维度收在一处。
     *
     * [serializer] 为 `null` 时不挂序列化器，数据只存在于服务端内存并随同步下发；
     * 不为 `null` 时用它附加 `builder.serialize`，数据才会被写进存档。
     * [sync] 为 `null` 时不同步，只在本侧使用。
     */
    private inline fun <reified T : Any> attachment(
        name: String,
        serializer: Supplier<Codec<T>>?,
        noinline sync: ((AttachmentType.Builder<T>) -> AttachmentType.Builder<T>)?,
    ): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
        return ATTACHMENT_TYPES.register(name, Supplier {
            val builder = AttachmentType.builder(Supplier { T::class.createInstance() })
            serializer?.let { builder.serialize(it.get()) }
            (sync?.invoke(builder) ?: builder).build()
        })
    }

    /** 要落盘的数据：NBT/JSON 走 [createMapCodec]，并全量同步给追踪该实体的玩家。 */
    private inline fun <reified T : Any> registerPersistent(
        name: String,
        noinline sync: ((AttachmentType.Builder<T>) -> AttachmentType.Builder<T>)? = { it.sync(createStreamCodec<T>()) },
    ) = attachment<T>(name, serializer = { createMapCodec<T>().codec() }, sync = sync)

    /**
     * 不落盘的数据：没有序列化器，只存在于服务端内存 + 网络同步。
     *
     * 数据来源由持有方状态派生、重登后一定能重新推导出来时用这个。没有序列化器的 attachment
     * 也不会被复制到重生后的新实体上（NeoForge 会跳过），因此不需要额外考虑死亡重生时的清理。
     *
     * [sync] 默认走 kotlinx 序列化器：可空字段和 primitive 字段它都能处理，只有字段是
     * `Serialized*` 这类自定义类型、要换自己的 wire format 时才需要显式覆盖。
     */
    private inline fun <reified T : Any> registerSynced(
        name: String,
        noinline sync: ((AttachmentType.Builder<T>) -> AttachmentType.Builder<T>)? = { it.sync(createStreamCodec<T>()) },
    ) = attachment<T>(name, serializer = null, sync = sync)
}
