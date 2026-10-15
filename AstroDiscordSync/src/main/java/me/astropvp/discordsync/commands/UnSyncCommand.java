package me.astropvp.discordsync.commands;


import me.astropvp.discordsync.DiscordSyncPlugin;
import me.astropvp.discordsync.managers.SyncManager;
import me.astropvp.discordsync.utils.CC;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

@Command("unsync")
@Alias({"unsyncaccount", "unsyncdiscord"})
public class UnSyncCommand extends CommandBase {
    private DiscordSyncPlugin plugin;
    private SyncManager syncManager;
    private static final long ASTRO_GUILD_ID = 470324147411615744L;
    private final Guild astroDiscord;
    public UnSyncCommand(DiscordSyncPlugin plugin) {
        this.plugin = plugin;
        this.syncManager = plugin.getSyncManager();
        this.astroDiscord = plugin.getJda().getGuildById(ASTRO_GUILD_ID);

    }

    @Default
    public void onCommand(Player player, String toUnsync) {
        if (!player.isOp()) {
            player.sendMessage(CC.translate("&c&l(!)&c Only administrators can unsync your account, open a #general-support ticket in the astropvp /discord!"));
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(toUnsync);
        if (!this.plugin.getDatabaseManager().isConnected()) {
            player.sendMessage(CC.translate("&c&l(!)&c Account Sync is currently unavailable right now, please try again in a few."));
        } else if (!this.syncManager.isAccountLinked(offlinePlayer.getUniqueId())) {
            player.sendMessage((String) CC.translate("&c&l(!) &cTheir account is not synced!"));
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, (Runnable) (() -> {
                this.plugin.getDatabaseManager().remove(offlinePlayer.getUniqueId());
            }));
            player.sendMessage((String) CC.translate("&a&l(!) &fYou have &asuccessfully &funsync'ed " + offlinePlayer.getName() + "'s account"));
            if (offlinePlayer.isOnline()) {
                offlinePlayer.getPlayer().sendMessage(CC.translate("&c&l(!)&c Your account has been &funsync'ed &cby an administrator!"));
            }


            if (syncManager.isAccountLinked(offlinePlayer.getUniqueId())) {
                this.plugin.getJda().getGuildById("470324147411615744").removeRoleFromMember(this.syncManager.getLinkedUser(offlinePlayer.getUniqueId()).getId(), Objects.requireNonNull(this.plugin.getJda().getGuildById("470324147411615744").getRoleById("980116575736696853"))).queue();
            }
        }
    }
}
