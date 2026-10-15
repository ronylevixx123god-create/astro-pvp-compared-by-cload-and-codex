package net.astropvp.customblocks.guis;

import com.cosmicpvp.cosmicutils.utils.*;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import net.astropvp.customblocks.struct.impl.AutoGrinder;
import net.astropvp.customblocks.utils.ItemBuilder;
import net.astropvp.customblocks.utils.TimeUtil;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class AutoGrinderGui {
    private PaginatedGui gui;
    private final ItemStack grinderKeyItem = CrateKeyItem.getItem(CrateType.SOLAR);
    private final ItemStack godlyKeyItem = CrateKeyItem.getItem(CrateType.COSMIC);
    public AutoGrinderGui(Player player, AutoGrinder autoGrinder) {
        this.gui = new PaginatedGui(3, 27, "Auto Mob Grinder Menu");
        this.gui.setItem(2, 2, getKeyClaimItem(autoGrinder));
        this.gui.setItem(3, 2, getGodlyKeysClaimItem(autoGrinder));
        this.gui.setItem(2, 4, getStatusItem(autoGrinder));
        this.gui.setItem(2, 6, getExpClaimItem(autoGrinder));
        this.gui.setItem(2, 8, getUpgradeItem(autoGrinder));

        this.gui.setDefaultClickAction(event -> event.setCancelled(true));

    }

    public GuiItem getKeyClaimItem(AutoGrinder autoGrinder) {
        ItemStack item = new ItemBuilder(Material.GOLD_NUGGET)
                .setName("&e&lSolar Keys &7(&f" + NumberUtils.formatMoney(autoGrinder.getGrinderKeys()) + "&7/" + autoGrinder.getMaxKeys(autoGrinder.getLevel()) + "&7)")
                .addLoreLine("&7Click to claim the solar keys in this auto grinder!")
                .colorize()
                .build();
        return new GuiItem(item, event -> {
            if (autoGrinder.getGrinderKeys() == 0) return;
            while (event.getWhoClicked().getInventory().firstEmpty() != -1 && autoGrinder.getGrinderKeys() != 0) {
                autoGrinder.setGrinderKeys(autoGrinder.getGrinderKeys()-1);
                PlayerUtil.giveOrDropProtectedItem((Player) event.getWhoClicked(), grinderKeyItem, 60);
            }
            this.gui.updateItem(2, 2, getKeyClaimItem(autoGrinder));
        });
    }

    public GuiItem getGodlyKeysClaimItem(AutoGrinder autoGrinder) {
        ItemStack item = new ItemBuilder(Material.GOLD_NUGGET)
                .setName("&b&lCosmic Keys &7(&f" + NumberUtils.formatMoney(autoGrinder.getGodlyKeys()) + "&7/" + autoGrinder.getMaxKeys(autoGrinder.getLevel()) + "&7)")
                .addLoreLine("&7Click to claim the cosmic keys in this auto grinder!")
                .colorize()
                .build();
        return new GuiItem(item, event -> {
            if (autoGrinder.getGodlyKeys() == 0) return;
            while (event.getWhoClicked().getInventory().firstEmpty() != -1 && autoGrinder.getGodlyKeys() != 0) {
                autoGrinder.setGodlyKeys(autoGrinder.getGodlyKeys()-1);
                PlayerUtil.giveOrDropProtectedItem((Player) event.getWhoClicked(), godlyKeyItem, 60);
            }
            this.gui.updateItem(3, 2, getGodlyKeysClaimItem(autoGrinder));
        });
    }

    public GuiItem getStatusItem(AutoGrinder autoGrinder) {
        ItemStack activateButton = new com.cosmicpvp.cosmicutils.utils.ItemBuilder(Material.STONE_BUTTON, CC.translate("&6&lActivate Grinder"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&6Expires in: &f" + ((autoGrinder.getLastActivated() + 3600000) - System.currentTimeMillis() <= 0 ? "✗" : TimeUtil.fancy((autoGrinder.getLastActivated() + 3600000) - System.currentTimeMillis()))))
                .addLoreLine(CC.translate("&6Status: " + (autoGrinder.isActive() ? "&a&lACTIVE" : "&c&lINACTIVE")))
                .addLoreLine(CC.translate("&6Price: &2$&a" + NumberUtils.formatMoney(autoGrinder.getActivationPrice())))
                .addLoreLine("")
                .addLoreLine(CC.translate("&c&lWARNING &7You must stay in a &f16x16&7 radius of"))
                .addLoreLine(CC.translate("&7this auto grinder block for it to keep grinding keys."))
                .addLoreLine()
                .addLoreLine(CC.translate("&7Click to activate this auto grinder"))

                .build();
        return new GuiItem(activateButton, event -> {
            if (EconomyUtils.hasBalance((OfflinePlayer) event.getWhoClicked(), autoGrinder.getActivationPrice())) {
                autoGrinder.activate((Player) event.getWhoClicked());
                EconomyUtils.withdrawBalance((OfflinePlayer) event.getWhoClicked(), autoGrinder.getActivationPrice());
                event.getWhoClicked().sendMessage(CC.translate("&c&l- " + NumberUtils.formatMoney(autoGrinder.getActivationPrice())));
            } else {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c You do not have enough money to activate this auto grinder!"));
            }
            this.gui.updateItem(2, 4, getStatusItem(autoGrinder));

        });
    }

    public GuiItem getExpClaimItem(AutoGrinder autoGrinder) {
        ItemStack item = new ItemBuilder(Material.EXP_BOTTLE)
                .setName("&a&lEXP &7(" + NumberUtils.formatMoney(autoGrinder.getExp()) + "&7)")
                .addLoreLine("&7Click to claim the exp in this auto grinder!")
                .colorize()
                .build();
        return new GuiItem(item, event -> {
            if (autoGrinder.getExp() == 0) return;
            ExpUtils.giveXP((Player) event.getWhoClicked(), autoGrinder.getExp());
            event.getWhoClicked().sendMessage(CC.translate("&a&l+ " + NumberUtils.formatMoney(autoGrinder.getExp()) + " EXP"));
            autoGrinder.setExp(0);
            this.gui.updateItem(2, 6, getExpClaimItem(autoGrinder));

        });
    }

    public GuiItem getUpgradeItem(AutoGrinder autoGrinder) {
        ItemStack item = new ItemBuilder(Material.EMERALD)
                .setName("&a&lUpgrade Autogrinder")
                .addLoreLine("")
                .addLoreLine("&aCurrent Level: &f" + autoGrinder.getLevel() + "&7/" + autoGrinder.getMaxLevel())
                .addLoreLine(autoGrinder.getLevel() == autoGrinder.getMaxLevel() ? "&aUpgrade Price: &c" + "MAX" : "&aUpgrade Price: &f$" + NumberUtils.formatMoney(autoGrinder.getUpgradePrice(autoGrinder.getLevel())))
                .addLoreLine("")
                .addLoreLine("&7Click to upgrade the auto grinder!")
                .colorize()
                .build();
        return new GuiItem(item, event -> {
            if (autoGrinder.getLevel() == autoGrinder.getMaxLevel()) return;
            if (!EconomyUtils.hasBalance((OfflinePlayer) event.getWhoClicked(), autoGrinder.getUpgradePrice(autoGrinder.getLevel()))) {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c You do not have enough money to upgrade your auto grinder! ($" + NumberUtils.formatMoney(autoGrinder.getUpgradePrice(autoGrinder.getLevel())) + ")"));
                return;
            }

            EconomyUtils.withdrawBalance((OfflinePlayer) event.getWhoClicked(), autoGrinder.getUpgradePrice(autoGrinder.getLevel()));
            event.getWhoClicked().sendMessage(CC.translate("&c&l- " + NumberUtils.formatMoney(autoGrinder.getUpgradePrice(autoGrinder.getLevel()))));
            autoGrinder.setLevel(autoGrinder.getLevel()+1);
            this.gui.updateItem(2, 8, getUpgradeItem(autoGrinder));
        });
    }
}
