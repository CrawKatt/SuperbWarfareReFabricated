package com.atsuishio.superbwarfare.data.vehicle.subdata

import com.atsuishio.superbwarfare.tools.ParticleTool
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DestroyInfo {
    @JvmField
    @SerialName("CrashPassengers")
    var crashPassengers: Boolean = false

    @JvmField
    @SerialName("ExplodePassengers")
    var explodePassengers: Boolean = true

    @JvmField
    @SerialName("ExplodeBlocks")
    var explodeBlocks: Boolean = true

    @JvmField
    @SerialName("ExplosionDamage")
    var explosionDamage: Float = 0f

    @JvmField
    @SerialName("ExplosionRadius")
    var explosionRadius: Float = 0f

    @JvmField
    @SerialName("ParticleType")
    var particleType: ParticleTool.ParticleType = ParticleTool.ParticleType.MINI

    @JvmField
    @SerialName("SympatheticDetonation")
    var sympatheticDetonation: Boolean = false

    @JvmField
    @SerialName("SympatheticDetonationForce")
    var sympatheticDetonationForce: Float = 1.5f

    @JvmField
    @SerialName("SympatheticDetonationChance")
    var sympatheticDetonationChance: Float = 0.5f

    @JvmField
    @SerialName("NoWreck")
    var noWreck: Boolean = false

    constructor(
        crashPassengers: Boolean,
        explodePassengers: Boolean,
        explodeBlocks: Boolean,
        explosionDamage: Float,
        explosionRadius: Float,
        particleType: ParticleTool.ParticleType
    ) {
        this.crashPassengers = crashPassengers
        this.explodePassengers = explodePassengers
        this.explodeBlocks = explodeBlocks
        this.explosionDamage = explosionDamage
        this.explosionRadius = explosionRadius
        this.particleType = particleType
    }

    constructor()
}