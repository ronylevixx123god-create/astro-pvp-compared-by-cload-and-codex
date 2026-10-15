package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.bukkit.ChatColor;

public class CmdStealth extends FCommand {

    public CmdStealth() {
        super();
        this.aliases.add("stealth");

        this.requirements = new CommandRequirements.Builder(Permission.STEALTH)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        FPlayer fPlayer = context.fPlayer;

        fPlayer.setStealth(!fPlayer.isStealth());
        fPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&a!&8) &7Stealth mode has been " + (fPlayer.isStealth() ? "&aenabled" : "&cdisabled")));
    }


    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_STEALTH_DESCRIPTION;
    }
}
