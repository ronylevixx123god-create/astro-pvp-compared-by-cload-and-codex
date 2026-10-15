package net.astropvp.astroevents.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.game.MiniGame;
import net.astropvp.astroevents.game.MiniGameRegister;
import net.astropvp.astroevents.game.MiniGameType;
import net.astropvp.astroevents.utils.Utils;
import net.astropvp.astroevents.utils.configuration.ConfigManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@CommandAlias("aevent|aevents|adminevent|adminevents")
@CommandPermission("astro.staff")
public class AdminEventsCommand extends BaseCommand {

    private final MiniGameRegister miniGameRegister;
    private final FileConfiguration languageFile;
    private final AstroEvents plugin;

    public AdminEventsCommand(AstroEvents plugin) {
        this.plugin = plugin;
        this.miniGameRegister = plugin.getMiniGameRegister();
        this.languageFile = plugin.getConfigManager().getConfig(ConfigManager.ConfigFile.LANGUAGE);
    }

    @Subcommand("start")
    @Description("starts an event")
    public void startEvent(CommandSender sender, String eventName) {
        if (Arrays.stream(MiniGameType.values()).noneMatch(miniGameType -> miniGameType.name().equals(eventName.toUpperCase(Locale.ROOT)))) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.invalid-event-name")));
            return;
        }

        MiniGameType miniGameType = MiniGameType.valueOf(eventName.toUpperCase(Locale.ROOT));
        this.miniGameRegister.getMiniGame(miniGameType).startMiniGameCountDown();
    }

    @Subcommand("stop")
    @Description("stops an event")
    public void stopEvent(CommandSender sender, String eventName) {
        if (Arrays.stream(MiniGameType.values()).noneMatch(miniGameType -> miniGameType.name().equals(eventName.toUpperCase(Locale.ROOT)))) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.invalid-event-name")));
            return;
        }

        MiniGameType miniGameType = MiniGameType.valueOf(eventName.toUpperCase(Locale.ROOT));
        miniGameRegister.getMiniGame(miniGameType).stopMiniGame();
    }

    @Subcommand("list")
    @Description("list of event")
    public void listEvent(CommandSender sender) {
        sender.sendMessage(Utils.color(languageFile.getString("messages.event.list-header")));

        Arrays.stream(MiniGameType.values()).forEach(game -> {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.list-format").replace("{event}", Utils.toNiceString(game.name()))));
        });
    }

    @Subcommand("info")
    public void eventInformation(CommandSender sender, String eventName) {
        if (Arrays.stream(MiniGameType.values()).noneMatch(miniGameType -> miniGameType.name().equals(eventName.toUpperCase(Locale.ROOT)))) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.invalid-event-name")));
            return;
        }

        MiniGameType miniGameType = MiniGameType.valueOf(eventName.toUpperCase(Locale.ROOT));
        MiniGame<?> miniGame = miniGameRegister.getMiniGame(miniGameType);
        Location spawnLocation = miniGame.getSpawnLocation();
        sender.sendMessage(Utils.color(
                "\n" +
                      "&a* &7Minigame &8- &a" + Utils.toNiceString(miniGameType.name()) + "\n" +
                      "&a* &7World name: &a" + miniGame.getGameWorld().getName() + "\n" +
                      "&a* &7Spawn location: &7X: &a" + spawnLocation.getX() + " &7Y: &a" + spawnLocation.getY() + " &7Z: &a" + spawnLocation.getZ() + "\n" +
                      "&a* &7Event state: &a" + Utils.toNiceString(miniGame.getMiniGameState().name()) + "\n" +
                      "&a* &7Min/Max players: &a" + miniGame.getMinPlayers()  + "&8/&a" + miniGame.getMaxPlayers() + "\n" +
                      "&a* &7Amount of participants: &a" + miniGame.getParticipants().size() + "\n"
        ));
    }

    @Subcommand("reload")
    @Description("reload events")
    public void reloadEvent(CommandSender sender) {
        this.plugin.getConfigManager().reloadFiles();
        this.miniGameRegister.reloadMiniGames();
        this.plugin.getCommands().forEach(baseCommand -> this.plugin.getCommandManager().unregisterCommand(baseCommand));
        this.plugin.initNewCommands();
        this.plugin.getCommands().forEach(baseCommand -> this.plugin.getCommandManager().registerCommand(baseCommand));
        sender.sendMessage(Utils.color("&aReload Successful"));
        sender.sendMessage(Utils.color("&cWarning: &7Reloading the events will interrupt any ongoing events"));
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        List<String> helpMessageList =  languageFile.getStringList("messages.events-help-message");
        String[] helpMessage = new String[helpMessageList.size()];
        helpMessage = Utils.color(helpMessageList.toArray(helpMessage));
        sender.sendMessage(helpMessage);
    }
}
