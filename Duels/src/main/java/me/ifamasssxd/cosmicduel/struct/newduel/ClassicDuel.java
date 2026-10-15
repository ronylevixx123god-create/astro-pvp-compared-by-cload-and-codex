/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.VanishUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldGuardUtils
 *  com.gmail.nossr50.api.PartyAPI
 *  com.gmail.nossr50.runnables.skills.BleedTimerTask
 *  com.gmail.nossr50.util.StringUtils
 *  com.google.common.collect.Lists
 *  com.rit.sucy.EnchantmentAPI
 *  org.arkhamnetwork.Arkkit.Arkkit
 *  org.arkhamnetwork.Arkkit.patches.CombatLog
 *  org.arkhamnetwork.enchantments.EnchantmentBook
 *  org.arkhamnetwork.enchantments.net.astropvp.armorsets.listeners.PlayerPotionEffectListener
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Instrument
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Note
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.ifamasssxd.cosmicduel.struct.newduel;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.cosmicpvp.cosmicutils.utils.*;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.listeners.DuelListener;
import me.ifamasssxd.cosmicduel.listeners.SpectateInventory;

import me.ifamasssxd.cosmicduel.struct.DuelPlayer;
import me.ifamasssxd.cosmicduel.struct.Kit;
import me.ifamasssxd.cosmicduel.struct.enums.*;
import me.ifamasssxd.cosmicduel.utils.*;
import me.ifamasssxd.cosmicduel.utils.JSONMessage;
import me.ifamasssxd.cosmicduel.utils.RandomFireworks;
import net.astropvp.CollectPlugin;
import net.astropvp.core.killtracker.KillTrackerAPI;
import net.astropvp.core.utils.TitleUtil;
import net.astropvp.struct.CollectCategory;
import net.astropvp.struct.CollectType;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.arkhamnetwork.enchantments.listeners.PlayerPotionEffectListener;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;
import org.yaml.snakeyaml.events.Event;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassicDuel extends NewDuel {
    private static int id = 1;
    private boolean ending;
    private final DecimalFormat format;

    public ClassicDuel(Player initiator, List<String> pendingInvites) {
        super(initiator, pendingInvites);
        this.ending = false;
        this.format = new DecimalFormat("#.#");
        this.duelID = id++;
    }

    public String getName() {
        return "1v1";
    }

    public DuelType getDuelType() {
        return DuelType.ONE_VS_ONE;
    }

    public int getPlayersToStart() {
        return 2;
    }

    public void addPlayer(Player player, boolean checkStart) {
        this.players.add(player);
        player.setMetadata("in_duel", (MetadataValue) new FixedMetadataValue((Plugin) CosmicDuel.get(), ""));
        if (checkStart && this.players.size() >= getPlayersToStart()) {
            setCreateState(DuelCreateState.STAKES);
            this.stakeInventory = new NewStakeInventory(this, getPlayers().get(0), player);
            for (Player p : getPlayers()) {
                p.setMetadata("close_exempt", (MetadataValue) new FixedMetadataValue((Plugin) CosmicDuel.get(), ""));
                p.closeInventory();
                if (p.getUniqueId().equals(this.stakeInventory.getP1().getUniqueId())) {
                    p.openInventory(this.stakeInventory.getPlayerOneInventory());
                    continue;
                }
                p.openInventory(this.stakeInventory.getPlayerTwoInventory());
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
        player.setMetadata("in_duel", (MetadataValue) new FixedMetadataValue((Plugin) CosmicDuel.get(), ""));
        Location spawn = checkIfSpawnLoaded(false);
        forceTeleport(player, spawn);
        System.out.println("[CosmicDuels] Teleporting " + player.getName() + " to " + spawn);
        clearChat(player);
        resetPlayerStats(player);
        applySettings(player);
        removeVanish(player);
    }

    public void onStart() {
        setDuelState(DuelState.STARTING);
        SpectateInventory.updateInventory();
        for (Player p : getPlayers())
            setupPlayer(p);
        clearArena();
        for (int i = 0; i < this.players.size(); i++) {
            if (getDuelType() == DuelType.ONE_VS_ONE) {
                Player p = this.players.get(i);
                Player pl = getPlayers().get((i == getPlayers().size() - 1) ? 0 : (i + 1));
                p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "Your " + (isRanked() ? (ChatColor.YELLOW + ChatColor.BOLD.toString() + "RANKED ") : "") + ChatColor.YELLOW + "duel with " + ChatColor.YELLOW + ChatColor.UNDERLINE.toString() + pl.getName() + ChatColor.YELLOW + " will begin shortly...");
            }
        }
        final NewDuel duel = this;
        (new BukkitRunnable() {
            AtomicInteger count = new AtomicInteger(10);

            public void run() {
                if (ClassicDuel.this.getPlayers().size() < ClassicDuel.this.getPlayersToStart()) {
                    cancel();
                    DuelAPI.cancelDuel(duel, CancelReason.NOT_ENOUGH_PLAYERS);
                    return;
                }
                if (this.count.decrementAndGet() <= 0) {
                    cancel();
                    CosmicDuel.logMessage("Starting duel in arena " + ClassicDuel.this.arena.getName() + " with id " + ClassicDuel.this.duelID + " settings -> " + ClassicDuel.this.getSettings().toString());
                    ClassicDuel.this.start();
                    return;
                }
                for (Player p : ClassicDuel.this.getPlayers()) {
                    p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "The duel will start in " + ChatColor.YELLOW + ChatColor.BOLD.toString() + this.count.get() + "s.");
                    p.playNote(p.getLocation(), Instrument.PIANO, new Note(3));
                }
            }
        }).runTaskTimer((Plugin) CosmicDuel.get(), 20L, 20L);
    }

    public void onDuelEnd(EndReason endReason, Player loser) {
        this.ending = true;
        resetEnvoys();
        CosmicDuel.logMessage("Ending duel for reason: " + endReason.name() + " loser: " + ((loser != null) ? loser.getName() : "none"));
        if (endReason == EndReason.WIN) {
            if (this.players.size() >= 1) {
                Player winner = this.players.get(0);
                String winningMsg = getColoredMessage("&e&l(!) &a&n%s&e has defeated &c&n%s&e!", new Object[]{winner.getName(), loser.getName()});
                sendMessage("", true);
                JSONMessage jsonMessage = new JSONMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Inventories: ");
                sendMessage("", true);
                sendMessage(winningMsg, true);

                if (!getEndingInventoryCache().containsKey(winner.getName())) {
                    UUID uuid = CosmicDuel.get().getDuelHistoryManager().storeInventorySnapshot(winner);
                    getEndingInventoryCache().put(winner.getName(), uuid);
                }
                int index = 0;
                for (Map.Entry<String, UUID> invs : getEndingInventoryCache().entrySet()) {
                    boolean includeComma = true;
                    if (index++ == getEndingInventoryCache().size() - 1)
                        includeComma = false;
                    jsonMessage.addRunCommand((String) invs.getKey() + (includeComma ? ", " : ""), winner.getName().equals(invs.getKey()) ? ChatColor.GREEN : ChatColor.RED, "/viewdeathinventory " + ((UUID) getEndingInventoryCache().get(invs.getKey())).toString(), ChatColor.GRAY + "Click to view duel inventory.");
                }
                sendJsonMessage(jsonMessage, true);


                loser.sendMessage("");
                loser.sendMessage(winningMsg);
                jsonMessage.sendToPlayer(loser);
                winner.sendMessage("");
                winner.sendMessage(CC.translate("&e&lMatch Stats:"));
                winner.sendMessage(CC.translate("&a" + winner.getName() + " &eTotal Hits: &f" + this.hitsMap.getOrDefault(winner.getUniqueId(), 0) + "&e, Longest Combo: &f" + this.longestComboMap.getOrDefault(winner.getUniqueId(), 0) + "&e."));
                winner.sendMessage(CC.translate("&a" + winner.getName() + " &eTotal Damage: &f" + this.getDf().format(this.damageMap.getOrDefault(winner.getUniqueId(), 0.0)) + " &7(~" + this.getAverageDamage(winner.getUniqueId()) + " DMG/hit)"));
                winner.sendMessage(CC.translate("&c" + loser.getName() + " &eTotal Hits: &f" + this.hitsMap.getOrDefault(loser.getUniqueId(), 0) + "&e, Longest Combo: &f" + this.longestComboMap.getOrDefault(loser.getUniqueId(), 0) + "&e."));
                winner.sendMessage(CC.translate("&c" + loser.getName() + " &eTotal Damage: &f" + this.getDf().format(this.damageMap.getOrDefault(loser.getUniqueId(), 0.0)) + " &7(~" + this.getAverageDamage(loser.getUniqueId()) + " DMG/hit)"));
                winner.sendMessage("");

                loser.sendMessage("");
                loser.sendMessage(CC.translate("&e&lMatch Stats:"));
                loser.sendMessage(CC.translate("&c" + loser.getName() + " &eTotal Hits: &f" + this.hitsMap.getOrDefault(loser.getUniqueId(), 0) + "&e, Longest Combo: &f" + this.longestComboMap.getOrDefault(loser.getUniqueId(), 0) + "&e."));
                loser.sendMessage(CC.translate("&c" + loser.getName() + " &eTotal Damage: &f" + this.getDf().format(this.damageMap.getOrDefault(loser.getUniqueId(), 0.0)) + " &7(~" + this.getAverageDamage(loser.getUniqueId()) + " DMG/hit)"));
                loser.sendMessage(CC.translate("&a" + winner.getName() + " &eTotal Hits: &f" + this.hitsMap.getOrDefault(winner.getUniqueId(), 0) + "&e, Longest Combo: &f" + this.longestComboMap.getOrDefault(winner.getUniqueId(), 0) + "&e."));
                loser.sendMessage(CC.translate("&a" + winner.getName() + " &eTotal Damage: &f" +  this.getDf().format(this.damageMap.getOrDefault(winner.getUniqueId(), 0.0)) + " &7(~" + this.getAverageDamage(winner.getUniqueId()) + " DMG/hit)"));
                loser.sendMessage("");

                removePlayer(winner, Reason.WIN, false);
                winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1.0F, 1.4F);
                RandomFireworks.getManager().launchRandomFirework(winner.getLocation());
                awardStakes(winner, loser);
                if (this.settings.hasSetting(Setting.DEATH_CERT))
                    giveWinnerPage(winner, loser);

                Title titleWinner = new Title(CC.translate("&a&lWinner"), CC.translate("&7") + winner.getName(), 10, 40, 10);
                winner.sendTitle(titleWinner);
                Title titleLoser = new Title(CC.translate("&c&lYou Lost"), CC.translate("&7Winner: ") + winner.getName(), 10, 40, 10);
                winner.sendTitle(titleWinner);
                loser.sendTitle(titleLoser);
                Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(winner, "DUEL_WIN", this.isRanked(), loser));
                Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(loser, "DUEL_LOSE", this.isRanked(), winner));
                if (this.settings.hasSetting(Setting.DROPITEMS)) {
                    Bukkit.broadcastMessage(CC.translate("&e&l(!)&e &a&n" + winner.getName() + "&r &ehas defeated &c&n" + loser.getName() + "&e in a Risk Inventory Duel!"));
                }
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
                    ClassicDuel.this.onEnd(true);
                    return;
                }
                ClassicDuel.this.sendMessage(ClassicDuel.this.getColoredMessage("&c&l(!) &cThe duel will end in %s.", new Object[]{Integer.valueOf(this.count)}), true);
            }
        }).runTaskTimer((Plugin) CosmicDuel.get(), 20L, 20L);
    }

    public Inventory getDroppedItemsInventory(Player loser) {
        Inventory inv = Bukkit.createInventory((InventoryHolder) null, loser.getInventory().getSize() + 9, loser.getName() + "'s Death Loot");
        int index = 0;
        for (ItemStack armor : loser.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR)
                inv.setItem(index, armor);
            index++;
        }
        loser.getInventory().setArmorContents((ItemStack[]) null);
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

            ItemStack item = new ItemBuilder(Material.PAPER)
                    .name(ChatColor.WHITE + ChatColor.BOLD.toString() + loser.getName() + "'s Death Certificate")
                    .addLoreLine(ChatColor.WHITE + loser.getName() + ChatColor.GRAY + " was defeated in a " + ChatColor.WHITE + getName())
                    .addLoreLine(ChatColor.GRAY + "duel on " + ChatColor.WHITE + format.format(date) + " at " + timeFormat.format(date))
                    .addLoreLine(ChatColor.GRAY + "in the " + ChatColor.WHITE + getArena().getName() + " Arena " + ChatColor.GRAY + "by " + ChatColor.WHITE + winner.getName())
                    .addLoreLine("")
                    .addLoreLine(CC.translate("&e&lMatch Stats:"))
                    .addLoreLine(CC.translate("&a" + winner.getName() + " &eTotal Hits: &f" + this.hitsMap.getOrDefault(winner.getUniqueId(), 1) + "&e, Longest Combo: &f" + this.longestComboMap.getOrDefault(winner.getUniqueId(), 1)))
                    .addLoreLine(CC.translate("&a" + winner.getName() + " &eTotal Damage: &f" + this.getDf().format(this.hitsMap.getOrDefault(winner.getUniqueId(), 1)) + " &7(~" + getAverageDamage(winner.getUniqueId()) + " DMG/hit)"))
                    .addLoreLine(CC.translate("&c" + loser.getName() + " &eTotal Hits: &f" + this.hitsMap.getOrDefault(loser.getUniqueId(), 1) + "&e, Longest Combo: &f" + this.longestComboMap.getOrDefault(loser.getUniqueId(), 1)))
                    .addLoreLine(CC.translate("&c" + loser.getName() + " &eTotal Damage: &f" + this.getDf().format(this.hitsMap.getOrDefault(loser.getUniqueId(), 1)) + " &7(~" + getAverageDamage(loser.getUniqueId()) + " DMG/hit)"))
                    .build();
            PlayerUtil.giveOrDropProtectedItem(winner, item, 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void start() {
        this.duelState = DuelState.PLAYING;
        for (Player p : getPlayers()) {
            p.setHealth(p.getMaxHealth());
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 0.6F, 1.0F);
            p.sendMessage(getColoredMessage("&e&lFIGHT!"));

            RandomFireworks.getManager().launchRandomFirework(p.getLocation());
            p.chat("/apply");
            Bukkit.getScheduler().runTaskLater(CosmicDuel.get(), () -> {
                Blessed.blessPlayer(p, false);

            }, 20L);
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

    public void removePlayer(Player player, Reason reason, boolean createSpectator) {
        if (!this.players.contains(player))
            return;
        CosmicDuel.logMessage("Removing " + player.getName() + " from duel ID: " + this.duelID);
        this.players.remove(player);
        player.setMetadata("lastDuelEnd", (MetadataValue) new FixedMetadataValue((Plugin) CosmicDuel.get(), Integer.valueOf(MinecraftServerUtils.getCurrentServerTick())));
        player.removeMetadata("in_duel", (Plugin) CosmicDuel.get());
        if (reason != Reason.WIN)
            sendMessage(reason.getReason(player), true);
        if (!getEndingInventoryCache().containsKey(player.getName())) {
            UUID uuid = CosmicDuel.get().getDuelHistoryManager().storeInventorySnapshot(player);
            getEndingInventoryCache().put(player.getName(), uuid);
        }
        resetPlayer(player);
        removePotionEffects(player);
        if (!this.ending && createSpectator && !willDuelEnd()) {
            prepareSpectator(player);
        } else {
            returnPlayerToStartLocation(player);
        }
        returnItems(player, (reason == Reason.RELOAD));
        if (reason == Reason.WIN || reason == Reason.RELOAD || reason == Reason.NO_WINNER)
            return;
        if (getCreateState() != DuelCreateState.DONE)
            return;
        if (willDuelEnd() && isCurrentlyInDuel())
            onDuelEnd(EndReason.WIN, player);
    }

    public boolean willDuelEnd() {
        return (this.players.size() <= 1);
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
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicDuel.get(), () -> {
                CombatLog.unflagCombat(p);
                if (p.hasMetadata("cl_combat"))
                    p.removeMetadata("cl_combat", (Plugin) Arkkit.get());
            }, 5L);
        } else {
            CombatLog.unflagCombat(p);
        }
        if (this.originalLocation.containsKey(p.getUniqueId()))
            forceTeleport(p, this.originalLocation.get(p.getUniqueId()));
        this.originalLocation.remove(p.getUniqueId());
        p.setFireTicks(0);
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
        spectator.setMetadata("spectator", (MetadataValue) new FixedMetadataValue((Plugin) CosmicDuel.get(), ""));
        this.spectators.add(spectator);
        resetPlayerStats(spectator);
        for (Player pl : Bukkit.getOnlinePlayers())
            pl.hidePlayer(spectator);
        spectator.spigot().setCollidesWithEntities(false);
        if (!this.originalLocation.containsKey(spectator.getUniqueId()))
            this.originalLocation.put(spectator.getUniqueId(), spectator.getLocation());
        Title titleWinner = new Title(CC.translate("&a&lSPECTATOR"), CC.translate("&7You are now spectating the duel."), 10, 40, 10);
        spectator.sendTitle(titleWinner);
        spectator.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "You are now " + ChatColor.GREEN + ChatColor.UNDERLINE + "SPECTATING" + ChatColor.GREEN + " this duel.");
        spectator.sendMessage(ChatColor.GRAY + "You can use " + ChatColor.YELLOW + "'/duel leave'" + ChatColor.GRAY + " to stop spectating.");
        forceTeleport(spectator, getArena().getSpawnLocation(true));
        for (Player spec : getSpectators())
            spectator.hidePlayer(spec);
        spectator.setAllowFlight(true);
        spectator.setFlying(true);
    }

    public void removeSpectator(Player p) {
        p.removeMetadata("spectator", (Plugin) CosmicDuel.get());
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
        if (getPlayers().size() == 2) {
            if (p.equals(getPlayers().get(0)))
                return getPlayers().get(1);
            if (p.equals(getPlayers().get(1)))
                return getPlayers().get(0);
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
                    p.getInventory().removeItem(new ItemStack[]{is});
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
                p.getInventory().setArmorContents((ItemStack[]) null);
                p.updateInventory();
            }
            if (hadDisabledItems) {
                p.sendMessage(getColoredMessage("&c&l(!) &cYou had items that were disabled in this duel."));
                p.sendMessage(ChatColor.GRAY + "They will be returned to you once the duel is over.");
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicDuel.get(), () -> {
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
                    p.getInventory().addItem(new ItemStack[]{toAdd});
                }
                continue;
            }
            p.getInventory().addItem(new ItemStack[]{is});
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
            p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.POTION, 1, (short) 16421)});
        }
        p.updateInventory();
        if (hadDisabledItems)
            p.sendMessage(getColoredMessage("&e&l(!) &eYour kit contained some items that were &e&ndisabled&e in this duel."));
    }

    public void wipePlayer(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents((ItemStack[]) null);
        p.updateInventory();
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicDuel.get(), () -> removePotionEffects(p), 7L);
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

    public void awardStakes(Player winner, Player loser) {
        if (winner == null) {
            returnStakes();
            return;
        }


        List<ItemStack> rewards = new ArrayList<>();
        for (List<ItemStack> stakeItems : this.stakes.values()) {
            rewards.addAll(stakeItems);
        }


        if (this.settings.hasSetting(Setting.DROPITEMS) && this.kit == Kit.BYOB) {
            for (ItemStack armor : loser.getInventory().getArmorContents()) {
                if (armor != null && armor.getType() != Material.AIR)
                    rewards.add(armor);
            }

            for (ItemStack is : loser.getInventory()) {
                if (is != null && is.getType() != Material.AIR)
                    rewards.add(is);
            }

            loser.getInventory().setArmorContents(null);
            removePotionEffects(loser);
            loser.getInventory().clear();
            loser.updateInventory();

        }

        if (!rewards.isEmpty()) {
            CollectCategory category = new CollectCategory(CollectType.DUEL, rewards.toArray(new ItemStack[rewards.size() - 1]), CC.GoldB + "Duel Collect", System.currentTimeMillis());
            CollectPlugin.getInstance().getCollectManager().getOrCreate(winner.getUniqueId().toString()).add(category);
            winner.sendMessage(CC.translate("&c&l(COLLECT&c&l)&c " + loser + "'s Death Loot has been put into your &n/collect&c."));

            this.stakes.clear();
        }

        winner.updateInventory();

    }

    public void returnStakes() {
        for (Map.Entry<UUID, List<ItemStack>> item : this.stakes.entrySet()) {
            Player p = Bukkit.getPlayer(item.getKey());
            if (p != null) {
                if (!item.getValue().isEmpty()) {
                    CollectCategory category = new CollectCategory(CollectType.DUEL, item.getValue().toArray(new ItemStack[item.getValue().size() - 1]), CC.GoldB + "Duel Collect", System.currentTimeMillis());
                    CollectPlugin.getInstance().getCollectManager().getOrCreate(p.getUniqueId().toString()).add(category);
                    p.sendMessage(CC.translate("&3&l(&b&lCOLLECT&3&l)&b The Duel Loot has been put into your &f/collect&b."));

                }
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



}
