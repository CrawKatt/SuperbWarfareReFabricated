package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.tools.LivingKillRecord;

import java.util.ArrayDeque;
import java.util.Queue;

public class KillMessageHandler {

    public static Queue<LivingKillRecord> QUEUE = new ArrayDeque<>();

    public static void onClientTick() {
        for (LivingKillRecord record : QUEUE) {
            if (record.freeze && record.tick >= 3) {
                continue;
            }
            record.tick++;
            if (record.fastRemove && record.tick >= 82 || record.tick >= 100) {
                QUEUE.poll();
            }
        }
    }
}
