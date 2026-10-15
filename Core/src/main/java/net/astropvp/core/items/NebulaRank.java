package net.astropvp.core.items;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
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

public class NebulaRank extends CosmicItem implements InventoryInteractable {
    public NebulaRank() {
        super("nebula_rank");
    }

    public ItemStack createItem(Player player, Object ... objects) {
        ItemStack itemStack = this.applyItemData((new ItemBuilder(Material.NETHER_STAR, CC.translate("&d&l✪ &5&l&o&nN&d&l&o&ne&5&l&n&ob&d&l&n&ou&5&l&n&ol&d&l&n&oa&5&l RANK &f&l&oVoucher &d&l✪ &7(Right Click)")
                , CC.Gray + ""
                , CC.translate("&7&nRight-Click&7 to unlock"),
                CC.translate("&7the &5N&de&5b&du&5l&da &7rank on this server!"),
                CC.Gray + "",
                CC.translate("&5&lNebula Perks"),
                CC.translate("&5&l* &dAll Apollo Perks"),
                CC.translate("&5&l* &dx2 Xp Rates"),
                CC.translate("&5&l* &dAccess to Nebula /kit &7(3 Days Cooldown)"),
                CC.translate("&5&l* &dAccess to &f/chatcolor"),
                CC.translate("&5&l* &d999 homes"),
                CC.translate("&5&l* &d99 playervaults"),
                CC.translate("&5&l* &dx3 Fly Boost"),
                CC.translate("&5&l* &dAccess to private giveaways"),
                CC.translate("&5&l* &dAccess to &f/armorcolor"),
                CC.translate("&5&l* &d5% Chance To Keep Lootbox"),
                CC.translate("&5&l* &dNebula Prefix"),
                CC.translate("&5&l* &dAccess To Nebula Shop"),
                CC.translate("&5&l* &dand much more..."),
                "",
                CC.translate("&8[&d✬&8] &5obtained from &f&nbuy.astropvp.net&7 (&n/buy&7)")

        ).build())).build();

        return Utils.addSkin("INTERSTELLAR_ORB", itemStack);
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

        if (!player.getLocation().getWorld().getName().equals("world") && !player.getLocation().getWorld().getName().equals("flat")) {
            throw new PrefixedException("You cannot claim this rank in this world!");
        }

        if (item.getAmount()>1) {
            throw new PrefixedException("You cannot apply multiple ranks at once!");
        }


        if (player.hasPermission("astrocore.nebula")) {
            throw new PrefixedException("You cannot redeem lower tier ranks.");
        }

        NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("conaxData");

        cosmicData.setInt("nebulaRank", 1);
        event.setCancelled(true);
        player.setItemInHand(null);
        wrapper.setNBTBase("conaxData", (NBTBase)cosmicData.get());
        item = wrapper.build();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent set interstellar");
        player.sendMessage("");
        player.sendMessage(CC.translate("&5&l(!)&5 Congratulations on redeeming the Nebula rank!"));
        player.sendMessage(CC.translate("&7You now have access to all the Nebula perks."));
        player.sendMessage("");

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(CC.translate("&8[&6&lAstro&f&lPvP&8] &5" + player.getName() + "&f has unlocked the &5Nebula Rank&f!"));
        Bukkit.broadcastMessage(CC.translate("&7Purchase this rank on &f&nbuy.astropvp.net&7! (/buy)"));
        Bukkit.broadcastMessage("");
    }
}

