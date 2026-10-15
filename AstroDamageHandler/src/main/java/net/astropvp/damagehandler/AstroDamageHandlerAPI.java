package net.astropvp.damagehandler;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class AstroDamageHandlerAPI {
    private final AstroDamageHandler plugin;
    private boolean debug;

    public AstroDamageHandlerAPI(AstroDamageHandler plugin) {
        this.plugin = plugin;
        this.debug = plugin.getConfig().getBoolean("debug-enabled");
    }


    public void increaseDamage(double amountInPercentage, EntityDamageByEntityEvent event, String instance) {
        double increaseAmount = this.getCorrectIncreasePercentage(amountInPercentage);
        increaseDamage(amountInPercentage, event, instance, increaseAmount);
    }

    public void increaseDamage(double amountInPercentage, EntityDamageByEntityEvent event, String instance, int crystalsAmount) {
        double increaseAmount = this.getCorrectIncreasePercentage(amountInPercentage * crystalsAmount);
        increaseDamage(amountInPercentage, event, instance, increaseAmount);
    }

    public void cancelDamage(EntityDamageByEntityEvent event, String instance) {
        event.setDamage(0);
        if (event.getEntity() instanceof Player)
        this.sendDebugMessage(event.getEntity(), "Cancelled Damage (" + instance  + ")");

    }

    public void cancelDamage(EntityDamageEvent event, String instance) {
        event.setDamage(0);
        if (event.getEntity() instanceof Player)
            this.sendDebugMessage(event.getEntity(), "Cancelled Damage (" + instance  + ")");
    }

    private void increaseDamage(double amountInPercentage, EntityDamageByEntityEvent event, String instance, double increaseAmount) {
        if (!WorldGuardUtils.isPvPEnabled(event.getEntity().getLocation()) || !WorldGuardUtils.isPvPEnabled(event.getDamager().getLocation())) return;
        double oldDamage = event.getDamage();
        double newDamage = oldDamage * increaseAmount;
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                if (this.debug) {
                    LivingEntity entity = (LivingEntity) event.getEntity();
                    double oldHealth = entity.getHealth();
                    double finalNewDamage = newDamage;
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        if (!entity.isDead()) {
                            double healthDifference = this.getHealthDifference(entity.getHealth(), oldHealth);
                            String color = healthDifference > 9.0D ? "&4&l" : "&c&l";
                            this.sendDebugMessage(event.getDamager(), "&a&l↑ Outgoing Damage &7(&a+" + Math.round(amountInPercentage) + "%&7) &7(&c" + (int) oldDamage + " &7-> &a" + (int) finalNewDamage + " &7[&f" + entity.getName() + ": " + color + "-" + (int) healthDifference + "HP&7]) &7(Instance: " + instance + "&7)");
                        }

                    }, 1L);
                }

                if (newDamage > 17) {
                    newDamage *= 0.75;
                }
                event.setDamage(newDamage);
            }
        }
    }

    public void reduceDamage(double amountInPercentage, EntityDamageByEntityEvent event, String instance, int crystalsAmount) {
        double decreaseAmount = this.getCorrectDecreasePercentage(amountInPercentage * crystalsAmount);
        reduceDamage(amountInPercentage, event, instance, decreaseAmount);
    }

    private void reduceDamage(double amountInPercentage, EntityDamageByEntityEvent event, String instance, double decreaseAmount) {
        if (!WorldGuardUtils.isPvPEnabled(event.getEntity().getLocation()) || !WorldGuardUtils.isPvPEnabled(event.getDamager().getLocation())) return;

        double oldDamage = event.getDamage();
        double newDamage = event.getDamage() * decreaseAmount;
        if (this.debug) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            double oldHealth = entity.getHealth();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!entity.isDead()) {
                    double healthDifference = this.getHealthDifference(entity.getHealth(), oldHealth);
                    String color = healthDifference > 9.0D ? "&4&l" : "&c&l";
                    this.sendDebugMessage(event.getDamager(), "&c&l↓ Outgoing Damage &7(&c-" + Math.round(amountInPercentage) + "%&7) &7(&a" + (int) oldDamage + " &7-> &c" + (int) newDamage + ") &7(Instance: &f&l" + entity.getName() + "'s " + instance + "&7)");
                }

            }, 1L);
        }

        event.setDamage(newDamage);
    }

    public void reduceDamage(double amountInPercentage, EntityDamageEvent event, String instance, int crystalsAmount) {
        double decreaseAmount = this.getCorrectDecreasePercentage(amountInPercentage * crystalsAmount);
        reduceDamage(amountInPercentage, event, instance, decreaseAmount);
    }

    private void reduceDamage(double amountInPercentage, EntityDamageEvent event, String instance, double decreaseAmount) {
        if (!WorldGuardUtils.isPvPEnabled(event.getEntity().getLocation())) return;

        double oldDamage = event.getDamage();
        double newDamage = event.getDamage() * decreaseAmount;
        if (this.debug) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            double oldHealth = entity.getHealth();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!entity.isDead()) {
                    double healthDifference = this.getHealthDifference(entity.getHealth(), oldHealth);
                    String color = healthDifference > 9.0D ? "&4&l" : "&c&l";
                    this.sendDebugMessage(event.getEntity(), "&c&l↓ Outgoing Damage &7(&c-" + Math.round(amountInPercentage) + "%&7) &7(&a" + (int) oldDamage + " &7-> &c" + (int) newDamage + ") &7(Instance: &f&l" + entity.getName() + "'s " + instance + "&7)");
                }

            }, 1L);
        }

        event.setDamage(newDamage);
    }

    public void reduceDamage(double amountInPercentage, EntityDamageByEntityEvent event, String instance) {
        double decreaseAmount = this.getCorrectDecreasePercentage(amountInPercentage);
        reduceDamage(amountInPercentage, event, instance, decreaseAmount);
    }

    public void reduceDamage(double amountInPercentage, EntityDamageEvent event, String instance) {
        double decreaseAmount = this.getCorrectDecreasePercentage(amountInPercentage);
        reduceDamage(amountInPercentage, event, instance, decreaseAmount);
    }

    public void dealDamage(Player to, double amount, String instance) {
        double getNewHealth = to.getHealth() - amount;
        if (this.debug) {
            this.sendDebugMessage(to, "&c&l- " + amount + " DMG &7(Instance: " + instance + "&7)");
        }

        to.setHealth(getNewHealth);
    }

    public void dealDamage(LivingEntity to, double amount, String instance) {
        if (!WorldGuardUtils.isPvPEnabled(to.getLocation())) return;

        double getNewHealth = to.getHealth() - amount;
        if (this.debug && to instanceof Player) {
            this.sendDebugMessage((Entity) to, "&c&l- " + amount + " DMG &7(Instance: " + instance + "&7)");
        }

        to.setHealth(getNewHealth);
    }

    public void healPlayer(LivingEntity entity, double healAmount, String instance) {
        this.setPlayerHealth(entity, entity.getHealth() + healAmount);
        if (this.debug) {
            this.sendDebugMessage((Entity) entity, "&a&l+ " + healAmount + " HP &7(Instance: " + instance + "&7)");
        }

    }

    private void sendDebugMessage(Entity entity, String message) {
            entity.sendMessage(CC.translate(message));

        Bukkit.getConsoleSender().sendMessage(CC.Aqua + CC.translate(message));

    }

    private void sendDebugMessage(Player player, String message) {
            player.sendMessage(message);

        Bukkit.getConsoleSender().sendMessage(CC.Aqua + CC.translate(message));

    }

    private Double getCorrectIncreasePercentage(double value) {
        return value < 100.0D ? 0.01D * value + 1.0D : 0.01D * value;
    }

    private Double getCorrectDecreasePercentage(double value) {
        value = 0.01D * value;
        value = 1.0D - value;
        return value;
    }

    private Double getHealthDifference(double healthBeforeModified, double healthAfterModified) {
        double health = (double) Math.round(healthBeforeModified - healthAfterModified);
        health = Math.abs(health);
        return health;
    }


    private void setPlayerHealth(LivingEntity entity, double newHealth) {
        double health = entity.getHealth();
        if (!(health <= 0.0D) && !entity.isDead() || !(entity instanceof Player)) {
            if (newHealth > entity.getMaxHealth()) {
                newHealth = entity.getMaxHealth();
            }

            entity.setHealth(newHealth);
        }
    }


}
