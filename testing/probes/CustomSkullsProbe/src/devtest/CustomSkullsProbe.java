package devtest;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.enums.AbilityType;
import com.conaxgames.customskulls.guis.CrateKeyOpenerGUI;
import com.conaxgames.customskulls.guis.GrinderCustomBlockGUI;
import com.conaxgames.customskulls.guis.SlotBotOpenerGUI;
import com.mojang.authlib.GameProfile;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import me.ily2bb.slotbot.CosmicSlotBotAPI;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Isolated-test-server probe for CustomSkullBlocks' compatibility with the current AstroCrateKeys / AstroLootFilter.
 * Uses the real blocks' menus: Security System (crate key opener), Cosmic Token Generator (slot bot opener) and the
 * Blaze Stone Totem mob grinder. Each opener gets 6 keys/tickets, its loot table holds two test prizes, and the
 * player has filtered prize "B" in AstroLootFilter, so only prize "A" should end up in the chest.
 * Logs "PROBE ..." lines, removes what it placed and stops the server.
 */
public class CustomSkullsProbe extends JavaPlugin {
    private EntityPlayer ep;

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
        MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
        List<Block> placed = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            for (String n : new String[]{"CustomSkullBlocks", "AstroCrateKeys", "AstroLootFilter", "AstroLoottables", "AstroSlotBot",
                    "AstroSpawners", "CosmicMysteryChest", "ParticleLIB", "Vault", "Multiverse-Core"}) {
                sb.append(n).append('=').append(enabled(n)).append(' ');
            }
            log("plugins: " + sb);

            // Test prizes in the SOLAR and SLOTS loot tables
            ItemStack solarA = named(Material.DIAMOND, "Probe Solar A"), solarB = named(Material.EMERALD, "Probe Solar B");
            ItemStack slotA = named(Material.GOLD_INGOT, "Probe Slot A"), slotB = named(Material.IRON_INGOT, "Probe Slot B");
            addLoot("SOLAR", solarA, solarB);
            addLoot("SLOTS", slotA, slotB);

            World w = Bukkit.getWorlds().get(0);
            WorldServer ws = ((CraftWorld) w).getHandle();
            ep = new EntityPlayer(ms, ws, new GameProfile(UUID.fromString("dddddddd-0000-0000-0000-000000000001"), "SkullTester"), new PlayerInteractManager(ws));
            ep.playerConnection = new PlayerConnection(ms, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), ep);
            ms.getPlayerList().players.add(ep);
            registerOnline(ms, ep, true);
            Player p = ep.getBukkitEntity();

            // The player filters prize B of both tables (through AstroLootFilter's own data, keyed by AstroLoottables' names)
            filter(p, "SLOTS", slotB);
            filter(p, "SOLAR", solarB);

            // --- Security System: crate key opener ---
            try {
                Block block = place(w, 100, 120, 100, Material.NOTE_BLOCK, placed);
                Block chestBlock = place(w, 99, 120, 100, Material.CHEST, placed);
                AbilityData data = data(block, AbilityType.SECURITY_SYSTEM, p);
                ItemStack keys = CrateKeyItem.getItem(CrateType.SOLAR);
                keys.setAmount(6);
                p.getInventory().clear();
                p.getInventory().addItem(keys);
                CrateKeyOpenerGUI gui = new CrateKeyOpenerGUI(data, block, p);
                click(gui.getCustomBlockGui(), p, 13);
                log("RESULT crate key opener: chest=" + contents(((Chest) chestBlock.getState()).getInventory())
                        + " | keys left in inventory=" + count(p.getInventory(), keys));
            } catch (Throwable t) {
                fail("crate key opener", t);
            }

            // --- Cosmic Token Generator: slot bot opener ---
            try {
                Block block = place(w, 110, 120, 100, Material.NOTE_BLOCK, placed);
                Block chestBlock = place(w, 109, 120, 100, Material.CHEST, placed);
                AbilityData data = data(block, AbilityType.COSMIC_TOKEN_GENERATOR, p);
                ItemStack tickets = CosmicSlotBotAPI.createSlotBotItem();
                tickets.setAmount(6);
                p.getInventory().clear();
                p.getInventory().addItem(tickets);
                SlotBotOpenerGUI gui = new SlotBotOpenerGUI(data, block, p);
                click(gui.getCustomBlockGui(), p, 13);
                log("RESULT slot bot opener: chest=" + contents(((Chest) chestBlock.getState()).getInventory())
                        + " | tickets left=" + CosmicSlotBotAPI.getSlotsInInventory(p));
            } catch (Throwable t) {
                fail("slot bot opener", t);
            }

            // --- Blaze Stone Totem: mob grinder key storage ---
            try {
                Block block = place(w, 120, 120, 100, Material.NOTE_BLOCK, placed);
                AbilityData data = data(block, AbilityType.BLAZE_STONE_TOTEM, p);
                AbilityData grinder = CoreSkulls.instance.getMobGrinderManager().get(data.getUuid());
                Map<Object, Integer> stored = keyMap(grinder);
                stored.put(CrateType.valueOf("SOLAR"), 3);
                p.getInventory().clear();
                GrinderCustomBlockGUI gui = new GrinderCustomBlockGUI(data, block);
                ItemStack shown = gui.getCustomBlockGui().getGuiItem(28).getItemStack();
                log("grinder menu key button: '" + shown.getItemMeta().getDisplayName() + "'");
                click(gui.getCustomBlockGui(), p, 28);
                log("RESULT grinder: player got " + contents(p.getInventory()) + " | stored keys now=" + stored);
            } catch (Throwable t) {
                fail("mob grinder", t);
            }
        } catch (Throwable t) {
            fail("setup", t);
        } finally {
            for (Block b : placed) {
                try {
                    AbilityData.getAbilityDataMap().remove(AbilityData.getUUID(b.getLocation()));
                } catch (Throwable ignored) {
                }
                if (b.getState() instanceof Chest) ((Chest) b.getState()).getInventory().clear();
                b.setType(Material.AIR);
            }
            if (ep != null) {
                ep.getBukkitEntity().getInventory().clear();
                registerOnline(ms, ep, false);
            }
            log("DONE");
            Bukkit.shutdown();
        }
    }

    private void fail(String what, Throwable t) {
        log("FAIL " + what + " -> " + t + (t.getCause() != null ? " caused by " + t.getCause() : ""));
        t.printStackTrace();
    }

    private static ItemStack named(Material m, String name) {
        ItemStack it = new ItemStack(m);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName(name);
        it.setItemMeta(meta);
        return it;
    }

    private Block place(World w, int x, int y, int z, Material m, List<Block> placed) {
        Block b = w.getBlockAt(x, y, z);
        b.setType(m);
        placed.add(b);
        return b;
    }

    private static AbilityData data(Block block, AbilityType type, Player owner) {
        AbilityData d = AbilityData.getByUUID(AbilityData.getUUID(block.getLocation()));
        d.setLocation(block.getLocation());
        d.setAbilityType(type);
        d.setOwner(owner.getUniqueId());
        return d;
    }

    @SuppressWarnings("unchecked")
    private static Map<Object, Integer> keyMap(AbilityData d) throws ReflectiveOperationException {
        Field f = AbilityData.class.getDeclaredField("crateKeys");
        f.setAccessible(true);
        return (Map<Object, Integer>) f.get(d);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void click(BaseGui gui, Player p, int slot) throws ReflectiveOperationException {
        gui.open(p);
        GuiItem item = gui.getGuiItem(slot);
        Method getAction = GuiItem.class.getDeclaredMethod("getAction");
        getAction.setAccessible(true);
        Object action = getAction.invoke(item);
        InventoryClickEvent e = new InventoryClickEvent(p.getOpenInventory(), InventoryType.SlotType.CONTAINER, slot, ClickType.RIGHT, InventoryAction.PICKUP_HALF);
        Method execute = action.getClass().getMethod("execute", Object.class);
        execute.setAccessible(true);
        execute.invoke(action, e);
    }

    private static String contents(Inventory inv) {
        Map<String, Integer> m = new TreeMap<>();
        for (ItemStack it : inv.getContents()) {
            if (it == null || it.getType() == Material.AIR) continue;
            String n = it.hasItemMeta() && it.getItemMeta().hasDisplayName() ? it.getItemMeta().getDisplayName() : it.getType().name();
            m.merge(n, it.getAmount(), Integer::sum);
        }
        return m.isEmpty() ? "(empty)" : m.toString();
    }

    private static int count(Inventory inv, ItemStack like) {
        int n = 0;
        for (ItemStack it : inv.getContents()) if (it != null && it.isSimilar(like)) n += it.getAmount();
        return n;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addLoot(String table, ItemStack... items) throws Exception {
        ClassLoader cl = Bukkit.getPluginManager().getPlugin("AstroLoottables").getClass().getClassLoader();
        Class<?> api = Class.forName("net.astropvp.loottable.LootTableAPI", true, cl);
        Class<?> tableEnum = Class.forName("net.astropvp.loottable.struct.LootTable", true, cl);
        Field mgrF = api.getDeclaredField("lootTableManager");
        mgrF.setAccessible(true);
        Object data = call(mgrF.get(null), "getLootTable", Enum.valueOf((Class<Enum>) tableEnum, table));
        Object eco = call(data, "getCurrentEcoUpdateLoot");
        for (ItemStack it : items) call(data, "addItem", eco, 1.0, it);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void filter(Player p, String table, ItemStack item) throws Exception {
        Plugin lf = Bukkit.getPluginManager().getPlugin("AstroLootFilter");
        Object mgr = call(lf, "getLootFilterManager");
        Object d = call(mgr, "getLootFilterData", p.getUniqueId());
        Class<?> tableEnum = Class.forName("net.astropvp.loottable.struct.LootTable", true, lf.getClass().getClassLoader());
        call(d, "filterItem", Enum.valueOf((Class<Enum>) tableEnum, table), item);
    }

    private static Object call(Object target, String method, Object... args) throws Exception {
        for (Method m : target.getClass().getMethods()) {
            if (m.getName().equals(method) && m.getParameterCount() == args.length) {
                return m.invoke(target, args);
            }
        }
        throw new NoSuchMethodException(target.getClass().getName() + "." + method);
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
