package net.astropvp.core.items;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.github.paperspigot.Title;

import java.util.Random;

public class RogueScratchCard extends CosmicItem
        implements InventoryInteractable {
    public RogueScratchCard() {
        super("rogue_scratchcard");
    }

    public ItemStack createItem(Player player, Object ... objects) {
        return this.applyItemData((new ItemBuilder(Material.PAPER, CC.translate("&c&lScratchCard &7(Right-Click)")
                , CC.Gray + ""
                , CC.translate("&7&nRight-Click&7 to have a chance"),
                CC.translate("&7to unlock a reward from this scratchcard!"),
                CC.translate(""),
                CC.translate("&c&l * &cReward: &fRogue Book"),
                CC.translate("&c&l * &cChance: &f0% - 100%"),
                CC.translate("&c&l * &cLevel: &fI - III")
        ).build())).build();
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CLICKED;
    }

    public static ItemStack getRogueSratchCard() {
        return new ItemBuilder(Material.PAPER, CC.translate("&c&lScratchCard &7(Right-Click)")
                , CC.Gray + ""
                , CC.translate("&7&nRight-Click&7 to have a chance"),
                CC.translate("&7to unlock a reward from this scratchcard!"),
                CC.translate(""),
                CC.translate("&c&l * &cReward: &fRogue Book"),
                CC.translate("&c&l * &cChance: &f0% - 100%"),
                CC.translate("&c&l * &cLevel: &fI - III")
        ).build();
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

        if (item.getType() != Material.PAPER) {
            return;
        }

        if (!(player.getWorld().getName().equals("world")) && !(player.getWorld().getName().equals("flat"))) {
            throw new PrefixedException("You cannot claim this rank in this world!");
        }


        NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("conaxData");

        cosmicData.setInt("rogue_scratchcard", Integer.valueOf(1));
        event.setCancelled(true);
        wrapper.setNBTBase("conaxData", (NBTBase)cosmicData.get());
        item = wrapper.build();

        if(player.getItemInHand().getAmount() > 1) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
        } else {
            player.setItemInHand((ItemStack) null);
        }

        ItemStack book = EnchantmentBook.getEnchantmentBook(EnchantmentAPI.getEnchantment("Rogue"), generateRandomNumberOneToThree(), generateRandomNumberOneToHundred(), generateRandomNumberOneToHundred());


        if (Math.random() < 0.15) {
            player.sendMessage(CC.translate("&a&l(!) &aYou have Won! You have received the Reward!"));
            player.sendTitle(new Title(CC.translate("&a&lYou won!"), CC.translate("&7You have received the reward."),10,20,10));
            player.getInventory().addItem(book);
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 10,10);

        } else {
            player.sendTitle(new Title(CC.translate("&c&lYou Lost!"), CC.translate("&7Better Luck Next Time."),10,20,10));
            player.sendMessage(CC.translate("&c&l(!) &cYou have Lost!"));
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 10,10);
        }

    }

    public int generateRandomNumberOneToThree() {
        Random rand = new Random();
        return rand.nextInt((3 - 1) + 1) + 1;

    }

    public int generateRandomNumberOneToHundred() {
        Random rand = new Random();
        return rand.nextInt((100 - 1) + 1) + 1;

    }
}

