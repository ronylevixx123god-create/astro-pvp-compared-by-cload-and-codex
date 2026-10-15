package me.kingsavaqe.astrotournaments.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.Gui;
import me.astropvp.astropvpskins.enums.SkinType;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.inventory.adminSettingsGUI.AdminSettingsGUI;
import me.kingsavaqe.astrotournaments.inventory.settingsGUI.PlayerSettingsGUI;
import me.kingsavaqe.astrotournaments.managers.TournamentManager;
import me.kingsavaqe.astrotournaments.objects.Arena;
import me.kingsavaqe.astrotournaments.objects.TournamentPlayer;
import me.kingsavaqe.astrotournaments.utils.ItemBuilder;
import me.kingsavaqe.astrotournaments.utils.Utils;
import dev.triumphteam.gui.guis.GuiItem;

import net.astropvp.armorsets.struct.ArmorSetType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class TournamentGUI {
    private final Gui astroTournamentInventory;
    private final TournamentManager tournamentManager;

    public Gui getUI() {
        return this.astroTournamentInventory;
    }


    public TournamentGUI(AstroTournamentsPlugin astroTournamentsPlugin, Player player) {
        this.tournamentManager = AstroTournamentsPlugin.instance.getTournamentManager();
        this.astroTournamentInventory = new Gui(3, "Astro Tournaments Menu");
        this.astroTournamentInventory.setDefaultClickAction(event -> event.setCancelled(true));
        ItemStack glass = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName("&7").colorize().build();
        this.astroTournamentInventory.getFiller().fill(new GuiItem(glass));
        this.astroTournamentInventory.setItem(1, 1, new GuiItem(createProfile(tournamentManager.getTournamentPlayer(player.getUniqueId()))));
        this.astroTournamentInventory.setItem(1, 9, new GuiItem(createInfoItem()));
        this.astroTournamentInventory.setItem(2, 5, new GuiItem(create1V1Info(tournamentManager.getTournamentPlayer(player.getUniqueId())), event -> {
            if (tournamentManager.isActive()) {
                player.performCommand("tm join");
            } else {
                player.sendMessage(CC.translate("&c&l(!)&c There is currently no active tournament to join!"));
            }
        }));
        this.astroTournamentInventory.setItem(3, 9, new GuiItem(createSettingsItem(), event -> {
            if (tournamentManager.getTournamentPlayer(player.getUniqueId()).isInTournament()) {
                player.sendMessage(CC.translate("&c&l(!)&c You cannot open this menu when in a tournament!"));
                return;
            }
            new PlayerSettingsGUI(astroTournamentsPlugin, astroTournamentsPlugin.getTournamentManager().getTournamentPlayer(player.getUniqueId())).getUI().open(player);
        }));

        if (player.hasPermission("astro.srmod")) {
            this.astroTournamentInventory.setItem(3, 8, new GuiItem(createAdminMenu(), event -> {
                new AdminSettingsGUI(astroTournamentsPlugin).getUI().open(player);
            }));
        }

    }

    public String getCrystalFormatted() {
        StringBuilder string = new StringBuilder();
        if (tournamentManager.getCurrentArmorCrystal().isEmpty()) {
            return "none";
        }
        for (ArmorSetType type : tournamentManager.getCurrentArmorCrystal()) {
            string.append(type.name().charAt(0));
        }
        return string.toString();
    }

    public String getSkinFormatted(SkinType skinType) {
        if (skinType == null) return "none";
        return skinType.getDisplayName();
    }

    private ItemStack create1V1Info(TournamentPlayer player) {
        ItemBuilder item = new ItemBuilder(Material.GOLD_SWORD, "&6&lTournament: &n&e&l1v1");
        Map<String, Arena> arenaMap = tournamentManager.getAstroTournamentsPlugin().getArenaRegister().getArenas();
        item.addLoreLine("");
        item.addLoreLine("&e&lStatus: " + (tournamentManager.isActive() ? "&a&lOpen" : "&c&lClosed"));
        item.addLoreLine("&e&lCrystal: &f&l" + getCrystalFormatted());
        item.addLoreLine("&e&lSkins:");
        item.addLoreLine("&e&l* &7Helmet Skin: &f" + getSkinFormatted(player.getHelmetSkin()));
        item.addLoreLine("&e&l* &7Chestplate Skin: &f" + getSkinFormatted(player.getChestplateSkin()));
        item.addLoreLine("&e&l* &7Leggings Skin: &f" + getSkinFormatted(player.getLeggingsSkin()));
        item.addLoreLine("&e&l* &7Boots Skin: &f" + getSkinFormatted(player.getBootsSkin()));
        item.addLoreLine("&e&l* &7Backpack Skin: &f" + getSkinFormatted(player.getBackpackSkin()));
        item.addLoreLine("&e&l* &7Sword Skin: &f" + getSkinFormatted(player.getSwordSkin()));
        item.addLoreLine("");
        item.addLoreLine("&3&lArena's:");
        item.addLoreLine("");
        for (Arena arena : arenaMap.values()) {
            item.addLoreLine("&3&l* " + arena.getId() + ": ");
            item.addLoreLine("&7Status: " + (arena.isInUse() ? "&a&lActive" : "&c&lInactive"));
            if (arena.isInUse()) {
                TournamentPlayer player1 = arena.getCurrentlyFighting().getLeft();
                TournamentPlayer player2 = arena.getCurrentlyFighting().getRight();
                item.addLoreLine("&bMatch: &7" + player1.getPlayer().getName() + " &f" + player1.getKDR() + " &7[" + player1.getKills() + ":" + player1.getDeaths() + "] VS " + player2.getPlayer().getName() + " &f" + player2.getKDR() + " &7[" + player2.getKills() + ":" + player2.getDeaths() + "]");
            }

        }
        item.addLoreLine("");

        if (tournamentManager.isActive()) {
            item.addLoreLine("&7Click to join the active tournament.");
        } else {
            item.addLoreLine("&7There is currently no tournament to join.");

        }

        item.colorize();
        return item.build();

    }

    private ItemStack createProfile(TournamentPlayer player) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("&e&lTournament W/L Ratio: &f" + player.getWLRatio());
        lore.add("&e&l* &7Tournament Wins:&f " + player.getTournamenWins());
        lore.add("&e&l* &7Tournament Losses:&f " + player.getTournamenLosses());
        lore.add("");
        lore.add("&e&lMatch W/L Ratio: &f" + player.getKDR());
        lore.add("&e&l* &7Match Wins:&f " + player.getKills());
        lore.add("&e&l* &7Match Losses:&f " + player.getDeaths());
        lore.add("");
        lore.add("&e&lK/D Ratio: &f" + player.getKDR());
        lore.add("&e&l* &7Kills:&f " + player.getKills());
        lore.add("&e&l* &7Deaths:&f " + player.getDeaths());
        return Utils.createPlayerSkull(player.getPlayer().getName(), "&6&l" + player.getPlayer().getName() + "'s Tournament Stats", lore);

    }

    private ItemStack createSettingsItem() {
        return new ItemBuilder(Material.DIAMOND_HELMET, "&6&lTournament Profile", "&7Customize your set layout and").addLoreLine("&7select your custom skins and more.").addLoreLine("").addLoreLine("&7Click to edit profile.").colorize().build();
    }

    private ItemStack createAdminMenu() {
        return new ItemBuilder(Material.SLIME_BALL, "&d&lAdmin Settings &7(Admin Only)", "&7Click here to edit the tournament settings").colorize().build();
    }

    private ItemStack createInfoItem() {
        return new ItemBuilder(Material.BOOK, "&6&lAstro&f&lPvP &6&lTournaments")
                .addLoreLine("&7The ultimate battle of all battles, tournaments are a")
                .addLoreLine("&fround-based, knockout event ensuring&7 only the best")
                .addLoreLine("&7and smartest survive while the rest are left to watch")
                .addLoreLine("&7in awe - are you that gladiator?")
                .addLoreLine("")
                .addLoreLine("&7Tournaments are &fmade up of rounds&7 where each player")
                .addLoreLine("&7must win their match in the round to progress to the")
                .addLoreLine("&7next. If a player loses their match, they are eliminated")
                .addLoreLine("&7from the tournament. The objective is simple, defeat")
                .addLoreLine("&7all of your foes and show no mercy!")
                .addLoreLine("")
                .addLoreLine("&6&lWinner Rewards:")
                .addLoreLine("&6&l* &fTournament Lootbox")
                .addLoreLine("&6&l* &fAdmin Rewards")
                .colorize().build();

    }

}
