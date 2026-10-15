package me.astropvp.astrospacegrinding.entities.mobs;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.google.common.collect.Lists;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astrospacegrinding.AstroSpaceGrindingPlugin;
import me.astropvp.astrospacegrinding.data.SpacePlayerData;
import me.astropvp.astrospacegrinding.entities.SpaceMonster;
import me.astropvp.astrospacegrinding.enums.MonsterType;
import me.astropvp.astrospacegrinding.utils.ActionBarMessageUtils;
import me.astropvp.astrospacegrinding.utils.DamageIndicatorUtils;
import me.astropvp.astrospacegrinding.utils.config.ConfigManager;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import net.astropvp.core.utils.OutpostUtil;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import net.crankedpvp.shards.MobcoinAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;


public class SpaceAlien extends SpaceMonster {

    @Override
    public LivingEntity spawn(Location location) {

        Zombie entity = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        entity.setCustomName(CC.GreenB + "Space Zombie");
        AstroSpaceGrindingPlugin.activeMonsters.remove(entity.getUniqueId());

        entity.setRemoveWhenFarAway(false);
        entity.setCustomNameVisible(true);
        entity.setCanPickupItems(false);

        entity.getEquipment().setHelmet(new ItemBuilder(new ItemStack(Material.GLASS, 1, (short) 95))
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .enchant(Enchantment.DURABILITY, 3).build());
        entity.setMaxHealth(12.5);
        entity.setHealth(entity.getMaxHealth());
        entity.getEquipment().setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .enchant(Enchantment.DURABILITY, 3).build());
        entity.getEquipment().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .enchant(Enchantment.DURABILITY, 3).build());
        entity.getEquipment().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .enchant(Enchantment.DURABILITY, 3).build());
        entity.getEquipment().setItemInHand(new ItemBuilder(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 5)
                .enchant(Enchantment.DURABILITY, 3).build());
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999, 1));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, 4));
        if (entity.getEquipment() != null) {
            entity.getEquipment().setItemInHandDropChance(0.0F);
            entity.getEquipment().setHelmetDropChance(0.0F);
            entity.getEquipment().setChestplateDropChance(0.0F);
            entity.getEquipment().setLeggingsDropChance(0.0F);
            entity.getEquipment().setBootsDropChance(0.0F);
        }

        AstroSpaceGrindingPlugin.activeMonsters.put(entity.getUniqueId(), MonsterType.SPACE_ALIEN);

        return entity;
    }

    private boolean isInForcefieldWorld(Player player) {
        return player.getLocation().getWorld().getName().equalsIgnoreCase("space");
    }


    @Override
    public List<ItemStack> getRewards(Player player, LivingEntity entity) {
        List<ItemStack> rewards = Lists.newArrayList();
        SpacePlayerData data = SpacePlayerData.findPlayer(player);
        Random random = new Random();


        if (Math.random() < 0.5) {
            int maxMoney = AstroSpaceGrindingPlugin.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spaceAlienMaxMoney");
            int minMoney = AstroSpaceGrindingPlugin.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spaceAlienMinMoney");
            int randomNumberMoney = random.nextInt(maxMoney - minMoney + 1) + minMoney;
            if(isInForcefieldWorld(player)) {
                randomNumberMoney *= 0.2;
            }

        /*
            if (ForceFieldData.findPlayer(player).isActive()) {
                randomNumberMoney *= 0.7;
            }

         */


            player.sendMessage(CC.translate("&a&l+ $" + NumberUtils.formatMoney(randomNumberMoney)));
            EconomyUtils.depositBalance(player, randomNumberMoney);
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
            data.addMoney(randomNumberMoney);

        } else {
            int maxXP = AstroSpaceGrindingPlugin.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spaceAlienMaxXP");
            int minXP = AstroSpaceGrindingPlugin.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spaceAlienMinXP");
            int randomNumberXP = random.nextInt(maxXP - minXP + 1) + minXP;
            /*
            if (ForceFieldData.findPlayer(player).isActive()) {
                randomNumberXP *= 0.70;
            }

             */
            if(isInForcefieldWorld(player)) {
                randomNumberXP *= 0.2D;
            }
            player.sendMessage(CC.translate("&a&l+ " + NumberUtils.formatMoney(randomNumberXP)) + " xp");
            player.giveExp(randomNumberXP);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            DamageIndicatorUtils.sendDamageIndicator(entity, CC.translate("&a&l+ " + NumberUtils.formatMoney(randomNumberXP)) + " xp", "EXECUTE" );
            data.addXp(randomNumberXP);

        }


        double chance = AstroSpaceGrindingPlugin.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("spaceAlienRewardChance");
        if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.SUMMER_2021_BELT)) {
            chance *= 1.30;
        }

        if (OutpostUtil.hasMarsOutpost(player)) {
            chance *= 2;
        }

        /*
        if (ForceFieldData.findPlayer(player).isActive()) {
            chance *= 0.70;
        }

         */

        if(isInForcefieldWorld(player)) {
            chance *= 0.2;
        }

        if (HCFOutpostAPI.isControllingOutpost(player, OutpostType.SPACE)) {
            chance*=1.15;
        }


        for (int i = 0; i < 3; i++) {
            if (Math.random() < chance / 100) {
                rewards.add(LootTableAPI.getRandomLootItem(LootTable.SPACE));

                if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.NYE_AMULET) && Math.random() < 0.15) {
                    player.sendMessage(CC.translate("&d&l(Radioactive Amulet) &dYou were &flucky&d and received &fdouble&d space loot."));
                    rewards.add(LootTableAPI.getRandomLootItem(LootTable.SPACE));
                }
                if (HCFOutpostAPI.isControllingOutpost(player, OutpostType.SPACE) && Math.random() < 0.35) {
                    player.sendMessage(CC.translate("&d&l(Space Stronghold) &dYou were &flucky&d and received &fdouble&d space loot."));
                    rewards.add(LootTableAPI.getRandomLootItem(LootTable.SPACE));

                }
            }
        }


        int amount = AstroSpaceGrindingPlugin.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("mobcoinAmount");
        if (Math.random() < AstroSpaceGrindingPlugin.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getDouble("mobcoinChance") / 100) {
            MobcoinAPI.addMobcoins(player, amount);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 1);
            player.sendMessage(CC.translate("&6&l+ &6⛃&e" + amount));
            DamageIndicatorUtils.sendDamageIndicator(entity,CC.translate("&6&l+ &6⛃&e" + amount), "EXECUTE" );
        }

        return rewards;
    }

    @Override
    public void onDeath(EntityDeathEvent event, SpacePlayerData killer) {
        AstroSpaceGrindingPlugin.activeMonsters.remove(event.getEntity().getUniqueId());

    }

    @Override
    public void onPlayerDamageMob(EntityDamageByEntityEvent event, Player attacker) {
        ActionBarMessageUtils.sendMessage(attacker, CC.translate("&a&lSpace Alien &7(&f" + Math.round(((LivingEntity) event.getEntity()).getHealth()) + "/" + Math.round(((LivingEntity) event.getEntity()).getMaxHealth()) + " HP&7) "));
    }


}
