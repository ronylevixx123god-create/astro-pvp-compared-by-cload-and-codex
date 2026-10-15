package net.crankedpvp.pvptop.listeners;

import com.astropvp.cc.player.CosmicPlayers;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.massivecraft.factions.FPlayers;
import net.crankedpvp.pvptop.PvPTopAPI;
import net.crankedpvp.pvptop.PvPTopPlugin;
import net.crankedpvp.pvptop.data.FactionData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final PvPTopPlugin plugin;
    public PlayerDeathListener(PvPTopPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handlePlayerDeath(PlayerDeathEvent event) {
        if(event.getEntity() == null) return;
        if(event.getEntity().getKiller() == null) return;

        Player attacker = event.getEntity().getKiller();
        Player victim = event.getEntity();

        if(FPlayers.getInstance().getByPlayer(victim).hasFaction()) {
            if(CosmicPlayers.get().getCosmicPlayer(victim.getUniqueId()).isOnCosmicClient()) {
                PvPTopAPI.getFactionData(victim).addPoint(FactionData.Points.DEATH);
                return;
            }
            PvPTopAPI.getFactionData(victim).addPoint(FactionData.Points.DEATH);
            PvPTopAPI.getFactionData(victim).addPoint(FactionData.Points.DEATH);
            victim.sendMessage(CC.translate(PvPTopAPI.PREFIX + "&fYou have received &c-2 &ffaction points because you are not on &dCosmic Client"));

        }
        if(FPlayers.getInstance().getByPlayer(attacker).hasFaction()) {
            if(CosmicPlayers.get().getCosmicPlayer(attacker.getUniqueId()).isOnCosmicClient()) {
                PvPTopAPI.getFactionData(attacker).addPoint(FactionData.Points.KILL);
            }
        }
    }
}
