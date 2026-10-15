package me.ifamasssxd.cosmicbosses.dungeonboss.abilities;

import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.citizensnpcs.nms.v1_8_R3.entity.EntityHumanNPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.concurrent.TimeUnit;

public class EndOfWorld extends DungeonAbility {
    private final int COUNTDOWN_TIME;
    private int secondsCounted;
    int ticks;
    private boolean executed;

    public EndOfWorld(final Boss boss) {
        super(boss);
        this.COUNTDOWN_TIME = (int) TimeUnit.MINUTES.toSeconds(3L);
        this.secondsCounted = 0;
        this.ticks = 0;
        this.executed = false;
    }

    @Override
    public void onTick() {
        ++this.ticks;
        if (this.ticks % 20 == 0 && !this.executed) {
            ++this.secondsCounted;
            try {
                de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), this.getBoss().getEntity().getLocation(), 1f, 1f, 1f, 0.1f, 20);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            this.boss.getEntity().getWorld().getPlayers().forEach(pl -> pl.playSound(pl.getLocation(), Sound.NOTE_PLING, 1.0f, 1.1f));
            if (this.secondsCounted >= this.COUNTDOWN_TIME) {
                this.executed = true;
                Bukkit.getLogger().info("Killing everyone due to End Of World time running out.");
                for (final Player player : ((DungeonYijki) this.boss).getWorldPlayers()) {
                    if (!(player instanceof EntityHumanNPC.PlayerNPC) && !BossUtils.isInGodMode(player) && player.getHealth() > 0.0) {
                        player.setHealth(0.0);
                        Bukkit.getLogger().info("Killing " + player.getName());
                    }
                }
            }
        }
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(300L);
    }

    @Override
    public boolean shouldStopTicking() {
        return this.executed;
    }

    @Override
    public void onAbilityEnable() {
        this.initAbility();
        final DungeonYijki boss = (DungeonYijki) this.boss;
        for (final Player pl : boss.getWorldPlayers()) {
            pl.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "End of The World >> " + ChatColor.RED + "This reality does not exist with out me. I will not die in a place of my own creation. I am deleting this dimension. All your lives will end shortly, along with this world.");
        }
    }

    @Override
    public void onAbilityDisable() {
    }

    @Override
    public void onAbilityEvent(final Event event) {
    }

    @Override
    public String getAbilityName() {
        return "End of World";
    }

    public int getCOUNTDOWN_TIME() {
        return this.COUNTDOWN_TIME;
    }

    public int getSecondsCounted() {
        return this.secondsCounted;
    }
}
