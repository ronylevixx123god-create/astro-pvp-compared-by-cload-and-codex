package me.ifamasssxd.cosmicduel.listeners;

import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.TeamData;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TeamDuelListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
            return;
        Player entity = (Player)event.getEntity();
        Player damager = (Player)event.getDamager();
        NewDuel duel = DuelAPI.getDuel(damager);
        if (duel instanceof me.ifamasssxd.cosmicduel.struct.newduel.TeamDuel &&
                DuelAPI.inSameDuel(damager, entity)) {
            TeamData damagedTeamdata = CosmicDuel.get().getTeamDataManager().tryFindTeam(entity.getUniqueId());
            TeamData damagerTeamData = CosmicDuel.get().getTeamDataManager().tryFindTeam(damager.getUniqueId());
            if (damagerTeamData == damagedTeamdata)
                event.setCancelled(true);
        }
    }
}
