package org.demonicpvp.demonictrials.trial.modifier.madness;

import org.demonicpvp.demonictrials.trial.modifier.madness.impl.*;

public enum MadnessModifier {
    HOTBAR_SHUFFLE(HotbarShuffleModifier.class),
    INVENTORY_SWAP(InventorySwapModifier.class),
    HOTBAR_LOCK(HotbarLockModifier.class),
    TIME_GLITCH(TimeGlitchModifier.class),
    CURSED_LIFE(CursedLifeModifier.class),
    ITEM_HELD_RANDOMIZER(ItemHeldRandomizerModifier.class),
    LOST_AND_FOUND(LostAndFoundModifier.class),
    STICKY_KEYS(StickyKeysModifier.class),
    STATUES(StatuesModifier.class),
    OWL_GENE(OwlGeneModifier.class),
    ROCKET_MAN(RocketManModifier.class),
    HELL_WRATH(HellWrathModifier.class),
    PROTECT_THE_PRESIDENT(ProtectThePresidentModifier.class);

    private final Class<? extends AbstractMadnessModifier> clazz;

    MadnessModifier(Class<? extends AbstractMadnessModifier> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends AbstractMadnessModifier> getClazz() {
        return this.clazz;
    }
}
