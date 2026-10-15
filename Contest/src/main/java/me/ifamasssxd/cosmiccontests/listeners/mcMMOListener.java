package me.ifamasssxd.cosmiccontests.listeners;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.struct.ContestManager;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.contests.mcMMOSkillContest;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class mcMMOListener
implements Listener {
    /*@EventHandler
    public void onPLayerGainXP(McMMOPlayerXpGainEvent event) {
        mcMMOSkillContest contest;
        ContestManager manager = CosmicContests.get().getContestManager();
        if (manager.getActiveContests().contains((Object)Contests.MCMMO_SKILL) && (contest = (mcMMOSkillContest)Contests.MCMMO_SKILL.getContest()).getSkillName().equalsIgnoreCase(event.getSkill().name())) {
            ContestUser user = ContestUser.getUser(event.getPlayer().getUniqueId());
            if (user == null) {
                return;
            }
            contest.onPlayerGainXP(user, event.getSkill().name(), event.getRawXpGained());
        }
    }*/
}

