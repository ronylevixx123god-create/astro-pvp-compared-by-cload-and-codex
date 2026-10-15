package net.astropvp.astroevents.game.games;

import lombok.Getter;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.game.MiniGame;
import net.astropvp.astroevents.game.MiniGameOptions;
import net.astropvp.astroevents.game.MiniGameState;
import net.astropvp.astroevents.game.MiniGameType;
import net.astropvp.astroevents.game.players.SumoPlayer;
import net.astropvp.astroevents.game.tasks.*;
import net.astropvp.astroevents.utils.Pair;
import net.astropvp.astroevents.utils.Utils;
import org.apache.commons.collections4.map.LinkedMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class SumoMiniGame extends MiniGame<SumoPlayer> {

    @Getter
    private final Location fightLocationOne;
    @Getter
    private final Location fightLocationTwo;
    private final AstroEvents plugin;
    @Getter
    private final Pair<SumoPlayer, SumoPlayer> duelPlayers;
    private final Map<UUID, SumoPlayer> tempMap;

    public SumoMiniGame(AstroEvents plugin) {
        super(plugin, MiniGameType.SUMO, new LinkedMap<>(), MiniGameOptions.builder().excludedBlockedCommands(new String[]{"sumo"}).build(), CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("SumoLootbox"), Material.STICK);
        this.plugin = plugin;
        this.duelPlayers = new Pair<>(null, null);
        this.tempMap = new HashMap<>();

        ConfigurationSection fightLocationOneSection = super.getConfiguration().getConfigurationSection("fight-location-one");
        Optional<Location> possibleFightLocationOne = Optional.of(super.getLocationFromConfig(fightLocationOneSection));

        this.fightLocationOne = possibleFightLocationOne.orElseThrow(() -> new IllegalArgumentException("Invalid fight one location"));

        ConfigurationSection fightLocationTwoSection = super.getConfiguration().getConfigurationSection("fight-location-two");
        Optional<Location> possibleFightLocationTwo = Optional.of(super.getLocationFromConfig(fightLocationTwoSection));

        this.fightLocationTwo = possibleFightLocationTwo.orElseThrow(() -> new IllegalArgumentException("Invalid fight two location"));;
    }

    @Override
    public void startMiniGame() {
        super.setMiniGameState(MiniGameState.IN_PROGRESS);
        this.fight();
        new SumoDeathCheckTask(this).runTaskTimer(plugin, 0L, 20L);
        new SpectatorTeleportBackTask(this).runTaskTimer(plugin, 0L, 20L);
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

        super.getSpectators().forEach(spectator -> {
            this.leaveMiniGame(spectator.getPlayer(), true);
        });

        super.clearParticipants();
        super.clearSpectators();
        super.setMiniGameState(MiniGameState.NOT_STARTED);
    }

    @Override
    public void joinMiniGame(Player player) {
        if (isParticipant(player)) return;
        super.addParticipant(new SumoPlayer(player));
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

        if (super.isSpectator(player)) {
            Optional<SumoPlayer> sumoPlayer = super.getSpectator(player);
            sumoPlayer.ifPresent(participant -> participant.getPlayerInventoryContents().restore(player));
        }

        if (stopMiniGame) return;

        if (isParticipant(player)) {
            super.removeParticipant(player);
        }

        if (isSpectator(player)) {
            super.removeSpectator(player);
        }

        if (this.isInDuel(player)) {
            this.choseWinner(this.getOpposingPlayer(player).getPlayer(), player);
        }
    }

    @EventHandler
    public void onDamageWhileInGame(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;
        if (!isParticipant((Player) event.getEntity())) return;
        if (!isParticipant((Player) event.getDamager())) return;

        if (super.getMiniGameState() == MiniGameState.STARTING) {
            event.setCancelled(true);
            return;
        }

        if (super.getMiniGameState() == MiniGameState.IN_PROGRESS) {
            event.setDamage(0.0);
        }

        if (!this.isInDuel(super.getParticipant((Player) event.getEntity()))) {
            event.setCancelled(true);
        }
    }

    public void choseWinner(Player winner, Player loser) {
        this.getDuelPlayers().clear();

        if (loser.isOnline()) {
            SumoPlayer sumoPlayer = super.getParticipant(loser);
            this.addSpectator(sumoPlayer);
        }

        if (this.getParticipants().size() <= 1) {
            this.stopMiniGame();
            this.giveWinnerRewards(winner);
            Bukkit.broadcastMessage(Utils.color(super.getConfiguration().getString("win-message").replace("{player}", winner.getName())));
            return;
        }

        winner.teleport(this.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);

        this.nextFight();
    }

    public void fight() {
        this.duelPlayers.setLeft(this.getParticipantsQueue().getValue(0));
        this.duelPlayers.setRight(this.getParticipantsQueue().getValue(1));

        this.duelPlayers.getLeft().getPlayer().setAllowFlight(false);
        this.duelPlayers.getLeft().getPlayer().setFlying(false);

        this.duelPlayers.getRight().getPlayer().setAllowFlight(false);
        this.duelPlayers.getRight().getPlayer().setFlying(false);

        this.duelPlayers.getLeft().getPlayer().teleport(fightLocationOne, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        this.duelPlayers.getRight().getPlayer().teleport(fightLocationTwo, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        new SumoStartDuelTask(this).runTaskTimer(plugin, 10, 10);
    }

    public void nextFight() {
        this.moveToEndOfQueue();
        this.moveToEndOfQueue();

        this.fight();
    }

    public void moveToEndOfQueue() {
        this.tempMap.put(this.getParticipantsQueue().get(0), this.getParticipantsQueue().getValue(0));
        this.getParticipantsQueue().remove(0);
        this.getParticipants().putAll(this.tempMap);
        this.tempMap.clear();
    }

    public boolean isInDuel(SumoPlayer sumoPlayer) {
        return this.duelPlayers.contains(sumoPlayer);
    }

    public SumoPlayer getOpposingPlayer(SumoPlayer sumoPlayer) {
        return this.getOpposingPlayer(sumoPlayer.getPlayer());
    }

    public SumoPlayer getOpposingPlayer(Player player) {
        if (!this.isInDuel(player)) return null;
        return player.equals(this.duelPlayers.getLeft().getPlayer()) ? this.duelPlayers.getRight() : this.duelPlayers.getLeft();
    }

    public boolean isInDuel(Player sumoPlayer) {
        if (this.duelPlayers.getLeft() == null) return false;
        if (this.duelPlayers.getRight() == null) return false;
        return this.duelPlayers.getLeft().getPlayer().equals(sumoPlayer) || this.duelPlayers.getRight().getPlayer().equals(sumoPlayer);
    }

    public LinkedMap<UUID, SumoPlayer> getParticipantsQueue() {
        return ((LinkedMap<UUID, SumoPlayer>) super.getParticipants());
    }
}
