package me.astropvp.astrospawners.xpboosters;

import com.compilex.CompilexPermissions.PermissionsAPI;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import me.ily2bb.slotbot.utils.Utils;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Booster extends CosmicItem
        implements InventoryInteractable {
    public Booster() {
        super("booster");
    }

    public ItemStack createItem(Player player, Object ... objects) {
        boolean isXpBooster = false;
        double multiplier = 0;
        int durationInMinutes = 0;
        ItemStack itemStack;
        if (objects[0].equals("xp")) {
            isXpBooster = true;
        }

        multiplier = Double.parseDouble(objects[1].toString());
        durationInMinutes = Integer.parseInt(objects[2].toString());


        if (isXpBooster) {

        }

        return null;
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CLICKED;
    }

    public void onInventoryClick(Player player, ItemStack cursor, NBTWrapper wrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent e) throws CosmicException {

    }

    public void onItemUse(Player player, ItemStack item, NBTWrapper wrapper, PlayerInteractEvent event) throws CosmicException {
        if (item == null) {
            return;
        }
        if (wrapper == null) {
            return;
        }
        if (item.getType() == Material.AIR) {
            return;
        }

        if (item.getType() != Material.NETHER_STAR) {
            return;
        }

        if (!player.getLocation().getWorld().getName().equals("world") && !(player.getLocation().getWorld().getName().equals("flat"))) {
            throw new PrefixedException("You cannot claim this rank in this world!");
        }

        if (item.getAmount()>1) {
            throw new PrefixedException("You cannot apply multiple ranks at once!");
        }

        String rank = PermissionsAPI.getPermissionUser(player.getUniqueId()).getGroup();

        if (!rank.equalsIgnoreCase("astro") && !rank.equalsIgnoreCase("astro+") && !rank.equalsIgnoreCase("media")) {
            throw new PrefixedException("You cannot redeem lower tier ranks.");
        }

        NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("conaxData");

        cosmicData.setInt("apolloRank", Integer.valueOf(1));
        event.setCancelled(true);
        player.setItemInHand(null);
        wrapper.setNBTBase("conaxData", (NBTBase)cosmicData.get());
        item = wrapper.build();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " group add apollo");
        player.sendMessage("");
        player.sendMessage(CC.translate("&b&l(!)&b Congratulations on redeeming the apollo rank!"));
        player.sendMessage(CC.translate("&7You now have access to all the apollo perks."));
        player.sendMessage("");

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(CC.translate("&8[&a&lAstro&e&lPvP&8] &b" + player.getName() + "&f has unlocked the &bApollo Rank&f!"));
        Bukkit.broadcastMessage(CC.translate("&7Purchase this rank on &f&nbuy.astropvp.net&7! (/buy)"));
        Bukkit.broadcastMessage("");
    }
}

