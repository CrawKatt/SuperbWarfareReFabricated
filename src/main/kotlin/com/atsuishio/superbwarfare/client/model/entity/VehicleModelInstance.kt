package com.atsuishio.superbwarfare.client.model.entity

import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.baked.BakedBedrockModel
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance

/**
 * A [com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance] subclass that pre-computes bone groups at construction time,
 * so the renderer can access them without any cache or per-frame regex matching.
 */
open class VehicleModelInstance(base: BakedBedrockModel) : BakedModelInstance(base) {
    val boneGroups: VehicleModelBoneGroups = VehicleModelBoneGroups.compute(this)
}