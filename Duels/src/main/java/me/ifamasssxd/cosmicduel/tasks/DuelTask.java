package me.ifamasssxd.cosmicduel.tasks;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import java.util.ArrayList;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.enums.DuelCreateState;
import me.ifamasssxd.cosmicduel.struct.enums.DuelState;
import me.ifamasssxd.cosmicduel.struct.enums.EndReason;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import me.ifamasssxd.cosmicduel.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DuelTask implements Runnable {
    public void run() {
        for (NewDuel d : DuelAPI.duels) {
            if (d.getDuelState() == DuelState.PLAYING && d.getCreateState() == DuelCreateState.DONE) {
                d.setGameTimer(d.getGameTimer() + 1);
                d.setPvpTimer(d.getPvpTimer() - 1);
                if (d.getPvpTimer() <= 0) {
                    d.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Damage has not been taken for 2 minutes.", true);
                    d.sendMessage(ChatColor.RED + "The duel has been cancelled.", true);
                    d.onDuelEnd(EndReason.NO_WINNER, null);
                    continue;
                }

                if (d.getGameTimer() == 300) {
                    d.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "All damage is now " + ChatColor.RED + ChatColor.BOLD
                            .toString() + "2X" + ChatColor.RED + "!", true);
                    continue;
                }
                if (d.getGameTimer() >= 660) {
                    for (Player p : d.getPlayers()) {
                        if (!p.isDead()) {
                            p.damage(0.0D);
                            p.setHealth((PlayerUtils.getHealth(p) < 2.0D) ? 0.0D : (PlayerUtils.getHealth(p) - 2.0D));
                        }
                    }
                    continue;
                }
                if (d.getGameTimer() >= 600)
                    for (Player p : d.getPlayers()) {
                        if (!p.isDead()) {
                            p.damage(0.0D);
                            p.setHealth((PlayerUtils.getHealth(p) < 1.0D) ? 0.0D : (PlayerUtils.getHealth(p) - 1.0D));
                        }
                    }
            }
        }
    }

    public void spawnEnvoy(NewDuel d) {
        for (Player p : d.getPlayers())
            CosmicSpaceChestsAPI.dropChest(p.getLocation().clone().add(0.0D, 10.0D, 0.0D));
    }
}
