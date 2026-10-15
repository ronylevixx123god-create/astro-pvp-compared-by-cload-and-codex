package net.astropvp.adminitems.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.PVPUtils;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import lombok.Getter;
import net.astropvp.adminitems.AdminItemsPlugin;
import net.astropvp.adminitems.language.Language;
import net.astropvp.adminitems.managers.AdminItemAbilityManager;


import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

@Getter
public class TimeTravelerTask extends BukkitRunnable {
    private AdminItemAbilityManager abilityManager = AdminItemsPlugin.getInstance().getAbilityManager();
    private int duration;
    private Hologram hologram;
    private Player player;
    private Location spawnLocation;
    private ParticleBuilder particle;

    public TimeTravelerTask(int duration, Hologram hologram, Player player, Location spawnLocation) {
        this.duration = duration;
        this.hologram = hologram;
        this.player = player;
        this.spawnLocation = spawnLocation;
        this.particle = new ParticleBuilder(ParticleEffect.PORTAL, player.getLocation())
                .setAmount(200)
                .setOffsetY(1.5f)
                .setOffsetX(1.5f)
                .setOffsetZ(1.5f)
                .setSpeed(0.05f);
    }

    @Override
    public void run() {
        if (player == null) {
            hologram.delete();
            abilityManager.getTimeTravellers().remove(player.getUniqueId());
            this.cancel();
            return;
        }
        if (!player.isOnline()) {
            hologram.delete();
            abilityManager.getTimeTravellers().remove(player.getUniqueId());
            this.cancel();
            return;
        }
        if (duration == 0) {
            Language.TIME_TRAVEL_ENDED.send(player);
            player.teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.UNKNOWN);
            player.playSound(player.getLocation(), Sound.PORTAL_TRIGGER, 10f, 1.4f);
            hologram.delete();
            abilityManager.getTimeTravellers().remove(player.getUniqueId());
            this.cancel();
            return;
        }

        updateHologram(CC.translate("&d&l * &fTime: &d" + duration + "s &7left"));
        this.particle.setLocation(player.getLocation()).display(PVPUtils.getNearbyPlayers(player, 30));
        player.sendMessage(CC.translate(Language.TIME_TRAVEL_TIME.getMessage().replace("%time%", String.valueOf(duration))));
        duration--;

    }

    private void updateHologram(String newText) {
            final TextLine textLine = (TextLine)this.hologram.getLine(1);
                textLine.setText(newText);
            }
}