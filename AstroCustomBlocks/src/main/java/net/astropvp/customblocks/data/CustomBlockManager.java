package net.astropvp.customblocks.data;

import com.cosmicpvp.cosmicutils.utils.LocationUtils;
import net.astropvp.customblocks.CustomBlocks;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;
import org.bukkit.Location;

public class CustomBlockManager extends SaveLoadManager<CustomBlock> {


    public CustomBlockManager(CustomBlocks plugin) {
        super(plugin, CustomBlocks.getInstance().getGson(), "/data", CustomBlock.class);
    }

    public CustomBlock getCustomBlockData(Location location) {
        return getFromCache(LocationUtils.getStringFromLocation(location));
    }

}

