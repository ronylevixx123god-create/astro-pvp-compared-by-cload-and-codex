package me.astropvp.discordsync.listeners;

import me.astropvp.discordsync.utils.CC;
import me.astropvp.discordsync.DiscordSyncPlugin;
import java.util.UUID;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DiscordListener implements EventListener {
    private final DiscordSyncPlugin plugin;

    public DiscordListener(DiscordSyncPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEvent(GenericEvent genericEvent) {
        if (!(genericEvent instanceof PrivateMessageReceivedEvent)) {
            return;
        }
        PrivateMessageReceivedEvent event = (PrivateMessageReceivedEvent) genericEvent;
        User user = event.getAuthor();
        if (user.isBot()) {
            return;
        }
        String message = event.getMessage().getContentStripped();
        if (!message.startsWith("!verify ")) {
            return;
        }
        final int code;
        try {
            code = Integer.parseInt(message.substring("!verify ".length()).trim());
            if (code < 10000 || code > 99999) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException invalidCode) {
            event.getChannel().sendMessage("You have entered an invalid code!").queue();
            return;
        }
        // Sync codes and Bukkit players belong to the server thread, never the JDA thread.
        Bukkit.getScheduler().runTask(plugin, () -> {
            UUID playerId = plugin.getSyncManager().getActiveSyncMap().get(code);
            if (playerId == null) {
                event.getChannel().sendMessage("You have entered an invalid code!").queue();
                return;
            }
            Player player = Bukkit.getPlayer(playerId);
            if (player == null) {
                event.getChannel().sendMessage("That player isn't online anymore!").queue();
                return;
            }
            // Consume the code before leaving the main thread to prevent concurrent reuse.
            plugin.getSyncManager().getActiveSyncMap().remove(code);
            String playerName = player.getName();
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!plugin.getDatabaseManager().insertLink(playerId, user.getIdLong())) {
                    event.getChannel().sendMessage("Could not link these accounts. They may already be linked; please try /discordsync again.").queue();
                    return;
                }
                event.getChannel().sendMessage("Congrats! You have successfully linked your account to " + playerName + " on AstroPvP.").queue();
                Guild guild = plugin.getJda().getGuildById("470324147411615744");
                if (guild != null) {
                    Member member = guild.getMember(user);
                    Role role = guild.getRoleById("980116575736696853");
                    if (member != null) {
                        member.modifyNickname(playerName).queue();
                        if (role != null) {
                            guild.addRoleToMember(user.getId(), role).queue();
                        }
                    }
                }
                Bukkit.getScheduler().runTask(plugin, () -> {
                    Player online = Bukkit.getPlayer(playerId);
                    if (online != null) {
                        online.sendMessage(CC.translate("&a&l(!)&a You have successfully linked your account to &f&n" + user.getAsTag() + "&a."));
                        online.playSound(online.getLocation(), Sound.ORB_PICKUP, 10, 10);
                    }
                    plugin.getChatListener().updateNitroBoosters();
                });
            });
        });
    }
}
