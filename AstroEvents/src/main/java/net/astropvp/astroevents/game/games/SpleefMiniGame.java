package net.astropvp.astroevents.game.games;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.game.MiniGame;
import net.astropvp.astroevents.game.MiniGameOptions;
import net.astropvp.astroevents.game.MiniGameState;
import net.astropvp.astroevents.game.MiniGameType;
import net.astropvp.astroevents.game.players.SpleefPlayer;
import net.astropvp.astroevents.game.tasks.MiniGameCountDown;
import net.astropvp.astroevents.game.tasks.MiniGameFlyDisableTask;
import net.astropvp.astroevents.utils.BlockUtil;
import net.astropvp.astroevents.utils.Pair;
import net.astropvp.astroevents.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpleefMiniGame extends MiniGame<SpleefPlayer> {

    private final AstroEvents plugin;
    private final List<Pair<Location, Location>> platformCorners;

    protected SpleefMiniGame(AstroEvents plugin) {
        super(plugin,
                MiniGameType.SPLEEF,
                new HashMap<>(),
                MiniGameOptions.builder()
                        .blockBreak(true)
                        .itemPickUp(true)
                        .build(),
                CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("SpleefLootbox"),
                Material.SNOW_BALL
        );
        this.plugin = plugin;
        this.platformCorners = new ArrayList<>();

        ConfigurationSection platformCornersConfigSection = this.getConfiguration().getConfigurationSection("minigame.platform-corners");

        platformCornersConfigSection.getKeys(false).forEach(str -> {
            Location cornerOne = super.getLocationFromConfig(platformCornersConfigSection.getConfigurationSection(str + ".corner-one"));
            Location cornerTwo = super.getLocationFromConfig(platformCornersConfigSection.getConfigurationSection(str + ".corner-two"));
            Pair<Location, Location> corners = new Pair<>(cornerOne, cornerTwo);
            this.platformCorners.add(corners);
        });
    }

    @Override
    public void startMiniGame() {
        super.setMiniGameState(MiniGameState.IN_PROGRESS);

        super.getParticipants().forEach(((uuid, spleefPlayer) -> {
            Utils.sendTitle(spleefPlayer.getPlayer(), CC.Aqua + "Start!", "", 10, 40, 10);
        }));
    }

    @Override
    public void startMiniGameCountDown() {
        super.setMiniGameState(MiniGameState.STARTING);
        new MiniGameCountDown(this).runTaskTimer(plugin, 0L, 20L);
        new MiniGameFlyDisableTask(this).runTaskTimer(plugin, 0L, 10L);
    }

    @Override
    public void stopMiniGame() {
        super.getParticipants().forEach((player, participant) -> {
            this.leaveMiniGame(participant.getPlayer(), true);
        });

        super.clearParticipants();
        super.setMiniGameState(MiniGameState.NOT_STARTED);
        this.fixAllPlatforms();
    }

    @Override
    public void joinMiniGame(Player player) {
        if (isParticipant(player)) return;
        super.addParticipant(new SpleefPlayer(player));
        player.teleport(super.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.setHealth(20);
        player.setFoodLevel(20);
        super.clearPotionEffects(player);
        super.clearInventory(player);
        player.setAllowFlight(false);
        player.setFlying(false);
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
        if (!isParticipant(player) && !isSpectator(player)) return;

        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.setWalkSpeed((float) 0.2);
        super.clearInventory(player);
        super.clearPotionEffects(player);

        if (super.isParticipant(player)) {
            super.getParticipant(player).getPlayerInventoryContents().restore(player);
        }

        if (stopMiniGame) return;

        if (isParticipant(player)) {
            super.removeParticipant(player);
        }

        //winner
    }

    @EventHandler
    public void onDamageWhileInSpleef(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!super.isParticipant((Player) event.getEntity())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreakWhileSpleefIsStarting(BlockBreakEvent event) {
        if (!super.isParticipant(event.getPlayer())) return;
        if (super.getMiniGameState() != MiniGameState.IN_PROGRESS) return;

        event.setCancelled(true);
    }

    private void fixAllPlatforms() {
        for (Pair<Location, Location> locationPair : this.platformCorners) {
            this.fixPlatform(locationPair.getLeft(), locationPair.getRight());
        }
    }

    private void fixPlatform(Location cornerOne, Location cornerTwo) {
        final int y = (int) cornerOne.getY();

        for (int i = (int) cornerOne.getX(); i < cornerTwo.getX(); i++) {
            for (int j = (int) cornerOne.getZ(); j < cornerTwo.getZ(); j++) {
                BlockUtil.setBlockFast(this.getGameWorld(), i, y, j, 80, (byte) 0, false);
            }
        }
    }
}
