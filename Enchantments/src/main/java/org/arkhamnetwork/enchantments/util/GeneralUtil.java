package org.arkhamnetwork.enchantments.util;

import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class GeneralUtil {
    public static Material[] swords;
    public static Material[] swords_and_bow;
    public static Material[] armor;
    public static Material[] helmets;
    public static Material[] helmets_and_boots;
    public static Material[] chestplates;
    public static Material[] leggings;
    public static Material[] boots;
    public static Material[] pickaxe;
    public static Material[] tools;
    public static Material[] axe;
    public static Material[] weapons;
    public static Material[] weapons_and_tools;

    static {
        GeneralUtil.swords = new Material[]{Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD};
        GeneralUtil.swords_and_bow = new Material[]{Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD, Material.BOW};
        GeneralUtil.armor = new Material[]{Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS};
        GeneralUtil.helmets = new Material[]{Material.LEATHER_HELMET, Material.GOLD_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET};
        GeneralUtil.helmets_and_boots = new Material[]{Material.LEATHER_HELMET, Material.GOLD_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET, Material.LEATHER_BOOTS, Material.GOLD_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS};
        GeneralUtil.chestplates = new Material[]{Material.LEATHER_CHESTPLATE, Material.GOLD_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE};
        GeneralUtil.leggings = new Material[]{Material.LEATHER_LEGGINGS, Material.GOLD_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS};
        GeneralUtil.boots = new Material[]{Material.LEATHER_BOOTS, Material.GOLD_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS};
        GeneralUtil.pickaxe = new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE};
        GeneralUtil.tools = new Material[]{Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE, Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE, Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE, Material.GOLD_SPADE, Material.DIAMOND_SPADE, Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE, Material.FISHING_ROD};
        GeneralUtil.axe = new Material[]{Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE};
        GeneralUtil.weapons = new Material[]{Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD, Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE, Material.BOW};
        GeneralUtil.weapons_and_tools = new Material[]{Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD, Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE, Material.BOW, Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE, Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE, Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE, Material.GOLD_SPADE, Material.DIAMOND_SPADE, Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE};
    }

    public static boolean isKOTHWorld(final World world) {
        return world.getName().equals("world_koth");
    }

    public static boolean healPlayer(final LivingEntity entity, final double healAmount) {
        return setPlayerHealth(entity, entity.getHealth() + healAmount);
    }

    public static boolean reducePlayerHealth(final Player entity, final double damageAmount) {
        return setPlayerHealth((LivingEntity) entity, Math.max(0.0, entity.getHealth() - damageAmount));
    }

    public static boolean setPlayerHealth(final LivingEntity entity, double newHealth) {
        final double health = entity.getHealth();
        if ((health <= 0.0 || entity.isDead()) && entity instanceof Player) {
            return false;
        }
        if (newHealth > entity.getMaxHealth()) {
            newHealth = entity.getMaxHealth();
        }
        entity.setHealth(newHealth);
        return true;
    }

    public static boolean isUsingAxe(LivingEntity attacker) {
        if (attacker instanceof Player) {
            return ((Player)attacker).getItemInHand() != null && ((Player)attacker).getItemInHand().getType().name().endsWith("_AXE");
        }
        return false;
    }

    public static ItemStack getPlayerHead(final String p_name) {
        final ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        final SkullMeta sm = (SkullMeta) is.getItemMeta();
        sm.setOwner(p_name);
        sm.setDisplayName(ChatColor.WHITE + "Skull of " + p_name);
        is.setItemMeta((ItemMeta) sm);
        return is;
    }

    public static boolean isArmor(final ItemStack i) {
        for (final Material m : GeneralUtil.helmets) {
            if (i.getType() == m) {
                return true;
            }
        }
        for (final Material m : GeneralUtil.chestplates) {
            if (i.getType() == m) {
                return true;
            }
        }
        for (final Material m : GeneralUtil.leggings) {
            if (i.getType() == m) {
                return true;
            }
        }
        for (final Material m : GeneralUtil.boots) {
            if (i.getType() == m) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWeapon(final ItemStack i) {
        for (final Material m : GeneralUtil.weapons) {
            if (i.getType() == m) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSword(final ItemStack i) {
        for (final Material m : GeneralUtil.swords) {
            if (i.getType() == m) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTool(final ItemStack i) {
        for (final Material m : GeneralUtil.tools) {
            if (i.getType() == m) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAlly(final Player p1, final Player p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        if (p1.hasMetadata("in_duel") || p2.hasMetadata("in_duel")) {
            return false;
        }
        final FPlayer fp1 = (FPlayer) FPlayers.getInstance().getByPlayer(p1);

        final FPlayer fp2 = (FPlayer) FPlayers.getInstance().getByPlayer(p2);
        if (!fp1.hasFaction() || !fp2.hasFaction()) {
            return false;
        }
        final Relation r1 = fp1.getFaction().getRelationTo(fp2.getFaction());
        final Relation r2 = fp2.getFaction().getRelationTo(fp1.getFaction());
        return (r1.isAlly() || r1.isMember()) && (r2.isAlly() || r2.isMember());
    }

    public static boolean isAtleastTruce(final Player p1, final Player p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        if (p1.hasMetadata("in_duel") || p2.hasMetadata("in_duel")) {
            return false;
        }
        final FPlayer fp1 = (FPlayer) FPlayers.getInstance().getByPlayer(p1);
        final FPlayer fp2 = (FPlayer) FPlayers.getInstance().getByPlayer(p2);
        if (!fp1.hasFaction() || !fp2.hasFaction()) {
            return false;
        }
        final Relation r1 = fp1.getFaction().getRelationTo((RelationParticipator) fp2.getFaction());
        final Relation r2 = fp2.getFaction().getRelationTo((RelationParticipator) fp1.getFaction());
        return (r1.isAlly() || r1.isMember() || r1.isTruce()) && (r2.isAlly() || r2.isMember() || r2.isTruce());
    }

    public static boolean isAtleastTruce(final Faction faction, final Player p2) {
        if (p2 == null) {
            return false;
        }
        if (p2.hasMetadata("in_duel")) {
            return false;
        }
        final FPlayer fp2 = (FPlayer) FPlayers.getInstance().getByPlayer(p2);
        if (faction == null || !fp2.hasFaction()) {
            return false;
        }
        final Relation r1 = fp2.getFaction().getRelationTo((RelationParticipator) faction);
        return r1.isAlly() || r1.isMember() || r1.isTruce();
    }

    public static boolean canEffectEntity(final Entity entity) {
        return !(entity instanceof EnderDragon);
    }

    public static boolean isEnemy(final Faction faction, final Player p2) {
        if (p2 == null) {
            return true;
        }
        if (p2.hasMetadata("in_duel")) {
            return true;
        }
        final FPlayer fp2 = (FPlayer) FPlayers.getInstance().getByPlayer(p2);
        if (faction == null || !fp2.hasFaction()) {
            return true;
        }
        final Relation r2 = fp2.getFaction().getRelationTo((RelationParticipator) faction);
        return r2.isEnemy();
    }

    public static boolean isEnemy(final Player p1, final Player p2) {
        if (p1 == null || p2 == null) {
            return true;
        }
        if (p1.hasMetadata("in_duel") || p2.hasMetadata("in_duel")) {
            return true;
        }
        final FPlayer fp1 = (FPlayer) FPlayers.getInstance().getByPlayer(p1);
        final FPlayer fp2 = (FPlayer) FPlayers.getInstance().getByPlayer(p2);
        if (!fp1.hasFaction() || !fp2.hasFaction()) {
            return true;
        }
        final Relation r1 = fp1.getFaction().getRelationTo((RelationParticipator) fp2.getFaction());
        final Relation r2 = fp2.getFaction().getRelationTo((RelationParticipator) fp1.getFaction());
        return r1.isEnemy() || r2.isEnemy();
    }

    public static boolean isEffectedByRage(final LivingEntity le) {
        return le.hasMetadata("effectedByRage") && System.currentTimeMillis() - le.getMetadata("effectedByRage").get(0).asLong() <= 200L;
    }

    public static boolean isEffectedByDevour(final LivingEntity le) {
        return le.hasMetadata("effectedByDevour") && System.currentTimeMillis() - le.getMetadata("effectedByDevour").get(0).asLong() <= 200L;
    }

    public static void pushBackEntity(final LivingEntity damager, final Entity toPush, final int knockbackLevel) {
        final Vector velocity = toPush.getVelocity();
        final float yaw = damager.getLocation().getYaw();
        final Vector push = new Vector((double) (-MathHelper.sin(yaw * 3.1415927f / 180.0f) * knockbackLevel * 0.5f), 0.1, (double) (MathHelper.cos(yaw * 3.1415927f / 180.0f) * knockbackLevel * 0.5f));
        velocity.add(push);
        if (velocity.getX() != Double.NaN) {
            toPush.setVelocity(velocity);
        }
    }

    public static void pushAwayEntity(final LivingEntity center, final Entity entity, final double speed) {
        pushAwayEntity(center.getLocation(), entity, speed);
    }

    public static void pushAwayEntity(final Location center, final Entity entity, final double speed) {
        if (entity.hasMetadata("inDungeonParkour")) {
            Bukkit.getLogger().info("[Enchantments] Cancelling pushAwayEntity(" + entity + ", " + speed + ") due to inDungeonParkour: " + entity.getMetadata("inDungeonParkour").get(0).asString());
            return;
        }
        entity.setMetadata("pushAwayEntityEvent", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) MinecraftServerUtils.getCurrentServerTick()));
        final Vector unitVector = entity.getLocation().toVector().subtract(center.toVector());
        if (unitVector.length() != 0.0) {
            unitVector.normalize();
        }
        entity.setVelocity(unitVector.multiply(speed));
    }
}
