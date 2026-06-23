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

class CustomCloudOption(
    val color: Int,
    val life: Int,
    val size: Float,
    val gravity: Float,
    val cooldown: Boolean,
    val light: Boolean
) : ParticleOptions {
    constructor(
        r: Float,
        g: Float,
        b: Float,
        life: Int,
        size: Float,
        gravity: Float,
        cooldown: Boolean,
        light: Boolean
    ) : this(
        (r * 255).roundToInt() shl 16 or ((g * 255).roundToInt() shl 8) or (b * 255).roundToInt(),
        life,
        size,
        gravity,
        cooldown,
        light
    )

    val red: Float
        get() = (this.color shr 16 and 255) / 255f

    val green: Float
        get() = (this.color shr 8 and 255) / 255f

    val blue: Float
        get() = (this.color and 255) / 255f

    override fun getType(): ParticleType<*> {
        return ModParticleTypes.CUSTOM_CLOUD
    }

    companion object {
        @JvmField
        val CODEC: MapCodec<CustomCloudOption> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codec.INT.fieldOf("color").forGetter { it.color },
                Codec.INT.fieldOf("life").forGetter { it.life },
                Codec.FLOAT.fieldOf("size").forGetter { it.size },
                Codec.FLOAT.fieldOf("gravity").forGetter { it.gravity },
                Codec.BOOL.fieldOf("cooldown").forGetter { it.cooldown },
                Codec.BOOL.fieldOf("light").forGetter { it.light }
            ).apply(builder, ::CustomCloudOption)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, CustomCloudOption> = StreamCodec.composite(
            ByteBufCodecs.INT,
            CustomCloudOption::color,
            ByteBufCodecs.INT,
            CustomCloudOption::life,
            ByteBufCodecs.FLOAT,
            CustomCloudOption::size,
            ByteBufCodecs.FLOAT,
            CustomCloudOption::gravity,
            ByteBufCodecs.BOOL,
            CustomCloudOption::cooldown,
            ByteBufCodecs.BOOL,
            CustomCloudOption::light,
            ::CustomCloudOption
        )
    }
}