package com.atsuishio.superbwarfare;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
public class Mod {

    public static final String MODID = "superbwarfare";
    public static final ResourceLocation ATTRIBUTE_MODIFIER = loc("attribute_modifier");

    public static final Logger LOGGER = LogManager.getLogger(Mod.class);

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> SERVER_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> CLIENT_QUEUE = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        SERVER_QUEUE.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    public static void queueClientWork(int tick, Runnable action) {
        CLIENT_QUEUE.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    public static void tickServer() {
        executeWork(SERVER_QUEUE);
    }

    public static void tickClient() {
        executeWork(CLIENT_QUEUE);
    }

    private static void executeWork(Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue) {
        List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
        workQueue.forEach(work -> {
            work.setValue(work.getValue() - 1);
            if (work.getValue() == 0)
                actions.add(work);
        });
        actions.forEach(e -> e.getKey().run());
        workQueue.removeAll(actions);
    }
}
*/