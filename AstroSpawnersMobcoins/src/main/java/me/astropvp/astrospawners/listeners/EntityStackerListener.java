package me.astropvp.astrospawners.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import io.netty.util.internal.ThreadLocalRandom;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.struct.GrindingCache;
import me.astropvp.astrospawners.struct.GrindingManager;
import me.astropvp.astrospawners.utils.MobStackerUtils;
import me.astropvp.astrospawners.utils.StackedMobDeathEvent;
import me.astropvp.astrospawners.utils.config.ConfigManager;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.core.utils.OutpostUtil;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityStackerListener implements Listener {


    private final MobStackerUtils mobStackerUtils;
    private final AstroSpawners plugin;
    private final GrindingManager grindingManager;
    private final Map<UUID, Map<CustomEnchantment, Integer>> cachedWeaponEnchants;
    private final ItemStack grinderKey = CrateKeyItem.getItem(CrateType.SOLAR);
    private final ItemStack godlyKey = CrateKeyItem.getItem(CrateType.COSMIC);
    //private final ItemStack mysteryLootbox = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("GrindingLootbox").createClaimableItem(false);


    private Map<UUID, Long> blazeCooldown;

    public EntityStackerListener(AstroSpawners plugin) {
        this.plugin = plugin;
        this.mobStackerUtils = plugin.getMobStackerUtils();
        this.blazeCooldown = new HashMap<>();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(AstroSpawners.getInstance(), () -> {
            this.blazeCooldown.clear();
        }, 10, 5 * 20 * 60);
        this.cachedWeaponEnchants = new HashMap<>();
        this.grindingManager = new GrindingManager();
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onMobSpawn(SpawnerSpawnEvent event) {
        mobStackerUtils.stackMobs(event);
    }

    @EventHandler
    public void onStackedEntityDeath(StackedMobDeathEvent e) throws Exception {
        Entity entity = e.getEntity();
        if (e.getDamager() instanceof Player) {

            Player p = (Player) e.getDamager();

            if (!mobStackerUtils.isStackedMob(entity)) return;

            if (e.getEntity().getLastDamageCause() != null) {
                if (e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) return;
                if (e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) return;
                if (e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) return;
            }

            if (AstroSpawners.getInstance().getWaitingForAfkGrindingVerification().contains(e.getDamager().getUniqueId())) {
                e.setCancelled(true);
                e.getDamager().sendMessage(CC.translate("&c&l(!)&c You cannot interact with blazes until you have solved the captcha, if you think there is an issue, relog and it will be resolved."));
                return;
            }

            boolean allowFullInvGrinding = AstroSpawners.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getBoolean("allow-full-inv-grinding");

            if (p.getInventory().firstEmpty() == -1 && !allowFullInvGrinding) {
                p.sendMessage(CC.translate("&c&l(!)&c Clear some space in your inventory before you keep grinding mobs!"));
                //p.playSound(p.getLocation(), Sound.VILLAGER_NO, 2f, 2f);
                return;
            }

            double exp = AstroSpawners.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("baseXp");
            int remove = 1;

            if (((Player) e.getDamager()).getItemInHand() != null) {
                Map<CustomEnchantment, Integer> enchants;
                UUID uuid = e.getDamager().getUniqueId();
                if (cachedWeaponEnchants.containsKey(uuid)) {
                    enchants = cachedWeaponEnchants.get(e.getDamager().getUniqueId());
                } else {
                    enchants = EnchantmentAPI.getEnchantments(((Player) e.getDamager()).getItemInHand());
                    cachedWeaponEnchants.put(uuid, enchants);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(AstroSpawners.getInstance(), () -> {
                        cachedWeaponEnchants.remove(uuid);
                    }, 60 * 20L);
                }

                if (enchants.containsKey(EnchantmentAPI.getEnchantment("Master Inquisitive"))) {
                    exp *= 1.15;
                } else if (enchants.containsKey(EnchantmentAPI.getEnchantment("Inquisitive"))) {
                    exp *= 1.05;
                }

                if (enchants.containsKey(EnchantmentAPI.getEnchantment("Kill Aura")) && Math.random() < 0.075) {
                    remove = ThreadLocalRandom.current().nextInt(2, 5 + 1);
                }
            }


            int amount = mobStackerUtils.getStackSize(entity);
            if (amount <= 1) {
                mobStackerUtils.removeStackedMob(entity);
                return;
            }
            amount -= remove;
            mobStackerUtils.setStackSize(entity, amount);
            if (remove > 1) {
                p.sendMessage(CC.translate("&6&l* KILL AURA [&7-" + remove + "x&6&l] *"));
            }


            if (AstroArmorSetsAPI.getCurrentArmorSet(p) != null && AstroArmorSetsAPI.getCurrentArmorSet(p) == ArmorSetType.GRINDER) {
                exp *= 1.15;
            }

            if (CosmicSkyInventoryPets.get().getPetManager().getPetBuffExpiration().containsKey(p.getUniqueId())) {
                exp *= 2.05;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.MEDIEVAL_SWORD_KING)) {
                exp *= 1.15;
            }


            if (HCFOutpostAPI.isControllingOutpost(FactionUtils.getFaction(p), OutpostType.COURTYARD)) {
                exp *= 1.15;
            }

            if (OutpostUtil.hasMarsOutpost(p)) {
                exp *= 1.15;
            }

            if (e.getDamager().hasPermission("astrocore.nebula")) {
                exp *= 2;
            }

            GrindingCache cache = grindingManager.getGrindingCache(p);
            cache.addMobKilled(remove);
            while (remove != 0) {
                blazeLoot(cache, p);
                remove--;
            }
            cache.addExp((int) exp);
            //plugin.addPlayerToAfkGrinding(p);
            boolean showParticles = AstroSpawners.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getBoolean("showGrindParticles");
            if (showParticles) {
                de.inventivegames.particle.ParticleEffect.CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), entity.getLocation(), 0, 1, 0, 0.4f, 12);
            }

        }


    }


    private void blazeLoot(GrindingCache cache, Player p) {
        double grinderKeyChance = AstroSpawners.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("grinderKeyChancePercent") * 0.01; //0.01
        double godlyKeyChance = AstroSpawners.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("godlyKeyChancePercent") * 0.01; //0.01
        double mobcoinChance = AstroSpawners.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getDouble("mobcoinChance") * 0.01;
        double treasureChance = AstroSpawners.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getDouble("treasureChance") * 0.01;

        Map<CustomEnchantment, Integer> enchants;
        UUID uuid = p.getUniqueId();
        if (cachedWeaponEnchants.containsKey(uuid)) {
            enchants = cachedWeaponEnchants.get(p.getUniqueId());
        } else {
            enchants = EnchantmentAPI.getEnchantments((p.getItemInHand()));
            cachedWeaponEnchants.put(uuid, enchants);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroSpawners.getInstance(), () -> {
                cachedWeaponEnchants.remove(uuid);
            }, 60 * 20L);
        }

        cache.addMoney(22);

        if (enchants.containsKey(EnchantmentAPI.getEnchantment("Coins"))) {
            mobcoinChance *= 1.4;
        }

        int mcAmount = AstroSpawners.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("mobcoinAmount");
        if (Math.random() < mobcoinChance) {
            //cache.addCoins(mcAmount);
        }


        if (AstroArmorSetsAPI.getCurrentArmorSet(p) != null && AstroArmorSetsAPI.getCurrentArmorSet(p) == ArmorSetType.GRINDER) {
            grinderKeyChance *= 1.2;
            godlyKeyChance *= 1.2;
        }

        if (enchants.containsKey(EnchantmentAPI.getEnchantment("Key Grind"))) {
            grinderKeyChance *= 1.4;
            godlyKeyChance *= 1.4;
        }
        /*

        if (Math.random() < treasureChance) {
            LootFilterAPI.giveItem(p, LootTable.GRINDING, mysteryLootbox);
            Bukkit.getScheduler().runTaskAsynchronously(AstroSpawners.getInstance(), ()-> {

                if (p.getInventory().firstEmpty() == -1) {
                    Collector collector = CollectorManager.DELEGATE.getCollector(Arkkit.get().getPlayerLocations().get(p.getName()));
                    if (collector != null) {
                        collector.addLootbox(1);
                        p.sendMessage(CC.translate("&a&l+ &6&lGrinding Lootbox &7(Collector collected [+1])"));
                    }


            } else {
                p.sendMessage(CC.translate("&a&l+ &6&lGrinding Lootbox"));
                PlayerUtil.giveOrDropProtectedItem(p, mysteryLootbox, 60);
            }
            });
        }

         */

        double num = Math.random();

        if (godlyKeyChance >= num) {
            if (!LootFilterAPI.isFiltered(p, LootTable.GRINDING, godlyKey)) {

                PlayerUtil.giveOrDropProtectedItem(p, godlyKey, 60);
            }
        } else if (grinderKeyChance >= num) {
            if (!LootFilterAPI.isFiltered(p, LootTable.GRINDING, grinderKey)) {
                PlayerUtil.giveOrDropProtectedItem(p, grinderKey, 60);
            }
        }
    }


    @EventHandler
    public void onMobDeath(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Creature)) return;

        if (e.getEntity().getLastDamageCause() != null) {
            if (e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) return;
            if (e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) return;
            if (e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) return;
        }

        Creature entity = (Creature) e.getEntity();
        if (!mobStackerUtils.isStackedMob(entity)) {
            return;
        }

        if (this.blazeCooldown.containsKey(e.getEntity().getUniqueId())) {
            if (System.currentTimeMillis() < this.blazeCooldown.get(e.getEntity().getUniqueId()) + (long) 83) {
                e.setCancelled(true);
                return;
            }

        }
        this.blazeCooldown.put(e.getEntity().getUniqueId(), System.currentTimeMillis());


        StackedMobDeathEvent stackedMobDeathEvent = new StackedMobDeathEvent(entity, e.getDamager());
        try {
            Bukkit.getPluginManager().callEvent(stackedMobDeathEvent);
        } catch (Exception ignored) {

        }
        entity.setHealth(entity.getMaxHealth());
        e.setCancelled(true);
    }


}
