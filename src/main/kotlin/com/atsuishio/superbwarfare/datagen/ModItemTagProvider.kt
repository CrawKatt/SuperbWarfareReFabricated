package com.atsuishio.superbwarfare.datagen

import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModPerks
import com.atsuishio.superbwarfare.init.ModTags
import com.atsuishio.superbwarfare.init.ModTags.commonItemTag
import com.atsuishio.superbwarfare.item.misc.PerkItem
import com.atsuishio.superbwarfare.perk.Perk
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.data.tags.TagsProvider.TagLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import java.util.concurrent.CompletableFuture

class ModItemTagProvider(
    packOutput: PackOutput,
    providerCompletableFuture: CompletableFuture<HolderLookup.Provider>,
    tagLookupCompletableFuture: CompletableFuture<TagLookup<Block>>
) : ItemTagsProvider(packOutput, providerCompletableFuture, tagLookupCompletableFuture) {
    override fun addTags(pProvider: HolderLookup.Provider) {
        this.tag(ModTags.Items.DUSTS)
            .addTag(commonItemTag("dusts/coal_coke"))
            .addTag(commonItemTag("dusts/coal"))
            .addTag(commonItemTag("dusts/iron"))
            .addTag(commonItemTag("dusts/tungsten"))
            .addTag(commonItemTag("dusts/scheelite"))
        this.tag(commonItemTag("dusts/coal_coke")).add(ModItems.COAL_POWDER)
        this.tag(commonItemTag("dusts/coal")).add(ModItems.COAL_POWDER)
        this.tag(commonItemTag("dusts/iron")).add(ModItems.IRON_POWDER)
        this.tag(commonItemTag("dusts/tungsten")).add(ModItems.TUNGSTEN_POWDER)
        this.tag(commonItemTag("dusts/scheelite")).add(ModItems.TUNGSTEN_POWDER)

        this.tag(ModTags.Items.INGOTS)
            .addTag(commonItemTag("ingots/lead"))
            .addTag(commonItemTag("ingots/steel"))
            .addTag(commonItemTag("ingots/tungsten"))
            .addTag(commonItemTag("ingots/silver"))
            .addTag(commonItemTag("ingots/scheelite"))
        this.tag(commonItemTag("ingots/lead")).add(ModItems.LEAD_INGOT)
        this.tag(commonItemTag("ingots/steel")).add(ModItems.STEEL_INGOT)
        this.tag(commonItemTag("ingots/tungsten")).add(ModItems.TUNGSTEN_INGOT)
        this.tag(commonItemTag("ingots/silver")).add(ModItems.SILVER_INGOT)
        // 这个tag仅用于其他mod配方兼容，自己家配方不用这个
        this.tag(commonItemTag("ingots/scheelite")).add(ModItems.TUNGSTEN_INGOT)

        this.tag(ModTags.Items.INGOTS_STEEL).addTag(commonItemTag("ingots/steel"))
            .addOptional(ResourceLocation("dreamaticvoyage", "fukamizu_bread_ingot"))
        this.tag(ModTags.Items.INGOTS_LEAD).addTag(commonItemTag("ingots/lead"))

        this.tag(ModTags.Items.INGOTS_CEMENTED_CARBIDE).add(ModItems.CEMENTED_CARBIDE_INGOT)
            .addOptional(ResourceLocation("dreamaticvoyage", "hqss_bread_ingot"))

        this.tag(ModTags.Items.STORAGE_BLOCKS)
            .addTag(commonItemTag("storage_blocks/lead"))
            .addTag(commonItemTag("storage_blocks/steel"))
            .addTag(commonItemTag("storage_blocks/tungsten"))
            .addTag(commonItemTag("storage_blocks/silver"))
            .addTag(commonItemTag("storage_blocks/raw_lead"))
            .addTag(commonItemTag("storage_blocks/raw_tungsten"))
            .addTag(commonItemTag("storage_blocks/raw_silver"))
            .addTag(commonItemTag("storage_blocks/raw_scheelite"))
        this.tag(commonItemTag("storage_blocks/lead")).add(ModItems.LEAD_BLOCK)
        this.tag(commonItemTag("storage_blocks/steel")).add(ModItems.STEEL_BLOCK)
        this.tag(commonItemTag("storage_blocks/tungsten")).add(ModItems.TUNGSTEN_BLOCK)
        this.tag(commonItemTag("storage_blocks/scheelite")).add(ModItems.TUNGSTEN_BLOCK)
        this.tag(commonItemTag("storage_blocks/silver")).add(ModItems.SILVER_BLOCK)

        this.tag(commonItemTag("storage_blocks/raw_lead")).add(ModItems.RAW_GALENA_BLOCK)
        this.tag(commonItemTag("storage_blocks/raw_tungsten")).add(ModItems.RAW_SCHEELITE_BLOCK)
        this.tag(commonItemTag("storage_blocks/raw_scheelite")).add(ModItems.RAW_SCHEELITE_BLOCK)
        this.tag(commonItemTag("storage_blocks/raw_silver")).add(ModItems.RAW_SILVER_BLOCK)

        this.tag(ModTags.Items.STORAGE_BLOCK_STEEL).addTag(commonItemTag("storage_blocks/steel"))
            .addOptional(ResourceLocation("dreamaticvoyage", "fukamizu_bread_bricks"))
        this.tag(ModTags.Items.STORAGE_BLOCK_CEMENTED_CARBIDE).add(ModItems.CEMENTED_CARBIDE_BLOCK)
            .addOptional(ResourceLocation("dreamaticvoyage", "hqss_bread_bricks"))

        this.tag(ModTags.Items.ORES)
            .addTag(commonItemTag("ores/lead"))
            .addTag(commonItemTag("ores/tungsten"))
            .addTag(commonItemTag("ores/scheelite"))
            .addTag(commonItemTag("ores/silver"))
        this.tag(commonItemTag("ores/lead")).add(ModItems.GALENA_ORE, ModItems.DEEPSLATE_GALENA_ORE)
        this.tag(commonItemTag("ores/tungsten"))
            .add(ModItems.SCHEELITE_ORE, ModItems.DEEPSLATE_SCHEELITE_ORE)
        this.tag(commonItemTag("ores/scheelite"))
            .add(ModItems.SCHEELITE_ORE, ModItems.DEEPSLATE_SCHEELITE_ORE)
        this.tag(commonItemTag("ores/silver")).add(ModItems.SILVER_ORE, ModItems.DEEPSLATE_SILVER_ORE)

        this.tag(ModTags.Items.RAW_MATERIALS)
            .addTag(commonItemTag("raw_materials/lead"))
            .addTag(commonItemTag("raw_materials/tungsten"))
            .addTag(commonItemTag("raw_materials/scheelite"))
            .addTag(commonItemTag("raw_materials/silver"))
        this.tag(commonItemTag("raw_materials/lead")).add(ModItems.GALENA)
        this.tag(commonItemTag("raw_materials/tungsten")).add(ModItems.SCHEELITE)
        this.tag(commonItemTag("raw_materials/scheelite")).add(ModItems.SCHEELITE)
        this.tag(commonItemTag("raw_materials/silver")).add(ModItems.RAW_SILVER)

        this.tag(ModTags.Items.ORE_RATES_SINGULAR).add(
            ModItems.GALENA_ORE, ModItems.DEEPSLATE_GALENA_ORE,
            ModItems.SCHEELITE_ORE, ModItems.DEEPSLATE_SCHEELITE_ORE,
            ModItems.SILVER_ORE, ModItems.DEEPSLATE_SILVER_ORE
        )

        this.tag(ModTags.Items.ORES_IN_GROUND_STONE)
            .add(ModItems.GALENA_ORE, ModItems.SCHEELITE_ORE, ModItems.SILVER_ORE)
        this.tag(ModTags.Items.ORES_IN_GROUND_DEEPSLATE).add(
            ModItems.DEEPSLATE_GALENA_ORE,
            ModItems.DEEPSLATE_SCHEELITE_ORE,
            ModItems.DEEPSLATE_SILVER_ORE
        )

        this.tag(commonItemTag("plates")).addTags(commonItemTag("plates/copper"), commonItemTag("plates/steel"), commonItemTag("plates/plastic"))
        this.tag(commonItemTag("plates/copper")).add(ModItems.COPPER_PLATE)
        this.tag(commonItemTag("plates/steel")).add(ModItems.STEEL_PLATE)
        this.tag(commonItemTag("plates/plastic")).add(ModItems.ENGINEERING_PLASTIC)

        this.tag(commonItemTag("tools/crowbar")).add(ModItems.CROWBAR)

        this.tag(ModTags.Items.HAMMER).add(
            ModItems.HAMMER,
            ModItems.GOLDEN_HAMMER,
            ModItems.STEEL_HAMMER,
            ModItems.DIAMOND_HAMMER,
            ModItems.CEMENTED_CARBIDE_HAMMER,
            ModItems.NETHERITE_HAMMER
        )
        this.tag(ModTags.Items.TOOLS_HAMMER).addTag(ModTags.Items.HAMMER)

        this.tag(commonItemTag("armors/helmets"))
            .add(ModItems.RU_HELMET_6B47, ModItems.US_HELMET_PASGT, ModItems.GE_HELMET_M_35)
        this.tag(commonItemTag("armors/chestplates")).add(ModItems.RU_CHEST_6B43, ModItems.US_CHEST_IOTV)

        this.tag(ModTags.Items.RESEARCH_FUEL).add(Items.GUNPOWDER, Items.GLOWSTONE_DUST, Items.REDSTONE, Items.SUGAR)

        // 专门给其他模组添加动画用的枪械武器分类 tag
        this.tag(ModTags.Items.ANIMATED_PISTOL).add(
            ModItems.TASER,
            ModItems.GLOCK_17,
            ModItems.GLOCK_18,
            ModItems.MP_443,
            ModItems.M_1911,
            ModItems.TRACHELIUM,
            ModItems.REPAIR_TOOL
        )
        this.tag(ModTags.Items.ANIMATED_SNIPER).add(
            ModItems.MOSIN_NAGANT,
            ModItems.SVD,
            ModItems.AWM,
            ModItems.NTW_20
        )
        this.tag(ModTags.Items.ANIMATED_RIFLE).add(
            ModItems.AK_47,
            ModItems.AK_12,
            ModItems.SKS,
            ModItems.M_4,
            ModItems.HK_416,
            ModItems.QBZ_95,
            ModItems.QBZ_191,
            ModItems.INSIDIOUS,
            ModItems.MK_14,
            ModItems.MARLIN,
            ModItems.K_98,
            ModItems.M_98B,
            ModItems.SENTINEL,
            ModItems.HUNTING_RIFLE,
            ModItems.QL_1031
        )
        this.tag(ModTags.Items.ANIMATED_SHOTGUN).add(
            ModItems.HOMEMADE_SHOTGUN,
            ModItems.M_870,
            ModItems.AA_12,
            ModItems.M_79,
            ModItems.SECONDARY_CATACLYSM
        )
        this.tag(ModTags.Items.ANIMATED_SMG).add(
            ModItems.MP_5,
            ModItems.VECTOR
        )
        this.tag(ModTags.Items.ANIMATED_RPG).add(
            ModItems.RPG,
            ModItems.JAVELIN,
            ModItems.IGLA_9K38
        )
        this.tag(ModTags.Items.ANIMATED_MG).add(
            ModItems.DEVOTION,
            ModItems.RPK,
            ModItems.M_60,
            ModItems.M_2_HB
        )
        this.tag(ModTags.Items.ANIMATED_MINIGUN).add(
            ModItems.MINIGUN
        )

        ModItems.GUNS.forEach {
            this.tag(ModTags.Items.GUN).add(it)
        }

        this.tag(ModTags.Items.SMG).add(ModItems.VECTOR, ModItems.MP_5)
        this.tag(ModTags.Items.RIFLE).add(
            ModItems.M_4,
            ModItems.HK_416,
            ModItems.SKS,
            ModItems.MK_14,
            ModItems.MARLIN,
            ModItems.AK_47,
            ModItems.AK_12,
            ModItems.QBZ_95,
            ModItems.QBZ_191
        )
        this.tag(ModTags.Items.SNIPER_RIFLE).add(
            ModItems.HUNTING_RIFLE,
            ModItems.SENTINEL,
            ModItems.NTW_20,
            ModItems.SVD,
            ModItems.M_98B,
            ModItems.K_98,
            ModItems.MOSIN_NAGANT,
            ModItems.AWM,
            ModItems.QL_1031
        )
        this.tag(ModTags.Items.SHOTGUN).add(ModItems.HOMEMADE_SHOTGUN, ModItems.M_870, ModItems.AA_12)
        this.tag(ModTags.Items.MACHINE_GUN).add(ModItems.MINIGUN, ModItems.M_2_HB)
        this.tag(ModTags.Items.LAUNCHER).add(
            ModItems.RPG, ModItems.JAVELIN, ModItems.IGLA_9K38,
            ModItems.M_79, ModItems.SECONDARY_CATACLYSM, ModItems.SUPER_STAR_SHOOTER
        )

        this.tag(ModTags.Items.MILITARY_ARMOR).add(ModItems.RU_CHEST_6B43, ModItems.US_CHEST_IOTV)

        this.tag(ModTags.Items.BLUEPRINT)
            .addTag(ModTags.Items.COMMON_BLUEPRINT)
            .addTag(ModTags.Items.RARE_BLUEPRINT)
            .addTag(ModTags.Items.EPIC_BLUEPRINT)
            .addTag(ModTags.Items.LEGENDARY_BLUEPRINT)
            .addTag(ModTags.Items.CANNON_BLUEPRINT)

        this.tag(ModTags.Items.COMMON_BLUEPRINT).add(
            ModItems.GLOCK_17_BLUEPRINT, ModItems.MP_443_BLUEPRINT, ModItems.MARLIN_BLUEPRINT,
            ModItems.TASER_BLUEPRINT, ModItems.M_1911_BLUEPRINT
        )

        this.tag(ModTags.Items.RARE_BLUEPRINT).add(
            ModItems.GLOCK_18_BLUEPRINT,
            ModItems.M_79_BLUEPRINT,
            ModItems.M_4_BLUEPRINT,
            ModItems.SKS_BLUEPRINT,
            ModItems.M_870_BLUEPRINT,
            ModItems.AK_47_BLUEPRINT,
            ModItems.K_98_BLUEPRINT,
            ModItems.MOSIN_NAGANT_BLUEPRINT,
            ModItems.M_2_HB_BLUEPRINT,
            ModItems.HK_416_BLUEPRINT,
            ModItems.AK_12_BLUEPRINT,
            ModItems.QBZ_95_BLUEPRINT,
            ModItems.RPG_BLUEPRINT,
            ModItems.HUNTING_RIFLE_BLUEPRINT
        )

        this.tag(ModTags.Items.EPIC_BLUEPRINT).add(
            ModItems.BOCEK_BLUEPRINT,
            ModItems.RPK_BLUEPRINT,
            ModItems.VECTOR_BLUEPRINT,
            ModItems.MK_14_BLUEPRINT,
            ModItems.M_60_BLUEPRINT,
            ModItems.SVD_BLUEPRINT,
            ModItems.M_98B_BLUEPRINT,
            ModItems.DEVOTION_BLUEPRINT,
            ModItems.INSIDIOUS_BLUEPRINT,
            ModItems.QBZ_191_BLUEPRINT,
            ModItems.AWM_BLUEPRINT,
            ModItems.IGLA_BLUEPRINT,
            ModItems.SENTINEL_BLUEPRINT
        )

        this.tag(ModTags.Items.LEGENDARY_BLUEPRINT).add(
            ModItems.AA_12_BLUEPRINT,
            ModItems.NTW_20_BLUEPRINT,
            ModItems.MINIGUN_BLUEPRINT,
            ModItems.JAVELIN_BLUEPRINT,
            ModItems.MK_42_BLUEPRINT,
            ModItems.MLE_1934_BLUEPRINT,
            ModItems.ANNIHILATOR_BLUEPRINT,
            ModItems.HPJ_11_BLUEPRINT,
            ModItems.BL_132_BLUEPRINT
        )

        this.tag(ModTags.Items.SUPERB_BLUEPRINT).add(ModItems.SUPER_STAR_SHOOTER_BLUEPRINT)

        this.tag(ModTags.Items.VIRTUAL_BLUEPRINT).add(
            ModItems.TRACHELIUM_BLUEPRINT, ModItems.SECONDARY_CATACLYSM_BLUEPRINT, ModItems.QL_1031_BLUEPRINT
        )

        this.tag(ModTags.Items.CANNON_BLUEPRINT).add(
            ModItems.MK_42_BLUEPRINT, ModItems.MLE_1934_BLUEPRINT, ModItems.ANNIHILATOR_BLUEPRINT,
            ModItems.HPJ_11_BLUEPRINT, ModItems.BL_132_BLUEPRINT
        )

        this.tag(ModTags.Items.ENLARGED_COMMON_BLUEPRINT)
            .addTag(ModTags.Items.COMMON_BLUEPRINT)
            .addTag(ModTags.Items.RARE_BLUEPRINT)
        this.tag(ModTags.Items.ENLARGED_RARE_BLUEPRINT)
            .addTag(ModTags.Items.RARE_BLUEPRINT)
            .addTag(ModTags.Items.EPIC_BLUEPRINT)
        this.tag(ModTags.Items.ENLARGED_EPIC_BLUEPRINT)
            .addTag(ModTags.Items.EPIC_BLUEPRINT)
            .addTag(ModTags.Items.LEGENDARY_BLUEPRINT)
        this.tag(ModTags.Items.ENLARGED_LEGENDARY_BLUEPRINT)
            .addTag(ModTags.Items.LEGENDARY_BLUEPRINT)
            .addTag(ModTags.Items.SUPERB_BLUEPRINT)

        this.tag(ItemTags.SWORDS).add(
            ModItems.MILITARY_SHOVEL,
            ModItems.KNIFE,
            ModItems.T_BATON,
            ModItems.ELECTRIC_BATON,
            ModItems.STEEL_PIPE,
            ModItems.CROWBAR,
            ModItems.CEMENTED_CARBIDE_SWORD
        ).addTag(ModTags.Items.HAMMER)

        this.tag(ItemTags.AXES).add(ModItems.MILITARY_SHOVEL, ModItems.CEMENTED_CARBIDE_AXE)
        this.tag(ItemTags.SHOVELS).add(ModItems.MILITARY_SHOVEL, ModItems.CEMENTED_CARBIDE_SHOVEL)
        this.tag(ItemTags.HOES).add(ModItems.MILITARY_SHOVEL, ModItems.CEMENTED_CARBIDE_HOE)
        this.tag(ItemTags.PICKAXES).add(ModItems.CEMENTED_CARBIDE_PICKAXE)


        ModItems.PERKS.forEach { item ->
            if (item is PerkItem) {
                when (item.perk.type) {
                    Perk.Type.AMMO -> {
                        this.tag(ModTags.Items.AMMO_PERK).add(item)
                        if (item.perk != ModPerks.BEAST_BULLET) {
                            this.tag(ModTags.Items.RESEARCHABLE_AMMO_PERK).add(item)
                        }
                    }

                    Perk.Type.FUNCTIONAL -> {
                        this.tag(ModTags.Items.FUNCTIONAL_PERK).add(item)
                        this.tag(ModTags.Items.RESEARCHABLE_FUNCTIONAL_PERK).add(item)
                    }

                    Perk.Type.DAMAGE -> {
                        this.tag(ModTags.Items.DAMAGE_PERK).add(item)
                        this.tag(ModTags.Items.RESEARCHABLE_DAMAGE_PERK).add(item)
                    }
                }
            }
        }
    }
}
