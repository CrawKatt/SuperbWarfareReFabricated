package com.atsuishio.superbwarfare.data.gun.subdata

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.init.ModPerks
import com.atsuishio.superbwarfare.item.misc.PerkItem
import com.atsuishio.superbwarfare.perk.Perk
import com.atsuishio.superbwarfare.perk.PerkInstance
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag

class Perks(gun: GunData) {
    private val rootTag: CompoundTag = gun.perk()

    private fun findPerkByName(name: String): Perk? {
        val allPerks = ModPerks.AMMO_PERKS + ModPerks.FUNC_PERKS + ModPerks.DAMAGE_PERKS
        return allPerks.firstOrNull { it.name == name }
    }

    fun getOrCreateList(type: Perk.Type): ListTag {
        val typeName = type.typeName

        if (rootTag.contains(typeName, Tag.TAG_LIST.toInt())) {
            return rootTag.getList(typeName, Tag.TAG_COMPOUND.toInt())
        }

        val list = ListTag()
        rootTag.put(typeName, list)
        return list
    }

    fun has(perk: Perk): Boolean {
        val list = rootTag.getList(perk.type.typeName, Tag.TAG_COMPOUND.toInt())

        return list.any {
            it is CompoundTag && it.getString("Name") == perk.name
        }
    }

    fun has(type: Perk.Type): Boolean {
        val list = rootTag.getList(type.typeName, Tag.TAG_COMPOUND.toInt())
        return !list.isEmpty()
    }

    fun set(perk: Perk, level: Short) {
        val list = getOrCreateList(perk.type)

        val existing = list.firstOrNull {
            it is CompoundTag && it.getString("Name") == perk.name
        } as? CompoundTag

        if (existing != null) {
            existing.putShort("Level", level)
        } else {
            val newEntry = CompoundTag().apply {
                putString("Name", perk.name)
                putShort("Level", level)
            }

            list.add(newEntry)
        }

        rootTag.put(perk.type.typeName, list)
    }

    fun set(instance: PerkInstance) {
        set(instance.perk, instance.level)
    }

    fun getLevel(perk: Perk): Short {
        val typeName = perk.type.typeName

        if (rootTag.contains(typeName, Tag.TAG_LIST.toInt())) {
            val list = rootTag.getList(typeName, Tag.TAG_COMPOUND.toInt())

            val entry = list.firstOrNull {
                it is CompoundTag && it.getString("Name") == perk.name
            } as? CompoundTag

            return entry?.getShort("Level") ?: 0
        }

        if (rootTag.contains(typeName, Tag.TAG_COMPOUND.toInt())) {
            val tag = rootTag.getCompound(typeName)

            if (tag.getString("Name") == perk.name) {
                return tag.getShort("Level")
            }
        }

        return 0
    }

    fun getLevel(item: PerkItem<*>): Short {
        return getLevel(item.perk)
    }

    fun getInstances(type: Perk.Type): List<PerkInstance> {
        val typeName = type.typeName
        val instances = mutableListOf<PerkInstance>()

        if (rootTag.contains(typeName, Tag.TAG_LIST.toInt())) {
            val list = rootTag.getList(typeName, Tag.TAG_COMPOUND.toInt())

            for (i in 0 until list.size) {
                val tag = list.getCompound(i)
                val name = tag.getString("Name")
                val level = tag.getShort("Level")

                val perk = findPerkByName(name)
                if (perk != null) {
                    instances.add(PerkInstance(perk, level))
                }
            }
        } else if (rootTag.contains(typeName, Tag.TAG_COMPOUND.toInt())) {
            val tag = rootTag.getCompound(typeName)
            val name = tag.getString("Name")
            val level = tag.getShort("Level")

            val perk = findPerkByName(name)
            if (perk != null) {
                instances.add(PerkInstance(perk, level))
            }
        }

        return instances
    }

    fun get(perk: Perk): Perk? {
        return get(perk.type)
    }

    fun get(type: Perk.Type): Perk? {
        val typeName = type.typeName

        if (rootTag.contains(typeName, Tag.TAG_LIST.toInt())) {
            val list = rootTag.getList(typeName, Tag.TAG_COMPOUND.toInt())

            if (list.isEmpty()) {
                return null
            }

            return findPerkByName(list.getCompound(0).getString("Name"))
        }

        if (rootTag.contains(typeName, Tag.TAG_COMPOUND.toInt())) {
            return findPerkByName(rootTag.getCompound(typeName).getString("Name"))
        }

        return null
    }

    fun reduceCooldown(perk: Perk, cooldownKey: String) {
        val list = rootTag.getList(perk.type.typeName, Tag.TAG_COMPOUND.toInt())

        val entry = list.firstOrNull {
            it is CompoundTag && it.getString("Name") == perk.name
        } as? CompoundTag

        if (entry != null) {
            if (!entry.contains(cooldownKey)) {
                return
            }

            val newValue = entry.getInt(cooldownKey) - 1

            if (newValue <= 0) {
                entry.remove(cooldownKey)
            } else {
                entry.putInt(cooldownKey, newValue)
            }
        }
    }

    fun remove(perk: Perk) {
        val typeName = perk.type.typeName

        if (!rootTag.contains(typeName, Tag.TAG_LIST.toInt())) {
            return
        }

        val list = rootTag.getList(typeName, Tag.TAG_COMPOUND.toInt())

        list.removeIf {
            it is CompoundTag && it.getString("Name") == perk.name
        }

        if (list.isEmpty()) {
            rootTag.remove(typeName)
        }
    }

    fun removeAll(type: Perk.Type) {
        rootTag.remove(type.typeName)
    }

    fun getTag(perk: Perk): CompoundTag? {
        val typeName = perk.type.typeName

        if (!rootTag.contains(typeName, Tag.TAG_LIST.toInt())) {
            return null
        }

        return rootTag.getList(typeName, Tag.TAG_COMPOUND.toInt())
            .filterIsInstance<CompoundTag>()
            .firstOrNull { perk.name == it.getString("Name") }
    }

    fun getOrCreateTag(perk: Perk): CompoundTag {
        val list = getOrCreateList(perk.type)

        val existing = list.firstOrNull {
            it is CompoundTag && it.getString("Name") == perk.name
        } as? CompoundTag

        if (existing != null) {
            return existing
        }

        val tag = CompoundTag().apply {
            putString("Name", perk.name)
            putShort("Level", 0)
        }

        list.add(tag)
        return tag
    }
}
