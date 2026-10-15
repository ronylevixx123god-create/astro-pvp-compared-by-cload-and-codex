package me.ifamasssxd.cosmicduel.struct.newduel;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.cosmicpvp.cosmicutils.utils.VanishUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rit.sucy.EnchantmentAPI;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.listeners.DuelListener;
import me.ifamasssxd.cosmicduel.listeners.SpectateInventory;

import me.ifamasssxd.cosmicduel.struct.DuelPlayer;
import me.ifamasssxd.cosmicduel.struct.Kit;
import me.ifamasssxd.cosmicduel.struct.TeamData;
import me.ifamasssxd.cosmicduel.struct.enums.CancelReason;
import me.ifamasssxd.cosmicduel.struct.enums.DuelCreateState;
import me.ifamasssxd.cosmicduel.struct.enums.DuelState;
import me.ifamasssxd.cosmicduel.struct.enums.DuelType;
import me.ifamasssxd.cosmicduel.struct.enums.EndReason;
import me.ifamasssxd.cosmicduel.struct.enums.Reason;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.utils.DuelUtils;
import me.ifamasssxd.cosmicduel.utils.JSONMessage;
import me.ifamasssxd.cosmicduel.utils.KitUtils;
import me.ifamasssxd.cosmicduel.utils.RandomFireworks;
import me.ifamasssxd.cosmicduel.utils.WorldGuardUtils;
import net.astropvp.CollectPlugin;
import net.astropvp.core.killtracker.KillTrackerAPI;
import net.astropvp.struct.CollectCategory;
import net.astropvp.struct.CollectType;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.arkhamnetwork.enchantments.listeners.PlayerPotionEffectListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

public class TeamDuel extends NewDuel {
    public List<ItemStack> getContentsToDrop(TeamData data) {
        return this.contentsToDrop.get(data);
    }

    public void addToContentsToDrop(TeamData data, List<ItemStack> itemStack) {
        List<ItemStack> list = Lists.newArrayList();
        if (getContentsToDrop(data) != null) {
            for (ItemStack oldItem : getContentsToDrop(data))
                list.add(oldItem);
            this.contentsToDrop.remove(data);
        }
        for (ItemStack item : itemStack)
            list.add(item);
        this.contentsToDrop.put(data, list);
    }

    public TeamData getTeamA() {
        return this.teamA;
    }

    public TeamData getTeamB() {
        return this.teamB;
    }

    public void setTeamA(TeamData teamA) {
        this.teamA = teamA;
    }

    public void setTeamB(TeamData teamB) {
        this.teamB = teamB;
    }

    public TeamDuel(Player initiator, List<String> pendingInvites) {
        super(initiator, pendingInvites);
        this.ending = false;
        this.format = new DecimalFormat("#.#");
        this.duelID = id++;
        setTeamA(CosmicDuel.get().getTeamDataManager().tryFindTeam(initiator.getUniqueId()));
        this.contentsToDrop = Maps.newHashMap();
    }

    public TeamDuel(Player initiator, List<String> pendingInvites, boolean ranked) {
        super(initiator, pendingInvites, ranked);
        this.ending = false;
        this.format = new DecimalFormat("#.#");
        this.duelID = id++;
        this.teams.add(CosmicDuel.get().getTeamDataManager().tryFindTeam(initiator.getUniqueId()));
    }

    public String getName() {
        return "Team v Team";
    }

    public DuelType getDuelType() {
        return DuelType.TEAM_VS_TEAM;
    }

    public int getPlayersToStart() {
        return getPlayers().size();
    }

    public List<TeamData> getTeams() {
        return this.teams;
    }

    public TeamData getOppositeTeam(TeamData data) {
        return (this.teams.get(0) == data) ? this.teams.get(1) : this.teams.get(0);
    }

    public TeamData getOppositeTeam(Player player) {
        return getOppositeTeam(CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId()));
    }

    public void addPlayer(Player player, boolean checkStart) {
        this.players.add(player);
        player.setMetadata("in_duel", new FixedMetadataValue(CosmicDuel.get(), ""));
        if (CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId()) != null) {
            TeamData data = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
            player.setMetadata("in_team_duel", new FixedMetadataValue(CosmicDuel.get(), data.getLeaderUUID()));
        }
        if (checkStart && this.players.size() == getPlayersToStart()) {
            setCreateState(DuelCreateState.STAKES);
            this.stakeInventory = new NewStakeInventory(this, getTeamA(), getTeamB(), Bukkit.getPlayer(getTeamA().getLeaderUUID()), Bukkit.getPlayer(getTeamB().getLeaderUUID()));
            for (Player p : getPlayers()) {
                p.setMetadata("close_exempt", new FixedMetadataValue(CosmicDuel.get(), ""));
                p.closeInventory();
                if (CosmicDuel.get().getTeamDataManager().tryFindTeam(p.getUniqueId()) == this.stakeInventory.teamA) {
                    p.openInventory(this.stakeInventory.getTeamALeaderInventory());
                    continue;
                }
                p.openInventory(this.stakeInventory.getTeamBLeaderInventory());
            }
        }
    }

    private void removeVanish(Player player) {
        if (VanishUtils.isEssentialsVanished(player)) {
            VanishUtils.toggleEssentialsVanished(player, false);
            player.sendMessage(CC.translate("&6Your vanish has been &cremoved &6since you are in a duel."));
        }
    }

    private void setupPlayer(Player player) {
        CosmicDuel.logMessage("Preparing " + player.getName() + " for duel " + this.duelID);
        this.storedPlayers.put(player.getUniqueId(), new StoredPlayer(player));
        this.originalLocation.put(player.getUniqueId(), player.getLocation());
        player.setMetadata("in_duel", new FixedMetadataValue(CosmicDuel.get(), ""));
        clearChat(player);
        resetPlayerStats(player);
        applySettings(player);
        removeVanish(player);
    }

    public void teleportToSpawnLocation(TeamData a, TeamData b) {
        Location locationA = checkIfTeamSpawnLoaded(this.arena.getTeamASpawnLocation());
        Location locationB = checkIfTeamSpawnLoaded(this.arena.getTeamBSpawnLocation());
        List<Player> teamA = Lists.newArrayList();
        teamA.add(Bukkit.getPlayer(a.getLeaderUUID()));
        a.getTeamates().stream().forEach(teamate -> teamA.add(Bukkit.getPlayer(teamate)));
        List<Player> teamB = Lists.newArrayList();
        teamB.add(Bukkit.getPlayer(b.getLeaderUUID()));
        b.getTeamates().stream().forEach(teamate -> teamB.add(Bukkit.getPlayer(teamate)));
        teamA.stream().forEach(team -> {
            if (team.hasMetadata("in_team_duel") && team.hasMetadata("in_duel")) {
                forceTeleport(team, locationA);
                System.out.println("[CosmicDuels] Teleporting " + team.getName() + " to " + locationA);
            }
        });
        teamB.stream().forEach(team -> {
            if (team.hasMetadata("in_team_duel") && team.hasMetadata("in_duel")) {
                forceTeleport(team, locationB);
                System.out.println("[CosmicDuels] Teleporting " + team.getName() + " to " + locationB);
            }
        });
    }

    public void onStart() {
        setDuelState(DuelState.STARTING);
        SpectateInventory.updateInventory();
        for (Player p : getPlayers())
            setupPlayer(p);
        teleportToSpawnLocation(getTeamA(), getTeamB());
        clearArena();
        for (int i = 0; i < this.players.size(); i++) {
            if (getDuelType() == DuelType.TEAM_VS_TEAM) {
                Player player = this.players.get(i);
                TeamData data = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
                TeamData enemyData = null;
                if (data == this.teamA) {
                    enemyData = this.teamB;
                } else {
                    enemyData = this.teamA;
                }
                player.sendMessage(CC.translate("&e&l(!) &eYour duel against " + Bukkit.getPlayer(enemyData.getLeaderUUID()).getName() + "'s Team will begin shortly..."));
            }
        }
        final NewDuel duel = this;
        (new BukkitRunnable() {
            final AtomicInteger count = new AtomicInteger(10);

            public void run() {
                if (TeamDuel.this.getPlayers().size() < TeamDuel.this.getPlayersToStart()) {
                    cancel();
                    DuelAPI.cancelDuel(duel, CancelReason.NOT_ENOUGH_PLAYERS);
                    return;
                }
                if (this.count.decrementAndGet() <= 0) {
                    cancel();
                    CosmicDuel.logMessage("Starting team duel in arena " + TeamDuel.this.arena.getName() + " with id " + TeamDuel.this.duelID + " settings -> " + TeamDuel.this.getSettings().toString());
                    TeamDuel.this.start();
                    return;
                }
                for (Player p : TeamDuel.this.getPlayers()) {
                    p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "The duel will start in " + ChatColor.YELLOW + ChatColor.BOLD.toString() + this.count.get() + "s.");
                    p.playNote(p.getLocation(), Instrument.PIANO, new Note(3));
                }
            }
        }).runTaskTimer(CosmicDuel.get(), 20L, 20L);
    }

    private List<Player> returnTeamPlayers(Player winner, Player loser) {
        List<Player> players = Lists.newArrayList();
        players.add(winner);
        players.add(loser);
        TeamData winnerData = CosmicDuel.get().getTeamDataManager().tryFindTeam(winner.getUniqueId());
        winnerData.getTeamates().stream().forEach(teamate -> players.add(Bukkit.getPlayer(teamate)));
        TeamData loserData = CosmicDuel.get().getTeamDataManager().tryFindTeam(loser.getUniqueId());
        loserData.getTeamates().stream().forEach(teamate -> players.add(Bukkit.getPlayer(teamate)));
        return players;
    }

    private List<String> getWinnerTeamNames(Player winner) {
        List<String> winners = Lists.newArrayList();
        winners.add(winner.getName());
        TeamData winnerData = CosmicDuel.get().getTeamDataManager().tryFindTeam(winner.getUniqueId());
        winnerData.getTeamates().stream().forEach(teamate -> winners.add(Bukkit.getPlayer(teamate).getName()));
        return winners;
    }

    private void sendEndDuelMessage(String msg, Player winner, Player loser, boolean includeSpectators) {
        List<Player> players = returnTeamPlayers(winner, loser);
        for (Player player : players) {
            if (player != null)
                player.sendMessage(msg);
        }
        if (includeSpectators)
            for (Player p : this.spectators) {
                if (players.contains(p))
                    continue;
                p.sendMessage(msg);
            }
    }

    private void sendEndDuelJsonMessage(JSONMessage msg, Player winner, Player loser, boolean includeSpectators) {
        List<Player> players = returnTeamPlayers(winner, loser);
        for (Player player : players) {
            if (player != null)
                msg.sendToPlayer(player);
        }
        if (includeSpectators)
            for (Player p : this.spectators) {
                if (players.contains(p))
                    continue;
                msg.sendToPlayer(p);
            }
    }

    public void onDuelEnd(EndReason endReason, Player loser) {
        this.ending = true;
        resetEnvoys();
        CosmicDuel.logMessage("Ending team duel for reason: " + endReason.name() + " loser: " + ((loser != null) ? (Bukkit.getPlayer(CosmicDuel.get().getTeamDataManager().tryFindTeam(loser.getUniqueId()).getLeaderUUID()) + "'s Team") : "none"));
        if (endReason == EndReason.WIN) {
            if (this.players.size() >= 1) {
                Player winner = Bukkit.getPlayer(CosmicDuel.get().getTeamDataManager().tryFindTeam(this.players.get(0).getUniqueId()).getLeaderUUID());
                if (loser != null)
                    loser = Bukkit.getPlayer(CosmicDuel.get().getTeamDataManager().tryFindTeam(loser.getUniqueId()).getLeaderUUID());
                String winningMsg = CC.translate("&e&l(!) &a&n" + winner.getName() + "'s&e team has defeated &c" + loser.getName() + "'s&e team!");
                for (Player player : returnTeamPlayers(winner, loser)) {
                    if (!getEndingInventoryCache().containsValue(player)) {
                        UUID uuid = CosmicDuel.get().getDuelHistoryManager().storeInventorySnapshot(player);
                        getEndingInventoryCache().put(player.getName(), uuid);
                    }
                }
                JSONMessage jsonMessage = new JSONMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Inventories: ");
                int index = 0;
                for (Map.Entry<String, UUID> invs : getEndingInventoryCache().entrySet()) {
                    boolean includeComma = true;
                    if (index++ == getEndingInventoryCache().size() - 1)
                        includeComma = false;
                    jsonMessage.addRunCommand(invs.getKey() + (includeComma ? ", " : ""), getWinnerTeamNames(winner).contains(invs.getKey()) ? ChatColor.GREEN : ChatColor.RED, "/viewdeathinventory " + getEndingInventoryCache().get(invs.getKey()).toString(), ChatColor.GRAY + "Click to view duel inventory.");
                }
                removePlayer(winner, Reason.WIN, false);
                sendEndDuelMessage("", winner, loser, true);
                sendEndDuelMessage(winningMsg, winner, loser, true);
                sendEndDuelJsonMessage(jsonMessage, winner, loser, true);
                sendEndDuelMessage("", winner, loser, true);
                winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1.0F, 1.4F);
                RandomFireworks.getManager().launchRandomFirework(winner.getLocation());
                for (UUID uuid : CosmicDuel.get().getTeamDataManager().tryFindTeam(winner.getUniqueId()).getTeamates()) {
                    removePlayer(Bukkit.getPlayer(uuid), Reason.WIN, false);
                    Bukkit.getPlayer(uuid).playSound(Bukkit.getPlayer(uuid).getLocation(), Sound.LEVEL_UP, 1.0F, 1.4F);
                    RandomFireworks.getManager().launchRandomFirework(Bukkit.getPlayer(uuid).getLocation());
                }
                awardStakes(winner);
                if (this.settings.hasSetting(Setting.DEATH_CERT))
                    giveWinnerPage(winner, loser);
                if (this.settings.hasSetting(Setting.DROPITEMS) && this.kit == Kit.BYOB) {
                    CosmicDuel.logMessage("Dropping all of " + loser.getName() + "'s team items to " + winner.getName());
                    TeamData loserData = CosmicDuel.get().getTeamDataManager().tryFindTeam(loser.getUniqueId());
                    List<ItemStack> items = getContentsToDrop(loserData);
                    if (items != null) {
                        //CollectionBin.addItems(winner, items);
                        CollectCategory category = new CollectCategory(CollectType.DUEL, items.toArray(new ItemStack[items.size() - 1]), CC.GoldB + "Duel Collect", System.currentTimeMillis());
                        CollectPlugin.getInstance().getCollectManager().getOrCreate(winner.getUniqueId().toString()).add(category);
                        winner.sendMessage("");
                        winner.sendMessage(CC.translate("&3&l(&b&lCOLLECT&3&l)&b The Duel Loot has been put into your &f/collect&b."));
                        winner.sendMessage("");
                    }
                }

                Title titleWinner = new Title(CC.translate("&a&lWinner"), CC.translate("&7") + winner.getName(), 10, 40, 10);
                winner.sendTitle(titleWinner);
                Title titleLoser = new Title(CC.translate("&c&lYou Lost"), CC.translate("&7Winner: ") + winner.getName(), 10, 40, 10);
                winner.sendTitle(titleWinner);
                loser.sendTitle(titleLoser);
            }
        } else if (endReason == EndReason.RELOAD || endReason == EndReason.NO_WINNER) {
            onEnd(false);
            returnStakes();
            DuelAPI.cancelDuel(this, CancelReason.RELOAD);
            return;
        }
        this.duelState = DuelState.ENDING;
        (new BukkitRunnable() {
            int count = 5;

            public void run() {
                int count = this.count - 1;
                this.count = count;
                if (count <= 0) {
                    cancel();
                    TeamDuel.this.onEnd(true);
                    return;
                }
                TeamDuel.this.sendMessage(TeamDuel.this.getColoredMessage("&c&l(!) &cThe duel will end in %s.", this.count), true);
            }
        }).runTaskTimer(CosmicDuel.get(), 20L, 20L);
    }

    public Inventory getDroppedItemsInventory(Player loser) {
        Inventory inv = Bukkit.createInventory(null, loser.getInventory().getSize() + 9, loser.getName() + "'s Death Loot");
        int index = 0;
        for (ItemStack armor : loser.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR)
                inv.setItem(index, armor);
            index++;
        }
        loser.getInventory().setArmorContents(null);
        removePotionEffects(loser);
        index = 9;
        for (ListIterator<ItemStack> listIterator = loser.getInventory().iterator(); listIterator.hasNext(); ) {
            ItemStack is = listIterator.next();
            if (is != null && is.getType() != Material.AIR)
                inv.setItem(index, is);
            index++;
        }
        loser.getInventory().clear();
        loser.updateInventory();
        return inv;
    }

    public void giveWinnerPage(Player winner, Player loser) {
        if (winner == null || loser == null) {
            Bukkit.getLogger().info("Null player for giveWinnerPage() FeelsBadMan");
            return;
        }
        Date date = new Date();
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE MM/dd/yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
            String extraKitName = null;
            ItemStack is = getWinnerPage(winner, loser, date, format, timeFormat);
            PlayerUtil.giveOrDropProtectedItem(winner, is, 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ItemStack getWinnerPage(Player winner, Player loser, Date date, SimpleDateFormat format, SimpleDateFormat timeFormat) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&f&l" + loser.getName() + " Team's Death Certificate"));
        List<String> lore = Lists.newArrayList();
        lore.add(CC.translate("&f" + loser.getName() + "'s team &7was defeated in a &fTeam v Team"));
        lore.add(CC.translate("&7duel on &f" + format.format(date) + " &7at &f" + timeFormat.format(date)));
        lore.add(CC.translate("&7in the " + getArena().getName() + " Arena &7by &f" + winner.getName() + "'s team&7."));
        lore.add(CC.translate(""));
        TeamData winnerTeam = CosmicDuel.get().getTeamDataManager().tryFindTeam(winner.getUniqueId());
        TeamData loserTeam = CosmicDuel.get().getTeamDataManager().tryFindTeam(loser.getUniqueId());
        lore.add(CC.translate("&a&l" + winner.getName() + "'s Team"));
        winnerTeam.getTeamates().stream().forEach(teamate -> lore.add(CC.translate("&f&l* &a" + Bukkit.getPlayer(teamate).getName())));
        lore.add(CC.translate(""));
        lore.add(CC.translate("&c&l" + loser.getName() + "'s Team"));
        loserTeam.getTeamates().stream().forEach(teamate -> lore.add(CC.translate("&f&l* &c" + Bukkit.getPlayer(teamate).getName())));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private void awardBounty(Player winner, Player loser) {
    }


    private void clearArena() {
        for (Entity e : getArena().getSpawnLocation(true).getWorld().getEntities()) {
            if (e instanceof Player)
                continue;
            if (!WorldGuardUtils.getRegionName(e.getLocation()).toLowerCase().startsWith(getArena().getRegion().toLowerCase()))
                continue;
            e.remove();
        }
    }


    private void clearChat(Player p) {
        for (int i = 0; i < 35; i++)
            p.sendMessage("");
    }

    private Location checkIfSpawnLoaded(boolean random) {
        Location retr = getArena().getSpawnLocation(random);
        if (retr.getChunk() != null && !retr.getChunk().isLoaded())
            retr.getChunk().load();
        return retr;
    }

    private Location checkIfTeamSpawnLoaded(Location location) {
        if (location.getChunk() != null && !location.getChunk().isLoaded())
            location.getChunk().load();
        return location;
    }

    public void start() {
        this.duelState = DuelState.PLAYING;
        for (Player p : getPlayers()) {
            p.setHealth(p.getMaxHealth());
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 0.6F, 1.0F);
            p.sendMessage(getColoredMessage("&e&lFIGHT!"));
            RandomFireworks.getManager().launchRandomFirework(p.getLocation());
            p.chat("/apply");
            Blessed.blessPlayer(p, true);
        }
    }

    public void onEnd(boolean cancelDuel) {
        this.duelState = DuelState.ENDED;
        for (Object pl1 : Lists.newArrayList(getPlayers())) {
            Player pl = (Player) pl1;
            removePlayer(pl, Reason.WIN, false);
        }
        clearSpectators();
        if (cancelDuel)
            DuelAPI.cancelDuel(this, CancelReason.DUEL_END);
    }

    public int getTeamCount(Player player) {
        TeamData teamData = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
        int teamCount = 0;
        for (Player duelPlayer : this.players) {
            if (CosmicDuel.get().getTeamDataManager().tryFindTeam(duelPlayer.getUniqueId()) == teamData)
                teamCount++;
        }
        return teamCount;
    }

    public void removePlayer(Player player, Reason reason, boolean createSpectator) {
        if (!this.players.contains(player))
            return;
        CosmicDuel.logMessage("Removing " + player.getName() + " from duel ID: " + this.duelID);
        this.players.remove(player);
        player.setMetadata("lastDuelEnd", new FixedMetadataValue(CosmicDuel.get(), Integer.valueOf(MinecraftServerUtils.getCurrentServerTick())));
        player.removeMetadata("in_duel", CosmicDuel.get());
        player.removeMetadata("in_team_duel", CosmicDuel.get());
        if (reason != Reason.WIN)
            sendMessage(reason.getReason(player), true);
        if (!getEndingInventoryCache().containsKey(player.getName())) {
            UUID uuid = CosmicDuel.get().getDuelHistoryManager().storeInventorySnapshot(player);
            getEndingInventoryCache().put(player.getName(), uuid);
        }
        resetPlayer(player);
        removePotionEffects(player);
        if (!this.ending && createSpectator && !willDuelEnd(player)) {
            prepareSpectator(player);
        } else {
            returnPlayerToStartLocation(player);
        }
        returnItems(player, (reason == Reason.RELOAD));
        if (reason == Reason.WIN || reason == Reason.RELOAD || reason == Reason.NO_WINNER)
            return;
        if (this.settings.hasSetting(Setting.DROPITEMS) && this.kit == Kit.BYOB) {
            TeamData data = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
            List<ItemStack> items = Lists.newArrayList();
            for (ItemStack armor : player.getInventory().getArmorContents()) {
                if (armor != null)
                    items.add(armor);
            }
            for (ItemStack inventoryitem : player.getInventory().getContents()) {
                if (inventoryitem != null)
                    items.add(inventoryitem);
            }
            addToContentsToDrop(data, items);
            resetInventory(player);
        }
        if (getCreateState() != DuelCreateState.DONE)
            return;
        if (willDuelEnd(player) && isCurrentlyInDuel())
            onDuelEnd(EndReason.WIN, player);
    }

    public boolean willDuelEnd(Player player) {
        return (getTeamCount(player) <= 0);
    }

    private void resetInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setItemOnCursor(null);
    }

    public void resetPlayerStats(Player p) {
        p.setAllowFlight(false);
        p.setFlying(false);
        p.setFireTicks(0);
        p.setFoodLevel(20);
        p.setFallDistance(0.0F);
        p.setHealth(p.getMaxHealth());
        p.spigot().setCollidesWithEntities(true);
        if (!VanishUtils.isVanished(p))
            for (Player pl : Bukkit.getOnlinePlayers())
                pl.showPlayer(p);
    }

    public void returnItems(Player p, boolean reloading) {
        if (!this.storedPlayers.containsKey(p.getUniqueId()))
            return;
        StoredPlayer player = this.storedPlayers.get(p.getUniqueId());
        player.restorePlayer(p, reloading);
        this.storedPlayers.remove(p.getUniqueId());
    }

    public void returnPlayerToStartLocation(Player p) {
        if (!DuelAPI.isRebooting() && MinecraftServerUtils.isRunning()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicDuel.get(), () -> {
                CombatLog.unflagCombat(p);
                if (p.hasMetadata("cl_combat"))
                    p.removeMetadata("cl_combat", Arkkit.get());
            }, 5L);
        } else {
            CombatLog.unflagCombat(p);
        }
        if (this.originalLocation.containsKey(p.getUniqueId()))
            forceTeleport(p, this.originalLocation.get(p.getUniqueId()));
        this.originalLocation.remove(p.getUniqueId());
    }



    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Player> getSpectators() {
        return this.spectators;
    }

    public void prepareSpectator(Player spectator) {
        if (getDuelState() == DuelState.ENDING || getDuelState() == DuelState.IN_QUEUE)
            return;
        if (this.spectators.contains(spectator))
            return;
        spectator.setMetadata("spectator", new FixedMetadataValue(CosmicDuel.get(), ""));
        this.spectators.add(spectator);
        resetPlayerStats(spectator);
        for (Player pl : Bukkit.getOnlinePlayers())
            pl.hidePlayer(spectator);
        spectator.spigot().setCollidesWithEntities(false);
        if (!this.originalLocation.containsKey(spectator.getUniqueId()))
            this.originalLocation.put(spectator.getUniqueId(), spectator.getLocation());

        Title specTitle = new Title(CC.translate("&a&lSPECTATOR"), CC.translate("&eYou are now spectating the duel."), 10, 40, 10);
        spectator.sendTitle(specTitle);

        spectator.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "You are now " + ChatColor.GREEN + ChatColor.UNDERLINE + "SPECTATING" + ChatColor.GREEN + " this duel.");
        spectator.sendMessage(ChatColor.GRAY + "You can use " + ChatColor.YELLOW + "'/duel leave'" + ChatColor.GRAY + " to stop spectating.");
        forceTeleportToSpectator(spectator, this.players.get(0).getLocation());
        for (Player spec : getSpectators())
            spectator.hidePlayer(spec);
        spectator.setAllowFlight(true);
        spectator.setFlying(true);
        spectator.setHealth(spectator.getHealth());
    }

    public void removeSpectator(Player p) {
        p.removeMetadata("spectator", CosmicDuel.get());
        if (!this.spectators.contains(p))
            return;
        this.spectators.remove(p);
        resetPlayer(p);
        returnPlayerToStartLocation(p);
        if (p.hasPotionEffect(PotionEffectType.POISON))
            p.removePotionEffect(PotionEffectType.POISON);
    }

    public void clearSpectators() {
        for (Player pl : getSpectators())
            removeSpectator(pl);
    }

    public void resetPlayer(Player p) {
        resetPlayerStats(p);
    }

    public boolean isCurrentlyInDuel() {
        return (getDuelState().isValid() && getCreateState() == DuelCreateState.DONE);
    }

    public Player getOppositePlayer(Player p) {
        for (TeamData data : this.teams) {
            if (data.getTeamates().contains(p.getUniqueId()))
                continue;
            Player opposite = Bukkit.getPlayer(data.getLeaderUUID());
            if (!opposite.isOnline() || !this.players.contains(opposite)) {
                for (Player player : this.players) {
                    if (data.getTeamates().contains(player.getUniqueId()))
                        return player;
                }
                return null;
            }
            return opposite;
        }
        return null;
    }

    public void applySettings(Player p) {

        if (this.kit != Kit.BYOB) {
            wipePlayer(p);
            applyKit(p);
        } else {
            boolean hadDisabledItems = false;
            for (ItemStack is : p.getInventory()) {
                if (is != null && is.getType() != Material.AIR && (isDisabledDuelItem(is) || !isValidItem(is))) {
                    p.getInventory().removeItem(is);
                    hadDisabledItems = true;
                }
            }
            p.updateInventory();
            if (!this.settings.hasSetting(Setting.ARMOR)) {
                for (ItemStack armor : p.getInventory().getArmorContents()) {
                    if (armor != null && armor.getType() != Material.AIR) {
                        CosmicDuel.logMessage("Removing armor for duel " + p.getName());
                        p.sendMessage(ChatColor.RED + "Your armor has been stored and will be returned after the duel ends.");
                        break;
                    }
                }
                p.getInventory().setArmorContents(null);
                p.updateInventory();
            }
            if (hadDisabledItems) {
                p.sendMessage(getColoredMessage("&c&l(!) &cYou had items that were disabled in this duel."));
                p.sendMessage(ChatColor.GRAY + "They will be returned to you once the duel is over.");
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicDuel.get(), () -> {
            if (p.isValid())
                PlayerPotionEffectListener.rescanPlayerArmor(p);
        }, 4L);
    }

    public void applyKit(Player p) {
        List<ItemStack> list;
        DuelPlayer duelPlayer = DuelPlayer.getDuelPlayer(p);

        list = getKit().getItems();

        boolean hadDisabledItems = false;
        int potionsToAdd = 0;
        for (ItemStack is : list) {
            if (isDisabledDuelItem(is)) {
                potionsToAdd++;
                hadDisabledItems = true;
                continue;
            }
            if (isRanked() && EnchantmentAPI.getEnchantments(is).size() > 0)
                is = EnchantmentBook.applyTransmogScroll(is.clone());
            is = KitUtils.addDuelWatermark(is.clone());
            if ((is.getType() == Material.POTION || is.getType() == Material.MUSHROOM_SOUP) && is.getAmount() > 1) {
                while (is.getAmount() > 0) {
                    ItemStack toAdd = is.clone();
                    toAdd.setAmount(1);
                    is.setAmount(is.getAmount() - 1);
                    p.getInventory().addItem(toAdd);
                }
                continue;
            }
            p.getInventory().addItem(is);
        }
        if (isRanked()) {
            ItemStack potion = new ItemStack(Material.POTION, 1, (short) 16421);
            potion = KitUtils.addDuelWatermark(potion);
            for (int i = 0; i < p.getInventory().getSize(); i++) {
                if (p.getInventory().getItem(i) == null || p.getInventory().getItem(i).getType() == Material.AIR)
                    p.getInventory().setItem(i, potion);
            }
        }
        for (int j = 0; j < potionsToAdd; j++) {
            p.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 16421));
        }
        p.updateInventory();
        if (hadDisabledItems)
            p.sendMessage(getColoredMessage("&e&l(!) &eYour kit contained some items that were &e&ndisabled&e in this duel."));
    }

    @Override
    public void awardStakes(Player var1, Player var2) {

    }

    public void wipePlayer(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.updateInventory();
        Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicDuel.get(), () -> removePotionEffects(p), 7L);
    }

    public void removePotionEffects(Player p) {
        for (PotionEffect pe : p.getActivePotionEffects())
            p.removePotionEffect(pe.getType());
    }

    public boolean isDisabledDuelItem(ItemStack is) {
        if (KillTrackerAPI.isSoulGem(is))
            return !getSettings().hasSetting(Setting.SOUL_GEM);
        if (is.getType() == Material.SKULL_ITEM) {
            NBTWrapper wrapper = new NBTWrapper(is);
            if (wrapper.getCompoundOrCreate("cosmicData").hasKey("petType"))
                return !getSettings().hasSetting(Setting.PET);
        }
        if (DuelUtils.isArmor(is))
            return !getSettings().hasSetting(Setting.ARMOR);
        if (DuelUtils.isWeapon(is) && is.getType() != Material.BOW)
            return !getSettings().hasSetting(Setting.WEAPONS);
        switch (is.getType()) {
            case GOLDEN_APPLE:
                return !getSettings().hasSetting(Setting.GOLDEN_APPLES);
            case ENDER_PEARL:
                return !getSettings().hasSetting(Setting.ENDER_PEARLS);
            case BOW:
            case ARROW:
                return !getSettings().hasSetting(Setting.BOWS);
            case POTION:
                return !getSettings().hasSetting(Setting.POTIONS);
            case COOKED_BEEF:
            case GOLDEN_CARROT:
                return !getSettings().hasSetting(Setting.FOOD_LOSS);
        }
        return false;
    }

    private boolean isValidItem(ItemStack is) {
        return (DuelUtils.isArmor(is) || DuelUtils.isWeapon(is) || is.getType() == Material.POTION || is.getType() == Material.SKULL_ITEM || is.getType() == Material.GOLDEN_APPLE || is.getType() == Material.EMERALD || is.getType() == Material.MUSHROOM_SOUP || is.getType() == Material.ARROW || is.getType() == Material.ENDER_PEARL);
    }

    public void awardStakes(Player winner) {
        if (winner == null) {
            returnStakes();
            return;
        }
        List<ItemStack> rewards = new ArrayList<>();
        for (List<ItemStack> itemStackList : this.stakes.values()) {
            rewards.addAll(itemStackList);
        }
        CollectCategory category = new CollectCategory(CollectType.DUEL, rewards.toArray(new ItemStack[rewards.size() - 1]), CC.GoldB + "Duel Collect", System.currentTimeMillis());
        CollectPlugin.getInstance().getCollectManager().getOrCreate(winner.getUniqueId().toString()).add(category);
        winner.sendMessage("");
        winner.sendMessage(CC.translate("&3&l(&b&lCOLLECT&3&l)&b The Duel Loot has been put into your &f/collect&b."));
        winner.sendMessage("");
        this.stakes.clear();
        winner.updateInventory();
    }

    public void returnStakes() {
        for (Map.Entry<UUID, List<ItemStack>> item : this.stakes.entrySet()) {
            Player p = Bukkit.getPlayer(item.getKey());
            if (p != null) {
                CollectCategory category = new CollectCategory(CollectType.DUEL, item.getValue().toArray(new ItemStack[item.getValue().size() - 1]), CC.GoldB + "Duel Collect", System.currentTimeMillis());
                CollectPlugin.getInstance().getCollectManager().getOrCreate(p.getUniqueId().toString()).add(category);
                p.sendMessage("");
                p.sendMessage(CC.translate("&3&l(&b&lCOLLECT&3&l)&b The Duel Loot has been put into your &f/collect&b."));
                p.sendMessage("");
            }
        }
        this.stakes.clear();
    }

    public void resetEnvoys() {
        for (Location l : this.envoys) {
            CosmicSpaceChestsPlugin.chest_locations.remove(l);
            if (l.getBlock().getType() == Material.CHEST)
                DuelListener.removeChest(l);
        }
        this.envoys.clear();
    }

    public void onConfirmedQueue() {
        setCreateState(DuelCreateState.DONE);
        onStart();
    }


    private static int id = 1;

    private boolean ending;

    private final DecimalFormat format;

    private Map<TeamData, List<ItemStack>> contentsToDrop;

    private TeamData teamA;

    private TeamData teamB;
}
