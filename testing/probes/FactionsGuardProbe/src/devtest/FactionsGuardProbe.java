package devtest;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.util.TitleAPI;
import com.massivecraft.factions.util.material.MaterialDb;
import com.massivecraft.factions.util.particle.ParticleColor;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftTravelAgent;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

/**
 * Isolated-test-server probe: do Factions' version guards keep newer-Minecraft code off a 1.8.8 server,
 * and does claim protection still work with 1.8.8 block names?
 * Read-only towards Factions' code; it only creates a temporary test faction + one claim, then removes them.
 * Logs "PROBE ..." lines and stops the server.
 */
public class FactionsGuardProbe extends JavaPlugin {
    private int errors = 0;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::run, 20L);
    }

    private void log(String s) {
        getLogger().info("PROBE " + s);
    }

    private void err(String what, Throwable t) {
        errors++;
        log("ERROR " + what + " -> " + t);
        t.printStackTrace();
    }

    private void run() {
        MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
        EntityPlayer owner = null, outsider = null;
        Faction fac = null;
        FLocation claim = null;
        try {
            FactionsPlugin fp = FactionsPlugin.getInstance();
            log("server version string = '" + Bukkit.getVersion() + "' -> Factions mcVersion=" + FactionsPlugin.getMCVersion());
            log("particle provider = " + fp.getParticleProvider().name());
            StringBuilder portal = new StringBuilder();
            for (RegisteredListener rl : PlayerPortalEvent.getHandlerList().getRegisteredListeners()) {
                if (rl.getPlugin() == fp) portal.append(rl.getListener().getClass().getSimpleName()).append(' ');
            }
            log("Factions portal listener(s) = " + portal);
            log("WorldGuard integration object = " + fp.getWorldguard());

            // 1.8 GUI material names used by Factions menus, through MaterialDb
            for (String n : new String[]{"ARROW", "LEVER", "WOODEN_SWORD", "STONE_SWORD", "GOLD_SWORD", "STAINED_GLASS_PANE", "LIME_STAINED_GLASS", "BLACK_STAINED_GLASS"}) {
                log("MaterialDb.get(" + n + ") = " + MaterialDb.get(n, null));
            }

            World w = Bukkit.getWorlds().get(0);
            WorldServer ws = ((CraftWorld) w).getHandle();
            owner = fake(ms, ws, "aaaaaaaa-0000-0000-0000-000000000001", "FacOwner");
            outsider = fake(ms, ws, "aaaaaaaa-0000-0000-0000-000000000002", "FacOutsider");
            Player po = owner.getBukkitEntity(), px = outsider.getBukkitEntity();

            Location base = new Location(w, 8, 120, 8);
            fac = Factions.getInstance().createFaction();
            fac.setTag("ProbeFac");
            FPlayer fOwner = FPlayers.getInstance().getByPlayer(po);
            fOwner.setFaction(fac);
            fOwner.setRole(Role.ADMIN);
            claim = new FLocation(base);
            Board.getInstance().setFactionAt(fac, claim);
            log("test faction '" + fac.getTag() + "' claims chunk " + claim + "; FacOutsider has no faction, FacOwner is its leader");

            // Right-click blocks (1.8.8 names) inside the claim
            Material[] blocks = {Material.STONE, Material.CHEST, Material.TRAPPED_CHEST, Material.ENDER_CHEST, Material.FURNACE,
                    Material.BURNING_FURNACE, Material.DISPENSER, Material.DROPPER, Material.HOPPER, Material.BREWING_STAND,
                    Material.CAULDRON, Material.BEACON, Material.ANVIL, Material.JUKEBOX, Material.FLOWER_POT,
                    Material.ENCHANTMENT_TABLE, Material.WOODEN_DOOR, Material.IRON_DOOR_BLOCK, Material.TRAP_DOOR,
                    Material.IRON_TRAPDOOR, Material.FENCE_GATE, Material.WOOD_BUTTON, Material.STONE_BUTTON, Material.LEVER,
                    Material.DIODE_BLOCK_OFF, Material.REDSTONE_COMPARATOR_OFF, Material.SIGN_POST, Material.SOIL};
            Block b = base.getBlock();
            for (Material m : blocks) {
                try {
                    b.setType(m);
                    log(String.format("block %-24s outsider cancelled=%-5s | owner cancelled=%s", m, clickBlock(px, b), clickBlock(po, b)));
                } catch (Throwable t) {
                    err("click " + m, t);
                }
            }
            for (Material plate : new Material[]{Material.WOOD_PLATE, Material.STONE_PLATE}) {
                try {
                    b.setType(plate);
                    PlayerInteractEvent e = new PlayerInteractEvent(px, Action.PHYSICAL, null, b, BlockFace.SELF);
                    Bukkit.getPluginManager().callEvent(e);
                    log(String.format("step on %-22s outsider cancelled=%s", plate, e.isCancelled()));
                } catch (Throwable t) {
                    err("plate " + plate, t);
                }
            }
            b.setType(Material.AIR);

            // Placing items that the item switch lists under 1.13 names (1.8.8 names here)
            b.getRelative(BlockFace.DOWN).setType(Material.RAILS);
            for (Material item : new Material[]{Material.ARMOR_STAND, Material.MINECART, Material.STORAGE_MINECART,
                    Material.POWERED_MINECART, Material.EXPLOSIVE_MINECART, Material.HOPPER_MINECART, Material.COMMAND_MINECART}) {
                try {
                    PlayerInteractEvent e = new PlayerInteractEvent(px, Action.RIGHT_CLICK_BLOCK, new ItemStack(item), b.getRelative(BlockFace.DOWN), BlockFace.UP);
                    Bukkit.getPluginManager().callEvent(e);
                    log(String.format("place %-24s on rail, outsider cancelled=%s", item, e.isCancelled()));
                } catch (Throwable t) {
                    err("place " + item, t);
                }
            }
            b.getRelative(BlockFace.DOWN).setType(Material.AIR);

            // Right-click entities (1.8.8 types) inside the claim
            for (EntityType t : new EntityType[]{EntityType.HORSE, EntityType.PIG, EntityType.MINECART_CHEST, EntityType.MINECART_HOPPER, EntityType.MINECART_FURNACE}) {
                Entity ent = null;
                try {
                    ent = w.spawnEntity(base.clone().add(0.5, 0, 0.5), t);
                    PlayerInteractEntityEvent e = new PlayerInteractEntityEvent(px, ent);
                    Bukkit.getPluginManager().callEvent(e);
                    log(String.format("entity %-20s outsider cancelled=%s", t, e.isCancelled()));
                } catch (Throwable th) {
                    err("entity " + t, th);
                } finally {
                    if (ent != null) ent.remove();
                }
            }

            // Title (1.8.8 has no 5-arg sendTitle -> reflection/packet path)
            try {
                TitleAPI.getInstance().sendTitle(px, "probe", "title", 10, 20, 10);
                log("TitleAPI.sendTitle on 1.8.8 -> no exception");
            } catch (Throwable t) {
                err("TitleAPI", t);
            }

            // Particles (/f sc pillars and fly trails) through the packet provider
            try {
                Object effect = fp.getParticleProvider().effectFromString("REDSTONE");
                fp.getParticleProvider().playerSpawn(px, effect, base, ParticleColor.fromChatColor(ChatColor.RED));
                fp.getParticleProvider().playerSpawn(px, effect, base, 1);
                fp.getParticleProvider().spawn(effect, base, 1, 0, 0, 0, 0);
                log("particles (" + fp.getParticleProvider().effectName(effect) + ") colored + plain + world spawn -> no exception");
            } catch (Throwable t) {
                err("particles", t);
            }

            // Portal into the claim (legacy TravelAgent listener)
            try {
                PlayerPortalEvent e = new PlayerPortalEvent(px, px.getLocation(), base.clone().add(0, 5, 0), new CraftTravelAgent(ws), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
                Bukkit.getPluginManager().callEvent(e);
                log("portal event into claim -> handled by legacy listener, cancelled=" + e.isCancelled()
                        + " (portal limiting enabled in config=" + fp.conf().factions().portals().isLimit() + ")");
            } catch (Throwable t) {
                err("portal", t);
            }

            // /f show (tag parsing incl. PlaceholderAPI guards)
            try {
                boolean ok = Bukkit.dispatchCommand(px, "f show ProbeFac");
                log("/f show ProbeFac by outsider -> dispatched=" + ok + " (no exception)");
            } catch (Throwable t) {
                err("/f show", t);
            }
        } catch (Throwable t) {
            err("setup", t);
        } finally {
            try {
                if (claim != null) Board.getInstance().removeAt(claim);
                if (fac != null) {
                    for (FPlayer f : fac.getFPlayers()) f.resetFactionData();
                    Factions.getInstance().removeFaction(fac.getId());
                }
            } catch (Throwable t) {
                err("cleanup", t);
            }
            if (owner != null) registerOnline(ms, owner, false);
            if (outsider != null) registerOnline(ms, outsider, false);
            log("RESULT errors=" + errors);
            Bukkit.shutdown();
        }
    }

    private boolean clickBlock(Player p, Block b) {
        PlayerInteractEvent e = new PlayerInteractEvent(p, Action.RIGHT_CLICK_BLOCK, null, b, BlockFace.UP);
        Bukkit.getPluginManager().callEvent(e);
        return e.isCancelled();
    }

    private EntityPlayer fake(MinecraftServer ms, WorldServer ws, String uuid, String name) {
        EntityPlayer ep = new EntityPlayer(ms, ws, new GameProfile(UUID.fromString(uuid), name), new PlayerInteractManager(ws));
        ep.playerConnection = new PlayerConnection(ms, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), ep);
        ep.setLocation(8.5, 121, 30.5, 0, 0);
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
