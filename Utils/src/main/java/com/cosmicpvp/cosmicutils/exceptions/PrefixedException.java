/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.exceptions;

import com.cosmicpvp.cosmicutils.utils.CC;

public class PrefixedException
        extends CosmicException {
    public PrefixedException(String msg) {
        super(CC.RedB + "(!) " + CC.Red + msg);
    }
}

