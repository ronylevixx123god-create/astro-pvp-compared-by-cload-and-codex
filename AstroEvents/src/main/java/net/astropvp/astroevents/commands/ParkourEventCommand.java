package net.astropvp.astroevents.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.game.MiniGameRegister;
import net.astropvp.astroevents.game.MiniGameState;
import net.astropvp.astroevents.game.MiniGameType;
import net.astropvp.astroevents.game.games.ParkourMiniGame;
import net.astropvp.astroevents.utils.Utils;
import net.astropvp.astroevents.utils.configuration.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("parkour")
public class ParkourEventCommand extends BaseCommand {

    private final MiniGameRegister miniGameRegister;
    private final FileConfiguration languageFile;

    public ParkourEventCommand(AstroEvents plugin) {
        this.miniGameRegister = plugin.getMiniGameRegister();
        this.languageFile = plugin.getConfigManager()
                .getConfig(ConfigManager.ConfigFile.LANGUAGE);
    }


    @Subcommand("join")
    @Description("joins the parkour event")
    public void joinParkour(Player sender) {

        if (miniGameRegister.getMiniGame(MiniGameType.PARKOUR) == null) return;

        ParkourMiniGame parkourMiniGame = (ParkourMiniGame) miniGameRegister.getMiniGame(MiniGameType.PARKOUR);

        if (parkourMiniGame.getMiniGameState() == MiniGameState.IN_PROGRESS) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.already-in-progress").replace("{game}", "Parkour")));
            return;
        }

        if (parkourMiniGame.getMiniGameState() == MiniGameState.NOT_STARTED) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.not-started").replace("{game}", "Parkour")));
            return;
        }

        if (parkourMiniGame.isFull()) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.full").replace("{game}", "Parkour")));
            return;
        }

        if (!Utils.isInventoryEmpty(sender)) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.inventory-empty")));
            return;
        }

        parkourMiniGame.joinMiniGame(sender);
        parkourMiniGame.sendMessageToAllParticipants(languageFile.getString("messages.event.player-joined")
                .replace("{game}", "Parkour")
                .replace("{player}", sender.getName())
                .replace("{amount}", String.valueOf(parkourMiniGame.getParticipants().size()))
                .replace("{max}", String.valueOf(parkourMiniGame.getMaxPlayers()))
        );
    }

    @Subcommand("leave")
    @Description("leaves the parkour event")
    public void leaveParkour(Player sender) {
        if (miniGameRegister.getMiniGame(MiniGameType.PARKOUR) == null) return;

        ParkourMiniGame parkourMiniGame = (ParkourMiniGame) miniGameRegister.getMiniGame(MiniGameType.PARKOUR);

        if (!parkourMiniGame.isParticipant(sender)) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.not-participant").replace("{game}", "Parkour")));
            return;
        }

        parkourMiniGame.leaveMiniGame(sender);
        parkourMiniGame.sendMessageToAllParticipants(languageFile.getString("messages.event.player-left")
                .replace("{game}", "Parkour")
                .replace("{player}", sender.getName())
                .replace("{amount}", String.valueOf(parkourMiniGame.getParticipants().size()))
                .replace("{max}", String.valueOf(parkourMiniGame.getMaxPlayers()))
        );
    }

    @Subcommand("checkpoint|cp")
    public void checkpointParkour(Player sender) {
        if (miniGameRegister.getMiniGame(MiniGameType.PARKOUR) == null) return;

        ParkourMiniGame parkourMiniGame = (ParkourMiniGame) miniGameRegister.getMiniGame(MiniGameType.PARKOUR);

        if (!parkourMiniGame.isParticipant(sender)) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.not-participant").replace("{game}", "Parkour")));
            return;
        }

        parkourMiniGame.teleportParticipantToCheckpoint(sender);
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        List<String> helpMessageList =  languageFile.getStringList("messages.parkour-help-message");
        String[] helpMessage = new String[helpMessageList.size()];
        helpMessage = Utils.color(helpMessageList.toArray(helpMessage));
        sender.sendMessage(helpMessage);
    }
}
