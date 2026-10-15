/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PotionSplashEvent
 */
package me.ifamasssxd.cosmicduel.listeners;

import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

public class PotionListener
implements Listener {
    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerTossPotion(PotionSplashEvent e) {
        NewDuel d;
        Player p;
        if (e.getEntity().getShooter() instanceof Player && DuelAPI.hasDuel(p = (Player)e.getEntity().getShooter()) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(p)) && d.getSettings().hasSetting(Setting.POTIONS)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerTossPotionHigh(PotionSplashEvent e) {
        NewDuel d;
        Player p;
        if (e.getEntity().getShooter() instanceof Player && DuelAPI.hasDuel(p = (Player)e.getEntity().getShooter()) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(p)) && d.getSettings().hasSetting(Setting.POTIONS)) {
            e.setCancelled(false);
        }
    }
}

