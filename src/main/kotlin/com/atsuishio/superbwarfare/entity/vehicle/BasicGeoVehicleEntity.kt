package com.atsuishio.superbwarfare.entity.vehicle

import com.atsuishio.superbwarfare.client.animation.entity.VehicleAnimationInstance
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.baked.BakedBedrockModel
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BoneState
import net.minecraft.resources.ResourceLocation
import java.util.regex.Pattern

data class VehicleModelEntry(
    val instance: BakedModelInstance,
    val texture: ResourceLocation,
    val emissiveTexture: ResourceLocation?,
    val lodDistance: Int,  // 0 = main model, > 0 = LOD
) {
    /** Pre-computed bone groups from the [VehicleModelInstance]. */
    val boneGroups: ModelBoneGroups get() = (instance as VehicleModelInstance).boneGroups
    fun isLOD() = lodDistance > 0
}

/**
 * A [BakedModelInstance] subclass that pre-computes bone groups at construction time,
 * so the renderer can access them without any cache or per-frame regex matching.
 */
class VehicleModelInstance(base: BakedBedrockModel) : BakedModelInstance(base) {
    val boneGroups: ModelBoneGroups = ModelBoneGroups.compute(this)
}

data class ModelBoneGroups(
    val leftWheels: List<BoneState>,
    val rightWheels: List<BoneState>,
    val leftWheelsTurn: List<BoneState>,
    val rightWheelsTurn: List<BoneState>,
    val leftTrackMove: List<BoneState>,
    val leftTrackRot: List<BoneState>,
    val rightTrackMove: List<BoneState>,
    val rightTrackRot: List<BoneState>,
    val shell: List<BoneState>,
    val flareBones: List<BoneState>,
    val laserBones: List<BoneState>,
    val dogTagBones: List<BoneState>,
) {
    companion object {
        val WHEEL_PATTERN: Pattern = Pattern.compile("^wheel(?<direction>[LR]).*$")
        val W_PATTERN: Pattern = Pattern.compile("^w_(?<direction>[lLrR]).*$")
        val SHELL_PATTERN: Pattern = Pattern.compile("^shell(?<id>\\d+)$")
        val TRACK_PATTERN: Pattern = Pattern.compile("^track(?<type>Mov|Rot)(?<direction>[LR])(?<id>\\d+)$")
        val FLARE_PATTERN: Pattern = Pattern.compile("^flare.*")
        val LASER_PATTERN: Pattern = Pattern.compile("^laser.*")
        val DOG_TAG_PATTERN: Pattern = Pattern.compile("^.*_dogTag_(?<w>\\d+)x(?<h>\\d+)$")

        /**
         * Parses the dog tag quad size from a bone name like "body_back_dogTag_10x10".
         * Returns a pair of (width, height) in block units (Bedrock units / 16),
         * or null if the name doesn't match.
         */
        @JvmStatic
        fun parseDogTagSize(boneName: String): Pair<Float, Float>? {
            val matcher = DOG_TAG_PATTERN.matcher(boneName)
            if (!matcher.matches()) return null
            val w = matcher.group("w")?.toFloatOrNull() ?: return null
            val h = matcher.group("h")?.toFloatOrNull() ?: return null
            return Pair(w / 16f, h / 16f)
        }

        @JvmStatic
        fun compute(instance: BakedModelInstance): ModelBoneGroups {
            val leftWheels = mutableListOf<BoneState>()
            val rightWheels = mutableListOf<BoneState>()
            val leftWheelsTurn = mutableListOf<BoneState>()
            val rightWheelsTurn = mutableListOf<BoneState>()

            val tempShell = hashMapOf<Int, BoneState>()

            val leftTrackMove = hashMapOf<Int, BoneState>()
            val leftTrackRot = hashMapOf<Int, BoneState>()
            val rightTrackMove = hashMapOf<Int, BoneState>()
            val rightTrackRot = hashMapOf<Int, BoneState>()

            val flareBones = mutableListOf<BoneState>()
            val laserBones = mutableListOf<BoneState>()

            val dogTagBones = mutableListOf<BoneState>()

            for (bone in instance.boneIndexes) {
                val name = bone.name()

                val wheelMatcher = WHEEL_PATTERN.matcher(name)
                if (wheelMatcher.matches()) {
                    val left = wheelMatcher.group("direction") == "L"
                    val turn = name.endsWith("Turn")

                    if (left) {
                        if (turn) {
                            leftWheelsTurn += bone
                        } else {
                            leftWheels += bone
                        }
                    } else {
                        if (turn) {
                            rightWheelsTurn += bone
                        } else {
                            rightWheels += bone
                        }
                    }
                }

                // Also match w_ prefix wheel bones (e.g. w_lb, w_rb, w_lr, w_rr)
                val wMatcher = W_PATTERN.matcher(name)
                if (wMatcher.matches()) {
                    val left = wMatcher.group("direction").lowercase() == "l"
                    val turn = name.endsWith("Turn")

                    if (left) {
                        if (turn) {
                            leftWheelsTurn += bone
                        } else {
                            leftWheels += bone
                        }
                    } else {
                        if (turn) {
                            rightWheelsTurn += bone
                        } else {
                            rightWheels += bone
                        }
                    }
                }

                val shellMatcher = SHELL_PATTERN.matcher(name)
                if (shellMatcher.matches()) {
                    val index = shellMatcher.group("id").toInt()
                    tempShell[index] = bone
                }

                val trackMatcher = TRACK_PATTERN.matcher(name)
                if (trackMatcher.matches()) {
                    val isRot = trackMatcher.group("type") == "Rot"
                    val isL = trackMatcher.group("direction") == "L"
                    val index = trackMatcher.group("id").toInt()

                    if (isRot) {
                        if (isL) {
                            leftTrackRot[index] = bone
                        } else {
                            rightTrackRot[index] = bone
                        }
                    } else {
                        if (isL) {
                            leftTrackMove[index] = bone
                        } else {
                            rightTrackMove[index] = bone
                        }
                    }
                }

                val flareMatcher = FLARE_PATTERN.matcher(name)
                if (flareMatcher.matches()) {
                    flareBones += bone
                }

                val laserMatcher = LASER_PATTERN.matcher(name)
                if (laserMatcher.matches()) {
                    laserBones += bone
                }

                val dogTagMatcher = DOG_TAG_PATTERN.matcher(name)
                if (dogTagMatcher.matches()) {
                    dogTagBones += bone
                }
            }

            return ModelBoneGroups(
                leftWheels = leftWheels,
                rightWheels = rightWheels,
                leftWheelsTurn = leftWheelsTurn,
                rightWheelsTurn = rightWheelsTurn,
                leftTrackMove = leftTrackMove.toSortedMap().values.toMutableList(),
                leftTrackRot = leftTrackRot.toSortedMap().values.toMutableList(),
                rightTrackMove = rightTrackMove.toSortedMap().values.toMutableList(),
                rightTrackRot = rightTrackRot.toSortedMap().values.toMutableList(),
                shell = tempShell.toSortedMap().values.toMutableList(),
                flareBones = flareBones,
                laserBones = laserBones,
                dogTagBones = dogTagBones,
            )
        }
    }
}

interface BasicGeoVehicleEntity {
    fun getAnimationInstance(): VehicleAnimationInstance<*>? = null

    fun getModelEntries(): List<VehicleModelEntry> = emptyList()
}
