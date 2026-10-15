package me.ifamasssxd.cosmicskycustoms.exceptions;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.CC;

public class PrefixedItemException
extends CosmicException {
    public PrefixedItemException(String message) {
        super(CC.RedB + "(!) " + CC.Red + message);
    }
}

