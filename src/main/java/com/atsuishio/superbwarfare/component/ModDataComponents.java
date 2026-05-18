package com.atsuishio.superbwarfare.component;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.data.gun.Ammo;
import com.atsuishio.superbwarfare.item.FiringParameters;
import com.atsuishio.superbwarfare.item.common.ammo.box.AmmoBoxInfo;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;

public class ModDataComponents {
    public static final DataComponentType<FiringParameters.Parameters> FIRING_PARAMETERS = register(
            "firing_parameters",
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            BlockPos.CODEC.fieldOf("pos").forGetter(FiringParameters.Parameters::pos),
                            Codec.INT.fieldOf("radius").forGetter(FiringParameters.Parameters::radius),
                            Codec.BOOL.fieldOf("is_depressed").forGetter(FiringParameters.Parameters::isDepressed)
                    ).apply(instance, FiringParameters.Parameters::new)
            )
    );

    public static final DataComponentType<Integer> ENERGY = register("energy", Codec.INT);

    public static final DataComponentType<List<Pair<Integer, Double>>> TRANSCRIPT_SCORE = register(
            "transcript_score",
            Codec.pair(
                    Codec.INT.fieldOf("score").codec(),
                    Codec.DOUBLE.fieldOf("distance").codec()
            ).listOf()
    );

    public static final DataComponentType<AmmoBoxInfo> AMMO_BOX_INFO = register("ammo_box_info", AmmoBoxInfo.CODEC);

    public static final DataComponentType<List<Short>> DOG_TAG_IMAGE = register("dog_tag_image", Codec.SHORT.listOf());

    private static <T> DataComponentType<T> register(String name, Codec<T> codec) {
        DataComponentType.Builder<T> builder = DataComponentType.builder();
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Mod.loc(name), builder.persistent(codec).build());
    }

    public static void init() {
        for (var type : Ammo.values()) {
            type.dataComponent = register("ammo_" + type.name, Codec.INT);
        }
    }
}
