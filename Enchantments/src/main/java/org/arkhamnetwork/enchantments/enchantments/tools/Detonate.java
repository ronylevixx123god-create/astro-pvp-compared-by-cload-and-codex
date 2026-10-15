/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CoreProtectUtils
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.Faction
 *  com.massivecraft.factions.FactionWrapper
 *  com.massivecraft.factions.FactionWrappers
 *  com.massivecraft.factions.P
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.FactionPermission
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.LogTimer$TimerSubType
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  me.ifamasssxd.cosmicguishop.CosmicGUIShop
 *  net.minecraft.server.v1_7_R4.Blocks
 *  net.minecraft.server.v1_7_R4.Chunk
 *  net.minecraft.server.v1_7_R4.ChunkSection
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.CreatureSpawner
 *  org.bukkit.craftbukkit.v1_7_R4.CraftChunk
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.tools;

import com.cosmicpvp.cosmicutils.utils.CoreProtectUtils;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.*;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import de.inventivegames.particle.ParticleEffect;
import me.ifamasssxd.cosmicguishop.CosmicGUIShop;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.libs.EnchantBreakBlockEvent;
import org.arkhamnetwork.enchantments.mastery.MasteryAutoSell;
import org.arkhamnetwork.enchantments.util.BlockUtils;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Detonate
extends CustomEnchantment {
    private static ConcurrentHashMap<UUID, BlockFace> loggedBlockFaces = new ConcurrentHashMap();
    protected static List<Material> pickaxeMaterials;
    protected static List<Material> spadeMaterials;

    public Detonate() {
        super("Detonate", GeneralUtil.tools, 6);
        this.max = 9;
        this.base = 10.0;
        this.interval = 10.0;
        pickaxeMaterials = new ArrayList<Material>(Arrays.asList(new Material[]{Material.SANDSTONE, Material.STONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.SMOOTH_BRICK, Material.OBSIDIAN, Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.DIAMOND_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE, Material.EMERALD_ORE, Material.NETHERRACK}));
        spadeMaterials = new ArrayList<Material>(Arrays.asList(new Material[]{Material.DIRT, Material.SAND, Material.GRAVEL, Material.GRASS, Material.MYCEL, Material.CLAY}));
        Bukkit.getPluginManager().registerEvents((Listener)new DetonateListener(), (Plugin)Enchantments.getInstance());
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), () -> {
            this.heroicVersion = EnchantmentAPI.getEnchantment((String)"Atomic Detonate");
        }, 2L);
    }

    public void applyToolEffect(Player player, Block block, int enchantLevel, BlockEvent event) {
        BlockBreakEvent bbe;
        if (event instanceof BlockBreakEvent && !(bbe = (BlockBreakEvent)event).isCancelled()) {
            Material type = block.getType();
            if (type == Material.REDSTONE_COMPARATOR || type == Material.SPONGE || type == Material.REDSTONE_COMPARATOR_ON || type == Material.REDSTONE_COMPARATOR_OFF) {
                return;
            }
            this.explode(player, bbe.getBlock(), enchantLevel);
        }
    }

    public static void updateSavedBlockFace(Player p, BlockFace b) {
        loggedBlockFaces.put(p.getUniqueId(), b);
    }

    private BlockFace getBlockFace(Player p) {
        return loggedBlockFaces.containsKey(p.getUniqueId()) ? loggedBlockFaces.get(p.getUniqueId()) : null;
    }

    private void explode(Player pl, Block b, int level) {
        ItemStack pickaxeItem = pl.getItemInHand();
        boolean hasAutoSmelt = EnchantmentAPI.itemHasEnchantment((ItemStack)pl.getItemInHand(), (String)"Auto Smelt");
        boolean hasFuse = EnchantmentAPI.itemHasEnchantment((ItemStack)pl.getItemInHand(), (String)"Fuse");
        boolean telepathy = EnchantmentAPI.itemHasEnchantment((ItemStack)pl.getItemInHand(), (String)"Telepathy");
        boolean autoSell = EnchantmentAPI.itemHasEnchantment((ItemStack)pl.getItemInHand(), (String)"Auto Sell");
        boolean pickaxe = pl.getItemInHand().getType().name().contains("_PICKAXE");
        boolean spade = pl.getItemInHand().getType().name().contains("_SPADE");
        double autoSellMoney = 0.0;
        Set<Block> blocks = BlockUtils.getSquare(b, this.getBlockFace(pl), level);
        blocks.add(b);
        boolean brokeBlock = false;
        boolean tookDurability = false;
        FPlayer fp = null;
        for (Block bl : blocks) {
            BlockState state;
            Block above;
            Material aboveType;
            Material m = bl.getType();
            if (m == Material.NOTE_BLOCK || m == Material.JUKEBOX || m == Material.BEACON || m == Material.LADDER || m == Material.WATER || m == Material.STATIONARY_WATER || m == Material.LAVA || m == Material.STATIONARY_LAVA || m == Material.SIGN || m == Material.SIGN_POST || m == Material.AIR || m == Material.BEDROCK || m == Material.OBSIDIAN || m == Material.SPONGE || m == Material.IRON_DOOR || m == Material.IRON_DOOR_BLOCK || m == Material.WOODEN_DOOR || m == Material.WOOD_DOOR || m == Material.HOPPER || m == Material.MOB_SPAWNER || m == Material.ANVIL || m == Material.REDSTONE_COMPARATOR || m == Material.REDSTONE_COMPARATOR_OFF || m == Material.REDSTONE_COMPARATOR_ON || pickaxeMaterials.contains((Object) m) && !pickaxe || spadeMaterials.contains((Object) m) && !spade || (aboveType = (above = bl.getRelative(BlockFace.UP)).getType()) == Material.REDSTONE_COMPARATOR || aboveType == Material.REDSTONE_COMPARATOR_OFF || aboveType == Material.REDSTONE_COMPARATOR_ON || aboveType == Material.DIODE || aboveType == Material.DIODE_BLOCK_OFF || aboveType == Material.DIODE_BLOCK_ON) continue;
            FLocation floc = new FLocation(bl.getLocation());
            Faction f = FactionUtils.getFactionAt((FLocation)floc);
            if (!f.isNone()) {
                if (fp == null) {
                    fp = (FPlayer)FPlayers.getInstance().getByPlayer(pl);
                }
            }
            if (f.isWarZone()) continue;
            try {
                ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), bl.getLocation().add(0.0, 0.5, 0.0), 0.025f, 0.025f, 0.025f, 0.1f, 1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if (bl.getDrops(pl.getItemInHand()).size() > 0) {
                double price;
                ItemStack loot = null;
                try {
                    loot = (ItemStack)bl.getDrops(pl.getItemInHand()).toArray()[0];
                }
                catch (Exception err) {
                    continue;
                }
                EnchantBreakBlockEvent enchantEvent = new EnchantBreakBlockEvent(pl, bl, m);
                Bukkit.getPluginManager().callEvent((Event)enchantEvent);
                if (enchantEvent.isCancelled()) continue;
                if (loot.getType().name().startsWith("REDSTONE_COMPARATOR")) {
                    loot.setAmount(1);
                }
                if (hasAutoSmelt && (m == Material.IRON_ORE || m == Material.GOLD_ORE) && hasFuse) {
                    Integer autoSmeltLevel;
                    if (m == Material.IRON_ORE) {
                        loot.setType(Material.IRON_INGOT);
                    }
                    if (m == Material.GOLD_ORE) {
                        loot.setType(Material.GOLD_INGOT);
                    }
                    CustomEnchantment enchantment = EnchantmentAPI.getEnchantment((String)"Auto Smelt");
                    Map enchants = EnchantmentAPI.getEnchantments((ItemStack)pl.getItemInHand());
                    if (enchantment != null && enchants != null && (autoSmeltLevel = (Integer)enchants.get((Object)enchantment)) != null) {
                        loot.setAmount(autoSmeltLevel.intValue());
                    }
                }
                brokeBlock = true;
                CoreProtectUtils.logBreak((String)pl.getName(), (Location)bl.getLocation(), (int)m.getId(), (byte)bl.getData());
                bl.setTypeIdAndData(0, (byte)0, false);
                if (loot.getType() == Material.RAILS) continue;
                double d = price = autoSell ? MasteryAutoSell.getSellPrice(loot.getData()) * (double)loot.getAmount() : 0.0;
                if (price > 0.0) {
                    autoSellMoney += price;
                } else if (telepathy && pl.getInventory().firstEmpty() != -1) {
                    pl.getInventory().addItem(new ItemStack[]{loot});
                } else {
                    pl.getWorld().dropItem(pl.getLocation(), loot);
                }
            } else {
                CoreProtectUtils.logBreak((String)pl.getName(), (Location)bl.getLocation(), (int)m.getId(), (byte)bl.getData());
                bl.breakNaturally(pl.getItemInHand());
                tookDurability = true;
            }

        }
        if (autoSellMoney > 0.0) {
            CosmicGUIShop.getEconomy().depositPlayer((OfflinePlayer)pl, autoSellMoney);
        }
        if (brokeBlock && !tookDurability && pickaxe) {
            if (pickaxeItem.getDurability() >= pickaxeItem.getType().getMaxDurability() - 1) {
                if (pickaxeItem.equals((Object)pl.getItemInHand())) {
                    pl.setItemInHand(null);
                } else {
                    pl.getInventory().removeItem(new ItemStack[]{pickaxeItem});
                }
                pl.updateInventory();
                pl.playSound(pl.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                Bukkit.getLogger().info("Destroying pickaxe " + (Object)pickaxeItem + " for " + pl.getName() + " due to durability: " + pickaxeItem.getDurability());
            } else {
                pickaxeItem.setDurability((short)(pickaxeItem.getDurability() + 1));
            }
        }
    }

    private void setBlockSuperFast(Block b, int typeId, byte data) {
        Chunk c = b.getChunk();
        net.minecraft.server.v1_8_R3.Chunk chunk = ((CraftChunk)c).getHandle();
        try {
            Field f = chunk.getClass().getDeclaredField("sections");
            f.setAccessible(true);
            ChunkSection[] sections = (ChunkSection[])f.get((Object)chunk);
            ChunkSection chunksection = sections[b.getY() >> 4];
            if (chunksection == null) {
                if (typeId == 0) {
                    return;
                }
                ChunkSection chunkSection = new ChunkSection(b.getY() >> 4 << 4, !chunk.world.worldProvider.d());
                sections[b.getY() >> 4] = chunkSection;
                chunksection = chunkSection;
            }
            chunksection.setType(b.getX() & 0xF, b.getY() & 0xF, b.getZ() & 0xF, (IBlockData) Blocks.BEDROCK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

