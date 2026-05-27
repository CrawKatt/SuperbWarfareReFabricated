package com.atsuishio.superbwarfare.datagen;

public class ModDamageTypeTagProvider {

    /*
    public ModDamageTypeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, Mod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.DamageTypes.PROJECTILE).add(ModDamageTypes.GUN_FIRE, ModDamageTypes.GUN_FIRE_HEADSHOT,
                        DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.THROWN)
                .addOptional(new ResourceLocation("tacz", "bullet"))
                .addOptional(new ResourceLocation("tacz", "bullet_void"))
                .addOptional(new ResourceLocation("virtuarealcraft", "rain_crystal"))
                .addOptional(new ResourceLocation("virtuarealcraft", "rain_shower_butterfly"))
                .addOptional(new ResourceLocation("virtuarealcraft", "sparkle_butterfly"))
                .addOptional(new ResourceLocation("dreamaticvoyage", "blood_crystal"))
                .addOptional(new ResourceLocation("dreamaticvoyage", "leviy_beam"));
        this.tag(ModTags.DamageTypes.PROJECTILE_ABSOLUTE).add(ModDamageTypes.GUN_FIRE_ABSOLUTE, ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE)
                .addOptional(new ResourceLocation("tacz", "bullet_ignore_armor"))
                .addOptional(new ResourceLocation("tacz", "bullet_void_ignore_armor"))
                .addOptional(new ResourceLocation("dreamaticvoyage", "leviy_beam_absolute"));
        this.tag(ModTags.DamageTypes.VEHICLE_IGNORE)
                .addOptional(new ResourceLocation("sona", "injury"));
        this.tag(ModTags.DamageTypes.VEHICLE_NOT_ABSORB)
                .add(DamageTypes.EXPLOSION, DamageTypes.PLAYER_EXPLOSION, ModDamageTypes.CUSTOM_EXPLOSION, ModDamageTypes.MINE, ModDamageTypes.PROJECTILE_EXPLOSION);
        this.tag(ModTags.DamageTypes.VEHICLE_IMMUNE)
                .add(DamageTypes.CACTUS, DamageTypes.SWEET_BERRY_BUSH, DamageTypes.IN_WALL)
                .addOptional(new ResourceLocation("iceandfire", "gorgon"));
        this.tag(ModTags.DamageTypes.GUN_DAMAGE).add(
                ModDamageTypes.GUN_FIRE,
                ModDamageTypes.GUN_FIRE_HEADSHOT,
                ModDamageTypes.GUN_FIRE_ABSOLUTE,
                ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE,
                ModDamageTypes.LASER,
                ModDamageTypes.LASER_HEADSHOT,
                ModDamageTypes.SHOCK,
                ModDamageTypes.BURN,
                ModDamageTypes.REPAIR_TOOL,
                ModDamageTypes.PROJECTILE_HIT,
                ModDamageTypes.PROJECTILE_EXPLOSION
        );

        this.tag(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS).add(ModDamageTypes.PROJECTILE_EXPLOSION, ModDamageTypes.CUSTOM_EXPLOSION,
                ModDamageTypes.PROJECTILE_HIT, ModDamageTypes.GRAPESHOT_HIT, ModDamageTypes.LASER, ModDamageTypes.LASER_HEADSHOT, ModDamageTypes.LASER_STATIC, ModDamageTypes.REPAIR_TOOL);
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(ModDamageTypes.GUN_FIRE_ABSOLUTE, ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE,
                ModDamageTypes.SHOCK, ModDamageTypes.PROJECTILE_HIT, ModDamageTypes.GRAPESHOT_HIT, ModDamageTypes.LASER, ModDamageTypes.LASER_HEADSHOT, ModDamageTypes.LASER_STATIC,
                ModDamageTypes.VEHICLE_STRIKE, ModDamageTypes.VEHICLE_EXPLOSION, ModDamageTypes.AIR_CRASH, ModDamageTypes.REPAIR_TOOL);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(ModDamageTypes.SHOCK);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(ModDamageTypes.GUN_FIRE_ABSOLUTE, ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE,
                ModDamageTypes.SHOCK, ModDamageTypes.PROJECTILE_HIT, ModDamageTypes.GRAPESHOT_HIT, ModDamageTypes.LASER, ModDamageTypes.LASER_HEADSHOT, ModDamageTypes.LASER_STATIC,
                ModDamageTypes.VEHICLE_STRIKE, ModDamageTypes.VEHICLE_EXPLOSION, ModDamageTypes.AIR_CRASH);
        this.tag(DamageTypeTags.IS_EXPLOSION).add(ModDamageTypes.PROJECTILE_EXPLOSION, ModDamageTypes.CUSTOM_EXPLOSION, ModDamageTypes.LUNGE_MINE);
        this.tag(DamageTypeTags.IS_FIRE).add(ModDamageTypes.BURN);
        this.tag(ModTags.DamageTypes.BYPASSES_VEHICLE).add(ModDamageTypes.REPAIR_TOOL);

        this.tag(otherModTag("cataclysm", "bypasses_hurt_time")).add(
                ModDamageTypes.GUN_FIRE_ABSOLUTE,
                ModDamageTypes.GUN_FIRE_HEADSHOT_ABSOLUTE,
                ModDamageTypes.AIR_CRASH,
                ModDamageTypes.BURN,
                ModDamageTypes.REPAIR_TOOL,
                ModDamageTypes.PROJECTILE_HIT,
                ModDamageTypes.GRAPESHOT_HIT,
                ModDamageTypes.CUSTOM_EXPLOSION,
                ModDamageTypes.DRONE_HIT,
                ModDamageTypes.LASER,
                ModDamageTypes.LASER_HEADSHOT,
                ModDamageTypes.LASER_STATIC,
                ModDamageTypes.LUNGE_MINE,
                ModDamageTypes.MINE,
                ModDamageTypes.PROJECTILE_EXPLOSION,
                ModDamageTypes.SHOCK,
                ModDamageTypes.VEHICLE_EXPLOSION,
                ModDamageTypes.VEHICLE_STRIKE
        );
    }

    public static TagKey<DamageType> otherModTag(String modId, String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(modId, name));
    }
    */
}
