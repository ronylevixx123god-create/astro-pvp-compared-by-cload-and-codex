package net.crankedpvp.hypeboxes.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import net.crankedpvp.hypeboxes.language.Language;
import net.crankedpvp.hypeboxes.struct.HypeBox;
import net.crankedpvp.hypeboxes.struct.HypeBoxManager;
import net.crankedpvp.hypeboxes.struct.battle.BattleQueueManager;
import net.crankedpvp.hypeboxes.utils.Clickable;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HypeboxBattleCreateInventory {

    private final Gui preBattleInv;

    public Gui getUI() {
        return this.preBattleInv;
    }

    private final Map<String, Integer> hypeboxAmounts = new HashMap<>();

    private boolean isSpinning = false;

    private final Player player;

    public HypeboxBattleCreateInventory(Player player) {

        this.player = player;

        this.preBattleInv = new Gui(3, "Create Battle");
        this.preBattleInv.setDefaultTopClickAction(event -> event.setCancelled(true));

        this.preBattleInv.setItem(2, 2, new GuiItem(buildBotItem(), event -> {
            playAgainstBot();
        }));
        this.preBattleInv.setItem(2, 8, new GuiItem(buildPlayerItem(), event -> {
            addPlayerToQueue(player);
        }));

        this.preBattleInv.setDefaultClickAction(event -> {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                if(HypeBoxManager.isHypebox(clickedItem)) {
                    HypeBox hypeBox = HypeBoxManager.getHypeBoxFromItemStack(clickedItem);
                    int amount = clickedItem.getAmount();
                    event.setCurrentItem(null);
                    addToAmount(hypeBox, amount);
                    updateSlot();
                } else {
                    event.setCancelled(true);
                }
            }
        });

        this.preBattleInv.setItem(Arrays.asList(1, 11, 19, 7, 15, 25), new GuiItem(createFillerItem(DyeColor.RED)));
        this.preBattleInv.setItem(2, 5, new GuiItem(createEmptySlot(), event -> {
            //tryOpenHypeboxBattle(player);
        }));

        this.preBattleInv.setCloseGuiAction(event -> {
            if(!isSpinning) {
                hypeboxAmounts.forEach((hb, amt) -> {
                    ItemStack itemStack = HypeboxesPlugin.getInstance().getHypeBoxRegister().getHypebox(hb).createHypeBox(amt);
                    player.getInventory().addItem(itemStack);
                    player.sendMessage(CC.translate("&aYour hype boxes have been refunded"));
                });
            }
        });

    }

    private void addPlayerToQueue(Player player) {
        if(!canBattle(player)) return;
        if(BattleQueueManager.getBattleQueue().containsKey(player)) {
            player.sendMessage(CC.translate("&cYou already have a battle pending"));
            return;
        }
        isSpinning = true;
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
        player.sendMessage(CC.translate(Language.PREFIX.getMessage() + " &fYour hypebox battle has been added to the &c'/hb battle' queue"));
        BattleQueueManager.getBattleQueue().put(player, this.hypeboxAmounts);
    }

    private void playAgainstBot() {
        if(!canBattle(player)) return;
        isSpinning = true;
        player.closeInventory();
        startBattleAgainstBot(player);
    }

    private ItemStack buildBotItem() {
        ItemBuilder itemBuilder = new ItemBuilder(SkullUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjJjYWYxMGQzZjc4ZDQ0MTBlYmJhZjYyZDNhZjU3NGI2ZTdlYTNkZDg0MjQ4NzEyYzJhNzdmMTM1MWYyYmFjNCJ9fX0="));
        itemBuilder.setName("&6&lBattle Astro Bot?");
        itemBuilder.addLore("&7&oEnter a fair battle against the",
                "&7&oserver, led by the &6&oAstro Bot",
                "",
                "&7&oIt won't let you win, but it isn't",
                "&7&obias either, robots aren't that smart yet",
                "",
                "&6&l(!) &6Click to battle the Astro Bot");
        return itemBuilder.colorize().build();
    }

    private ItemStack buildPlayerItem() {
        ItemBuilder itemBuilder = new ItemBuilder(SkullUtils.getSkullFromPlayer(Bukkit.getOfflinePlayer("KingSavaqe")));
        itemBuilder.setName("&4&lBattle Another Player?");
        itemBuilder.addLore("&7&oEnter a &f&o50/50 &7&obattle against",
                "&7&oanother &4&oplayer&7&o, winner takes all!",
                "",
                "&7&oTake someones &c&oloot&7&o.. or they might take",
                "&7&oyours.. either way, it's a bit of &a&ofun",
                "",
                "&4&l(!) &4Click to join the queue");
        return itemBuilder.colorize().build();
    }

    private boolean canBattle(Player player) {
        int amt = 0;
        for (String s : this.hypeboxAmounts.keySet()) {
            if(this.hypeboxAmounts.get(s) > 0) amt += this.hypeboxAmounts.get(s);
        }
        if(amt < 3) {
            player.sendMessage(CC.translate("&cYou must open a minimum of 3 hypeboxes at once"));
            player.closeInventory();
            return false;
        }
        return true;
    }

    private void tryOpenHypeboxBattle(Player player) {
        int amt = 0;
        for (String s : this.hypeboxAmounts.keySet()) {
            if(this.hypeboxAmounts.get(s) > 0) amt += this.hypeboxAmounts.get(s);
        }
        if(amt < 3) {
            player.sendMessage(CC.translate("&cYou must open a minimum of 3 hypeboxes at once"));
            player.closeInventory();
            return;
        }
        startBattleAgainstBot(player);
    }

    private void startBattleAgainstBot(Player player) {
        for (String s : this.hypeboxAmounts.keySet()) {
            if(this.hypeboxAmounts.get(s) > 0) {
                this.isSpinning = true;
                player.closeInventory();
                HypeboxBattleInventory hypeboxBattleInventory = new HypeboxBattleInventory(player, null, hypeboxAmounts);
                hypeboxBattleInventory.getUI().open(player);

                UUID uuid = UUID.randomUUID();
                HypeboxesPlugin.getInstance().getHypeboxBattles().put(uuid, hypeboxBattleInventory);
                Clickable clickable = new Clickable("&f✧ &eClick to watch &c" + LuckpermUtils.getDisplayName(player) + " &eplay against &f&l<&6&lBot&f&l> &6Astro Bot &ein a &4&lHype Box Battle", "&eClick to view &6" + player.getName() + "'s &ehypebox battle &f✧", "/viewbattleopen " + uuid);
                clickable.add(CC.translate("\n&eYou can purchase hypeboxes at &6buy.astropvp.net&e!"));
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage("");
                    clickable.sendToPlayer(onlinePlayer);
                    onlinePlayer.sendMessage("");
                }
                return;
            }
        }
        player.sendMessage(CC.translate("&cYou can't do this battle, contact an admin to find out why"));
    }

    private void updateSlot() {
        if(hypeboxAmounts.isEmpty()) {
            this.preBattleInv.setItem(2, 5, new GuiItem(createEmptySlot(), event -> {
                //tryOpenHypeboxBattle(player);
            }));
        } else {
            this.preBattleInv.setItem(2, 5, new GuiItem(createReadySlot(), event -> {
                //tryOpenHypeboxBattle(player);
            }));
        }

        this.preBattleInv.update();
    }

    private ItemStack createEmptySlot() {
        return new ItemBuilder(Material.BARRIER, "&c&lInsert Hypebox", "&7Click a hypebox in your inventory",
                "&7to insert it into the battle.").colorize().build();
    }

    private ItemStack createReadySlot() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.SLIME_BALL, "&a&lInsert Hypebox", "&7Click a hypebox in your inventory",
                "&7to insert it into the battle.", "", "&fCurrent Hypeboxes:");
        for (String s : hypeboxAmounts.keySet()) {
            itemBuilder.addLore(" &f* " + hypeboxAmounts.get(s) + "x " + s + " Hypeboxes");
        }
        itemBuilder.addLore("", "&a&lClick on the player or bot to start");
        return itemBuilder.colorize().build();
    }


    private ItemStack createFillerItem(DyeColor dyeColor) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, dyeColor.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(" "));
        item.setItemMeta(meta);
        return item;
    }

    private void addToAmount(HypeBox hypeBox, int amount) {
        String id = hypeBox.getId();
        if(this.hypeboxAmounts.containsKey(id)) {
            this.hypeboxAmounts.put(id, this.hypeboxAmounts.get(id) + amount);
        } else {
            this.hypeboxAmounts.put(id, amount);
        }
    }

}
