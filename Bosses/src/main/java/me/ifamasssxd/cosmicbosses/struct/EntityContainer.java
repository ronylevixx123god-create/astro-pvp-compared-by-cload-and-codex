package me.ifamasssxd.cosmicbosses.struct;

import org.bukkit.entity.Entity;

import java.util.Set;

public interface EntityContainer {
    Set<Entity> getEntities();

    void clearEntities();
}
