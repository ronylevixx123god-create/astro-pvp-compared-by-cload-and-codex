package me.ifamasssxd.cosmicbosses.bosses.brood;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.entities.CustomEntity;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossAbility;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SpiderSpawn extends BossAbility {
    Random random;

    public SpiderSpawn(final Boss boss) {
        super(boss);
        this.random = new Random();
    }

    @Override
    public void onAbility() {
        if (this.getBoss().getMinions().size() < 15) {
            final int toSpawn = this.random.nextInt(5) + 7;
            boolean spawned = false;
            int forTarget = toSpawn / 5;
            final Spider boss = (Spider) this.getBoss().getEntity();
            for (int i = 0; i < toSpawn && this.getBoss().getMinions().size() < 15; ++i) {
                spawned = true;
                Location spawnLoc = null;
                if (boss.getTarget() != null && forTarget > 0) {
                    final Player pl = (Player) boss.getTarget();
                    if (BossUtils.canAffectPlayer(pl, false)) {
                        spawnLoc = pl.getLocation();
                        --forTarget;
                    }
                }
                if (spawnLoc == null) {
                    spawnLoc = this.getBoss().getEntity().getLocation();
                }
                final CaveSpider spider = (CaveSpider) this.spawnMinion(BossUtils.getRandomLocationNearby(spawnLoc, 5, 2, 2));
                spider.setMaxHealth(140.0);
                spider.setHealth(140.0);
            }
            if (spawned) {
                this.getBoss().sendMessage(ChatColor.ITALIC + "Go my children, devour their very souls!", Sound.ZOMBIE_REMEDY, 1.4f);
            }
        }
        this.activateCooldown();
    }

    public Entity spawnMinion(final Location l) {
        final Entity ent = BossUtils.createCustomEntity(CustomEntity.CAVE_SPIDER, l);
        ent.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        ent.setMetadata("minion", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) this.getBoss().getEntity().getUniqueId()));
        this.getBoss().getMinions().add(ent);
        if (ent instanceof LivingEntity) {
            final LivingEntity li = (LivingEntity) ent;
            li.setCustomName(this.getBoss().getColor(true) + "Brood Spawn");
            //li.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            if (li.getEquipment() != null) {
                li.getEquipment().setItemInHandDropChance(0.0f);
                li.getEquipment().setHelmetDropChance(0.0f);
                li.getEquipment().setChestplateDropChance(0.0f);
                li.getEquipment().setLeggingsDropChance(0.0f);
                li.getEquipment().setBootsDropChance(0.0f);
            }
        }
        return ent;
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(10L);
    }

    @Override
    public String getAbilityName() {
        return "Spider Spawn";
    }
}
