package com.atsuishio.superbwarfare.data.gun

import com.atsuishio.superbwarfare.annotation.ServerOnly
import com.atsuishio.superbwarfare.data.ObjectToList
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedSoundEvent
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.sounds.SoundEvents

@Serializable
class SoundInfo {
    // 正常的开火音效
    @JvmField
    @SerializedName("Fire1P")
    @SerialName("Fire1P")
    var fire1P: SerializedSoundEvent? = null

    @JvmField
    @ServerOnly
    @SerializedName("Fire3P")
    @SerialName("Fire3P")
    var fire3P: SerializedSoundEvent? = null

    @JvmField
    @ServerOnly
    @SerializedName("Fire3PFar")
    @SerialName("Fire3PFar")
    var fire3PFar: SerializedSoundEvent? = null

    @JvmField
    @ServerOnly
    @SerializedName("Fire3PVeryFar")
    @SerialName("Fire3PVeryFar")
    var fire3PVeryFar: SerializedSoundEvent? = null

    // 装备消音器时的开火音效
    @JvmField
    @SerializedName("Fire1PSilent")
    @SerialName("Fire1PSilent")
    var fire1PSilent: SerializedSoundEvent? = null

    @JvmField
    @ServerOnly
    @SerializedName("Fire3PSilent")
    @SerialName("Fire3PSilent")
    var fire3PSilent: SerializedSoundEvent? = null

    @JvmField
    @ServerOnly
    @SerializedName("Fire3PFarSilent")
    @SerialName("Fire3PFarSilent")
    var fire3PFarSilent: SerializedSoundEvent? = null

    @JvmField
    @ServerOnly
    @SerializedName("Fire3PVeryFarSilent")
    @SerialName("Fire3PVeryFarSilent")
    var fire3PVeryFarSilent: SerializedSoundEvent? = null

    // 换弹音效
    @JvmField
    @SerializedName("ReloadNormal")
    @SerialName("ReloadNormal")
    var reloadNormal: SerializedSoundEvent? = null

    @JvmField
    @SerializedName("ReloadEmpty")
    @SerialName("ReloadEmpty")
    var reloadEmpty: SerializedSoundEvent? = null

    @JvmField
    @SerializedName("VehicleReload")
    @SerialName("VehicleReload")
    var vehicleReload: SerializedSoundEvent = SoundEvents.EMPTY

    @JvmField
    @SerializedName("VehicleReload3p")
    @SerialName("VehicleReload3p")
    var vehicleReload3p: SerializedSoundEvent = SoundEvents.EMPTY

    @JvmField
    @SerializedName("VehicleReloadSoundTime")
    var vehicleReloadSoundTime: Int = 0

    @JvmField
    @SerializedName("ReloadPrepare")
    @SerialName("ReloadPrepare")
    var reloadPrepare: SerializedSoundEvent? = null

    @JvmField
    @SerializedName("ReloadPrepareEmpty")
    @SerialName("ReloadPrepareEmpty")
    var reloadPrepareEmpty: SerializedSoundEvent? = null

    @JvmField
    @SerializedName("ReloadPrepareLoad")
    @SerialName("ReloadPrepareLoad")
    var reloadPrepareLoad: SerializedSoundEvent? = null

    @JvmField
    @SerializedName("ReloadLoop")
    @SerialName("ReloadLoop")
    var reloadLoop: SerializedSoundEvent? = null

    @JvmField
    @SerializedName("ReloadEnd")
    @SerialName("ReloadEnd")
    var reloadEnd: SerializedSoundEvent? = null

    @JvmField
    @SerializedName("Bolt")
    @SerialName("Bolt")
    var bolt: SerializedSoundEvent? = null

    @JvmField
    @SerializedName("Change")
    @SerialName("Change")
    var change: SerializedSoundEvent? = null

    @JvmField
    @SerializedName("Locking")
    @SerialName("Locking")
    var locking: SerializedSoundEvent = SoundEvents.EMPTY

    @JvmField
    @SerializedName("Locked")
    @SerialName("Locked")
    var locked: SerializedSoundEvent = SoundEvents.EMPTY

    @JvmField
    @SerializedName("FireSoundInstances")
    @SerialName("FireSoundInstances")
    var fireSoundInstances: SerializedSoundEvent? = null

    // 切枪时应该被中止播放的音效
    @JvmField
    @SerializedName("CancellableSounds")
    @SerialName("CancellableSounds")
    var cancellableSounds: ObjectToList<String> = ObjectToList()
}
