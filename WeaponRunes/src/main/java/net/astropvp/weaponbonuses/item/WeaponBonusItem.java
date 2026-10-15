package net.astropvp.weaponbonuses.item;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.*;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import net.astropvp.weaponbonuses.api.WeaponBonusesAPI;
import net.astropvp.weaponbonuses.bonus.BonusType;
import net.astropvp.weaponbonuses.bonus.BonusWeapon;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WeaponBonusItem extends CosmicItem implements InventoryInteractable {
    private static WeaponBonusItem instance;

    private WeaponBonusItem() {
        super("weapon_bonus");
    }

    public static WeaponBonusItem getInstance() {
        return instance != null ? instance : (instance = new WeaponBonusItem());
    }

    public ItemStack createItem(Player player, Object... objects) {
        int percent = 100;
        List<BonusType> bonuses = Lists.newArrayList();
        Object[] var5 = objects;
        int var6 = objects.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Object object = var5[var7];
            if (object instanceof String) {
                Arrays.stream(BonusType.values()).filter((value) -> value.toString().equals(object.toString())).findAny().ifPresent(bonuses::add);
            }
        }

        Object object = objects[0];
        if (object != null && StringUtils.isAlphanumeric(object.toString())) {
            percent = Integer.parseInt(object.toString());
        }

        List<String> lore = new ArrayList(Arrays.asList("&7Apply this crystal to any", "&7weapon without bonuses to", "&7add the bonuses of this crystal to it!", "", "&6&lBonuses:"));
        bonuses.stream().forEach((bonus) -> {
            lore.add(CC.translate(" &6&l* &7" + bonus.getCrystalName()));
        });
        List<String> stringBonus = Lists.newArrayList();
        bonuses.stream().forEach((bonus) -> {
            stringBonus.add(bonus.getCrystalName());
        });
        ItemStack weaponBonusCrystal = CommonsUtil.addGlow(CommonsUtil.createItem(Material.INK_SACK, "&6&lWeapon Bonus Crystal (&7" + percent + "%&6&l)", 1, 6, lore));
        this.setCosmicNBTData(weaponBonusCrystal, percent, stringBonus);
        return this.applyItemData(weaponBonusCrystal).build();
    }

    private void setCosmicNBTData(ItemStack item, int percent, List<String> stringBonus) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder compoundBuilder = wrapper.getCompoundOrCreate("cosmicData");
        compoundBuilder.setInt("weaponBonusApplyChance", percent);
        compoundBuilder.setString("weaponBonusBonuses", String.join("@", stringBonus));
        wrapper.setNBTBase("cosmicData", compoundBuilder.get());
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CURSOR;
    }

    public void onInventoryClick(Player player, ItemStack itemStack, NBTWrapper cursorWrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent event) throws CosmicException {
        if (clicked != null && clickedWrapper != null && GeneralUtil.isWeapon(clicked)) {
            if (WeaponBonusesAPI.hasWeaponBonuses(clicked)) {
                player.sendMessage("§c§l(!) §cYou must apply this Weapon Bonus Crystal to a weapon without any bonuses on it.");
            } else {
                if (Math.random() < (double)cursorWrapper.getCompound("cosmicData").getInt("weaponBonusApplyChance") / 100.0D) {
                    NBTTagCompoundBuilder comp = clickedWrapper.getCompoundOrCreate("weaponBonusData").setInt("weaponBonusLevel", 1).setInt("weaponBonusExperience", 0).setString("weaponBonuses", cursorWrapper.getCompound("cosmicData").getString("weaponBonusBonuses"));
                    clickedWrapper.setNBTBase("weaponBonusData", comp.get());
                    ItemStack i = clickedWrapper.build();
                    GlintUtils.addGlint(i,  -218112214);
                    (new BonusWeapon(i)).applyVisualChanges("6");
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                    player.sendMessage("§a§l(!) §aYour Weapon Bonus Crystal has successfully applied!");
                } else {
                    player.playSound(player.getLocation(), Sound.LAVA_POP, 1.0F, 1.0F);
                    player.sendMessage("§c§l(!) §cYour Weapon Bonus Crystal failed to apply.");
                }

                if (event.getCursor().getAmount() > 1) {
                    event.getCursor().setAmount(event.getCursor().getAmount() - 1);
                } else {
                    event.setCursor((ItemStack)null);
                }

                event.setCancelled(true);
            }
        }
    }
}
