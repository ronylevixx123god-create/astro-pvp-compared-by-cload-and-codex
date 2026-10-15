package net.astropvp.astroevents.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import net.astropvp.astroevents.AstroEvents;
import net.astropvp.astroevents.game.MiniGameRegister;
import net.astropvp.astroevents.game.MiniGameState;
import net.astropvp.astroevents.game.MiniGameType;
import net.astropvp.astroevents.game.games.SumoMiniGame;
import net.astropvp.astroevents.utils.Utils;
import net.astropvp.astroevents.utils.configuration.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("sumo")
public class SumoEventCommand extends BaseCommand {

    private final MiniGameRegister miniGameRegister;
    private final FileConfiguration languageFile;

    public SumoEventCommand(AstroEvents plugin) {
        this.miniGameRegister = plugin.getMiniGameRegister();
        this.languageFile = plugin.getConfigManager()
                .getConfig(ConfigManager.ConfigFile.LANGUAGE);
    }


    @Subcommand("join")
    @Description("joins the sumo event")
    public void joinSumo(Player sender) {

        if (miniGameRegister.getMiniGame(MiniGameType.SUMO) == null) return;

        SumoMiniGame sumoMiniGame = (SumoMiniGame) miniGameRegister.getMiniGame(MiniGameType.SUMO);

        if (sumoMiniGame.getMiniGameState() == MiniGameState.IN_PROGRESS) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.already-in-progress").replace("{game}", "Sumo")));
            return;
        }

        if (sumoMiniGame.getMiniGameState() == MiniGameState.NOT_STARTED) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.not-started").replace("{game}", "Sumo")));
            return;
        }

        if (sumoMiniGame.isFull()) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.full").replace("{game}", "Sumo")));
            return;
        }

        if (!Utils.isInventoryEmpty(sender)) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.inventory-empty")));
            return;
        }

        sumoMiniGame.joinMiniGame(sender);
        sumoMiniGame.sendMessageToAllParticipants(languageFile.getString("messages.event.player-joined")
                .replace("{game}", "Sumo")
                .replace("{player}", sender.getName())
                .replace("{amount}", String.valueOf(sumoMiniGame.getParticipants().size()))
                .replace("{max}", String.valueOf(sumoMiniGame.getMaxPlayers()))
        );
    }

    @Subcommand("leave")
    @Description("leaves the sumo event")
    public void leaveSumo(Player sender) {
        if (miniGameRegister.getMiniGame(MiniGameType.SUMO) == null) return;

        SumoMiniGame sumoMiniGame = (SumoMiniGame) miniGameRegister.getMiniGame(MiniGameType.SUMO);

        if (!sumoMiniGame.isParticipant(sender) && !sumoMiniGame.isSpectator(sender)) {
            sender.sendMessage(Utils.color(languageFile.getString("messages.event.not-participant").replace("{game}", "Sumo")));
            return;
        }

        sumoMiniGame.leaveMiniGame(sender);
        sumoMiniGame.sendMessageToAllParticipants(languageFile.getString("messages.event.player-left")
                .replace("{game}", "Sumo")
                .replace("{player}", sender.getName())
                .replace("{amount}", String.valueOf(sumoMiniGame.getParticipants().size()))
                .replace("{max}", String.valueOf(sumoMiniGame.getMaxPlayers()))
        );
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        List<String> helpMessageList =  languageFile.getStringList("messages.sumo-help-message");
        String[] helpMessage = new String[helpMessageList.size()];
        helpMessage = Utils.color(helpMessageList.toArray(helpMessage));
        sender.sendMessage(helpMessage);
    }
}
