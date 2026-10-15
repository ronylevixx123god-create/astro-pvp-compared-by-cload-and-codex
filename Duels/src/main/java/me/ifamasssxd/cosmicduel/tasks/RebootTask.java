/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package me.ifamasssxd.cosmicduel.tasks;

import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.enums.EndReason;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class RebootTask
implements Runnable {
    @Override
    public void run() {
        try {
            if (DuelAPI.isRebooting()) {
                for (NewDuel duel : new ArrayList<NewDuel>(DuelAPI.duels)) {
                    if (!DuelAPI.isDuelValid(duel)) continue;
                    duel.sendMessage((Object)ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "The duel has been cancelled due to server reboot.", true);
                    duel.onDuelEnd(EndReason.RELOAD, null);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

