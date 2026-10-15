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

public class ApolloRank extends CosmicItem
        implements InventoryInteractable {
    public ApolloRank() {
        super("apollo_rank");
    }

    public ItemStack createItem(Player player, Object ... objects) {
        ItemStack itemStack = this.applyItemData((new ItemBuilder(Material.NETHER_STAR, CC.translate("&3&l✪ &b&l&o&nA&f&l&o&np&b&l&n&oo&f&l&n&ol&b&l&n&ol&f&l&n&oo&b&l RANK &f&l&oVoucher &3&l✪ &7(Right Click)")
                , CC.Gray + ""
                , CC.translate("&7&nRight-Click&7 to unlock"),
                CC.translate("&7the &bA&fp&bo&fl&bl&fo &7rank on this server!"),
                CC.Gray + "",
                CC.translate("&b&lApollo Perks"),
                CC.translate("&b&l* &3Apollo &f/kit"),
                CC.translate("&b&l* &350 playervaults"),
                CC.translate("&b&l* &330 homes"),
                CC.translate("&b&l* &320 AH listings"),
                CC.translate("&b&l* &3Apollo Header"),
                CC.translate("&b&l* &3No &f/cf &3tax"),
                CC.translate("&b&l* &3Apollo prefix"),
                CC.translate("&b&l* &3Access to &f/armorcolor"),
                CC.translate("&b&l* &3and much more..."),
                "",
                CC.translate("&8[&3✬&8] &bobtained from &f&nbuy.astropvp.net&7 (&n/buy&7)")

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

        if (!player.getLocation().getWorld().getName().equals("world") && !(player.getLocation().getWorld().getName().equals("flat"))) {
            throw new PrefixedException("You cannot claim this rank in this world!");
        }

        if (item.getAmount()>1) {
            throw new PrefixedException("You cannot apply multiple ranks at once!");
        }


        if (player.hasPermission("astrocore.apollo")) {
            throw new PrefixedException("You cannot redeem lower tier ranks.");
        }

        NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("conaxData");

        cosmicData.setInt("apolloRank", Integer.valueOf(1));
        event.setCancelled(true);
        player.setItemInHand(null);
        wrapper.setNBTBase("conaxData", (NBTBase)cosmicData.get());
        item = wrapper.build();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent set apollo");
        player.sendMessage("");
        player.sendMessage(CC.translate("&b&l(!)&b Congratulations on redeeming the apollo rank!"));
        player.sendMessage(CC.translate("&7You now have access to all the apollo perks."));
        player.sendMessage("");

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(CC.translate("&8[&6&lAstro&f&lPvP&8] &b" + player.getName() + "&f has unlocked the &bApollo Rank&f!"));
        Bukkit.broadcastMessage(CC.translate("&7Purchase this rank on &f&nbuy.astropvp.net&7! (/buy)"));
        Bukkit.broadcastMessage("");
    }
}

