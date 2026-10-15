package me.ifamasssxd.cosmicskycustoms.exceptions;

import me.ifamasssxd.cosmicskycustoms.exceptions.PrefixedItemException;

public class ItemDupedException
extends PrefixedItemException {
    public ItemDupedException(String message) {
        super(message);
    }
}

