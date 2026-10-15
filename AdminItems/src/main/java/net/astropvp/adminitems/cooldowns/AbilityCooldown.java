package net.astropvp.adminitems.cooldowns;


import com.cosmicpvp.cosmicutils.cache.cooldown.CurrentCooldown;

public class AbilityCooldown extends CurrentCooldown {
    public AbilityCooldown(String name, int durationInSeconds) {
        super(name, durationInSeconds);
    }
}
