/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.struct;

import java.text.DecimalFormat;

public enum DecimalFormatType {
    MONEY(new DecimalFormat("#,###.##")),
    SECONDS(new DecimalFormat("#.#")),
    LOCATION(new DecimalFormat("#.##"));

    private DecimalFormat format;

    private DecimalFormatType(DecimalFormat format) {
        this.format = format;
    }

    public String format(Number value) {
        return this.format.format(value);
    }

    public DecimalFormat getFormat() {
        return this.format;
    }
}

