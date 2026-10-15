package net.crankedpvp.hypeboxes.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.astropvp.CollectPlugin;
import net.astropvp.struct.CollectCategory;
import net.astropvp.struct.CollectType;
import net.astropvp.struct.PlayerCollect;
import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import net.crankedpvp.hypeboxes.interfaces.ActiveInventory;
import net.crankedpvp.hypeboxes.struct.HypeBox;
import net.crankedpvp.hypeboxes.struct.HypeBoxReward;
import net.crankedpvp.hypeboxes.struct.battle.BattleInfo;
import net.crankedpvp.hypeboxes.utils.Arrow;
import net.crankedpvp.hypeboxes.utils.ItemBuilder;
import net.crankedpvp.hypeboxes.utils.LuckpermUtils;
import net.crankedpvp.hypeboxes.utils.SkullUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.text.NumberFormat;
import java.util.*;

public class HypeboxBattleInventory implements ActiveInventory {

    private final Gui hypeboxBattleInventory;
    public Gui getUI() {
        return this.hypeboxBattleInventory;
    }

    private int amountOfHypeboxes = 0;

    private final Map<String, Integer> hypeboxAmounts;
    private final Map<String, Integer> toGive;

    private final Player player;
    private final Player opponent;

    private final BattleInfo playerWins = new BattleInfo();
    private final BattleInfo opponentWins = new BattleInfo();

    private boolean given = false;

    private BukkitTask roller;

    private int count = 0;

    private boolean active = true;

    private LinkedList<HypeBoxReward> rightQueue;
    private LinkedList<HypeBoxReward> leftQueue;

    public HypeboxBattleInventory(Player player, Player opponent, Map<String, Integer> hypeboxAmounts) {
        this.player = player;
        this.opponent = opponent;
        this.amountOfHypeboxes = getAmountFromMap(hypeboxAmounts);
        this.hypeboxAmounts = hypeboxAmounts;
        this.toGive = new HashMap<>(hypeboxAmounts);
        this.hypeboxBattleInventory = new Gui(6, "Hypebox Battle Inventory");
        this.hypeboxBattleInventory.setDefaultClickAction(event -> event.setCancelled(true));
        this.hypeboxBattleInventory.setItem(Arrays.asList(0, 1, 3, 5, 7, 8, 9, 10, 16, 17, 27, 28, 29, 30, 31, 32, 33, 34, 35, 40, 49), new GuiItem(createFillerItem(DyeColor.GRAY)));
        this.hypeboxBattleInventory.setItem(Arrays.asList(4, 12, 14, 22), new GuiItem(createFillerItem(DyeColor.LIME)));

        this.hypeboxBattleInventory.setItem(Arrays.asList(11, 15), new GuiItem(new ItemBuilder(Material.HOPPER, "&dRolling...").colorize().build()));
        this.updatePlayerHead();
        this.updateOpponentHead();

        this.rollNextBox();
    }

    private String getDisplayName(Player player) {
        if(player == null) {
            return "&f&l<&6&lBot&f&l> &6Astro Bot";
        }
        return LuckpermUtils.getDisplayName(player);
    }

    private int getAmountFromMap(Map<String, Integer> hypeboxAmounts) {
        int amt = 0;
        for (String s : hypeboxAmounts.keySet()) {
            if(hypeboxAmounts.get(s) > 0) amt += hypeboxAmounts.get(s);
        }
        return amt;
    }


    /*
    private void handleBattleCompletion() {
        if (playerWins.getValue() == opponentWins.getValue()) {
            handleDraw();
        } else if (playerWins.getValue() > opponentWins.getValue()) {
            handlePlayerWin();
        } else {
            handlePlayerLoss();
        }
    }
     */

    private void handleBattleCompletion() {
        if (playerWins.getValue() == opponentWins.getValue()) {
            handleDraw();
        } else {
            handlePlayerWin();
        }
    }

    private void handleDraw() {
        Bukkit.broadcastMessage(CC.translate("&4&lHype Box Battles &8" + Arrow.arrow() + "&f" + LuckpermUtils.getDisplayName(player) + " &8(&e$" + this.playerWins.getValue() + "&8) &fhas &b&lDREW &fwith " + getDisplayName(opponent) + " &8(&e$" + this.opponentWins.getValue() + "&8) &fin a hype box battle &8(&4" + this.amountOfHypeboxes * 2 + " Hypeboxes&8)"));
        Bukkit.broadcastMessage(CC.translate("&fVisit &6buy.astropvp.net &fto purchase hype boxes! &7(/hypebox battle)"));
        toGive.forEach((lb, amt) -> player.getInventory().addItem(HypeboxesPlugin.getInstance().getHypeBoxRegister().getHypebox(lb).createHypeBox(amt)));
        if(opponent != null) {
            toGive.forEach((lb, amt) -> opponent.getInventory().addItem(HypeboxesPlugin.getInstance().getHypeBoxRegister().getHypebox(lb).createHypeBox(amt)));
        }
    }

    private void handlePlayerWin() {
        if(playerWins.getValue() > opponentWins.getValue()) {
            Bukkit.broadcastMessage(CC.translate("&4&lHype Box Battles &8" + Arrow.arrow() + "&f" + getDisplayName(player) + " &8(&e$" + this.playerWins.getValue() + "&8) &fhas &a&lWON &fagainst " + getDisplayName(opponent) + " &8(&e$" + this.opponentWins.getValue() + "&8) &fin a hype box battle &8(&4" + this.amountOfHypeboxes * 2 + " Hypeboxes&8)"));
        } else {
            Bukkit.broadcastMessage(CC.translate("&4&lHype Box Battles &8" + Arrow.arrow() + "&f" + getDisplayName(opponent) + " &8(&e$" + this.opponentWins.getValue() + "&8) &fhas &a&lWON &fagainst " + getDisplayName(player) + " &8(&e$" + this.playerWins.getValue() + "&8) &fin a hype box battle &8(&4" + this.amountOfHypeboxes * 2 + " Hypeboxes&8)"));
        }
        Player rewarding = playerWins.getValue() > opponentWins.getValue() ? player : opponent;
        if(rewarding != null) {
            PlayerCollect collect = CollectPlugin.getInstance().getCollectManager().getOrCreate(rewarding.getUniqueId().toString());
            List<ItemStack> items = new ArrayList<>();
            playerWins.getHypeBoxRewardList().forEach(item -> {
                Bukkit.broadcastMessage(CC.translate(" &f• &l" + item.getItemStack().getAmount() + "x " + item.getItemStack().getItemMeta().getDisplayName() + " &8(&e" + formatAsCurrency(item.getValue()) + "&8)"));
                //rewarding.getInventory().addItem(item.getItemStack());
                items.add(item.getItemStack());
            });
            opponentWins.getHypeBoxRewardList().forEach(item -> {
                Bukkit.broadcastMessage(CC.translate(" &f• &l" + item.getItemStack().getAmount() + "x " + item.getItemStack().getItemMeta().getDisplayName() + " &8(&e" + formatAsCurrency(item.getValue()) + "&8)"));
                //rewarding.getInventory().addItem(item.getItemStack());
                items.add(item.getItemStack());
            });
            Bukkit.broadcastMessage(CC.translate("&fVisit &6buy.astropvp.net &fto purchase hype boxes! &7(/hypebox battle)"));
            ItemStack[] arr = new ItemStack[items.size()];
            collect.add(new CollectCategory(CollectType.HYPEBOX_BATTLE, items.toArray(arr), CC.DarkRedB + "Hype Box Battle Loot", System.currentTimeMillis()));
            rewarding.sendMessage(CC.translate("&4&lCollect &8➥ &fYour Hype Box Battle rewards have been added to your &c/collect"));
        }
    }

    /*
    private void handlePlayerLoss() {
        Bukkit.broadcastMessage(CC.translate("&4&lHype Box Battles &8" + Arrow.arrow() + "&f" + LuckpermUtils.getDisplayName(player) + " &8(&e$" + this.playerWins.getValue() + "&8) &fhas &c&lLOST &fagainst " + getDisplayName(opponent) + " &8(&e$" + this.opponentWins.getValue() + "&8) &fin a hype box battle &8(&4" + this.amountOfHypeboxes * 2 + " Hypeboxes&8)"));
        playerWins.getHypeBoxRewardList().forEach(item -> Bukkit.broadcastMessage(CC.translate(" &f• &l" + item.getItemStack().getAmount() + "x " + item.getItemStack().getItemMeta().getDisplayName() + " &8(&e" + formatAsCurrency(item.getValue()) + "&8)")));
        opponentWins.getHypeBoxRewardList().forEach(item -> Bukkit.broadcastMessage(CC.translate(" &f• &l" + item.getItemStack().getAmount() + "x " + item.getItemStack().getItemMeta().getDisplayName() + " &8(&e" + formatAsCurrency(item.getValue()) + "&8)")));
        Bukkit.broadcastMessage(CC.translate("&fVisit &cbuy.crankedpvp.org &fto purchase hype boxes! &7(/hypebox battle)"));
    }

     */

    private void addPlayerItem(HypeBoxReward reward) {
        if(count <= 4) {
            this.hypeboxBattleInventory.updateItem(count + 35, new GuiItem(reward.buildWithInfo()));
        } else {
            this.hypeboxBattleInventory.updateItem(count + 40, new GuiItem(reward.buildWithInfo()));
        }
    }

    private void addOpponentItem(HypeBoxReward reward) {
        if(count <= 4) {
            this.hypeboxBattleInventory.updateItem(count + 40, new GuiItem(reward.buildWithInfo()));
        } else {
            this.hypeboxBattleInventory.updateItem(count + 45, new GuiItem(reward.buildWithInfo()));
        }
    }

    private int spins = 0;

    private void rollNextBox() {
        HypeBox currentBox = getNextBox();
        if (currentBox == null) {
            Bukkit.getScheduler().runTaskLater(HypeboxesPlugin.getInstance(), () -> {
                if(this.active) {
                    this.active = false;
                    handleBattleCompletion();
                }
            }, 20L);
            return;
        }

        updateCurrentBox(currentBox);

        rightQueue = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            rightQueue.addFirst(currentBox.pickRandomRewardAsReward());
        }

        leftQueue = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            leftQueue.addFirst(currentBox.pickRandomRewardAsReward());
        }


        roller = Bukkit.getScheduler().runTaskTimer(HypeboxesPlugin.getInstance(), () -> {
            performRoll(currentBox);
        }, 0L, 3L);
    }

    private void updateCurrentBox(HypeBox currentBox) {
        this.hypeboxBattleInventory.updateItem(13, new GuiItem(currentBox.createHypeBox(amountOfHypeboxes)));
    }

    private void performRoll(HypeBox currentBox) {
        rollRight(currentBox);
        rollLeft(currentBox);
        this.hypeboxBattleInventory.update();
        spins++;
        playRollSound();

        if (spins >= 40) {
            endRoll();
        }
    }

    private void playRollSound() {
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1f, 1f);
    }

    private void endRoll() {
        Bukkit.getScheduler().cancelTask(roller.getTaskId());

        HypeBoxReward playerReward = leftQueue.get(1);
        HypeBoxReward botReward = rightQueue.get(1);

        if(count == 8) {
            resetCount();
        }
        count++;


        playerWins.addItem(playerReward);
        addPlayerItem(playerReward);
        updatePlayerHead();

        opponentWins.addItem(botReward);
        addOpponentItem(botReward);
        updateOpponentHead();

        spins = 0;

        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);

        scheduleNextRoll();
    }

    private void resetCount() {
        count = 0;
        for (Integer i : Arrays.asList(36, 37, 38, 39, 41, 42, 43, 44, 45, 46, 47, 48, 50, 51, 52, 53)) {
            this.hypeboxBattleInventory.updateItem(i, new GuiItem(new ItemStack(Material.AIR)));
        }
    }

    private void scheduleNextRoll() {
        Bukkit.getScheduler().runTaskLater(HypeboxesPlugin.getInstance(), this::rollNextBox, 35L);
    }


    private void rollRight(HypeBox currentBox) {
        rightQueue.removeLast();
        HypeBoxReward hypeBoxReward = currentBox.pickRandomRewardAsReward();
        rightQueue.addFirst(hypeBoxReward);
        this.hypeboxBattleInventory.updateItem(23, buildWithLore(rightQueue.get(0)));
        this.hypeboxBattleInventory.updateItem(24, buildWithLore(rightQueue.get(1)));
        this.hypeboxBattleInventory.updateItem(25, buildWithLore(rightQueue.get(2)));
        this.hypeboxBattleInventory.updateItem(26, buildWithLore(rightQueue.get(3)));

    }

    private void rollLeft(HypeBox currentBox) {
        HypeBoxReward hypeBoxReward = currentBox.pickRandomRewardAsReward();
        leftQueue.removeFirst();
        leftQueue.addLast(hypeBoxReward);
        this.hypeboxBattleInventory.updateItem(18, buildWithLore(leftQueue.get(3)));
        this.hypeboxBattleInventory.updateItem(19, buildWithLore(leftQueue.get(2)));
        this.hypeboxBattleInventory.updateItem(20, buildWithLore(leftQueue.get(1)));
        this.hypeboxBattleInventory.updateItem(21, buildWithLore(leftQueue.get(0)));
    }

    private ItemStack buildWithLore(HypeBoxReward reward) {
        ItemBuilder itemBuilder = new ItemBuilder(reward.getItemStack().clone());
        itemBuilder.addLore("", "&fChance: &e" + reward.getChance() + "%", "&fValue: &e" + formatAsCurrency(reward.getValue()));
        return itemBuilder.colorize().build();
    }

    private HypeBox getNextBox() {
        HashMap<String, Integer> test = new HashMap<>(this.hypeboxAmounts);
        for (Map.Entry<String, Integer> stringIntegerEntry : test.entrySet()) {
            if(stringIntegerEntry.getValue() > 0) {
                HypeBox hypeBox = HypeboxesPlugin.getInstance().getHypeBoxRegister().getHypebox(stringIntegerEntry.getKey());
                this.hypeboxAmounts.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue() - 1);
                return hypeBox;
            } else {
                this.hypeboxAmounts.remove(stringIntegerEntry.getKey());
            }
        }
        return null;
    }

    private void updateOpponentHead() {

        ItemBuilder itemBuilder = opponent == null ? new ItemBuilder(SkullUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjJjYWYxMGQzZjc4ZDQ0MTBlYmJhZjYyZDNhZjU3NGI2ZTdlYTNkZDg0MjQ4NzEyYzJhNzdmMTM1MWYyYmFjNCJ9fX0=")) : new ItemBuilder(SkullUtils.getSkullFromPlayer(opponent));
        itemBuilder.setName(getDisplayName(opponent) + "'s Statistics");
        itemBuilder.setLore("", "&eRewards Value", " &f* &e" + formatAsCurrency(opponentWins.getValue()), "", "&eRewards Won");
        for (HypeBoxReward hypeBoxReward : opponentWins.getHypeBoxRewardList()) {
            itemBuilder.addLore(" &f* " + hypeBoxReward.getItemStack().getItemMeta().getDisplayName());
        }
        this.hypeboxBattleInventory.updateItem(1, 7, new GuiItem(itemBuilder.colorize().build()));
    }

    private void updatePlayerHead() {
        ItemBuilder itemBuilder = new ItemBuilder(SkullUtils.getSkullFromPlayer(player));
        itemBuilder.setName("&7" + LuckpermUtils.getDisplayName(player) + "'s Statistics");
        itemBuilder.setLore("", "&eRewards Value", " &f* &e$" + playerWins.getValue(), "", "&eRewards Won");
        for (HypeBoxReward hypeBoxReward : playerWins.getHypeBoxRewardList()) {
            itemBuilder.addLore(" &f* " + hypeBoxReward.getItemStack().getItemMeta().getDisplayName());
        }
        this.hypeboxBattleInventory.updateItem(1, 3, new GuiItem(itemBuilder.colorize().build()));
    }


    private ItemStack createFillerItem(DyeColor dyeColor) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, dyeColor.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(" "));
        item.setItemMeta(meta);
        return item;
    }

    public static String formatAsCurrency(double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormatter.format(amount);
    }

    @Override
    public boolean isActive() {
        return active;
    }
}