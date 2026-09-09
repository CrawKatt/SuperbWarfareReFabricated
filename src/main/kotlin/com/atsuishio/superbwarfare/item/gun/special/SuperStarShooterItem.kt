package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.data.gun.GunProp
import com.atsuishio.superbwarfare.init.ModRarities
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.gun.GeoGunItemV2
import com.atsuishio.superbwarfare.tools.playLocalSound
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity

@RegistryName("super_star_shooter")
class SuperStarShooterItem : GeoGunItemV2(Properties().rarity(ModRarities.SUPERB)) {

    override fun tick(shooter: Entity?, data: GunData, inMainHand: Boolean) {
        val level = shooter?.level() ?: return

        if (level.isNight && level.gameTime % 84L == 0L && data.ammo.get() < data.get(GunProp.MAGAZINE)) {
            data.ammo.add(1)

            if (inMainHand && shooter is ServerPlayer) {
                shooter.playLocalSound(
                    ModSounds.STAR_RECOVER,
                    SoundSource.PLAYERS,
                    0.5f,
                    1f
                )
            }
        }
    }
}