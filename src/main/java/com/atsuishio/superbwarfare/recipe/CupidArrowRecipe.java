package com.atsuishio.superbwarfare.recipe;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CupidArrowRecipe extends CustomRecipe {
    private static final ItemStack HEALING_POTION = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HEALING);

    public CupidArrowRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, @NotNull Level level) {
        if (container.getWidth() != 3 || container.getHeight() != 3) {
            return false;
        }

        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            boolean matches = switch (slot) {
                case 0, 2, 6, 8 -> stack.is(ItemTags.ARROWS);
                case 1, 7 -> stack.is(Items.BOW);
                case 3, 5 -> ItemStack.isSameItemSameTags(HEALING_POTION, stack);
                case 4 -> stack.is(ModItems.EMPTY_PERK);
                default -> false;
            };
            if (!matches) return false;
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
        return new ItemStack(ModItems.PERK_ITEMS.getOrDefault(ModPerks.CUPID_ARROW, Items.AIR));
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(ItemTags.ARROWS), Ingredient.of(Items.BOW), Ingredient.of(ItemTags.ARROWS),
                Ingredient.of(HEALING_POTION), Ingredient.of(ModItems.EMPTY_PERK), Ingredient.of(HEALING_POTION),
                Ingredient.of(ItemTags.ARROWS), Ingredient.of(Items.BOW), Ingredient.of(ItemTags.ARROWS));
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
        return ModRecipes.CUPID_ARROW_SERIALIZER;
    }
}
