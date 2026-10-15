/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmicduel.struct.enums;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Reason {
    WIN(null),
    DEAD("&e&l(!) &e&n[PLAYER]&e has been slain."),
    LOGGED_OFF("&c[PLAYER] &elogged off."),
    LEFT_DUEL("&e&l(!) &a[PLAYER] &eleft the duel."),
    RELOAD("Reloading the server..."),
    LEFT_ARENA("&c&l(!) &c&n[PLAYER]&e has left the duel arena."),
    NO_WINNER("&a&l(!) &aThe stakes have been returned.");

    String reason;

    public String getReason(Player p) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)this.reason.replace("[PLAYER]", p.getName()));
    }

    private Reason(String reason) {
        this.reason = reason;
    }
}

