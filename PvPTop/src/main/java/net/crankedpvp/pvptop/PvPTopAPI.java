package net.crankedpvp.pvptop;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import net.crankedpvp.pvptop.data.FactionData;
import org.bukkit.entity.Player;

public class PvPTopAPI {

    public static final String PREFIX = "&b&lPoints &8➥ &7";

    public static FactionData getFactionData(Faction faction) {
        return PvPTopPlugin.getInstance().getPvpTopManager().getOrCreate(faction);
    }

    public static FactionData getFactionData(String factionId) {
        return PvPTopPlugin.getInstance().getPvpTopManager().getOrCreate(Factions.getInstance().getFactionById(factionId));
    }

    public static FactionData getFactionData(Player player) {
        return PvPTopPlugin.getInstance().getPvpTopManager().getOrCreate(FPlayers.getInstance().getByPlayer(player).getFaction());
    }
}
