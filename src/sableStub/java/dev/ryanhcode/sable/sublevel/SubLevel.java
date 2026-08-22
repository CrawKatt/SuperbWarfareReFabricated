package dev.ryanhcode.sable.sublevel;

import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.world.level.Level;

/**
 * Stub de compilación: la clase real vive en el mod Sable y se aporta en runtime.
 * Solo refleja la superficie usada por {@code SableCompatHandler}.
 */
public abstract class SubLevel implements SubLevelAccess {

    public abstract Level getLevel();
}
