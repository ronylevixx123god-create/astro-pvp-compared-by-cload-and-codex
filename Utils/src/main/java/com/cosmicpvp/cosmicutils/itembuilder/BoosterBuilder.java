package com.cosmicpvp.cosmicutils.itembuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BoosterBuilder
        extends ItemBuilder {
    public String boosterType;
    public int duration;
    private double multiplier;

    public BoosterBuilder(String booster, int duration, double multiplier) {
        super((Material) null, 0);
        this.boosterType = booster;
        this.duration = duration;
        this.multiplier = multiplier;
    }

    public static BoosterBuilderBuilder builder() {
        return new BoosterBuilderBuilder();
    }

    @Override
    public ItemStack build() {
        return null;
    }

    public String getBoosterType() {
        return this.boosterType;
    }

    public int getDuration() {
        return this.duration;
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    public static class BoosterBuilderBuilder {
        private String boosterType;
        private int duration;
        private double multiplier;

        BoosterBuilderBuilder() {
        }

        public BoosterBuilderBuilder boosterType(String boosterType) {
            this.boosterType = boosterType;
            return this;
        }

        public BoosterBuilderBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public BoosterBuilderBuilder multiplier(double multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        public BoosterBuilder build() {
            return new BoosterBuilder(this.boosterType, this.duration, this.multiplier);
        }

        public String toString() {
            return "BoosterBuilder.BoosterBuilderBuilder(boosterType=" + this.boosterType + ", duration=" + this.duration + ", multiplier=" + this.multiplier + ")";
        }
    }
}

