package org.demonicpvp.demonicruins.task;

import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.EssentialsUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.demonicpvp.demonicruins.mob.MobUtil;

import java.util.List;
import java.util.Optional;

import static org.demonicpvp.demonicruins.listener.MobListener.RUINS_WORLD_NAME;
import static org.demonicpvp.demonicruins.mob.MobUtil.ELITE_GUARDIAN_RUINS_MOB_VALUE;

public class ScoreboardTask extends BukkitRunnable {
    @Override
    public void run() {
        World ruinsWorld = Bukkit.getWorld(RUINS_WORLD_NAME);
        Optional<Entity> eliteGuardian = ruinsWorld.getEntities().stream()
                .filter(e -> MobUtil.isRuinMob(e) && MobUtil.getRuinsMobType(e).equals(ELITE_GUARDIAN_RUINS_MOB_VALUE))
                .findFirst();

        long players = ruinsWorld.getPlayers().stream().filter(p -> !EssentialsUtils.isVanished(p)).count();

        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getWorld().getName().equals(RUINS_WORLD_NAME))
                .filter(p -> WorldGuardUtils.getRegionsIn(p.getLocation()).stream().noneMatch(rg -> rg.contains("ruins")))
                .forEach(player -> {
            //BPlayerBoard board = DemonicScoreboard.getOrCreateBoard(player, CommonsUtil.color("&fCosmic &7⎜ &b&lRuins"));
            List<String> lines = Lists.newArrayList("&7&l&m--------------------");

            lines.add("&b&lPlayer Info");
            lines.add(" &7&l⋆&f Account: &c" + player.getName());
            //lines.add(" &7&l⋆&f Money: &c" + NumberUtils.formatMoney((long) EconomyUtils.getBalance(player)));
            //lines.add(" &7&l⋆&f XP: &c" + NumberUtils.formatMoney(ExperienceUtil.getTotalExperience(player)));

            lines.add("&1");

            lines.add("b&lRuis Info");
            lines.add(" &7&l⋆&f Players: " + players);
            lines.add(" &7&l⋆&f Current Elite Guardian: ");

            if (eliteGuardian.isPresent()) {
                Location location = eliteGuardian.get().getLocation();
                lines.add("   &7&l⋆ &b&l" + location.getBlockX() + "&bx &l" + location.getBlockY() + "&by &l" + location.getBlockZ() + "&bz");
            } else {
                lines.add("   &7&l⋆ &7None");
            }

            lines.add("&7&l&m--------------------&7");

            //board.setAll(lines.stream().map(CommonsUtil::color).toArray(String[]::new));
        });
    }
}
