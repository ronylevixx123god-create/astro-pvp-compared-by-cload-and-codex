package org.arkhamnetwork.Arkkit.patches;

import com.gmail.nossr50.datatypes.skills.SkillType;
import org.arkhamnetwork.Arkkit.patches.McmmoCreditsListener;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class McmmoCredits
        extends ArkPlugin {
    public McmmoCredits(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerListener(new McmmoCreditsListener());
    }

    @Override
    public void disable() {
    }

    public static ItemStack buildMcmmoCreditItem(int credits) {
        return ItemUtils.buildItem(Material.PAPER, (short)0, ChatColor.GOLD + ChatColor.BOLD.toString() + "MCMMO Voucher " + ChatColor.GRAY + "(Right Click)", String.valueOf(ChatColor.GOLD.toString()) + credits + " credit(s)", ChatColor.GRAY + "Use /redeem after depositing voucher.");
    }

    public static ItemStack buildSkillCreditItem(SkillType type, int credits) {
        return ItemUtils.buildItem(Material.PAPER, (short)0, String.valueOf(ChatColor.GOLD.toString()) + ChatColor.BOLD.toString() + "Skill Voucher " + ChatColor.GRAY + "(Right Click)", String.valueOf(ChatColor.GOLD.toString()) + "Skill " + ChatColor.GRAY + type.getName(), String.valueOf(ChatColor.GOLD.toString()) + "+" + credits + " Level(s)", ChatColor.GRAY + "Automatically applies levels on use.");
    }
}
