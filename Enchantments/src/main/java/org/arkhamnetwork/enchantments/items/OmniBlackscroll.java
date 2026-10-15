package org.arkhamnetwork.enchantments.items;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class OmniBlackscroll extends CosmicItem implements InventoryInteractable {
    private static Map<UUID, OmniBlackscrollItem> modifiedItems;

    public OmniBlackscroll() {
        super("omni_black_scroll");
        modifiedItems = new HashMap<>();
    }

    public ItemStack createItem(Player player, Object... objects) {


        return this.applyItemData((new ItemBuilder(Material.INK_SACK, CC.translate("&b&k:&3&k:&3&l Omni &3&k:&b&k:&7 &f&lBlack Scroll")
                , CC.translate("&7Removes a random enchantment of")
                , CC.translate("&3&lYOUR&7 choice at 100% success.")).build())).build();

    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return (inventoryItemType == InventoryItemType.CURSOR);
    }

    public void onInventoryClick(Player player, ItemStack cursor, NBTWrapper wrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent e) throws CosmicException {
        if (clicked == null || clicked.getType() == Material.AIR || clickedWrapper == null) {
            return;
        }
        if (!GeneralUtil.isArmor(clicked) && !GeneralUtil.isWeapon(clicked)) {
            player.sendMessage(CC.translate("&c&l(!) &cYou can only apply omni blackscrolls to armor or weapons!"));
            return;
        }
        Map<CustomEnchantment, Integer> enchants = EnchantmentAPI.getEnchantments(clicked);
        if (enchants.keySet().isEmpty()) {
            player.sendMessage(CC.translate("&c&l(!) &cThis item does not have any enchantments on it!"));
            return;
        }
        if (e.getCursor().getAmount() > 1) {
            player.sendMessage(CC.translate("&c&l(!) &cYou cannot apply stacked omni black scrolls."));
            return;
        }
        if (e.getInventory().getName().equalsIgnoreCase(CC.translate("&8Select an enchantment to remove.")))
            return;

        NBTTagCompoundBuilder cosmicData = clickedWrapper.getCompoundOrCreate("conaxData");

        cosmicData.setInt("omniBlackScrpmm", Integer.valueOf(1));
        e.setCancelled(true);
        clickedWrapper.setNBTBase("conaxData", (NBTBase)cosmicData.get());
        clicked = clickedWrapper.build();

        List<ItemStack> items = Lists.newArrayList();
        for (CustomEnchantment enchant : enchants.keySet()) {
            final int level = enchants.get(enchant);
            if (level > enchant.getMaxLevel()) {
                continue;
            }
            ItemStack enchantmentBook = EnchantmentBook.getEnchantmentBook(enchant, enchant.getMaxLevel(), 100, 0);
            NBTWrapper enchantmentBookWrapper = new NBTWrapper(enchantmentBook);
            NBTTagCompoundBuilder compound = enchantmentBookWrapper.getCompoundOrCreate("EnchantmentData");
            compound.setString("enchantName", enchant.name());
            enchantmentBookWrapper.setNBTBase("EnchantmentData", (NBTBase) compound.get());
            enchantmentBook = enchantmentBookWrapper.build();
            items.add(enchantmentBook);
        }
        player.setMetadata("omniBlackScrollEdit", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), player.getName()));
        modifiedItems.put(player.getUniqueId(), new OmniBlackscrollItem(clicked, e.getSlot(), player));
        player.playSound(player.getLocation(), Sound.NOTE_BASS, 0.5F, 0.5F);
        e.setCursor(null);
        e.setCancelled(true);
        //player.getInventory().remove(clicked);
        player.openInventory(getInventory(items));
    }

    public void onItemUse(Player player, ItemStack item, NBTWrapper wrapper, PlayerInteractEvent event) throws CosmicException {
        event.setCancelled(true);
    }

    private Inventory getInventory(List<ItemStack> enchantmentBooks) {
        Inventory inventory = Bukkit.createInventory(null, 27, CC.translate("&8Select an enchantment to remove."));
        for (ItemStack item : enchantmentBooks) {
            inventory.addItem(item);
        }
        fillInventory(inventory);
        return inventory;
    }

    private void fillInventory(Inventory inventory) {
        ItemStack pane = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) DyeColor.BLACK.getData())).name(CC.translate("&7")).build();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR)
                inventory.setItem(i, pane);
        }
    }

    public static String getEnchantmentName(ItemStack item) {
        return (new NBTWrapper(item)).getCompoundOrCreate("EnchantmentData").getString("enchantName");
    }

    public static Map<UUID, OmniBlackscrollItem> getModifiedItems() {
        return modifiedItems;
    }
}
