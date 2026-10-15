package net.crankedpvp.pvptop.data;

import com.massivecraft.factions.Faction;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;
import net.crankedpvp.pvptop.PvPTopPlugin;

public class PvPTopManager extends SaveLoadManager<FactionData> {

    public PvPTopManager(PvPTopPlugin plugin) {
        super(plugin, StoragePlugin.getInstance().getGson(), "data", FactionData.class);
    }

    public FactionData getOrCreate(Faction faction) {

        if(!faction.isNormal()) return null;

        if(super.contains(faction.getId())) {
            return super.getFromCache(faction.getId());
        }

        FactionData factionData = new FactionData(faction.getId());
        super.addToCache(factionData);
        return factionData;
    }


}
