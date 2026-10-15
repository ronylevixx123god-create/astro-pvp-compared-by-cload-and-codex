package org.arkhamnetwork.enchantments.libs;

public enum OrbType {
    ARMOR("Armor"),
    WEAPON("Weapon");

    public String label;

    private OrbType(final String label) {
        this.label = label;
    }
}
