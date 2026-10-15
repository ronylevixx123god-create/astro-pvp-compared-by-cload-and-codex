package net.astropvp.core.commands;

import net.astropvp.core.AstroCore;
import net.astropvp.core.cooldown.BlessCooldown;
import net.astropvp.core.utils.ChatManager;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Bless implements CommandExecutor {
    private FileConfiguration config;
    private BlessCooldown cooldown = new BlessCooldown();

    public Bless() {
        this.config = AstroCore.getPlugin(AstroCore.class).getConfig();
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        Player player = (Player) sender;
        PlayerPotionEffects.untrackPotionEffect(player, PotionEffectType.BLINDNESS);
        PlayerPotionEffects.untrackPotionEffect(player, PotionEffectType.CONFUSION);
        PlayerPotionEffects.untrackPotionEffect(player, PotionEffectType.HARM);
        PlayerPotionEffects.untrackPotionEffect(player, PotionEffectType.POISON);
        PlayerPotionEffects.untrackPotionEffect(player, PotionEffectType.SLOW);
        PlayerPotionEffects.untrackPotionEffect(player, PotionEffectType.SLOW_DIGGING);
        PlayerPotionEffects.untrackPotionEffect(player, PotionEffectType.WEAKNESS);
        PlayerPotionEffects.untrackPotionEffect(player, PotionEffectType.WITHER);
        player.sendMessage(ChatManager.translate(config.getString("bless.msg")));
        return false;
    }
}
