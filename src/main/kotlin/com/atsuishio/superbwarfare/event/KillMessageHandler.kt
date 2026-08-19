package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.tools.LivingKillRecord
import java.util.*

object KillMessageHandler {
    @JvmField
    val QUEUE: Queue<LivingKillRecord> = ArrayDeque()

    @JvmStatic
    fun onClientTick() {
        for (record in QUEUE) {
            if (record.freeze && record.tick >= 3) {
                continue
            }
            record.tick++
            if (record.fastRemove && record.tick >= 82 || record.tick >= 100) {
                QUEUE.poll()
            }
        }
    }
}
