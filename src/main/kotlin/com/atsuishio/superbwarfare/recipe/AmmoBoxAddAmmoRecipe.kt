package com.atsuishio.superbwarfare.recipe

import com.atsuishio.superbwarfare.data.gun.Ammo
import com.atsuishio.superbwarfare.init.ModRecipes
import com.atsuishio.superbwarfare.item.ammo.AmmoBoxItem
import com.atsuishio.superbwarfare.item.ammo.AmmoSupplierItem
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import javax.annotation.ParametersAreNonnullByDefault

class AmmoBoxAddAmmoRecipe(pCategory: CraftingBookCategory) : CustomRecipe(pCategory) {
    override fun matches(input: CraftingInput, pLevel: Level): Boolean {
        var hasAmmoBox = false
        var hasAmmo = false

        for (item in input.items()) {
            if (item.item is AmmoBoxItem) {
                if (hasAmmoBox) return false
                hasAmmoBox = true
            } else if (item.item is AmmoSupplierItem) {
                hasAmmo = true
            } else if (!item.isEmpty) {
                return false
            }
        }

        return hasAmmoBox && hasAmmo
    }


    private fun addAmmo(map: HashMap<Ammo, Int>, type: Ammo, count: Int) {
        map[type] = map.getOrDefault(type, 0) + count
    }

    @ParametersAreNonnullByDefault
    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        val map = HashMap<Ammo, Int>()
        var ammoBox = ItemStack.EMPTY

        for (item in input.items()) {
            if (item.item is AmmoSupplierItem) {
                val ammoSupplier = item.item as AmmoSupplierItem
                addAmmo(map, ammoSupplier.type, ammoSupplier.ammoToAdd)
            } else if (item.item is AmmoBoxItem) {
                ammoBox = item.copy()
                for (type in Ammo.entries) {
                    addAmmo(map, type, type.get(item))
                }
            }
        }

        for (type in Ammo.entries) {
            type.set(ammoBox, map.getOrDefault(type, 0))
        }

        return ammoBox
    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        return true
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipes.AMMO_BOX_ADD_AMMO_SERIALIZER
    }
}