package org.arkhamnetwork.enchantments.menus;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.*;
import net.astropvp.core.objects.EnchantmentCrystal;
import net.astropvp.core.objects.EnchantmentCrystalType;
import net.astropvp.core.utils.EnchantmentCrystalUtils;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.ExpUtils;
import org.arkhamnetwork.Arkkit.patches.ItemNametag;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.cmds.CommandTransmog;
import org.arkhamnetwork.enchantments.libs.EnchanterRarity;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EnchanterGUI extends CosmicGUI {
    public EnchanterGUI(Player player) {
        super(player, "Astro Enchanter", 18);
    }

    public void redraw() {
        boolean capsInfernal = HCFOutpostUtils.doesControlOutpost(this.player, "infernal");
        int index = 0;
        for (EnchanterRarity rarity : EnchanterRarity.values()) {
            int xpPrice = (int) rarity.getPrice();
            String realPriceString = NumberUtils.formatMoney(rarity.getPrice());
            int adjustedPrice = capsInfernal ? (int) (xpPrice * 0.9D) : xpPrice;
            setItem(index++, (new ItemBuilder(Material.STAINED_GLASS_PANE, rarity.getColor() + CC.Bold + rarity.getName() + " Enchantment" + CC.Gray + " (Right Click)", CC.Gray + "Examine to receive a random", rarity

                    .getColor() + rarity.getName().toLowerCase() + CC.Gray + " enchantment book.", "", CC.Gray + "Use " + rarity

                    .getColor() + "/help enchants " + rarity.getName().toLowerCase() + CC.Gray + " to view a list", CC.Gray + "of possible enchantments you could unlock!", "", CC.AquaB + "COST " + CC.White + (capsInfernal ? (CC.White + CC.Strike + realPriceString + CC.White + " -> " + CC.Aqua +

                    NumberUtils.formatMoney(adjustedPrice) + CC.White + " EXP " + CC.Gray + "(Infernal Stronghold)") : (realPriceString + " EXP")))).data(
                    (short) rarity.getPaneColor().getWoolData()).build(), () -> {
                int playerXp = this.player.getTotalExperience();
                int price = (int) rarity.getPrice();
                int newAdjustedPrice = HCFOutpostUtils.doesControlOutpost(this.player, "infernal") ? (int) (price * 0.9D) : price;
                if (newAdjustedPrice > playerXp) {
                    this.player.sendMessage(ChatColor.RED + "You do not have enough EXP to purchase that.");
                    this.player.sendMessage(ChatColor.GRAY + "Your XP: " + playerXp);
                    this.player.closeInventory();
                    return;
                }
                if (this.player.getInventory().firstEmpty() == -1) {
                    this.player.sendMessage(ChatColor.RED + "You do not have any room in your inventory.");
                    this.player.closeInventory();
                    return;
                }
                int exp = (playerXp - newAdjustedPrice);
                this.player.setTotalExperience(0);
                this.player.setExp(0);
                this.player.setLevel(0);
                this.player.giveExp(exp);
                this.player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "- " + NumberUtils.formatMoney(newAdjustedPrice) + " xp");
                this.player.getInventory().addItem(TieredEnchantments.getMysteryEnchantmentBook(rarity.getTier()));
                this.player.playSound(this.player.getLocation(), Sound.VILLAGER_HAGGLE, 1.0F, 0.44F);
                Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(this.player, "ENCHANTER_BUY", new Object[]{rarity.name(), newAdjustedPrice}));
            });
        }

        setItem(9, new InventoryItem(EnchantmentBook.getTransmogScroll()));
        setItem(10, new InventoryItem(EnchantmentBook.getWhiteScroll()));
        setItem(11, new InventoryItem(EnchantmentBook.getBlackScroll(100)));
        setItem(12, new InventoryItem(EnchantmentBook.getHeroicBlackscroll(100, 100, false)));
        setItem(13, new InventoryItem(ItemNametag.getNametagItem()));
    }

}
