package me.astropvp.itemflip.inventory;

import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.google.common.collect.Maps;
import me.astropvp.itemflip.AstroItemFlipPlugin;
import me.astropvp.itemflip.struct.ItemFlipColor;
import me.astropvp.itemflip.struct.ItemFlipManager;
import me.astropvp.itemflip.struct.ItemFlipMatch;
import me.astropvp.itemflip.struct.ItemFlipPlayer;
import net.astropvp.CollectPlugin;
import net.astropvp.core.AstroCore;
import net.astropvp.core.cosmiccoinflip.utils.ItemBuilder;
import net.astropvp.struct.CollectCategory;
import net.astropvp.struct.CollectType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.OfflinePlayer;
import java.text.DecimalFormat;
import org.bukkit.command.CommandSender;
import me.ifamasssxd.cosmicbosses.struct.RewardInventory;

import java.util.*;

import com.google.common.collect.Lists;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import org.bukkit.inventory.ItemStack;

public class CoinFlipGui implements InventoryProvider {
    private static Map<UUID, ItemStack> defaultHeadStacks = Maps.newHashMap();
    private final AstroItemFlipPlugin plugin;
    private SmartInventory inventory;
    private ItemFlipMatch match;
    private ItemFlipColor winningColor;
    private ItemFlipColor previousColor;

    public CoinFlipGui(ItemFlipMatch match, AstroItemFlipPlugin plugin) {
        this.plugin = plugin;
        this.match = match;
        this.previousColor = match.getIfPlayerOne().getChosenColor();
        this.inventory = SmartInventory.builder().type(InventoryType.CHEST).size(1, 9).provider(this).manager(plugin.getInventoryManager()).title("Item Coin Flip Match").id(UUID.randomUUID().toString()).build();
    }

    public void init(Player player, final InventoryContents contents) {
        Player playerOne = this.match.getPlayerOne();
        Player playerTwo = this.match.getPlayerTwo();
        ItemFlipPlayer ifPlayerOne = this.match.getIfPlayerOne();
        ItemFlipPlayer ifPlayerTwo = this.match.getIfPlayerTwo();
        contents.set(0, ClickableItem.empty(this.getSkull(playerOne, playerTwo)));
        contents.set(1, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, ifPlayerOne.getChosenColor().getColor() + playerOne.getName() + "'s Color", 1, ifPlayerOne.getChosenColor().getWoolId(), new String[0])));
        contents.set(4, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&a&l5", 5, 5, new String[]{"&7Rolling in 5s"})));
        contents.set(7, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, ifPlayerTwo.getChosenColor().getColor() + playerTwo.getName() + "'s Color", 1, ifPlayerTwo.getChosenColor().getWoolId(), new String[0])));
        contents.set(8, ClickableItem.empty(this.getSkull(playerTwo, playerOne)));
        this.match.setCoinFlipState(ItemFlipMatch.MatchState.STARTING);
        (new BukkitRunnable() {
            int timer = 0;

            public void run() {
                ++this.timer;
                if (this.timer >= 5) {
                    this.cancel();
                    CoinFlipGui.this.startRoll(contents);
                } else {

                    for (Player pl : CoinFlipGui.this.match.getPlayers()) {
                        if (pl == null || !pl.isOnline()) {
                            this.cancel();
                            return;
                        }

                        pl.playSound(pl.getLocation(), Sound.NOTE_PIANO, 1.0F, 1.4F);
                    }

                    contents.set(4, ClickableItem.empty(ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, 5 - this.timer, this.timer <= 2 ? (short)DyeColor.LIME.getWoolData() : (short)(this.timer <= 4 ? DyeColor.YELLOW.getWoolData() : DyeColor.RED.getWoolData()), ChatColor.GREEN + ChatColor.BOLD.toString() + (5 - this.timer), new String[]{ChatColor.GRAY + "Rolling in " + (5 - this.timer) + "s"})));
                }
            }
        }).runTaskTimer(this.plugin, 20L, 20L);
    }

    private void startRoll(final InventoryContents contents) {
        this.match.setCoinFlipState(ItemFlipMatch.MatchState.FLIPPING);
        (new BukkitRunnable() {
            int ticks = 0;
            int maxTicks = 70 + ThreadLocalRandom.current().nextInt(2) + ThreadLocalRandom.current().nextInt(2) + ThreadLocalRandom.current().nextInt(2);

            public void run() {
                if (CoinFlipGui.this.match.isGavePrize()) {
                    this.cancel();
                } else if (this.ticks >= this.maxTicks) {
                    this.cancel();
                    Optional<ClickableItem> winner = contents.get(0, 4);
                    if (winner.isPresent() && ((ClickableItem)winner.get()).getItem().getType() == Material.WOOL) {
                        CoinFlipGui.this.winningColor = ItemFlipColor.getByWoolId(((ClickableItem)winner.get()).getItem().getDurability());
                        Player winningPlayer = CoinFlipGui.this.match.getPlayerByColor(CoinFlipGui.this.winningColor);
                        CoinFlipGui.this.endItemFlip(CoinFlipGui.RemoveReason.WON, winningPlayer);
                    }

                } else {
                    ++this.ticks;
                    boolean lastTick = this.ticks == this.maxTicks;
                    if (lastTick || this.ticks < 20 || (this.ticks < 50 || this.ticks % 2 != 0) && (this.ticks < 40 || this.ticks % 3 != 0) && (this.ticks < 30 || this.ticks % 4 != 0)) {
                        Iterator var2 = CoinFlipGui.this.match.getPlayers().iterator();

                        while(var2.hasNext()) {
                            Player player = (Player)var2.next();
                            if (player == null || !player.isOnline()) {
                                this.cancel();
                                return;
                            }

                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.1F);
                        }

                        ItemFlipColor color = lastTick ? CoinFlipGui.this.getWinnerColor() : (CoinFlipGui.this.previousColor == CoinFlipGui.this.getFirstColor() ? CoinFlipGui.this.getSecondColor() : CoinFlipGui.this.getFirstColor());
                        String winnerName = lastTick && color.equals(CoinFlipGui.this.getFirstColor()) ? CoinFlipGui.this.getFirstColor().getColor() + ChatColor.BOLD.toString() + CoinFlipGui.this.match.getPlayerOne().getName() : (lastTick && color.equals(CoinFlipGui.this.getSecondColor()) ? CoinFlipGui.this.getSecondColor().getColor() + ChatColor.BOLD.toString() + CoinFlipGui.this.match.getPlayerTwo().getName() : null);
                        CoinFlipGui.this.previousColor = color;
                        contents.set(4, ClickableItem.empty(ItemBuilder.buildItem(Material.WOOL, color.getWoolId(), winnerName != null ? winnerName : color.getColoredName(), Lists.newArrayList(new String[]{winnerName != null ? winnerName + ChatColor.GRAY + " has won the Item Flip!" : ChatColor.GRAY + "Rolling..."}))));
                    }
                }
            }
        }).runTaskTimer(this.plugin, 20L, 2L);
    }

    public void endItemFlip(CoinFlipGui.RemoveReason reason, Player forceWinner) {
        if (this.match.getCoinFlipState() != ItemFlipMatch.MatchState.DONE) {
            this.match.setCoinFlipState(ItemFlipMatch.MatchState.DONE);
            ItemFlipManager.getMatches().remove(ItemFlipManager.getMatch(forceWinner));
            if (reason == CoinFlipGui.RemoveReason.LEAVE) {
                Player winner = ThreadLocalRandom.current().nextBoolean() ? this.match.getPlayerOne() : this.match.getPlayerTwo();
                this.givePrize(winner);
            } else if (reason == CoinFlipGui.RemoveReason.WON && forceWinner != null) {
                Iterator var5 = this.match.getPlayers().iterator();

                while(var5.hasNext()) {
                    Player player = (Player)var5.next();
                    if (player != null) {
                        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + forceWinner.getName() + " has won the Item Flip with " + this.winningColor.getColoredName() + ChatColor.GREEN + "!");
                    }
                }

                this.givePrize(forceWinner);
            }

        }
    }

    private void givePrize(Player player) {
        if (!this.match.isGavePrize()) {
            this.match.setGavePrize(true);
            ItemFlipPlayer winner = this.match.getIfPlayer(player);
            ItemFlipPlayer loser = this.match.getIfPlayer(this.match.getOppositePlayer(player));
            List<ItemStack> winnings = this.match.getIfPlayerOne().getWageredItems();
            winnings.addAll(this.match.getIfPlayerTwo().getWageredItems());
            CollectPlugin.getInstance().getCollectManager().getOrCreate(winner.getPlayer().getUniqueId().toString()).add(new CollectCategory(CollectType.ITEMFLIP, winnings.toArray(new ItemStack[winnings.size() - 1]), CC.RedB + loser.getPlayer().getName() + "'s Itemflip Match", System.currentTimeMillis()));
            CommonsUtil.sendMessage(winner.getPlayer(), "&7Your items have been put in your /collect.");
            this.match.getPlayers().forEach((p) -> {
                p.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "** " + winner.getChosenColor().getColor() + "■ " + FactionUtils.getFactionTag(player) + ChatColor.GREEN + winner.getPlayer().getName() + ChatColor.GRAY + " has defeated " + ChatColor.RED + loser.getChosenColor().getColor() + "■ " + FactionUtils.getFactionTag(player) + ChatColor.RED + loser.getPlayer().getName() + ChatColor.GRAY + " in an /itemflip:");
                winnings.forEach((item) -> {
                    p.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + item.getAmount() + "x " + CommonsUtil.formatDisplayName(item));
                });
            });
        }
    }

    private ItemFlipColor getFirstColor() {
        return this.match.getIfPlayerOne().getChosenColor();
    }

    private ItemFlipColor getSecondColor() {
        return this.match.getIfPlayerTwo().getChosenColor();
    }

    private ItemFlipColor getWinnerColor() {
        return this.match.getWinnerColor();
    }

    private ItemStack getSkull(Player player, Player other) {
        ItemStack defaultSkill = (ItemStack)defaultHeadStacks.computeIfAbsent(player.getUniqueId(), (k) -> {
            return ItemBuilder.createSkull(player.getName(), (String)null, new String[]{(String)null});
        });
        ItemStack skull = defaultSkill.clone();
        ItemMeta im = skull.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + FactionUtils.getFactionTag(other) + " " +  ChatColor.BOLD.toString() + player.getName());
        im.setLore(Lists.newArrayList(new String[]{ChatColor.GREEN + "Balance: " + ChatColor.GREEN + ChatColor.BOLD + "$" + ChatColor.GREEN + (new DecimalFormat("#,###")).format(AstroCore.getInstance().getEcon().getBalance(player))}));
        skull.setItemMeta(im);
        return skull;
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }

    public static enum RemoveReason {
        LEAVE,
        WON;
    }
}
