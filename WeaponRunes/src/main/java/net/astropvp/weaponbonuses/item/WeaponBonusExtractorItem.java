package net.astropvp.weaponbonuses.item;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import net.astropvp.weaponbonuses.api.WeaponBonusesAPI;
import net.astropvp.weaponbonuses.bonus.BonusWeapon;
import net.astropvp.weaponbonuses.command.WeaponBonusCommand;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponBonusExtractorItem extends CosmicItem implements InventoryInteractable {
    private static WeaponBonusExtractorItem instance;

    private WeaponBonusExtractorItem() {
        super("weapon_bonus_extractor");
    }

    public static WeaponBonusExtractorItem getInstance() {
        return instance != null ? instance : (instance = new WeaponBonusExtractorItem());
    }

    public ItemStack createItem(Player player, Object... objects) {
        List<String> lore = Lists.newArrayList();
        lore.add(CC.translate("&7Drag n Drop this item on a weapon"));
        lore.add(CC.translate("&7that has a &eWeapon Bonus Crystal"));
        lore.add(CC.translate("&7attached onto it."));
        ItemStack item = this.addGlow(CommonsUtil.createItem(Material.GOLD_NUGGET, "&6&lWeapon Bonus Crystal Extractor", lore));
        return this.applyItemData(item).build();
    }

    private ItemStack addGlow(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CURSOR;
    }

    public void onInventoryClick(Player player, ItemStack itemStack, NBTWrapper cursorWrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent event) throws CosmicException {
        if (clicked != null && clickedWrapper != null && GeneralUtil.isWeapon(clicked)) {
            if (!WeaponBonusesAPI.hasWeaponBonuses(clicked)) {
                player.sendMessage(CC.translate("&c&l(!) &cYou use this because this weapon does not have a Weapon Bonus attached."));
            } else if (WeaponBonusCommand.isSkitItem(clicked)) {
                player.sendMessage(CC.translate("&c&l(!) &cYou Cannot Extract the WeaponBonus from an Admin Item Weapon."));
            } else {
                BonusWeapon weapon = new BonusWeapon(clicked);
                List<String> stringBonuses = weapon.getBonuses();
                List<Object> objects = Lists.newArrayList(new Object[]{100});
                stringBonuses.stream().forEach((string) -> {
                    objects.add(string.replace(" ", "_").toUpperCase());
                });
                ItemStack weaponBonusCrystal = CosmicSkyCustomItems.get().getItemManager().getCustomItem("weapon_bonus").createItem(null, objects.toArray());
                player.getInventory().addItem(weaponBonusCrystal);
                List<String> lore = clicked.getItemMeta().getLore();
                this.applyVisuals(clicked, lore, objects);
                ItemMeta meta = clicked.getItemMeta();
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                player.updateInventory();
                player.playSound(player.getLocation(), Sound.ANVIL_USE, 0.75F, 0.75F);
                player.sendMessage(CC.translate("&a&l(!) &aYou have extracted the Weapon Bonus Crystal off of " + clicked.getItemMeta().getDisplayName() + "&a!"));
                if (event.getCursor().getAmount() > 1) {
                    event.getCursor().setAmount(event.getCursor().getAmount() - 1);
                } else {
                    event.setCursor(null);
                }

                event.setCancelled(true);
            }
        }
    }

    private void applyVisuals(ItemStack item, List<String> lore, List<Object> objects) {
        int index = -1;
        int amountOfBonuses = objects.size();

        for(Iterator var6 = lore.iterator(); var6.hasNext(); ++index) {
            String l = (String)var6.next();
            if (l.contains("WEAPON XP:")) {
                break;
            }
        }

        ++index;

        for(int i = index; i > index - 4 - amountOfBonuses; --i) {
            lore.remove(i);
        }

        NBTWrapper wrapper = new NBTWrapper(item);
        wrapper.remove("weaponBonusData");
    }
}
