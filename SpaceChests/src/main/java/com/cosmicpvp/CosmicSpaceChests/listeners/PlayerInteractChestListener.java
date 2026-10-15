package com.cosmicpvp.CosmicSpaceChests.listeners;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.generators.ItemGenerator;
import com.cosmicpvp.CosmicSpaceChests.libs.ChestLootSelector;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.utils.MiscUtil;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.enchantments.util.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

public class PlayerInteractChestListener implements Listener {
    static CosmicSpaceChestsPlugin plugin;

    public PlayerInteractChestListener(CosmicSpaceChestsPlugin fdp) {
        plugin = fdp;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerOpenDropPackage(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().getType() == Material.CHEST) {
            ItemStack is = e.getItem();
            if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
                return;
            if (!CosmicSpaceChestsAPI.isDropPackageChest(is))
                return;
            String chestName = is.getItemMeta().getDisplayName();
            final Player pl = e.getPlayer();
            ItemRarity ir = null;
            if (chestName.startsWith(ChatColor.WHITE.toString()))
                ir = ItemRarity.SIMPLE;
            if (chestName.startsWith(ChatColor.GREEN.toString()))
                ir = ItemRarity.UNIQUE;
            if (chestName.startsWith(ChatColor.AQUA.toString()))
                ir = ItemRarity.ELITE;
            if (chestName.startsWith(ChatColor.YELLOW.toString()))
                ir = ItemRarity.ULTIMATE;
            if (chestName.startsWith(ChatColor.GOLD.toString()))
                ir = ItemRarity.LEGENDARY;
            if (chestName.startsWith(ChatColor.RED.toString()))
                ir = ItemRarity.GOD;
            if (ir != null) {
                e.setCancelled(true);
                e.setUseItemInHand(Event.Result.DENY);
                e.setUseInteractedBlock(Event.Result.DENY);
            } else {
                return;
            }
            if (getEmptySlots(pl) < 5) {
                pl.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You need 5 empty inventory slots to open a Cosmic Chest.");
                return;
            }
            if (getTarget(e.getPlayer()) != null) {
                e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot be aiming at an NPC while opening your Cosmic Chest.");
                return;
            }
            if (e.getPlayer().hasMetadata("lastInventoryCloseEvent")) {
                long tick = ((MetadataValue)pl.getMetadata("lastInventoryCloseEvent").get(0)).asLong();
                if (MinecraftServer.currentTick <= tick + 2L) {
                    Bukkit.getLogger().info("[Arkkit (InventoryRatelimit)] Blocking playerInteract (" + ((e
                            .getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) ? e.getItem().getItemMeta().getDisplayName() : e.getItem().getType().name()) + ") for " + e.getPlayer().getName() + " due to inventoryCloseEvent <= 2ticks ago.");
                    e.setCancelled(true);
                    e.setUseItemInHand(Event.Result.DENY);
                    e.setUseInteractedBlock(Event.Result.DENY);
                    Bukkit.getScheduler().runTaskLater((Plugin)Arkkit.get(), new Runnable() {
                        public void run() {
                            pl.updateInventory();
                        }
                    },  1L);
                    return;
                }
                e.getPlayer().removeMetadata("lastInventoryCloseEvent", (Plugin)Arkkit.get());
            }
            if (is.getAmount() > 1) {
                is.setAmount(is.getAmount() - 1);
                pl.setItemInHand(is);
            } else {
                pl.setItemInHand(new ItemStack(Material.AIR));
            }
            ChestLootSelector cls = new ChestLootSelector(pl, ir);
            pl.setMetadata("chestLootSelector", (MetadataValue)new FixedMetadataValue((Plugin)CosmicSpaceChestsPlugin.plugin, cls));
            cls.open();
        }
    }

    private int getEmptySlots(Player p) {
        int x = 0;
        for (ListIterator<ItemStack> listIterator = p.getInventory().iterator(); listIterator.hasNext(); ) {
            ItemStack i = listIterator.next();
            if (i == null || i.getType() == Material.AIR)
                x++;
        }
        return x;
    }

    public LivingEntity getTarget(Player player) {
        List<Entity> nearbyE = player.getNearbyEntities(5.0D, 5.0D, 5.0D);
        ArrayList<LivingEntity> livingE = new ArrayList<>();
        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity)
                livingE.add((LivingEntity)e);
        }
        BlockIterator bItr = new BlockIterator((LivingEntity)player, 5);
        while (bItr.hasNext()) {
            Block block = bItr.next();
            int bx = block.getX();
            int by = block.getY();
            int bz = block.getZ();
            for (LivingEntity e : livingE) {
                if (e.hasMetadata("NPC")) {
                    Location loc = e.getLocation();
                    double ex = loc.getX();
                    double ey = loc.getY();
                    double ez = loc.getZ();
                    if (bx - 0.75D <= ex && ex <= bx + 1.75D && bz - 0.75D <= ez && ez <= bz + 1.75D && (by - 1) <= ey && ey <= by + 2.5D)
                        return e;
                }
            }
        }
        return null;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasBlock() && e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b.getType() == Material.CHEST) {
                if (e.getPlayer().hasMetadata("spectator"))
                    return;
                if (CosmicSpaceChestsPlugin.chest_locations.containsKey(b.getLocation())) {
                    Player pl = e.getPlayer();
                    final Location loc = b.getLocation();
                    ItemRarity ir = CosmicSpaceChestsPlugin.chest_locations.getChestRarity(loc);
                    if (!SpaceFlareListener.getSpaceFlares().contains(loc)) {
                        if (ir == ItemRarity.LEGENDARY && Math.random() <= 0.75D) {
                            if (Math.random() < 0.4D)
                                spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.GHAST);
                            if (Math.random() < 0.6D) {
                                spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.PIG_ZOMBIE);
                                spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.SKELETON);
                            }
                            spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.PIG_ZOMBIE);
                            spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.SKELETON);
                            spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.SKELETON);
                        } else if (ir == ItemRarity.ULTIMATE) {
                            if (Math.random() <= 0.6D)
                                if ((new Random()).nextBoolean()) {
                                    spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.SKELETON);
                                } else {
                                    spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.GHAST);
                                }
                            spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.PIG_ZOMBIE);
                            spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.SKELETON);
                        }
                        String worldName = loc.getWorld().getName();
                        if (worldName.equals("world")) {
                            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "fquests add ENVOY_LOOTER " + pl.getName());
                            if (Bukkit.getPluginManager().isPluginEnabled("CosmicContests"))
                                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "contests add " + pl.getName() + " envBroken 1");
                        }
                        if (worldName.equalsIgnoreCase("world") || worldName.equalsIgnoreCase("world_arena"))
                            Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(pl, "ENVOY_LOOTER", new Object[] { loc }));
                    } else {
                        SpaceFlareListener.getSpaceFlares().remove(loc);
                    }
                    pl.getInventory().addItem(ItemGenerator.generate(ir));
                    CosmicSpaceChestsPlugin.chest_locations.removeChest(loc);
                    b.setType(Material.AIR);
                    pl.playSound(pl.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5F, 1.2F);
                    pl.getInventory().addItem(ItemGenerator.generate(ir));

                    e.setCancelled(true);
                    e.setUseItemInHand(Event.Result.DENY);

                    return;
                }
                boolean warzone = false;
                String region = MiscUtil.getRegionName(e.getClickedBlock().getLocation()).toLowerCase();
                boolean spawn = region.startsWith("spaw");

                if (!spawn && warzone && !region.contains("frozen_") && !region.contains("infernal_")) {
                    Chest c = (Chest)e.getClickedBlock().getState();
                    if (getItemCount(c.getBlockInventory()) <= 0) {
                        Player pl = e.getPlayer();
                        final Location loc = b.getLocation();
                        b.setType(Material.AIR);
                        pl.playSound(pl.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5F, 1.2F);

                        e.setCancelled(true);
                        e.setUseItemInHand(Event.Result.DENY);

                    }
                }
            }
        }
    }

    @EventHandler
    public void removeGlitchedChestItemsInventoryClose(InventoryCloseEvent e) {
        if (!e.getPlayer().getInventory().contains(Material.CHEST))
            return;
        //for (ItemStack is : (ItemStack[])e.getPlayer().getInventory().getContents().clone()) {
            //if (isGlitchedChest(is))
                //e.getPlayer().getInventory().remove(is);
        //}
    }

    @EventHandler
    public void removeGLitchedChestItemsPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().getInventory().contains(Material.CHEST))
            return;
        //for (ItemStack is : (ItemStack[])e.getPlayer().getInventory().getContents().clone()) {
            //if (isGlitchedChest(is))
                //e.getPlayer().getInventory().remove(is);
        //}
    }

    private boolean isGlitchedChest(ItemStack i) {
        return (i != null && i.getType() == Material.CHEST && i.hasItemMeta() && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().contains("???"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof Chest))
            return;
        if (CosmicSpaceChestsAPI.isEmpty(e.getInventory())) {
            Chest c = (Chest)e.getInventory().getHolder();
            final Block b = c.getBlock();
            if (CosmicSpaceChestsPlugin.chest_locations.containsKey(b.getLocation()) && b.getType() == Material.CHEST) {
                final Player pl = (Player)e.getPlayer();
                ItemRarity ir = CosmicSpaceChestsPlugin.chest_locations.getChestRarity(b.getLocation());
                if (!SpaceFlareListener.getSpaceFlares().contains(b.getLocation())) {
                    if (ir == ItemRarity.LEGENDARY && Math.random() <= 0.75D) {
                        if (Math.random() < 0.4D)
                            spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.GHAST);
                        if (Math.random() < 0.6D) {
                            spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.PIG_ZOMBIE);
                            spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.SKELETON);
                        }
                        spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.PIG_ZOMBIE);
                        spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.SKELETON);
                        spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.SKELETON);
                    } else if (ir == ItemRarity.ULTIMATE) {
                        if (Math.random() <= 0.6D)
                            if ((new Random()).nextBoolean()) {
                                spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.SKELETON);
                            } else {
                                spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.GHAST);
                            }
                        spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.PIG_ZOMBIE);
                        spawnEnvoyMonster(b.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.SKELETON);
                    }
                } else {
                    SpaceFlareListener.getSpaceFlares().remove(b.getLocation());
                }
                plugin.getServer().getScheduler().runTaskLater((Plugin)plugin, new Runnable() {
                    public void run() {
                        final Location loc = b.getLocation();
                        CosmicSpaceChestsPlugin.chest_locations.removeChest(loc);
                        if (b.getType() == Material.CHEST && !b.getWorld().getName().contains("duel")) {
                            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "fquests add ENVOY_LOOTER " + pl.getName());
                            Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(pl, "ENVOY_LOOTER", new Object[] { loc }));
                        }
                        b.setType(Material.AIR);
                        pl.getInventory().addItem(ItemGenerator.generate(ir));

                        pl.playSound(pl.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5F, 1.2F);

                    }
                },1L);
            }
        }
    }

    @EventHandler
    public void onEntityBlockChange(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock && ((FallingBlock)e.getEntity()).getMaterial() == Material.CHEST) {
            Location hit_loc = e.getEntity().getLocation();
            ItemRarity ir = null;
            boolean spaceFlare = e.getEntity().hasMetadata("spaceFlare");
            int chestAmount = e.getEntity().hasMetadata("spaceFlareChests") ? ((MetadataValue)e.getEntity().getMetadata("spaceFlareChests").get(0)).asInt() : 1;
            if (e.getEntity().hasMetadata("chestRarity"))
                ir = ItemRarity.valueOf(((MetadataValue)e.getEntity().getMetadata("chestRarity").get(0)).asString());
            e.getEntity().remove();
            e.setCancelled(true);
            Block b = hit_loc.getBlock();
            final Block f_b = b;
            b.setType(Material.CHEST);
            b.getWorld().playSound(b.getLocation(), Sound.ZOMBIE_WOODBREAK, 1.0F, 1.0F);
            Chest chest = (Chest)b.getState();
            ir = (ir == null) ? CosmicSpaceChestsAPI.getRandomItemRarity() : ir;
            int is_offset = 3;
            CosmicSpaceChestsPlugin.chest_locations.putChest(b.getLocation(), ir);
            boolean battlefield = b.getWorld().getName().contains("_arena");
            if (!spaceFlare) {
                List<ItemStack> items = battlefield ? CosmicSpaceChestsAPI.getItemList(ir, is_offset) : CosmicSpaceChestsAPI.getEnvoyItems(ir, is_offset);
                for (ItemStack is : items) {
                    chest.getBlockInventory().addItem(new ItemStack[] { is });
                }
            } else {
                for (int x = 0; x < chestAmount; x++) {
                    ItemStack i = CosmicSpaceChestsAPI.getDropPackageChest(ir);
                    chest.getBlockInventory().addItem(new ItemStack[] { i });
                }
                SpaceFlareListener.getSpaceFlares().add(chest.getLocation());
            }
            chest.update();
            Firework fw = (Firework)hit_loc.getWorld().spawnEntity(hit_loc, EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            Color c = Color.WHITE;
            if (ir == ItemRarity.SIMPLE)
                c = Color.WHITE;
            if (ir == ItemRarity.UNIQUE)
                c = Color.GREEN;
            if (ir == ItemRarity.ELITE)
                c = Color.AQUA;
            if (ir == ItemRarity.ULTIMATE)
                c = Color.YELLOW;
            if (ir == ItemRarity.LEGENDARY || ir == ItemRarity.GOD)
                c = Color.BLACK;
            FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(c).withFade(c).with(FireworkEffect.Type.STAR).trail(true).build();
            fwm.addEffect(effect);
            fwm.setPower(1);
            fw.setFireworkMeta(fwm);
            Location particle_loc = b.getLocation().subtract(0.0D, 2.0D, 0.0D);
            final int block_id = particle_loc.getBlock().getTypeId();

        }
    }




    private int getItemCount(Inventory inv) {
        int x = 0;
        for (ItemStack is : inv.getContents()) {
            if (is != null && is.getType() != Material.AIR)
                x++;
        }
        return x;
    }

    private void spawnEnvoyMonster(Location l, EntityType et) {

    }
}
