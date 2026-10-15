package net.astropvp.core.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.HCFOutpostUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astrospawners.utils.ActionBarMessageUtils;
import net.astropvp.core.cachers.LastManStandingCacher;
import net.astropvp.core.enums.CapType;
import net.astropvp.core.utils.StringUtils;
import org.arkhamnetwork.Arkkit.patches.chat_filter.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sun.misc.MessageUtils;

import java.util.concurrent.ThreadLocalRandom;

public class LastManStandingCapTask extends BukkitRunnable {
    private static World world = Bukkit.getWorlds().get(0);

    public void run() {
        for (int i = LastManStandingCacher.getCachers().size() - 1; i >= 0; i--) {
            LastManStandingCacher cacher = LastManStandingCacher.getCachers().get(i);
            Player player = cacher.getPlayer();
            if (player != null &&
                    player.isOnline() && !player.isDead()) {
                try {
                    CapType type = WorldGuardUtils.inRegion(player.getLocation(), "lms_region_desert") ? CapType.DESERT : (WorldGuardUtils.inRegion(player.getLocation(), "lms_region_spawn") ? CapType.SPAWN : null);
                    if (type == null) {
                        LastManStandingCacher.getCachers().remove(i);
                        player.sendMessage(CC.translate("&6&l(&e&l/LMS&6&l) &eYou have left the lms cap zone"));
                        continue;
                    }
                } catch (NullPointerException e) {
                    cacher.remove();
                }
                if (player.isFlying() && !player.isOp()) {
                    cacher.remove();
                    player.sendMessage(String.valueOf(CC.RedB) + "(!) " + CC.Red + "Since you were flying, you lost your LMS Timer.");
                    player.setFlying(false);
                    player.setAllowFlight(false);
                }
            }
            continue;
        }
        LastManStandingCacher cacher2 = LastManStandingCacher.getLongestPlayerAlive(CapType.DESERT);
        LastManStandingCacher other = LastManStandingCacher.getLongestPlayerAlive(CapType.SPAWN);
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        LastManStandingCacher[] cachers = {cacher2, other};
        LastManStandingCacher[] array;
        for (int length = (array = cachers).length, j = 0; j < length; j++) {
            LastManStandingCacher x = array[j];
            if (x != null) {
                ActionBarMessageUtils.sendMessage(x.getPlayer(), "&6&lLMS Timer: &f" + x.getLMSTime() + "s");
                x.add();
                if (x.getLMSTime() % 30 == 0 && x.getLMSTime() != 0) {
                    Player player2 = x.getPlayer();
                    if (Math.random() < 0.5D) {
                        int give = ThreadLocalRandom.current().nextInt(10000, 25000);
                        if (HCFOutpostUtils.doesControlOutpost(player2, "frozen"))
                            give *= 1;
                        player2.sendMessage(String.valueOf(CC.WhiteB) + "+ " + StringUtils.formatWithCommas(give) + "XP " + CC.Gray + "(LMS Reward " + CC.White + "#" + (x.getLMSTime() / 30) + CC.Gray + ")");
                        player2.giveExp(give);
                        x.addExperience(give);
                    } else {
                        double multiplier = 1.0D;
                        if (HCFOutpostUtils.doesControlOutpost(player2, "frozen"))
                            multiplier += 0.25D;
                        int give2 = (int) (multiplier * ThreadLocalRandom.current().nextInt(75000, 500000));
                        player2.sendMessage(String.valueOf(CC.WhiteB) + "+ $" + StringUtils.formatWithCommas(give2) + " " + CC.Gray + "(LMS Reward " + CC.White + "#" + (x.getLMSTime() / 30) + CC.Gray + ")");
                        EconomyUtils.depositBalance((OfflinePlayer) player2, give2);
                        x.addMoney(give2);

                    }
                    if (Math.random() <= 0.125) {
                        Bukkit.broadcastMessage(CC.translate("&6&l(&e&l/LMS&6&l) &f" + player2.getName() + " &egot lucky and received a &6Slotbot Ticket &7(/spawn)&e."));
                        Bukkit.dispatchCommand(console, "slotbot give " + player2.getName() + " 1");
                    }
                }
            }
        }
        for (Player player3 : world.getPlayers()) {
            CapType type2 = WorldGuardUtils.inRegion(player3.getLocation(), "lms_region_desert") ? CapType.DESERT : (WorldGuardUtils.inRegion(player3.getLocation(), "lms_region_spawn") ? CapType.SPAWN : null);
            if (type2 != null && LastManStandingCacher.getPlayer(player3) == null) {
                player3.sendMessage(CC.translate("&6&l(&e&l/LMS&6&l) &eYou have entered the lms cap zone"));
                new LastManStandingCacher(player3, type2);
            }
        }
    }
}
