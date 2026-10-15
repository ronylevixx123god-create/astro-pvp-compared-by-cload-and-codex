package me.ifamasssxd.cosmicbosses.bosses.skeletonhorse;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.YijkiDisciple;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossAbility;
import me.ifamasssxd.cosmicbosses.utils.CustomItem;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Summoning extends BossAbility {
    Random random;

    public Summoning(final Boss boss) {
        super(boss);
        this.random = new Random();
    }

    @Override
    public void onAbility() {
        if (this.getBoss() instanceof UndeadAssassin) {
            final UndeadAssassin boss = (UndeadAssassin) this.getBoss();
            if (boss.invincible) {
                return;
            }
            boss.invincible = true;
            final Entity handle = ((CraftEntity) this.getBoss().getEntity()).getHandle();
            handle.motY += 0.5;
        } else if (this.getBoss() instanceof YijkiDisciple) {
            final YijkiDisciple disk = (YijkiDisciple) this.getBoss();
            if (disk.invincible) {
                return;
            }
            disk.invincible = true;
            final Entity handle2 = ((CraftEntity) this.getBoss().getEntity()).getHandle();
            handle2.motY += 0.5;
        }
        this.boss.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
        for (int amount = this.random.nextInt(3) + 2, i = 0; i < amount && this.boss.getMinions().size() <= 3; ++i) {
            final Skeleton skeleton = (Skeleton) this.boss.spawnMinion(EntityType.SKELETON, this.boss.getEntity().getLocation());
            skeleton.setMaxHealth(50.0);
            skeleton.setHealth(skeleton.getMaxHealth());
            skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            skeleton.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
            skeleton.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
            skeleton.getEquipment().setHelmet(new CustomItem(new ItemStack(Material.DIAMOND_HELMET)).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).setUnbreakable(true).build());
            skeleton.getEquipment().setChestplate(new CustomItem(new ItemStack(Material.DIAMOND_CHESTPLATE)).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).setUnbreakable(true).build());
            skeleton.getEquipment().setLeggings(new CustomItem(new ItemStack(Material.DIAMOND_LEGGINGS)).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).setUnbreakable(true).build());
            skeleton.getEquipment().setBoots(new CustomItem(new ItemStack(Material.DIAMOND_BOOTS)).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).setUnbreakable(true).build());
            skeleton.getEquipment().setItemInHand(new CustomItem(new ItemStack(Material.DIAMOND_SWORD)).setUnbreakable(true).addEnchant(Enchantment.DAMAGE_ALL, 2).build());
            skeleton.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
            skeleton.setMetadata("no_drops", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
            Bukkit.getScheduler().runTaskLater(CosmicBosses.getInstance(), skeleton::remove, 20*300);

        }
        this.activateCooldown();
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(40L);
    }

    @Override
    public String getAbilityName() {
        return "Summoning";
    }
}
