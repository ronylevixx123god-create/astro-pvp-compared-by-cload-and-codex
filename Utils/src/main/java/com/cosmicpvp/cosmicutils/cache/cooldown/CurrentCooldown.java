package com.cosmicpvp.cosmicutils.cache.cooldown;

import com.cosmicpvp.cosmicutils.cache.Cache;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import lombok.Getter;

@Getter
public abstract class CurrentCooldown extends Cache {

    private final long started;
    private final long duration;

    public CurrentCooldown(String name, int durationInSeconds) {
        super(name);
        this.started = System.currentTimeMillis();
        this.duration = durationInSeconds * 1000L;
    }

    public CurrentCooldown(String name, long durationInMs) {
        super(name);
        this.started = System.currentTimeMillis();
        this.duration = durationInMs;
    }

    public boolean isActive() {
        return this.started + this.duration > System.currentTimeMillis();
    }

    public String getFormattedTime() {
        return TimeUtils.formatSeconds((this.started + this.duration - System.currentTimeMillis())/1000);
    }
}
