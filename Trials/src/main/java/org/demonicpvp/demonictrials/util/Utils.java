package org.demonicpvp.demonictrials.util;


import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.concurrent.TimeUnit;

public class Utils {
    public static String formatTimeAbbreviated(final long seconds) {
        if (seconds == 0L) {
            return "0s";
        } else {
            long day = TimeUnit.SECONDS.toDays(seconds);
            long hours = TimeUnit.SECONDS.toHours(seconds) - day * 24L;
            long minutes = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
            long secs = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;
            StringBuilder sb = new StringBuilder();
            if (day > 0L) {
                sb.append(day).append("d").append(" ");
            }

            if (hours > 0L) {
                sb.append(hours).append("h").append(" ");
            }

            if (minutes > 0L) {
                sb.append(minutes).append("m").append(" ");
            }

            if (secs > 0L) {
                sb.append(secs).append("s");
            }

            String diff = sb.toString();
            return diff.isEmpty() ? "Now" : diff.trim();
        }
    }

    public static int[] getCenteredSlots(int amt) {
        switch (amt) {
            case 1:
                return new int[] { 4 };
            case 2:
                return new int[] { 3, 5 };
            case 3:
                return new int[] { 3, 4, 5 };
            case 4:
                return new int[] { 2, 3, 5, 6 };
            case 5:
                return new int[] { 2, 3, 4, 5, 6 };
            case 6:
                return new int[] { 1, 2, 3, 5, 6, 7 };
            case 7:
                return new int[] { 1, 2, 3, 4, 5, 6, 7 };
            case 8:
                return new int[] { 0, 1, 2, 3, 5, 6, 7, 8 };
            case 9:
                return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        }

        return new int[] {};
    }
    public static String locationToString(Location location) {
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
    }
    public static Location locationFromString(String str) {
        String[] splits = str.split(";");

        return new Location(Bukkit.getWorld(splits[0]), Double.parseDouble(splits[1]), Double.parseDouble(splits[2]), Double.parseDouble(splits[3]), Float.parseFloat(splits[4]), Float.parseFloat(splits[5]));
    }
    public static int rgbaToInt(int r, int g, int b, int a) {
        return (b & 0xFF) | ((g & 0xFF) << 8) | ((r & 0xFF) << 16) | ((a & 0xFF) << 24);
    }
    public static void fillEmptySlots(SmartInventory inventory, InventoryContents contents, String name) {
        for (int row = 0; row < inventory.getRows(); row++) {
            for (int column = 0; column < inventory.getColumns(); column++) {
                if (!contents.get(row, column).isPresent()) {
                    contents.set(row, column, ClickableItem.empty(
                            CommonsUtil.createItem(
                                    Material.STAINED_GLASS_PANE,
                                    name,
                                    1,
                                    7
                            )
                    ));
                }
            }
        }
    }
}
