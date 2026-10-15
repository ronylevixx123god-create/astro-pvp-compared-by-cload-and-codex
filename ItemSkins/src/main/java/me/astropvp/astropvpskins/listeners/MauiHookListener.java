package me.astropvp.astropvpskins.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.swords.MauisHookSkin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

import java.util.List;

public class MauiHookListener implements Listener {

    @EventHandler
    public void sneakMauiEffect(PlayerToggleSneakEvent e) {
        if (!e.getPlayer().getWorld().getName().equals("world") && !e.getPlayer().getWorld().getName().equals("flat") ) return;
        if (e.getPlayer().getItemInHand() != null) {
        if (e.getPlayer().getItemInHand().getType().name().contains("SWORD")) {
            if (e.isSneaking() && MauisHookSkin.mauiEffect.containsKey(e.getPlayer()) && (
                    MauisHookSkin.mauiEffect.get(e.getPlayer()) >= 10)) {
                if (AstroItemSkinsAPI.hasSkinEquipped(e.getPlayer(), SkinType.SUMMER_2021_SWORD)) {
                    List<Entity> nearbyPlayers = e.getPlayer().getNearbyEntities(8.0D, 8.0D, 8.0D);
                    if (nearbyPlayers.isEmpty())
                        e.getPlayer().sendMessage(CC.translate("&cNo nearby enemies to activate Transcendence Ability!"));
                    MauisHookSkin.mauiEffect.remove(e.getPlayer());
                    e.getPlayer().sendMessage(CC.translate("&c&l* TRANSCENDENCE ABILITY &con Nearby Players &7(8x8) &c&l*"));
                    e.getPlayer().sendMessage(CC.translate("&7(Damaging enemies, Silencing them and their Pets.)"));
                    e.getPlayer().sendTitle(new Title(CC.translate("&c&l* Transcendence Ability *"), CC.translate("&7Maui's Stack Reset"), 10, 20, 10));
                    for (Entity entity : nearbyPlayers) {
                        if (!(entity instanceof Player))
                            continue;
                        final Player target = ((Player) entity).getPlayer();
                        if (GeneralUtil.isAtleastTruce(target, e.getPlayer()))
                            continue;
                        if (FactionUtils.inSameFaction(target, e.getPlayer()))
                            continue;
                        if (!WorldGuardUtils.isPvPEnabled(target.getLocation()))
                            continue;
                        target.setMetadata("transEffect", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), ""));
                        target.sendTitle(new Title(CC.translate("&c&l* &f" + e.getPlayer().getName() + "'s &cTranscendence Ability &c&l*"), CC.translate("&7Silenced (Pets) and Massive Damage"), 10, 20, 10));
                        target.playSound(target.getLocation(), Sound.WITHER_HURT, 1.0F, 0.25F);
                        target.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "* SILENCED " + ChatColor.GRAY + " [7s] " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "*");
                        target.setMetadata("noDefenseProcs", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), Boolean.valueOf(true)));
                        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> target.removeMetadata("noDefenseProcs", AstroItemSkinsPlugin.getInstance()), 140L);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> target.damage(2.0D), 140L);
                        (new BukkitRunnable() {
                            int count = 0;

                            public void run() {
                                if (this.count >= 20)
                                    cancel();
                                this.count++;
                                if (WorldGuardUtils.isPvPEnabled(target.getLocation())) {
                                    target.damage(4.0D);
                                }
                            }
                        }).runTaskTimer(AstroItemSkinsPlugin.getInstance(), 0L, 10L);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                            target.sendMessage(CC.translate("&a&lPET: &aYour Pets are no longer Silenced."));
                            target.removeMetadata("transEffect", AstroItemSkinsPlugin.getInstance());
                        }, 200L);
                    }
                }
            }
            }
        }
    }
}
