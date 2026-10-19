package devtest;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Isolated-test-server probe: opens CosmicDuel's real /team menus (MainTeamGUI, TeamListGUI) on the
 * running 1.8 server, using CosmicDuel's bundled MF-GUI. The player is a real server-side player
 * object whose network connection only queues packets (no Minecraft client is connected).
 * Logs "PROBE ..." lines, then stops the server.
 */
public class DuelMenuProbe extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::run, 20L);
    }

    private void log(String s) {
        getLogger().info("PROBE " + s);
    }

    // A real CraftPlayer backed by an offline network connection: packets are queued, never sent anywhere.
    private Player realServerPlayer() {
        MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer ws = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        EntityPlayer ep = new EntityPlayer(ms, ws,
                new GameProfile(UUID.fromString("22222222-3333-4444-5555-666666666666"), "MenuTester"),
                new PlayerInteractManager(ws));
        ep.playerConnection = new PlayerConnection(ms, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), ep);
        return ep.getBukkitEntity();
    }

    private void describe(String label, InventoryView view) {
        Inventory inv = view.getTopInventory();
        log(label + ": title='" + view.getTitle() + "' size=" + inv.getSize());
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack it = inv.getItem(i);
            if (it == null) continue;
            String name = it.hasItemMeta() && it.getItemMeta().hasDisplayName() ? it.getItemMeta().getDisplayName() : "";
            log("   slot " + i + " = " + it.getType() + (name.isEmpty() ? "" : " '" + name + "'"));
        }
    }

    private void run() {
        Plugin duel = Bukkit.getPluginManager().getPlugin("CosmicDuel");
        log("CosmicDuel enabled=" + (duel != null && duel.isEnabled()));
        try {
            ClassLoader cl = duel.getClass().getClassLoader();
            Player p = realServerPlayer();
            log("player=" + p.getClass().getName() + " name=" + p.getName());

            Class<?> main = Class.forName("me.ifamasssxd.cosmicduel.inventory.teamgui.MainTeamGUI", true, cl);
            Object mainGui = main.getConstructor(Player.class).newInstance(p);
            Object paginated = main.getMethod("getExpShopUI").invoke(mainGui);
            log("MainTeamGUI menu class = " + paginated.getClass().getName());
            invokeOpen(paginated, p);
            describe("Team Menu", p.getOpenInventory());

            Class<?> list = Class.forName("me.ifamasssxd.cosmicduel.inventory.teamgui.TeamListGUI", true, cl);
            Object listGui = list.getConstructor(Player.class).newInstance(p);
            Object listPaginated = list.getMethod("getExpShopUI").invoke(listGui);
            invokeOpen(listPaginated, p);
            describe("Team List", p.getOpenInventory());
            log("RESULT done");
        } catch (Throwable t) {
            log("FAIL " + t);
            t.printStackTrace();
        }
        Bukkit.shutdown();
    }

    private static void invokeOpen(Object gui, Player p) throws Exception {
        for (Method m : gui.getClass().getMethods()) {
            if (m.getName().equals("open") && m.getParameterCount() == 1) {
                m.invoke(gui, p);
                return;
            }
        }
        throw new IllegalStateException("no open(HumanEntity) on " + gui.getClass());
    }
}
