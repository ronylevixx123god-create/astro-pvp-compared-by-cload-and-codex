/*
 * Decompiled with CFR 0.150.
 */
package com.rit.sucy.service;

public interface ConfigNode {
    public String getPath();

    public VarType getVarType();

    public SubType getSubType();

    public Object getDefaultValue();

    public static enum SubType {
        PERCENTAGE,
        Y_VALUE,
        HEALTH,
        NATURAL_NUMBER;

    }

    public static enum VarType {
        STRING,
        INTEGER,
        DOUBLE,
        BOOLEAN,
        LIST;

    }
}

