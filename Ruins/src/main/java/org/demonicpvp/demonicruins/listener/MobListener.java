package org.demonicpvp.demonicruins.listener;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftIronGolem;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.demonicpvp.demonicruins.DemonicRuins;
import org.demonicpvp.demonicruins.mob.MobUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.demonicpvp.demonicruins.mob.MobUtil.*;

public class MobListener implements Listener {
    public static final String RUINS_WORLD_NAME = "world_ruins";

    private static List<String> allowedCommands = Lists.newArrayList();


    static {
        allowedCommands.addAll(Arrays.asList("msg", "message", "staffmode", "staff", "freeze", "lms", "ft", "filter", "r", "bless", "spawn", "warp", "blessme", "response", "space", "bless", "heal", "cf", "coinflip", "jackpot",
                "feed", "f", "fix", "filter edit", "filter toggle", "ft", "bless", "r", "reply", "spawn", "warp", "pots", "filters", "outpost", "pin", "sc", "staffchat", "mute", "ban", "tempban", "pin"));
    }

    public MobListener() {
        new BukkitRunnable() {
            @Override
            public void run() {
                World ruinsWorld = Bukkit.getWorld(RUINS_WORLD_NAME);

                ruinsWorld.getEntities().stream().filter(MobUtil::isRuinMob).forEach(mob -> {
                    Creature le = (Creature) mob;

                    le.getNearbyEntities(10.0, 10.0, 10.0).stream().filter(e -> e.getType() == EntityType.PLAYER).findFirst().ifPresent(player -> {
                        le.setTarget((LivingEntity) player);
                    });
                });
            }
        }.runTaskTimer(DemonicRuins.getInstance(), 5L, 5L);
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (MobUtil.isRuinMob(event.getEntity()) && (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageReduction(EntityDamageByEntityEvent event) {
        Entity mob = event.getEntity();

        if (MobUtil.isRuinMob(mob)) {
            if (event.getDamager() instanceof Player && ((Player) event.getDamager()).getItemInHand().getType() == Material.AIR) {
                //event.setUseRawDamage(true);
                event.setDamage(1.0F);
            } else {
                event.setDamage(event.getDamage() * 0.7);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(EntityDamageByEntityEvent event) {
        Entity mob = event.getEntity();

        if (MobUtil.isRuinMob(mob)) {
            //if (!event.isUseRawDamage()) {
                String mobType = MobUtil.getRuinsMobType(mob);

                if (event.getFinalDamage() < 1.0F) {
                    //event.setUseRawDamage(true);
                    event.setDamage(1.0F);
                } else if (mobType.equals(GUARDIAN_RUINS_MOB_VALUE) || mobType.equals(ELITE_GUARDIAN_RUINS_MOB_VALUE)) {
                    float maxDamage = 10.0F + new Random().nextInt(8);

                    if (event.getFinalDamage() > maxDamage) {
                        //event.setUseRawDamage(true);
                        event.setDamage(maxDamage);
                    }

                    if (mobType.equals(ELITE_GUARDIAN_RUINS_MOB_VALUE)) {
                        ((CraftIronGolem) mob).getHandle().getAttributeInstance(GenericAttributes.c).setValue(0.75); // knockback resistance
                    }
                //}
            }
        } else if (MobUtil.isRuinMob(event.getDamager()) || (event.getDamager() instanceof Projectile && MobUtil.isRuinMob((Entity) ((Projectile) event.getDamager()).getShooter()))) {
            Entity damager = event.getDamager() instanceof Projectile ? (Entity)((Projectile)event.getDamager()).getShooter() : event.getDamager();
            String mobType = MobUtil.getRuinsMobType(damager);

            if (mobType.equals(GUARDIAN_RUINS_MOB_VALUE)) {
                //event.setUseRawDamage(true);
                event.setDamage(4 + new Random().nextInt(3));
            } else if (mobType.equals(ELITE_GUARDIAN_RUINS_MOB_VALUE)) {
                //event.setUseRawDamage(true);
                event.setDamage(2 + new Random().nextInt(5));

                if (Math.random() < 0.3 && event.getEntity() instanceof Player) {
                    CommonsUtil.sendMessage(event.getEntity(), "&3&lElite Guardian >> &fThink you can out run me? Well I can out kill you!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals(RUINS_WORLD_NAME)) {
            return;
        }

        if (player.isOp()) {
            return;
        }

        if (player.hasPermission("astrocore.staffmode")) {
            return;
        }
        if (allowedCommands.contains(event.getMessage().split(" ")[0].replace("/", "").toLowerCase())) {
            return;
        }
        player.sendMessage(CC.translate(  "&c&l(!) &c" + event.getMessage() + " is denied in this world!"));
        event.setMessage("/null");
        event.setCancelled(true);
    }

    @EventHandler
    public void on(ChunkUnloadEvent event) {
        if (event.getWorld().getName().equals(RUINS_WORLD_NAME)) {
            boolean atLeastOne = false;
            Entity[] entities = event.getChunk().getEntities();

            for (Entity entity : entities) {
                if (entity.hasMetadata(RUINS_MOB_KEY)) {
                    atLeastOne = true;
                    break;
                }
            }

            if (atLeastOne) event.setCancelled(true);
        }
    }


}
