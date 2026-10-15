package me.ifamasssxd.cosmicbosses.dungeonboss.planetnul;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BloodMagicAbility extends DungeonAbility {
    public BloodMagicAbility(Boss boss) {
        super(boss);
    }

    private int totalDamageBefore;

    @Override
    public void onTick() {

    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(8L);
    }

    @Override
    public String getAbilityName() {
        return "Blood Magic";
    }

    @Override
    public boolean shouldStopTicking() {
        return System.currentTimeMillis() - this.initTime >= this.getCooldown();
    }

    @Override
    public void onAbilityEnable() {
        this.totalDamageBefore = 0;
        this.initAbility();

        this.boss.sendMessage(CC.translate("&5&lWant to see a magic trick? Go on then, hit me, hit me! &c&l[8s]"));

        for (int dmg : boss.getDamageTracker().values()) {
            this.totalDamageBefore += dmg;
        }
    }

    @Override
    public void onAbilityDisable() {
        int totalDamageAfter = 0;

        for (int dmg : boss.getDamageTracker().values()) {
            totalDamageAfter += dmg;
        }

        int damageDone = totalDamageAfter - this.totalDamageBefore;
        int finalDamage = (int) (Math.min((int) (damageDone * 0.15), 18) * (((CorruptCosmonaut) this.boss).hasFloorBroke() ? 1 : 1.1));

        for (Player player : this.getBoss().getNearbyEntities(150.0, EntityType.PLAYER).stream().map(e -> (Player) e).collect(Collectors.toList())) {
            this.boss.sendMessage(player, CC.translate("&5&lBlood Magic! &c&l[-" + finalDamage + "HP]"));
            player.setHealth(Math.max(player.getHealth() - finalDamage, 0));
        }

        ((CorruptCosmonaut) this.boss).doTrickster();
    }

    @Override
    public void onAbilityEvent(Event p0) {

    }
}
