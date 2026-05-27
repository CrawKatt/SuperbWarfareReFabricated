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

    public static <T extends Item> Supplier<T> item(String name, Supplier<T> item) {
        var registered = Registry.register(BuiltInRegistries.ITEM, Mod.loc(name), item.get());
        return () -> registered;
    }

    public static <T extends Block> Supplier<T> block(String name, Supplier<T> block) {
        var registered = Registry.register(BuiltInRegistries.BLOCK, Mod.loc(name), block.get());
        return () -> registered;
    }

    public static <T extends EntityType<?>> Supplier<T> entity(String name, Supplier<T> type) {
        var registered = Registry.register(BuiltInRegistries.ENTITY_TYPE, Mod.loc(name), type.get());
        return () -> registered;
    }

    public static <T extends BlockEntityType<?>> Supplier<T> blockEntity(String name, Supplier<T> type) {
        var registered = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Mod.loc(name), type.get());
        return () -> registered;
    }

    public static <T extends MenuType<?>> Supplier<T> menu(String name, Supplier<T> type) {
        var registered = Registry.register(BuiltInRegistries.MENU, Mod.loc(name), type.get());
        return () -> registered;
    }

    public static <T extends SoundEvent> Supplier<T> sound(String name, Supplier<T> event) {
        var registered = Registry.register(BuiltInRegistries.SOUND_EVENT, Mod.loc(name), event.get());
        return () -> (T) registered;
    }

    public static <T extends MobEffect> Supplier<T> effect(String name, Supplier<T> effect) {
        var registered = Registry.register(BuiltInRegistries.MOB_EFFECT, Mod.loc(name), effect.get());
        return () -> registered;
    }

    public static <T extends ParticleType<?>> Supplier<T> particle(String name, Supplier<T> type) {
        var registered = Registry.register(BuiltInRegistries.PARTICLE_TYPE, Mod.loc(name), type.get());
        return () -> (T) registered;
    }

    public static <T extends Potion> Supplier<T> potion(String name, Supplier<T> potion) {
        var registered = Registry.register(BuiltInRegistries.POTION, Mod.loc(name), potion.get());
        return () -> registered;
    }

    public static <T extends RecipeSerializer<?>> Supplier<T> recipeSerializer(String name, Supplier<T> serializer) {
        var registered = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Mod.loc(name), serializer.get());
        return () -> registered;
    }

    public static <T extends RecipeType<?>> Supplier<T> recipeType(String name, Supplier<T> type) {
        var registered = Registry.register(BuiltInRegistries.RECIPE_TYPE, Mod.loc(name), type.get());
        return () -> registered;
    }

    public static <T extends Attribute> Supplier<T> attribute(String name, Supplier<T> attribute) {
        var registered = Registry.register(BuiltInRegistries.ATTRIBUTE, Mod.loc(name), attribute.get());
        return () -> registered;
    }

    public static <T extends CreativeModeTab> Supplier<T> creativeTab(String name, Supplier<T> tab) {
        var registered = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Mod.loc(name), tab.get());
        return () -> registered;
    }

    public static <T> Supplier<T> custom(Registry<T> registry, String name, Supplier<T> entry) {
        var registered = Registry.register(registry, Mod.loc(name), entry.get());
        return () -> registered;
    }
}
