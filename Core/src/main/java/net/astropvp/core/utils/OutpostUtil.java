package net.astropvp.core.utils;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.golfing8.kore.FactionsKore;
import com.golfing8.kore.feature.OutpostFeature;
import com.massivecraft.factions.Faction;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class OutpostUtil {
    private static final FactionsKore kore = FactionsKore.get();
    private static final OutpostFeature outpostFeature = kore.getFeature(OutpostFeature.class);


    public static boolean hasMarsOutpost(Faction faction) {
        return outpostFeature.hasOutpost("mars", faction.getId());
    }

    public static boolean hasMarsOutpost(Player player) {
        return outpostFeature.hasOutpost("mars", FactionUtils.getFactionId(player));
    }

    public static boolean hasPlutoOutpost(Faction faction) {
        return outpostFeature.hasOutpost("pluto", faction.getId());
    }

    public static boolean hasPlutoOutpost(Player player) {
        return outpostFeature.hasOutpost("pluto", FactionUtils.getFactionId(player));
    }


}
