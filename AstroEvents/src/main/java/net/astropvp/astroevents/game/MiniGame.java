package net.astropvp.astroevents.game;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.utils.Utils;
import net.astropvp.astroevents.utils.configuration.AbstractConfig;
import net.astropvp.giftcardshop.GiftcardAPI;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

@Getter
public abstract class MiniGame<T extends MiniGamePlayer> implements Listener {

    private final byte minPlayers;
    private final byte maxPlayers;
    private final Location spawnLocation;
    private final AbstractConfig abstractConfig;
    private final ConfigurationSection configuration;
    private final Map<UUID, T> participants;
    private final MiniGameType miniGameType;
    private final String countdownMessage;
    private final World gameWorld;
    private final MiniGameOptions miniGamesOptions;
    private final int countdownTime;
    private final List<String> rewardCommands;
    private final Set<T> spectators;
    private final MysteryChest mysteryChest;
    private final Material guiMaterial;

    @Setter
    private MiniGameState miniGameState;

    @SneakyThrows
    protected MiniGame(AstroEvents plugin, MiniGameType miniGameType, Map<UUID, T> participantsMap, MiniGameOptions miniGamesOptions, MysteryChest mysteryChest, Material guiMaterial) {
        this.miniGameType = miniGameType;
        this.miniGameState = MiniGameState.NOT_STARTED;
        this.miniGamesOptions = miniGamesOptions;
        this.participants = participantsMap;
        this.spectators = new HashSet<>();
        this.abstractConfig = plugin.getConfigManager().getAbstractConfig(miniGameType.getConfigFile());

        this.configuration = abstractConfig.getConfig().getConfigurationSection("minigame");
        this.gameWorld = Bukkit.getWorld(this.configuration.getString("world"));
        this.countdownMessage = Utils.color(this.configuration.getString("countdown-message"));
        this.minPlayers = (byte) this.configuration.getInt("min-players");
        this.maxPlayers = (byte) this.configuration.getInt("max-players");
        this.countdownTime = this.configuration.getInt("countdown-time");

        this.spawnLocation = this.getLocationFromConfig(configuration.getConfigurationSection("spawn-location"));
        this.rewardCommands = this.configuration.getStringList("rewards");
        this.mysteryChest = mysteryChest;
        this.guiMaterial = guiMaterial;

    }

    protected MiniGame(AstroEvents plugin, MiniGameType miniGameType) {
        this(plugin, miniGameType, new HashMap<>(), MiniGameOptions.builder().build(), CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("BloodyNote"), Material.STONE);
    }

    public void addParticipant(T participant) {
        this.participants.put(participant.getUUID(), participant);
    }

    protected void removeParticipant(Player player) {
        this.participants.remove(player.getUniqueId());
    }

    protected void removeParticipant(T participant) {
        this.participants.remove(participant.getPlayer().getUniqueId());
    }

    protected void clearParticipants() {
        this.participants.clear();
    }

    public boolean isParticipant(Player player) {
        return this.participants.containsKey(player.getUniqueId());
    }

    public T getParticipant(Player player) {
        return this.getParticipants().get(player.getUniqueId());
    }

    public boolean isFull() {
        return this.maxPlayers <= this.participants.size();
    }

    public abstract void startMiniGame();
    public abstract void startMiniGameCountDown();
    public abstract void stopMiniGame();
    public abstract void joinMiniGame(Player player);
    public abstract void leaveMiniGame(Player player);

    public void leaveMiniGame(T miniGamePlayer) {
        this.leaveMiniGame(miniGamePlayer.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (!isParticipant(event.getPlayer()) && !isSpectator(event.getPlayer())) return;
        leaveMiniGame(event.getPlayer());
    }

    @EventHandler
    public void onFoodLossWhileInGame(FoodLevelChangeEvent event) {
        if (!isValidParticipant(event.getEntity())) return;
        if (miniGamesOptions.isFoodLoss()) return;
        ((Player) event.getEntity()).setFoodLevel(20);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickUpWhileInGame(PlayerPickupItemEvent event) {
        if (!isValidParticipant(event.getPlayer())) return;
        if (miniGamesOptions.isItemPickUp()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlaceWhileInGame(BlockPlaceEvent event) {
        if (!isValidParticipant(event.getPlayer())) return;
        if (miniGamesOptions.isBlockPlace()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreakWhileInGame(BlockBreakEvent event) {
        if (!isValidParticipant(event.getPlayer())) return;
        if (miniGamesOptions.isBlockBreak()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPickUpWhileInGame(PlayerDropItemEvent event) {
        if (!isValidParticipant(event.getPlayer())) return;
        if (miniGamesOptions.isItemDrop()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onFallDamageToPlayerWhileInGame(EntityDamageEvent event) {
        if (!isValidParticipant(event.getEntity())) return;
        if (miniGamesOptions.isFallDamage()) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if (!isValidParticipant(event.getWhoClicked())) return;
        if (miniGamesOptions.isInventoryInteract()) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandWhileInGame(PlayerCommandPreprocessEvent event) {
        if (!isValidParticipant(event.getPlayer())) return;

        if (!miniGamesOptions.isBlockCommands()) return;

        String command = event.getMessage().replace("/", "").toLowerCase(Locale.ROOT);

        if (Arrays.stream(this.miniGamesOptions.getExcludedBlockedCommands()).noneMatch(command::startsWith))
            event.setCancelled(true);
    }

    private boolean isValidParticipant(Player player) {
        if (!this.isParticipant(player) && !this.isSpectator(player)) return false;
        if (player.isOp()) return false;
        return !player.hasPermission("astro.admin");
    }

    private boolean isValidParticipant(Entity entity) {
        if (!(entity instanceof Player)) return false;
        return isValidParticipant((Player) entity);
    }

    public void giveWinnerRewards(Player player) {
        this.getRewardCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
        try {
            if (Math.random() < 0.1) {
                GiftcardAPI.createGiftCard(player, 10);
            } else {
               player.sendMessage(CC.translate("&c&l(!)&c Unfortunately you didn't get a 5$ Buycraft Giftcard, better luck next time!"));
            }
        } catch (IOException | UnirestException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void giveWinnerRewards(T miniGamePlayer) {
        this.giveWinnerRewards(miniGamePlayer.getPlayer());
    }

    protected void clearInventory(Player player){
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.getInventory().clear();
        player.updateInventory();
    }

    protected void clearPotionEffects(Player player){
        for(PotionEffect effect:player.getActivePotionEffects()){
            player.removePotionEffect(effect.getType());
        }
    }


    protected void addSpectator(T player) {
        if (player == null) return;
        this.spectators.add(player);
        if (this.isParticipant(player.getPlayer())) this.removeParticipant(player);
        player.getPlayer().teleport(this.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    protected Optional<T> getSpectator(Player player) {
        return this.spectators.stream().filter(spectator -> spectator.getPlayer().equals(player)).findFirst();
    }

    public boolean isSpectator(Player player) {
        if (player == null) return false;
        return this.spectators.stream().anyMatch(spectator -> spectator.getPlayer().equals(player));
    }

    public void sendMessageToAllParticipants(String message) {
        this.getParticipants().forEach((uuid, player) -> player.sendMessage(message));
    }

    public boolean isSpectator(T sumoPlayer) {
        return this.spectators.contains(sumoPlayer);
    }

    protected void clearSpectators() {
        this.spectators.clear();
    }

    protected void removeSpectator(T player) {
        this.spectators.remove(player);
    }

    protected void removeSpectator(Player player) {
        this.spectators.removeIf(spectator -> spectator.getPlayer().equals(player));
    }

    protected Location getLocationFromConfig(ConfigurationSection configSection) {
        double x = configSection.getDouble("x");
        double y = configSection.getDouble("y");
        double z = configSection.getDouble("z");
        float yaw = Utils.defaultIfNull((float) configSection.getDouble("yaw"), (float) 0.0);
        float pitch = Utils.defaultIfNull((float) configSection.getDouble("pitch"), (float) 0.0);
        return new Location(this.gameWorld, x, y, z, yaw, pitch);
    }
}
