package com.conaxgames.customskulls.guis;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.impl.MobGrinder;
import com.conaxgames.customskulls.data.SaveCustomBlock;
import com.conaxgames.customskulls.struct.MobGrinderManager;
import com.conaxgames.customskulls.util.CC;
import com.conaxgames.customskulls.util.inventoryui.TimeUtil;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import de.inventivegames.particle.ParticleEffect;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;

import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@Getter
public class GrinderCustomBlockGUI {
    private final PaginatedGui customBlockGui;
    private final ItemStack godlyKey = CrateKeyItem.getItem(CrateType.GRINDER);
    private final MobGrinderManager mobGrinderManager = CoreSkulls.instance.getMobGrinderManager();

    public GrinderCustomBlockGUI(AbilityData data, Block block) {
        AbilityData mobGrinder = mobGrinderManager.get(data.getUuid());
        int level = mobGrinder.getLevel();
        int price = 0;
        if (level == 0) {
            price = 15000000;
        } else if (level == 1) {
            price = 50000000;
        } else if (level == 2) {
            price = 250000000;
        }

        int time = 4500000;
        if (level == 1) {
            time = 5000000;
        } else if (level == 2) {
            time = 5850000;
        } else if (level == 3) {
            time = 7200000;
        }

        this.customBlockGui = new PaginatedGui(5, 27, "Grinder Custom Block Menu");
        ItemStack activateButton = new ItemBuilder(Material.STONE_BUTTON, CC.translate("&a&lActivate Button"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&f&l● &6Expires in: &f" + ((mobGrinder.getLastActivated() + time) - System.currentTimeMillis() <= 0 ? "✗" : TimeUtil.fancy((mobGrinder.getLastActivated() + time) - System.currentTimeMillis()))))
                .addLoreLine(CC.translate("&f&l● &6Status: " + (mobGrinder.isActive() ? "&a&lACTIVE" : "&c&lINACTIVE")))
                .addLoreLine(CC.translate("&f&l● &6Price: &e$" + NumberUtils.formatMoney(data.getAbilityType().getPricePerAction())))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7Click to activate this custom block"))
                .build();


        this.customBlockGui.setDefaultClickAction(event -> event.setCancelled(true));

        this.customBlockGui.setItem(3, 4, new GuiItem(CoreSkulls.customBlocks.get(data.getAbilityType().getName()).getCustomBlockItem()));

        this.customBlockGui.setItem(3, 6, new GuiItem(activateButton, event -> {
            if (CoreSkulls.isSavingGrinderBlocks) {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c Please retry in a second, the customblock is saving the data"));
                return;
            }


            mobGrinder.activate((Player) event.getWhoClicked(), mobGrinder.getUuid());
            event.getWhoClicked().closeInventory();


        }));

        //Godly key
        ItemStack godlyKey = new ItemBuilder(Material.GOLD_NUGGET, CC.translate("&4&lGodly Crate Keys &8(&7" + mobGrinder.getCrateKeys().get(CrateType.GRINDER) + "&8)"), CC.translate("&7Click to redeem the godly crate keys")).build();
        this.customBlockGui.setItem(4, 2, new GuiItem(godlyKey, event -> {
            if (CoreSkulls.isSavingGrinderBlocks) {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c Please retry in a second, the customblock is saving the data"));
                return;
            }
            int godlyKeys = mobGrinder.getCrateKeys().get(CrateType.GRINDER);
            if (godlyKeys == 0) return;
            int givenKeys = 0;
            int amountToGive;
            if (event.getWhoClicked().getInventory().firstEmpty() != -1) {
                int space = getEmptySlots((Player) event.getWhoClicked()) * 64;
                amountToGive = Math.min(godlyKeys, space);
                while (givenKeys != amountToGive) {
                    event.getWhoClicked().getInventory().addItem(this.godlyKey);
                    givenKeys++;
                }
            } else {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c Please clear some inventory space."));
                return;
            }


            event.getWhoClicked().sendMessage(CC.translate("&a&l+ " + NumberUtils.formatMoney(givenKeys) + " &4&lGodly &f&lCrate Keys"));
            mobGrinder.getCrateKeys().put(CrateType.GRINDER, godlyKeys - givenKeys);
            GrinderCustomBlockGUI grinderCustomBlockGUI = new GrinderCustomBlockGUI(data, block);
            grinderCustomBlockGUI.getCustomBlockGui().open(event.getWhoClicked());

        }));

        //XPBottle key
        double xp = mobGrinder.getXp();
        ItemStack xpBottle = new ItemBuilder(Material.EXP_BOTTLE, CC.translate("&d&lExp Bottle &8(&7" + NumberUtils.formatMoney((int) xp) + "&8)"), CC.translate("&7Click to redeem the experience")).build();
        this.customBlockGui.setItem(4, 8, new GuiItem(xpBottle, event -> {
            if (CoreSkulls.isSavingGrinderBlocks) {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c Please retry in a second, the customblock is saving the data"));
                return;
            }
            if (xp == 0) return;

            if (event.getWhoClicked().getInventory().firstEmpty() == -1) {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c Please clear some inventory space."));
                return;
            }

            event.getWhoClicked().getInventory().addItem(ExpBottles.getXpBottle((int) xp, "MobGrinder"));

            event.getWhoClicked().sendMessage(CC.translate("&a&l(!)&a You have received an expbottle worth " + NumberUtils.formatMoney(xp) + "XP"));
            mobGrinder.setXp(0);
            GrinderCustomBlockGUI grinderCustomBlockGUI = new GrinderCustomBlockGUI(data, block);
            grinderCustomBlockGUI.getCustomBlockGui().open(event.getWhoClicked());
        }));


        //Upgrade
        //XPBottle key

        ItemStack levelItem;
        if (level != 3) {
            levelItem = new ItemBuilder(Material.FEATHER, CC.translate("&e&lGrinder Block Upgrade &7&nLVL " + level))
                    .addLoreLine(CC.translate("&7Click to upgrade this auto mob grinder."))
                    .addLoreLine("")
                    .addLoreLine(CC.translate("&f&l● &eCurrent Level: &f" + level))
                    .addLoreLine(CC.translate("&8Price to Upgrade: &7$" + NumberUtils.formatMoney(price)))
                    .addLoreLine("")
                    .addLoreLine(CC.translate("&7Upgrading your grinder block will increase"))
                    .addLoreLine(CC.translate("&7the chance of getting more and better crate"))
                    .addLoreLine(CC.translate("&7keys and increase the grinding duration."))
                    .addLoreLine(CC.translate("&7The max auto mob grinder level is 3."))
                    .build();
        } else {
            levelItem = new ItemBuilder(Material.FEATHER, CC.translate("&e&lGrinder Block Upgrade &7&nLVL " + level))
                    .addLoreLine(CC.translate("&7Your auto mob grinder block has reached max level."))
                    .addLoreLine("")
                    .addLoreLine(CC.translate("&f&l● &eCurrent Level: &f" + level))
                    .addLoreLine("")
                    .addLoreLine(CC.translate("&7Upgrading your grinder block will increase"))
                    .addLoreLine(CC.translate("&7the chance of getting more and better crate"))
                    .addLoreLine(CC.translate("&7keys and increase the grinding duration."))
                    .addLoreLine(CC.translate("&7The max auto mob grinder level is 3."))

                    .build();
        }
        int finalPrice = price;
        this.customBlockGui.setItem(2, 8, new GuiItem(levelItem, event -> {
            if (CoreSkulls.isSavingGrinderBlocks) {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c Please retry in a second, the customblock is saving the data"));
                return;
            }

            if (mobGrinder.getLevel() == 3) {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c This grinder customblock has reached the max level of 3."));
                return;
            }


            if (EconomyUtils.hasBalance((OfflinePlayer) event.getWhoClicked(), finalPrice)) {
                EconomyUtils.withdrawBalance((OfflinePlayer) event.getWhoClicked(), finalPrice);
                mobGrinder.setLevel(mobGrinder.getLevel() + 1);
                event.getWhoClicked().sendMessage(CC.translate("&c&l- $" + NumberUtils.formatMoney(finalPrice)));
                event.getWhoClicked().sendMessage(CC.translate("&a&l(!)&a You have upgraded your grinder customblock to level " + mobGrinder.getLevel()));
            } else {
                event.getWhoClicked().sendMessage(CC.translate("&c(!)&c You don't not have enough money to purchase this upgrade."));
            }


            GrinderCustomBlockGUI grinderCustomBlockGUI = new GrinderCustomBlockGUI(data, block);
            grinderCustomBlockGUI.getCustomBlockGui().open(event.getWhoClicked());
        }));


        this.customBlockGui.getFiller().

                fillBorder(new GuiItem(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).

                        name(CC.translate("&7")).

                        build()));
    }

    public int getEmptySlots(Player p) {
        PlayerInventory inventory = p.getInventory();
        ItemStack[] cont = inventory.getContents();
        int i = 0;
        for (ItemStack item : cont)
            if (item != null && item.getType() != Material.AIR) {
                i++;
            }
        return 36 - i;
    }


}
