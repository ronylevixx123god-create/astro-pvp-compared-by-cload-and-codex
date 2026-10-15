package me.astropvp.astrospawners.utils;

import java.util.Arrays;
import java.util.List;

import me.astropvp.astrospawners.AstroSpawners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

import io.netty.util.internal.ThreadLocalRandom;

public class PlayerUtils {


    public static ItemStack getPlayerSkull(Player player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        item.setItemMeta(meta);
        return item;
    }

    public static List<Player> getPlayersWithinDistance(Location location, double distance) {
        List<Player> players = Lists.newArrayList();
        for (Player player : location.getWorld().getPlayers()) {
            double length = location.distance(player.getLocation());
            if (distance < length) {
                continue;
            }
            players.add(player);
        }
        return players;
    }


    public static void subtractExpFromPlayer(Player player, int amount) {
        int xp = player.getTotalExperience();
        player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0);
        player.giveExp(xp - amount);
    }

    public static int getInstancesOfItem(Player player, ItemStack instance) {
        int instances = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == instance.getType()
                    && item.getDurability() == instance.getDurability()) {
                instances += item.getAmount();
            }
        }
        return instances;
    }


    public static void removeInstanceOfItem(Player player, ItemStack instance, int amount) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {

            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() == instance.getType()
                    && item.getDurability() == instance.getDurability()) {
                if (item.getAmount() > amount) {
                    item.setAmount(item.getAmount() - amount);
                    break;
                } else if (item.getAmount() == amount) {
                    player.getInventory().setItem(i, null);
                    break;
                } else {
                    amount -= item.getAmount();
                    player.getInventory().setItem(i, null);
                }
            }
        }
    }

    public static void pushAwayEntity(LivingEntity center, Entity entity, double speed) {
        pushAwayEntity(center.getLocation(), entity, speed);
    }

    public static void pushAwayEntity(Location center, Entity entity, double speed) {
        org.bukkit.util.Vector unitVector = entity.getLocation().toVector().subtract(center.toVector());
        if (unitVector.length() != 0.0D) {
            unitVector.normalize();
        }
        entity.setVelocity(unitVector.multiply(speed));
    }

    public static boolean isPlayerInventoryFull(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                return false;
            }
        }
        return true;
    }

    public static int getEmptySpace(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                count++;
            }
        }
        return count;
    }




    private static List<ItemStack> getContents(Player player) {
        List<ItemStack> contents = Lists.newArrayList();
        if (player.getItemOnCursor() != null && player.getItemOnCursor().getType() != Material.AIR) {
            contents.add(player.getItemOnCursor());
        }

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                contents.add(item);
            }
        }
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && item.getType() != Material.AIR) {
                contents.add(item);
            }
        }
        return contents;
    }

    public static List<ItemStack> getDropsOfEntity(LivingEntity entity) {
        if (entity.hasMetadata("drop_no_items")) {
            return Lists.newArrayList();
        }
        if (entity instanceof Player) {
            return getContents((Player) entity);
        }
        if (entity instanceof Blaze) {
            return Arrays.asList(new ItemStack(Material.BLAZE_ROD, ThreadLocalRandom.current().nextInt(1, 4)));
        }
        if (entity instanceof IronGolem) {
            return Arrays.asList(new ItemStack(Material.IRON_BLOCK, ThreadLocalRandom.current().nextInt(1, 3)));
        }
        if (entity instanceof Villager) {
            return Arrays.asList(new ItemStack(Material.EMERALD_BLOCK));
        }
        if(entity instanceof MushroomCow) {
            return Arrays.asList(new ItemStack(Material.GOLD_BLOCK));
        }
        if(entity instanceof Pig) {
            return Arrays.asList(new ItemStack(entity.getFireTicks() > 0 ? Material.GRILLED_PORK :
                    Material.PORK, ThreadLocalRandom.current().nextInt(1, 3)));
        }
        if(entity instanceof Cow) {
            return Arrays.asList(new ItemStack(entity.getFireTicks() > 0 ? Material.COOKED_BEEF :
                    Material.RAW_BEEF, ThreadLocalRandom.current().nextInt(1, 3)));
        }
        if(entity instanceof Ghast) {
            return Arrays.asList(new ItemStack(Material.EMERALD_BLOCK));
        }
        if(entity instanceof MagmaCube) {
            return Arrays.asList(new ItemStack(Material.DIAMOND_BLOCK));
        }
        if(entity instanceof Creeper) {
            return Arrays.asList(new ItemStack(Material.TNT));
        }
        return Lists.newArrayList();
    }

    public static void neutralizeKnockback(LivingEntity entity) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(AstroSpawners.getInstance(), (Runnable) new BukkitRunnable() {
            @Override
            public void run() {
                entity.setVelocity(new Vector(0, 0, 0));
            }
        }, 1);
    }

    public static int getEntityExperience(LivingEntity entity) {
        if (entity instanceof Blaze) {
            return 250;
        }
        if (entity instanceof CaveSpider) {
            return 125;
        }
        if (entity instanceof Skeleton || entity instanceof Zombie) {
            return 100;
        }
        if (entity instanceof Spider) {
            return 75;
        }
        if (entity instanceof Pig || entity instanceof Sheep || entity instanceof Wolf) {
            return 25;
        }
        return 0;
    }

    public static void clearContents(Player player) {
        player.setItemOnCursor(null);

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            player.getInventory().setItem(i, null);
        }
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

}