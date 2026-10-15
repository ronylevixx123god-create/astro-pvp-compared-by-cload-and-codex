package me.ifamasssxd.cosmicconquest.listeners;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.LandClaimEvent;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossDespawnEvent;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicconquest.CosmicConquestAPI;
import me.ifamasssxd.cosmicconquest.CosmicConquests;
import me.ifamasssxd.cosmicconquest.struct.ChestData;
import me.ifamasssxd.cosmicconquest.struct.ConquestManager;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConquestListener implements Listener {
    private long lastHit = 0L;

    @EventHandler
    public void onChunkClaim(LandClaimEvent event) {
        Chunk chunk = event.getLocation().getWorld().getChunkAt((int)event.getLocation().getX(), (int)event.getLocation().getZ());
        ConquestManager manager = CosmicConquests.get().getManager();
        if (manager.getNextSpawnLocation() != null && manager.getNextSpawnLocation().getChunk().getX() == chunk.getX() && manager.getNextSpawnLocation().getChunk().getZ() == chunk.getZ()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "This chunk is currently hosting a Conquest!");
            return;
        }
        for (Location location : CosmicConquests.get().getManager().getConquestChests().keySet()) {
            Chunk ch = location.getChunk();
            if (chunk.getX() == ch.getX() && ch.getZ() == chunk.getZ()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "This chunk is currently hosting a Conquest!");
                return;
            }
        }
        FLocation current = new FLocation(event.getLocation().getWorldName(), FLocation.blockToChunk((int)event.getLocation().getX()), FLocation.blockToChunk((int)event.getLocation().getZ()));
        if (manager.getWarzoned().values().stream().anyMatch(list -> list.contains(current))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "This chunk is currently hosting a Conquest!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        FLocation to = new FLocation(event.getToBlock());
        ConquestManager manager = CosmicConquests.get().getManager();
        if (manager != null && manager.getConquestChests().size() > 0 && manager.getWarzoned().values().stream().anyMatch(list -> list.contains(to)))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockFromTo(BlockFormEvent event) {
        FLocation to = new FLocation(event.getBlock());
        ConquestManager manager = CosmicConquests.get().getManager();
        if (manager != null && manager.getConquestChests().size() > 0 && manager.getWarzoned().values().stream().anyMatch(list -> list.contains(to)))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (CosmicConquests.get().getManager().getConquestChests().containsKey(event.getBlock().getLocation()) || event.getBlock().getLocation().equals(CosmicConquests.get().getManager().getNextSpawnLocation()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBossDespawn(BossDespawnEvent event) {
        ConquestManager manager = CosmicConquests.get().getManager();
        if (manager.getBosses().contains(event.getEntity())) {
            LivingEntity lE = (LivingEntity)event.getEntity();
            for (Entity ent : manager.getBosses())
                ent.remove();
            manager.getBosses().clear();
            Iterator<Map.Entry<Location, ChestData>> iterator2 = manager.getConquestChests().entrySet().iterator();
            if (iterator2.hasNext()) {
                Map.Entry<Location, ChestData> loc = iterator2.next();
                ((ChestData)loc.getValue()).setHits(maxHits);
            }
            Bukkit.getLogger().info("Resetting Conquest hits to " + maxHits + " due to bosses despawning..");
        }
    }

    private boolean isLagging() {
        double[] tpss = (MinecraftServer.getServer()).recentTps;
        if (tpss.length < 2)
            return false;
        double mostRecentTPS = tpss[1];
        return (mostRecentTPS <= 15.0D);
    }

    @EventHandler
    public void onPLayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST) {
            Block block = event.getClickedBlock();
            ConquestManager manager = CosmicConquests.get().getManager();
            Player player = event.getPlayer();
            ChestData hitsLeft = (ChestData)manager.getConquestChests().get(block.getLocation());
            if (hitsLeft != null) {
                event.setCancelled(true);
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (isLagging()) {
                        player.sendMessage(ChatColor.RED + "The server is currently under heavy load and damaging Conquest Chests have been disabled until performance improves.");
                        Bukkit.getLogger().info("Not letting " + player.getName() + " interact with Conquest Chest due to < 15 TPS.");
                        return;
                    }
                    try {
                        CombatLog.flagCombat(player);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (hitsLeft.getHits() == 1) {
                        List<ItemStack> itemsToDrop = (List<ItemStack>)manager.getLoot().remove(block.getLocation());
                        manager.getConquestChests().remove(block.getLocation());
                        if (block.getType() == Material.CHEST) {
                            Chest chest = (Chest)block.getState();
                            chest.getInventory().clear();
                            chest.update();
                        }
                        block.setType(Material.AIR);
                        itemsToDrop.forEach(i -> block.getWorld().dropItem(block.getLocation().clone().add(0.5D, 0.5D, 0.5D), i));
                        itemsToDrop.clear();
                        block.getWorld().playEffect(block.getLocation().add(0.5D, 0.0D, 0.5D), Effect.STEP_SOUND, Material.CHEST.getId());
                        block.getWorld().playSound(block.getLocation(), Sound.WITHER_SHOOT, 1.0F, 0.85F);
                        FPlayer fplayer = (FPlayer)FPlayers.getInstance().getByPlayer(player);
                        Faction faction = fplayer.getFaction();
                        String name = ChatColor.AQUA + ((faction != null && faction.isNormal()) ? (faction.getTag() + " ") : "") + ChatColor.AQUA + player.getName();
                        if (manager.getLastConquer() != null)
                            manager.getLastConquer().setWhoUnlocked(player.getName());
                        Location loc = block.getLocation();
                        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CosmicConquests.get(), () -> manager.unclaimWarzone(loc), (1200 * removeDelay));
                        Bukkit.broadcastMessage(ChatColor.WHITE + ChatColor.BOLD.toString() + "(!) " + ChatColor.GRAY + ChatColor.UNDERLINE.toString() + name + ChatColor.GRAY + " has unlocked the Conquest Chest at " + CosmicConquests.get().getManager().getFormattedLocation(ChatColor.WHITE, block.getLocation()) + ChatColor.GRAY + " its loot is spilling everywhere!");
                        for (Entity ent : manager.getBosses()) {
                            if (ent != null)
                                ent.remove();
                        }
                        manager.getBosses().clear();
                        CosmicConquests.get().getConfig().set("unclaimed", Boolean.valueOf(false));
                        CosmicConquests.get().saveConfig();
                        Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(player, "CONQUEST_BREAK", new Object[] { Integer.valueOf(hitsLeft.getHits()) }));
                        return;
                    }
                    boolean onCooldown = (System.currentTimeMillis() - this.lastHit <= 1000L);
                    if (!onCooldown) {
                        int prevHits = hitsLeft.getHits();
                        if (hitsLeft.getHits() == maxHits) {
                            Location location = block.getLocation().add(0.0D, 1.0D, 0.0D);
                            for (BossType type : hitsLeft.getBossesTypeSpawn()) {
                                Bukkit.getLogger().info("Spawning " + type + " due to first hit!");
                                type.createInstance(location).onBossSpawn();
                            }
                            for (Boss boss : Boss.bosses.values()) {
                                if (boss != null && !boss.getEntity().isDead() && location.equals(boss.getLocation())) {
                                    manager.getBosses().add(boss.getEntity());
                                    Bukkit.getLogger().info("Found boss to register!");
                                    boss.getEntity().setMetadata("spawnLocation", (MetadataValue)new FixedMetadataValue((Plugin)CosmicConquests.get(), block.getLocation()));
                                }
                            }
                            player.setVelocity(player.getLocation().getDirection().multiply(-2).setY(0.4D));
                        } else {
                            player.setVelocity(player.getLocation().getDirection().multiply(-1.6D).normalize());
                        }
                        if (player.isOp() && player.isSneaking() && (player.getName().equals("iFamasssxD") || player.getName().equals("Xell_"))) {
                            if (hitsLeft.getHits() < 50) {
                                hitsLeft.setHits(1);
                            } else {
                                hitsLeft.setHits(hitsLeft.getHits() - 50);
                            }
                        } else {
                            hitsLeft.setHits(hitsLeft.getHits() - 1);
                        }
                        Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(player, "CONQUEST_DAMAGE", new Object[] {hitsLeft.getHits(), prevHits}));
                    }
                    player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5F, 1.4F);
                    try {
                        if (!onCooldown) {
                            this.lastHit = System.currentTimeMillis();
                            int maxBreaks = 10;
                            double percentLeft = hitsLeft.getHits() * 1.0D / maxHits * 1.0D;
                            int breaks = (int)Math.min(10.0D, Math.floor(10.0D - percentLeft * 10.0D));
                            PacketPlayOutBlockBreakAnimation animation = new PacketPlayOutBlockBreakAnimation(0, new BlockPosition(block.getX(), block.getY(), block.getZ()), breaks);
                            (((CraftPlayer)player).getHandle()).playerConnection.sendPacket(animation);
                            for (Entity ent2 : player.getNearbyEntities(64.0D, 64.0D, 64.0D)) {
                                if (ent2 instanceof Player)
                                    (((CraftPlayer)ent2).getHandle()).playerConnection.sendPacket(animation);
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (!onCooldown)
                        sendEasterChestOpen(player, hitsLeft.getHits(), true);
                    CosmicConquests.get().getManager().getConquestChests().put(block.getLocation(), hitsLeft);
                } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    sendEasterChestOpen(player, hitsLeft.getHits(), false);
                    player.sendMessage(ChatColor.GRAY + "Punch the Conquest Chest to open it!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem() && event.getItem() != null && (CosmicConquestAPI.isFactionToken(event.getItem()) || CosmicConquestAPI.isHeroicFactionToken(event.getItem())))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Chicken && event.getEntity().hasMetadata("easter_chicken")) {
            if (event.getEntity().getPassenger() != null && event.getEntity().getPassenger().isValid()) {
                ((LivingEntity)event.getEntity().getPassenger()).removePotionEffect(PotionEffectType.INVISIBILITY);
                ((LivingEntity)event.getEntity().getPassenger()).getEquipment().setHelmet(CosmicConquests.get().getManager().getChickenHead().clone());
            }
        } else if (event.getEntity() instanceof org.bukkit.entity.Zombie && event.getEntity().hasMetadata("easter_chicken")) {
            event.getDrops().clear();
            for (ItemStack drop : Lists.newArrayList(event.getDrops()))
                event.getDrops().remove(drop);
            if (Math.random() <= 0.75D)
                event.getDrops().add(new ItemStack(Material.RAW_CHICKEN, 1));
        }
        LivingEntity livingEntity = event.getEntity();
        ConquestManager manager = CosmicConquests.get().getManager();
        Boss boss = (Boss)Boss.bosses.get(livingEntity.getUniqueId());
        if (boss != null && event.getEntity().hasMetadata("boss")) {
            if (!boss.getEntity().hasMetadata("spawnLocation"))
                return;
            Location spawn = (Location)((MetadataValue)boss.getEntity().getMetadata("spawnLocation").get(0)).value();
            ChestData chestData = (ChestData)manager.getConquestChests().get(spawn);
            if (chestData != null) {
                Bukkit.getLogger().info("Removing " + boss.getBossType() + " from bosses that are alive at conquest!");
                chestData.getBossesTypeSpawn().remove(boss.getBossType());
            } else {
                Bukkit.getLogger().info("Chest data not found!");
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (event.getEntity().getItemStack() != null && event.getEntity().getItemStack().getType() == Material.EGG)
            for (Entity nearby : event.getEntity().getNearbyEntities(2.0D, 2.0D, 2.0D)) {
                if (nearby instanceof org.bukkit.entity.Chicken && nearby.hasMetadata("easter_chicken") && Math.random() <= 0.5D) {
                    event.setCancelled(true);
                    event.getEntity().remove();
                    Bukkit.getLogger().info("Removing easter egg from chicken...");
                }
            }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity.hasMetadata("easter_chicken"))
                event.setIntensity((LivingEntity)entity, 0.0D);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntrityExplode(EntityExplodeEvent event) {
        for (Block block : Lists.newArrayList(event.blockList())) {
            if (CosmicConquests.get().getManager().getConquestChests().containsKey(block.getLocation()))
                event.blockList().remove(block);
        }
    }

    private void sendEasterChestOpen(Player player, int hits, boolean nearby) {
        String msg = ChatColor.GRAY + ChatColor.BOLD.toString() + "** " + ChatColor.GRAY + "Conquest Chest " + ChatColor.AQUA + hits + ChatColor.GRAY + " / " + maxHits + " HP";
        player.sendMessage(msg);
        if (nearby)
            for (Entity ent : player.getNearbyEntities(26.0D, 24.0D, 26.0D)) {
                if (ent instanceof Player)
                    ((Player)ent).sendMessage(msg);
            }
    }

    public static int maxHits = 150;

    public static int removeDelay = 10;
}
