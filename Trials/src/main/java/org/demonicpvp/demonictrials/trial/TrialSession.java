package org.demonicpvp.demonictrials.trial;


import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.utils.DuelUtils;
import me.libraryaddict.disguise.DisguiseAPI;
import moss.factions.shade.mkremins.fanciful.FancyMessage;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.enchantments.enchantments.armor.Phoenix;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.gui.DealOrNoDealGui;
import org.demonicpvp.demonictrials.gui.MadnessModifierBallotGui;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;
import org.demonicpvp.demonictrials.trial.modifier.madness.MadnessModifier;
import org.demonicpvp.demonictrials.trial.modifier.madness.impl.ProtectThePresidentModifier;
import org.demonicpvp.demonictrials.trial.modifier.stackable.StackableModifier;
import org.demonicpvp.demonictrials.trial.room.RoomProgress;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.trial.room.impl.*;
import org.demonicpvp.demonictrials.trial.trinket.ITrialTrinket;
import org.demonicpvp.demonictrials.trial.trinket.TrialTrinket;
import org.demonicpvp.demonictrials.util.WorldEditHelper;
import org.github.paperspigot.Title;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TrialSession implements Listener {
    private final int id;

    private final String NEXT_ROOM_PORTAL_KEY = "nextRoomPortalBlock";

    private World world;
    private final OfflinePlayer spawnPlayer;
    private final Set<Player> players;
    private final int maxPlayers;
    private Set<OfflinePlayer> allPlayers;
    private final List<Pair<ITrialTrinket, Integer>> trinkets;
    private final Map<StackableModifier, Integer> stackableModifiers;
    private final Map<MadnessModifier, AbstractMadnessModifier> madnessModifiers;
    private final Location portalLocation;
    private final int portalSecondsLeft;
    private final double ticksSpawnedAt;
    private final int madnessModifierBallotOptions;
    private int currentRoomNumber;
    private MadnessModifierBallotGui madnessModifierBallotGui;

    private final List<TrialRoom> rooms;
    private final List<RoomProgress> roomStats;

    private Location spawnLocation;

    private TrialRoom currentRoom;

    private List<Player> playersEnteredNextRoomPortal;

    private int roomSecondsLeft;
    private int runtimeInSeconds;
    private int roomCompletedAtTicks;
    private int nextRoomStartCountdownTicks;

    private boolean pauseSecondsLeft;

    private boolean startedPrompts;
    private boolean startedMadnessModifierPrompt;
    private boolean startedDecisionSkip;
    private Set<UUID> decisionSkips;

    private TrialTier currentTier;

    private BukkitRunnable timeLeftTask;
    private BukkitRunnable madnessModifierTask;
    private BukkitTask roomEndTask;

    private TrialState state;

    public TrialSession(World world, Location portalLocation, List<Pair<ITrialTrinket, Integer>> trinkets, int portalSecondsLeft, double ticksSpawnedAt, OfflinePlayer spawnPlayer) {
        this.id = TrialManager.NEXT_TRIAL_ID++;
        this.world = world;
        this.portalLocation = portalLocation;
        this.trinkets = trinkets;
        this.maxPlayers = 8 + this.getTrinket(TrialTrinket.MAX_PLAYERS);
        this.players = Sets.newHashSet();
        this.state = TrialState.LOADING;
        this.currentTier = TrialTier.RECRUIT;
        this.ticksSpawnedAt = ticksSpawnedAt;
        this.portalSecondsLeft = portalSecondsLeft;
        this.pauseSecondsLeft = false;
        this.roomStats = Lists.newLinkedList();
        this.spawnPlayer = spawnPlayer;
        this.stackableModifiers = Maps.newHashMap();
        this.madnessModifiers = Maps.newHashMap();
        this.currentRoomNumber = 1;
        this.madnessModifierBallotOptions = 2 + this.getTrinket(TrialTrinket.MADNESS_BALLOT_OPTIONS);
        this.startedPrompts = false;

        this.rooms = Lists.newArrayList(new HotscotchRoom(this), new ColorCrisisRoom(this), new TargetPracticeRoom(this), new MazeSweeperRoom(this), new SimonRoom(this), new CodeSwitchingRoom(this), new RaidingRainbowRoom(this));
        /*new MysteryCubeRoom(this),*/ /*new ThunderstruckRoom(this),new PortalPanicRoom(this), new ParkourRelayRoom(this)*/
        //this.currentRoom = this.rooms.get(0);
    }

    public int getId() {
        return this.id;
    }

    public World getWorld() {
        return this.world;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }

    public Set<OfflinePlayer> getAllPlayers() {
        return this.allPlayers;
    }

    public List<Player> getPlayersEnteredNextRoomPortal() {
        return this.playersEnteredNextRoomPortal;
    }

    public boolean isPausingTimer() {
        return this.pauseSecondsLeft;
    }

    public Location getPortalLocation() {
        return this.portalLocation;
    }

    public TrialState getState() {
        return this.state;
    }

    public void setState(TrialState state) {
        this.state = state;
    }

    public void sendMessage(String message) {
        this.getPlayers().forEach(p -> CommonsUtil.sendMessage(p, message));
    }

    public ItemStack applySneakSkin(ItemStack item, String skinId) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("cosmicData");
        cosmicData.setString("itemSkinType", skinId);
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        return wrapper.build();
    }

    public ItemStack[] getHazmatArmor() {
        ItemStack[] armor = new ItemStack[4];

        armor[3] = this.getItemWithTrialLore(this.applySneakSkin(
                CommonsUtil.createItem(
                        Material.GOLD_HELMET,
                        "&e&lHazmat Helmet",
                        "&7&oAn impermeable whole-body",
                        "&7&ogarment worn as protection",
                        "&7&oagainst hazardous materials."
                ),
                "VIRUS_HELMET_HAZMAT"
        ));

        armor[2] = this.getItemWithTrialLore(this.applySneakSkin(
                CommonsUtil.createItem(
                        Material.GOLD_CHESTPLATE,
                        "&e&lHazmat Chestplate",
                        "&7&oAn impermeable whole-body",
                        "&7&ogarment worn as protection",
                        "&7&oagainst hazardous materials."
                ),
                "DEEP_SPACE_BACKPACK"
        ));

        armor[1] = this.getItemWithTrialLore(this.applySneakSkin(
                CommonsUtil.createItem(
                        Material.GOLD_LEGGINGS,
                        "&e&lHazmat Leggings",
                        "&7&oAn impermeable whole-body",
                        "&7&ogarment worn as protection",
                        "&7&oagainst hazardous materials."
                ),
                "CORRUPT_BELT_UTILITY"
        ));

        armor[0] = this.getItemWithTrialLore(this.applySneakSkin(
                CommonsUtil.createItem(
                        Material.GOLD_BOOTS,
                        "&e&lHazmat Boots",
                        "&7&oAn impermeable whole-body",
                        "&7&ogarment worn as protection",
                        "&7&oagainst hazardous materials."
                ),
                "VIRUS_BOOTS"
        ));

        return armor;
    }

    public MadnessModifierBallotGui getMadnessModifierBallotGui() {
        return this.madnessModifierBallotGui;
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public OfflinePlayer getSpawnPlayer() {
        return this.spawnPlayer;
    }

    public TrialRoom getCurrentRoom() {
        return this.currentRoom;
    }

    public int getCurrentRoomNumber() {
        return this.currentRoomNumber;
    }

    public int getRuntimeInSeconds() {
        return this.runtimeInSeconds;
    }

    public TrialTier getCurrentTier() {
        return this.currentTier;
    }

    public boolean isFull() {
        return this.getPlayers().size() >= this.maxPlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void updateScoreboard(Player player) {
        //BPlayerBoard board = DemonicScoreboard.getOrCreateBoard(player, "&6&lTrial &7" + this.getDateSuffix());

        ArrayList<String> lines = Lists.newArrayList(
                "&7ID: " + this.getId()
        );

        String color = this.currentTier.getColor() + "&l";

        if (this.getState() == TrialState.LOADING) {
            lines.add("&6&lStatus");
            lines.add("&a&lOPEN");
            lines.add("&r");
            lines.add("&6&lOpen for");
            lines.add("&e" + (this.portalSecondsLeft - (int) ((MinecraftServer.currentTick - this.ticksSpawnedAt) / 20)) + "s");
            lines.add("&r&r");
            lines.add("&6&lPlayers");
            lines.add("&e" + this.getPlayers().size() + " &7/ " + this.getMaxPlayers());
        } else if (this.getState() == TrialState.RUNNING) {
            lines.add(color + "Tier &7(" + (this.currentTier.ordinal() + 1) + "/" + TrialTier.values().length + ")");
            lines.add("&7" + this.currentTier.getName());
            lines.add("&r&r&r");
            lines.add(color + "Room &7(#" + this.currentRoomNumber + ")");
            lines.add("&7" + (this.playersEnteredNextRoomPortal.contains(player) ? "Decision Box" : this.currentRoom.getName()));
            lines.add("&r&r&r&r");
            lines.add(color + "Time Left");

            if (this.hasMadnessModifier(MadnessModifier.TIME_GLITCH) && Math.random() < 0.99) {
                Random r = new Random();
                int length = TimeUtils.formatSeconds(this.roomSecondsLeft).length();
                StringBuilder newStr = new StringBuilder();

                for (int i = 0; i < length; i++) {
                    newStr.append((char) (r.nextInt(26) + 'a'));
                }

                lines.add("&7&k" + newStr);
            } else {
                lines.add("&7" + TimeUtils.formatSeconds(this.roomSecondsLeft));
            }

            if (this.currentRoom != null && !this.currentRoom.hasStarted()) {
                lines.add("&e&e");
                lines.add(color + "Starting in");
                lines.add("&7" + (10 - ((MinecraftServer.currentTick - this.nextRoomStartCountdownTicks) / 20)) + "s&2");
            } else if (this.pauseSecondsLeft) {
                lines.add("&b");
                lines.add(color + "" + (this.playersEnteredNextRoomPortal.contains(player) ? "Open for" : "Ending in"));
                lines.add("&7" + ((this.playersEnteredNextRoomPortal.contains(player) ? 45 : 30) - ((MinecraftServer.currentTick - this.roomCompletedAtTicks) / 20)) + "s&1");
            }

            if (this.startedDecisionSkip) {
                lines.add("&1&2&6");
                lines.add(color + "Skips");
                lines.add("&7" + this.decisionSkips.size() + " / " + (int) Math.ceil(this.players.size() / 2.0D));
            }

            if (this.currentRoom != null && !this.pauseSecondsLeft && this.currentRoom.hasStarted())
                this.currentRoom.updateScoreboard(player, lines);
        }

        //board.setAll(lines.stream().map(CommonsUtil::color).toArray(String[]::new));
    }

    private String getDateSuffix() {
        return new SimpleDateFormat("MM/dd").format(new Date());
    }

    public void addPlayer(Player player) {
        this.players.add(player);

        //DemonicTournaments.getInstance().getSavedInventoriesManager().saveInventory(player);

        this.resetPlayer(player);

        DisguiseAPI.undisguiseToAll(player);

        this.teleportPlayer(player, this.getSpawnLocation());

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);

        /*CosmicPacketHandler.sendPacketToPlayer(
                player,
                new SSetMapImagePacket(
                        "https://i.imgur.com/NqyAknj.png",
                        501,
                        501,
                        0,
                        0
                )
        );*/
    }

    public void removePlayer(Player player, TrialPlayerRemoveReason reason) {
        this.players.remove(player);

        if (reason != TrialPlayerRemoveReason.DIED_REAL) {
            player.setHealth(player.getMaxHealth());
            player.getActivePotionEffects().clear();
        }

        this.teleportPlayer(player, Bukkit.getWorlds().get(0).getSpawnLocation());

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);

        if (this.currentRoom != null) {
            this.currentRoom.onPlayerRemove(player);
        }

        if (this.players.isEmpty() && this.state != TrialState.LOADING) {
            this.end();
        }

        if (this.madnessModifiers.containsKey(MadnessModifier.PROTECT_THE_PRESIDENT)) {
            UUID presidentUUID = ((ProtectThePresidentModifier) this.madnessModifiers.get(MadnessModifier.PROTECT_THE_PRESIDENT)).getPresident();

            if (player.getUniqueId() == presidentUUID && reason != TrialPlayerRemoveReason.ACCEPTED_LOOT && reason != TrialPlayerRemoveReason.PLUGIN_DISABLE) {
                TrialSession.this.sendMessage("&6&l[Trials] &cThe president has died! Everyone has been eliminated from the trial.");

                Iterator<Player> iterator = TrialSession.this.getPlayers().iterator();

                while (iterator.hasNext()) {
                    Player p = iterator.next();
                    iterator.remove();
                    TrialSession.this.removePlayer(p, TrialPlayerRemoveReason.DIED);
                }
            }
        }

        if (reason != TrialPlayerRemoveReason.DIED_REAL) {
            this.restoreInventory(player, reason == TrialPlayerRemoveReason.PLUGIN_DISABLE);
        }
    }

    private void restoreInventory(Player player, boolean reloading) {
/*        TournamentStoredPlayer savedInventory = DemonicTournaments.getInstance().getSavedInventoriesManager().getSavedInventories().get(player.getUniqueId());

        if (savedInventory != null) {
            savedInventory.restorePlayer(player, reloading);
            DemonicTournaments.getInstance().getSavedInventoriesManager().getSavedInventories().remove(player.getUniqueId());
        }*/
    }

    private void end() {
        if (this.currentRoom != null) {
            HandlerList.unregisterAll(this.currentRoom);
        }

        List<String> tooltip = Lists.newArrayList();
        tooltip.add("&7&l&m----&e&l&m{ &6&l " + this.spawnPlayer.getName() + "'s /trial Report &e&l&m }&7&m----)");
        tooltip.add("&6&lParticipants: &f" + this.allPlayers.stream().map(OfflinePlayer::getName).collect(Collectors.joining(", ")));
        tooltip.add("");
        tooltip.add("&6&lRooms");

        for (RoomProgress stats : this.roomStats) {
            String timeTaken = stats.isDidSkip() ? "&7(Skipped)" : (stats.getSecondsTaken() == 0 ? "" : "&7(" + TimeUtils.formatSeconds(stats.getSecondsTaken()) + ")");
            tooltip.add("&e&l* " + stats.getTier().getColor() + "&l" + stats.getRoom().getName() + ": " + (stats.isCompleted() ? "&a&l✔" : "&c&l✘") + " " + timeTaken);
        }

        if (!this.madnessModifiers.isEmpty()) {
            tooltip.add("");
            tooltip.add("&4&lMadness Modifiers");

            for (AbstractMadnessModifier madnessModifier : this.madnessModifiers.values()) {
                tooltip.add("&4&l* &c" + madnessModifier.getName());

                if (madnessModifier instanceof Listener) {
                    HandlerList.unregisterAll((Listener) madnessModifier);
                }
            }
        }

        if (!this.stackableModifiers.isEmpty()) {
            if (!tooltip.get(tooltip.size() - 1).isEmpty()) tooltip.add("");
            tooltip.add("&6&lStackable Modifiers");

            for (StackableModifier stackableModifier : this.stackableModifiers.keySet()) {
                tooltip.add("&6&l* &e" + stackableModifier.getName() + " &7(" + this.stackableModifiers.get(stackableModifier) + " / " + stackableModifier.getMaxStacks() + ")");
            }
        }

        tooltip = tooltip.stream().map(CommonsUtil::color).collect(Collectors.toList());

        FancyMessage fancyMessageOne = new FancyMessage().color(ChatColor.GRAY).text(CommonsUtil.getCenteredMessage(CommonsUtil.color("&7&l&m----&e&l&m{ &6&l " + this.spawnPlayer.getName() + "'s /trial Report &7(Hover) &e&l&m }&7&m----"))).tooltip(tooltip);
        FancyMessage fancyMessageTwo = new FancyMessage().color(ChatColor.GRAY).text(CommonsUtil.getCenteredMessage(CommonsUtil.color("&6&lParticipants: &e" + this.allPlayers.size() + " &7* &6&lRooms: &e" + (int) this.roomStats.stream().filter(RoomProgress::isCompleted).count()))).tooltip(tooltip);

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendMessage("");
            fancyMessageOne.send(p);
            fancyMessageTwo.send(p);
            p.sendMessage("");
        });

        this.timeLeftTask.cancel();
        this.madnessModifierTask.cancel();

        TrialManager.removeTrial(this);
    }

    public void onRoomComplete() {
        this.currentRoom.setHasEnded(true);

        this.roomCompletedAtTicks = MinecraftServer.currentTick;
        this.pauseSecondsLeft = true;
        this.roomStats.get(this.roomStats.size() - 1).setCompleted(true);
        //this.nextRoom.reset();

        this.madnessModifiers.forEach((type, instance) -> instance.onRoomEnd());

        boolean gaveExtraTimeFromTier = false;

        if (this.currentRoomNumber % 3 == 0) {
            TrialTier tierBefore = this.currentTier;
            this.currentTier = TrialTier.values()[Math.min(TrialTier.values().length - 1, this.currentTier.ordinal() + 1)];

            if (this.currentTier != tierBefore || this.currentTier == TrialTier.DEMONIC) {
                this.roomSecondsLeft = (int) TimeUnit.MINUTES.toSeconds(this.currentTier.getSetMinutes());
                gaveExtraTimeFromTier = true;
                WorldEditHelper.pasteSchematic(this.currentTier.getSchemName(), new Location(this.getWorld(), 1000, 100, 1000));
            }
        }

        this.currentRoomNumber++;

        if (!gaveExtraTimeFromTier) {
            int tiktokStacks = this.getModifierStacks(StackableModifier.TIKTOK);
            int extraSeconds = (int) Math.floor(this.currentTier.getExtraSeconds() * (1.0 - (tiktokStacks * 0.08)));

            this.roomSecondsLeft += extraSeconds;
            this.sendMessage("&6&l[Trials] &aYou completed the room and received an additional " + extraSeconds + "s!");
        }

        Set<Location> exitPortalLocations = this.currentRoom.getExitPortalLocations();
        Set<Block> blocks = Sets.newHashSet();

        for (Location exitPortalLocation : exitPortalLocations) {
            Block portalBlockA = exitPortalLocation.getBlock();
            Block portalBlockB = exitPortalLocation.clone().add(0, 1, 0).getBlock();

            portalBlockA.setMetadata(this.NEXT_ROOM_PORTAL_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), this.getId()));
            portalBlockB.setMetadata(this.NEXT_ROOM_PORTAL_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), this.getId()));

            portalBlockA.setTypeIdAndData(90, (byte) 0, false);
            portalBlockB.setTypeIdAndData(90, (byte) 0, false);

            ParticleEffect.playEffect(ParticleEffect.WITCH_MAGIC, portalBlockA.getLocation(), 0.45f, 50);
            ParticleEffect.playEffect(ParticleEffect.PORTAL, portalBlockA.getLocation(), 0.6f, 50);
            ParticleEffect.playEffect(ParticleEffect.PORTAL, portalBlockB.getLocation(), 0.6f, 50);

            blocks.add(portalBlockA);
            blocks.add(portalBlockB);
        }

        this.roomEndTask = new BukkitRunnable() {
            private int secondsLeft = 45;

            @Override
            public void run() {
                if (TrialSession.this.players.isEmpty()) {
                    this.cancel();
                    return;
                }

                if (this.secondsLeft == (30 + 15) || this.secondsLeft == (15 + 15) || this.secondsLeft == (10 + 15) || (this.secondsLeft <= (5 + 15) && this.secondsLeft >= (1 + 15))) {
                    String color = this.secondsLeft > (3 + 15) ? "&a" : (this.secondsLeft == (3 + 15) ? "&e" : (this.secondsLeft == (2 + 15) ? "&6" : "&c"));

                    boolean isAlone = this.secondsLeft == (30 + 15) || this.secondsLeft == (15 + 15) || this.secondsLeft == (10 + 15);

                    TrialSession.this.players.forEach(p -> {
                        if (!TrialSession.this.playersEnteredNextRoomPortal.contains(p)) {
                            p.sendTitle(new Title(
                                    TrialSession.this.currentRoom.getColoredName(),
                                    CommonsUtil.color("&7Ending in... " + color + "&l" + (this.secondsLeft - 15) + "s"),
                                    isAlone ? 5 : 0,
                                    22,
                                    isAlone ? 10 : 0
                            ));
                        }
                    });
                } else if (this.secondsLeft == 15) {
                    TrialSession.this.players.forEach(p -> {
                        if (!TrialSession.this.playersEnteredNextRoomPortal.contains(p)) {
                            CommonsUtil.sendMessage(p, "&6&l[Trials] &cYou have been eliminated from the trial.");
                            TrialSession.this.removePlayer(p, TrialPlayerRemoveReason.DIED);
                        }
                    });

                    for (Block block : blocks) {
                        block.removeMetadata(TrialSession.this.NEXT_ROOM_PORTAL_KEY, DemonicTrials.getInstance());
                        block.removeMetadata(TrialSession.this.NEXT_ROOM_PORTAL_KEY, DemonicTrials.getInstance());

                        block.setType(Material.AIR);
                        block.setType(Material.AIR);
                    }

                    if (!TrialSession.this.startedPrompts) {
                        TrialSession.this.startPrompts();
                    }
                } else if (this.secondsLeft == 10) {
                    if (!TrialSession.this.startedMadnessModifierPrompt && TrialSession.this.madnessModifierBallotGui != null) {
                        TrialSession.this.startMadnessModifierPrompt();
                    }

                    if (!TrialSession.this.startedDecisionSkip) {
                        TrialSession.this.startDecisionSkip();
                    }
                } else if (this.secondsLeft == 0) {
                    TrialSession.this.startNextRoom();
                    this.cancel();
                    return;
                }

                this.secondsLeft--;
            }
        }.runTaskTimer(DemonicTrials.getInstance(), 0L, 20L);
    }

    @EventHandler
    private void on(PlayerPortalEvent event) {
        this.handlePortalTeleport(event);
    }

    @EventHandler
    private void on(PlayerMoveEvent event) {
        this.handlePortalTeleport(event);
    }

    private void handlePortalTeleport(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block metadataBlock = player.getLocation().add(0.0, 1.0, 0.0).getBlock();

        if (metadataBlock.hasMetadata(this.NEXT_ROOM_PORTAL_KEY) && metadataBlock.getType() != Material.AIR) {
            int id = metadataBlock.getMetadata(this.NEXT_ROOM_PORTAL_KEY).get(0).asInt();

            TrialManager.getTrial(player).ifPresent(trial -> {
                if (trial == this && id == this.getId()) {
                    this.teleportPlayer(player, this.getSpawnLocation());
                    this.resetPlayer(player);

                    player.getInventory().clear();
                    player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);

                    this.playersEnteredNextRoomPortal.add(player);

                    if (this.playersEnteredNextRoomPortal.size() == this.players.size() && !this.startedPrompts) {
                        this.startPrompts();
                    }
                }
            });
        }
    }

    // called after either all players go in the exit portal of a room or the time runs out
    // the minimum time here is 15s
    // the order of prompts are:
    // - deal or no deal
    // - madness modifier ballot
    // - decision skip
    private void startPrompts() {
        this.startedPrompts = true;

        Bukkit.getScheduler().runTaskLater(DemonicTrials.getInstance(), () -> {
            int currentRoomNumber = TrialSession.this.currentRoomNumber;

            if (currentRoomNumber >= 12 || currentRoomNumber == 4 || currentRoomNumber == 7 || currentRoomNumber == 10) {
                this.madnessModifierBallotGui = new MadnessModifierBallotGui(this);
            }

            this.startDealOrNoDeal();
        }, 20L);
    }

    public void onMadnessModifierBallotComplete() {
        if (!this.startedDecisionSkip) {
            this.startDecisionSkip();
        }
    }

    public void onDealOrNoDealComplete() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!TrialSession.this.startedPrompts) return;

                if (!TrialSession.this.startedMadnessModifierPrompt && (TrialSession.this.madnessModifierBallotGui != null && !TrialSession.this.madnessModifierBallotGui.getOptions().isEmpty())) {
                    TrialSession.this.startMadnessModifierPrompt();
                } else if (!TrialSession.this.startedDecisionSkip) {
                    TrialSession.this.startDecisionSkip();
                }
            }
        }.runTaskLater(DemonicTrials.getInstance(), 20L);
    }

    private void startDealOrNoDeal() {
        for (Player player : this.players) {
            new DealOrNoDealGui(this, player).getInventory().open(player);
        }
    }

    private void startMadnessModifierPrompt() {
        this.startedMadnessModifierPrompt = true;

        if (!this.madnessModifierBallotGui.getOptions().isEmpty()) {
            for (Player player : this.getPlayers()) {
                this.madnessModifierBallotGui.getInventory().open(player);
            }
        }
    }

    private void startDecisionSkip() {
        for (Player player : this.players) {
            player.getInventory().addItem(
                    this.getItemWithTrialLore(
                            CommonsUtil.createItem(
                                    Material.DOUBLE_PLANT,
                                    "&e&lDecision Box Skip Token",
                                    "&7Right-Click to vote to skip",
                                    "&7remaining time in Decision Box!"
                            )
                    )
            );
        }

        this.startedDecisionSkip = true;
        this.decisionSkips = Sets.newHashSet();

        this.sendDecisionSkipMessage();
    }

    private void sendDecisionSkipMessage() {
        int needed = (int) Math.ceil(this.players.size() / 2.0D);

        this.sendMessage("");
        this.sendMessage(CommonsUtil.getCenteredMessage("&e&l** &eDecision Box Skip Tokens Used: &e&l&n" + this.decisionSkips.size() + " / " + needed + "&e&l **"));

        if (this.decisionSkips.size() >= needed) {
            this.sendMessage(CommonsUtil.getCenteredMessage("&7Enough votes have been reached, teleporting..."));

            Set<Block> blocks = Sets.newHashSet();

            for (Location exitPortalLocation : this.currentRoom.getExitPortalLocations()) {
                blocks.add(exitPortalLocation.getBlock());
                blocks.add(exitPortalLocation.clone().add(0, 1, 0).getBlock());
            }

            for (Block block : blocks) {
                block.removeMetadata(TrialSession.this.NEXT_ROOM_PORTAL_KEY, DemonicTrials.getInstance());
                block.removeMetadata(TrialSession.this.NEXT_ROOM_PORTAL_KEY, DemonicTrials.getInstance());

                block.setType(Material.AIR);
                block.setType(Material.AIR);
            }

            this.roomEndTask.cancel();
            this.startNextRoom();
        } else {
            this.sendMessage(CommonsUtil.getCenteredMessage("&7All players will be teleported immediately to the"));
            this.sendMessage(CommonsUtil.getCenteredMessage("&7next room if " + needed + " more player" + (needed > 1 ? "s" : "") + " vote" + (needed > 1 ? "" : "s") + " to skip!"));
        }

        this.sendMessage("");
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            if (this.playersEnteredNextRoomPortal == null || !this.playersEnteredNextRoomPortal.contains(event.getPlayer())) {
                return;
            }

            Player player = event.getPlayer();

            TrialManager.getTrial(player).ifPresent(trial -> {
                if (trial != this) {
                    return;
                }

                if (event.getItem() != null && event.getItem().getType() == Material.DOUBLE_PLANT && event.getItem().hasItemMeta()) {
                    player.setItemInHand(null);
                    this.decisionSkips.add(player.getUniqueId());

                    this.sendDecisionSkipMessage();
                }
            });
        }
    }

    private void tryDeleteWorld() {
        try {
            FileUtils.deleteDirectory(new File(this.getWorld().getName()));
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to delete " + this.getWorld().getName() + " directory in `tryDeleteWorld`, retrying!");
            e.printStackTrace();

            this.tryDeleteWorld();
        }
    }

    private void resetWorld() {
        Location spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
        String worldName = this.world.getName();

        /*for (Player player : this.world.getPlayers()) {
            player.setMetadata("bypassCombatTeleport", new FixedMetadataValue(DemonicTrials.getInstance(), spawnLocation));
            player.teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        }

        for (Entity entity : this.world.getEntities()) {
            entity.remove();
        }

        if (ArkPlugins.CHUNK_GC.getArkPlugin().isRunning()) {
            ChunkGCTask.loaded_chunks.remove(worldName);
            ((ConcurrentHashMap) ReflectionUtils.getObject(ChunkGCTask.class, null, "chunks_to_unload")).remove(worldName);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload " + this.getWorld().getName());

        Bukkit.unloadWorld(this.getWorld(), false);

        WorldUtil.forceUnloadWorld(this.getWorld(), false);

        this.tryDeleteWorld();

        try {
            FileUtils.copyDirectory(new File("trial.template"), new File(worldName));
        } catch (IOException e) {
            Bukkit.getLogger().info("Failed copying world, trying again!");
            e.printStackTrace();
        }*/

        WorldCreator wc = new WorldCreator(worldName).generateStructures(false).generator("VoidGenerator");

        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);

        this.world = Bukkit.createWorld(wc);

        this.world.setFullTime(18000L);
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setGameRuleValue("doMobSpawning", "false");
        this.world.setGameRuleValue("keepInventory", "true");

        this.spawnLocation = new Location(this.world, 43, 235, 172, 180.0f, 0.0f);
    }

    public void resetRooms() {
        this.resetWorld();

        WorldEditHelper.pasteSchematic("trials_lobby", new Location(this.world, 1, 180, 129));

        //this.rooms.forEach(TrialRoom::reset);

        this.world.getEntities().stream().filter(e -> e.getType() != EntityType.PLAYER).forEach(Entity::remove);
    }

    public void start() {
        this.roomSecondsLeft = (int) TimeUnit.MINUTES.toSeconds(10L);

        final int skipRooms = this.getTrinket(TrialTrinket.SKIP_ROOMS);
        if (skipRooms > 0) {
            for (int i = 0; i < skipRooms; i++) {
                this.currentRoom = this.getNextRoom();
                this.roomStats.add(new RoomProgress(this.currentRoom, 0, true, true, this.currentTier));

                if (i != 0) {
                    this.addRandomStackableModifier();
                }

                if (this.currentRoomNumber % 3 == 0) {
                    this.currentTier = TrialTier.values()[Math.min(TrialTier.values().length - 1, this.currentTier.ordinal() + 1)];
                    this.roomSecondsLeft = (int) TimeUnit.MINUTES.toSeconds(this.currentTier.getSetMinutes());
                    this.addMadnessModifier(MadnessModifier.values()[new Random().nextInt(MadnessModifier.values().length)]);
                }

                this.currentRoomNumber++;
            }
        }

        this.allPlayers = new LinkedHashSet<>(this.players);
        this.timeLeftTask = new BukkitRunnable() {
            @Override
            public void run() {
                TrialSession.this.runtimeInSeconds++;

                if (TrialSession.this.pauseSecondsLeft) return;

                RoomProgress roomProgress = TrialSession.this.roomStats.get(TrialSession.this.roomStats.size() - 1);
                if (roomProgress != null) roomProgress.incrementSecondsTaken();

                if (TrialSession.this.roomSecondsLeft == 60 || TrialSession.this.roomSecondsLeft == 30 || TrialSession.this.roomSecondsLeft == 15 || TrialSession.this.roomSecondsLeft == 10 || (TrialSession.this.roomSecondsLeft <= 5 && TrialSession.this.roomSecondsLeft >= 1)) {
                    TrialSession.this.getPlayers().forEach(p -> {
                        p.sendTitle(new Title(TrialSession.this.currentRoom.getColoredName(), CommonsUtil.color("&7Time Left: " + TrialSession.this.getCountdownColor(TrialSession.this.roomSecondsLeft) + "&l" + (TrialSession.this.roomSecondsLeft == 60 ? "1m" : TrialSession.this.roomSecondsLeft + "s")), TrialSession.this.roomSecondsLeft < 5 ? 0 : 10, 22, TrialSession.this.roomSecondsLeft >= 5 || TrialSession.this.roomSecondsLeft == 1 ? 10 : 0));
                    });
                } else if (TrialSession.this.roomSecondsLeft <= 0) {
                    TrialSession.this.sendMessage("&6&l[Trials] &cAll players have been eliminated from this trial since the time ran out!");

                    Iterator<Player> iterator = TrialSession.this.getPlayers().iterator();

                    while (iterator.hasNext()) {
                        Player p = iterator.next();
                        iterator.remove();
                        TrialSession.this.removePlayer(p, TrialPlayerRemoveReason.TIME_RAN_OUT);
                    }

                    this.cancel();
                }

                TrialSession.this.roomSecondsLeft--;
            }
        };

        this.madnessModifierTask = new BukkitRunnable() {
            private boolean shouldReset = false;

            @Override
            public void run() {
                if (!TrialSession.this.pauseSecondsLeft) {
                    for (AbstractMadnessModifier modifier : TrialSession.this.madnessModifiers.values()) {
                        if (this.shouldReset) {
                            modifier.reset();
                        }

                        modifier.onTick();
                    }

                    if (this.shouldReset) {
                        this.shouldReset = false;
                    }
                } else {
                    this.shouldReset = true;
                }
            }
        };

        this.timeLeftTask.runTaskTimer(DemonicTrials.getInstance(), 20L, 20L);
        this.madnessModifierTask.runTaskTimer(DemonicTrials.getInstance(), 20L, 20L);

        this.setState(TrialState.RUNNING);
        this.startNextRoom();

        WorldEditHelper.pasteSchematic(this.currentTier.getSchemName(), new Location(this.getWorld(), 1000, 100, 1000));
        this.spawnLocation = new Location(this.getWorld(), 1047.5, 104, 1047.5, 180, 0);
    }

    private void startNextRoom() {
        this.startedPrompts = false;
        this.startedDecisionSkip = false;
        this.startedMadnessModifierPrompt = false;
        if (this.decisionSkips != null) this.decisionSkips.clear();

        final MadnessModifierBallotGui finalMadnessModifierBallotGui = this.madnessModifierBallotGui;
        this.madnessModifierBallotGui = null;

        if (this.playersEnteredNextRoomPortal != null) {
            this.playersEnteredNextRoomPortal.clear();
        } else {
            this.playersEnteredNextRoomPortal = Lists.newArrayList();
        }

        if (this.currentRoom != null) {
            HandlerList.unregisterAll(this.currentRoom);
        }

        // for testing purposes
        if (this.currentRoomNumber != 1 || this.currentRoom == null) {
            this.currentRoom = this.getNextRoom();
        }

        this.currentRoom.reset();

        this.roomStats.add(new RoomProgress(this.currentRoom, 0, false, false, this.currentTier));

        this.currentRoom.teleportPlayersToSpawn();

        for (Player player : this.players) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[4]);
        }

        //this.players.forEach(p -> this.teleportPlayer(p, this.currentRoom.getSpawnLocation()));

        Bukkit.getServer().getPluginManager().registerEvents(this.currentRoom, DemonicTrials.getInstance());

        this.nextRoomStartCountdownTicks = MinecraftServer.currentTick;
        this.pauseSecondsLeft = true;

        new BukkitRunnable() {
            private int secondsLeft = 10;

            @Override
            public void run() {
                if (TrialSession.this.players.isEmpty()) {
                    this.cancel();
                    return;
                }

                if (this.secondsLeft == 10 || (this.secondsLeft <= 5 && this.secondsLeft >= 1)) {
                    String color = this.secondsLeft > 3 ? "&a" : (this.secondsLeft == 3 ? "&e" : (this.secondsLeft == 2 ? "&6" : "&c"));
                    TrialSession.this.showTitle(TrialSession.this.currentRoom.getColoredName(), CommonsUtil.color("&7Starting in... " + color + "&l" + this.secondsLeft), this.secondsLeft == 10 || this.secondsLeft == 5 ? 5 : 0, 22, this.secondsLeft == 10 ? 10 : 0);

                    if (this.secondsLeft == 10) {
                        if (finalMadnessModifierBallotGui != null && !finalMadnessModifierBallotGui.getOptions().isEmpty()) {
                            Map<MadnessModifier, Integer> votes = finalMadnessModifierBallotGui.getVotes();
                            MadnessModifier highestVoted = null;
                            int highestVotes = -1;

                            for (MadnessModifier key : votes.keySet()) {
                                int v = votes.get(key);

                                if (v > highestVotes) {
                                    highestVoted = key;
                                    highestVotes = v;
                                }
                            }

                            if (highestVoted != null) {
                                TrialSession.this.sendMessage("");
                                TrialSession.this.sendMessage(CommonsUtil.getCenteredMessage("&4&l  Madness Modifier Ballot Results"));

                                for (MadnessModifier key : votes.keySet()) {
                                    String str = "&4&l* &c&l" + finalMadnessModifierBallotGui.getOptions().get(key).getName() + ": &f" + votes.get(key) + " votes";

                                    if (highestVoted == key) {
                                        str += " &a&l(Selected)";
                                    }

                                    TrialSession.this.sendMessage(CommonsUtil.getCenteredMessage(str));
                                }

                                TrialSession.this.sendMessage("");

                                TrialSession.this.addMadnessModifier(highestVoted);
                            }
                        }
                    }
                } else if (this.secondsLeft == 0) {
                    if (TrialSession.this.roomStats.size() > 1) {
                        TrialSession.this.addRandomStackableModifier();

                        if (TrialSession.this.currentRoomNumber > 10) {
                            TrialSession.this.addRandomStackableModifier();
                        }

                        if (TrialSession.this.currentRoomNumber > 11) {
                            TrialSession.this.addRandomStackableModifier();
                        }
                    }

                    TrialSession.this.showTitle(TrialSession.this.currentRoom.getColoredName(), CommonsUtil.color("&7Started!"), 0, 20, 10);
                    TrialSession.this.sendModifierMessages();
                    TrialSession.this.currentRoom.start();
                    TrialSession.this.pauseSecondsLeft = false;


                    this.cancel();
                    return;
                }

                this.secondsLeft--;
            }
        }.runTaskTimer(DemonicTrials.getInstance(), 0L, 20L);
    }

    private TrialRoom getNextRoom() {
        List<TrialRoom> roomsCopy = this.rooms.stream().filter(room -> this.getCurrentTier().ordinal() <= room.getRollUntil().ordinal()).collect(Collectors.toList());
        if (this.currentRoom != null) roomsCopy.remove(this.currentRoom);

        Collections.shuffle(roomsCopy);

        return roomsCopy.get(0);
    }

    public int getRoomSecondsLeft() {
        return this.roomSecondsLeft;
    }

    public Map<StackableModifier, Integer> getStackableModifiers() {
        return this.stackableModifiers;
    }

    public Map<MadnessModifier, AbstractMadnessModifier> getMadnessModifiers() {
        return this.madnessModifiers;
    }

    public int getMadnessModifierBallotOptions() {
        return this.madnessModifierBallotOptions;
    }

    private ChatColor getCountdownColor(int seconds) {
        switch (seconds) {
            case 5:
                return ChatColor.DARK_GREEN;
            case 4:
                return ChatColor.GREEN;
            case 3:
                return ChatColor.YELLOW;
            case 2:
                return ChatColor.GOLD;
            case 1:
                return ChatColor.RED;
            default:
                return ChatColor.GRAY;
        }
    }

    private void showTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.getPlayers().forEach(p -> p.sendTitle(new Title(title, subtitle, fadeIn, stay, fadeOut)));
    }

    public ItemStack getItemWithTrialLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : Lists.newArrayList();
        lore.add(CommonsUtil.color("&8Trial Item"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void teleportPlayer(Player p, Location location) {
        p.setMetadata("queuedTeleportPass", new FixedMetadataValue(Arkkit.get(), location));
        p.setMetadata("ignore_combat_tag", new FixedMetadataValue(Arkkit.get(), true));
        p.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        p.removeMetadata("ignore_combat_tag", Arkkit.get());

        p.closeInventory();
    }

    public void addRandomStackableModifier() {
        List<StackableModifier> possible = Arrays.stream(StackableModifier.values()).collect(Collectors.toList());

        this.stackableModifiers.forEach((k, v) -> {
            if (v >= k.getMaxStacks()) {
                possible.remove(k);
            }
        });

        Collections.shuffle(possible);

        if (!possible.isEmpty()) {
            StackableModifier modifier = possible.get(0);
            this.stackableModifiers.put(modifier, this.stackableModifiers.getOrDefault(modifier, 0) + 1);

            for (Player p : this.getPlayers()) {
                /*CosmicPacketHandler.sendPacketToPlayer(p, new SCooldownPacket(
                        "stackablemodifier" + modifier.getName().toLowerCase(),
                        "DEBUFF",
                        true,
                        CosmicPacketHandler.rgbaToInt(0, 0, 0, 0),
                        "Stackable Modifier: " + modifier.getName() + ": " + this.stackableModifiers.get(modifier),
                        0,
                        0,
                        modifier.getMaterial().getId()
                ));*/
            }
        }
    }

    public int getModifierStacks(StackableModifier modifier) {
        return this.stackableModifiers.getOrDefault(modifier, 0);
    }

    public void addMadnessModifier(MadnessModifier modifier) {
        try {
            AbstractMadnessModifier instance = modifier.getClazz().getConstructor(TrialSession.class).newInstance(this);
            this.madnessModifiers.put(modifier, instance);

            if (instance instanceof Listener) {
                Bukkit.getServer().getPluginManager().registerEvents((Listener) instance, DemonicTrials.getInstance());
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public boolean hasMadnessModifier(MadnessModifier modifier) {
        return this.madnessModifiers.containsKey(modifier);
    }

    public void sendModifierMessages() {
        for (AbstractMadnessModifier modifier : this.madnessModifiers.values()) {
            this.sendMessage("");
            this.sendMessage(CommonsUtil.getCenteredMessage("&4&lMadness Modifier: &c&l" + modifier.getName()));

            for (String s : WordUtils.wrap(modifier.getDescription(), 40).split("\n")) {
                this.sendMessage("&7" + CommonsUtil.getCenteredMessage(s));
            }

            this.sendMessage("");
        }

        this.stackableModifiers.forEach((m, v) -> {
            this.sendMessage("");
            this.sendMessage(CommonsUtil.getCenteredMessage("&6&lStackable Modifier: &e&l" + m.getName()) + " &7(" + v + "/" + m.getMaxStacks() + ")");
            this.sendMessage(CommonsUtil.getCenteredMessage("&7" + m.getDescription()));
            this.sendMessage(CommonsUtil.getCenteredMessage("&7(Max Stacks: " + m.getMaxStacks() + ")"));
            this.sendMessage("");
        });
    }

    public void resetPlayer(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setFireTicks(0);
        player.setFoodLevel(20);
        player.setFallDistance(0.0f);
        player.setHealth(player.getMaxHealth());
        player.spigot().setCollidesWithEntities(true);
        //BleedTimerTask.remove(player);
        DuelUtils.unequipEffects(player);

        if (player.hasMetadata("spectator")) player.removeMetadata("spectator", CosmicDuel.get());

        if (player.hasMetadata("divineFire")) {
            player.removeMetadata("divineFire", player.getMetadata("divineFire").get(0).getOwningPlugin());
        }

        if (player.hasMetadata("lastDivinedBy")) {
            player.removeMetadata("lastDivinedBy", player.getMetadata("lastDivinedBy").get(0).getOwningPlugin());
        }

        if (player.hasMetadata("divineFireLevel")) {
            player.removeMetadata("divineFireLevel", player.getMetadata("divineFireLevel").get(0).getOwningPlugin());
        }

        if (player.hasMetadata("golden_apple_count")) {
            player.removeMetadata("golden_apple_count", player.getMetadata("golden_apple_count").get(0).getOwningPlugin());
        }

        if (player.hasMetadata("golden_apple_time")) {
            player.removeMetadata("golden_apple_time", player.getMetadata("golden_apple_time").get(0).getOwningPlugin());
        }

        if (!VanishUtils.isVanished(player)) {
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                pl.showPlayer(player);
            }
        }

        AstroArmorSetsAPI.refreshArmorSets(player, true);


        Phoenix.last_phoenix.remove(player.getUniqueId().toString());

        /*CosmicPacketHandler.sendPacketToPlayer(player, new SCooldownPacket(
                "phoenix",
                "DEBUFF",
                false,
                CosmicPacketHandler.rgbaToInt(215, 99, 103, 255),
                "Phoenix Cooldown",
                0,
                0,
                288
        ));*/
    }

    public int getTrinket(TrialTrinket trinket) {
        Optional<Pair<ITrialTrinket, Integer>> optional = this.trinkets.stream().filter(t -> t.getValue0().equals(trinket.getInstance())).findFirst();

        if (optional.isPresent()) {
            return optional.get().getValue1();
        }

        return 0;
    }

    public enum TrialPlayerRemoveReason {
        ACCEPTED_LOOT,
        LEAVE_COMMAND,
        LOGGED_OUT,
        PLUGIN_DISABLE,
        TIME_RAN_OUT,
        DIED,
        DIED_REAL
    }
}
