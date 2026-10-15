package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.config.file.MainConfig;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.SpiralTask;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.Triplet;

import java.util.*;

public class CmdRaidClaim  extends FCommand {

    public CmdRaidClaim() {
        super();
        this.aliases.add("raidclaim");
        this.aliases.add("rclaim");
        this.aliases.add("rc");

        this.requirements = new CommandRequirements.Builder(Permission.CLAIM)
                .playerOnly()
                .build();
    }

    public static final Map<UUID, Triplet<Integer, Faction, Long>> attemptingBypass = new HashMap<>();

    @Override
    public void perform(final CommandContext context) {
        int radius = FactionsPlugin.getInstance().conf().commands().raidClaim().getRaidClaimRadius();
        Faction forFaction = context.faction;

        if (!Permission.CLAIM_RADIUS.has(context.sender, false)) {
            context.msg(TL.COMMAND_CLAIM_DENIED);
            return;
        }

        FLocation fLocation = new FLocation(context.player);

        context.msg(TL.COMMAND_CLAIM_RADIUS, radius, fLocation.getChunk().getX(), fLocation.getChunk().getZ(), forFaction.describeTo(context.fPlayer, true));

        final Faction loopFac = forFaction;

        new SpiralTask(fLocation, radius) {
            private int failCount = 0;
            private final int limit = FactionsPlugin.getInstance().conf().factions().claims().getRadiusClaimFailureLimit() - 1;

            private int claimed = 0;

            @Override
            public boolean work() {
                boolean success = context.fPlayer.attemptClaim(loopFac, this.currentFLocation(), true, this);
                if (success) {
                    failCount = 0;
                    claimed++;
                } else return failCount++ < limit;

                return true;
            }

            @Override
            public void finish() {
                Set<FPlayer> informTheseFPlayers = new HashSet<>(loopFac.getFPlayersWhereOnline(true));
                informTheseFPlayers.remove(context.fPlayer);
                for (FPlayer fp : informTheseFPlayers) {
                    fp.msg(TL.CLAIM_CLAIMEDAMOUNT, context.fPlayer.describeTo(fp, true), claimed, loopFac.describeTo(fp));
                }

                context.fPlayer.msg(TL.CLAIM_CLAIMEDAMOUNT, context.fPlayer.describeTo(context.fPlayer, true), claimed, loopFac.describeTo(context.fPlayer));
                super.finish();
            }
        };

    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CLAIM_DESCRIPTION;
    }

}
