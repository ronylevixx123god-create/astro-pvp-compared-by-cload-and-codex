package me.astropvp.discordsync.commands.discord;

import me.astropvp.discordsync.DiscordSyncPlugin;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.events.GenericEvent;
import org.bukkit.Bukkit;

public class WhitelistCommand implements EventListener {
    private static final String PERMISSION_ROLE_ID = "1165312504289841152";
    private final DiscordSyncPlugin plugin;

    public WhitelistCommand(DiscordSyncPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof MessageReceivedEvent) {
            onMessageReceived((MessageReceivedEvent) event);
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        String message = event.getMessage().getContentRaw().trim();
        if (!message.equalsIgnoreCase("!whitelist on") && !message.equalsIgnoreCase("!whitelist off")) {
            return;
        }
        if (event.getMember() == null || event.getMember().getRoles().stream()
                .noneMatch(role -> role.getId().equals(PERMISSION_ROLE_ID))) {
            event.getChannel().sendMessage("You do not have permission to use this command.").queue();
            return;
        }
        boolean enabled = message.equalsIgnoreCase("!whitelist on");
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getServer().setWhitelist(enabled);
            event.getChannel().sendMessage("Whitelist is now " + (enabled ? "enabled!" : "disabled!")).queue();
        });
    }
}
