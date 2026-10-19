package devtest;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.PlayerList;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

/** Controlled-loot fixture for the real slot rolling/reward path on an isolated server.
 * Ticket insertion and consumption are not exercised by this probe. */
public class SlotRewardProbe extends JavaPlugin {
    private EntityPlayer tester;
    private Player p;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::start, 20L);
        Bukkit.getScheduler().runTaskLater(this, this::finish, 100L);
    }

    private void log(String s) {
        getLogger().info("PROBE " + s);
    }

    private static String enabled(String name) {
        Plugin pl = Bukkit.getPluginManager().getPlugin(name);
        return pl == null ? "missing" : String.valueOf(pl.isEnabled());
    }

    private Object call(Object target, String method, Object... args) throws Exception {
        for (Method m : (target instanceof Class ? (Class<?>) target : target.getClass()).getMethods()) {
            if (m.getName().equals(method) && m.getParameterCount() == args.length) {
                return m.invoke(target instanceof Class ? null : target, args);
            }
        }
        throw new NoSuchMethodException(method);
    }

    private String commandsOf(String plugin) {
        TreeSet<String> names = new TreeSet<>();
        for (Command c : ((CraftServer) Bukkit.getServer()).getCommandMap().getCommands()) {
            if (c instanceof PluginIdentifiableCommand && ((PluginIdentifiableCommand) c).getPlugin().getName().equals(plugin)) {
                names.add(c.getName());
            }
        }
        return names.toString();
    }

    private ItemStack named(Material m, String name) {
        ItemStack it = new ItemStack(m);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName(name);
        it.setItemMeta(meta);
        return it;
    }

    private String rewards() {
        StringBuilder sb = new StringBuilder();
        for (ItemStack it : p.getInventory().getContents()) {
            if (it == null) continue;
            sb.append(it.getType()).append(it.hasItemMeta() && it.getItemMeta().hasDisplayName() ? " '" + it.getItemMeta().getDisplayName() + "'" : "").append("; ");
        }
        return sb.length() == 0 ? "(none)" : sb.toString();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void start() {
        log("plugins: AstroCore=" + enabled("AstroCore") + " AstroLootFilter=" + enabled("AstroLootFilter")
                + " AstroSlotBot=" + enabled("AstroSlotBot") + " AstroLoottables=" + enabled("AstroLoottables"));
        log("AstroCore commands: " + commandsOf("AstroCore"));
        log("AstroLootFilter commands: " + commandsOf("AstroLootFilter"));
        try {
            ClassLoader ltCl = Bukkit.getPluginManager().getPlugin("AstroLoottables").getClass().getClassLoader();
            Class<?> apiCls = Class.forName("net.astropvp.loottable.LootTableAPI", true, ltCl);
            Class<?> tableEnum = Class.forName("net.astropvp.loottable.struct.LootTable", true, ltCl);
            Object slots = Enum.valueOf((Class<Enum>) tableEnum, "SLOTS");
            Field mgrF = apiCls.getDeclaredField("lootTableManager");
            mgrF.setAccessible(true);
            Object data = call(mgrF.get(null), "getLootTable", slots);
            Object eco = call(data, "getCurrentEcoUpdateLoot");
            call(data, "addItem", eco, 1.0, named(Material.DIAMOND, "Probe Prize A"));
            call(data, "addItem", eco, 1.0, named(Material.EMERALD, "Probe Prize B"));
            call(data, "addItem", eco, 1.0, named(Material.GOLD_INGOT, "Probe Prize C"));

            MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
            WorldServer ws = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
            tester = new EntityPlayer(ms, ws, new GameProfile(UUID.fromString("44444444-5555-6666-7777-888888888888"), "SlotTester"), new PlayerInteractManager(ws));
            tester.playerConnection = new PlayerConnection(ms, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), tester);
            ms.getPlayerList().players.add(tester);
            registerOnline(ms, tester, true);
            p = tester.getBukkitEntity();

            Bukkit.dispatchCommand(p, "slot");
            Inventory top = p.getOpenInventory().getTopInventory();
            log("player ran /slot -> menu '" + p.getOpenInventory().getTitle() + "' size=" + top.getSize() + " inventory before spin: " + rewards());

            ClassLoader slotCl = Bukkit.getPluginManager().getPlugin("AstroSlotBot").getClass().getClassLoader();
            Class<?> inv = Class.forName("me.ily2bb.slotbot.utils.Inventories", true, slotCl);
            Class<?> mgr = Class.forName("me.ily2bb.slotbot.managers.SlotBotManager", true, slotCl);
            Inventory rolling = (Inventory) call(inv, "createRollingInventory", p, 5 - 1);
            top.setContents(rolling.getContents());
            call(mgr, "startRolling", p, top, 1);
            log("spin started with 1 ticket (same calls as the Spin button)");
        } catch (Throwable t) {
            log("FAIL " + t + (t.getCause() != null ? " caused by " + t.getCause() : ""));
            t.printStackTrace();
        }
    }

    private void finish() {
        try {
            int prizes = 0;
            if (p != null) for (ItemStack item : p.getInventory().getContents()) {
                if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                        && item.getItemMeta().getDisplayName().startsWith("Probe Prize ")) prizes += item.getAmount();
            }
            log((prizes == 1 ? "PASS" : "FAIL") + " exactly one fixture reward for one rolling column: " + prizes);
            log("RESULT 4 s after spin: player inventory = " + (p == null ? "n/a" : rewards()));
        } catch (Throwable t) {
            log("FAIL finish " + t);
        }
        if (tester != null) registerOnline(((CraftServer) Bukkit.getServer()).getServer(), tester, false);
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
