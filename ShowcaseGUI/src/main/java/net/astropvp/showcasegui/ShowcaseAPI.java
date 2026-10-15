package net.astropvp.showcasegui;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.astropvp.showcasegui.custominventory.CustomInventory;
import net.astropvp.showcasegui.struct.Showcase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
public class ShowcaseAPI {
    public static ConcurrentHashMap<UUID, Showcase> showcases = new ConcurrentHashMap();
    public static final int MAX_SHOWCASE_SIZE = 558;

    public static Showcase getShowcase(Player p) {
        Showcase showcase = getShowcase(p.getUniqueId());
        CustomInventory first = showcase.getFirstPage();
        int maxSize = getMaxShowcaseSize(p);
        if (maxSize > first.getInventorySize()) {
            int slotsMissing = maxSize - first.getInventorySize();
            int rows = slotsMissing / 9;
            if (rows > 0) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "editshowcase " + p.getName() + " add " + rows);
                return getShowcase(p.getUniqueId());
            }
        }

        return showcase;
    }

    public static boolean hasShowcase(Player p) {
        return showcases.containsKey(p.getUniqueId());
    }

    public static boolean hasShowcase(UUID uuid) {
        return showcases.containsKey(uuid);
    }

    public static Showcase getShowcase(UUID uuid) {
        return (Showcase)showcases.get(uuid);
    }

    public static void saveShowcase(UUID uuid, boolean async, boolean remove) {
        if (hasShowcase(uuid)) {
            Showcase showcase = getShowcase(uuid);
            if (showcase.isEditted()) {
                if (async && Bukkit.isPrimaryThread()) {
                    Bukkit.getScheduler().scheduleAsyncDelayedTask(ShowcaseGUI.get(), () -> {
                        saveShowcase(uuid, false, remove);
                    });
                } else {
                    try {
                        PreparedStatement pst = ShowcaseGUI.getConnection().prepareStatement(showcase.toString());

                        try {
                            pst.executeUpdate();
                            pst.close();
                        } finally {
                            if (Collections.singletonList(pst).get(0) != null) {
                                pst.close();
                            }

                        }
                    } catch (Exception var9) {
                        var9.printStackTrace();
                    }

                    if (remove) {
                        showcases.remove(uuid);
                    }

                }
            }
        }
    }

    public static void saveAllShowcases() {
        try {
            PreparedStatement pst = ShowcaseGUI.getConnection().prepareStatement("");
            Iterator var1 = Bukkit.getOnlinePlayers().iterator();

            while(var1.hasNext()) {
                Player p = (Player)var1.next();
                if (hasShowcase(p)) {
                    Showcase showcase = getShowcase(p);

                    try {
                        pst.addBatch(showcase.toString());
                    } catch (Exception var5) {
                        var5.printStackTrace();
                    }
                }
            }

            pst.executeBatch();
            pst.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static int getMaxShowcaseSize(Player player) {
        for(int i = 54; i > 0; i -= 9) {
            if (player.hasPermission("showcasegui.showcase." + i)) {
                return i;
            }
        }

        if (player.hasPermission("enchantments.max.20")) {
            return 45;
        } else if (player.hasPermission("enchantments.max.12")) {
            return 36;
        } else if (player.hasPermission("enchantments.max.9")) {
            return 27;
        } else if (player.hasPermission("enchantments.max.6")) {
            return 18;
        } else if (player.hasPermission("enchantments.max.3")) {
            return 9;
        } else {
            return 9;
        }
    }

    public static Showcase createShowcase(UUID uuid, String name, String data, int size) {
        Player pl = Bukkit.getPlayer(uuid);
        Showcase showcase = getShowcase(uuid);
        if (showcase != null) {
            if (showcase.getPlayer() == null) {
                showcase.setPlayer(pl);
            }

            return showcase;
        } else {
            showcase = new Showcase(pl, uuid, name, data, size);
            showcases.put(uuid, showcase);
            return showcase;
        }
    }

    public static void loadShowcase(UUID uuid, String name, boolean async) {
        if (async && Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(ShowcaseGUI.get(), () -> {
                loadShowcase(uuid, name, false);
            });
        } else {
            try {
                PreparedStatement pst = ShowcaseGUI.getConnection().prepareStatement("SELECT showcase, size FROM showcases WHERE uuid = '" + uuid.toString() + "';");
                ResultSet rs = pst.executeQuery();
                if (rs.first()) {
                    createShowcase(uuid, name, rs.getString("showcase"), rs.getInt("size"));
                }

                pst.close();
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }
    }
}
