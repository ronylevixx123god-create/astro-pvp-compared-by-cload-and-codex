package org.demonicpvp.demonictrials.trial.modifier.madness;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.demonicpvp.demonictrials.trial.TrialSession;

public abstract class AbstractMadnessModifier {
    private final TrialSession trial;

    // Since these modifiers need a trial instance, they *CANNOT* do anything
    // that interacts with a trial in their constructor.
    // It should be assumed that trial is passed in as null
    public AbstractMadnessModifier(TrialSession trial) {
        this.trial = trial;
    }

    public abstract String getName();
    public abstract String getDescription();
    public abstract Material getMaterial();

    public TrialSession getTrial() {
        return this.trial;
    }

    public void onTick() {}
    public void onRoomEnd() {}
    public void reset() {}

    protected void sendMessage(Player player, String message) {
        CommonsUtil.sendMessage(player, "&4&l[" + this.getName() + "] &c" + message);
    }

    protected void sendMessage(String message) {
        this.trial.sendMessage("&4&l[" + this.getName() + "] &c" + message);
    }

    protected void sendCooldown(int seconds) {
        for (Player p : this.trial.getPlayers()) {
            /*CosmicPacketHandler.sendPacketToPlayer(p, new SCooldownPacket(
                    "madnessmodifier" + this.getName().toLowerCase(),
                    "DEBUFF",
                    false,
                    CosmicPacketHandler.rgbaToInt(255, 0, 0, 255),
                    "Madness Modifier: " + this.getName(),
                    seconds * 1000,
                    seconds * 1000,
                    this.getMaterial().getId()
            ));*/
        }
    }

    protected void sendInfiniteCooldown() {
        for (Player p : this.trial.getPlayers()) {
            /*CosmicPacketHandler.sendPacketToPlayer(p, new SCooldownPacket(
                    "madnessmodifier" + this.getName().toLowerCase(),
                    "DEBUFF",
                    true,
                    CosmicPacketHandler.rgbaToInt(255, 0, 0, 255),
                    "Madness Modifier: " + this.getName(),
                    0,
                    0,
                    this.getMaterial().getId()
            ));*/
        }
    }

    protected void clearCooldown() {
        for (Player p : this.trial.getPlayers()) {
            /*CosmicPacketHandler.sendPacketToPlayer(p, new SCooldownPacket(
                    "madnessmodifier" + this.getName().toLowerCase(),
                    "DEBUFF",
                    false,
                    CosmicPacketHandler.rgbaToInt(0, 0, 0, 0),
                    "Madness Modifier: " + this.getName(),
                    0,
                    0,
                    this.getMaterial().getId()
            ));*/
        }
    }
}
