package org.demonicpvp.demonictrials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.MadnessModifier;

import java.util.Optional;

@CommandAlias("admintrial|admintrials|at|atrial")
@CommandPermission("demonictrials.admin")
public class AdminTrialsCommand extends BaseCommand {
    public static boolean TRIAL_PORTAL_PLACEMENT_ENABLED = true;

    @Default
    public void onDefault(Player player) {
        CommonsUtil.sendMessage(player, Lists.newArrayList(
                "&4&lDemonic Admin Trials",
                "&4/atrial complete &7- Completes the current room",
                "&4/atrial addmadnessmodifier <modifier> &7- Adds a madness modifier to the trial",
                "&4/atrial reload&7 - Reload the trials loot table",
                "&4/atrial list&7 - List trial players",
                "&4/atrial enable&7 - Enables trial portal placement",
                "&4/atrial disable&7 - Disables trial portal placement"
        ));
    }

    @Subcommand("complete")
    public void onCompleteCommand(Player player) {
        Optional<TrialSession> trialSession = TrialManager.getTrial(player);

        if (trialSession.isPresent()) {
            trialSession.get().onRoomComplete();
            CommonsUtil.sendMessage(player, "&a&l(!) &aSuccess");
        } else {
            Optional<TrialSession> first = TrialManager.getTrials().stream().filter(t -> t.getWorld().getName().equals(player.getWorld().getName())).findFirst();

            if (first.isPresent()) {
                first.get().onRoomComplete();
                CommonsUtil.sendMessage(player, "&a&l(!) &aSuccess");
            } else {
                CommonsUtil.sendMessage(player, "&c&l(!) &cYou are not in a trial!");
            }
        }
    }

    @Subcommand("addmadnessmodifier")
    public void onCompleteCommand(Player player, String madnessModifier) {
        Optional<TrialSession> trialSession = TrialManager.getTrial(player);

        if (trialSession.isPresent()) {
            try {
                MadnessModifier modifier = MadnessModifier.valueOf(madnessModifier.toUpperCase());

                if (trialSession.get().hasMadnessModifier(modifier)) {
                    CommonsUtil.sendMessage(player, "&c&l(!) &cThis trial already has this madness modifier!");
                } else {
                    trialSession.get().addMadnessModifier(modifier);
                    CommonsUtil.sendMessage(player, "&a&l(!) &aSuccess");
                }
            } catch (IllegalArgumentException ignored) {
                CommonsUtil.sendMessage(player, "&c&l(!) &cInvalid modifier name.");
            }
        } else {
            CommonsUtil.sendMessage(player, "&c&l(!) &cYou are not in a trial!");
        }
    }

    @Subcommand("reload")
    public void onReloadCommand(CommandSender sender) {
        CommonsUtil.sendMessage(sender, "&a&l(!) &aSuccess");
    }

    @Subcommand("list")
    public void onListCommand(CommandSender sender) {
        for (TrialSession trial : TrialManager.getTrials()) {
            trial.getPlayers().stream().findFirst().ifPresent(p -> {
                CommonsUtil.sendMessage(sender, "&a- &f" + p.getName());
            });
        }
    }

    @Subcommand("disable")
    public void onDisableCommand(CommandSender sender) {
        TRIAL_PORTAL_PLACEMENT_ENABLED = false;
        CommonsUtil.sendMessage(sender, "&c&l(!) &cTrial portal placement disabled.");
    }

    @Subcommand("enable")
    public void onEnableCommand(CommandSender sender) {
        TRIAL_PORTAL_PLACEMENT_ENABLED = true;
        CommonsUtil.sendMessage(sender, "&a&l(!) &aTrial portal placement enabled.");
    }
}
