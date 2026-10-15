package org.demonicpvp.demonictrials.trial.room;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialTier;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class TrialRoom implements Listener {
    protected TrialSession trial;

    public TrialRoom(TrialSession trial) {
        this.trial = trial;
    }

    protected boolean hasStarted;
    protected boolean hasEnded;

    public abstract String getName();
    public abstract ChatColor getColor();
    public abstract Material getMaterial();
    public abstract List<String> getDescription();

    public String getColoredName() {
        return CommonsUtil.color(this.trial.getCurrentTier().getColor() + "&l" + this.getName());
    }

    public abstract Location getSpawnLocation();
    public abstract Set<Location> getExitPortalLocations();
    public TrialTier getRollUntil() { return TrialTier.DEMONIC; }

    public boolean canDropItems() { return false; }

    public void updateScoreboard(Player player, List<String> lines) {}

    public void reset() {
        this.hasEnded = false;
        this.hasStarted = false;
    }

    protected void sendToolTipAndMessage() {
        this.trial.sendMessage("");
        this.trial.sendMessage(CommonsUtil.getCenteredMessage(this.getColor() + "&l" + this.getName()));

        for (String line : this.getDescription()) {
            this.trial.sendMessage(CommonsUtil.getCenteredMessage("&7" + line));
        }

        this.trial.sendMessage("");

        /*for (Player player : this.trial.getPlayers()) {
            CosmicPacketHandler.sendPacketToPlayer(
                    player,
                    new STooltipPacket(
                            CommonsUtil.color(this.getColor() + "&l" + this.getName()),
                            CommonsUtil.color("&7" + String.join(" ", this.getDescription())),
                            this.getMaterial().getId(),
                            0
                    )
            );
        }*/
    }

    public void start() {
        this.hasStarted = true;
    }

    public void setHasEnded(boolean hasEnded) {
        this.hasEnded = hasEnded;
    }

    public boolean hasStarted() {
        return this.hasStarted;
    }

    public void teleportPlayersToSpawn() {
        for (Player player : this.trial.getPlayers()) {
            this.trial.teleportPlayer(player, this.getSpawnLocation());
        }
    }

    public void onPlayerRemove(Player player) {}
}
