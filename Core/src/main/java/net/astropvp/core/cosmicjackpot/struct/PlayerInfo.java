/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.builder.HashCodeBuilder
 *  org.bukkit.entity.Player
 */
package net.astropvp.core.cosmicjackpot.struct;

import java.util.UUID;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.entity.Player;

public class PlayerInfo {
    private UUID uuid;
    private String username;

    public PlayerInfo(Player player) {
        this.username = player.getName();
        this.uuid = player.getUniqueId();
    }

    public boolean equals(Object obj) {
        PlayerInfo other;
        if (obj instanceof PlayerInfo && (other = (PlayerInfo)obj).getUuid() == this.uuid) {
            return true;
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append((Object)this.uuid).toHashCode();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }
}

