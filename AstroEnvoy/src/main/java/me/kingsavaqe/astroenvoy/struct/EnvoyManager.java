package me.kingsavaqe.astroenvoy.struct;

import com.cosmicpvp.cosmicutils.utils.BlockUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import lombok.Getter;
import lombok.Setter;

import me.kingsavaqe.astroenvoy.EnvoyPlugin;
import me.kingsavaqe.astroenvoy.language.Language;
import me.kingsavaqe.astroenvoy.tasks.FireworkTask;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class EnvoyManager implements PlaceholderReplacer {

    @Getter
    private static String prefix = "&d&lEnvoys &8➥ ";

    private final EnvoyPlugin plugin;

    @Getter
    private long lastEnvoyTime;

    @Getter @Setter
    private int remainingCrates = 0;

    @Getter @Setter
    private boolean active;

    private FireworkTask fireworkTask;

    @Getter @Setter
    private List<Location> locations = new ArrayList<>();

    @Getter @Setter
    private List<Location> currentChests = new ArrayList<>();

    public EnvoyManager(EnvoyPlugin plugin) {
        this.plugin = plugin;
        this.active = false;
        this.updateLastEnvoyTime();
    }

    public void handleCrateSpawning() {
        List<Location> locations = new ArrayList<>(getLocations());
        List<Location> finalLocations = new ArrayList<>();
        this.currentChests = new ArrayList<>();
        int crates = Bukkit.getOnlinePlayers().size() > 15 ? Bukkit.getOnlinePlayers().size() / 6 : 7;
        for(int i = 0; i < crates; i++) {
            Location location = locations.get(ThreadLocalRandom.current().nextInt(0, locations.size() - 1));
            locations.remove(location);
            finalLocations.add(location);
        }
        this.remainingCrates = finalLocations.size();
        this.currentChests = finalLocations;
        finalLocations.stream()
                .filter(location -> location.getBlock().getType() == Material.AIR)
                .collect(Collectors.toList())
                .forEach(crateLocation -> {
                    //crateLocation.getBlock().setType(Material.CHEST);
                    BlockUtils.setBlockFast(crateLocation.getBlock(), Material.CHEST.getId(), (byte)0, false);
                    ArmorStand armorStand = (ArmorStand)crateLocation.getWorld().spawnEntity(crateLocation.clone().add(0.5D, 0.1D, 0.5D), EntityType.ARMOR_STAND);
                    armorStand.setCustomName(CC.translate("&6✧ &e&lEnvoy Crate &6✧"));
                    armorStand.setCustomNameVisible(true);
                    armorStand.setGravity(false);
                    armorStand.setVisible(false);
                    armorStand.setSmall(true);
                    Firework firework = crateLocation.getWorld().spawn(crateLocation.clone().add(0.5D, 0.0D, 0.5D), Firework.class);
                    FireworkMeta fireworkMeta = firework.getFireworkMeta();
                    fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.WHITE)
                            .with(FireworkEffect.Type.BALL_LARGE).flicker(true).withTrail().build());
                    firework.setFireworkMeta(fireworkMeta);
                    fireworkMeta.setPower(5);
                    Bukkit.getScheduler().runTaskLater(EnvoyPlugin.getInstance(), firework::detonate, 20L);
                });
        this.fireworkTask = new FireworkTask();
        fireworkTask.runTaskTimer(plugin, 0, 5*20);
    }

    public void handleCrateRemoving() {
        this.locations.stream()
                .filter(Objects::nonNull)
                .filter(location -> location.getBlock().getType() == Material.CHEST)
                .forEach(this::removeCrateAt);
        this.currentChests.clear();
    }

    public void removeCrateAt(Location location) {
        //location.getBlock().setType(Material.AIR);
        BlockUtils.setAir(location.getBlock(), false);
        removeNearbyHologram(location);
        this.currentChests.remove(location);
    }

    public void removeNearbyHologram(Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, 0.3D, 0.6D, 0.3D)) {
            if (!(entity instanceof ArmorStand))
                continue;
            entity.remove();
        }
    }

    public void endEnvoyEvent() {
        this.setActive(false);
        this.plugin.getServer().getOnlinePlayers()
                .stream()
                .filter(Objects::nonNull) //TODO filter people with envoy messages enabled
                .forEach(player -> player.sendMessage(CC.translate(EnvoyManager.prefix + "&cThe previous envoy event has ended")));
        this.handleCrateRemoving();
        this.fireworkTask.cancel();
        this.fireworkTask = null;
        //this.plugin.getServer().getScheduler().runTaskLater(this.plugin, this::startEnvoyEvent, 20L * 60 * ThreadLocalRandom.current().nextInt(25, 35));
    }

    public void startEnvoyEvent() {
        if(this.isActive()) {
            System.out.println("[Envoy] Stopping previous envoy to start a new one");
            endEnvoyEvent();
        }

        this.updateLastEnvoyTime();

        this.setActive(true);
        this.handleCrateSpawning();
        this.plugin.getServer().getOnlinePlayers()
                .stream()
                .filter(Objects::nonNull) //TODO filter people with envoy messages enabled
                .forEach(player -> sendEnvoyStartMessage(player, this.remainingCrates));
    }

    private void sendEnvoyStartMessage(Player player, int chests) {
        for (String line : Language.ENVOY_STARTED.getValuesAsList()) {
            line = line.replace("{chests}", String.valueOf(chests));
            player.sendMessage(CC.translate(line));
        }
    }

    public boolean canSpawnEnvoy() {
        return (this.getLastEnvoyTime() + (300 * 1000)) < System.currentTimeMillis();
    }

    public long secondsUntilNextEnvoy() {
        int minutes = 120;
        return (this.getLastEnvoyTime() + (1000 * 60 * minutes) - System.currentTimeMillis()) / 1000;
    }

    public String formatedTimeUntilNextEnvoy() {
        return TimeUtils.formatSeconds(this.secondsUntilNextEnvoy());
    }

    public void updateLastEnvoyTime() {
        this.lastEnvoyTime = System.currentTimeMillis();
    }

    @Override
    public String update() {
        return formatedTimeUntilNextEnvoy();
    }
}
