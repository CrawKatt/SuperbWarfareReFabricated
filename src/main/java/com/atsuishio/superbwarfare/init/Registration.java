package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class Registration {

    public static <T extends Item> T item(String name, Supplier<T> item) {
        return Registry.register(BuiltInRegistries.ITEM, Mod.loc(name), item.get());
    }

    public static <T extends Block> T block(String name, Supplier<T> block) {
        return Registry.register(BuiltInRegistries.BLOCK, Mod.loc(name), block.get());
    }

    public static <T extends EntityType<?>> T entity(String name, Supplier<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, Mod.loc(name), type.get());
    }

    public static <T extends BlockEntityType<?>> T blockEntity(String name, Supplier<T> type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Mod.loc(name), type.get());
    }

    public static <T extends MenuType<?>> T menu(String name, Supplier<T> type) {
        return Registry.register(BuiltInRegistries.MENU, Mod.loc(name), type.get());
    }

    public static <T extends SoundEvent> T sound(String name, Supplier<T> event) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, Mod.loc(name), event.get());
    }

    public static <T extends MobEffect> T effect(String name, Supplier<T> effect) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, Mod.loc(name), effect.get());
    }

    public static <T extends ParticleType<?>> T particle(String name, Supplier<T> type) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Mod.loc(name), type.get());
    }

    public static <T extends Potion> T potion(String name, Supplier<T> potion) {
        return Registry.register(BuiltInRegistries.POTION, Mod.loc(name), potion.get());
    }

    public static <T extends RecipeSerializer<?>> T recipeSerializer(String name, Supplier<T> serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Mod.loc(name), serializer.get());
    }

    public static <T extends RecipeType<?>> T recipeType(String name, Supplier<T> type) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, Mod.loc(name), type.get());
    }

    public static <T extends Attribute> T attribute(String name, Supplier<T> attribute) {
        return Registry.register(BuiltInRegistries.ATTRIBUTE, Mod.loc(name), attribute.get());
    }

    public static <T extends CreativeModeTab> T creativeTab(String name, Supplier<T> tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Mod.loc(name), tab.get());
    }

    public static <T> T custom(Registry<T> registry, String name, Supplier<T> entry) {
        return Registry.register(registry, Mod.loc(name), entry.get());
    }
}
