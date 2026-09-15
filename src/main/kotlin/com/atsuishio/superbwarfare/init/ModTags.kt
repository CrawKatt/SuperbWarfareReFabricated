package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.perk.Perk
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

@Suppress("unused")
object ModTags {
    @JvmStatic
    fun commonItemTag(name: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, ResourceLocation("c", name))
    }

    @JvmStatic
    fun commonBlockTag(name: String): TagKey<Block> {
        return TagKey.create(Registries.BLOCK, ResourceLocation("c", name))
    }

    @JvmStatic
    fun modItemTag(name: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, Mod.loc(name))
    }

    @JvmStatic
    fun modBlockTag(name: String): TagKey<Block> {
        return TagKey.create(Registries.BLOCK, Mod.loc(name))
    }

    @JvmStatic
    fun modDamageTag(name: String): TagKey<DamageType> {
        return TagKey.create(Registries.DAMAGE_TYPE, Mod.loc(name))
    }

    @JvmStatic
    fun modEntityTag(name: String): TagKey<EntityType<*>> {
        return TagKey.create(Registries.ENTITY_TYPE, Mod.loc(name))
    }

    object Items {
        @JvmField val GUN = modItemTag("gun")
        @JvmField val SMG = modItemTag("smg")
        @JvmField val RIFLE = modItemTag("rifle")
        @JvmField val SNIPER_RIFLE = modItemTag("sniper_rifle")
        @JvmField val SHOTGUN = modItemTag("shotgun")
        @JvmField val MACHINE_GUN = modItemTag("machine_gun")
        @JvmField val LAUNCHER = modItemTag("launcher")

        @JvmField val MILITARY_ARMOR = modItemTag("military_armor")
        @JvmField val MILITARY_ARMOR_HEAVY = modItemTag("military_armor_heavy")

        @JvmField val INGOTS_CEMENTED_CARBIDE = modItemTag("ingots/cemented_carbide")
        @JvmField val INGOTS_STEEL = modItemTag("ingots/steel")
        @JvmField val INGOTS_LEAD = modItemTag("ingots/lead")
        @JvmField val STORAGE_BLOCK_STEEL = modItemTag("storage_blocks/steel")
        @JvmField val STORAGE_BLOCK_CEMENTED_CARBIDE = modItemTag("storage_blocks/cemented_carbide")

        @JvmField val BLUEPRINT = modItemTag("blueprint")
        @JvmField val COMMON_BLUEPRINT = modItemTag("blueprint/common")
        @JvmField val RARE_BLUEPRINT = modItemTag("blueprint/rare")
        @JvmField val EPIC_BLUEPRINT = modItemTag("blueprint/epic")
        @JvmField val LEGENDARY_BLUEPRINT = modItemTag("blueprint/legendary")
        @JvmField val SUPERB_BLUEPRINT = modItemTag("blueprint/superb")
        @JvmField val VIRTUAL_BLUEPRINT = modItemTag("blueprint/virtual")
        @JvmField val CANNON_BLUEPRINT = modItemTag("blueprint/cannon")

        @JvmField val ENLARGED_COMMON_BLUEPRINT = modItemTag("blueprint/enlarged/common")
        @JvmField val ENLARGED_RARE_BLUEPRINT = modItemTag("blueprint/enlarged/rare")
        @JvmField val ENLARGED_EPIC_BLUEPRINT = modItemTag("blueprint/enlarged/epic")
        @JvmField val ENLARGED_LEGENDARY_BLUEPRINT = modItemTag("blueprint/enlarged/legendary")

        @JvmField val AMMO_PERK = modItemTag("perk/ammo")
        @JvmField val FUNCTIONAL_PERK = modItemTag("perk/functional")
        @JvmField val DAMAGE_PERK = modItemTag("perk/damage")

        @JvmField val RESEARCHABLE_AMMO_PERK = modItemTag("perk/researchable/ammo")
        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK = modItemTag("perk/researchable/functional")
        @JvmField val RESEARCHABLE_DAMAGE_PERK = modItemTag("perk/researchable/damage")

        @JvmField val RESEARCHABLE_AMMO_PERK_COMMON = modItemTag("perk/researchable/ammo/common")
        @JvmField val RESEARCHABLE_AMMO_PERK_RARE = modItemTag("perk/researchable/ammo/rare")
        @JvmField val RESEARCHABLE_AMMO_PERK_EPIC = modItemTag("perk/researchable/ammo/epic")
        @JvmField val RESEARCHABLE_AMMO_PERK_LEGENDARY = modItemTag("perk/researchable/ammo/legendary")
        @JvmField val RESEARCHABLE_AMMO_PERK_SUPERB = modItemTag("perk/researchable/ammo/superb")

        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK_COMMON = modItemTag("perk/researchable/functional/common")
        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK_RARE = modItemTag("perk/researchable/functional/rare")
        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK_EPIC = modItemTag("perk/researchable/functional/epic")
        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK_LEGENDARY = modItemTag("perk/researchable/functional/legendary")
        @JvmField val RESEARCHABLE_FUNCTIONAL_PERK_SUPERB = modItemTag("perk/researchable/functional/superb")

        @JvmField val RESEARCHABLE_DAMAGE_PERK_COMMON = modItemTag("perk/researchable/damage/common")
        @JvmField val RESEARCHABLE_DAMAGE_PERK_RARE = modItemTag("perk/researchable/damage/rare")
        @JvmField val RESEARCHABLE_DAMAGE_PERK_EPIC = modItemTag("perk/researchable/damage/epic")
        @JvmField val RESEARCHABLE_DAMAGE_PERK_LEGENDARY = modItemTag("perk/researchable/damage/legendary")
        @JvmField val RESEARCHABLE_DAMAGE_PERK_SUPERB = modItemTag("perk/researchable/damage/superb")

        // Attachment tag
        @JvmField val ATTACHMENT = modItemTag("attachment")

        @JvmField val ATTACHMENT_SCOPE = modItemTag("attachment/scope")
        @JvmField val ATTACHMENT_MAGAZINE = modItemTag("attachment/magazine")
        @JvmField val ATTACHMENT_BARREL = modItemTag("attachment/barrel")
        @JvmField val ATTACHMENT_STOCK = modItemTag("attachment/stock")
        @JvmField val ATTACHMENT_GRIP = modItemTag("attachment/grip")

        @JvmField val ATTACHMENT_SCOPE_COMMON = modItemTag("attachment/scope/common")
        @JvmField val ATTACHMENT_SCOPE_RARE = modItemTag("attachment/scope/rare")
        @JvmField val ATTACHMENT_SCOPE_EPIC = modItemTag("attachment/scope/epic")
        @JvmField val ATTACHMENT_SCOPE_LEGENDARY = modItemTag("attachment/scope/legendary")
        @JvmField val ATTACHMENT_SCOPE_SUPERB = modItemTag("attachment/scope/superb")
        @JvmField val ATTACHMENT_SCOPE_VIRTUAL = modItemTag("attachment/scope/virtual")

        @JvmField val ATTACHMENT_MAGAZINE_COMMON = modItemTag("attachment/magazine/common")
        @JvmField val ATTACHMENT_MAGAZINE_RARE = modItemTag("attachment/magazine/rare")
        @JvmField val ATTACHMENT_MAGAZINE_EPIC = modItemTag("attachment/magazine/epic")
        @JvmField val ATTACHMENT_MAGAZINE_LEGENDARY = modItemTag("attachment/magazine/legendary")
        @JvmField val ATTACHMENT_MAGAZINE_SUPERB = modItemTag("attachment/magazine/superb")
        @JvmField val ATTACHMENT_MAGAZINE_VIRTUAL = modItemTag("attachment/magazine/virtual")

        @JvmField val ATTACHMENT_BARREL_COMMON = modItemTag("attachment/barrel/common")
        @JvmField val ATTACHMENT_BARREL_RARE = modItemTag("attachment/barrel/rare")
        @JvmField val ATTACHMENT_BARREL_EPIC = modItemTag("attachment/barrel/epic")
        @JvmField val ATTACHMENT_BARREL_LEGENDARY = modItemTag("attachment/barrel/legendary")
        @JvmField val ATTACHMENT_BARREL_SUPERB = modItemTag("attachment/barrel/superb")
        @JvmField val ATTACHMENT_BARREL_VIRTUAL = modItemTag("attachment/barrel/virtual")

        @JvmField val ATTACHMENT_STOCK_COMMON = modItemTag("attachment/stock/common")
        @JvmField val ATTACHMENT_STOCK_RARE = modItemTag("attachment/stock/rare")
        @JvmField val ATTACHMENT_STOCK_EPIC = modItemTag("attachment/stock/epic")
        @JvmField val ATTACHMENT_STOCK_LEGENDARY = modItemTag("attachment/stock/legendary")
        @JvmField val ATTACHMENT_STOCK_SUPERB = modItemTag("attachment/stock/superb")
        @JvmField val ATTACHMENT_STOCK_VIRTUAL = modItemTag("attachment/stock/virtual")

        @JvmField val ATTACHMENT_GRIP_COMMON = modItemTag("attachment/grip/common")
        @JvmField val ATTACHMENT_GRIP_RARE = modItemTag("attachment/grip/rare")
        @JvmField val ATTACHMENT_GRIP_EPIC = modItemTag("attachment/grip/epic")
        @JvmField val ATTACHMENT_GRIP_LEGENDARY = modItemTag("attachment/grip/legendary")
        @JvmField val ATTACHMENT_GRIP_SUPERB = modItemTag("attachment/grip/superb")
        @JvmField val ATTACHMENT_GRIP_VIRTUAL = modItemTag("attachment/grip/virtual")

        @JvmField val ATTACHMENT_RESEARCHABLE_COMMON = modItemTag("attachment/researchable/common")
        @JvmField val ATTACHMENT_RESEARCHABLE_RARE = modItemTag("attachment/researchable/rare")
        @JvmField val ATTACHMENT_RESEARCHABLE_EPIC = modItemTag("attachment/researchable/epic")
        @JvmField val ATTACHMENT_RESEARCHABLE_LEGENDARY = modItemTag("attachment/researchable/legendary")
        @JvmField val ATTACHMENT_RESEARCHABLE_SUPERB = modItemTag("attachment/researchable/superb")
        @JvmField val ATTACHMENT_RESEARCHABLE_VIRTUAL = modItemTag("attachment/researchable/virtual")

        @JvmField val HAMMER = modItemTag("hammer")
        @JvmField val WRENCHES = commonItemTag("wrenches")
        @JvmField val TOOLS_WRENCH = commonItemTag("tools/wrench")
        @JvmField val TOOLS_CROWBAR = commonItemTag("tools/crowbar")
        @JvmField val TOOLS_HAMMER = commonItemTag("tools/hammer")

        @JvmField val RESEARCH_FUEL = modItemTag("research_fuel")

        @JvmField val ANIMATED_PISTOL = modItemTag("animated/pistol")
        @JvmField val ANIMATED_SNIPER = modItemTag("animated/sniper")
        @JvmField val ANIMATED_RIFLE = modItemTag("animated/rifle")
        @JvmField val ANIMATED_SHOTGUN = modItemTag("animated/shotgun")
        @JvmField val ANIMATED_SMG = modItemTag("animated/smg")
        @JvmField val ANIMATED_RPG = modItemTag("animated/rpg")
        @JvmField val ANIMATED_MG = modItemTag("animated/mg")
        @JvmField val ANIMATED_MINIGUN = modItemTag("animated/minigun")

        // Common/convention tags
        @JvmField val DUSTS = commonItemTag("dusts")
        @JvmField val INGOTS = commonItemTag("ingots")
        @JvmField val STORAGE_BLOCKS = commonItemTag("storage_blocks")
        @JvmField val ORES = commonItemTag("ores")
        @JvmField val RAW_MATERIALS = commonItemTag("raw_materials")
        @JvmField val ORE_RATES_SINGULAR = commonItemTag("ore_rates/singular")
        @JvmField val ORES_IN_GROUND_STONE = commonItemTag("ores_in_ground/stone")
        @JvmField val ORES_IN_GROUND_DEEPSLATE = commonItemTag("ores_in_ground/deepslate")
        @JvmField val ARMORS = commonItemTag("armors")
        @JvmField val INGOTS_GOLD = commonItemTag("gold_ingots")
        @JvmField val INGOTS_IRON = commonItemTag("iron_ingots")
        @JvmField val INGOTS_COPPER = commonItemTag("copper_ingots")
        @JvmField val INGOTS_NETHERITE = commonItemTag("netherite_ingots")
        @JvmField val STORAGE_BLOCKS_IRON = commonItemTag("storage_blocks/iron")
        @JvmField val STORAGE_BLOCKS_GOLD = commonItemTag("storage_blocks/gold")
        @JvmField val STORAGE_BLOCKS_DIAMOND = commonItemTag("storage_blocks/diamond")
        @JvmField val STORAGE_BLOCKS_NETHERITE = commonItemTag("storage_blocks/netherite")
        @JvmField val STORAGE_BLOCKS_LAPIS = commonItemTag("storage_blocks/lapis")
        @JvmField val STORAGE_BLOCKS_COPPER = commonItemTag("storage_blocks/copper")
        @JvmField val STORAGE_BLOCKS_REDSTONE = commonItemTag("storage_blocks/redstone")
        @JvmField val NUGGETS_IRON = commonItemTag("nuggets/iron")
        @JvmField val NUGGETS_GOLD = commonItemTag("nuggets/gold")
        @JvmField val GEMS_DIAMOND = commonItemTag("diamonds")
        @JvmField val GEMS_AMETHYST = commonItemTag("gems/amethyst")
        @JvmField val GEMS_EMERALD = commonItemTag("emeralds")
        @JvmField val GEMS_LAPIS = commonItemTag("lapis")
        @JvmField val GEMS_QUARTZ = commonItemTag("quartz")
        @JvmField val DUSTS_REDSTONE = commonItemTag("redstone_dusts")
        @JvmField val GLASS_BLOCKS = commonItemTag("glass_blocks")
        @JvmField val GLASS_PANES = commonItemTag("glass_panes")
        @JvmField val SANDS = commonItemTag("sands")
        @JvmField val DYES_BLACK = commonItemTag("black_dyes")
        @JvmField val DYES_GREEN = commonItemTag("green_dyes")
        @JvmField val ORES_NETHERITE_SCRAP = commonItemTag("ores/netherite_scrap")
        @JvmField val CHESTS_ENDER = commonItemTag("chests/ender")
        @JvmField val CHESTS_WOODEN = commonItemTag("chests/wooden")
        @JvmField val ENDER_PEARLS = commonItemTag("ender_pearls")
    }

    object Blocks {
        @JvmField
        val MINEABLE_WITH_MILITARY_SHOVEL = modBlockTag("mineable/military_shovel")

        @JvmField
        val GLASS_BLOCKS = commonBlockTag("glass_blocks")

        @JvmField
        val GLASS_PANES = commonBlockTag("glass_panes")

        @JvmField
        val ORES = commonBlockTag("ores")

        @JvmField
        val ORES_IN_GROUND_STONE = commonBlockTag("ores_in_ground/stone")

        @JvmField
        val ORES_IN_GROUND_DEEPSLATE = commonBlockTag("ores_in_ground/deepslate")

        @JvmField
        val SOFT_COLLISION = modBlockTag("soft_collision")

        @JvmField
        val NORMAL_COLLISION = modBlockTag("normal_collision")

        @JvmField
        val HARD_COLLISION = modBlockTag("hard_collision")

        // 子弹会穿过的方块
        @JvmField
        val BULLET_IGNORE = modBlockTag("bullet_ignore")

        // 子弹会破坏的方块
        @JvmField
        val BULLET_CAN_DESTROY = modBlockTag("bullet_can_destroy")

        // 炮射霰弹会破坏的反馈过
        @JvmField
        val CANNON_SHOT_CAN_DESTROY = modBlockTag("cannon_shot_can_destroy")

        // 辅助降落可识别的方块
        @JvmField
        val AUTO_LANDING = modBlockTag("auto_landing")

        // 载具可以穿过的方块
        @JvmField
        val VEHICLE_PASS_THROUGH = modBlockTag("vehicle_pass_through")
    }

    object DamageTypes {
        @JvmField val PROJECTILE = modDamageTag("projectile")
        @JvmField val PROJECTILE_ABSOLUTE = modDamageTag("projectile_absolute")

        @JvmField val VEHICLE_IGNORE = modDamageTag("vehicle_ignore")
        @JvmField val VEHICLE_NOT_ABSORB = modDamageTag("vehicle_not_absorb")
        @JvmField val VEHICLE_IMMUNE = modDamageTag("vehicle_immune")

        @JvmField val GUN_DAMAGE = modDamageTag("gun_damage")

        // 能够由卓越前线的枪械造成的伤害，可用于进度的伤害类型判断
        @JvmField
        val SBW_GUN_FIRE_DAMAGE = modDamageTag("sbw_gun_fire_damage")

        // 载具减伤不会计算的伤害类型
        @JvmField
        val BYPASSES_VEHICLE = modDamageTag("bypasses_vehicle")

        // 没有任何受伤提示的伤害类型
        @JvmField
        val NO_HURT_EFFECT = modDamageTag("no_hurt_effect")
    }

    object EntityTypes {
        @JvmField val AERIAL_BOMB = modEntityTag("aerial_bomb")
        @JvmField val DESTROYABLE_PROJECTILE = modEntityTag("destroyable_projectile")
        @JvmField val DECOY = modEntityTag("decoy")
        @JvmField val NO_EXPERIENCE = modEntityTag("no_experience")
        @JvmField val CAN_REPAIR = modEntityTag("can_repair")
        @JvmField val MINE = modEntityTag("mine")
        @JvmField val AT_ROCKET = modEntityTag("at_rocket")
        @JvmField val AA_MISSILE = modEntityTag("aa_missile")
        @JvmField val SEEK_BLACKLIST = modEntityTag("seek_blacklist")
        @JvmField val BIOGAS_GENERATOR_WHITELIST = modEntityTag("biogas_generator_whitelist")
        @JvmField val SENPAI = modEntityTag("senpai")
    }

    object Perks {
        @JvmField
        val TEST: TagKey<Perk> = TagKey.create(ModPerks.PERK_KEY, Mod.loc("test"))
    }

    @JvmStatic
    fun init() {
    }
}
