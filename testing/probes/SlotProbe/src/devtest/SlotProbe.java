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
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

/**
 * Isolated-test-server probe for the Slotbot module (plugin "AstroSlotBot").
 * A non-OP server-side player "SlotTester" (no client; packets only queued) is used.
 * 1. Puts 3 named test items into THIS test server's AstroLoottables SLOTS table.
 * 2. Console: /slot give SlotTester 3   -> player gets 3 tickets.
 * 3. Player: /slot                       -> "Astro Roll Bot" menu opens.
 * 4. Player left-clicks a ticket in their own inventory twice (real InventoryClickEvent through
 *    the plugin's listener) -> tickets move into the bot.
 * 5. Player clicks Spin -> the bot rolls; 3 s later the probe lists what the player received.
 * Also records whether a NON-OP player can run "/slot give" (no permission check in the code).
 * Logs "PROBE ..." lines, removes the test player and stops the server.
 */
public class SlotProbe extends JavaPlugin {
    private EntityPlayer tester;
    private Player p;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::setup, 20L);
        Bukkit.getScheduler().runTaskLater(this, this::insertAndSpin, 40L);
        Bukkit.getScheduler().runTaskLater(this, this::finish, 120L);
    }

    private void log(String s) {
        getLogger().info("PROBE " + s);
    }

    private static String enabled(String name) {
        Plugin pl = Bukkit.getPluginManager().getPlugin(name);
        return pl == null ? "missing" : String.valueOf(pl.isEnabled());
    }

    private ItemStack named(Material m, String name) {
        ItemStack it = new ItemStack(m);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName(name);
        it.setItemMeta(meta);
        return it;
    }

    private Object call(Object target, String method, Object... args) throws Exception {
        for (Method m : (target instanceof Class ? (Class<?>) target : target.getClass()).getMethods()) {
            if (m.getName().equals(method) && m.getParameterCount() == args.length) {
                return m.invoke(target instanceof Class ? null : target, args);
            }
        }
        throw new NoSuchMethodException(method);
    }

    private int countTickets(ClassLoader slotCl) throws Exception {
        Class<?> api = Class.forName("me.ily2bb.slotbot.CosmicSlotBotAPI", true, slotCl);
        int n = 0;
        for (ItemStack it : p.getInventory().getContents()) {
            if (it != null && (Boolean) call(api, "isSlotbotTicket", it)) n += it.getAmount();
        }
        return n;
    }

    private String rewards() {
        StringBuilder sb = new StringBuilder();
        for (ItemStack it : p.getInventory().getContents()) {
            if (it == null || it.getType() == Material.PAPER) continue;
            sb.append(it.getType()).append(it.hasItemMeta() && it.getItemMeta().hasDisplayName() ? " '" + it.getItemMeta().getDisplayName() + "'" : "").append("; ");
        }
        return sb.length() == 0 ? "(none)" : sb.toString();
    }

    private void setup() {
        log("plugins: AstroSlotBot=" + enabled("AstroSlotBot") + " AstroLootFilter=" + enabled("AstroLootFilter")
                + " AstroLoottables=" + enabled("AstroLoottables") + " CosmicPvPUtils=" + enabled("CosmicPvPUtils")
                + " CosmicKOTH=" + enabled("CosmicKOTH") + " AstroItemSkins=" + enabled("AstroItemSkins") + " AstroCore=" + enabled("AstroCore"));
        try {
            // 1. test items into this test server's SLOTS loot table
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
            log("SLOTS table now has " + ((java.util.List<?>) call(apiCls, "getAllLootFromLootTable", slots)).size() + " test items (test server only)");

            // test player (non-OP)
            MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
            WorldServer ws = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
            tester = new EntityPlayer(ms, ws, new GameProfile(UUID.fromString("44444444-5555-6666-7777-888888888888"), "SlotTester"), new PlayerInteractManager(ws));
            tester.playerConnection = new PlayerConnection(ms, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), tester);
            ms.getPlayerList().players.add(tester);
            registerOnline(ms, tester, true);
            p = tester.getBukkitEntity();
            log("tester " + p.getName() + " online=" + p.isOnline() + " op=" + p.isOp());

            // 2. console gives 3 tickets
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "slot give SlotTester 3");
            ClassLoader slotCl = Bukkit.getPluginManager().getPlugin("AstroSlotBot").getClass().getClassLoader();
            log("after console '/slot give SlotTester 3': tickets in inventory=" + countTickets(slotCl));

            // 3. player opens the menu
            Bukkit.dispatchCommand(p, "slot");
            InventoryView v = p.getOpenInventory();
            log("player ran /slot -> menu title='" + v.getTitle() + "' size=" + v.getTopInventory().getSize());
        } catch (Throwable t) {
            log("FAIL setup " + t);
            t.printStackTrace();
        }
    }

    private void clickRaw(InventoryView v, int raw) {
        InventoryClickEvent e = new InventoryClickEvent(v, InventoryType.SlotType.CONTAINER, raw, ClickType.LEFT, InventoryAction.PICKUP_ALL);
        Bukkit.getPluginManager().callEvent(e);
    }

    private void insertAndSpin() {
        try {
            ClassLoader slotCl = Bukkit.getPluginManager().getPlugin("AstroSlotBot").getClass().getClassLoader();
            Class<?> mgr = Class.forName("me.ily2bb.slotbot.managers.SlotBotManager", true, slotCl);
            Class<?> gui = Class.forName("me.ily2bb.slotbot.utils.GuiItems", true, slotCl);
            InventoryView v = p.getOpenInventory();
            Inventory top = v.getTopInventory();
            int topSize = top.getSize();

            // 4. click the ticket stack in the player's inventory twice
            for (int n = 1; n <= 2; n++) {
                int raw = -1;
                for (int r = topSize; r < v.countSlots(); r++) {
                    ItemStack it = v.getItem(r);
                    if (it != null && it.getType() == Material.PAPER) { raw = r; break; }
                }
                if (raw < 0) { log("FAIL no ticket found in player inventory"); return; }
                clickRaw(v, raw);
                log("left-click ticket #" + n + " -> tickets in bot=" + call(mgr, "getSlotBotTickets", top) + " tickets left in inventory=" + countTickets(slotCl));
            }

            // 5. click Spin
            ItemStack spin = (ItemStack) call(gui, "createSpinTicket", true);
            int spinSlot = -1;
            for (int i = 0; i < topSize; i++) if (spin.equals(top.getItem(i))) { spinSlot = i; break; }
            log("spin button slot=" + spinSlot + " rewards before spin=" + rewards());
            if (spinSlot >= 0) clickRaw(v, spinSlot);
            log("clicked Spin -> menu title now='" + p.getOpenInventory().getTitle() + "'");

            // also: can a NON-OP player run /slot give? (code has no permission check)
            int before = countTickets(slotCl);
            Bukkit.dispatchCommand(p, "slot give SlotTester 5");
            log("NON-OP player ran '/slot give SlotTester 5' -> tickets " + before + " -> " + countTickets(slotCl));
        } catch (Throwable t) {
            log("FAIL insert/spin " + t);
            t.printStackTrace();
        }
    }

    private void finish() {
        try {
            log("RESULT 4 s after spin: rewards received=" + rewards() + " open menu='" + p.getOpenInventory().getTitle() + "'");
        } catch (Throwable t) {
            log("FAIL finish " + t);
        }
        registerOnline(((CraftServer) Bukkit.getServer()).getServer(), tester, false);
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
