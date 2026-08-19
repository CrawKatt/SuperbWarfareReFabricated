package com.atsuishio.superbwarfare.world.saveddata

import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.atsuishio.superbwarfare.network.message.receive.TDMSyncMessage
import com.atsuishio.superbwarfare.tools.sendPacketToAll
import com.atsuishio.superbwarfare.tools.sendPacketTo
import com.google.common.collect.Sets
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.saveddata.SavedData

class TDMSavedData : SavedData {
    @JvmField
    val entities: MutableSet<String> = Sets.newHashSet()

    constructor()

    constructor(entities: Collection<String>) {
        this.entities.addAll(entities)
    }

    override fun save(pCompoundTag: CompoundTag): CompoundTag {
        pCompoundTag.put("Entities", this.saveEntities())
        return pCompoundTag
    }

    private fun saveEntities(): ListTag {
        val tags = ListTag()
        for (s in this.entities) {
            tags.add(StringTag.valueOf(s))
        }
        return tags
    }

    private fun loadEntities(pTagList: ListTag) {
        for (i in pTagList.indices) {
            this.entities.add(pTagList.getString(i))
        }
    }

    fun addEntity(entity: String): Boolean {
        return this.entities.add(entity)
    }

    fun removeEntity(entity: String): Boolean {
        return this.entities.remove(entity)
    }

    fun containsEntity(entity: String): Boolean {
        return this.entities.contains(entity)
    }

    fun sync() {
        this.setDirty()
        sendPacketToAll(TDMSyncMessage(this))
    }

    companion object {
        const val FILE_ID: String = "superbwarfare_tdm"

        @JvmStatic
        fun load(tag: CompoundTag): TDMSavedData {
            val data = TDMSavedData()

            if (tag.contains("Entities", Tag.TAG_LIST.toInt())) {
                data.loadEntities(tag.getList("Entities", Tag.TAG_STRING.toInt()))
            }

            return data
        }

        @JvmStatic
        fun enabledTDM(entity: Entity): Boolean {
            val level = entity.level()

            return if (level is ServerLevel) {
                level.dataStorage
                    .computeIfAbsent(
                        { load(it) },
                        { TDMSavedData() },
                        FILE_ID
                    ).containsEntity(entity.getStringUUID())
            } else {
                ClientEventHandler.tdmSavedData.containsEntity(entity.stringUUID)
            }
        }

        @JvmStatic
        fun register() {
            ServerPlayConnectionEvents.JOIN.register join@{ handler, _, _ ->
                val player = handler.player
                val level = player.serverLevel()

                val data = level.dataStorage.get(
                    { load(it) },
                    FILE_ID
                ) ?: return@join

                sendPacketTo(player, TDMSyncMessage(data))
            }
        }
    }
}
