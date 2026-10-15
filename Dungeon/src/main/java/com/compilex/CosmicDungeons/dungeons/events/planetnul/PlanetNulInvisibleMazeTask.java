package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.dungeons.libs.PlanetNulDungeon;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.VanishUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class PlanetNulInvisibleMazeTask
extends BukkitRunnable {
    private PlanetNulDungeon dungeon;
    public int timer = 0;

    public PlanetNulInvisibleMazeTask(PlanetNulDungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void run() {
        if(this.dungeon.getPlayers().isEmpty())
            return;
        boolean playSound;
        List<Zombie> zombies = this.dungeon.getDungeonEvents().getInvisibleMazeRegionEvent().getZombies();
        ++this.timer;
        boolean bl = playSound = this.timer >= 20;
        if (playSound) {
            this.timer = 0;
        }
        for (Player p : this.dungeon.getPlayers()) {
            DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(p);
            if (dp.getCurrentDungeon() == null || !dp.getCurrentDungeon().equals(this.dungeon) || zombies == null) continue;
            Location loc = p.getEyeLocation();
            Vector v = loc.getDirection().normalize();
            for (int i = 1; i <= 5; ++i) {
                loc.add(v);
                if (loc.getBlock().getType() != Material.AIR) break;
            }
            if (loc.getBlock().getType() == Material.STAINED_GLASS) {
                for (Zombie zombie : zombies) {
                    if (zombie == null || zombie.isDead())
                        continue;
                    CosmicDungeonsPlugin.get().getEntityHider().showEntity(p, zombie);

                }
                continue;
            }
            for (Zombie zombie : zombies) {
                if (zombie == null || zombie.isDead())
                    continue;
                if (p.hasLineOfSight(zombie)) {
                    CosmicDungeonsPlugin.get().getEntityHider().showEntity(p, zombie);
                    continue;
                }
                CosmicDungeonsPlugin.get().getEntityHider().hideEntity(p, zombie);
            }
        }
    }

    private void spawnMazeZombie(Location location, final Player target) {
        final Zombie zombie = (Zombie)location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        zombie.setCustomNameVisible(true);
        zombie.setCustomName(CC.translate("&3&lMaze Monster"));
        zombie.setVillager(false);
        zombie.setBaby(false);
        zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
        zombie.getEquipment().setItemInHandDropChance(0.0f);
        zombie.getEquipment().setHelmetDropChance(0.0f);
        zombie.getEquipment().setChestplateDropChance(0.0f);
        zombie.getEquipment().setLeggingsDropChance(0.0f);
        zombie.getEquipment().setBootsDropChance(0.0f);
        zombie.setMetadata("inInvisibleMaze", new FixedMetadataValue(CosmicDungeonsPlugin.get(), true));
        zombie.setMetadata("invisibleMazeZombie", new FixedMetadataValue(CosmicDungeonsPlugin.get(), true));
        zombie.setHealth(20.0);
        zombie.setTarget(target);
        new BukkitRunnable(){
            int counter = 0;

            public void run() {
                ++this.counter;
                if (PlanetNulInvisibleMazeTask.this.dungeon == null || PlanetNulInvisibleMazeTask.this.dungeon.getPlayers().isEmpty()) {
                    this.cancel();
                    return;
                }
                if (target.isDead() || PlanetNulInvisibleMazeTask.this.dungeon.getPlayers().stream().noneMatch(p -> p.getUniqueId().toString().equals(target.getUniqueId().toString())) || !zombie.getTarget().getUniqueId().equals(target.getUniqueId())) {
                    zombie.setTarget((LivingEntity)target);
                }
                if (this.counter / 4 > 30) {
                    zombie.setHealth(0.0);
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)CosmicDungeonsPlugin.get_(), 0L, 5L);
    }
}

