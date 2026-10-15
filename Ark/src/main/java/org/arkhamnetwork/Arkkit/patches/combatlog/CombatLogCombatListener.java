package org.arkhamnetwork.Arkkit.patches.combatlog;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.wimbli.WorldBorder.WorldBorder;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CombatLogCombatListener
        implements Listener {
    public static int getTimeLeftMSInCombat(Player pl) {
        if (pl.hasMetadata("cl_combat") && pl.getMetadata("cl_combat").get(0) != null && System.currentTimeMillis() - pl.getMetadata("cl_combat").get(0).asLong() <= 10000L) {
            return (int) (10000L - (System.currentTimeMillis() - pl.getMetadata("cl_combat").get(0).asLong()));
        }
        return 0;
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCombatTag(EntityDamageEvent e) {
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getCause() == EntityDamageEvent.DamageCause.CONTACT || e.getCause() == EntityDamageEvent.DamageCause.STARVATION || e.getCause() == EntityDamageEvent.DamageCause.FALL || e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION || e.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK || e.getCause() == EntityDamageEvent.DamageCause.LAVA || e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            return;
        }
        if (e.getEntity() instanceof Player || e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player || e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile && ((Projectile) ((EntityDamageByEntityEvent) e).getDamager()).getShooter() instanceof Player) {
            if (e.getEntity() instanceof Player) {
                Entity damager;
                if (e instanceof EntityDamageByEntityEvent && (damager = ((EntityDamageByEntityEvent) e).getDamager()) instanceof LivingEntity) {
                    LivingEntity leDamager = (LivingEntity) damager;
                    if (leDamager.getType() == EntityType.MAGMA_CUBE || ((CraftLivingEntity) leDamager).getHandle().fromMobSpawner) {
                        return;
                    }
                    if (leDamager instanceof Player && MaskUtils.hasMaskEquipped((Player) leDamager, "JOKER")) {
                        e.getEntity().setMetadata("affectedByJokerMask", new FixedMetadataValue(Arkkit.get(), MinecraftServer.currentTick + 400));
                    }
                }
                this.disableFlight((Player) e.getEntity());
                CombatLog.flagCombat((Player) e.getEntity());

            }
            if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player && (e.getEntity() instanceof Player || e.getEntity().hasMetadata("boss") || e.getEntity().hasMetadata("minion") || e.getEntity().hasMetadata("endMonsterKills"))) {
                Player pDamager = (Player) ((EntityDamageByEntityEvent) e).getDamager();
                if (e.getEntity() instanceof Player) {
                    Player pHurt = (Player) e.getEntity();
                    pHurt.setMetadata("lastPlayerDamagerUUID", new FixedMetadataValue(Arkkit.get(), pDamager.getUniqueId()));
                    if (!pHurt.canSee(pDamager)) {
                        boolean allowedToOverride;
                        boolean hasFeignDeath = pDamager.hasMetadata("feign_vanish");
                        boolean bl = allowedToOverride = hasFeignDeath && pDamager.hasMetadata("feign_hits") && pDamager.hasMetadata("feign_hits_max") && pDamager.getMetadata("feign_hits").get(0).asInt() < pDamager.getMetadata("feign_hits_max").get(0).asInt() || hasFeignDeath;
                        if (!allowedToOverride) {
                            User uDamager = ((Essentials) Bukkit.getPluginManager().getPlugin("Essentials")).getUser(pDamager);
                            User uHurt = ((Essentials) Bukkit.getPluginManager().getPlugin("Essentials")).getUser(pHurt);
                            if (!uDamager.isVanished()) {
                                pHurt.showPlayer(pDamager);
                                Bukkit.getLogger().info("[Arkkit (CombatLog)] Player " + pHurt.getName() + " could not see " + pDamager.getName() + ", executed .showPlayer()[1]!");
                            } else {
                                Bukkit.getLogger().info("[Arkkit (CombatLog)] Skipping .showPlayer()[1] on " + pHurt.getName() + ".show(" + pDamager.getName() + ") because " + pDamager.getName() + " is vanished!");
                            }
                            if (!uHurt.isVanished()) {
                                pDamager.showPlayer(pHurt);
                                Bukkit.getLogger().info("[Arkkit (CombatLog)] Player " + pHurt.getName() + " could not see " + pDamager.getName() + ", executed .showPlayer()[2]!");
                            } else {
                                Bukkit.getLogger().info("[Arkkit (CombatLog)] Skipping .showPlayer()[2] on " + pDamager.getName() + ".show(" + pHurt.getName() + ") because " + pHurt.getName() + " is vanished!");
                            }
                        }
                    }
                }
                this.disableFlight(pDamager);
                CombatLog.flagCombat(pDamager);


            }
            if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile && ((Projectile) ((EntityDamageByEntityEvent) e).getDamager()).getShooter() instanceof Player && e.getEntity() instanceof Player) {
                this.disableFlight((Player) ((Projectile) ((EntityDamageByEntityEvent) e).getDamager()).getShooter());
                CombatLog.flagCombat((Player) ((Projectile) ((EntityDamageByEntityEvent) e).getDamager()).getShooter());
            }
        }
    }

    private void disableFlight(Player pl) {
        pl.setFlying(false);
        pl.setAllowFlight(false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerRespawnEvent event) {
        CombatLog.unflagCombat(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityShootBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            CombatLog.flagCombat((Player) e.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
        if (CombatLog.inCombat(e.getPlayer())) {
            String pName;
            if ((e.getPlayer().isOp() || e.getPlayer().hasPermission("arkkit.combatlog.use_commands")) && !(pName = e.getPlayer().getName()).equals("TBNRfrags")) {
                return;
            }
            String rootCommand = e.getMessage().toLowerCase();
            if (rootCommand.contains(" ")) {
                rootCommand = rootCommand.split(" ")[0];
            }
            if (rootCommand.equals("/ci") || rootCommand.equals("/clearinventory") || rootCommand.equals("/eci") || rootCommand.equals("/clean") || rootCommand.equals("/eclean") || rootCommand.equals("/clear") || rootCommand.equals("/eclear") || rootCommand.equals("/clearinvent") || rootCommand.equals("/eclearinvent") || rootCommand.equals("/eclearinventory")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "You cannot use that command while in combat! (" + getTimeLeftMSInCombat(e.getPlayer()) + "ms)");
            }
            if (e.getMessage().contains(":") || rootCommand.equals("/tpahere") || rootCommand.equals("/etpahere") || rootCommand.equals("/tpa") || rootCommand.equals("/etpa") || rootCommand.equals("/tpaccept") || rootCommand.equals("/tpyes") || rootCommand.equals("/etpyes") || rootCommand.equals("/etpaccept") || rootCommand.equals("/spawn") || rootCommand.equals("/home") || rootCommand.equals("/espawn") || rootCommand.equals("/ehome") || rootCommand.equals("/vault") || rootCommand.equals("/pvault") || rootCommand.equals("/pv") || rootCommand.equals("/echest") || rootCommand.equals("/enderchest") || rootCommand.equals("/ec") || rootCommand.equals("/bec") || rootCommand.equals("/betterenderchest") || rootCommand.equals("/hub") || rootCommand.equals("/lobby") || rootCommand.equals("/chest") || rootCommand.equals("/vault") || rootCommand.equals("/playervaults") || rootCommand.equals("/hub") || rootCommand.equals("/quit") || rootCommand.equals("/lobby") || rootCommand.equals("/server") || rootCommand.equals("/ah") || rootCommand.equals("/auction") || rootCommand.equals("/auc") || rootCommand.equals("/fly") || rootCommand.equals("/efly") || rootCommand.equals("/tinkerer") || rootCommand.equals("/enchanter") || rootCommand.equals("/bid") || rootCommand.equals("/gift") || rootCommand.equals("/merchant") || rootCommand.equals("/armormerchant") || rootCommand.equals("/showcase") || rootCommand.equals("/tnt") || rootCommand.equals("/sell") || rootCommand.equals("/esell") || rootCommand.equals("/coinflip") || rootCommand.equals("/cf") || rootCommand.equals("/shop") || rootCommand.equals("/store") || rootCommand.equals("/shops") || rootCommand.equals("/cosmicshop") || rootCommand.equals("/cshop") || rootCommand.equals("/fund") || rootCommand.equals("/cosmicfund") || rootCommand.equals("/trade") || rootCommand.equals("/kit") || rootCommand.equals("/kits") || rootCommand.equals("/ekit") || rootCommand.equals("/ekits") || rootCommand.equals("/tnt")) {
                e.setCancelled(true);
                e.setMessage("/null");
                e.getPlayer().sendMessage(ChatColor.RED + "You cannot use that command while in combat! (" + getTimeLeftMSInCombat(e.getPlayer()) + "ms)");
            }
            if ((rootCommand.equals("/heal") || rootCommand.equals("/eheal")) && Arkkit.get().getConfig().getBoolean("settings.combat_log.block_heal")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "You cannot use that command while in combat! (" + getTimeLeftMSInCombat(e.getPlayer()) + "ms)");
            }
            if (rootCommand.equals("/ct")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "Combat Tag Timer: " + getTimeLeftMSInCombat(e.getPlayer()) + "ms.");

            }
        }
    }



    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (e.getPlayer().hasMetadata("ignore_combat_tag")) {
            return;
        }
        if (CombatLog.inCombat(e.getPlayer())) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) return;
            if (e.getPlayer().isOp() || e.getPlayer().hasPermission("arkkit.combatlog.use_enderpearls")) {
                return;
            }
            if (e.getPlayer().hasMetadata("bypassCombatTeleport") && e.getPlayer().getMetadata("bypassCombatTeleport").get(0).value().equals(e.getTo())) {
                e.getPlayer().removeMetadata("bypassCombatTeleport", Enchantments.getInstance());
                return;
            }
            if (!(!CombatLog.block_enderpearls && e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL || e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN && CombatLog.duel_plugin)) {
                if (!e.getTo().getWorld().equals(e.getFrom().getWorld())) {
                    e.setCancelled(true);
                    if (!e.getPlayer().hasMetadata("arkkit.combatlog.tp_deny") || System.currentTimeMillis() - e.getPlayer().getMetadata("arkkit.combatlog.tp_deny").get(0).asLong() > 5000L) {
                        e.getPlayer().setMetadata("arkkit.combatlog.tp_deny", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
                        e.getPlayer().sendMessage(ChatColor.RED + "You cannot teleport while in combat!");
                    }
                    return;
                }
                if (WorldBorder.plugin.getWorldBorder(e.getFrom().getWorld().getName()) != null && !WorldBorder.plugin.GetWorldBorder(e.getFrom().getWorld().getName()).insideBorder(e.getFrom())) {
                    return;
                }
                double dist_sqrd = e.getTo().distanceSquared(e.getFrom());
                if (dist_sqrd >= 25.0) {
                    e.setCancelled(true);
                    if (!e.getPlayer().hasMetadata("arkkit.combatlog.tp_deny") || System.currentTimeMillis() - e.getPlayer().getMetadata("arkkit.combatlog.tp_deny").get(0).asLong() > 5000L) {
                        e.getPlayer().setMetadata("arkkit.combatlog.tp_deny", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
                        e.getPlayer().sendMessage(ChatColor.RED + "You cannot teleport while in combat!");
                    }
                }
            }
        }
    }
}

