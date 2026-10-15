package me.astropvp.discordsync.commands.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

public class WhitelistCommand extends ListenerAdapter {

    private static final String PERMISSION_ROLE_ID = "1165312504289841152"; // Replace with the role ID

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        // Check if the user has the required role ID
        if (event.getMember() == null || event.getMember().getRoles().stream().noneMatch(role -> role.getId().equals(PERMISSION_ROLE_ID))) {
            event.getChannel().sendMessage("You do not have permission to use this command.").queue();
            return;
        }

        if (message.equalsIgnoreCase("!whitelist on")) {
            Bukkit.getServer().setWhitelist(true);
            event.getChannel().sendMessage("Whitelist is now enabled!").queue();
        } else if (message.equalsIgnoreCase("!whitelist off")) {
            Bukkit.getServer().setWhitelist(false);
            event.getChannel().sendMessage("Whitelist is now disabled!").queue();
        }
    }
}

