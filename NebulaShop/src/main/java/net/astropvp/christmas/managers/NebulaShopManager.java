package net.astropvp.christmas.managers;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.christmas.NebulaShop;
import net.astropvp.christmas.utils.ConfigUtils;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class NebulaShopManager extends SaveLoadManager<NebulaPlayer> {


    private ItemStack currentItem;


    private double currentPrice;


    private Location shopLocation;


    public NebulaShopManager() {
        super(NebulaShop.getInstance(), StoragePlugin.getInstance().getGson(), "data/", NebulaPlayer.class);
        this.currentItem = ConfigUtils.getItem(NebulaShop.getInstance().getConfig().getString("current-item"));
        this.currentPrice = NebulaShop.getInstance().getConfig().getDouble("current-price");
        this.shopLocation = ConfigUtils.stringToLocation(NebulaShop.getInstance().getConfig().getString("shop-location"));
    }



    public NebulaPlayer getNebulaPlayer(Player player) {

        if (getFromCache(player.getUniqueId().toString()) == null) {
            addToCache(new NebulaPlayer(player.getUniqueId(), player.getAddress().getAddress().toString()));
        }
        return getFromCache(player.getUniqueId().toString());
    }

    public void saveShop() {
        NebulaShop.getInstance().getConfig().set("current-item", ConfigUtils.toBase64(new ItemStack[]{this.currentItem}, 9));
        NebulaShop.getInstance().getConfig().set("current-price", this.currentPrice);
        NebulaShop.getInstance().getConfig().set("shop-location", ConfigUtils.locationToString(this.shopLocation));
        NebulaShop.getInstance().saveConfig();
    }


    public boolean canBuy(Player player) {
        NebulaPlayer nebulaPlayer = getNebulaPlayer(player);
        return !nebulaPlayer.isHasPurchased() && !isUniqueIPAndHasntPurchased(player);
    }

    public boolean isUniqueIPAndHasntPurchased(Player player) {
        NebulaPlayer nebulaPlayer = getNebulaPlayer(player);
        for (NebulaPlayer other : getCacheValues()) {
            if (other.getIp().equals(nebulaPlayer.getIp()) && other.isHasPurchased()) {
                return true;
            }
        }
        return false;
    }

    public void setAlreadyBought(Player player) {
        getNebulaPlayer(player).setHasPurchased(true);
    }

    public void clearAlreadyBought() {
        for (NebulaPlayer christmasPlayer : getCacheValues()) {
            christmasPlayer.setHasPurchased(false);
        }
    }


}
