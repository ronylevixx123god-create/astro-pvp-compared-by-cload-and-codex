package me.ifamasssxd.cosmicsky.inventorypets.struct;

import com.cosmicpvp.cosmicutils.cache.cooldown.CurrentCooldown;

public class PetCooldown extends CurrentCooldown {
    public PetCooldown(String name, int durationInSeconds) {
        super(name, durationInSeconds);
    }
}
