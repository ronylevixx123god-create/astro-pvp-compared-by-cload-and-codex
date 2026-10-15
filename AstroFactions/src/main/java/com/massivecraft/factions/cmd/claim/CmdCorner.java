package com.massivecraft.factions.cmd.claim;


import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.bukkit.Location;

public class CmdCorner extends FCommand {
    public CmdCorner(){
        this.aliases.add("claimcorner");
        this.aliases.add("corner");

        this.requirements = new CommandRequirements.Builder(Permission.CLAIM_CORNER)
                .withAction(PermissibleAction.CLAIM_CORNER)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        FPlayer fPlayer = context.fPlayer;
        FLocation fLocation = fPlayer.getFLocation();

        if(!FactionsPlugin.getInstance().isSOTW() && !FactionsPlugin.getInstance().conf().corner().onlyAllowInSOTW()){
            fPlayer.msg(TL.COMMAND_CORNER_ISNTSOTW);
            return;
        }

        FLocation.EnumCorner corner = fLocation.getCorner();

        if(corner == null){
            fPlayer.msg(TL.COMMAND_CORNER_MUSTBEINCORNER);
            return;
        }

        if(fPlayer.getRelationToLocation() != Relation.MEMBER && !Board.getInstance().getFactionAt(new FLocation(fPlayer)).isWilderness()){
            fPlayer.msg(TL.COMMAND_CORNER_MUSTOWN);
            return;
        }

        int xStart = corner == FLocation.EnumCorner.NEG_NEG || corner == FLocation.EnumCorner.NEG_POS ? 0 : -FactionsPlugin.getInstance().conf().corner().diameterClaim();
        int zStart = corner == FLocation.EnumCorner.NEG_NEG || corner == FLocation.EnumCorner.POS_NEG ? 0 : -FactionsPlugin.getInstance().conf().corner().diameterClaim();
        int xEnd = xStart + FactionsPlugin.getInstance().conf().corner().diameterClaim();
        int zEnd = zStart + FactionsPlugin.getInstance().conf().corner().diameterClaim();

        Location location = fPlayer.getPlayer().getLocation();
        int myCX = location.getBlockX() >> 4;
        int myCZ = location.getBlockZ() >> 4;

        for(int x = xStart; x <= xEnd; x++){
            for(int z = zStart; z <= zEnd; z++){
                Board.getInstance().setFactionAt(fPlayer.getFaction(), new FLocation(fPlayer.getPlayer().getWorld().getName(), myCX + x, myCZ + z));
            }
        }

        fPlayer.msg(TL.COMMAND_CORNER_SUCCESS);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CORNER_DESCRIPTION;
    }
}
