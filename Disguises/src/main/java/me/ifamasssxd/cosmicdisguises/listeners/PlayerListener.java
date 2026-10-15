/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  me.ifamasssxd.cosmicmasks.MaskType
 *  me.libraryaddict.disguise.DisguiseAPI
 *  me.libraryaddict.disguise.disguisetypes.Disguise
 *  me.libraryaddict.disguise.disguisetypes.DisguiseType
 *  me.libraryaddict.disguise.disguisetypes.MobDisguise
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package me.ifamasssxd.cosmicdisguises.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicdisguises.CosmicDisguises;
import me.ifamasssxd.cosmicdisguises.managers.DisguiseManager;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Calendar;
import java.util.TimeZone;

public class PlayerListener
implements Listener {
    private Calendar calendar = Calendar.getInstance();

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerPreProcessCommand(PlayerCommandPreprocessEvent event) {
        if (CosmicDisguises.get().isOverwriteAliases()) {
            String cmd;
            String msg = event.getMessage().toLowerCase();
            String string = cmd = msg.contains(" ") ? msg.split(" ")[0] : msg;
            if (cmd.equals("/disguise") || msg.equals("/d") || msg.equals("/dis")) {
                event.setMessage(msg.replace(cmd, "/disguises"));
            } else if (cmd.equals("/undis") || cmd.equalsIgnoreCase("/undisguise")) {
                event.setMessage(msg.replace(cmd, "/disguises off"));
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        boolean aprilFirst;
        DisguiseManager manager = CosmicDisguises.get().getDisguiseManager();
        DisguiseType disguiseAvailable = manager.getDisguiseAvailable(event.getPlayer());
        this.calendar.setTimeZone(TimeZone.getTimeZone("EST"));
        this.calendar.setTimeInMillis(System.currentTimeMillis());
        this.calendar.add(10, 1);
        boolean bl = aprilFirst = this.calendar.get(5) == 1 && this.calendar.get(2) == 3;
        if (!aprilFirst && DisguiseAPI.isDisguised((Entity)event.getPlayer())) {
            DisguiseAPI.undisguiseToAll((Entity)event.getPlayer());
        }
        if (disguiseAvailable != null && aprilFirst) {
            DisguiseAPI.undisguiseToAll((Entity)event.getPlayer());
            DisguiseAPI.disguiseToAll((Entity)event.getPlayer(), (Disguise)new MobDisguise(disguiseAvailable));
            Bukkit.getLogger().info("[CosmicDisguises] Disguising " + event.getPlayer().getName() + " as a " + disguiseAvailable.name() + " for April Fools!");
        }
        manager.getAvailableDisguises(event.getPlayer(), true);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        CosmicDisguises.get().getDisguiseManager().getCachedAvailableTypes().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equals("world_duels2") && DisguiseAPI.isDisguised((Entity)e.getPlayer())) {
            e.getPlayer().sendMessage(String.valueOf(CC.RedB) + "(!) " + CC.Red + "Your disguise has been removed due to entering a duel!");
            DisguiseAPI.undisguiseToAll((Entity)e.getPlayer());
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onTakeDamage(EntityDamageByEntityEvent event) {
        Player player;
        if (event.getEntity() instanceof Player && DisguiseAPI.isDisguised((Entity)(player = (Player)event.getEntity()))) {
            if (DisguiseAPI.getDisguise((Entity)player).getType() == DisguiseType.ZOMBIE && player.getLocation().getY() >= 200.0 && MaskType.hasEquipped((Player)player, (String)"SPECTRAL")) {
                return;
            }
            player.sendMessage(String.valueOf(CC.RedB) + "(!) " + CC.Red + "Your disguise has been removed due to taking damage!");
            DisguiseAPI.undisguiseToAll((Entity)event.getEntity());
        }
        if (event.getDamager() instanceof Player || event.getDamager() instanceof Projectile && ((Projectile)event.getDamager()).getShooter() instanceof Player) {
            Player damager;
            Player player2 = damager = event.getDamager() instanceof Player ? (Player)event.getDamager() : (Player)((Projectile)event.getDamager()).getShooter();
            if (DisguiseAPI.isDisguised((Entity)damager)) {
                if (DisguiseAPI.getDisguise((Entity)damager).getType() == DisguiseType.ZOMBIE && damager.getLocation().getY() >= 200.0 && MaskType.hasEquipped((Player)damager, (String)"SPECTRAL")) {
                    return;
                }
                damager.sendMessage(String.valueOf(CC.RedB) + "(!) " + CC.Red + "Your disguise has been removed due to dealing damage!");
                DisguiseAPI.undisguiseToAll((Entity)damager);
            }
        }
    }
}

