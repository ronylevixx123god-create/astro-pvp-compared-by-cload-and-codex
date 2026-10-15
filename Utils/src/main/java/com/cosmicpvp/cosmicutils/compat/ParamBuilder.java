/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.compat;

import java.util.LinkedList;
import java.util.List;

public class ParamBuilder {
    private List<RParam> parameters = new LinkedList<RParam>();
    private int validObjectCount = 0;

    public ParamBuilder() {
    }

    public ParamBuilder(Class<?> clazz, Object obj) {
        this.add(clazz, obj);
    }

    public ParamBuilder add(RParam param) {
        this.parameters.add(param);
        if (param != null) {
            ++this.validObjectCount;
        }
        return this;
    }

    public ParamBuilder add(Class<?> clazz, Object param) {
        this.add(new RParam(clazz, param));
        return this;
    }

    public List<RParam> getParameters() {
        return this.parameters;
    }

    public int getValidObjectCount() {
        return this.validObjectCount;
    }

    public void setValidObjectCount(int validObjectCount) {
        this.validObjectCount = validObjectCount;
    }
}

