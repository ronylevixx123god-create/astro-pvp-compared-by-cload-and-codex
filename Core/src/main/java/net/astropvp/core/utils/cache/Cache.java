package net.astropvp.core.utils.cache;

import lombok.Getter;

@Getter
public abstract class Cache {

    private final String name;

    public Cache(String name) {
        this.name = name;
    }
}