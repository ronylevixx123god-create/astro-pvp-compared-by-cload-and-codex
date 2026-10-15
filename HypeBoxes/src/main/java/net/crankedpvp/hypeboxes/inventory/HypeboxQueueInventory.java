package net.crankedpvp.hypeboxes.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class HypeboxQueueInventory {
    private final Gui hypeboxQueueInventory;

    public Gui getUI() {
        return this.hypeboxQueueInventory;
    }

    private Player viewer;

    public HypeboxQueueInventory(Player viewer) {
        this.viewer = viewer;
        this.hypeboxQueueInventory = new Gui(1, "Hypebox Battle Queue");
        this.hypeboxQueueInventory.setDefaultClickAction(event -> event.setCancelled(true));
        populateQueue();
        populateCreateButton();
    }

    private final ItemStack createButton = new ItemBuilder(Material.SLIME_BALL, "&a&lCreate Your Own",
            "&7Click to create your own Hype", "&7Box battle, choose which hypeboxes",
            "&7and how many of them you wish", "&7to battle with!",
            "", "&7Will you battle against the &6Astro", "&6Bot&7, or another &4player&7...",
            "", "&a&l(!) &aClick to create a battle").colorize().build();

    private void populateCreateButton() {
        this.hypeboxQueueInventory.setItem(8, new GuiItem(createButton, event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
            HypeboxBattleCreateInventory hypeboxBattleCreateInventory = new HypeboxBattleCreateInventory(player);
            hypeboxBattleCreateInventory.getUI().open(player);
        }));
    }

    private void createItemForPlayer(Player player, Map<String, Integer> hypeboxes) {
        ItemBuilder itemBuilder = new ItemBuilder(SkullUtils.getSkullFromPlayer(player));
        itemBuilder.setName(LuckpermUtils.getDisplayName(player) + "'s Hypebox Battle");
        itemBuilder.addLore("", "&fBattle Boxes:");
        for (String s : hypeboxes.keySet()) {
            itemBuilder.addLore(" &f* " + hypeboxes.get(s) + "x " + s + " Hypeboxes");
        }
        itemBuilder.addLore("");
        String line = player == viewer ? "&c&lClick &fto cancel your hypebox battle" : "&a&lClick &fto enter the hypebox battle";
        itemBuilder.addLore(line);

        this.hypeboxQueueInventory.addItem(new GuiItem(itemBuilder.colorize().build(), event -> {
            if(player == viewer) {
                tryCancelHypeboxBattle(player, hypeboxes);
            } else {
                tryPutClickingPlayerInBattle((Player) event.getWhoClicked(), player, hypeboxes);
            }
        }));
    }

    private void tryCancelHypeboxBattle(Player player, Map<String, Integer> hypeboxes) {
        player.closeInventory();
        BattleQueueManager.getBattleQueue().remove(player);
        hypeboxes.forEach((hb, amt) -> player.getInventory().addItem(HypeboxesPlugin.getInstance().getHypeBoxRegister().getHypebox(hb).createHypeBox(amt)));
        player.sendMessage(CC.translate(Language.PREFIX.getMessage() + " &aYou have cancelled your hypebox battle"));

    }

    private void tryPutClickingPlayerInBattle(Player whoClicked, Player player, Map<String, Integer> hypeboxes) {
        if(!hasRequiredHypeboxes(whoClicked, hypeboxes)) {
            whoClicked.sendMessage(CC.translate("&cYou do not have enough hypeboxes for this battle!"));
            return;
        }

        if(!BattleQueueManager.getBattleQueue().containsKey(player)) {
            whoClicked.sendMessage(CC.translate(Language.PREFIX.getMessage() + " &cThis hypebox battle has already ended"));
            return;
        }

        takeHypeboxes(whoClicked, hypeboxes);
        removeFromQueue(player);
        updateInventory();
        startBattle(player, whoClicked, hypeboxes);
    }

    private void takeHypeboxes(Player whoClicked, Map<String, Integer> hypeboxes) {
        for (Map.Entry<String, Integer> kv : hypeboxes.entrySet()) {
            HypeBox hypeBox = HypeboxesPlugin.getInstance().getHypeBoxRegister().getHypebox(kv.getKey());
            int amount = kv.getValue();
            ItemStack[] items = whoClicked.getInventory().getContents();
            for (int i = 0; i < items.length; i++) {
                if (ItemUtils.isAirOrNull(items[i])) continue;
                if (HypeBoxManager.isHypebox(items[i]) && HypeBoxManager.getHypeBoxFromItemStack(items[i]) == hypeBox) {
                    int amt = items[i].getAmount();
                    if (amt == amount) {
                        whoClicked.getInventory().setItem(i, null);  // Clears the slot
                        return;
                    } else if (amt > amount) {
                        items[i].setAmount(items[i].getAmount() - amount);
                        return;
                    } else {
                        continue;
                    }
                }
            }
        }
        whoClicked.updateInventory();
    }

    private void startBattle(Player player, Player opponent, Map<String, Integer> hypeboxes) {
        opponent.closeInventory();
        player.closeInventory();

        HypeboxBattleInventory hypeboxBattleInventory = new HypeboxBattleInventory(player, opponent, hypeboxes);
        hypeboxBattleInventory.getUI().open(player);
        hypeboxBattleInventory.getUI().open(opponent);

        UUID uuid = UUID.randomUUID();
        HypeboxesPlugin.getInstance().getHypeboxBattles().put(uuid, hypeboxBattleInventory);
        Clickable clickable = new Clickable("&f✧ &eClick to watch &c" + LuckpermUtils.getDisplayName(player) + " &eplay against " +  LuckpermUtils.getDisplayName(opponent) + " &ein a &4&lHype Box Battle", "&eClick to view &6" + player.getName() + "'s &ehypebox battle &f✧", "/viewbattleopen " + uuid);
        clickable.add(CC.translate("\n&eYou can purchase hypeboxes at &6buy.astropvp.net&e!"));
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage("");
            clickable.sendToPlayer(onlinePlayer);
            onlinePlayer.sendMessage("");
        }
    }

    private void updateInventory() {
        this.hypeboxQueueInventory.setItem(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8), new GuiItem(new ItemStack(Material.AIR)));
        populateQueue();
        populateCreateButton();
    }

    private void populateQueue() {
        BattleQueueManager.getBattleQueue().forEach(this::createItemForPlayer);
    }

    private void removeFromQueue(Player player) {
        BattleQueueManager.getBattleQueue().remove(player);
    }

    private boolean hasRequiredHypeboxes(Player player, Map<String, Integer> hypeboxes) {
        for (Map.Entry<String, Integer> kv : hypeboxes.entrySet()) {
            HypeBox hypeBox = HypeboxesPlugin.getInstance().getHypeBoxRegister().getHypebox(kv.getKey());
            int amount = kv.getValue();
            if(!playerHasHypeboxes(player, hypeBox, amount)) {
                return false;
            }
        }
        return true;
    }

    private boolean playerHasHypeboxes(Player player, HypeBox hypeBox, int amount) {
        for (ItemStack content : player.getInventory().getContents()) {
            if(HypeBoxManager.isHypebox(content) && HypeBoxManager.getHypeBoxFromItemStack(content) == hypeBox) {
                return content.getAmount() >= amount;
            }
        }
        return false;
    }

}
