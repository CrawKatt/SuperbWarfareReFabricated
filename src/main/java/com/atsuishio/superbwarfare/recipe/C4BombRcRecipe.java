package com.atsuishio.superbwarfare.recipe;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModRecipes;
import com.atsuishio.superbwarfare.item.projectile.C4BombItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class C4BombRcRecipe extends CustomRecipe {
    public C4BombRcRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, @NotNull Level level) {
        if (container.getWidth() != 3 || container.getHeight() != 3) {
            return false;
        }

        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (slot == 4) {
                if (!stack.is(Items.COMPARATOR)) return false;
            } else if (!stack.is(ModItems.HIGH_ENERGY_EXPLOSIVES)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        return createResult();
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return createResult();
    }

    private static ItemStack createResult() {
        ItemStack result = new ItemStack(ModItems.C4_BOMB, 2);
        result.getOrCreateTag().putBoolean(C4BombItem.TAG_CONTROL, true);
        return result;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
        Ingredient explosive = Ingredient.of(ModItems.HIGH_ENERGY_EXPLOSIVES);
        for (int slot = 0; slot < ingredients.size(); slot++) {
            ingredients.set(slot, slot == 4 ? Ingredient.of(Items.COMPARATOR) : explosive);
        }
        return ingredients;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.C4_BOMB_RC_SERIALIZER;
    }
}
