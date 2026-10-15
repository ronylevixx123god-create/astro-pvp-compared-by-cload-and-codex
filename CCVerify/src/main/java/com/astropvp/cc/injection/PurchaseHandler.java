package com.astropvp.cc.injection;

import com.astropvp.cc.packet.type.out.PacketOutPurchaseFeed;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import net.buycraft.plugin.bukkit.BuycraftPlugin;
import net.buycraft.plugin.data.Category;
import net.buycraft.plugin.data.Package;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PurchaseHandler {

    private BuycraftPlugin buycraftPlugin;

    public PurchaseHandler(){
        this.buycraftPlugin = BuycraftPlugin.getPlugin(BuycraftPlugin.class);
    }

    public void runPurchase(int packageID, String player){
        Package pckg = findPackage(packageID);

        if(pckg == null)
            return;

        for(CosmicPlayer cosmicPlayer : CosmicPlayers.get().getAll()){
            cosmicPlayer.sendPacket(new PacketOutPurchaseFeed(player, pckg.getName(), "https://i.imgur.com/kFXx6Km.png"));
        }
    }

    private Package findPackage(int packageID){
        for(Category category : buycraftPlugin.getListingUpdateTask().getListing().getCategories()){
            for(Package pckg : category.getPackages()){
                if(pckg.getId() == packageID)return pckg;
            }
        }
        return null;
    }
}
