package devtest;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

/**
 * Isolated-test-server probe for Commons + NebulaShop + AstroRuins.
 * 1. Logs plugin load/enable state and load order (Commons must enable first).
 * 2. A non-OP server-side player (no client) runs /nebula -> the "Nebula Shop" menu opens.
 * 3. Clicks the information book (a no-op item; no purchase) through the real click event and
 *    reports whether Commons' GUI listener cancelled it (players can't take items out).
 * 4. AstroRuins: calls Commons' CC.translate through AstroRuins' class loader.
 * Logs "PROBE ..." lines, removes the test player and stops the server.
 */
public class CommonsProbe extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::run, 20L);
    }

    private void log(String s) {
        getLogger().info("PROBE " + s);
    }

    private static String enabled(String n) {
        Plugin p = Bukkit.getPluginManager().getPlugin(n);
        return p == null ? "missing" : String.valueOf(p.isEnabled());
    }

    private void run() {
        EntityPlayer ep = null;
        MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
        try {
            log("plugins: Commons=" + enabled("Commons") + " NebulaShop=" + enabled("NebulaShop") + " AstroRuins=" + enabled("AstroRuins"));
            StringBuilder order = new StringBuilder();
            for (Plugin p : Bukkit.getPluginManager().getPlugins()) order.append(p.getName()).append(" ");
            log("load order: " + order);

            WorldServer ws = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
            ep = new EntityPlayer(ms, ws, new GameProfile(UUID.fromString("55555555-6666-7777-8888-999999999999"), "ShopTester"), new PlayerInteractManager(ws));
            ep.playerConnection = new PlayerConnection(ms, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), ep);
            ms.getPlayerList().players.add(ep);
            registerOnline(ms, ep, true);
            Player p = ep.getBukkitEntity();

            p.addAttachment(this, "astrocore.nebula", true);
            Bukkit.dispatchCommand(p, "nebula shop");
            InventoryView v = p.getOpenInventory();
            log("player (with astrocore.nebula) ran /nebula shop -> menu '" + v.getTitle() + "' size=" + v.getTopInventory().getSize());
            int bookSlot = -1;
            for (int i = 0; i < v.getTopInventory().getSize(); i++) {
                ItemStack it = v.getTopInventory().getItem(i);
                if (it == null) continue;
                String name = it.hasItemMeta() && it.getItemMeta().hasDisplayName() ? " '" + it.getItemMeta().getDisplayName() + "'" : "";
                log("   slot " + i + " = " + it.getType() + name);
                if (it.getType().name().contains("BOOK")) bookSlot = i;
            }
            if (bookSlot >= 0) {
                InventoryClickEvent e = new InventoryClickEvent(v, InventoryType.SlotType.CONTAINER, bookSlot, ClickType.LEFT, InventoryAction.PICKUP_ALL);
                Bukkit.getPluginManager().callEvent(e);
                log("clicked information book (slot " + bookSlot + ") -> cancelled by Commons GUI listener=" + e.isCancelled());
            } else {
                log("no information book found");
            }

            Plugin ruins = Bukkit.getPluginManager().getPlugin("AstroRuins");
            Class<?> cc = Class.forName("me.matthawkk.chat.CC", true, ruins.getClass().getClassLoader());
            log("AstroRuins -> Commons CC.translate(\"&aok\") = '" + cc.getMethod("translate", String.class).invoke(null, "&aok") + "'");
            log("RESULT done");
        } catch (Throwable t) {
            log("FAIL " + t + (t.getCause() != null ? " caused by " + t.getCause() : ""));
            t.printStackTrace();
        }
        if (ep != null) registerOnline(ms, ep, false);
        Bukkit.shutdown();
    }

    @SuppressWarnings("unchecked")
    private static void registerOnline(MinecraftServer ms, EntityPlayer ep, boolean add) {
        try {
            PlayerList pl = ms.getPlayerList();
            Field byUuid = PlayerList.class.getDeclaredField("j");
            Field byName = PlayerList.class.getDeclaredField("playersByName");
            byUuid.setAccessible(true); byName.setAccessible(true);
            Map<UUID, EntityPlayer> u = (Map<UUID, EntityPlayer>) byUuid.get(pl);
            Map<String, EntityPlayer> n = (Map<String, EntityPlayer>) byName.get(pl);
            if (add) { u.put(ep.getUniqueID(), ep); n.put(ep.getName(), ep); }
            else { pl.players.remove(ep); u.remove(ep.getUniqueID()); n.remove(ep.getName()); }
        } catch (ReflectiveOperationException e) { throw new IllegalStateException(e); }
    }
}
