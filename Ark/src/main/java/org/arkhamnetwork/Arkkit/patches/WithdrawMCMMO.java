package org.arkhamnetwork.Arkkit.patches;

import com.gmail.nossr50.datatypes.skills.SkillType;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class WithdrawMCMMO extends ArkPlugin {
    private static DecimalFormat format = new DecimalFormat("#,###");

    public WithdrawMCMMO(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerListener(new WithdrawMCMMOListener());
        this.registerCommand(new WithdrawMCMMOCommand("withdrawmcmmo", "/<command>", "Converts your vanilla levels into a tradeable XP bottle.", new ArrayList<String>(Arrays.asList("withdrawskill", "mcmmocredit", "withdrawmcmmoxp"))));
        this.registerCommand(new MCMMOXPCommand("mcmmoxp", "/<command>", "Displays total exp values of specified mcMMO skill.", new ArrayList<String>(Arrays.asList("mxp", "mmoxp"))));
    }

    @Override
    public void disable() {
    }

    public static ItemStack getMCMMOXpCert(int xpAmount, String skillType, String trainer) {
        return ItemUtils.buildItem(Material.PAPER, (short)0, String.valueOf(ChatColor.GOLD.toString()) + ChatColor.BOLD.toString() + "MCMMO XP Voucher " + ChatColor.GRAY + "(Right Click)", String.valueOf(ChatColor.GOLD.toString()) + "Skill " + ChatColor.GRAY + skillType, String.valueOf(ChatColor.GOLD.toString()) + "+" + format.format(xpAmount) + ChatColor.GRAY + " XP", String.valueOf(ChatColor.GOLD.toString()) + "Trainer " + ChatColor.GRAY + trainer, ChatColor.GRAY + "Applies mcMMO experience on use.");
    }

    protected static int getMCMMOXPCreditValue(ItemStack is) {
        if (is.getType() == Material.PAPER && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().startsWith(String.valueOf(ChatColor.GOLD.toString()) + ChatColor.BOLD.toString() + "MCMMO XP Voucher")) {
            return Integer.parseInt(ChatColor.stripColor((String)((String)is.getItemMeta().getLore().get(1)).split(" ")[0].replace("+", "").replaceAll(",", ""))) * is.getAmount();
        }
        return 0;
    }

    protected static SkillType getMCMMOXPCreditType(ItemStack is) {
        if (is.getType() == Material.PAPER && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + ChatColor.BOLD.toString() + "MCMMO XP Voucher")) {
            return SkillType.valueOf((String)ChatColor.stripColor((String)((String)is.getItemMeta().getLore().get(0)).split(" ")[1]).toUpperCase());
        }
        return null;
    }
}
