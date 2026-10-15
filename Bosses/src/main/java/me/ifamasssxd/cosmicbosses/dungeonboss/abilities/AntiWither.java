package me.ifamasssxd.cosmicbosses.dungeonboss.abilities;

import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.concurrent.TimeUnit;

public class AntiWither extends DungeonAbility {
    long lastTick;

    public AntiWither(final Boss boss) {
        super(boss);
    }

    @Override
    public void onTick() {
        if (this.lastTick == 0L || System.currentTimeMillis() - this.lastTick >= TimeUnit.SECONDS.toMillis(1L)) {
            this.lastTick = System.currentTimeMillis();
            for (final Player player : ((DungeonYijki) this.boss).getWorldPlayers()) {
                if (!player.hasMetadata("NPC") && !player.isDead() && !BossUtils.isInGodMode(player)) {
                    final double damage = 2.0;
                    player.damage(0.0);
                    if (player.getHealth() <= damage) {
                        player.setHealth(0.0);
                    } else {
                        player.setHealth(player.getHealth() - damage);
                    }
                }
            }
        }
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(10L);
    }

    @Override
    public String getAbilityName() {
        return "Anti Wither";
    }

    @Override
    public boolean shouldStopTicking() {
        return System.currentTimeMillis() - this.initTime >= this.getCooldown();
    }

    @Override
    public void onAbilityEnable() {
        this.initAbility();
        this.boss.sendMessage("You will truly feel what its like to be mortal..");
    }

    @Override
    public void onAbilityDisable() {
    }

    @Override
    public void onAbilityEvent(final Event event) {
    }
}
