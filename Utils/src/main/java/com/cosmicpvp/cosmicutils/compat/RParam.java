/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.compat;

public class RParam {
    private Class<?> methodClass;
    private Object instance;

    public RParam(Class<?> clazz) {
        this.methodClass = clazz;
    }

    public RParam(Class<?> methodClass, Object instance) {
        this.methodClass = methodClass;
        this.instance = instance;
    }

    public Class<?> getMethodClass() {
        return this.methodClass;
    }

    public Object getInstance() {
        return this.instance;
    }
}

