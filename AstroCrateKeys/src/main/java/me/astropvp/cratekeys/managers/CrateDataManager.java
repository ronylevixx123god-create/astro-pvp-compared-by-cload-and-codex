package me.astropvp.cratekeys.managers;
import java.util.List;

import me.astropvp.cratekeys.AstroCrateKeysAPI;
import me.astropvp.cratekeys.data.CrateData;
import me.astropvp.cratekeys.enums.CrateType;
import org.bukkit.Location;
import org.bukkit.Material;

public class CrateDataManager {
    private static List<CrateData> crates;

    public static void init() {
        crates = AstroCrateKeysAPI.getCrates();
    }

    public static void onDisable() {
        AstroCrateKeysAPI.deleteCrates();
        crates.forEach(crate -> AstroCrateKeysAPI.addCrate(crate.getCrateType(), crate.getLocation()));
        for (int i = crates.size() - 1; i >= 0; i--) {
            CrateData data = crates.get(i);
            if (data.getLocation().getBlock().getType() == Material.AIR)
                crates.remove(i);
        }
    }

    public static void clearCrates() {
        crates.clear();
    }

    public static void newInstance(CrateType type, Location location) {
        CrateData data = new CrateData(type, location);
        crates.add(data);
    }

    public static void removeInstance(CrateData crate) {
        for (int i = 0; i < crates.size(); i++) {
            CrateData data = crates.get(i);
            if (data.equals(crate)) {
                crates.remove(i);
                return;
            }
        }
    }

    public static List<CrateData> getCrates() {
        return crates;
    }
}
