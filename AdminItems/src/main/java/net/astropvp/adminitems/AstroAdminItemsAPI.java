package net.astropvp.adminitems;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.astropvp.adminitems.language.Language;
import net.astropvp.adminitems.struct.AdminItem;
import net.astropvp.adminitems.struct.AdminItemRegister;
import net.astropvp.adminitems.struct.Throwable;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AstroAdminItemsAPI {

    public static boolean isAdminItem(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (itemStack.getType() == Material.AIR) return false;
        NBTWrapper nbtItem = new NBTWrapper(itemStack);

        if (!nbtItem.hasKey("cosmicType")) return false;
        return nbtItem.getString("cosmicType").equals("adminItem");
    }

    public static AdminItem getAdminItemFromItem(ItemStack itemStack) {
        NBTWrapper nbtItem = new NBTWrapper(itemStack);

        NBTTagCompoundBuilder compound = nbtItem.getCompound("cosmicData");
        String adminItemType = compound.getString("adminItemType");

        return AdminItemRegister.getInstance().getAdminItemFromID(adminItemType);
    }

    public static int getUsesFromFromItem(ItemStack itemStack) {
        NBTWrapper nbtItem = new NBTWrapper(itemStack);

        NBTTagCompoundBuilder compound = nbtItem.getCompound("cosmicData");
        return compound.getInt("adminItemUses");
    }



    public static boolean canUseAdminItem(Player player, ItemStack itemStack, String cooldownID, AdminItem adminItem) {
        if (!AstroAdminItemsAPI.getAdminItemFromItem(itemStack).getID().equals(adminItem.getID())) {
            return false;
        }

        if (!Boolean.parseBoolean(Language.enabled.getValue())) {
            player.sendMessage(CC.translate("&c&l(!)&c Partner items are currently disabled."));
            return false;
        }

        if (itemStack.getAmount() > 1) {
            Language.USE_STACKED_ADMIN_ITEM.send(player);
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10f, 1.1f);
            if (adminItem instanceof Throwable) {
                int amount = itemStack.getAmount();
                int uses = AstroAdminItemsAPI.getUsesFromFromItem(itemStack);

                ItemStack item = adminItem.getItem(uses).clone();
                item.setAmount(amount);
                player.setItemInHand(item);
            }
            return false;
        }
        if (CooldownUtils.isCooldownActive(player, cooldownID)) {
            player.sendMessage(CC.translate(Language.ADMIN_ITEM_ABILITY_COOLDOWN.getMessage().replace("%time%", CooldownUtils.getFormattedCooldown(player, cooldownID))));
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10f, 1.1f);
            if (adminItem instanceof Throwable) {
                int uses = AstroAdminItemsAPI.getUsesFromFromItem(itemStack);
                player.setItemInHand(adminItem.getItem(uses));
            }
            return false;
        }


        return true;
    }


    public static void decreaseUses(Player player, AdminItem adminItem) {
            int uses = AstroAdminItemsAPI.getUsesFromFromItem(player.getItemInHand());
            if (uses != -1) {
                if (uses - 1 == 0) {
                    player.setItemInHand(null);
                } else {
                    player.setItemInHand(adminItem.getItem(uses-1));
                }
            }

    }

}
