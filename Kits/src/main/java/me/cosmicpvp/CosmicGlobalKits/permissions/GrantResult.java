package me.cosmicpvp.CosmicGlobalKits.permissions;

/** Outcome of a global-kit permission grant. Only {@link #GRANTED} may consume an unlock item. */
public enum GrantResult {
    /** The node was newly added and saved to LuckPerms storage. */
    GRANTED,
    /** The user already had exactly this node (same scope); nothing changed. */
    ALREADY_GRANTED,
    /** The grant could not be applied or saved; nothing should be consumed. */
    FAILED
}
