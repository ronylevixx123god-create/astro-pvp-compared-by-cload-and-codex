/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.ChunkLoadEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.List;

final class ChunkLoadFlushListener
        implements Listener {
    List<EntityType> noRemove = new ArrayList<EntityType>();

    ChunkLoadFlushListener() {
        this.noRemove.add(EntityType.PLAYER);
        this.noRemove.add(EntityType.ITEM_FRAME);
        this.noRemove.add(EntityType.LEASH_HITCH);
        this.noRemove.add(EntityType.PAINTING);
        this.noRemove.add(EntityType.PRIMED_TNT);
    }

    /*@EventHandler(priority = EventPriority.LOW)
    public void onChunkLoad(ChunkLoadEvent e) {
        this.removeEntities(e.getChunk());
    }*/

    private void removeEntities(Chunk c) {
        for (Entity e : c.getEntities()) {
            if (this.noRemove.contains(e.getType())) continue;
            e.remove();
        }
    }
}

