package net.astropvp.armorsets.items;

import com.cosmicpvp.cosmicutils.compat.ASound;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagListBuilder;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicskycustoms.exceptions.ItemDupedException;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.antiMkit.AntiMkit;
import net.astropvp.armorsets.utils.Pair;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AntiMKitCrystalItem extends CosmicItem implements InventoryInteractable {
    public AntiMKitCrystalItem() {
        super("anti_mkit");
    }

    public ItemStack createItem(Player player, Object... objects) {
        if (objects == null || objects.length < 2)
            return null;
        Integer success = NumberUtils.parseIntOrNull(objects[0].toString());
        if (success == null)
            success = Integer.valueOf(1);
        Set<AntiMkit> set = new HashSet<>();
        for (int i = 1; i < objects.length; i++) {
            Object object = objects[i];
            AntiMkit armorSet = null;
            if (object instanceof AntiMkit) {
                armorSet = (AntiMkit)object;
            } else if (object instanceof Set) {
                set = (Set<AntiMkit>)object;
            } else { try {
                armorSet = AstroArmorSetsAPI.mkitFromName(object.toString());
            } catch(NullPointerException ignored) {

            }
            }
            if (armorSet != null)
                set.add(armorSet);
        }
        List<String> lore =
                Lists.newArrayList(String.valueOf(String.valueOf(CC.Green)) + success + "% Success Rate",
                        CC.Gray + "Apply to any piece of armor",
                        CC.Gray + "to negate the following",
                        CC.Gray + "custom Mastery Enchantments:");
        NBTTagListBuilder list = new NBTTagListBuilder();
        for (AntiMkit found : set) {
            if (set.size() > 1)
                lore.add(found.getDisplayName());
            list.addString(found.name());
            for (CustomEnchantment ench : EnchantmentAPI.getEnchantments()) {
                if (ench instanceof MasteryCustomEnchantment &&
                        found.name().equals(((MasteryCustomEnchantment)ench).getArmorSet()))
                    lore.add(CC.GoldB + " * " + CC.DarkRed + ench.name());
            }
        }
        String name = null;
        if (set.size() > 1) {
            StringBuilder builder = new StringBuilder();
            for (AntiMkit found2 : set)
                builder.append(found2.getFirstDisplayNameLetter()).append(CC.Gold).append(", ");
            String combined = builder.toString();
            if (combined.endsWith(", "))
                combined = combined.substring(0, combined.length() - 2);
            name = CC.GoldB + "Multi Anti M-Kit Crystal (" + combined + CC.GoldB + ")";
        } else if (!set.isEmpty()) {
            name = CC.GoldB + "Anti M-Kit Crystal (" + set.stream().findFirst().orElse(null).getDisplayName() + CC.GoldB + ")";
        }
        if (name == null)
            return null;
        return
                applyItemData((new ItemBuilder(Material.INK_SACK, name)).lore(lore)
                        .data(DyeColor.YELLOW.getDyeData()).build())
                        .setNBTBase("cosmicData",
                                (new NBTTagCompoundBuilder()).setNBTBase("mKits", list.build()).setInt("success", success).get())
                        .assignIID().build();
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return (inventoryItemType == InventoryItemType.CURSOR);
    }

    public void onInventoryClick(Player player, ItemStack itemStack, NBTWrapper nbtWrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent inventoryClickEvent) throws CosmicException {
        if (!GeneralUtil.isArmor(clicked))
            return;
        Pair<Set<AntiMkit>, Integer> crystal = AstroArmorSetsAPI.getFromAntiMkitCrystal(nbtWrapper);
        if (crystal != null) {
            if (!AstroArmorSetsAPI.getAntiMkitCrystal(clickedWrapper).isEmpty())
                throw new PrefixedException(
                        "You can not apply more than 1 Anti M-Kit Crystal to a single piece of gear!");
            if (nbtWrapper.isBlacklistedIID())
                throw new ItemDupedException("Anti M-Kit Crystal already used!");
            inventoryClickEvent.setCancelled(true);
            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            } else {
                player.setItemOnCursor(null);
            }
            nbtWrapper.blacklistIID("anti m-kit player: " + player.getName());
            if (Math.random() > crystal.second * 0.01D) {
                player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 3.0F, 1.1F);
                ASound.GLASS.play(player, 3.0F, 1.1F);
                player.updateInventory();
                throw new PrefixedException(String.valueOf(ItemUtils.getDisplayName(itemStack)) + CC.Red +
                        " fails to apply and has been destroyed!");
            }
            inventoryClickEvent.setCurrentItem(applyToItem(clickedWrapper, clicked, crystal));
            ASound.LEVEL_UP.play(player, 3.0F, 1.1F);
            player.sendMessage(CC.GoldB + "(!) " + CC.Gold + ItemUtils.getDisplayName(itemStack) +
                    CC.Gold + " has been applied to " + ItemUtils.getDisplayName(clicked) + CC.Gold + "!");
            player.sendMessage(String.valueOf(CC.Gray) +
                    "While equipped you will be immune to any Mastery Enchantments associated with that /mkit(s)!");
            player.updateInventory();
        }
    }

    private ItemStack applyToItem(NBTWrapper wrapper, ItemStack item, Pair<Set<AntiMkit>, Integer> pair) {
        NBTTagCompoundBuilder builder = wrapper.getCompoundOrCreate("cosmicData");
        NBTTagListBuilder list = new NBTTagListBuilder();
        Set<AntiMkit> set = pair.first;
        StringBuilder loreBuild = new StringBuilder();
        for (AntiMkit armor : set) {
            list.addString(armor.name());
            loreBuild.append(armor.getDisplayName()).append(CC.Gray).append(", ");
        }
        builder.setNBTBase("antiMkits", list.build());
        wrapper.setNBTBase("cosmicData", builder.get());
        item = wrapper.build();
        ItemMeta im = item.getItemMeta();
        List<String> lore = im.getLore();
        if (lore == null)
            lore = Lists.newArrayList();
        String str = loreBuild.toString();
        if (str.endsWith(", "))
            str = str.substring(0, str.length() - 2);
        lore.add(CC.GoldB + "Anti M-Kit Crystal (" + str + CC.GoldB + ")");
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }
}

