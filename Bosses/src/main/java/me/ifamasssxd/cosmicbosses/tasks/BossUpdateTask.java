package me.ifamasssxd.cosmicbosses.tasks;

import lombok.SneakyThrows;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossDespawnEvent;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

public class BossUpdateTask implements Runnable {
    @SneakyThrows
    public void run() {
        try {
            for (Boss boss : Boss.bosses.values()) {
                if (boss.isBossSpawned()) {
                    boss.onUpdate();
                    continue;
                }
                if (boss.getEntity() != null && boss.getEntity().isValid())
                    continue;
                if (boss.getBossType() == BossType.HOLIDAY_SPIRIT || boss.getBossType() == BossType.DUNGEON_YIJKI || boss.getBossType() == BossType.DEMONIC_WITHER || boss.getBossType() == BossType.EVIL_ABOMINATION || boss.getBossType() == BossType.CHRISTMAS_SPIRIT)
                    boss.cleanup();
                if (boss.getEntity() != null && !boss.getEntity().hasMetadata("killed")) {
                    Bukkit.getPluginManager().callEvent((Event)new BossDespawnEvent((Entity)boss.getEntity(), boss));
                    boss.getEntity().remove();
                }
                Boss.bosses.remove(boss.getUuid());
                System.out.println("Removing invalid boss!");
            }
        } catch (Throwable $ex) {
            throw $ex;
        }
    }
}
