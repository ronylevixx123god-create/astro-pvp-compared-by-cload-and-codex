package me.astropvp.astrospacegrinding;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.InventoryUtils;
import com.google.common.collect.Lists;
import me.astropvp.astrospacegrinding.enums.MonsterType;
import me.astropvp.astrospacegrinding.utils.LocationUtils;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class AstroSpaceGrindingAPI {
	private static final String[] WORLD_NAMES = new String[]{"mars", "space"};

	public static boolean isInSpaceWorld(Player player) {
		for (String worldName : WORLD_NAMES) {
			if(Objects.equals(player.getLocation().getWorld().getName(), worldName)) return true;
		}
		return false;
	}

	public static boolean isSpaceWorld(World world) {
		for (String worldName : WORLD_NAMES) {
			if(Objects.equals(world.getName(), worldName)) return true;
		}
		return false;
	}
	
	public static void addLocation(MonsterType type, Location location) {
		FileConfiguration file = AstroSpaceGrindingPlugin.getInstance().getConfig();
		List<String> locations = file.contains("boss-locations") ? file.getStringList("boss-locations") :
		Lists.newArrayList();
		locations.add(type.name() + ":" + LocationUtils.toStringLocation(location));
		file.set("boss-locations", locations);
		AstroSpaceGrindingPlugin.getInstance().saveConfig();
		AstroSpaceGrindingPlugin.getInstance().reloadConfig();
	}
	
	public static void removeLocations(MonsterType type) {
		FileConfiguration file = AstroSpaceGrindingPlugin.getInstance().getConfig();
		List<String> locations = file.contains("boss-locations") ? file.getStringList("boss-locations") :
		Lists.newArrayList();
		if(locations.isEmpty()) {
			return;
		}
		String name = type.name();
		locations.removeIf(location -> location.split(":")[0].equals(name));
		file.set("boss-locations", locations);
		AstroSpaceGrindingPlugin.getInstance().saveConfig();
		AstroSpaceGrindingPlugin.getInstance().reloadConfig();
	}
	
	public static Object[] getValues(String value) {
		String[] values = value.split(":");
		return new Object[] {MonsterType.valueOf(values[0]), LocationUtils.fromStringLocation(values[1])};
	}
	
	public static List<String> getBossLocations(){
		FileConfiguration file = AstroSpaceGrindingPlugin.getInstance().getConfig();
		return file.contains("boss-locations") ? file.getStringList("boss-locations") :
		Lists.newArrayList();
	}
	
	public static String[] getWorldNames() {
		return WORLD_NAMES;
	}
	
	public static World[] getWorlds() {
		return new World[]{Bukkit.getWorld(WORLD_NAMES[0]), Bukkit.getWorld(WORLD_NAMES[1])};
	}

	public static List<ItemStack> getListItems(MonsterType type) {
		return LootTableAPI.getAllLootFromLootTable(LootTable.SPACE);
	}


	public static Inventory getLootInventory(MonsterType type) {
		List<ItemStack> items = AstroSpaceGrindingAPI.getListItems(type);
		Inventory inventory = Bukkit.createInventory(null, InventoryUtils.fitSize(items.size()), CC.DarkGray + type.getName() + " Loot Items");
		int pos = 0;
		for (ItemStack item : items)
			inventory.setItem(pos++, item);

		return inventory;
	}


}
