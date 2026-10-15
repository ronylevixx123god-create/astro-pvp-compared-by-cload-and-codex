package net.astropvp.christmas.managers;
import net.astropvp.christmas.ChristmasPlugin;
import net.astropvp.christmas.utils.ConfigUtils;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ChristmasManager extends SaveLoadManager<ChristmasPlayer> {
    public ChristmasManager() {

        super(ChristmasPlugin.getInstance(), StoragePlugin.getInstance().getGson(), "santa-data/", ChristmasPlayer.class);
    }

    public ChristmasPlayer getChristmasPlayer(Player player) {
        if (getFromCache(player.getUniqueId().toString()) == null) {
            addToCache(new ChristmasPlayer(player.getUniqueId(), player.getAddress().getAddress().toString()));
        }
        return getFromCache(player.getUniqueId().toString());
    }

    private ItemStack currentItem = ConfigUtils.getItem(ChristmasPlugin.getInstance().getConfig().getString("current-item"));
    private Location shopLocation = ConfigUtils.stringToLocation(ChristmasPlugin.getInstance().getConfig().getString("shop-location"));


    public void saveShop() {
        ChristmasPlugin.getInstance().getConfig().set("current-item", (Object)ConfigUtils.toBase64(new ItemStack[]{this.currentItem}, 9));
        ChristmasPlugin.getInstance().getConfig().set("shop-location", (Object)ConfigUtils.locationToString(this.shopLocation));
        ChristmasPlugin.getInstance().saveConfig();
    }


    public boolean canBuy(Player player) {
        ChristmasPlayer christmasPlayer = getChristmasPlayer(player);
        return !christmasPlayer.isHasPurchased() && !isUniqueIPAndHasntPurchased(player);
    }

    public boolean isUniqueIPAndHasntPurchased(Player player) {
        ChristmasPlayer christmasPlayer = getChristmasPlayer(player);
        for (ChristmasPlayer other : getCacheValues()) {
            if (other.getIp().equals(christmasPlayer.getIp()) && other.isHasPurchased()) {
                return true;
            }
        }
        return false;
    }

    public void setAlreadyBought(Player player) {
        getChristmasPlayer(player).setHasPurchased(true);
    }

    public void clearAlreadyBought() {
        for (ChristmasPlayer christmasPlayer : getCacheValues()) {
            christmasPlayer.setHasPurchased(false);
        }
    }

    public ItemStack getCurrentItem() {
        return this.currentItem;
    }

    public void setCurrentItem(ItemStack currentItem) {
        this.currentItem = currentItem;
    }

    public Location getShopLocation() {
        return this.shopLocation;
    }

    public void setShopLocation(Location shopLocation) {
        this.shopLocation = shopLocation;
    }
}
