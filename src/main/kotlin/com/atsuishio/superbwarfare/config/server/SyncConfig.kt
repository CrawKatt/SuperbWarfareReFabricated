package com.atsuishio.superbwarfare.config.server

import com.atsuishio.superbwarfare.config.buildServerConfig

object SyncConfig {
    @JvmField
    val SYNC_ENTITY_OVER_RANGE = buildServerConfig {
        push("sync")

        comment("Set true to enable synchronizing client entities with the server")
        comment("是否允许服务端同步超视距的客户端实体")
        define("sync_entity_over_range", true)
    }

    @JvmField
    val SYNC_ENTITY_INTERVAL = buildServerConfig {
        comment("The interval for synchronizing client entities with the server (tick)")
        comment("服务端同步客户端实体的间隔（单位为刻）")
        defineInRange("sync_entity_interval", 1, 1, Int.MAX_VALUE)
    }

    @JvmField
    val CLIENT_SYNC_EXPIRE_TIME = buildServerConfig {
        comment("The expire time for synchronized client entities (ms)")
        comment("同步到客户端的实体的数据失效时间（单位为毫秒）")
        defineInRange("client_sync_expire_time", 1000, 1, Int.MAX_VALUE)
    }

    @JvmField
    val SERVER_SYNC_EXPIRE_TIME = buildServerConfig {
        comment("The expire time for synchronized server entities (ms)")
        comment("需要同步的服务端实体的数据失效时间（单位为毫秒）")
        defineInRange("server_sync_expire_time", 10000, 1, Int.MAX_VALUE)
    }

    @JvmField
    val SERVER_SYNC_CLEAN_INTERVAL = buildServerConfig {
        comment("The interval for cleaning synchronized server entities (ms)")
        comment("清理需要同步的服务端的实体数据的间隔（单位为毫秒）")
        defineInRange("server_sync_clean_interval", 200, 1, Int.MAX_VALUE)
    }

    @JvmField
    val MAX_RENDER_DISTANCE = buildServerConfig {
        comment("The maximum distance for rendering synchronized entities")
        comment("允许渲染超视距同步实体的最大距离")
        defineInRange("max_render_distance", 2048, 1, Int.MAX_VALUE)
    }

    @JvmField
    val MIN_RENDER_HEIGHT = buildServerConfig {
        comment("The minimum height for rendering synchronized entities")
        comment("允许渲染超视距同步实体的最小高度")
        defineInRange("min_render_height", 256, 1, Int.MAX_VALUE).also { pop() }
    }
}