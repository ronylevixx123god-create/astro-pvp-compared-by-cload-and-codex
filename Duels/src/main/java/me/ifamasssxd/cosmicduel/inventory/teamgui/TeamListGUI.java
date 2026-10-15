package me.ifamasssxd.cosmicduel.inventory.teamgui;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.TeamData;
import me.ifamasssxd.cosmicduel.struct.TeamDataManager;
import me.ifamasssxd.cosmicduel.utils.ItemBuilder;
import me.ifamasssxd.cosmicguishop.gui.builder.item.SkullBuilder;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamListGUI {
    private final PaginatedGui expShopUI;

    public PaginatedGui getExpShopUI() {
        return this.expShopUI;
    }


    public TeamListGUI(Player player) {

        this.expShopUI = new PaginatedGui(6, 45, "Team List");
        expShopUI.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        expShopUI.getFiller().fillBottom(new GuiItem(createFillerItem()));

        ItemStack prevPage = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prevPage.getItemMeta();
        prevMeta.setDisplayName(CC.translate("&c<- Previous Page"));
        List<String> prevLore = new ArrayList<>();
        prevLore.add(CC.translate("&7Right-Click&7 to go to the previous page."));
        prevMeta.setLore(prevLore);
        prevPage.setItemMeta(prevMeta);

        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextPage.getItemMeta();
        nextMeta.setDisplayName(CC.translate("&aNext Page ->"));
        List<String> nextLore = new ArrayList<>();
        nextLore.add(CC.translate("&7Right-Click&7 to go to the next page."));
        nextMeta.setLore(nextLore);
        nextPage.setItemMeta(nextMeta);


        ItemStack refreshButton = ItemBuilder.buildItem(Material.STONE_BUTTON, CC.translate("&a&lRefresh Page"), CC.translate("&7Click to refresh the page."));

        expShopUI.setItem(6, 1, new GuiItem(prevPage, event -> {
            expShopUI.previous();
            event.setCancelled(true);
        }));

        expShopUI.setItem(6, 4, new GuiItem(refreshButton, event -> {
            TeamListGUI teamListGUI = new TeamListGUI(player);
            teamListGUI.getExpShopUI().open(player);
        }));

        ItemStack homePage = ItemBuilder.buildItem(Material.BED, CC.translate("&6&lMain Menu"), CC.translate("&7Click to go back to the main menu."));
        expShopUI.setItem(6, 6, new GuiItem(homePage, event -> {
            MainTeamGUI teamListGUI = new MainTeamGUI(player);
            teamListGUI.getExpShopUI().open(player);
        }));

        expShopUI.setItem(6, 9, new GuiItem(nextPage, event -> {
            expShopUI.next();
            event.setCancelled(true);
        }));


        for (TeamData teamData : CosmicDuel.get().getTeamDataManager().getTeams().values()) {

            if (Bukkit.getPlayer(teamData.getLeaderUUID()) == null) continue;
            ItemStack skull = ItemBuilder.createSkull(Bukkit.getPlayer(teamData.getLeaderUUID()).getName(), CC.translate("&a&l" + Bukkit.getPlayer(teamData.getLeaderUUID()).getName() + "'s Team"));
            List<String> lore = new ArrayList<>();
            ItemMeta meta = skull.getItemMeta();
            lore.add("");
            lore.add(CC.translate("&a&lMembers:"));
            for (UUID uuid : teamData.getTeamates()) {
                if (Bukkit.getPlayer(uuid) == null) {
                    lore.add(CC.translate("&f&l ●&c Offline Player"));
                } else {
                    Player player1 = Bukkit.getPlayer(uuid);
                    lore.add(CC.translate("&f&l ●&a " + player1.getName()));
                }
            }
            lore.add("");
            lore.add(CC.translate("&7Click to duel this team!"));
            meta.setLore(lore);
            skull.setItemMeta(meta);


            expShopUI.addItem(new GuiItem(skull, event -> {
                TeamData playerTeam = CosmicDuel.get().getTeamDataManager().tryFindTeam(player.getUniqueId());
                if (playerTeam == null) {
                    player.sendMessage(CC.translate("&cYou are not apart of a team!"));
                    event.setCancelled(true);
                    player.closeInventory();
                    return;
                }

                Player sender = (Player) event.getWhoClicked();
                sender.performCommand("duel " + Bukkit.getPlayer(teamData.getLeaderUUID()).getName());


            }));
        }

    }


    public ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&f"));
        item.setItemMeta(meta);
        return item;
    }
}
