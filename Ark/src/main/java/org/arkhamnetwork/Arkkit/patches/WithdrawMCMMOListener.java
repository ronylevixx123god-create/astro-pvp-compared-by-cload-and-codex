package org.arkhamnetwork.Arkkit.patches;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.AbilityType;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityEvent;
import com.gmail.nossr50.events.skills.unarmed.McMMOPlayerDisarmEvent;
import com.gmail.nossr50.util.player.UserManager;
import java.text.DecimalFormat;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.patches.WithdrawMCMMO;
import org.arkhamnetwork.Arkkit.patches.WithdrawMCMMOCommand;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

final class WithdrawMCMMOListener
        implements Listener {
    private static DecimalFormat format = new DecimalFormat("#,###");
    private final String xpGainReason = "UNKNOWN";

    WithdrawMCMMOListener() {
    }

    @EventHandler
    public void onPlayerUseSkillCredit(PlayerInteractEvent e) {
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            long itemID;
            int xpValue = WithdrawMCMMO.getMCMMOXPCreditValue(e.getItem());
            SkillType type = WithdrawMCMMO.getMCMMOXPCreditType(e.getItem());
            if (xpValue <= 0) {
                return;
            }
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            e.setUseInteractedBlock(Event.Result.DENY);
            if (ExperienceAPI.getLevel((Player)e.getPlayer(), (String)type.name()) >= ExperienceAPI.getLevelCap((String)type.name())) {
                e.getPlayer().sendMessage(String.valueOf(ChatColor.RED.toString()) + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot add more EXP to " + type.name() + ", as it would exceed the level cap.");
                return;
            }
            if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning() && (itemID = ItemID.getItemID(e.getItem())) != -1L) {
                ItemID.blacklistIID(itemID, "mcMMOLevelRedeem");
            }
            WithdrawMCMMOCommand.addXPAndLevel(e.getPlayer(), type.name(), xpValue);
            e.getPlayer().setItemInHand(null);
            e.getPlayer().updateInventory();
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ORB_PICKUP, 1.0f, 0.33f);
            if (ExperienceAPI.getLevel((Player)e.getPlayer(), (String)type.name()) < ExperienceAPI.getLevelCap((String)type.name())) {
                e.getPlayer().sendMessage(String.valueOf(ChatColor.GREEN.toString()) + ChatColor.BOLD.toString() + "+" + format.format(xpValue) + " " + type.name() + " XP!");
            } else {
                e.getPlayer().sendMessage(String.valueOf(ChatColor.RED.toString()) + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You added more EXP to " + type.name() + ", than the max level cap, your level was adjusted to " + ExperienceAPI.getLevelCap((String)type.name()));
                int extraXp = this.getXPAboveMaxLevel(e.getPlayer(), type.name());
                ExperienceAPI.setLevel((Player)e.getPlayer(), (String)type.name(), (int)ExperienceAPI.getLevelCap((String)type.name()));
                ExperienceAPI.setXP((Player)e.getPlayer(), (String)type.name(), (int)0);
                if (e.getPlayer().getInventory().firstEmpty() != -1) {
                    e.getPlayer().getInventory().addItem(new ItemStack[]{WithdrawMCMMO.getMCMMOXpCert(extraXp, type.name(), e.getPlayer().getName())});
                } else {
                    e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), WithdrawMCMMO.getMCMMOXpCert(extraXp, type.name(), e.getPlayer().getName()));
                }
            }

            try {
                PlayerProfile pp = UserManager.getPlayer((Player) e.getPlayer()).getProfile();
                pp.changed = true;
                pp.scheduleAsyncSave();
            } catch(IllegalAccessError ex) {

                }
        }
    }

    @EventHandler
    public void onMcMMOPlayerDisarm(McMMOPlayerDisarmEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onMcMMOPlayerSerrated(McMMOPlayerAbilityActivateEvent e) {
        if (e.getAbility() == AbilityType.SERRATED_STRIKES || e.getAbility() == AbilityType.SKULL_SPLITTER) e.setCancelled(true);
    }

    private int getXPAboveMaxLevel(Player p, String skillType) {
        int level = ExperienceAPI.getLevel((Player)p, (String)skillType);
        int maxLevel = ExperienceAPI.getLevelCap((String)skillType);
        int extraXp = 0;
        while (level > maxLevel) {
            extraXp += ExperienceAPI.getXpNeededToLevel((int)(--level));
        }
        return extraXp + ExperienceAPI.getXP((Player)p, (String)skillType);
    }
}
