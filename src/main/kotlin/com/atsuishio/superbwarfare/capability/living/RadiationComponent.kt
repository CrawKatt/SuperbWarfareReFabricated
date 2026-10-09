package com.atsuishio.superbwarfare.capability.living

import com.atsuishio.superbwarfare.serialization.decodeFromCompoundTag
import com.atsuishio.superbwarfare.serialization.encodeToCompoundTag
import com.atsuishio.superbwarfare.serialization.serializersModule
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent

class RadiationComponent : AutoSyncedComponent {
    var value = RadiationCapability()

    override fun readFromNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        value = decodeFromCompoundTag(RadiationCapability.serializer(), tag, serializersModule)
    }

    override fun writeToNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        tag.merge(encodeToCompoundTag(RadiationCapability.serializer(), value, serializersModule))
    }
}
