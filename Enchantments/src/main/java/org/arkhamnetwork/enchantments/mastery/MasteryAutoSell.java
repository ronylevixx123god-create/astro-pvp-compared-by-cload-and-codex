package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicguishop.CosmicGUIShop;
import me.ifamasssxd.cosmicguishop.struct.ItemPrice;
import me.ifamasssxd.cosmicguishop.struct.ShopItemMap;
import me.ifamasssxd.cosmicguishop.struct.ShopManager;
import me.ifamasssxd.cosmicguishop.struct.ShopMaterialData;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MasteryAutoSell extends MasteryCustomEnchantment {
    private static HashMap<MaterialData, Double> cachedShopPrices;
    private ShopManager shopManager;

    public MasteryAutoSell() {
        super("Auto Sell", GeneralUtil.tools, 10);
        this.max = 1;
        MasteryAutoSell.cachedShopPrices = new HashMap<MaterialData, Double>();
        this.shopManager = CosmicGUIShop.get().getShopManager();
        for (final ShopItemMap items : this.shopManager.getShopItems().values()) {
            for (final Map.Entry<ShopMaterialData, ItemPrice> itemData : items.entrySet()) {
                MasteryAutoSell.cachedShopPrices.put(new MaterialData(itemData.getKey().getType(), (byte) itemData.getKey().getData()), itemData.getValue().getSellPrice());
            }
        }
        Bukkit.getLogger().info("[Enchantments (AutoSell)] Cached " + MasteryAutoSell.cachedShopPrices.size() + " /shop material sell prices.");
    }

    public static double getSellPrice(final MaterialData md) {
        return MasteryAutoSell.cachedShopPrices.getOrDefault(md, 0.0);
    }

    public void applyToolEffect(final Player player, final Block block, final int enchantLevel, final BlockEvent event) {
        if (event instanceof BlockBreakEvent) {
            final BlockBreakEvent bbe = (BlockBreakEvent) event;
            final Material m = bbe.getBlock().getType();
            if (bbe.getBlock().getDrops(player.getItemInHand()).size() < 0) {
                return;
            }
            final Collection<ItemStack> drops = (Collection<ItemStack>) bbe.getBlock().getDrops(player.getItemInHand());
            if (drops.size() <= 0) {
                return;
            }
            final ItemStack drop = (ItemStack) drops.toArray()[0];
            final boolean hasDetonate = EnchantmentAPI.itemHasEnchantment(player.getItemInHand(), "Detonate") || EnchantmentAPI.itemHasEnchantment(player.getItemInHand(), "Atomic Detonate");
            if (hasDetonate) {
                return;
            }
            if (bbe.getBlock().getDrops().size() < 0 || ((bbe.getBlock().getType() == Material.IRON_ORE || bbe.getBlock().getType() == Material.GOLD_ORE) && EnchantmentAPI.itemHasEnchantment(player.getItemInHand(), "Auto Smelt"))) {
                return;
            }
            final double price = getSellPrice(drop.getData());
            if (price <= 0.0) {
                return;
            }
            block.setTypeIdAndData(0, (byte) 0, true);
            player.giveExp(bbe.getExpToDrop());
            CosmicGUIShop.getEconomy().depositPlayer((OfflinePlayer) player, price);
            Bukkit.getPluginManager().callEvent((Event) new CustomCosmicPlayerEvent(player, "SHOP_SELL", new Object[]{price, drop.getAmount(), drop.getType(), drop.getData()}));
        }
    }
}
