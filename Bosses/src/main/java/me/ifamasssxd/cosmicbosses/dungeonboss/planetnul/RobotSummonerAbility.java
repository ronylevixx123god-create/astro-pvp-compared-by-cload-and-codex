package me.ifamasssxd.cosmicbosses.dungeonboss.planetnul;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreeper;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RobotSummonerAbility extends DungeonAbility {
    public RobotSummonerAbility(Boss boss) {
        super(boss);
    }

    private boolean hasSpawnedCreepers = false;
    private int tickCount = 0;

    @Override
    public void onTick() {
        tickCount++;

        if (tickCount > 65 && !hasSpawnedCreepers) {
            int creeperSpawnAmount = new Random().nextInt(4) + 4;

            for (int i = 0; i < creeperSpawnAmount; i++) {
                Location spawnLocation = this.boss.getEntity().getLocation();

                for (int j = 0; j < 10; j++) {
                    int offX = -9 + new Random().nextInt(18);
                    int offZ = -9 + new Random().nextInt(18);

                    Location newSpawnLocation = new Location(spawnLocation.getWorld(), spawnLocation.getX() + offX, ((CorruptCosmonaut) this.boss).hasFloorBroke() ? 88 : 116, spawnLocation.getZ() + offZ);

                    if (newSpawnLocation.getBlock().getType() == Material.AIR && newSpawnLocation.clone().add(0, -1, 0).getBlock().getType() != Material.AIR) {
                        spawnLocation = newSpawnLocation;
                        break;
                    }
                }

                this.spawnCreeper(spawnLocation);
            }

            this.hasSpawnedCreepers = true;
        }

        this.boss.getMinions().stream().filter(ent -> !ent.isDead()).forEach(minion -> {
            if (minion.getType() == EntityType.CREEPER) {
                Creeper creeper = (Creeper) minion;

                ((CraftCreeper) creeper).getHandle().setGoalTarget((EntityLiving) ((CraftEntity) boss.getEntity()).getHandle());
                creeper.getLocation().setPitch(-55);
                creeper.setTarget(boss.getEntity());
            } else if (minion.getType() == EntityType.PIG_ZOMBIE) {
                PigZombie pigZombie = (PigZombie) minion;

                if (pigZombie.getTarget() == null) {
                    if (((Creature) boss.getEntity()).getTarget() != null) {
                        pigZombie.setTarget(((Creature) boss.getEntity()).getTarget());
                    } else {
                        pigZombie.getNearbyEntities(15.0, 15.0, 15.0).stream().filter(ent -> ent.getType() == EntityType.PLAYER).findAny().ifPresent(ent -> {
                            pigZombie.setTarget((LivingEntity) ent);
                        });
                    }
                }
            }
        });
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(15L);
    }

    @Override
    public String getAbilityName() {
        return "Robot Summoner";
    }

    @Override
    public boolean shouldStopTicking() {
        return System.currentTimeMillis() - this.initTime >= this.getCooldown();
    }

    @Override
    public void onAbilityEnable() {
        this.initAbility();

        int pigZombieSpawnAmount = new Random().nextInt(5) + 2;

        for (int i = 0; i < pigZombieSpawnAmount; i++) {
            Location spawnLocation = this.boss.getEntity().getLocation();

            for (int j = 0; j < 10; j++) {
                int offX = -9 + new Random().nextInt(18);
                int offZ = -9 + new Random().nextInt(18);

                Location newSpawnLocation = new Location(spawnLocation.getWorld(), spawnLocation.getX() + offX, ((CorruptCosmonaut) this.boss).hasFloorBroke() ? 88 : 116, spawnLocation.getZ() + offZ);

                if (newSpawnLocation.getBlock().getType() == Material.AIR && newSpawnLocation.clone().add(0, -1, 0).getBlock().getType() != Material.AIR) {
                    spawnLocation = newSpawnLocation;
                    break;
                }
            }

            this.spawnPigZombie(spawnLocation);
        }
    }

    @Override
    public void onAbilityDisable() {
        this.tickCount = 0;
        this.hasSpawnedCreepers = false;
        this.boss.removeMinions();
        ((CorruptCosmonaut) this.boss).doTrickster();
    }

    @Override
    public void onAbilityEvent(Event p0) {

    }

    private void spawnPigZombie(Location location) {
        PigZombie pigZombie = (PigZombie) this.boss.spawnMinion(EntityType.PIG_ZOMBIE, location);
        pigZombie.setCustomNameVisible(true);
        pigZombie.setCustomName(CC.translate("&6&lRogue Warrior"));
        pigZombie.setMaxHealth(120);
        pigZombie.setHealth(120);
        pigZombie.setTarget(((Creature) this.boss.getEntity()).getTarget());

        ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
        diamondSword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);

        pigZombie.getEquipment().setItemInHand(diamondSword);

        pigZombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        pigZombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        pigZombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        pigZombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));

        pigZombie.getEquipment().setItemInHandDropChance(0.0F);
        pigZombie.getEquipment().setHelmetDropChance(0.0F);
        pigZombie.getEquipment().setChestplateDropChance(0.0F);
        pigZombie.getEquipment().setLeggingsDropChance(0.0F);
        pigZombie.getEquipment().setBootsDropChance(0.0F);

        pigZombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

        pigZombie.setBaby(false);
    }

    private void spawnCreeper(Location location) {
        Creeper creeper = (Creeper) this.boss.spawnMinion(EntityType.CREEPER, location);
        creeper.setPowered(true);
        creeper.setTarget(this.boss.getEntity());
        creeper.getLocation().setPitch(-25);
        creeper.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        creeper.setCustomNameVisible(true);
        creeper.setCustomName(CC.translate("&6&lCreepy Healer"));
        creeper.setMaxHealth(325);
        creeper.setHealth(325);
    }
}
