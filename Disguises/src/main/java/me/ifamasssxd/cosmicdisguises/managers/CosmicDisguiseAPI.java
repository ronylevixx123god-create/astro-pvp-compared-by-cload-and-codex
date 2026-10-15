package me.ifamasssxd.cosmicdisguises.managers;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CosmicDisguiseAPI {
    public static ItemStack createRedeemableDisguise(String disguiseName) {
        try {
            return CosmicDisguiseAPI.createRedeemableDisguise(DisguiseType.valueOf(disguiseName.toUpperCase().replace(" ", "_")));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack createRedeemableDisguise(DisguiseType type) {
        ItemStack item = new ItemBuilder(Material.BONE, CC.GreenB + "Player Disguise \"" + CC.Green + StringUtils.capitaliseAllWords(type.name().toLowerCase().replace("_", " ")) + CC.GreenB + "\" " + CC.Gray + "(Right Click)", "", CC.Gray + "Click to unlock this cosmetic /disguise!").build();
        if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
            item = ItemID.assignItemID(null, item);
        }
        return item;
    }
}

