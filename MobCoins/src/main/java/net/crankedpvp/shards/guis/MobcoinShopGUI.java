package net.crankedpvp.shards.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.SkullUtils;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;

import net.astropvp.core.utils.ItemBuilder;
import net.crankedpvp.shards.MobcoinPlugin;
import net.crankedpvp.shards.data.MobcoinsUser;
import net.crankedpvp.shards.managers.MobcoinManager;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MobcoinShopGUI {
    private PaginatedGui shardsGUI;

    private MobcoinManager shardsManager = MobcoinPlugin.getInstance().getShardsManager();


    public MobcoinShopGUI(Player player) {
        MobcoinsUser shardsUser = shardsManager.getShardsUser(player.getUniqueId());
        this.shardsGUI = new PaginatedGui(6, 54, "Mobcoins Shop Menu");
        this.shardsGUI.setDefaultClickAction(event -> event.setCancelled(true));
        this.shardsGUI.getFiller().fillBorder(new GuiItem(createFillerItem()));
        this.shardsGUI.setItem(6, 5, new GuiItem(createUserInfoItem(shardsUser)));
        this.shardsGUI.setItem(6, 1, new GuiItem(createPreviousItem(), event -> {
            MainMobcoinGUI mainShardsGUI = new MainMobcoinGUI(player);
            mainShardsGUI.getShardsGUI().open(player);
        }));
            this.shardsGUI.updateItem(6, 5, new GuiItem(createUserInfoItem(shardsUser)));

        Map<ItemStack, Integer> map = sort(MobcoinPlugin.getInstance().getShardsShopManager().getShopItems());
        for (Map.Entry<ItemStack, Integer> entry : map.entrySet()) {
            int price = entry.getValue();
            ItemStack item = entry.getKey();
            this.shardsGUI.addItem(new GuiItem(new ItemBuilder(item.clone()).addLore("").addLore("&e&l* &fPrice: &6⛃&e" + price).colorize().build(), event -> {
                MobcoinPlugin.getInstance().getShardsManager().purchaseItem(player.getUniqueId(), item, price);
                this.shardsGUI.updateItem(6, 5, new GuiItem(createUserInfoItem(shardsUser)));

            }));
        }

    }

    public ItemStack createUserInfoItem(MobcoinsUser shardsUser) {
        return new ItemBuilder(SkullUtils.getSkullFromPlayer(Bukkit.getPlayer(shardsUser.getOfflinePlayer().getUniqueId())))
                .setName("&e&l"+shardsUser.getOfflinePlayer().getName() + "'s Mobcoins Info")
                .addLore("")
                .addLore("&6&lPlayer Info")
                .addLore("&e&l * &fBalance&7: &6⛃&e" + NumberUtils.formatMoney(shardsUser.getBalance()))
                .addLore("&e&l * &fSpent&7: &6⛃&e" + NumberUtils.formatMoney(shardsUser.getSpent()))
                .addLore("&e&l * &fTransactions&7: &e" + NumberUtils.formatMoney(shardsUser.getTransactions().size()))
                .addLore("")
                .addLore("&6&lServer Info")
                .addLore("&e&l * &fTotal Mobcoins&7: &6⛃&e" + NumberUtils.formatMoney(shardsManager.getTotalShards()))
                .addLore("&e&l * &fRichest Player&7: &d" + shardsManager.getRichestPlayer().getOfflinePlayer().getName() + " &8➥ &6⛃&e" + shardsManager.getRichestPlayer().getBalance())
                .colorize()
                .build();
    }

    public ItemStack createPreviousItem() {
        return new ItemBuilder(Material.ARROW, CC.translate("&c<- Go Back"), "&7Right-Click&7 to go to the previous page.").colorize().build();
    }

    public ItemStack createNextItem() {
        return new ItemBuilder(Material.ARROW, CC.translate("&aNext Page ->"), "&7Right-Click&7 to go to the next page.").colorize().build();

    }

    public ItemStack createFillerItem() {
        return new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData())).setName("&7").colorize().build();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sort(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<K, V> result = new LinkedHashMap<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map.Entry<K, V> entry = list.get(i);
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
