package com.atsuishio.superbwarfare.data.gun.ammo_consumer_strategy

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.client.language.ClientLanguageGetter
import com.atsuishio.superbwarfare.data.gun.AmmoConsumer
import com.atsuishio.superbwarfare.data.gun.AmmoSource
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.tools.InventoryTool
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.fabricmc.api.Environment
import net.fabricmc.api.EnvType
import com.atsuishio.superbwarfare.init.ModCapabilities
import com.atsuishio.superbwarfare.capability.api.IItemHandler
import net.minecraft.core.registries.BuiltInRegistries

/**
 * 物品弹药策略（兜底策略）— ammo 字符串形如 "minecraft:arrow"、 "mod:item"、 "mod:item{tag}"
 *
 * match: 非空白字符串均视为可能匹配（最后顺位，init 阶段再作验证）
 * init: 手动解析 id 和可选的 {data}
 */
object ItemAmmoStrategy : AmmoConsumeStrategy() {

    override val defaultType = AmmoConsumer.AmmoConsumeType.ITEM

    override fun match(ammo: String) = ammo.isNotBlank()

    override fun init(source: AmmoSource, count: Int, matchedString: String) {
        // 手动解析 id 和 data
        // matchedString 形如 "mod:item{tag}" 或 "minecraft:arrow"

        val id: String
        val data: String
        val braceIdx = matchedString.indexOf('{')
        if (braceIdx >= 0) {
            id = matchedString.substring(0, braceIdx).trim()
            data = matchedString.substring(braceIdx)
        } else {
            id = matchedString
            data = ""
        }

        val location = ResourceLocation.tryParse(id)
        if (location == null) {
            Mod.LOGGER.warn("invalid item id: {}", id)
            source.type = AmmoConsumer.AmmoConsumeType.INVALID
            return
        }
        val item = BuiltInRegistries.ITEM.get(location)
        if (item == null || item === Items.AIR) {
            Mod.LOGGER.warn("invalid item: {}", id)
            source.type = AmmoConsumer.AmmoConsumeType.INVALID
            return
        }

        source.stack = ItemStack(item)
        if (data.isNotEmpty()) {
            try {
                val tag = NbtUtils.snbtToStructure(data)
                tag.putString("id", location.toString())
                tag.putInt("count", 1)
                source.stack = ItemStack.of(tag)
            } catch (exception: Exception) {
                Mod.LOGGER.warn("invalid item data {}: {}", data, exception.message)
                source.type = AmmoConsumer.AmmoConsumeType.INVALID
                return
            }
        }
    }

    override fun consume(data: GunData, source: AmmoSource, shooter: Entity?, count: Int): Int {
        val handler = shooter?.let { ModCapabilities.ITEM_HANDLER_ENTITY.find(it, null) }
        if (handler != null) {
            return consume(data, source, handler, count)
        } else {
            Mod.LOGGER.warn("consume ammo failed: invalid item handler for entity {}", shooter)
            return 0
        }
    }

    override fun consume(data: GunData, source: AmmoSource, handler: IItemHandler, count: Int): Int {
        return InventoryTool.consumeItem(
            handler,
            { stack -> source.isAmmoItem(stack) },
            count
        )
    }

    override fun count(data: GunData, source: AmmoSource, entity: Entity?): Int {
        if (entity == null) return 0
        val handler = ModCapabilities.ITEM_HANDLER_ENTITY.find(entity, null)
        return count(data, source, handler)
    }

    override fun count(data: GunData, source: AmmoSource, handler: IItemHandler?): Int {
        if (handler == null) return 0
        return InventoryTool.countItem(handler) { stack -> source.isAmmoItem(stack) }
    }

    override fun withdraw(source: AmmoSource, ammoSupplier: Entity, count: Int): Int {
        if (ammoSupplier is Player) {
            InventoryTool.insertItem(ammoSupplier, source.stack(), count)
            return count
        } else {
            val itemHandler = ModCapabilities.ITEM_HANDLER_ENTITY.find(ammoSupplier, null)
            if (itemHandler != null) {
                return withdraw(source, itemHandler, count)
            } else {
                Mod.LOGGER.warn("withdraw ammo failed: invalid item handler")
            }
        }
        return 0
    }

    override fun withdraw(source: AmmoSource, handler: IItemHandler, count: Int): Int {
        return InventoryTool.insertItem(handler, source.stack(), count)
    }

    @Environment(EnvType.CLIENT)
    override fun getDisplayName(source: AmmoSource): String {
        val stack = source.stack
        if (stack.isEmpty) return super.getDisplayName(source)
        val nameComponent = source.stack().hoverName
        val contents = nameComponent.contents
        if (contents is TranslatableContents) {
            return ClientLanguageGetter.EN_US.getOrDefault(contents.key)
        }

        return ClientLanguageGetter.EN_US.getOrDefault(source.stack().descriptionId)
    }
}
