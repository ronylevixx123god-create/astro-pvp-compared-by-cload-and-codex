package me.astropvp.cratekeys.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import me.astropvp.cratekeys.AstroCrateKeysAPI;
import me.astropvp.cratekeys.data.CrateData;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import me.ifamasssxd.cosmicguishop.ArkhamMoneyNotesAPI;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CrateKeyInventoryListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();


        CrateData data = AstroCrateKeysAPI.findCrate(block.getLocation());
        if (data == null) {
            return;
        }

        if (!AstroCrateKeysAPI.isCrateKey(player.getItemInHand())) {
            player.sendMessage(CC.translate("&c&l(!)&c Please hold a " + data.getCrateType().getPrefix() + CC.Bold +
                    data.getCrateType().getName() + "&f&l Crate Key&c to open this crate."));
            event.setCancelled(true);
            return;
        }


        if (CrateKeyItem.getCrateType(player.getItemInHand()) != data.getCrateType()) {
            player.sendMessage(CC.translate("&c&l(!)&c This is not the correct crate type. You need to click the " + CrateKeyItem.getCrateType(player.getItemInHand()).getPrefix() +
                    CC.Bold + CrateKeyItem.getCrateType(player.getItemInHand()).getName() + CC.Bold + "&f&l Crate&c to open this key."));
            event.setCancelled(true);
            return;
        }
        if (isPlayerInventoryFull(player)) {
            player.sendMessage(CC.translate("&cYour inventory is full. Clear some space before opening more keys!"));
            event.setCancelled(true);
            return;
        }

        if (player.isSneaking()) {
            int amountOfKeys = player.getItemInHand().getAmount();
            while (amountOfKeys != 0) {
                addItemToPlayer(player, data);
                amountOfKeys--;
            }
            player.setItemInHand(null);
        } else {
            addItemToPlayer(player, data);

            if (player.getItemInHand().getAmount() > 1) {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            } else {
                player.setItemInHand(null);
            }
        }

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);


        event.setCancelled(true);
    }
        

    public static boolean isPlayerInventoryFull(Player player) {
        byte b;
        int i;
        ItemStack[] arrayOfItemStack;
        for (i = (arrayOfItemStack = player.getInventory().getContents()).length, b = 0; b < i; ) {
            ItemStack item = arrayOfItemStack[b];
            if (item == null)
                return false;
            b++;
        }
        return true;
    }

    public static void addItemToPlayer(Player player, CrateData data) {

        CrateType type = data.getCrateType();
        ItemStack item = LootTableAPI.getRandomLootItem(LootTable.valueOf(type.name()));
        if (ArkhamMoneyNotesAPI.isMoneyNote(item)) {
            double amount = ArkhamMoneyNotesAPI.getMoneyNoteVal(item);
            EconomyUtils.depositBalance(player, amount);
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
            player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "+ $" + amount);
        } else if (ExpBottles.isXpBottle(item)) {
            int xpValue = ExpBottles.getXpAmount(item);
            int exp = (player.getTotalExperience() + xpValue);
            player.setTotalExperience(0);
            player.setExp(0);
            player.setLevel(0);
            player.giveExp(exp);
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
            player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "+ " + xpValue + " xp");
        } else {
            LootFilterAPI.giveItem(player, LootTable.valueOf(type.name()), item);
        }
    }
}


