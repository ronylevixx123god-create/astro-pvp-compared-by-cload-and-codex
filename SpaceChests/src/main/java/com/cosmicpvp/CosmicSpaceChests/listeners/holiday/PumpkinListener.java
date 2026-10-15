package com.cosmicpvp.CosmicSpaceChests.listeners.holiday;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PumpkinListener
implements Listener {
    CosmicSpaceChestsPlugin plugin;
    private long lastDropped;

    public PumpkinListener(CosmicSpaceChestsPlugin plugi) {
        this.plugin = plugi;
    }

    @EventHandler
    public void noFireballFire(ExplosionPrimeEvent e) {
        if (e.getEntity() instanceof Fireball && e.getEntity().getWorld().getName().equals("world_arena")) {
            e.setFire(false);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("halloweenMonster")) {
            e.getDrops().clear();
        }
        if (this.lastDropped > System.currentTimeMillis()) {
            return;
        }
        if (e.getEntity().hasMetadata("pumpkinHarvest") && e.getEntity().getWorld().getName().equalsIgnoreCase("world_arena")) {
            e.getEntity().remove();
            this.lastDropped = System.currentTimeMillis() + 1500L;
        }
    }

    @EventHandler
    public void onEntityBlockChange(EntityChangeBlockEvent e) throws Exception {
        if (e.getEntity() instanceof FallingBlock && ((FallingBlock)e.getEntity()).getMaterial() == Material.PUMPKIN) {
            Block b;
            Location hit_loc = e.getEntity().getLocation();
            ItemRarity ir = this.getRandomItemRarity();
            e.getEntity().remove();
            e.setCancelled(true);
            final Block f_b = b = hit_loc.getBlock();
            b.setType(ir != ItemRarity.LEGENDARY ? Material.PUMPKIN : Material.JACK_O_LANTERN);
            b.getWorld().playSound(b.getLocation(), Sound.ZOMBIE_WOODBREAK, 1.0f, 1.0f);
            if (ir == ItemRarity.LEGENDARY) {
                this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.PIG_ZOMBIE);
            }
            CosmicSpaceChestsPlugin.chest_locations.putChest(b.getLocation(), ir);
            Firework fw = (Firework)hit_loc.getWorld().spawnEntity(hit_loc, EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            Color c = null;
            if (ir == ItemRarity.ELITE) {
                c = Color.YELLOW;
            }
            if (ir == ItemRarity.ULTIMATE) {
                c = Color.ORANGE;
            }
            if (ir == ItemRarity.LEGENDARY) {
                c = Color.BLACK;
            }
            FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(c).withFade(c).with(FireworkEffect.Type.STAR).trail(true).build();
            fwm.addEffect(effect);
            fwm.setPower(1);
            fw.setFireworkMeta(fwm);
            Location particle_loc = b.getLocation().subtract(0.0, 2.0, 0.0);
            final int block_id = particle_loc.getBlock().getTypeId();
            this.plugin.getServer().getScheduler().runTaskAsynchronously((Plugin)this.plugin, new Runnable(){

                @Override
                public void run() {
                    PacketPlayOutWorldEvent particles = new PacketPlayOutWorldEvent(2001, new BlockPosition(f_b.getX(), f_b.getY(), f_b.getZ()), block_id, false);
                    ((CraftServer)PumpkinListener.this.plugin.getServer()).getServer().getPlayerList().sendPacketNearby((double)f_b.getX(), (double)f_b.getY(), (double)f_b.getZ(), 32.0, ((CraftWorld)f_b.getWorld()).getHandle().dimension, (Packet)particles);
                }
            });
        }
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=false)
    public void onPlayerInteract(PlayerInteractEvent e) throws Exception {
        Block b;
        if (e.hasBlock() && e.getAction() == Action.LEFT_CLICK_BLOCK && ((b = e.getClickedBlock()).getType() == Material.PUMPKIN || b.getType() == Material.JACK_O_LANTERN) && CosmicSpaceChestsPlugin.chest_locations.containsKey((Object)b.getLocation())) {
            ItemStack[] items;
            Player pl = e.getPlayer();
            final Location loc = b.getLocation();
            ItemRarity ir = CosmicSpaceChestsPlugin.chest_locations.getChestRarity(b.getLocation());
            if (ir == ItemRarity.LEGENDARY && !pl.hasPermission("cosmicpvp.vip")) {
                pl.sendMessage((Object)ChatColor.RED + "You are not worthy to open this Legendary Chest...");
                pl.sendMessage((Object)ChatColor.GRAY + "Unlock the potential to open this chest at " + (Object)ChatColor.GOLD + "buy.astropvp.net");
                e.setCancelled(true);
                return;
            }
            Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(e.getPlayer(), "SPACE_PUMPKIN_BREAK", new Object[]{ir, b}));
            CosmicSpaceChestsPlugin.chest_locations.removeChest(loc);
            b.setType(Material.AIR);
            pl.playSound(pl.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5f, 1.2f);
            pl.playSound(pl.getLocation(), Sound.GHAST_MOAN, 0.8f, 0.4f);
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            this.plugin.getServer().getScheduler().runTaskAsynchronously((Plugin)this.plugin, new Runnable(){

                @Override
                public void run() {
                    PacketPlayOutWorldEvent particles = new PacketPlayOutWorldEvent(2001, new BlockPosition((int)Math.round(loc.getX()), (int)Math.round(loc.getY()), (int)Math.round(loc.getZ())), Material.PUMPKIN.getId(), false);
                    ((CraftServer)PumpkinListener.this.plugin.getServer()).getServer().getPlayerList().sendPacketNearby(loc.getX(), loc.getY(), loc.getZ(), 24.0, ((CraftWorld)loc.getWorld()).getHandle().dimension, (Packet)particles);
                }
            });
            if (ir == ItemRarity.LEGENDARY) {
                this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.GHAST);
                this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.BLAZE);
                this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.PIG_ZOMBIE);
                this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.PIG_ZOMBIE);
                this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.SKELETON);
                this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.SKELETON);
                this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.SKELETON);
            } else {
                if (ir == ItemRarity.ULTIMATE) {
                    if (new Random().nextBoolean()) {
                        this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.SKELETON);
                    } else {
                        this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.BLAZE);
                    }
                }
                this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.PIG_ZOMBIE);
                this.spawnHalloweenMonster(b.getLocation().add(0.0, 1.0, 0.0), EntityType.SKELETON);
            }
            for (ItemStack i : items = CosmicSpaceChestsAPI.getItems(ir, 4)) {
                b.getWorld().dropItemNaturally(b.getLocation(), i);
            }
            return;
        }
    }

    private void spawnHalloweenMonster(Location l, EntityType et) throws Exception {
        Blaze le = null;
        if (et == EntityType.BLAZE) {
            Blaze b = (Blaze)l.getWorld().spawnEntity(l, EntityType.BLAZE);
            b.setMaxHealth(b.getMaxHealth() * 2.0);
            b.setHealth(b.getMaxHealth());
            b.setCustomName(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Halloween Nightmare");
            b.setCustomNameVisible(true);
            le = b;
        } else if (et == EntityType.PIG_ZOMBIE) {
            PigZombie b = (PigZombie)l.getWorld().spawnEntity(l, EntityType.PIG_ZOMBIE);
            b.setMaxHealth(b.getMaxHealth() * 2.0);
            b.setHealth(b.getMaxHealth());
            b.setBaby(false);
            b.setAngry(true);
            b.setCustomName(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Halloween Monster");
            b.setCustomNameVisible(true);
            le = (Blaze) b;
        } else if (et == EntityType.SKELETON) {
            Skeleton b = (Skeleton)l.getWorld().spawnEntity(l, EntityType.SKELETON);
            b.setSkeletonType(Skeleton.SkeletonType.WITHER);
            b.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
            b.setMaxHealth(b.getMaxHealth() * 2.0);
            b.setHealth(b.getMaxHealth());
            b.setCustomName(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Halloween Demon");
            b.setCustomNameVisible(true);
            le = (Blaze) b;
        } else if (et == EntityType.GHAST) {
            Ghast b = (Ghast)l.getWorld().spawnEntity(l, EntityType.GHAST);
            b.setMaxHealth(b.getMaxHealth() * 5.0);
            b.setHealth(b.getMaxHealth());
            b.setCustomName(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Halloween Ghost");
            b.setCustomNameVisible(true);
            le = (Blaze) b;
        }
        le.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, new Random().nextInt(3)));
        le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, new Random().nextInt(2)));
        if (new Random().nextBoolean()) {
            le.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
        } else {
            le.getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
        }
        le.getEquipment().setHelmet(new ItemStack(new Random().nextBoolean() ? Material.IRON_HELMET : Material.DIAMOND_HELMET));
        le.getEquipment().setChestplate(new ItemStack(new Random().nextBoolean() ? Material.IRON_CHESTPLATE : Material.DIAMOND_CHESTPLATE));
        le.getEquipment().setLeggings(new ItemStack(new Random().nextBoolean() ? Material.IRON_LEGGINGS : Material.DIAMOND_LEGGINGS));
        le.getEquipment().setBoots(new ItemStack(new Random().nextBoolean() ? Material.IRON_BOOTS : Material.DIAMOND_BOOTS));
        le.setCanPickupItems(false);
        le.setMetadata("halloweenMonster", (MetadataValue)new FixedMetadataValue((Plugin)CosmicSpaceChestsPlugin.plugin, (Object)true));

    }

    private ItemRarity getRandomItemRarity() {
        int x = new Random().nextInt(1000);
        if (x <= 500) {
            return ItemRarity.ELITE;
        }
        if (x <= 750) {
            return ItemRarity.ULTIMATE;
        }
        if (x <= 950) {
            return ItemRarity.LEGENDARY;
        }
        return ItemRarity.ELITE;
    }
}

