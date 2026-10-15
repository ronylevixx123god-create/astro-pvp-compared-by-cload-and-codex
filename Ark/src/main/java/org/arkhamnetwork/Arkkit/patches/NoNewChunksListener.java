/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.ChunkLoadEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

final class NoNewChunksListener
        implements Listener {
    NoNewChunksListener() {
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        if (NoNewChunks.worlds.size() == 0) {
            return;
        }
        if (e.isNewChunk()) {
            String world = e.getWorld().getName();
            for (String w : NoNewChunks.worlds) {
                if (!w.equalsIgnoreCase(world)) continue;
                Chunk c = e.getChunk();
                c.unload(false, false);
                break;
            }
        }
    }
}

