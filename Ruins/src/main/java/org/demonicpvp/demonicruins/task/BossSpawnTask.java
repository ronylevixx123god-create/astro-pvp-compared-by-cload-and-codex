package org.demonicpvp.demonicruins.task;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.StringUtils;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.demonicpvp.demonicruins.DemonicRuins;

public class BossSpawnTask extends BukkitRunnable {

    private int seconds;
    public BossSpawnTask() {
        seconds = 10800;
        this.runTaskTimer(DemonicRuins.getInstance(), 20, 20);
    }

    @Override
    public void run() {
        seconds--;
        if(seconds == 60 || seconds == 300 || seconds == 900 || seconds == 1800) {
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&3&lLeviathan Boss &7&o(&7" + (seconds / 60) + " minute" + (seconds == 60 ? "" : "s") + "&7&o)")));
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&7&oA powerful opponent is about to summon...")));
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&7&oFeared by all the cosmonauts in the cosmoverse,")));
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&7&oand responsible for the destruction of")));
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&7&omillions of galaxies.")));
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&7Use &n/ruins&7 if you dare to fight the &3Leviathan&7!")));
            Bukkit.broadcastMessage("");
        }
        else if(seconds <= 0) {
            boolean alreadySpawned = false;
            for(Boss boss : Boss.bosses.values()) {
               /* if(boss.getBossType() != BossType.LEVIATHAN)
                    continue;*/
                alreadySpawned = true;
            }
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&3&lLeviathan Boss &7&o(&7summoned&7&o)")));
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&7&oA powerful opponent is about to summon...")));
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&7&oFeared by all the cosmonauts in the cosmoverse,")));
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&7&oand responsible for the destruction of")));
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&7&omillions of galaxies.")));
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(CC.translate(StringUtils.getCenteredMessage("&7Use &n/ruins&7 if you dare to fight the &3Leviathan&7!")));
            Bukkit.broadcastMessage("");
/*            if(!alreadySpawned)
                BossType.LEVIATHAN.createInstance(new Location(Bukkit.getWorld("world_ruins"), 275.5, 36, -90.5)).onBossSpawn();*/
            this.seconds = 10800;
        }
    }
}
