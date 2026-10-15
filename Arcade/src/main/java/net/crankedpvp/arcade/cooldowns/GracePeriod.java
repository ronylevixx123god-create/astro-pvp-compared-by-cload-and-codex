package net.crankedpvp.arcade.cooldowns;


import com.cosmicpvp.cosmicutils.cache.cooldown.CurrentCooldown;

public class GracePeriod extends CurrentCooldown {
    public GracePeriod(String name, int durationInSeconds) {
        super(name, durationInSeconds);
    }
}
