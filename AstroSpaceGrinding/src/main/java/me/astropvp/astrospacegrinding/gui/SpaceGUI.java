package me.astropvp.astrospacegrinding.gui;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import me.astropvp.astrospacegrinding.AstroSpaceGrindingAPI;
import me.astropvp.astrospacegrinding.AstroSpaceGrindingPlugin;
import me.astropvp.astrospacegrinding.data.SpacePlayerData;
import me.astropvp.astrospacegrinding.enums.MonsterType;
import me.astropvp.astrospacegrinding.utils.config.ConfigManager;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpaceGUI {
    @Getter
    private final Gui spaceGUI;

    public SpaceGUI(SpacePlayerData spacePlayerData) {
        Player player = spacePlayerData.getPlayer();
        this.spaceGUI = new Gui(2, "Space Menu");

        this.spaceGUI.setDefaultClickAction(event -> event.setCancelled(true));
        player.sendMessage(CC.translate("&a&l(!)&a Opening the space menu..."));
        double price = AstroSpaceGrindingPlugin.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getDouble("forceFieldPrice");
        if (HCFOutpostAPI.isControllingOutpost(player, OutpostType.SPACE)) price*=0.5;

        ItemStack infoItem = new ItemBuilder(Material.NETHERRACK, CC.translate("&4&lMars Warp &c(PvP Enabled)"))
                .addLoreLine(CC.translate("&7The space lands have been invaded by numerous"))
                .addLoreLine(CC.translate("&7weird creatures. &bAstronauts&7 team up to try and"))
                .addLoreLine(CC.translate("&7fight against these monsters."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7The creatures have already invaded certain"))
                .addLoreLine(CC.translate("&7spots in the space lands. Fight the monsters"))
                .addLoreLine(CC.translate("&7to obtain some of the &bbest items&7 in the universe."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7Once you are in the space lands, you can"))
                .addLoreLine(CC.translate("&7type &b/space stats&7 to view your space stats."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&dHeroic Enchantments&7 &7and &6Holy Whitescrolls&7."))
                .addLoreLine(CC.translate("&7are disabled in the space lands."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&cThere are currently &4&n" + getActivePlayersAtMars() + "&r &cactive players at /warp mars"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7&nClick here&7 to teleport to the mars lands."))
                .build();

        ItemStack shitWarp = new ItemBuilder(Material.ENDER_STONE, CC.translate("&d&lSpace Warp &7(PvP Disabled)"))
                .addLoreLine(CC.translate("&7The space lands have been invaded by numerous"))
                .addLoreLine(CC.translate("&7weird creatures. &bAstronauts&7 team up to try and"))
                .addLoreLine(CC.translate("&7fight against these monsters."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7The creatures have already invaded certain"))
                .addLoreLine(CC.translate("&7spots in the space lands. Fight the monsters"))
                .addLoreLine(CC.translate("&7to obtain some of the &bbest items&7 in the universe."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7Once you are in the space lands, you can"))
                .addLoreLine(CC.translate("&7type &b/space stats&7 to view your space stats."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&dHeroic Enchantments&7 &7and &6Holy Whitescrolls&7."))
                .addLoreLine(CC.translate("&7are disabled in the space lands."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&cPvP is &ndisabled&r &cat this warp"))
                .addLoreLine(CC.translate("&cDrop Chances are reduced by 80%"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7&nClick here&7 to teleport to the space lands."))
                .build();


        /*
        ItemStack forceField = new ItemBuilder(Material.DIAMOND_CHESTPLATE, CC.translate("&e&lPurchase Forcefield"))
                .addLoreLine(CC.translate("&7You can buy forcefield to grind peacefully"))
                .addLoreLine(CC.translate("&7without having to worry about enemies."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7While you have an active forcefield you cannot"))
                .addLoreLine(CC.translate("&7hit or be hit by enemy faction players."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7When you have an active forcefield, money"))
                .addLoreLine(CC.translate("&7EXP and loot chance are reduced by &e30%&7."))
                .addLoreLine(CC.translate("&7"))
                .addLoreLine(CC.translate("&7Leaving the space will reset your forcefield!"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&e&l* Price: &f$" + NumberUtils.formatMoney(price) + "/30 minutes"))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7&nClick here&7 to purchase 30 minutes of forcefield."))
                .build();

         */

        this.spaceGUI.setItem(1, 3, new GuiItem(shitWarp, event -> {
            event.getWhoClicked().closeInventory();
            player.chat("/warp space");
        }));

        this.spaceGUI.setItem(1, 7, new GuiItem(infoItem, event -> {
            event.getWhoClicked().closeInventory();
            player.chat("/warp mars");
        }));

        double finalPrice = price;
        /*
        this.spaceGUI.setItem(2, 5, new GuiItem(forceField, event -> {
            if (!player.getWorld().getName().equals(AstroSpaceGrindingAPI.getWorldName())) {
                player.sendMessage(CC.translate("&b&l(!)&b You can only enable &f&lFORCEFIELD&b in the space warp."));
                return;
            }

            if (CombatLog.inCombat(player)) {
                player.sendMessage(CC.translate("&c&l(!)&c You cannot purchase a forcefield while in combat!"));
                return;
            }

            if (ForceFieldData.findPlayer(player).isActive()) {
                player.sendMessage(CC.translate("&c&l(!)&c You cannot purchase a forcefield while you already have an active forcefield!"));
                return;
            }

            if (EconomyUtils.hasBalance(player, finalPrice)) {
                player.sendMessage(CC.translate("&b&l(!)&b You have purchased a forcefield. This will last for &f&l30 MINUTES&b. During this time you cannot hit or be hit by enemies."));
                EconomyUtils.withdrawBalance(player, finalPrice);
                ForceFieldData.findPlayer(player).activateShield();
            } else {
                player.sendMessage(CC.translate("&c&l(!)&c You do not have enough money to activate the forcefield."));
            }
        }));

         */



        ;




        ItemStack lootItem = new ItemBuilder(Material.CHEST, CC.translate("&6&lPossible Loot"))
                .addLoreLine(CC.translate("&7Possible loot you can get from killing"))
                .addLoreLine(CC.translate("&7space creatures in the space lands."))
                .addLoreLine("")
                .addLoreLine(CC.translate("&7&nClick here&7 to view the loot."))
                .build();

        this.spaceGUI.setItem(2, 5, new GuiItem(lootItem, event -> {
            player.openInventory(AstroSpaceGrindingAPI.getLootInventory(MonsterType.SPACE_ALIEN));
        }));

        spaceGUI.getFiller().fill(new GuiItem(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).name(CC.translate("&7")).build()));

    }

    private int getActivePlayersAtMars() {
        return Bukkit.getWorld("mars").getPlayers().size();
    }


}
