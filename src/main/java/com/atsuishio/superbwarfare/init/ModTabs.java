package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.energy.ModEnergyApi;
import com.atsuishio.superbwarfare.api.event.RegisterContainersEvent;
import com.atsuishio.superbwarfare.item.ArmorPlate;
import com.atsuishio.superbwarfare.item.BatteryItem;
import com.atsuishio.superbwarfare.item.C4BombItem;
import com.atsuishio.superbwarfare.item.ElectricBaton;
import com.atsuishio.superbwarfare.item.common.container.LuckyContainerBlockItem;
import com.atsuishio.superbwarfare.item.common.container.SmallContainerBlockItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModTabs {

    public static final Supplier<CreativeModeTab> GUN_TAB = Registration.creativeTab("guns",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("item_group.superbwarfare.guns"))
                    .icon(() -> new ItemStack(ModItems.TASER.get()))
                    .displayItems((param, output) -> ModItems.GUNS_LIST.forEach(registryObject -> {
                        if (registryObject == ModItems.VEHICLE_GUN) return;

                        output.accept(registryObject.get());

                        var stack = new ItemStack(registryObject.get());
                        int capacity = ModEnergyApi.getMaxEnergyStored(stack);
                        if (capacity > 0) {
                            ModEnergyApi.receiveEnergy(stack, capacity, false);
                            output.accept(stack);
                        }
                    }))
                    .build());

    public static final Supplier<CreativeModeTab> PERK_TAB = Registration.creativeTab("perk",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                    .title(Component.translatable("item_group.superbwarfare.perk"))
                    .icon(() -> new ItemStack(ModItems.AP_BULLET.get()))
                    .displayItems((param, output) -> {
                        output.accept(ModItems.REFORGING_TABLE.get());
                        ModItems.PERKS_LIST.forEach(registryObject -> output.accept(registryObject.get()));
                    })
                    .build());

    public static final Supplier<CreativeModeTab> AMMO_TAB = Registration.creativeTab("ammo",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
                    .title(Component.translatable("item_group.superbwarfare.ammo"))
                    .icon(() -> new ItemStack(ModItems.SHOTGUN_AMMO_BOX.get()))
                    .displayItems((param, output) -> {
                        ModItems.AMMO_LIST.forEach(registryObject -> {
                            if (registryObject.get() != ModItems.POTION_MORTAR_SHELL.get()) {
                                output.accept(registryObject.get());

                                if (registryObject.get() == ModItems.C4_BOMB.get()) {
                                    output.accept(C4BombItem.makeInstance());
                                }
                            }
                        });

                        param.holders().lookup(net.minecraft.core.registries.Registries.POTION)
                                .ifPresent(potion -> generatePotionEffectTypes(output, potion, ModItems.POTION_MORTAR_SHELL.get()));
                    })
                    .build());

    public static final Supplier<CreativeModeTab> ITEM_TAB = Registration.creativeTab("item",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 3)
                    .title(Component.translatable("item_group.superbwarfare.item"))
                    .icon(() -> new ItemStack(ModItems.TARGET_DEPLOYER.get()))
                    .displayItems((param, output) -> ModItems.ITEMS_LIST.forEach(registryObject -> {
                        var item = registryObject.get();
                        output.accept(item);
                        if (item instanceof BatteryItem batteryItem) {
                            output.accept(batteryItem.makeFullEnergyStack());
                        } else if (item == ModItems.ELECTRIC_BATON.get()) {
                            output.accept(ElectricBaton.makeFullEnergyStack());
                        }
                        if (item == ModItems.ARMOR_PLATE.get()) {
                            output.accept(ArmorPlate.getInfiniteInstance());
                        }
                    }))
                    .build());

    public static final Supplier<CreativeModeTab> BLOCK_TAB = Registration.creativeTab("block",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 4)
                    .title(Component.translatable("item_group.superbwarfare.block"))
                    .icon(() -> new ItemStack(ModItems.SANDBAG.get()))
                    .displayItems((param, output) -> ModItems.BLOCKS_LIST.forEach(registryObject -> output.accept(registryObject.get())))
                    .build());

    public static final Supplier<CreativeModeTab> VEHICLE_TAB = Registration.creativeTab("vehicle",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 5)
                    .title(Component.translatable("item_group.superbwarfare.vehicle"))
                    .icon(() -> new ItemStack(ModItems.CONTAINER.get()))
                    .displayItems((param, output) -> {
                        output.accept(ModItems.CROWBAR.get());
                        output.accept(ModItems.VEHICLE_ASSEMBLING_TABLE.get());

                        RegisterContainersEvent.CONTAINERS.forEach(output::accept);

                        output.accept(ModItems.LUCKY_CONTAINER.get());
                        LuckyContainerBlockItem.LUCKY_CONTAINERS.stream().map(Supplier::get).forEach(output::accept);

                        output.accept(ModItems.SMALL_CONTAINER.get());
                        SmallContainerBlockItem.SMALL_CONTAINERS.stream().map(Supplier::get).forEach(output::accept);
                    })
                    .build());

    private static void generatePotionEffectTypes(CreativeModeTab.Output output, net.minecraft.core.HolderLookup.RegistryLookup<net.minecraft.world.item.alchemy.Potion> potions, net.minecraft.world.item.Item potionItem) {
        potions.listElements().filter(potion -> !potion.is(net.minecraft.world.item.alchemy.Potions.EMPTY_ID))
                .map(potion -> net.minecraft.world.item.alchemy.PotionUtils.setPotion(new ItemStack(potionItem), potion.value()))
                .forEach(output::accept);
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS)
                .register(entries -> entries.accept(ModItems.SENPAI_SPAWN_EGG.get()));
    }
}
