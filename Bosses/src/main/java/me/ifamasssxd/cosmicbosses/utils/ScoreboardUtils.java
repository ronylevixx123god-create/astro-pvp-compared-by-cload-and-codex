package me.ifamasssxd.cosmicbosses.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragon;
import me.ifamasssxd.cosmicbosses.bosses.yijki.Yijki;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.dungeonboss.abilities.EndOfWorld;
import me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijki;
import me.ifamasssxd.cosmicbosses.dungeonboss.planetnul.CorruptCosmonaut;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ScoreboardUtils {
    private static HashMap<UUID, ObjectiveWrapper> wrappers;
    private static DecimalFormat format;

    public static void setupBossScoreboard(final Player p, final Boss boss) {
        p.setMetadata("boss_scoreboard", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30L))));
        final Scoreboard sb = p.getScoreboard();
        if (sb.getObjective("boss") != null) {
            sb.getObjective("boss").unregister();
        }
        final Objective objective = sb.registerNewObjective("boss", "dummy");
        objective.setDisplayName(boss.getDifficulty().getColor() + ChatColor.BOLD.toString() + boss.getName());
        objective.getScore(ChatColor.RED.toString()).setScore(15);
        objective.getScore(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Health").setScore(14);
        objective.getScore(ChatColor.RED.toString() + Math.ceil(boss.getEntity().getHealth())).setScore(13);
        objective.getScore(ChatColor.WHITE.toString()).setScore(12);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void clearBossScoreboard(final Player p) {
        final Scoreboard sb = p.getScoreboard();
        if (sb.getObjective("boss") != null) {
            sb.getObjective("boss").unregister();
        }
        p.removeMetadata("boss_scoreboard", (Plugin) CosmicBosses.getInstance());
        ScoreboardUtils.wrappers.remove(p.getUniqueId());
    }

    public static void updateBossScoreboard(final Player p, final Boss boss) { }

    static {
        ScoreboardUtils.wrappers = new HashMap<UUID, ObjectiveWrapper>();
        ScoreboardUtils.format = new DecimalFormat("###.#");
    }

    public static class ObjectiveWrapper {
        private Objective objective;
        private Scoreboard scoreboard;
        private HashMap<Integer, String> lines;
        private String bossName;

        public ObjectiveWrapper(final Objective obj, final Scoreboard sb, final String bossName) {
            this.lines = new HashMap<Integer, String>();
            this.objective = obj;
            this.scoreboard = sb;
            this.bossName = bossName;
        }

        public String getString(final int slot) {
            if (this.lines.containsKey(slot)) {
                return this.lines.get(slot);
            }
            return null;
        }

        public void clear() {
            this.lines.values().forEach(line -> this.scoreboard.resetScores(line));
            this.lines.clear();
        }

        public void remove(final int number) {
            final String line = this.lines.get(number);
            if (line != null) {
                this.scoreboard.resetScores(line);
            }
            this.lines.remove(number);
        }

        public void setScore(final int score, final String name) {
            final String current = this.getString(score);
            this.objective = this.scoreboard.getObjective("boss");
            if (current != null) {
                System.out.println();
                this.scoreboard.resetScores(current);
            }
            if (this.objective != null) {
                this.objective.getScore(name).setScore(score);
                this.lines.put(score, name);
            }
        }

        public Scoreboard getScoreboard() {
            return this.scoreboard;
        }

        public void setScoreboard(final Scoreboard scoreboard) {
            this.scoreboard = scoreboard;
        }

        public String getBossName() {
            return this.bossName;
        }

        public void setBossName(final String bossName) {
            this.bossName = bossName;
        }
    }
}
