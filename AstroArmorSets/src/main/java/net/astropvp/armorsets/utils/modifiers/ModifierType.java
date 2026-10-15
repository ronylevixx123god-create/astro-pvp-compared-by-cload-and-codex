package net.astropvp.armorsets.utils.modifiers;

import lombok.Getter;

public enum ModifierType {

    MAX_HEALTH("generic.maxHealth"),
    FOLLOW_RANGE("generic.followRange"),
    KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
    MOVEMENT_SPEED("generic.movementSpeed"),
    ATTACK_DAMAGE("generic.attackDamage"),
    ARMOR("generic.armor"),
    ARMOR_TOUGHNESS("generic.armorToughness"),
    ATTACK_SPEED("generic.attackSpeed"),
    LUCK("generic.luck"),
    JUMP_STRENGTH("horse.jumpStrength"),
    SPAWN_REINFORCEMENTS("zombie.spawnReinforcements");
    ;

    @Getter
    private final String modifier;

    ModifierType(String modifier) {
        this.modifier = modifier;
    }

}
