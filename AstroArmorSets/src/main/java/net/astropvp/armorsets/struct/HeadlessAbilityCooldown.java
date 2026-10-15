package net.astropvp.armorsets.struct;

import com.cosmicpvp.cosmicutils.cache.cooldown.CurrentCooldown;

public class HeadlessAbilityCooldown extends CurrentCooldown {
    public HeadlessAbilityCooldown(String name, int durationInSeconds) {
        super(name, durationInSeconds);
    }
}
