package com.atsuishio.superbwarfare.compat.tacz;

import net.fabricmc.loader.api.FabricLoader;

public class TACZGunEventHandler {

    /*
    public static void entityHurtByTACZGun(EntityHurtByGunEvent.Pre event) {
        if (event.getHurtEntity() instanceof VehicleEntity) {
            event.setHeadshot(false);
        }
    }

    public static boolean hasMod() {
        return FabricLoader.getInstance().isModLoaded("tacz");
    }

    public static boolean compatCondition() {
        var modFile = LoadingModList.get().getModFileById("tacz");
        if (modFile == null) return false;
        DefaultArtifactVersion modVersion = new DefaultArtifactVersion(modFile.versionString());
        return modVersion.compareTo(new DefaultArtifactVersion("1.1.4")) >= 0;
    }

    public static ResourceLocation getTaczCompatIcon(ItemStack stack) {
        if (stack.getItem() instanceof IGun iGun) {
            ResourceLocation gunId = iGun.getGunId(stack);
            GunData gunData = TimelessAPI.getClientGunIndex(gunId).map(ClientGunIndex::getGunData).orElse(null);
            GunDisplayInstance display = TimelessAPI.getGunDisplay(stack).orElse(null);
            if (gunData != null && display != null) {
                return display.getHUDTexture();
            }
        }
        return null;
    }
    */
}
