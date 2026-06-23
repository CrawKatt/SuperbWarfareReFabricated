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

class CustomSmokeOption(
    val red: Float,
    val green: Float,
    val blue: Float
) : ParticleOptions {

    override fun getType(): ParticleType<*> {
        return ModParticleTypes.CUSTOM_SMOKE
    }

    companion object {
        @JvmField
        val CODEC: MapCodec<CustomSmokeOption> = RecordCodecBuilder.mapCodec { builder ->
            builder.group(
                Codec.FLOAT.fieldOf("r").forGetter { option: CustomSmokeOption -> option.red },
                Codec.FLOAT.fieldOf("g").forGetter { option: CustomSmokeOption -> option.green },
                Codec.FLOAT.fieldOf("b").forGetter { option: CustomSmokeOption -> option.blue }
            ).apply(builder) { red, green, blue ->
                CustomSmokeOption(red, green, blue)
            }
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, CustomSmokeOption> = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            CustomSmokeOption::red,
            ByteBufCodecs.FLOAT,
            CustomSmokeOption::green,
            ByteBufCodecs.FLOAT,
            CustomSmokeOption::blue,
            ::CustomSmokeOption
        )
    }
}