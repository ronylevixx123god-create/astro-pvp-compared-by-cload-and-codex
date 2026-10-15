package me.ifamasssxd.cosmicbosses.dungeonboss.planetnul;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SoulReaperAbility extends DungeonAbility {
    public SoulReaperAbility(Boss boss) {
        super(boss);
    }

    @Override
    public void onTick() {

    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(4);
    }

    @Override
    public String getAbilityName() {
        return "Soul Reaper";
    }

    @Override
    public boolean shouldStopTicking() {
        return System.currentTimeMillis() - this.initTime >= this.getCooldown();
    }

    @Override
    public void onAbilityEnable() {
        this.initAbility();
        this.boss.sendMessage(CC.translate("&5&lBehold, the powers of a true Soul Reaper!"));

        for (Player player : this.getBoss().getNearbyEntities(250.0, EntityType.PLAYER).stream().map(e -> (Player) e).collect(Collectors.toList())) {
            if (KillTrackerAPI.hasSoulAmount(player, 100)) {
                KillTrackerAPI.removeSoulsFromGems(player, 100);
                this.boss.sendMessage(player, CC.translate("&5&lYour souls belong to ME! [&c&l-100 Souls&5&l]"));
            } else {
                this.boss.sendMessage(player, CC.translate("&5&lOh, no souls? I'll take yours then! [&c&l-" + ((player.getMaxHealth() - 1) / 2) + "HP&5&l]"));
                player.setHealth(0.5D);
            }
        }
    }

    @Override
    public void onAbilityDisable() {
        ((CorruptCosmonaut) this.boss).doTrickster();
    }

    @Override
    public void onAbilityEvent(Event p0) {

    }
}
