package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
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
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import team.reborn.energy.api.EnergyStorage

@Suppress("unused")
object ModTabs {

    private val tabs = mutableMapOf<String, CreativeModeTab>()

    @JvmField
    val GUN_TAB: CreativeModeTab = register(
        "guns",
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
                        Transaction.openOuter().use { t ->
                            storage.insert(Long.MAX_VALUE, t)
                            t.commit()
                        }
                        output.accept(charged)
                    }
                }
            }
            .build()
    )

    @JvmField
    val ATTACHMENT_TAB: CreativeModeTab = register(
        "attachment",
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.attachment"))
            .icon { ItemStack(ModItems.MAGAZINE_EXTEND_PRO) }
            .displayItems { _, output ->
                ModItems.ATTACHMENTS.forEach(output::accept)
            }
            .build()
    )

    @JvmField
    val PERK_TAB: CreativeModeTab = register(
        "perk",
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.perk"))
            .icon { ItemStack(ModItems.AP_BULLET) }
            .displayItems { _, output ->
                output.accept(ModItems.REFORGING_TABLE)
                ModItems.PERK_ITEMS.values.forEach(output::accept)
            }
            .build()
    )

    @JvmField
    val AMMO_TAB: CreativeModeTab = register(
        "ammo",
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.ammo"))
            .icon { ItemStack(ModItems.SHOTGUN_AMMO_BOX) }
            .displayItems { param, output ->
                ModItems.AMMO.forEach { item ->
                    if (item === ModItems.POTION_MORTAR_SHELL) return@forEach

                    output.accept(item)

                    if (item === ModItems.C4_BOMB) {
                        output.accept(C4BombItem.makeInstance())
                    }
                }

                param.holders().lookup(Registries.POTION).ifPresent { potion ->
                    generatePotionEffectTypes(
                        output, potion, ModItems.POTION_MORTAR_SHELL,
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                        param.enabledFeatures()
                    )
                }
            }
            .build()
    )

    @JvmField
    val ITEM_TAB: CreativeModeTab = register(
        "item",
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
    )

    @JvmField
    val BLOCK_TAB: CreativeModeTab = register(
        "block",
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.block"))
            .icon { ItemStack(ModItems.SANDBAG) }
            .displayItems { _, output ->
                ModItems.BLOCKS.forEach { output.accept(it) }
            }
            .build()
    )

    @JvmField
    val VEHICLE_TAB: CreativeModeTab = register(
        "vehicle",
        FabricItemGroup.builder()
            .title(Component.translatable("item_group.superbwarfare.vehicle"))
            .icon { ItemStack(ModItems.CONTAINER) }
            .displayItems { _, output ->
                output.accept(ModItems.CROWBAR)
                output.accept(ModItems.VEHICLE_ASSEMBLING_TABLE)

                RegisterContainersEvent.CONTAINERS.forEach { output.acceptSingle(it) }

                output.accept(ModItems.LUCKY_CONTAINER)
                LuckyContainerBlockItem.LUCKY_CONTAINERS.stream()
                    .map { it() }
                    .forEach { output.acceptSingle(it) }

                output.accept(ModItems.SMALL_CONTAINER)
                SmallContainerBlockItem.SMALL_CONTAINERS.stream()
                    .map { it() }
                    .forEach { output.acceptSingle(it) }
            }
            .build()
    )

    @JvmStatic
    fun init() {
        listOf("vehicle", "block", "item", "ammo", "perk", "attachment", "guns").forEach { name ->
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Mod.loc(name), tabs.getValue(name))
        }

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register { entries ->
            entries.accept(ModItems.SENPAI_SPAWN_EGG)
            entries.accept(ModItems.STEEL_COIL_SPAWN_EGG)
        }
    }

    private fun register(name: String, tab: CreativeModeTab): CreativeModeTab {
        tabs[name] = tab
        return tab
    }

    private fun CreativeModeTab.Output.acceptSingle(stack: ItemStack) {
        if (!stack.isEmpty) {
            accept(stack.copyWithCount(1))
        }
    }

    private fun generatePotionEffectTypes(
        output: CreativeModeTab.Output,
        potions: HolderLookup<Potion>,
        item: Item,
        visibility: CreativeModeTab.TabVisibility,
        requiredFeatures: FeatureFlagSet
    ) {
        potions.listElements()
            .filter { potion -> potion.value().isEnabled(requiredFeatures) }
            .map { potion -> PotionContents.createItemStack(item, potion) }
            .forEach { itemStack -> output.accept(itemStack, visibility) }
    }
}
