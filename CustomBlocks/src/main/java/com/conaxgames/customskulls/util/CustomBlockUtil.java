package com.conaxgames.customskulls.util;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.CustomBlock;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import de.tr7zw.nbtapi.NBTItem;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChunkCache;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomBlockUtil {

    public static CustomBlock getCustomItem(final ItemStack item) {
        NBTWrapper nbtItem = new NBTWrapper(item);
        if (nbtItem.hasKey("cosmicType")) {
            if (nbtItem.hasKey("cosmicType") && nbtItem.getString("cosmicType").equals("custom_block")) {
                NBTTagCompoundBuilder cosmicData = nbtItem.hasKey("cosmicData") ? new NBTTagCompoundBuilder(nbtItem.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
                NBTTagCompoundBuilder customBlockData = cosmicData.hasKey("customBlockData") ? new NBTTagCompoundBuilder(cosmicData.getNBTBase("customBlockData")) : new NBTTagCompoundBuilder();
                String abilityType = customBlockData.getString("type");
                if (CoreSkulls.customBlocks.containsKey(abilityType)) {
                    return CoreSkulls.customBlocks.get(abilityType);
                }
            }
        }
        return null;
    }


    public static Boolean isCustomItem(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasKey("cosmicType") && nbtItem.getString("cosmicType").equals("custom_block");
    }

    public static List<AbilityData> getBlockDatasInChunk(Player player) {
        List<AbilityData> abilityDataList = new ArrayList<>();
        final Collection<AbilityData> map = AbilityData.getAbilityDataMap().values();
        //final Chunk chunk = player.getLocation().getChunk();

        for (AbilityData data : map) {
            if (data == null || data.getLocation() == null) continue;
            if (data.getAbilityType() == null) continue;
            if (ChunkUtil.isLocationAInSameChunkAsLocationB(data.getLocation(), player.getLocation())) { // The chunk the player is in has a CustomBlock in it!
                abilityDataList.add(data);

            }
        }
        return abilityDataList;
    }

    public static List<AbilityData> getBlockDatasInChunkLocation(Location loc) {
        List<AbilityData> abilityDataList = new ArrayList<>();
        final Collection<AbilityData> map = AbilityData.getAbilityDataMap().values();
        //final Chunk chunk = player.getLocation().getChunk();

        for (AbilityData data : map) {
            if (data == null || data.getLocation() == null) continue;
            if (data.getAbilityType() == null) continue;
            if (ChunkUtil.isLocationAInSameChunkAsLocationB(data.getLocation(), loc)) { // The chunk the player is in has a CustomBlock in it!
                abilityDataList.add(data);

            }
        }
        return abilityDataList;
    }

    public static Location getClosestChestLocation(Location location) {
        if (location.clone().add(-1, 0, 0).getBlock().getType() == Material.CHEST || location.clone().add(-1, 0, 0).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(-1, 0, 0).getBlock().getState();
            if (CustomBlockUtil.isEmpty(chest.getInventory())) {
                return location.clone().add(-1, 0, 0);
            }
        }
        if (location.clone().add(0, 0, -1).getBlock().getType() == Material.CHEST || location.clone().add(0, 0, -1).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(0, 0, -1).getBlock().getState();
            if (CustomBlockUtil.isEmpty(chest.getInventory())) {
                return location.clone().add(0, 0, -1);
            }
        }


        if (location.clone().add(1, 0, 0).getBlock().getType() == Material.CHEST || location.clone().add(1, 0, 0).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(1, 0, 0).getBlock().getState();
            if (CustomBlockUtil.isEmpty(chest.getInventory())) {
                return location.clone().add(1, 0, 0);
            }
        }

        if (location.clone().add(0, 0, 1).getBlock().getType() == Material.CHEST || location.clone().add(0, 0, 1).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(0, 0, 1).getBlock().getState();
            if (CustomBlockUtil.isEmpty(chest.getInventory())) {
                return location.clone().add(0, 0, 1);
            }
        }
        return null;

    }

    public static boolean chunkContainsABlock(Player player) {
        for (AbilityData data : AbilityData.getAbilityDataMap().values()) {
            if (data == null || data.getLocation() == null)
                continue;
            if (ChunkUtil.isLocationAInSameChunkAsLocationB(player.getLocation(), data.getLocation())) { // The chunk the player is in has a CustomBlock in it!
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(Inventory inv) {
        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }

    public static List<Location> getNearbyChests(Location location) {
        List<Location> chests = new ArrayList<>();

        if (location.clone().add(-1, 0, 0).getBlock().getType() == Material.CHEST || location.clone().add(-1, 0, 0).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(-1, 0, 0).getBlock().getState();
            chests.add(chest.getLocation());

        }
        if (location.clone().add(0, 0, -1).getBlock().getType() == Material.CHEST || location.clone().add(0, 0, -1).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(0, 0, -1).getBlock().getState();
            chests.add(chest.getLocation());

        }


        if (location.clone().add(1, 0, 0).getBlock().getType() == Material.CHEST || location.clone().add(1, 0, 0).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(1, 0, 0).getBlock().getState();
            chests.add(chest.getLocation());

        }

        if (location.clone().add(0, 0, 1).getBlock().getType() == Material.CHEST || location.clone().add(0, 0, 1).getBlock().getType() == Material.TRAPPED_CHEST) {
            Chest chest = (Chest) location.clone().add(0, 0, 1).getBlock().getState();
            chests.add(chest.getLocation());

        }
        return chests;
    }

    public static boolean chunkHasCertainBlock(Player p, String abilityData) {
        for (AbilityData abilityData2 : CustomBlockUtil.getBlockDatasInChunk(p)) {

            if (abilityData2 == null) {
                return false;
            }
            if (abilityData2.getAbilityType() == null) continue;

            CustomBlock customBlock = CoreSkulls.customBlocks.get(abilityData2.getAbilityType().getName());


            if (customBlock == null) {
                return false;
            }

            if (customBlock.getIdentifier().equals(abilityData)) {
                return true;
            }
        }
        return false;
    }

    public static List<AbilityData> getBlockDatasInChunk(Location l) {
        List<AbilityData> abilityDataList = new ArrayList<>();
        for (AbilityData data : AbilityData.getAbilityDataMap().values()) {
            if (data == null) continue;
            if (data.getAbilityType() == null) continue;
            if (ChunkUtil.isLocationAInSameChunkAsLocationB(l, data.getLocation())) {
                abilityDataList.add(data);
            }
        }
        return abilityDataList;
    }


    public static boolean chunkHasCertainBlock(Location l, String abilityData) {
        for (AbilityData abilityData2 : CustomBlockUtil.getBlockDatasInChunk(l)) {

            if (abilityData2 == null) {
                return false;
            }

            CustomBlock customBlock = CoreSkulls.customBlocks.get(abilityData2.getAbilityType().getName());
            if (CoreSkulls.customBlocks.get(abilityData2.getAbilityType().getName()) == null) continue;

            if (customBlock == null) {
                return false;
            }

            if (customBlock.getIdentifier().equals(abilityData)) {
                return true;
            }
        }
        return false;
    }
}
