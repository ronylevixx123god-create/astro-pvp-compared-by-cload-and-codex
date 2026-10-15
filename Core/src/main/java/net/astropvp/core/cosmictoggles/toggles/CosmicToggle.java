/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.massivecraft.factions.FPlayer
 *  me.cosmicpvp.CosmicFactionUtils.cmds.CommandToggleSmoke
 *  me.ifamasssxd.betterpay.BetterPay
 *  me.ifamasssxd.cosmiccoinflip.CosmicCoinFlip
 *  me.ifamasssxd.cosmicduel.CosmicDuel
 *  me.ifamasssxd.cosmicjackpot.CosmicJackpot
 *  me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest
 *  me.ifamasssxd.itemfilter.Filter
 *  me.ifamasssxd.itemfilter.ItemFilter
 *  me.recolance.auction.Auction
 *  org.arkhamnetwork.Arkkit.patches.ClearInventoryConfirm
 *  org.arkhamnetwork.Arkkit.patches.chat_filter.ChatFilter
 *  org.arkhamnetwork.Arkkit.patches.networkevents.NetworkEventsListener
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package net.astropvp.core.cosmictoggles.toggles;

import com.massivecraft.factions.FPlayer;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.recolance.auction.Auction;
import net.astropvp.core.AstroCore;
import net.astropvp.itemfilter.ItemFilterPlugin;
import net.astropvp.itemfilter.data.FilterUser;
import org.arkhamnetwork.Arkkit.patches.ClearInventoryConfirm;
import org.arkhamnetwork.Arkkit.patches.chat_filter.ChatFilter;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.Arkkit.utils.EssentialsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum CosmicToggle {
    FILTER("Chat Filter", "/togglefilter"),
    MSG("Private Messages", "/msgtoggle"),
    ITEM_FILTER("Item Filter", "/filter toggle"),
    COINFLIP("Coin Flip Notifications", "/coinflip stfu"),
    JACKPOT("Jackpot Notifications", "/jackpot stfu"),
    AUCTION("Auction Bid Alerts", "/ah stfu"),
    DUELS("Duel Requests", "/duel toggle"),
    FACTION_LAND_TITLE("Faction Land Title", "/togglefactionland"),
    LOOTTABLE_MESSAGE("LootFilter Message", "/lootfilter togglemessage"),
    KILL_LAND_TITLE("Kill Title", "/togglekilltitle"),
    BOSS_MESSAGES("Boss Messages", "/togglebossmessages"),

    CLEAR_INVENTORY_CONFIRMATION("/clearinventory Confirmation", "/toggleciconfirm"),
    POTS_DROP("Pots Dropping from Kills", "/togglepotsdrop")
            ;

    private final String displayName;
    private final String command;

    public boolean getToggle(Player player, FPlayer fplayer) {

        if (this == FILTER) {
            return !ChatFilter.disabledFilteredPlayers.contains(player.getUniqueId());
        }

        if (this == MSG) {
            return !EssentialsUtils.getEssentials().getUser(player).isIgnoreMsg();
        }

        if (this == ITEM_FILTER) {
            FilterUser filterUser = ItemFilterPlugin.getInstance().getFilterManager().getFilterUser(player.getUniqueId());
            return filterUser.isEnabled();
        }

        if (this == COINFLIP) {
            return !AstroCore.getInstance().getCoinFlipManager().getToggledNotifications().contains(player.getUniqueId());
        }
        //if (this == JACKPOT) {
          //return !AstroCore.getInstance().getJackpotManager().getToggledNofications().contains(player.getUniqueId());
        //}
        if (this == AUCTION) {
            return !Auction.getPlugin().getToggles().isBidMessageToggled(player);
        }
        if (this == DUELS) {
            try {
                return !CosmicDuel.get().getDuelToggles().hasDuelsToggled(player);
            }
            catch (Exception e) {
                Bukkit.getLogger().info("Unable to pull toggle for " + player.getName() + "!");
                return false;
            }
        }


        if (this == FACTION_LAND_TITLE) {
            return AstroCore.getInstance().getAstroPlayerManager().getFromCache(player.getUniqueId().toString()).isFactionsLandTitleEnabled();
        }

        if (this == KILL_LAND_TITLE) {
            return AstroCore.getInstance().getAstroPlayerManager().getFromCache(player.getUniqueId().toString()).isKillTitleEnabled();
        }



        if (this == LOOTTABLE_MESSAGE) {
            return AstroCore.getInstance().getAstroPlayerManager().getFromCache(player.getUniqueId().toString()).isLootTableMessageEnabled();
        }

        if (this == CLEAR_INVENTORY_CONFIRMATION) {
            try {
                return !((ClearInventoryConfirm)ArkPlugins.CLEAR_INVENTORY_CONFIRM.getArkPlugin()).getClearInventoryToggled().contains(player.getUniqueId());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (this == BOSS_MESSAGES) {
            return AstroCore.getInstance().getAstroPlayerManager().getFromCache(player.getUniqueId().toString()).isBossMessagesEnabled();
        }
        if (this == POTS_DROP) {
            return AstroCore.getInstance().getAstroPlayerManager().getFromCache(player.getUniqueId().toString()).isPotsDropEnabled();
        }
        return false;
    }

    CosmicToggle(String displayName, String command) {
        this.displayName = displayName;
        this.command = command;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getCommand() {
        return this.command;
    }
}

