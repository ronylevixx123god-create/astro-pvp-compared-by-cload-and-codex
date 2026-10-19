package devtest;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

/**
 * Isolated-test-server probe for the /slot admin lock.
 * Normal player (not OP, no slotbot.admin), an admin with slotbot.admin, an OP without the node, and the console
 * run /slot give|giveheroic|enable|disable; reports the tickets each player holds and the slot-bot-enabled setting.
 * Also checks a normal player can still open the /slot menu. Removes the test players, restores the setting,
 * logs "PROBE ..." lines and stops the server.
 */
public class SlotGiveProbe extends JavaPlugin {
    private int fails = 0;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::run, 20L);
    }

    private void log(String s) {
        getLogger().info("PROBE " + s);
    }

    private void check(String what, boolean ok) {
        if (!ok) fails++;
        log((ok ? "PASS " : "FAIL ") + what);
    }

    private static int tickets(Player p, String nameContains) {
        int n = 0;
        for (ItemStack it : p.getInventory().getContents()) {
            if (it != null && it.getType() == Material.PAPER && it.hasItemMeta() && it.getItemMeta().hasDisplayName()
                    && it.getItemMeta().getDisplayName().contains(nameContains)
                    && (nameContains.equals("Heroic") || !it.getItemMeta().getDisplayName().contains("Heroic"))) n += it.getAmount();
        }
        return n;
    }

    private void run() {
        MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
        EntityPlayer eNormal = null, eAdmin = null, eOp = null;
        Plugin slot = Bukkit.getPluginManager().getPlugin("AstroSlotBot");
        Boolean enabledBefore = null;
        try {
            log("AstroSlotBot enabled=" + (slot != null && slot.isEnabled()));
            enabledBefore = slot.getConfig().getBoolean("slot-bot-enabled");
            log("slot-bot-enabled before = " + enabledBefore);
            WorldServer ws = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
            eNormal = fake(ms, ws, "bbbbbbbb-0000-0000-0000-000000000001", "SlotNormal");
            eAdmin = fake(ms, ws, "bbbbbbbb-0000-0000-0000-000000000002", "SlotAdmin");
            eOp = fake(ms, ws, "bbbbbbbb-0000-0000-0000-000000000003", "SlotOp");
            Player normal = eNormal.getBukkitEntity(), admin = eAdmin.getBukkitEntity(), op = eOp.getBukkitEntity();
            admin.addAttachment(this, "slotbot.admin", true);
            op.setOp(true);
            log("normal: op=" + normal.isOp() + " slotbot.admin=" + normal.hasPermission("slotbot.admin")
                    + " | admin: op=" + admin.isOp() + " slotbot.admin=" + admin.hasPermission("slotbot.admin")
                    + " | op: op=" + op.isOp() + " slotbot.admin=" + op.hasPermission("slotbot.admin"));

            // Normal player tries to give themselves tickets
            Bukkit.dispatchCommand(normal, "slot give SlotNormal 5");
            Bukkit.dispatchCommand(normal, "bot give SlotNormal 5");
            Bukkit.dispatchCommand(normal, "slot giveheroic SlotNormal 5");
            check("normal player /slot give, /bot give, /slot giveheroic on self -> tickets=" + tickets(normal, "Slot Bot") + " (expected 0)",
                    tickets(normal, "Slot Bot") == 0);

            // Normal player tries to disable / enable slot bots
            Bukkit.dispatchCommand(normal, enabledBefore ? "slot disable" : "slot enable");
            check("normal player /slot " + (enabledBefore ? "disable" : "enable") + " -> slot-bot-enabled still " + slot.getConfig().getBoolean("slot-bot-enabled"),
                    slot.getConfig().getBoolean("slot-bot-enabled") == enabledBefore);

            // Admins
            Bukkit.dispatchCommand(admin, "slot give SlotNormal 2");
            check("admin (slotbot.admin) /slot give SlotNormal 2 -> normal has " + tickets(normal, "Slot Bot") + " (expected 2)", tickets(normal, "Slot Bot") == 2);
            Bukkit.dispatchCommand(op, "slot give SlotNormal 1");
            check("OP /slot give SlotNormal 1 -> normal has " + tickets(normal, "Slot Bot") + " (expected 3)", tickets(normal, "Slot Bot") == 3);
            int heroicBefore = tickets(normal, "Heroic");
            Bukkit.dispatchCommand(admin, "slot giveheroic SlotNormal 1");
            check("admin /slot giveheroic SlotNormal 1 -> heroic tickets " + heroicBefore + " -> " + tickets(normal, "Heroic"), tickets(normal, "Heroic") > heroicBefore);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "slotbot give SlotNormal 1");
            check("console 'slotbot give SlotNormal 1' (AstroCore LMS reward) -> normal has " + tickets(normal, "Slot Bot") + " (expected >= 4)", tickets(normal, "Slot Bot") >= 4);
            Bukkit.dispatchCommand(admin, enabledBefore ? "slot disable" : "slot enable");
            check("admin /slot " + (enabledBefore ? "disable" : "enable") + " -> slot-bot-enabled now " + slot.getConfig().getBoolean("slot-bot-enabled"),
                    slot.getConfig().getBoolean("slot-bot-enabled") != enabledBefore);
            Bukkit.dispatchCommand(admin, enabledBefore ? "slot enable" : "slot disable");

            // Normal player can still open the slot bot (enable it first if it was disabled)
            slot.getConfig().set("slot-bot-enabled", true);
            Bukkit.dispatchCommand(normal, "slot");
            String title = normal.getOpenInventory().getTitle();
            check("normal player /slot opens menu -> '" + title + "' size=" + normal.getOpenInventory().getTopInventory().getSize(),
                    normal.getOpenInventory().getTopInventory().getSize() > 5);
            normal.closeInventory();
            op.setOp(false);
        } catch (Throwable t) {
            fails++;
            log("FAIL exception " + t);
            t.printStackTrace();
        } finally {
            if (slot != null && enabledBefore != null) {
                slot.getConfig().set("slot-bot-enabled", enabledBefore);
                slot.saveConfig();
            }
            for (EntityPlayer e : new EntityPlayer[]{eNormal, eAdmin, eOp}) if (e != null) registerOnline(ms, e, false);
            log("RESULT fails=" + fails);
            Bukkit.shutdown();
        }
    }

    private EntityPlayer fake(MinecraftServer ms, WorldServer ws, String uuid, String name) {
        EntityPlayer ep = new EntityPlayer(ms, ws, new GameProfile(UUID.fromString(uuid), name), new PlayerInteractManager(ws));
        ep.playerConnection = new PlayerConnection(ms, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), ep);
        ms.getPlayerList().players.add(ep);
        registerOnline(ms, ep, true);
        return ep;
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
