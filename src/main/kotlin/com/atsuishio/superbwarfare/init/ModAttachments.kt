package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.item.attachment.AttachmentItem
import net.minecraft.world.item.Item
import java.util.function.Supplier

object ModAttachments {
    @JvmField
    val OEM_STOCK_STANDARD: Item = register("oem_stock_standard")

    @JvmField
    val MAGAZINE_STANDARD: Item = register("magazine_standard")

    @JvmField
    val ITEMS: List<Item> = listOf(OEM_STOCK_STANDARD, MAGAZINE_STANDARD)

    private fun register(id: String): Item {
        return Registration.item(id, Supplier { AttachmentItem("${Mod.MODID}:$id") })
    }

    @JvmStatic
    fun init() = Unit
}
