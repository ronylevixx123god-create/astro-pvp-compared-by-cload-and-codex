package me.ifamasssxd.cosmicbosses.dungeonboss.abilities;

import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.dungeonboss.DeathCurseBlaze;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMonster;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class DeathCurse extends DungeonAbility {
    private Player player;
    private DeathCurseBlaze blaze;
    int ticks;
    private long spawnTime;

    public DeathCurse(final Boss boss) {
        super(boss);
        this.ticks = 0;
    }

    @Override
    public void onTick() {
        ++this.ticks;
        if (this.spawnTime != 0L && System.currentTimeMillis() - this.spawnTime >= 15000L && this.player != null && this.player.getWorld().getName().equals(this.blaze.world.getWorld().getName()) && this.blaze.getBukkitEntity().getLocation().distanceSquared(this.player.getLocation()) <= 10000.0 && !this.player.isDead()) {
            this.player.setHealth(0.0);
            this.boss.sendMessage("Such a waste of life you are [NAME]!");
            if (this.blaze != null) {
                this.boss.removeMinion((Entity) this.blaze.getBukkitEntity());
                this.blaze.die();
            }
            Bukkit.getLogger().info("Killing " + this.player.getName() + " because Death Curse wasnt killed!");
        }
        if (this.ticks % 5 == 0 && this.blaze != null && this.blaze.isAlive()) {
            //ParticleEffect.sendToLocation(ParticleEffect.LAVA, this.blaze.getBukkitEntity().getLocation(), 1.0f, 1.0f, 1.0f, 0.1f, 10);
            try {
                de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), this.blaze.getBukkitEntity().getLocation(), 1f, 1f, 1f, 0.1f, 10);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (this.ticks % 20 == 0 && this.blaze != null && this.blaze.isAlive() && this.player != null && this.player.isValid()) {
            this.blaze.getBukkitEntity().getWorld().playSound(this.blaze.getBukkitEntity().getLocation(), Sound.NOTE_PLING, 10.0f, 1.1f);
        }
    }

    @Override
    public long getCooldown() {
        return 0L;
    }

    @Override
    public boolean shouldStopTicking() {
        return this.blaze == null || this.blaze.dead || System.currentTimeMillis() - this.spawnTime >= TimeUnit.SECONDS.toMillis(17L);
    }

    @Override
    public void onAbilityEnable() {
        this.initAbility();
        final List<Entity> nearbyPlayers = Lists.newArrayList();
        for (final Entity ent : this.boss.getEntity().getNearbyEntities(128.0, 128.0, 128.0)) {
            if (ent instanceof Player && !ent.hasMetadata("NPC")) { //&& !EssentialsUtils.isInGodMode(this.player)) {
                nearbyPlayers.add(ent);
            }
        }
        if (nearbyPlayers.size() > 0) {
            final Entity ent2 = nearbyPlayers.get(ThreadLocalRandom.current().nextInt(nearbyPlayers.size()));
            this.player = (Player) ent2;
            Bukkit.getLogger().info("Spawning Death Curse on " + this.player.getName());
            final Random r = ThreadLocalRandom.current();
            final Location spawnLoc = this.player.getLocation().clone().add((double) (r.nextInt(2) * (r.nextBoolean() ? -1 : 1)), 1.0, (double) (r.nextInt(2) * (r.nextBoolean() ? -1 : 1)));
            this.spawnTime = System.currentTimeMillis();
            final WorldServer world = ((CraftWorld) this.player.getWorld()).getHandle();
            (this.blaze = new DeathCurseBlaze(world)).setPosition(spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ());
            world.addEntity((net.minecraft.server.v1_8_R3.Entity) this.blaze, CreatureSpawnEvent.SpawnReason.CUSTOM);
            this.blaze.setCustomName(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Death Curse");
            this.blaze.setCustomNameVisible(true);
            this.blaze.getAttributeInstance(GenericAttributes.c).setValue(1.0);
            final CraftMonster living = (CraftMonster) this.blaze.getBukkitEntity();
            living.setMaxHealth(10.0);
            living.setHealth(10.0);
            this.boss.sendMessage("I tire of these games. You, [NAME], your life will end in 15 seconds. Goodbye!", Sound.BLAZE_BREATH, 0.9f);
        } else {
            Bukkit.getLogger().info("No target found!");
        }
    }

    @Override
    public void onAbilityDisable() {
        if (this.blaze != null && this.blaze.isAlive()) {
            this.blaze.die();
            this.blaze = null;
        }
    }

    @Override
    public void onAbilityEvent(final Event event) {
    }

    @Override
    public String getAbilityName() {
        return "Death Curse";
    }
}
