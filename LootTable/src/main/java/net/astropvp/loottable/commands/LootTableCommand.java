package net.astropvp.loottable.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;

import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.LootTablePlugin;
import net.astropvp.loottable.data.LootTableData;
import net.astropvp.loottable.guis.LootTableMainGUI;
import net.astropvp.loottable.struct.ECO_UPDATE;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("loottable")
@Alias({"loottables", "loot"})
public class LootTableCommand extends CommandBase {

    @Permission("astro.admin")
    @Default
    public void onDefault(CommandSender sender) {
        LootTableMainGUI lootTableMainGUI = new LootTableMainGUI((Player) sender);
        lootTableMainGUI.getMainGUI().open((Player)sender);
    }


    @Permission("astro.admin")
    @SubCommand("save")
    public void onSave(CommandSender sender) {
        LootTablePlugin.getInstance().getLootTableManager().saveAll();
        sender.sendMessage(CC.Green + "Saved!!!");
    }

    @Permission("astro.admin")
    @SubCommand("fix")
    public void onFix(CommandSender sender) {
        for (LootTable table : LootTable.values()) {
            LootTableAPI.fixLoot(table);
        }
        sender.sendMessage(CC.Green + "Fixed!!!");
    }

    @Permission("astro.admin")
    @SubCommand("clone")
    public void cloneCommand(Player player, @Completion("#loottables") LootTable lootTableType, @Completion("#ecoupdates") ECO_UPDATE from, @Completion("#ecoupdates") ECO_UPDATE to) {
        LootTableData lootTable = LootTablePlugin.getInstance().getLootTableManager().getLootTable(lootTableType);
        lootTable.cloneFromTo(from, to);
        player.sendMessage(CC.translate("&a&l(!) &aCloned all data from " + from.getFancy() + " to " + to.getFancy()));
    }

    @Permission("astro.admin")
    @SubCommand("clonetest")
    public void cloneCommand(Player player, @Completion("#loottables") LootTable fromType, @Completion("#ecoupdates") ECO_UPDATE from, @Completion("#loottables") LootTable toType, @Completion("#ecoupdates") ECO_UPDATE to) {
        LootTableData lootTableFrom = LootTablePlugin.getInstance().getLootTableManager().getLootTable(fromType);
        lootTableFrom.cloneFromTo(from, toType, to);
        player.sendMessage(CC.translate("&a&l(!) &aCloned all data from &2" + fromType.getFancy() + " " + from.getFancy() + " &ato &2" + toType + " " + to.getFancy()));
    }

    @Permission("astro.admin")
    @SubCommand("additem")
    public void addItem(Player player, @Completion("#loottables") LootTable lootTableType, @Completion("#ecoupdates") ECO_UPDATE ecoUpdate, Double chance) {
        if (ItemUtils.isAirOrNull(player.getItemInHand())) {
            player.sendMessage(CC.translate("&c&l(!)&c Please hold an item!"));
            return;
        }
        if (chance < 0) {
            player.sendMessage(CC.translate("&c&l(!)&c Higher the chance retard!"));
            return;
        }
        player.sendMessage(CC.translate("&a&l(!)&a Added " + ItemUtils.getDisplayName(player.getItemInHand()) + " &ato the " + lootTableType.getFancy() + " &aLoottable for eco update: &f" + ecoUpdate.getFancy()));
        LootTablePlugin.getInstance().getLootTableManager().getLootTable(lootTableType).addItem(ecoUpdate,chance, player.getItemInHand().clone());

    }

    @Permission("astro.admin")
    @SubCommand("random")
    public void onRandomItem(CommandSender sender, @Completion("#players") Player receiver, @Completion("#loottables") LootTable lootTableType) {
        PlayerUtil.giveOrDropProtectedItem(receiver, LootTableAPI.getRandomLootItem(lootTableType), 60);
        sender.sendMessage(CC.translate("&aGiven " + receiver.getName() + " a random " + lootTableType.getFancy() + " item."));
        receiver.sendMessage(CC.translate("&aReceived a random " + lootTableType.getFancy() + " item."));
    }
}
