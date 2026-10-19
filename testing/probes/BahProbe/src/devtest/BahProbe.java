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
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

/**
 * Isolated-test-server probe for BAH management.
 * 1. Two server-side players join (packets are only queued; no client): XaitX (owner UUID from
 *    HotFix auto-op-uuids) and NotOwner. HotFix's real AutoOP join listeners run.
 * 2. After 3 s: checks who is OP, then runs the real commands as each player:
 *    NotOwner: /bah additem (must NOT add);  XaitX: /bah additem, /bah forcestart, /bah, /bahloot.
 * 3. Reads BAH's own state (item pool, current auction, open menu) and logs "PROBE ..." lines,
 *    then removes the test players and stops the server.
 */
public class BahProbe extends JavaPlugin {
    static final UUID XAITX = UUID.fromString("46f220b1-e5c5-4a55-8b43-a347db8c3b35");
    static final UUID OTHER = UUID.fromString("33333333-4444-5555-6666-777777777777");
    private EntityPlayer xaitx, other;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::join, 20L);
        Bukkit.getScheduler().runTaskLater(this, this::test, 80L);
    }

    private void log(String s) {
        getLogger().info("PROBE " + s);
    }

    private EntityPlayer serverPlayer(UUID id, String name) {
        MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer ws = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        EntityPlayer ep = new EntityPlayer(ms, ws, new GameProfile(id, name), new PlayerInteractManager(ws));
        ep.playerConnection = new PlayerConnection(ms, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), ep);
        ms.getPlayerList().players.add(ep);   // so Bukkit treats the player as online
        return ep;
    }

    private void join() {
        xaitx = serverPlayer(XAITX, "XaitX");
        other = serverPlayer(OTHER, "NotOwner");
        for (EntityPlayer ep : new EntityPlayer[]{xaitx, other}) {
            Player p = ep.getBukkitEntity();
            Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, null));
            log("joined " + p.getName() + " uuid=" + p.getUniqueId() + " online=" + p.isOnline() + " op(at join)=" + p.isOp());
        }
    }

    private ItemStack reward(String name) {
        ItemStack it = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta m = it.getItemMeta();
        m.setDisplayName(name);
        it.setItemMeta(m);
        return it;
    }

    private void test() {
        Player x = xaitx.getBukkitEntity();
        Player o = other.getBukkitEntity();
        try {
            Plugin bah = Bukkit.getPluginManager().getPlugin("BAH");
            log("plugins: BAH=" + enabled("BAH") + " Auction=" + enabled("Auction") + " Vault=" + enabled("Vault")
                    + " Essentials=" + enabled("Essentials") + " HotFix=" + enabled("HotFix") + " AstroLoottables=" + enabled("AstroLoottables"));
            log("after HotFix AutoOP: XaitX op=" + x.isOp() + "  NotOwner op=" + o.isOp());
            ClassLoader cl = bah.getClass().getClassLoader();
            Class<?> am = Class.forName("net.astropvp.auction.AuctionManager", true, cl);
            Object manager = am.getMethod("get").invoke(null);

            o.setItemInHand(reward("Probe Reward (NotOwner)"));
            Bukkit.dispatchCommand(o, "bah additem");
            log("NotOwner ran /bah additem -> pool=" + names(am.getMethod("getAvailableItems").invoke(manager))
                    + " openMenu='" + o.getOpenInventory().getTitle() + "'");

            x.setItemInHand(reward("Probe Reward (XaitX)"));
            Bukkit.dispatchCommand(x, "bah additem");
            log("XaitX ran /bah additem -> pool=" + names(am.getMethod("getAvailableItems").invoke(manager))
                    + " externalLootTable=" + am.getMethod("externalLootSelected").invoke(manager));

            Bukkit.dispatchCommand(x, "bah forcestart");
            Object auction = am.getMethod("getCurrentAuction").invoke(manager);
            if (auction == null) {
                log("XaitX ran /bah forcestart -> NO auction started");
            } else {
                Field item = auction.getClass().getField("item");
                Field endsAt = auction.getClass().getField("endsAt");
                long secs = (((Long) endsAt.get(auction)) - System.currentTimeMillis()) / 1000;
                log("XaitX ran /bah forcestart -> auction item='" + ((ItemStack) item.get(auction)).getItemMeta().getDisplayName()
                        + "' endsInSeconds=" + secs);
            }

            Bukkit.dispatchCommand(x, "bah");
            log("XaitX ran /bah -> openMenu='" + x.getOpenInventory().getTitle() + "' size=" + x.getOpenInventory().getTopInventory().getSize());
            Bukkit.dispatchCommand(x, "bahloot");
            log("XaitX ran /bahloot -> openMenu='" + x.getOpenInventory().getTitle() + "'");
            log("RESULT done");
        } catch (Throwable t) {
            log("FAIL " + t);
            t.printStackTrace();
        }
        MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
        ms.getPlayerList().players.remove(xaitx);
        ms.getPlayerList().players.remove(other);
        Bukkit.shutdown();
    }

    private static String enabled(String name) {
        Plugin p = Bukkit.getPluginManager().getPlugin(name);
        return p == null ? "missing" : String.valueOf(p.isEnabled());
    }

    private static String names(Object items) {
        StringBuilder sb = new StringBuilder("[");
        for (Object o : (List<?>) items) {
            ItemStack it = (ItemStack) o;
            sb.append(it.hasItemMeta() && it.getItemMeta().hasDisplayName() ? it.getItemMeta().getDisplayName() : it.getType().name()).append("; ");
        }
        return sb.append("]").toString();
    }
}
