package me.ifamasssxd.cosmicbosses.dungeonboss.planetnul;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import org.bukkit.event.Event;

import java.util.concurrent.TimeUnit;

import static org.bukkit.Sound.NOTE_PLING;

public class ComboBreakerAbility extends DungeonAbility {
    public ComboBreakerAbility(Boss boss) {
        super(boss);
    }

    @Override
    public void onTick() {

    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(6L);
    }

    @Override
    public String getAbilityName() {
        return "Combo Breaker";
    }

    @Override
    public boolean shouldStopTicking() {
        return System.currentTimeMillis() - this.initTime >= this.getCooldown();
    }

    @Override
    public void onAbilityEnable() {
        this.initAbility();
        this.boss.sendMessage(CC.translate("&5&lCombo Breaking in &c&l1s"), NOTE_PLING, 0.0f);
    }

    @Override
    public void onAbilityDisable() {
        ((CorruptCosmonaut) this.boss).doTrickster();
    }

    @Override
    public void onAbilityEvent(Event p0) {

    }
}
