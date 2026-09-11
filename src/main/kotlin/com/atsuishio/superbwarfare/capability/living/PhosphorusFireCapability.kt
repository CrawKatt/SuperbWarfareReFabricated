package com.atsuishio.superbwarfare.capability.living

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.capability.ModCapabilities
import com.atsuishio.superbwarfare.capability.sync.CapabilitySync
import com.atsuishio.superbwarfare.capability.sync.SyncedCapability
import com.atsuishio.superbwarfare.serialization.ByteBufDecoder
import com.atsuishio.superbwarfare.serialization.ByteBufEncoder
import com.atsuishio.superbwarfare.serialization.decodeFromCompoundTag
import com.atsuishio.superbwarfare.serialization.encodeToCompoundTag
import dev.onyxstudios.cca.api.v3.component.Component
import net.minecraft.world.entity.LivingEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation

@Serializable
data class PhosphorusFireCapability(
    @SerialName("SbwPhosphorusFire")
    var isOnFire: Boolean = false,
) : Component, SyncedCapability {

    override fun readFromNbt(tag: CompoundTag) {
        isOnFire = decodeFromCompoundTag(serializer<PhosphorusFireCapability>(), tag).isOnFire
    }

    override fun writeToNbt(tag: CompoundTag) {
        val written = encodeToCompoundTag(serializer<PhosphorusFireCapability>(), this)
        for (key in written.allKeys) written.get(key)?.let { tag.put(key, it) }
    }

    // 只有一个字段，增量与全量没有区别
    override fun writeSync(encoder: ByteBufEncoder, full: Boolean) {
        encoder.encodeBoolean(isOnFire)
    }

    override fun readSync(decoder: ByteBufDecoder, full: Boolean) {
        isOnFire = decoder.decodeBoolean()
    }

    companion object {
        @JvmField
        val ID: ResourceLocation = loc("phosphorus_fire_capability")

        @JvmStatic
        fun get(entity: LivingEntity): PhosphorusFireCapability = ModCapabilities.PHOSPHORUS_FIRE.get(entity)

        @JvmStatic
        fun set(entity: LivingEntity, value: Boolean) {
            get(entity).isOnFire = value
            // 由 CapabilitySync 自动决定何时、向谁同步（客户端调用时无副作用）
            CapabilitySync.markDirty(entity, ID)
        }
    }
}
