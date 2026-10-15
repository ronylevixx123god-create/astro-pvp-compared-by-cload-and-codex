package net.astropvp.customblocks;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.LocationUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import de.tr7zw.nbtapi.NBTItem;
import net.astropvp.customblocks.data.CustomBlockManager;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.tasks.ChunkCustomBlocksTask;
import net.astropvp.customblocks.utils.ChunkUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class CustomBlocksAPI {
    private static CustomBlockManager customBlockManager = CustomBlocks.getInstance().getCustomBlockManager();

    public static boolean isCustomBlockItem(ItemStack itemStack) {
        if (ItemUtils.isAirOrNull(itemStack)) return false;
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasKey("cosmicType") && nbtItem.getString("cosmicType").equals("custom_block");
    }

    public static int getLevelFromItem(ItemStack itemStack) {
        if (isCustomBlockItem(itemStack)) {
            NBTWrapper nbtItem = new NBTWrapper(itemStack);
            NBTTagCompoundBuilder cosmicData = nbtItem.hasKey("cosmicData") ? new NBTTagCompoundBuilder(nbtItem.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
            NBTTagCompoundBuilder customBlockData = cosmicData.hasKey("customBlockData") ? new NBTTagCompoundBuilder(cosmicData.getNBTBase("customBlockData")) : new NBTTagCompoundBuilder();
            return customBlockData.getInt("level");
        }
        return 1;
    }

    public static CustomBlockType getTypeFromItem(ItemStack itemStack) {
        if (isCustomBlockItem(itemStack)) {
            NBTWrapper nbtItem = new NBTWrapper(itemStack);
            NBTTagCompoundBuilder cosmicData = nbtItem.hasKey("cosmicData") ? new NBTTagCompoundBuilder(nbtItem.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
            NBTTagCompoundBuilder customBlockData = cosmicData.hasKey("customBlockData") ? new NBTTagCompoundBuilder(cosmicData.getNBTBase("customBlockData")) : new NBTTagCompoundBuilder();
            return CustomBlockType.valueOf(customBlockData.getString("type"));
        }
        return null;
    }

    public static CustomBlock getCustomBlockData(Location location) {
        if (isCustomBlock(location)) {
            return customBlockManager.getCustomBlockData(location);
        }
        return null;
    }

    public static boolean isCustomBlock(Location location) {
        return customBlockManager.contains(LocationUtils.getStringFromLocation(location));
    }

    public static boolean chunkContainsCustomBlock(Location location, CustomBlockType type) {
        for (String key : customBlockManager.getCacheKeys()) {
            Location loc = LocationUtils.getLocationFromString(key, false);
            CustomBlock data = customBlockManager.getCustomBlockData(loc);
            if (ChunkUtil.isLocationAInSameChunkAsLocationB(loc, location) && type == data.getType()) {
                return true;
            }
        }
        return false;
    }


    public static boolean removeCustomBlockFromChunkOfLocation(Location location, CustomBlockType type) {
        Set<String> keys = new HashSet<>(customBlockManager.getCacheKeys());
        for (String key : keys) {
            Location loc = LocationUtils.getLocationFromString(key, false);
            CustomBlock data = customBlockManager.getCustomBlockData(loc);
            if (ChunkUtil.isLocationAInSameChunkAsLocationB(loc, location) && type == data.getType()) {
                loc.getBlock().setType(Material.AIR);
                customBlockManager.removeFromCache(key);
            }
        }
        return false;
    }

    public static int getFactionCustomBlocksOfType(String factionId, CustomBlockType type) {
        int amount = 0;

        for (CustomBlock key : customBlockManager.getCacheValues()) {
            if (type != key.getType()) continue;
            FLocation fLocation = new FLocation(key.getLocation());
            if (!Board.getInstance().getFactionAt(fLocation).getId().equals(factionId)) continue;
            amount++;
        }
        return amount;
    }

    public static boolean playerChunkContainsBlock(Player player, CustomBlockType type) {
        if (ChunkCustomBlocksTask.playerChunkCustomBlocks.containsKey(player)) {
            return ChunkCustomBlocksTask.playerChunkCustomBlocks.get(player).contains(type);
        }
        return false;
    }


}
