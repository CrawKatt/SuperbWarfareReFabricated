package com.atsuishio.superbwarfare.data.gun

/**
 * Lightweight version counter attached to a single [GunData] instance.
 * 
 * 
 * Two independent counters track different classes of NBT mutation:
 * 
 *  * **structural**: changes that affect computed gun properties (attachment
 * swaps, perk changes, fire-mode selection, property overrides). Any change
 * here increments the structural version and requires a full PMC rebuild.
 *  * **state**: changes to ephemeral runtime values (ammo count, heat,
 * reload timers, bolt timers). These do NOT require a PMC rebuild.
 * 
 * 
 * 
 * Consumers compare a cached snapshot of [.getStructural] against the current
 * value; a mismatch means the PMC is stale and must be recomputed.
 * 
 * @author superbwarfare contributors
 * @since 0.8.9.1
 */
class NbtVersion {
    /**
     * Gets the current structural version counter.
     * 
     * @return current structural version.
     */
    /**
     * Incremented whenever a *structural* NBT field changes.
     * Wrap-around on [Integer.MAX_VALUE] is safe because comparisons use `!=`.
     */
    var structural: Int = 0
        private set

    /**
     * Gets the current state version counter.
     * 
     * @return current state version.
     */
    /**
     * Incremented whenever a *state* NBT field changes.
     */
    var state: Int = 0
        private set

    /**
     * Signals that a structural property has changed.
     * Also increments [.state] because structural changes encompass state changes.
     */
    fun invalidateStructural() {
        this.structural++
        this.state++
    }

    /**
     * Signals that only a state property has changed.
     * Does **not** increment [.structural].
     */
    fun invalidateState() {
        this.state++
    }
}