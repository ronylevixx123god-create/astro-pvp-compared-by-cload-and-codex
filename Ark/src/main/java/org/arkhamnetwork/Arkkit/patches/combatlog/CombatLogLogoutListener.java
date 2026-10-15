
package org.arkhamnetwork.Arkkit.patches.combatlog;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.Relation;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.utils.FactionUtil;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class CombatLogLogoutListener
        implements Listener {
    public static boolean spawnNPC = false;
    public static ConcurrentHashMap<String, NPC> combatNPCs;
    public static HashMap<String, ItemStack[]> combatNPCs_loot;
    protected static HashSet<String> playersToKill;

    static {
        playersToKill = new HashSet();
    }

    private final PlayerLogoutMonitorTask logoutMonitor = new PlayerLogoutMonitorTask();
    private CombatLog cl = null;

    CombatLogLogoutListener(CombatLog cl) {
        combatNPCs = new ConcurrentHashMap();
        combatNPCs_loot = new HashMap();
        this.cl = cl;
        this.cl.registerTask(this.logoutMonitor.runTaskTimer(Arkkit.get(), 5L, 5L), CombatLogLogoutListener.class);
        spawnNPC = cl.getBukkitPlugin().getConfig().getBoolean("settings.combat_log.spawnNPC");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (this.logoutMonitor.logouts++ > 2) {
            Bukkit.getLogger().info("Skipping combatLog check for " + e.getPlayer().getName() + " due to >2 logouts in 5 ticks.");
            return;
        }
        if (e.getPlayer().isDead() || e.getPlayer().getHealth() <= 0.0) {
            Bukkit.getLogger().info("Skipping combatLog check for " + e.getPlayer().getName() + " because their entity is dead.");
            return;
        }
        if (e.getPlayer().getWorld().getName().startsWith("dungeon_")) {
            Bukkit.getLogger().info("Skipping combatLog check for " + e.getPlayer().getName() + " due to worldName: " + e.getPlayer().getWorld().getName());
            return;
        }

        if (!(WorldGuardUtils.isPvPEnabled(e.getPlayer().getLocation()))) {
            Bukkit.getLogger().info("Skipping " + e.getPlayer().getName() + " combatlog death due to logging off in safezone (" + e.getPlayer().getLocation() + ")");
            return;
        }

        if (!(CombatLog.inCombat(e.getPlayer()))) {
            Bukkit.getLogger().info("Skipping " + e.getPlayer().getName() + " because not in combat");
            return;
        }
        if (!(Arkkit.get().shutting_down || !CombatLog.inCombat(e.getPlayer()) && CombatLog.isPvPDisabled(e.getPlayer().getLocation()))) {
            if (e.getPlayer().hasMetadata("noCombatLog")) {
                e.getPlayer().removeMetadata("noCombatLog", Arkkit.get());
                return;
            }
            if (spawnNPC && e.getPlayer().getHealth() >= 2.0) {
                Player pKiller;
                if (e.getPlayer().getWorld().getName().equals("world_duels")) {
                    return;
                }
                if (combatNPCs.containsKey(e.getPlayer().getUniqueId().toString())) {
                    Bukkit.getLogger().info("[Arkkit (CombatLog)] Skipping Combat NPC spawn for " + e.getPlayer() + " due to existing NPC: " + combatNPCs.get(e.getPlayer().getUniqueId().toString()));
                    return;
                }
                String playerName = e.getPlayer().getName();
                String playerUUID = e.getPlayer().getUniqueId().toString();
                NPC n = CitizensAPI.getNPCRegistry().createNPC(EntityType.ZOMBIE, ChatColor.RED.toString() + ChatColor.BOLD + "*OFFLINE* " + ChatColor.RESET + playerName);
                Player p = e.getPlayer();
                Bukkit.getLogger().info("[Arkkit (CombatLog)] Attempting to spawn combatNPC for player " + p.getName() + " at " + p.getLocation().getBlockX() + "," + p.getLocation().getBlockY() + "," + p.getLocation().getBlockZ() + " with HP: " + p.getHealth());
                Location npcLocation = p.getLocation();
                if (e.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END && e.getPlayer().getLocation().getY() < 35.0 && p.hasMetadata("lastPlayerDamagerUUID") && (pKiller = Bukkit.getPlayer((String) p.getMetadata("lastPlayerDamagerUUID").get(0).value())) != null && pKiller.isOnline()) {
                    npcLocation = pKiller.getLocation().add(0.0, 1.0, 0.0);
                }
                n.spawn(npcLocation);
                Zombie z = (Zombie) n.getEntity();
                z.setMaxHealth(p.getMaxHealth());
                z.setHealth(p.getHealth());
                z.setCustomNameVisible(true);
                z.setBaby(false);
                z.setVillager(true);
                z.setCanPickupItems(false);
                z.setRemoveWhenFarAway(false);
                z.setNoDamageTicks(0);
                z.setFireTicks(0);
                if (e.getPlayer().hasMetadata("resilienceEnchant")) {
                    z.setMetadata("resilienceEnchant", new FixedMetadataValue(Arkkit.get(), e.getPlayer().getMetadata("resilienceEnchant").get(0).asInt()));
                }
                ArrayList<ItemStack> loot = new ArrayList<ItemStack>();
                for (ItemStack i : p.getInventory().getContents().clone()) {
                    if (i == null || i.getTypeId() == 0) continue;
                    loot.add(i);
                }
                for (ItemStack i : p.getEquipment().getArmorContents().clone()) {
                    if (i == null || i.getTypeId() == 0) continue;
                    loot.add(i);
                }
                n.getEntity().setMetadata("combatNPC_PlayerName", new FixedMetadataValue(Arkkit.get(), playerName));
                n.getEntity().setMetadata("combatNPC_PlayerUUID", new FixedMetadataValue(Arkkit.get(), e.getPlayer().getUniqueId().toString()));
                n.getEntity().setMetadata("combatNPC_ID", new FixedMetadataValue(Arkkit.get(), n.getId()));
                n.getEntity().setMetadata("combatNPC_TimeoutTime", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis() + CombatLog.getTimeLeftInCombat(p)));
                n.getEntity().setMetadata("combatNPC_Faction", new FixedMetadataValue(Arkkit.get(), FactionUtils.getFaction(p, true)));
                n.setFlyable(false);
                n.setProtected(false);
                combatNPCs.put(playerUUID, n);
                combatNPCs_loot.put(playerUUID, loot.toArray(new ItemStack[loot.size()]));
                for (Entity ent : e.getPlayer().getNearbyEntities(48.0, 48.0, 48.0)) {
                    if (!(ent instanceof Player)) continue;
                    ent.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + e.getPlayer().getName() + " has logged out while in combat, kill their NPC before they reconnect!");
                }
                Bukkit.getLogger().info("[Arkkit (CombatLog)] Spawned combatNPC for player " + p.getName() + " at " + p.getLocation().getBlockX() + "," + p.getLocation().getBlockY() + "," + p.getLocation().getBlockZ() + " with HP: " + p.getHealth() + " / Inventory: " + combatNPCs_loot.get(playerUUID));
            } else {
                Bukkit.getLogger().info("[Arkkit (CombatLog)] Killing " + e.getPlayer().getName() + " due to logging out while still in cl_combat!");
                e.getPlayer().setHealth(0.0);
                for (Entity ent : e.getPlayer().getNearbyEntities(64.0, 64.0, 64.0)) {
                    if (!(ent instanceof Player)) continue;
                    ent.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + e.getPlayer().getName() + " was killed due to logging out in combat.");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p;
            e.getEntity().setMetadata("lastDeathEvent", new FixedMetadataValue(Arkkit.get(), MinecraftServer.currentTick));
            if (!e.getEntity().hasMetadata("NPC") && combatNPCs.containsKey((p = (Player) e.getEntity()).getUniqueId().toString())) {
                NPC n;
                String pName = p.getName();
                Bukkit.getLogger().info("[Arkkit (CombatLog)] Adding Player:" + p.getName() + ", UUID:" + p.getUniqueId().toString() + " to playersToKill due to NPC EntityDeathEvent.");
                playersToKill.add(p.getUniqueId().toString());
                combatNPCs.remove(p.getUniqueId().toString());
                combatNPCs_loot.remove(p.getUniqueId().toString());
                int id = e.getEntity().hasMetadata("combatNPC_ID") ? e.getEntity().getMetadata("combatNPC_ID").get(0).asInt() : -1;
                NPC nPC = n = id != -1 ? CitizensAPI.getNPCRegistry().getById(id) : CitizensAPI.getNPCRegistry().getNPC(e.getEntity());
                if (n != null) {
                    n.despawn();
                    n.destroy();
                    CitizensAPI.getNPCRegistry().deregister(n);
                } else {
                    Bukkit.getLogger().info("[Arkkit (CombatLog)] Null NPC object for: " + pName + " onEntityDeath.");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityCombust(final EntityCombustEvent e) {
        if (e.getEntity().hasMetadata("combatNPC_PlayerName")) {
            e.setCancelled(true);
            e.setDuration(0);
            Bukkit.getScheduler().runTaskLater(Arkkit.get(), new Runnable() {

                @Override
                public void run() {
                    e.getEntity().setFireTicks(0);
                }
            }, 1L);
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent e) {
        if (e.getEntity().hasMetadata("combatNPC_PlayerName") && e.getAmount() > 0.0) {
            LivingEntity le = (LivingEntity) e.getEntity();
            e.setAmount(le.getMaxHealth() * 0.1);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPotionSplashEvent(PotionSplashEvent e) {
        for (LivingEntity le : e.getAffectedEntities()) {
            if (!le.hasMetadata("combatNPC_PlayerName")) continue;
            for (PotionEffect pe : e.getPotion().getEffects()) {
                if (!pe.getType().equals(PotionEffectType.HEAL)) continue;
                le.setHealth(Math.min(le.getMaxHealth(), le.getHealth() + le.getMaxHealth() * 0.1));
            }
            e.getAffectedEntities().remove(le);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onNPCDamage(EntityDamageEvent e) {
        Player p;
        if (e.getEntity() instanceof Player && !e.getEntity().hasMetadata("NPC") && combatNPCs.containsKey((p = (Player) e.getEntity()).getUniqueId().toString())) {
            e.setCancelled(true);
            e.setDamage(0.0);
            return;
        }
        if (e.getEntity().hasMetadata("combatNPC_PlayerName") && e.getCause() != EntityDamageEvent.DamageCause.CUSTOM) {
            Player p2;
            Faction faction;
            EntityDamageByEntityEvent edbee;
            LivingEntity le = (LivingEntity) e.getEntity();
            double dmgMod = 0.1;
            if (le.hasMetadata("resilienceEnchant")) {
                int resilienceLevel = le.getMetadata("resilienceEnchant").get(0).asInt();
                dmgMod -= (double) resilienceLevel * 0.015;
            }
            double dmg = le.getMaxHealth() * dmgMod;
            e.setCancelled(true);
            e.setDamage(0.0);
            if (e.getCause() == EntityDamageEvent.DamageCause.MAGIC) {
                return;
            }
            Faction f = (Faction) e.getEntity().getMetadata("combatNPC_Faction").get(0).value();
            String pName = e.getEntity().getMetadata("combatNPC_PlayerName").get(0).asString();
            if ((e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) && (edbee = (EntityDamageByEntityEvent) e).getDamager() instanceof Player && FactionUtil.getRelation(faction = FactionUtils.getFaction(p2 = (Player) edbee.getDamager()), f).isAtLeast(Relation.TRUCE)) {
                p2.sendMessage(ChatColor.RED + "** " + ChatColor.GRAY + "You cannot hurt " + ChatColor.RED + pName + "'s NPC" + ChatColor.GRAY + " due to your faction relationship.");
                return;
            }
            if (le.getHealth() > 0.0) {
                double newHealth = Math.max(0.0, le.getHealth() - dmg);
                le.setHealth(newHealth);
                le.playEffect(EntityEffect.HURT);
                le.setMetadata("combatNPC_TimeoutTime", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis() + 10000L));
                if (newHealth <= 0.0 && le.hasMetadata("combatNPC_PlayerName")) {
                    String playerUUID = le.getMetadata("combatNPC_PlayerUUID").get(0).asString();
                    ItemStack[] storedItems = combatNPCs_loot.get(playerUUID);
                    CombatLoggerDeathEvent combatLoggerDeathEvent = new CombatLoggerDeathEvent(le, playerUUID, pName, e, storedItems);
                    Bukkit.getPluginManager().callEvent(combatLoggerDeathEvent);
                    if (combatLoggerDeathEvent.getInventoryItems() != null) {
                        for (ItemStack i : combatLoggerDeathEvent.getInventoryItems()) {
                            if (i == null || i.getType() == Material.AIR) continue;
                            e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), i);
                        }
                        combatLoggerDeathEvent.getInventoryItems().clear();
                    }
                    if (!playersToKill.contains(playerUUID)) {
                        Bukkit.getLogger().info("[Arkkit (CombatLog)] Adding UUID:" + playerUUID + " to playersToKill due to NPC death.");
                        playersToKill.add(playerUUID);
                    }
                    combatNPCs.remove(playerUUID);
                    combatNPCs_loot.remove(playerUUID);
                    int id = e.getEntity().getMetadata("combatNPC_ID").get(0).asInt();
                    NPC n = CitizensAPI.getNPCRegistry().getById(id);
                    if (n != null) {
                        n.despawn();
                        n.destroy();
                        CitizensAPI.getNPCRegistry().deregister(n);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerVelocity(PlayerVelocityEvent e) {
        if (e.getPlayer().hasMetadata("combatNPC_PlayerName")) {
            e.setCancelled(true);
            e.setVelocity(new Vector(0, 0, 0));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        String uuid;
        if (!e.getPlayer().isOnline()) {
            return;
        }
        if (combatNPCs.containsKey(e.getPlayer().getUniqueId().toString())) {
            Bukkit.getLogger().info("[Arkkit (CombatLog)] Removing " + e.getPlayer().getName() + "'s NPC due to relog.");
            NPC n = combatNPCs.get(e.getPlayer().getUniqueId().toString());
            if (n.getEntity() instanceof LivingEntity) {
                LivingEntity le = (LivingEntity) n.getEntity();
                e.getPlayer().setHealth(Math.max(0.0, Math.min(20.0, le.getHealth())));
                if (!n.getEntity().getWorld().getName().startsWith("dungeon_")) {
                    e.getPlayer().teleport(n.getEntity().getLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
                }
            }
            n.despawn();
            n.destroy();
            CitizensAPI.getNPCRegistry().deregister(n);
            combatNPCs.remove(e.getPlayer().getUniqueId().toString());
            CombatLog.flagCombat(e.getPlayer());
        }
        if (playersToKill.contains(uuid = e.getPlayer().getUniqueId().toString())) {
            Bukkit.getLogger().info("[Arkkit (CombatLog)] Killing " + e.getPlayer().getName() + " due to their combat NPC dieing.");
            e.getPlayer().getInventory().clear();
            e.getPlayer().getEquipment().setHelmet(null);
            e.getPlayer().getEquipment().setChestplate(null);
            e.getPlayer().getEquipment().setLeggings(null);
            e.getPlayer().getEquipment().setBoots(null);
            if (!e.getPlayer().isDead()) {
                e.getPlayer().setHealth(0.0);
            }
            playersToKill.remove(uuid);
            if (playersToKill.contains(uuid)) {
                Bukkit.getLogger().info("[Arkkit (CombatLog] playersToKill still contains uuid: " + uuid + " despite .remove() call?");
                playersToKill.remove(uuid);
            }
            final Player p = e.getPlayer();
            CombatLog.unflagCombat(p);
            Bukkit.getScheduler().runTaskLater(Arkkit.get(), new Runnable() {

                @Override
                public void run() {
                    if (p.isOnline()) {
                        p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You logged out in combat and your NPC was killed while you were away.");
                    }
                }
            }, 60L);
            for (NPC n : CitizensAPI.getNPCRegistry()) {
                if (!n.isSpawned() || n.getEntity() == null || !n.getEntity().hasMetadata("combatNPC_PlayerUUID") || !n.getEntity().getMetadata("combatNPC_PlayerUUID").get(0).asString().equals(e.getPlayer().getUniqueId().toString()))
                    continue;
                Bukkit.getLogger().info("[Arkkit (CombatLog)] Invalid NPC found for UUID (" + e.getPlayer().getUniqueId() + "): " + n.getFullName() + "(" + n.getId() + ") / " + n.getEntity());
                n.despawn();
                n.destroy();
            }
        }
    }
}

