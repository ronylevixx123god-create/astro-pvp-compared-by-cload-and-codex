package net.astropvp.astroevents.game.games;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.game.MiniGame;
import net.astropvp.astroevents.game.MiniGameOptions;
import net.astropvp.astroevents.game.MiniGameState;
import net.astropvp.astroevents.game.MiniGameType;
import net.astropvp.astroevents.game.players.ParkourPlayer;
import net.astropvp.astroevents.game.tasks.MiniGameCountDown;
import net.astropvp.astroevents.game.tasks.MiniGameFlyDisableTask;
import net.astropvp.astroevents.game.tasks.ParkourTimeTask;
import net.astropvp.astroevents.utils.Clickable;
import net.astropvp.astroevents.utils.Utils;
import net.astropvp.astroevents.utils.configuration.ConfigParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ParkourMiniGame extends MiniGame<ParkourPlayer> {

    @Getter
    private long startTime;
    private final BiMap<Location, Byte> parkourCheckpoints;
    private final Map<Byte, Float> parkourYawCheckpoints;
    private final AstroEvents plugin;
    private final Location endLocation;
    @Getter
    private final String parkourTimeMessage;
    private final ItemStack checkpointItem;
    private final ItemStack leaveItem;

    public ParkourMiniGame(AstroEvents plugin) {
        super(plugin, MiniGameType.PARKOUR, new HashMap<>(), MiniGameOptions.builder().excludedBlockedCommands(new String[]{"parkour"}).build(), CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("ParkourLootbox"), Material.FEATHER);
        this.plugin = plugin;

        this.startTime = 0;
        this.parkourCheckpoints = HashBiMap.create();
        this.parkourYawCheckpoints = new HashMap<>();

        ConfigurationSection checkpointsConfigSection = super.getConfiguration().getConfigurationSection("checkpoints");

        checkpointsConfigSection.getKeys(false).forEach((s -> {
            ConfigurationSection checkpointSection = checkpointsConfigSection.getConfigurationSection(s);
            Location location = super.getLocationFromConfig(checkpointSection);
            Byte number = Byte.parseByte(s);
            this.parkourYawCheckpoints.put(number, location.getYaw());
            location.setYaw((float) 0.0);
            parkourCheckpoints.put(location, Byte.parseByte(s));
        }));

        this.endLocation = super.getLocationFromConfig(super.getConfiguration().getConfigurationSection("end-location"));
        this.parkourTimeMessage = Utils.color(this.getConfiguration().getString("parkour-time-message"));
        this.leaveItem = ConfigParser.parseItem(super.getConfiguration().getConfigurationSection("items.leave-item"));
        this.checkpointItem = ConfigParser.parseItem(super.getConfiguration().getConfigurationSection("items.checkpoint-item"));
    }

    public Byte getCheckPoint(Location location) {
        return this.parkourCheckpoints.get(location);

    }

    public boolean isCheckPoint(Location location) {
        return this.getCheckpoints().containsKey(location);
    }

    @Override
    public void startMiniGame() {
        this.startTime = System.currentTimeMillis();
        super.setMiniGameState(MiniGameState.IN_PROGRESS);
        super.getParticipants().forEach((playerUUID, participant) -> {
            Player player = participant.getPlayer();
            player.getInventory().setItem(3, this.getCheckpointItem());
            player.getInventory().setItem(5, this.getLeaveItem());
            if (this.getCheckpoints().inverse().containsKey((byte) 0))
                player.teleport(this.getCheckpoints().inverse().get((byte) 0), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        });
        new ParkourTimeTask(this).runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void startMiniGameCountDown() {
        this.loadCheckpoints();
        super.setMiniGameState(MiniGameState.STARTING);
        new MiniGameCountDown(this).runTaskTimer(plugin, 0L, 20L);
        new MiniGameFlyDisableTask(this).runTaskTimer(plugin, 0L, 10L);
    }

    @Override
    public void stopMiniGame() {
        this.unLoadCheckpoints();
        super.getParticipants().forEach((player, participant) -> {
            this.leaveMiniGame(participant.getPlayer(), true);
        });
        super.clearParticipants();
        super.setMiniGameState(MiniGameState.NOT_STARTED);
    }

    @Override
    public void joinMiniGame(Player player) {
        if (isParticipant(player)) return;
        super.addParticipant(new ParkourPlayer(player));
        player.teleport(super.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setAllowFlight(false);
        player.setFlying(false);
        super.clearPotionEffects(player);
        super.clearInventory(player);
    }

    @Override
    public void leaveMiniGame(Player player) {
        if (this.getParticipants().size() <= 1 && this.getMiniGameState() == MiniGameState.IN_PROGRESS) {
            this.stopMiniGame();
            return;
        }
        this.leaveMiniGame(player, false);
    }

    private void leaveMiniGame(Player player, boolean stopMiniGame) {
        if (!isParticipant(player)) return;
        player.teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        super.clearInventory(player);
        super.clearPotionEffects(player);
        super.getParticipant(player).getPlayerInventoryContents().restore(player);
        if (stopMiniGame) return;
        super.removeParticipant(player);
    }

    @EventHandler
    public void onDamageWhileInParkour(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!super.isParticipant((Player) event.getEntity())) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            teleportParticipantToCheckpoint((Player) event.getEntity());
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void checkpointCross(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return;
        if (!super.isParticipant(event.getPlayer())) return;

        Location location = event.getClickedBlock().getLocation();
        ParkourPlayer parkourPlayer = super.getParticipant(event.getPlayer());

        if (this.endLocation.equals(location)) {
            this.announceWinner(event.getPlayer());
            this.stopMiniGame();
            super.giveWinnerRewards(event.getPlayer());
            return;
        }

        if (!isCheckPoint(location)) return;

        Byte locationCheckpoint = this.getCheckPoint(location);

        if (parkourPlayer.getCurrentCheckpoint() >= locationCheckpoint) return;

        parkourPlayer.sendMessage(super.getConfiguration().getString("checkpoint-reached-message").replace("{number}", String.valueOf(locationCheckpoint)));
        parkourPlayer.setCurrentCheckpoint(locationCheckpoint);

    }

    @EventHandler
    public void parkourItemClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK ) return;
        if (!super.isParticipant(event.getPlayer())) return;
        if (event.getItem() == null) return;

        if (event.getItem().getType() == this.checkpointItem.getType()) {
            teleportParticipantToCheckpoint(event.getPlayer());
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.0F);
        }

        if (event.getItem().getType() == this.leaveItem.getType()) {
            Clickable parkourLeaveClickable = new Clickable("");
            parkourLeaveClickable.add(Utils.color(super.getConfiguration().getString("parkour-confirm-leave-message")), Utils.color("&7click to confirm"), "/parkour leave");
            parkourLeaveClickable.sendToPlayer(event.getPlayer());
        }
    }

    public void announceWinner(Player player) {
        String time = Utils.getTimeFromSecsToMins((System.currentTimeMillis() - this.getStartTime()) / 1000);
        Bukkit.broadcastMessage(Utils.color(super.getConfiguration().getString("win-message").replace("{player}", player.getName()).replace("{time}", time)));
    }

    public void teleportParticipantToCheckpoint(Player player) {
        Byte checkpoint = super.getParticipant(player).getCurrentCheckpoint();
        Location location = this.getCheckpoints().inverse().get(checkpoint);

        if (location != null) {
            location = location.clone();
            location.add(0.5, 0.0, 0.5);
            location.setYaw(parkourYawCheckpoints.get(checkpoint));
        }

        player.teleport(Utils.defaultIfNull(location, getSpawnLocation()), PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    private void createCheckpointHolo(byte number, Location location) {
        location.getBlock().setType(Material.GOLD_PLATE);
        Hologram hologram = HologramsAPI.createHologram(plugin, location.add(0.5, 2.0, 0.5));
        hologram.appendTextLine(Utils.color("&aCheckpoint #" + number));

    }

    private void createEndCheckpoint() {
        this.endLocation.getBlock().setType(Material.GOLD_PLATE);
        Hologram hologram = HologramsAPI.createHologram(plugin, this.endLocation.clone().add(0.5, 2.0, 0.5));
        hologram.appendTextLine(Utils.color("&aParkour End"));
    }

    private void loadCheckpoints() {
        this.getCheckpoints().forEach(((location, number) -> {
            this.createCheckpointHolo(number, location.clone());
        }));
        createEndCheckpoint();
    }

    private void unLoadCheckpoints() {
        this.getCheckpoints().forEach(((location, number) -> {
            location.getBlock().setType(Material.AIR);
        }));

        this.endLocation.getBlock().setType(Material.AIR);

        for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {
            if (!hologram.getWorld().equals(super.getGameWorld())) return;
            //if (!parkourCheckpoints.containsKey(hologram.getLocation().add(0.5, 3.0, 0.5))) return;
            hologram.delete();
        }
    }

    public ImmutableBiMap<Location, Byte> getCheckpoints() {
        return ImmutableBiMap.copyOf(this.parkourCheckpoints);
    }

    private ItemStack getCheckpointItem() {
        return this.checkpointItem;
    }

    private ItemStack getLeaveItem() {
        return this.leaveItem;
    }
}
