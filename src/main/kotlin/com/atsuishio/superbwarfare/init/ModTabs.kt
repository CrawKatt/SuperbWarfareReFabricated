package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.api.event.RegisterContainersEvent
import com.atsuishio.superbwarfare.item.container.LuckyContainerBlockItem
import com.atsuishio.superbwarfare.item.container.SmallContainerBlockItem
import com.atsuishio.superbwarfare.item.material.BatteryItem
import com.atsuishio.superbwarfare.item.misc.ArmorPlateItem
import com.atsuishio.superbwarfare.item.projectile.C4BombItem
import com.atsuishio.superbwarfare.item.weapon.ElectricBatonItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import team.reborn.energy.api.EnergyStorage
import java.util.function.Supplier

@Suppress("unused")
object ModTabs {
    @JvmField
    val GUN_TAB: CreativeModeTab = Registration.creativeTab("guns") {
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.guns"))
            .icon { ItemStack(ModItems.TASER) }
            .displayItems { _, output ->
                ModItems.GUNS.forEach { item ->
                    if (item === ModItems.VEHICLE_GUN || item === ModItems.EMPTY_GUN) return@forEach
                    output.accept(ItemStack(item))

                    val charged = ItemStack(item)
                    val storage = EnergyStorage.ITEM.find(charged, null)
                    if (storage != null && storage.capacity > 0) {
                        Transaction.openOuter().use { transaction ->
                            storage.insert(Long.MAX_VALUE, transaction)
                            transaction.commit()
                        }
                        output.accept(charged)
                    }
                }
            }
            .build()
    }

    @JvmField
    val ATTACHMENT_TAB: CreativeModeTab = Registration.creativeTab("attachment") {
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.attachment"))
            .icon { ItemStack(ModItems.OEM_STOCK_STANDARD) }
            .displayItems { _, output ->
                ModItems.ATTACHMENTS.forEach(output::accept)
            }
            .build()
    }

    @JvmField
    val PERK_TAB: CreativeModeTab = Registration.creativeTab("perk") {
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.perk"))
            .icon { ItemStack(ModItems.AP_BULLET) }
            .displayItems { _, output ->
                output.accept(ModItems.REFORGING_TABLE)
                ModItems.PERKS.forEach(output::accept)
            }
            .build()
    }

    @JvmField
    val AMMO_TAB: CreativeModeTab = Registration.creativeTab("ammo") {
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.ammo"))
            .icon { ItemStack(ModItems.SHOTGUN_AMMO_BOX) }
            .displayItems { parameters, output ->
                ModItems.AMMO.forEach { item ->
                    if (item === ModItems.POTION_MORTAR_SHELL) return@forEach
                    output.accept(item)
                    if (item === ModItems.C4_BOMB) {
                        output.accept(C4BombItem.makeInstance())
                    }
                }

                parameters.holders().lookup(Registries.POTION).ifPresent { potions ->
                    generatePotionEffectTypes(output, potions, ModItems.POTION_MORTAR_SHELL)
                }
            }
            .build()
    }

    @JvmField
    val ITEM_TAB: CreativeModeTab = Registration.creativeTab("item") {
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.item"))
            .icon { ItemStack(ModItems.TARGET_DEPLOYER) }
            .displayItems { _, output ->
                ModItems.ITEMS.forEach { item ->
                    output.accept(item)

                    if (item === ModItems.ARMOR_PLATE) {
                        output.accept(ArmorPlateItem.getInfiniteInstance())
                    }
                    if (item is BatteryItem) {
                        output.accept(item.makeFullEnergyStack())
                    }
                    if (item === ModItems.ELECTRIC_BATON) {
                        output.accept(ElectricBatonItem.makeFullEnergyStack())
                    }
                }
            }
            .build()
    }

    @JvmField
    val BLOCK_TAB: CreativeModeTab = Registration.creativeTab("block") {
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.block"))
            .icon { ItemStack(ModItems.SANDBAG) }
            .displayItems { _, output ->
                ModItems.BLOCKS.forEach(output::accept)
            }
            .build()
    }

    @JvmField
    val VEHICLE_TAB: CreativeModeTab = Registration.creativeTab("vehicle") {
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.vehicle"))
            .icon { ItemStack(ModItems.CONTAINER) }
            .displayItems { _, output ->
                output.accept(ModItems.CROWBAR)
                output.accept(ModItems.VEHICLE_ASSEMBLING_TABLE)

                RegisterContainersEvent.CONTAINERS.forEach { output.acceptSingle(it) }

                output.accept(ModItems.LUCKY_CONTAINER)
                LuckyContainerBlockItem.LUCKY_CONTAINERS.asSequence()
                    .map { it() }
                    .forEach { output.acceptSingle(it) }

                output.accept(ModItems.SMALL_CONTAINER)
                SmallContainerBlockItem.SMALL_CONTAINERS.asSequence()
                    .map { it() }
                    .forEach { output.acceptSingle(it) }
            }
            .build()
    }

    @JvmStatic
    fun init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register { entries ->
            entries.accept(ModItems.SENPAI_SPAWN_EGG)
            entries.accept(ModItems.CREEPING_SENPAI_SPAWN_EGG)
            entries.accept(ModItems.STEEL_COIL_SPAWN_EGG)
        }
    }

    private fun CreativeModeTab.Output.acceptSingle(stack: ItemStack) {
        if (!stack.isEmpty) {
            accept(stack.copyWithCount(1))
        }
    }

    private fun generatePotionEffectTypes(
        output: CreativeModeTab.Output,
        potions: HolderLookup<Potion>,
        potionItem: Item
    ) {
        potions.listElements()
            .filter { !it.`is`(Potions.EMPTY_ID) }
            .map { PotionUtils.setPotion(ItemStack(potionItem), it.value()) }
            .forEach(output::accept)
    }
}
