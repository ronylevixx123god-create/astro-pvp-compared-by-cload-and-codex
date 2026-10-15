package org.arkhamnetwork.enchantments;

import com.cosmicpvp.cosmicutils.utils.UpgradeUtils;
import com.rit.sucy.EnchantmentAPI;
import de.inventivegames.particle.ParticleEffect;
import lombok.SneakyThrows;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.enchantments.cmds.CommandEnchDust;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class SoulModeTask implements Runnable {

    public SoulModeTask() {
    }

    @SneakyThrows
    public void run() {
        if (!CommandEnchDust.soulEnabled)
            return;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (KillTrackerAPI.hasGodMode(p)) {
                boolean KOTH = p.getWorld().getName().equals("world_koth");
                if (!KillTrackerAPI.hasSoulAmount(p, KOTH ? 8 : 4)) {
                    KillTrackerAPI.toggleGodMode(p);
                    p.sendMessage("");
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "** SOUL MODE: " + ChatColor.UNDERLINE + "OFF" + ChatColor.RED.toString() + ChatColor.BOLD.toString() + " **");
                    p.sendMessage(ChatColor.GRAY + "You have no soul gems left!");
                    p.sendMessage("");
                    p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 1.0F, 1.2F);
                } else {
                    int cost = KOTH ? (getSoulCostPerSecond(p) * 2) : getSoulCostPerSecond(p);
                    if (cost > 0) {
                        KillTrackerAPI.removeSoulsFromGems(p, cost);
                        p.setMetadata("lastSoulRemoveEvent", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), System.currentTimeMillis()));
                        if (!p.hasMetadata("spectator"))
                            //ParticleEffect.playEffect(ParticleEffect.SPELL, p.getLocation().add(0.0D, 1.0D, 0.0D), 0.5F, 65);
                            //ParticleEffect.SPELL.sendToPlayer(p.getPlayer(),p.getLocation(), 0,0, 0,1.5f,10);
                        p.playSound(p.getLocation(), Sound.EAT, 0.4F, 0.2F);
                        if (KillTrackerAPI.getAllSouls(p) % 100 == 0)
                            p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "** SOULS: " + ChatColor.UNDERLINE + KillTrackerAPI.getAllSouls(p) + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + " **");
                    } else if (!p.hasMetadata("spectator")) {
                        //ParticleEffect.playEffect(ParticleEffect.ENCHANTMENT_TABLE, p.getLocation().add(0.0D, 1.0D, 0.0D), 1.5F, 80);
                        //de.inventivegames.particle.ParticleEffect.ENCHANTMENT_TABLE.sendToPlayer(p.getPlayer(),p.getLocation(), 0,0, 0,1.5f,10);
                    }
                }
            }
        }
    }

    private int getSoulCostPerSecond(Player p) {
        int x = 0;
        ItemStack hand = p.getItemInHand();
        if (hand != null) {
            if (EnchantmentAPI.itemHasEnchantment(hand, "Divine Immolation"))
                x += 5;
            if (EnchantmentAPI.itemHasEnchantment(hand, "Soul Trap"))
                x += 2;
            if (EnchantmentAPI.itemHasEnchantment(hand, "Rogue"))
                x += 2;
            if (EnchantmentAPI.itemHasEnchantment(hand, "Hero Killer"))
                x++;
            if (EnchantmentAPI.itemHasEnchantment(hand, "Sabotage"))
                x += 2;
        }
        return x;
    }
}
