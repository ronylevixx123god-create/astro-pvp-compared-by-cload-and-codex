/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  co.aikar.commands.BaseCommand
 *  co.aikar.commands.annotation.CommandAlias
 *  co.aikar.commands.annotation.CommandPermission
 *  co.aikar.commands.annotation.Default
 *  co.aikar.commands.annotation.Optional
 *  co.aikar.commands.annotation.Subcommand
 *  co.aikar.commands.annotation.Syntax
 *  co.aikar.commands.bukkit.contexts.OnlinePlayer
 *  com.decimatemc.decimatecommons.inventory.InventoryManager
 *  com.decimatemc.decimatecommons.util.CommonsUtil
 *  com.google.common.collect.Maps
 *  net.jodah.expiringmap.ExpiringMap
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.meta.ItemMeta
 */
package net.astropvp.lootboxes.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import co.aikar.commands.lib.expiringmap.ExpiringMap;

import net.astropvp.lootboxes.manager.ForeignLootboxManager;
import net.astropvp.lootboxes.manager.LootboxCreationManager;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import net.astropvp.lootboxes.AstroLootboxes;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.api.RewardItem;
import net.astropvp.lootboxes.gui.LootboxPluginGui;
import net.astropvp.lootboxes.gui.LootboxSearchGui;
import net.astropvp.lootboxes.gui.LootboxViewGui;
import net.astropvp.lootboxes.lootbox.IncompleteLootbox;
import com.google.common.collect.Maps;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CommandAlias(value = "lootbox|lb|mc|al")
public class LootboxCmd
        extends BaseCommand {
    private static final String ADMIN_PERMISSION = "decimatelootboxes.admin";
    private static final List<String> HELP_MESSAGE = Arrays.asList("&c&m----------&c&lAstro&f&lLootboxes&c&m----------", "&c/al create &8- &fCreate a lootbox.", "&c/al give <player> <name> &8- &fGive a player a lootbox", "&c/al edit <name> &8- &fEdit a lootbox.", "&c/al delete <name> &8- &fDelete a lootbox.", "&c/al rename <name> &8- &fRename a lootbox.", "&c/al clone <name> <clonename> &8- &fClone a lootbox.", "&c/al list &8- &fList all lootboxes.", "&c/al view/gui &8- &fView all lootboxes.", "&c/al plugin &8- &fView API managed lootboxes.", "&c/al search <name/lore/both> <query> &8- &fSearches all lootboxes for a query.", "&c&m--------------------------------------");
    private final AstroLootboxes plugin;
    private final LootboxManager lootboxManager;
    private final ForeignLootboxManager foreignLootboxManager;
    private final LootboxCreationManager creationManager;
    private final InventoryManager inventoryManager;
    private final LootboxViewGui lootboxViewGui;
    private final Map<String, String> confirmingPlayers = ExpiringMap.builder().expiration(20L, TimeUnit.SECONDS).build();

    public LootboxCmd(AstroLootboxes plugin, LootboxManager lootboxManager, ForeignLootboxManager foreignLootboxManager, LootboxCreationManager creationManager, InventoryManager inventoryManager, LootboxViewGui lootboxViewGui) {
        this.plugin = plugin;
        this.lootboxManager = lootboxManager;
        this.foreignLootboxManager = foreignLootboxManager;
        this.creationManager = creationManager;
        this.inventoryManager = inventoryManager;
        this.lootboxViewGui = lootboxViewGui;
    }

    @Default
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onDefaultCommand(CommandSender sender) {
        CommonsUtil.sendMessage(sender, HELP_MESSAGE);
    }

    @Subcommand(value = "list")
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onListCommand(CommandSender sender) {
        this.lootboxManager.getLootboxes().forEach(lootbox -> CommonsUtil.sendMessage(sender, lootbox.getDisplayItem().getItemMeta().getDisplayName() + "&r &8- &f&l(" + lootbox.getName() + "&f&l)"));
    }

    @Subcommand(value = "give")
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onGiveCommand(CommandSender sender, OnlinePlayer player, String lootboxName, @co.aikar.commands.annotation.Optional Integer amount) {
        if (amount == null) {
            amount = 1;
        }
        int finalAmount = amount;
        Optional<Lootbox> lootboxOptional = this.lootboxManager.getLootbox(lootboxName);
        lootboxOptional.ifPresent(lootbox -> {
            IntStream.range(0, finalAmount).forEach(i -> lootbox.giveItemTo(player.getPlayer(), this.lootboxManager));
            CommonsUtil.sendMessage(sender, "&a&l(&f!&a&l) &aLootbox given to &f" + player.getPlayer().getName());
        });
        if (!lootboxOptional.isPresent()) {
            CommonsUtil.sendMessage(sender, "&c&l(!) &cNo lootbox found for &7[" + lootboxName + "]");
        }
    }

    @Subcommand(value = "create")
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onCreateCommand(Player player) {
        this.creationManager.startCreationSession(player);
    }

    @Subcommand(value = "clone")
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onCloneCommand(Player player, String name, String cloneName) {
        if (this.lootboxManager.getLootbox(cloneName).isPresent()) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cThat lootbox already exists! &7[" + cloneName + "]");
            return;
        }
        if (!this.lootboxManager.getLootbox(name).isPresent()) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cThat lootbox does not exist! &7[" + name + "]");
        }
        this.lootboxManager.getLootbox(name).ifPresent(lootbox -> {
            IncompleteLootbox incompleteLootbox = new IncompleteLootbox(lootbox);
            incompleteLootbox.setName(cloneName);
            this.lootboxManager.addNewLootbox(incompleteLootbox.convert(this.plugin, this.lootboxManager));
        });
    }

    @Subcommand(value = "rename")
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onRenameCommand(Player player, String name, String targetName) {
        if (this.lootboxManager.getLootbox(targetName).isPresent()) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cThat lootbox already exists! &7[" + targetName + "]");
            return;
        }
        if (!this.lootboxManager.getLootbox(name).isPresent()) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cThat lootbox does not exist! &7[" + name + "]");
        }
        this.lootboxManager.getLootbox(name).ifPresent(lootbox -> {
            IncompleteLootbox incompleteLootbox = new IncompleteLootbox(lootbox);
            incompleteLootbox.setName(targetName);
            this.lootboxManager.removeLootbox(name);
            this.lootboxManager.addNewLootbox(incompleteLootbox.convert(this.plugin, this.lootboxManager));
        });
    }

    @Subcommand(value = "edit")
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onEditCommand(Player player, String name) {
        Optional<Lootbox> lootbox = this.lootboxManager.getLootbox(name);
        if (!lootbox.isPresent()) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cThat lootbox does not exist!");
            return;
        }
        if (!this.creationManager.startEditingSession(player, lootbox.get(), this.lootboxManager)) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cThere was a problem instantiating your edit session!");
        }
    }

    @Subcommand(value = "delete")
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onDeleteCommand(CommandSender sender, String name) {
        if (this.confirmingPlayers.containsKey(sender.getName()) && this.confirmingPlayers.get(sender.getName()).equalsIgnoreCase(name)) {
            boolean success = this.lootboxManager.removeLootbox(name);
            if (success) {
                CommonsUtil.sendMessage(sender, "&a&lSUCCESS! &aLootbox &f" + name + " &awas removed.");
            } else {
                CommonsUtil.sendMessage(sender, "&c&lFAILURE! &cLootbox &f" + name + " &ccould not be removed.");
            }
        } else {
            CommonsUtil.sendMessage(sender, "&a&lAre you sure? &aThis will permanently delete the lootbox. Type the command again to confirm.");
            this.confirmingPlayers.put(sender.getName(), name);
        }
    }

    @Subcommand(value = "view|gui")
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onViewCommand(Player player) {
        this.lootboxViewGui.getInventory().open(player);
    }

    @Subcommand(value = "search")
    @CommandPermission(value = "decimatelootboxes.admin")
    @Syntax(value = "<name/lore/both> <query>")
    public void onSearchCommand(Player player, String type, String[] rawQuery) {
        String query = String.join(" ", rawQuery);
        String noColorQuery = CommonsUtil.stripColor(query).toLowerCase();
        int searchType = type.equalsIgnoreCase("name") ? 0 : (type.equalsIgnoreCase("lore") ? 1 : 2);
        Predicate<List<RewardItem>> containsQuery = list -> list.stream().anyMatch(rewardItem -> {
            if (rewardItem.getItem() == null || !rewardItem.getItem().hasItemMeta()) {
                return false;
            }
            ItemMeta meta = rewardItem.getItem().getItemMeta();
            if ((searchType == 2 || searchType == 1) && meta.hasLore() && meta.getLore().stream().anyMatch(s -> CommonsUtil.stripColor(s).toLowerCase().contains(noColorQuery))) {
                return true;
            }
            if (searchType == 2 || searchType == 0) {
                return meta.hasDisplayName() && CommonsUtil.stripColor(meta.getDisplayName()).toLowerCase().contains(noColorQuery);
            }
            return false;
        });
        List<Lootbox> lootboxesFound = this.lootboxManager.getLootboxes().stream().filter(lootbox -> containsQuery.test(lootbox.getRandomRewards()) || containsQuery.test(lootbox.getBonusRewards())).collect(Collectors.toList());
        HashMap pluginLootboxesFound = Maps.newHashMap();
        this.foreignLootboxManager.getPluginManagers().forEach(manager -> pluginLootboxesFound.put(manager, manager.getLootboxes().stream().filter(lootbox -> containsQuery.test(lootbox.getRandomRewards()) || containsQuery.test(lootbox.getBonusRewards())).collect(Collectors.toList())));
        if (lootboxesFound.isEmpty() && pluginLootboxesFound.isEmpty()) {
            player.sendMessage(CommonsUtil.color("&c&l(!) &cNo results found."));
        } else {
            new LootboxSearchGui(this.lootboxManager, this.inventoryManager, this.creationManager, lootboxesFound, pluginLootboxesFound).getInventory().open(player);
        }
    }

    @Subcommand(value = "plugin")
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onPluginCommand(Player player) {
        new LootboxPluginGui(this.plugin, this.foreignLootboxManager, this.creationManager, this.inventoryManager, player);
    }
}

