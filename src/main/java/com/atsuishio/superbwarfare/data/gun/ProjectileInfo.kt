package com.atsuishio.superbwarfare.data.gun

import com.atsuishio.superbwarfare.data.DeserializeFromString
import com.atsuishio.superbwarfare.data.IDBasedData
import com.atsuishio.superbwarfare.data.STOFactory
import com.atsuishio.superbwarfare.data.StringInstanceBuilder
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedGsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@STOFactory(ProjectileInfo.ProjectileInfoInstanceBuilder::class)
@Serializable
class ProjectileInfo : IDBasedData<ProjectileInfo>, DeserializeFromString {
    @JvmField
    @SerializedName("Type")
    @SerialName("Type")
    var type: String = "superbwarfare:projectile"

    override fun getId() = type

    override fun setId(id: String) {
        this.type = id
    }

    @JvmField
    @SerializedName("Data")
    @SerialName("Data")
    var data: SerializedGsonObject? = null

    override fun deserializeFromString(str: String) {
        this.type = str
    }

    object ProjectileInfoInstanceBuilder : StringInstanceBuilder<ProjectileInfo> {
        override fun fromString(value: String) = ProjectileInfo().apply {
            this.type = value
        }
    }
}
