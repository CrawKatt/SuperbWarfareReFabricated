package com.atsuishio.superbwarfare.client.particle

import com.atsuishio.superbwarfare.init.ModParticleTypes
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import kotlin.math.roundToInt

data class CannonMuzzleFlareOption(
    val color: Int,
    val life: Int,
    val fade: Float,
    val animationSpeed: Int,
    val sizeAdd: Float
) : ParticleOptions {
    constructor(
        r: Float,
        g: Float,
        b: Float,
        life: Int,
        fade: Float,
        animationSpeed: Int,
        sizeAdd: Float
    ) : this(
        (r * 255).roundToInt() shl 16 or ((g * 255).roundToInt() shl 8) or (b * 255).roundToInt(),
        life,
        fade,
        animationSpeed,
        sizeAdd
    )

    val red get() = (this.color shr 16 and 255) / 255f
    val green get() = (this.color shr 8 and 255) / 255f
    val blue get() = (this.color and 255) / 255f

    override fun getType(): ParticleType<*> = ModParticleTypes.CANNON_MUZZLE_FLARE

    companion object {
        @JvmField
        val CODEC: MapCodec<CannonMuzzleFlareOption> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codec.INT.fieldOf("color").forGetter { it.color },
                Codec.INT.fieldOf("life").forGetter { it.life },
                Codec.FLOAT.fieldOf("fade").forGetter { it.fade },
                Codec.INT.fieldOf("animationSpeed").forGetter { it.animationSpeed },
                Codec.FLOAT.fieldOf("sizeAdd").forGetter { it.sizeAdd }
            ).apply(builder, ::CannonMuzzleFlareOption)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, CannonMuzzleFlareOption> = StreamCodec.composite(
            ByteBufCodecs.INT,
            CannonMuzzleFlareOption::color,
            ByteBufCodecs.INT,
            CannonMuzzleFlareOption::life,
            ByteBufCodecs.FLOAT,
            CannonMuzzleFlareOption::fade,
            ByteBufCodecs.INT,
            CannonMuzzleFlareOption::animationSpeed,
            ByteBufCodecs.FLOAT,
            CannonMuzzleFlareOption::sizeAdd,
            ::CannonMuzzleFlareOption
        )
    }
}