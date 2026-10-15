package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CmdVault extends FCommand {

    public CmdVault() {
        this.aliases.add("vault");
        this.aliases.add("chest");

        this.requirements = new CommandRequirements.Builder(Permission.VAULT)
                .memberOnly()
                .noDisableOnLock()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        /*
             /f vault <number>
         */

        Player player = context.player;

        if(context.faction.isWilderness())return;

        Inventory inventory = context.faction.getChest();

        player.openInventory(inventory);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_VAULT_DESCRIPTION;
    }
}
