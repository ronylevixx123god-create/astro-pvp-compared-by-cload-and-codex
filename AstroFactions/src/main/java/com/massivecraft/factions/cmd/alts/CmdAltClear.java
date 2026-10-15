package com.massivecraft.factions.cmd.alts;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.bukkit.Bukkit;

public class CmdAltClear extends FCommand {

    public CmdAltClear() {
        super();
        this.aliases.add("clear");

        this.requirements = new CommandRequirements.Builder(Permission.ALTS).withAction(PermissibleAction.INVITE).memberOnly().build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction faction = context.faction;

        int amountKicked = 0;
        for (FPlayer fPlayer : faction.getFPlayersWhereRole(Role.ALT)) {
            faction.removeFPlayer(fPlayer);
            amountKicked++;
        }
        if (amountKicked == 0) {
            context.fPlayer.sendMessage(CC.translate("&6&lFactions &8⎜ &cError&8: &eThere are no alts in your faction"));
        } else {
            context.fPlayer.sendMessage(CC.translate("&6&lFactions &8⎜ &cError&8: &eRemoved a total of &7" + amountKicked + " &ealts from your faction"));

        }


    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_ALT_OPENUSAGE;
    }
}
