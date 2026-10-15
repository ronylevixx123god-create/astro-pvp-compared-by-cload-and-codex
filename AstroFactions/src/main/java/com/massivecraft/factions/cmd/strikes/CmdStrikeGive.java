package com.massivecraft.factions.cmd.strikes;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;

public class CmdStrikeGive extends FCommand {
    public CmdStrikeGive(){
        super();
        this.aliases.add("give");
        this.aliases.add("add");

        this.requiredArgs.add("faction");

        this.requiredArgs.add("reason");

        this.requirements = new CommandRequirements.Builder(Permission.STRIKE_MANAGE).noErrorOnManyArgs().build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction toStrike = context.argAsFaction(0);

        if(toStrike == null){
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 1; i < context.args.size(); i++){
            stringBuilder.append(context.args.get(i)).append(" ");
        }

        String trim = stringBuilder.toString().trim();
        toStrike.addStrike(trim, System.currentTimeMillis());

        if(context.fPlayer != null)context.fPlayer.msg(TL.COMMAND_STRIKE_GIVE_SUCCESS, toStrike.describeTo(context.fPlayer), trim);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_STRIKE_GIVE_USAGE;
    }
}
