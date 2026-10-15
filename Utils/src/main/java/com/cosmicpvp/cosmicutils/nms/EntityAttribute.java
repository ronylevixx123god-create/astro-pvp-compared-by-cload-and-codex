/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.nms;

import com.cosmicpvp.cosmicutils.utils.ReflectionUtils;
import org.bukkit.Bukkit;

public enum EntityAttribute {
    MAX_HEALTH("maxHealth"),
    FOLLOW_RANGE("b"),
    KNOCKBACK_RESISTANCE("c"),
    MOVEMENT_SPEED("d"),
    ATTACK_DAMAGE("e");

    private String genericAttributeField;
    private Object nmsEnumObject;

    private EntityAttribute(String genericAttributeField) {
        this.genericAttributeField = genericAttributeField;
        try {
            this.nmsEnumObject = ReflectionUtils.getField(ReflectionUtils.getGenericAttributesClass(), this.genericAttributeField).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Bukkit.getLogger().info("Unable to get NMS version of getGenericAttributes! Outdated version perhaps?");
            e.printStackTrace();
        }
    }

    public String getGenericAttributeField() {
        return this.genericAttributeField;
    }

    public Object getNmsEnumObject() {
        return this.nmsEnumObject;
    }
}

