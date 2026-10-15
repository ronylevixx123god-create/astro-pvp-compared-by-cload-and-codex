package net.astropvp.core.cooldown;

import com.cosmicpvp.cosmicutils.cache.cooldown.CurrentCooldown;

public class ElfMaskCooldown extends CurrentCooldown {
    public ElfMaskCooldown(String name, int durationInSeconds) {
        super(name, durationInSeconds);
    }
}
