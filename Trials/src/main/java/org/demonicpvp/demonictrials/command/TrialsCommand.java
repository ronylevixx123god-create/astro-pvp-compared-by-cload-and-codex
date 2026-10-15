package org.demonicpvp.demonictrials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import org.bukkit.entity.Player;
import org.demonicpvp.demonictrials.gui.TrialGui;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;

import java.util.Optional;

@CommandAlias("trials|trial")
public class TrialsCommand extends BaseCommand {
    @Default
    public void onDefaultCommand(Player player) {
        new TrialGui().getInventory().open(player);
    }

    @Subcommand("quit|leave")
    public void onLeaveCommand(Player player) {
        Optional<TrialSession> trial = TrialManager.getTrial(player);

        if (!trial.isPresent()) {
            CommonsUtil.sendMessage(player, "&c&l(!) &cYou are not currently in a trial.");
        } else {
            trial.get().removePlayer(player, TrialSession.TrialPlayerRemoveReason.LEAVE_COMMAND);
        }
    }
}
