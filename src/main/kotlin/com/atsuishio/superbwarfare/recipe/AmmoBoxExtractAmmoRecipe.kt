package com.atsuishio.superbwarfare.recipe

import com.atsuishio.superbwarfare.data.gun.Ammo
import com.atsuishio.superbwarfare.init.ModDataComponents
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModRecipes
import com.atsuishio.superbwarfare.item.ammo.AmmoBoxInfo
import com.atsuishio.superbwarfare.item.ammo.AmmoBoxItem
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import javax.annotation.ParametersAreNonnullByDefault

class AmmoBoxExtractAmmoRecipe(pCategory: CraftingBookCategory) : CustomRecipe(pCategory) {
    @ParametersAreNonnullByDefault
    override fun matches(input: CraftingInput, level: Level): Boolean {
        var hasAmmoBox = false
        var ammoBoxItem = ItemStack.EMPTY

        for (item in input.items()) {
            if (item.item is AmmoBoxItem) {
                if (hasAmmoBox) return false
                hasAmmoBox = true
                ammoBoxItem = item
            } else if (!item.isEmpty) {
                return false
            }
        }

        val data = ammoBoxItem.get(ModDataComponents.AMMO_BOX_INFO) ?: return false

        val typeString = data.type
        val type = Ammo.getType(typeString) ?: return false

        return type.get(ammoBoxItem) > 0
    }

    @ParametersAreNonnullByDefault
    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        var type: Ammo? = null

        for (item in input.items()) {
            if (item.item is AmmoBoxItem) {
                val data: AmmoBoxInfo = checkNotNull(item.get(ModDataComponents.AMMO_BOX_INFO))
                type = Ammo.getType(data.type)
                break
            }
        }

        checkNotNull(type)

        // 也许这边有更好的方案？
        return when (type) {
            Ammo.HANDGUN -> ItemStack(ModItems.HANDGUN_AMMO)
            Ammo.RIFLE -> ItemStack(ModItems.RIFLE_AMMO)
            Ammo.SHOTGUN -> ItemStack(ModItems.SHOTGUN_AMMO)
            Ammo.SNIPER -> ItemStack(ModItems.SNIPER_AMMO)
            Ammo.HEAVY -> ItemStack(ModItems.HEAVY_AMMO)
        }
    }

    override fun getRemainingItems(input: CraftingInput): NonNullList<ItemStack?> {
        val remaining = super.getRemainingItems(input)

        for (i in input.items().indices) {
            val item = input.getItem(i)
            if (item.item is AmmoBoxItem) {
                val ammoBox = item.copy()

                val data: AmmoBoxInfo = checkNotNull(ammoBox.get(ModDataComponents.AMMO_BOX_INFO))
                val type: Ammo = checkNotNull(Ammo.getType(data.type))

                type.add(ammoBox, -1)
                remaining[i] = ammoBox

                break
            }
        }

        return remaining
    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        return true
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipes.AMMO_BOX_EXTRACT_AMMO_SERIALIZER
    }
}
