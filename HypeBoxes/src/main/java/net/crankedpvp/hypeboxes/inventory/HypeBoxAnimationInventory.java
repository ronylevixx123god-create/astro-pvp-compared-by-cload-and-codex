package net.crankedpvp.hypeboxes.inventory;


import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import net.crankedpvp.hypeboxes.interfaces.ActiveInventory;
import net.crankedpvp.hypeboxes.struct.HypeBox;
import net.crankedpvp.hypeboxes.utils.Arrow;
import net.crankedpvp.hypeboxes.utils.ItemBuilder;
import net.crankedpvp.hypeboxes.utils.LuckpermUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

public class HypeBoxAnimationInventory implements ActiveInventory {

    private BukkitTask spinningTask;

    private int amountOfSpins = 0;

    private ItemStack receivingItem;

    private final ItemStack[] itemStacks = new ItemStack[9];

    private final Player player;

    private boolean active = true;

    private boolean given = false;


    private final Gui hypeBoxAnimationInventory;

    public Gui getUI() {
        return this.hypeBoxAnimationInventory;
    }

    public HypeBoxAnimationInventory(Player player, HypeBox hypebox) {
        this.player = player;
        this.amountOfSpins = 0;
        this.receivingItem = hypebox.pickRandomReward();
        this.hypeBoxAnimationInventory = new Gui(3, CC.translate(hypebox.getDisplayName()));
        this.hypeBoxAnimationInventory.setDefaultClickAction(event -> event.setCancelled(true));
        this.hypeBoxAnimationInventory.getFiller().fill(new GuiItem(createFillerItem()));
        this.hypeBoxAnimationInventory.setItem(1, 5, new GuiItem(this.createWinningFillerItem()));
        this.hypeBoxAnimationInventory.setItem(3, 5, new GuiItem(this.createWinningFillerItem()));
        this.hypeBoxAnimationInventory.setCloseGuiAction(inventoryCloseEvent -> {
            if(!inventoryCloseEvent.getPlayer().equals(player)) return;
            System.out.println(player.getName() + " has finished opening hypebox: " + hypebox.getId());
            if(given) return;
            active = false;
            if(this.spinningTask != null) this.spinningTask.cancel();
            given = true;
            player.getInventory().addItem(receivingItem);
            if(hypebox.doesBroadcast())Bukkit.broadcastMessage(CC.translate("&d&lHype Boxes &8" + Arrow.arrow() + " &f" + LuckpermUtils.getDisplayName(player) + " &fhas just opened a " + hypebox.getDisplayName() + "&r &fand received:"));
            if(hypebox.doesBroadcast())Bukkit.broadcastMessage(CC.translate(" &f• &l" + receivingItem.getAmount() + "x &r" + (receivingItem.hasItemMeta() ? receivingItem.getItemMeta().getDisplayName() : receivingItem.getType().name())));

        });

        for(int i = 0; i < 9; i++) {
            ItemStack itemStack = hypebox.pickRandomReward();
            this.itemStacks[i] = itemStack;
            this.hypeBoxAnimationInventory.setItem(2, i+1, new GuiItem(itemStack));
        }

        runTask(hypebox);
    }

    public void shiftAllItems(HypeBox hypeBox) {
        this.amountOfSpins++;
        ItemStack[] itemStacks = this.itemStacks.clone();
        System.arraycopy(itemStacks, 1, this.itemStacks, 0, 8);
        this.itemStacks[8] = hypeBox.pickRandomReward();

        for(int i = 0; i < 9; i++) {
            this.hypeBoxAnimationInventory.updateItem(2, i+1, new GuiItem(this.itemStacks[i]));
        }
        this.player.updateInventory();
        this.player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 2.0f);
    }

    public void check(HypeBox hypeBox) {
        shiftAllItems(hypeBox);
        if(this.amountOfSpins >= 50) {
            this.spinningTask.cancel();
            this.receivingItem = this.itemStacks[4];
            this.player.playSound(player.getLocation(), Sound.NOTE_PLING, 2.0f, 2.0f);
        } else if(this.amountOfSpins == 47) {
            this.spinningTask.cancel();
            this.spinningTask = Bukkit.getScheduler().runTaskTimer(HypeboxesPlugin.getInstance(), () -> check(hypeBox), 0L, 7L);
        } else if(this.amountOfSpins == 43) {
            this.spinningTask.cancel();
            this.spinningTask = Bukkit.getScheduler().runTaskTimer(HypeboxesPlugin.getInstance(), () -> check(hypeBox), 0L, 5L);
        } else if(this.amountOfSpins == 35) {
            this.spinningTask.cancel();
            this.spinningTask = Bukkit.getScheduler().runTaskTimer(HypeboxesPlugin.getInstance(), () -> check(hypeBox), 0L, 3L);
        }
    }

    public void runTask(HypeBox hypeBox) {
        this.spinningTask = Bukkit.getScheduler().runTaskTimer(HypeboxesPlugin.getInstance(), () -> check(hypeBox), 3L, 3L);
    }

    private ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(" "));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createWinningFillerItem() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData()).setName(" ").setGlowing().build();
    }

    @Override
    public boolean isActive() {
        return this.active;
    }
}
