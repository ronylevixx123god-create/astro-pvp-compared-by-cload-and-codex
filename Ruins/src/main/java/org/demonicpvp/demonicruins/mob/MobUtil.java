package org.demonicpvp.demonicruins.mob;

import com.astropvp.cc.packet.type.out.SSetEntityProperties;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.gson.Gson;
import me.matthawkk.chat.CC;
import net.astropvp.core.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.demonicpvp.demonicruins.DemonicRuins;

public class MobUtil {
    public static final String ENTITY_PROPERTIES_KEY = "entityProperties";
    public static final String GUARDIAN_RUINS_MOB_VALUE = "GUARDIAN";
    public static final String ELITE_GUARDIAN_RUINS_MOB_VALUE = "ELITE_GUARDIAN";
    public static final String IS_ELDER_GUARDIAN_KEY = "isElder";
    public static final String SPAWN_LOCATION_KEY = "ruinsSpawnLocation";
    public static final String RUINS_MOB_KEY = "ruinsMob";

    public static void spawnGuardian(Location location, boolean zombie) {
        if (!location.getChunk().isLoaded()) location.getChunk().load();

        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, zombie ? EntityType.ZOMBIE : EntityType.SKELETON);

        if (zombie) {
            ((Zombie) entity).setVillager(false);
            ((Zombie) entity).setBaby(false);
        }

        entity.setCustomNameVisible(true);
        entity.setCustomName(CC.translate("&3&lGuardian"));

        entity.getEquipment().clear();

        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

        entity.setMetadata(SPAWN_LOCATION_KEY, new FixedMetadataValue(DemonicRuins.getInstance(), LocationUtils.toStringLocation(location)));

        if (zombie) {
            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
            sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
            sword.addEnchantment(Enchantment.KNOCKBACK, 1);
            entity.getEquipment().setItemInHand(sword);
        } else {
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
            bow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 2);
            entity.getEquipment().setItemInHand(bow);
        }

        entity.getEquipment().setItemInHandDropChance(0.0F);

        entity.setMaxHealth(30.0D);
        entity.setHealth(entity.getMaxHealth());

        String entityProperties = new Gson().toJson(new SSetEntityProperties.EntityProperties(1.8, false, false, true, CommonsUtil.color("&3&lGuardian"), 0, zombie ? 1 : 2, false, ""));
        entity.setMetadata(ENTITY_PROPERTIES_KEY, new FixedMetadataValue(DemonicRuins.getInstance(), entityProperties));
        entity.setMetadata(RUINS_MOB_KEY, new FixedMetadataValue(DemonicRuins.getInstance(), GUARDIAN_RUINS_MOB_VALUE));
    }

    public static void spawnEliteGuardian(Location location, boolean isElder) {
        if (!location.getChunk().isLoaded()) location.getChunk().load();

        IronGolem golem = (IronGolem) location.getWorld().spawnEntity(location, EntityType.IRON_GOLEM);

        golem.setCustomNameVisible(true);
        golem.setCustomName(CommonsUtil.color("&3&l" + (isElder ? "Elder " : "") + "Elite Guardian"));

        golem.getEquipment().clear();

        golem.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

        golem.setMaxHealth(1000.0D);
        golem.setHealth(golem.getMaxHealth());

        String entityProperties = new Gson().toJson(new SSetEntityProperties.EntityProperties(3.2, false, false, true, CommonsUtil.color("&3&l" + (isElder ? "Elder " : "") + "Elite Guardian"), 0, 5, false, ""));
        golem.setMetadata(ENTITY_PROPERTIES_KEY, new FixedMetadataValue(DemonicRuins.getInstance(), entityProperties));
        golem.setMetadata(RUINS_MOB_KEY, new FixedMetadataValue(DemonicRuins.getInstance(), ELITE_GUARDIAN_RUINS_MOB_VALUE));

        if (isElder) {
            golem.setMetadata(IS_ELDER_GUARDIAN_KEY, new FixedMetadataValue(DemonicRuins.getInstance(), isElder));
        }
    }

    public static boolean isRuinMob(Entity mob) {
        return mob.hasMetadata(RUINS_MOB_KEY);
    }

    public static String getRuinsMobType(Entity mob) {
        return mob.getMetadata(RUINS_MOB_KEY).get(0).asString();
    }
}
