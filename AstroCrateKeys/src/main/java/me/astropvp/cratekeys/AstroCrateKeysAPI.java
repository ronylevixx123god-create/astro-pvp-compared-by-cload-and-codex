package me.astropvp.cratekeys;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import me.astropvp.cratekeys.data.CrateData;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import me.astropvp.cratekeys.managers.CrateDataManager;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AstroCrateKeysAPI {
    public static void addCrate(CrateType type, Location location) {
        FileConfiguration file = AstroCrateKeysPlugin.getInstance().getConfig();
        List<String> crates = file.contains("crates") ? file.getStringList("crates") : Lists.newArrayList();
        crates.add(String.valueOf(type.name()) + ":" + toStringLocation(location));
        file.set("crates", crates);
        AstroCrateKeysPlugin.getInstance().saveConfig();
        AstroCrateKeysPlugin.getInstance().reloadConfig();
    }

    public static void givePlayerKeys(Player player, CrateType type, int amount) {
        while (amount != 0) {
            player.getInventory().addItem(CrateKeyItem.getItem(type));
            amount--;
        }
    }

    public static boolean isCrateKey(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return false;
        NBTWrapper wrapper = new NBTWrapper(item);
        if (!wrapper.hasKey("cosmicData"))
            return false;
        NBTTagCompoundBuilder tag = new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData"));
        return tag.hasKey("crate_type");
    }

    public static void removeCrate(Location location) {
        FileConfiguration file = AstroCrateKeysPlugin.getInstance().getConfig();
        String loc = toStringLocation(location);
        List<String> crates = file.contains("crates") ? file.getStringList("crates") : Lists.newArrayList();
        for (int i = 0; i < crates.size(); i++) {
            if (((String)crates.get(i)).split(":")[1].equals(loc)) {
                crates.remove(i);
                file.set("crates", crates);
                AstroCrateKeysPlugin.getInstance().saveConfig();
                AstroCrateKeysPlugin.getInstance().reloadConfig();
            }
        }
    }

    public static void clearCrates() {
        CrateDataManager.clearCrates();
    }

    public static void deleteCrates() {
        FileConfiguration file = AstroCrateKeysPlugin.getInstance().getConfig();
        file.set("crates", null);
        AstroCrateKeysPlugin.getInstance().saveConfig();
        AstroCrateKeysPlugin.getInstance().reloadConfig();
    }

    public static List<CrateData> getCrates() {
        List<CrateData> crates = Lists.newArrayList();
        FileConfiguration file = AstroCrateKeysPlugin.getInstance().getConfig();
        List<String> list = file.contains("crates") ? file.getStringList("crates") : Lists.newArrayList();
        for (String data : list) {
            String[] split = data.split(":");
            crates.add(new CrateData(CrateType.valueOf(split[0]), fromStringLocation(split[1])));
        }
        return crates;
    }

    public static CrateData findCrate(Location location) {
        List<CrateData> crates = CrateDataManager.getCrates();
        for (CrateData data : crates) {
            if (data.getLocation().getX() == location.getX() &&
                    data.getLocation().getY() == location.getY() && data.getLocation().getZ() == location.getZ() &&
                    data.getLocation().getWorld().getName().equals(location.getWorld().getName()))
                return data;
        }
        return null;
    }

    public static String toStringLocation(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        String world = location.getWorld().getName();
        String result = String.valueOf(world) + " " + x + " " + y + " " + z + " " + yaw + " " + pitch;
        return result;
    }

    public static Location fromStringLocation(String location) {
        String[] list = location.split(" ");
        World world = Bukkit.getWorld(list[0]);
        double x = Double.parseDouble(list[1]);
        double y = Double.parseDouble(list[2]);
        double z = Double.parseDouble(list[3]);
        float yaw = Float.valueOf(list[4]).floatValue();
        float pitch = Float.valueOf(list[5]).floatValue();
        Location l = new Location(world, x, y, z);
        l.setPitch(pitch);
        l.setYaw(yaw);
        return l;
    }



    public static List<ItemStack> getListItems(CrateType type) {
        return LootTableAPI.getAllLootFromLootTable(LootTable.valueOf(type.name()));
    }
}
