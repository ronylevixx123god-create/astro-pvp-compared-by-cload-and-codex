package net.astropvp.lootfilter;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import net.astropvp.core.AstroCore;
import net.astropvp.core.struct.AstroPlayer;
import net.astropvp.lootfilter.data.LootFilterData;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LootFilterAPI {
    public static void giveItem(Player player, LootTable lootTable, ItemStack item) {
        AstroPlayer astroPlayer = AstroCore.getInstance().getAstroPlayerManager().getAstroPlayer(player.getUniqueId());
        LootFilterData lootFilterData = AstroLootFilterPlugin.getInstance().getLootFilterManager().getLootFilterData(player.getUniqueId());
        if (lootFilterData.isFiltered(lootTable, item)) {
            if (astroPlayer.isLootTableMessageEnabled()) {
                player.sendMessage(CC.translate("&c&l(!)&c " + ItemUtils.getDisplayName(item) + " &chas been filtered out, type &7\"/lootfilter\"&c to edit your &l" + lootTable.getFancy() + " Filter&c."));
            }

            return;
        }
        if (astroPlayer.isLootTableMessageEnabled()) {
            player.sendMessage(CC.translate("&a&l(!)&a You have received " + ItemUtils.getDisplayName(item) + " &afrom the &l" + lootTable.getFancy() + "&a. Type &7\"/lootfilter\"&a to edit your &l" + lootTable.getFancy() + " Filter&a."));
            player.sendMessage(CC.translate( "&7You can toggle this message with &f\"/lootfilter togglemessage\"&7."));
        }

        PlayerUtil.giveOrDropProtectedItem(player, item, 60);

        boolean lootFrenzy = AstroLootFilterPlugin.getInstance().getConfig().getBoolean("loot-frenzy-enabled");

        if (lootFrenzy) {
            player.sendMessage(CC.translate("&bLoot Frenzy &7- &fDouble Loot"));
            if (lootFilterData.isFiltered(lootTable, item)) {
                if (astroPlayer.isLootTableMessageEnabled()) {
                    player.sendMessage(CC.translate("&c&l(!)&c " + ItemUtils.getDisplayName(item) + " &chas been filtered out, type &7\"/lootfilter\"&c to edit your &l" + lootTable.getFancy() + " Filter&c."));
                }

                return;
            }
            if (astroPlayer.isLootTableMessageEnabled()) {
                player.sendMessage(CC.translate("&a&l(!)&a You have received " + ItemUtils.getDisplayName(item) + " &afrom the &l" + lootTable.getFancy() + "&a. Type &7\"/lootfilter\"&a to edit your &l" + lootTable.getFancy() + " Filter&a."));
                player.sendMessage(CC.translate( "&7You can toggle this message with &f\"/lootfilter togglemessage\"&7."));
            }

            PlayerUtil.giveOrDropProtectedItem(player,LootTableAPI.getRandomLootItem(lootTable), 60);
        }
    }

    public static boolean isFiltered(Player player, LootTable lootTable, ItemStack item) {
        LootFilterData lootFilterData = AstroLootFilterPlugin.getInstance().getLootFilterManager().getLootFilterData(player.getUniqueId());
        return lootFilterData.isFiltered(lootTable, item);
    }

}
