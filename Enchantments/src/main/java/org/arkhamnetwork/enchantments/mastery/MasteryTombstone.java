package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.utils.*;
import com.massivecraft.factions.Faction;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.*;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.listeners.CustomEnchantListener;
import org.arkhamnetwork.enchantments.util.ArmorSlot;
import org.arkhamnetwork.enchantments.util.ArmorUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class MasteryTombstone extends MasteryCustomEnchantment {
    public MasteryTombstone() {
        super("Tombstone", GeneralUtil.armor, 10);
        this.max = 10;
        this.armorSet = "DEATH_KNIGHT";
        //Bukkit.getPluginManager().registerEvents((Listener) new TombstoneSanityListener(this), (Plugin) Enchantments.getInstance());
    }

    @SneakyThrows
    public void applyDefenseEffect(final LivingEntity user, final LivingEntity target, final int enchantLevel, final EntityDamageEvent event) {
        /*if (!(target instanceof Player)) {
            return;
        }
        final Player damaged = (Player) user;
        if (Math.random() <= 0.02 * (enchantLevel / 3.0)) {
            if (!new CustomEnchantProcOnEntityEvent(user, target, (Event) event, this, enchantLevel).callEvent()) {
                return;
            }
            final int toEffect = 4 + enchantLevel / 2;
            int did = 0;
            final Faction faction = FactionUtils.getFaction(damaged, true);
            final double radius = 6 + enchantLevel;
            final UUID uuid = damaged.getUniqueId();
            final String name = damaged.getName();
            int affected = 0;
            damaged.getWorld().playSound(damaged.getLocation(), Sound.ANVIL_LAND, 4.0f, 1.9f);
            for (final Entity entity : damaged.getNearbyEntities(radius, radius, radius)) {
                if (!(entity instanceof Player)) {
                    continue;
                }
                final Player near = (Player) entity;
                if (!this.canEffectPlayer(near)) {
                    continue;
                }
                if (GeneralUtil.isAtleastTruce(faction, near)) {
                    continue;
                }
                if (did >= toEffect) {
                    break;
                }
                ++did;
                final long durationTicks = 60L;
                final long silenceDuration = 80L;
                near.setWalkSpeed(0.0f);
                near.removePotionEffect(PotionEffectType.JUMP);
                near.removePotionEffect(PotionEffectType.SLOW);
                near.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, (int) durationTicks, 128));
                near.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) durationTicks, 128));
                Player player = (Player) target;
                Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), () -> {
                    player.setWalkSpeed(0.2f);
                    player.removePotionEffect(PotionEffectType.SLOW);
                    return;
                }, durationTicks);
                near.playSound(near.getLocation(), Sound.WITHER_HURT, 1.0f, 0.25f);
                if (near.hasMetadata("immune_silence") && Math.random() < 0.75) {
                    near.sendMessage(CC.RedB + "* SILENCE BLOCKED [" + CC.Gray + damaged.getName() + CC.RedB + "] *");
                } else {
                    near.setMetadata("noDefenseProcs", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) true));
                    de.inventivegames.particle.ParticleEffect.ENCHANTMENT_TABLE.sendToPlayers(Bukkit.getOnlinePlayers(), near.getEyeLocation(), 0,0,0,0.4f,20);
                    de.inventivegames.particle.ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), near.getEyeLocation(), 0,0,0,0.4f,20);
                }
                ++affected;
                near.sendMessage(CC.DarkPurpleB + "* TOMBSTONE (" + CC.Gray + damaged.getName() + " " + ChatColor.GRAY + "[" + silenceDuration / 20L + "s]" + CC.DarkPurpleB + ") *");
                final Player player2 = (Player) user;
                Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), () -> {
                    player2.removeMetadata("noDefenseProcs", (Plugin) Enchantments.getInstance());
                    try {
                        de.inventivegames.particle.ParticleEffect.ENCHANTMENT_TABLE.sendToPlayers(Bukkit.getOnlinePlayers(), near.getEyeLocation(), 0,0,0,0.4f,20);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        de.inventivegames.particle.ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), near.getEyeLocation(), 0,0,0,0.4f,20);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }, silenceDuration);
                final Location loc = near.getLocation();
                final WorldServer world = ((CraftWorld) damaged.getWorld()).getHandle();
                near.playSound(near.getLocation().add(0.0, 5.0, 0.0), Sound.ANVIL_LAND, 3.0f, 1.1f);
                final EntityFallingBlock block = new EntityFallingBlock(world, loc.getX(), loc.getY() + 5.0, loc.getZ(), (IBlockData) Blocks.ANVIL) {
                    protected void b(final float f) {
                        if (this.dead) {
                            return;
                        }
                        final List near = this.world.getEntities(this, this.getBoundingBox());
                        this.world.getWorld().playSound(this.getBukkitEntity().getLocation(), Sound.ANVIL_LAND, 2.0f, 1.1f);
                        for (final Object ent : near) {
                            if (ent instanceof EntityHuman) {
                                final Player pl = (Player) ((EntityHuman) ent).getBukkitEntity();
                                if (pl == null) {
                                    continue;
                                }
                                if (pl.getUniqueId().equals(uuid)) {
                                    continue;
                                }
                                if (!MasteryTombstone.this.canEffectPlayer(pl)) {
                                    continue;
                                }
                                if (GeneralUtil.isAtleastTruce(faction, pl)) {
                                    continue;
                                }
                                for (final ArmorSlot slot : ArmorSlot.values()) {
                                    ArmorUtil.damagePlayerArmorByPercent(pl, slot, 10.0);
                                }
                                pl.updateInventory();
                                pl.playEffect(EntityEffect.HURT);
                                pl.playSound(pl.getLocation(), Sound.ZOMBIE_WOODBREAK, 5.0f, 0.8f);
                                pl.getWorld().playSound(pl.getLocation(), Sound.ZOMBIE_WOOD, 5.0f, 1.1f);
                                pl.sendMessage(CC.RedB + "* TOMBSTONE [" + CC.Gray + name + " (-10% Armor Durability)" + CC.RedB + "] *");
                                Bukkit.getLogger().info("Tombstoning " + pl.getName() + "!");
                            }
                        }
                        this.die();
                    }
                };
                block.a(block.dropItem = false);
                block.ticksLived = 1;
                ReflectionUtils.setObject((Object) block, ReflectionUtils.getField((Class) EntityFallingBlock.class, "f"), true);
                world.addEntity(block, CreatureSpawnEvent.SpawnReason.CUSTOM);
                block.getBukkitEntity().setMetadata("no_block", (MetadataValue) new FixedMetadataValue(Enchantments.getInstance(), (Object) ""));
            }
            damaged.sendMessage(CC.RedB + "* TOMBSTONE [" + CC.Gray + affected + " players" + CC.RedB + "] *");
        }
    }

    class TombstoneSanityListener extends CustomEnchantListener<MasteryTombstone> {
        public TombstoneSanityListener(final MasteryTombstone enchant) {
            super(enchant);
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onEntityChange(final EntityChangeBlockEvent event) {
            if (event.getEntity().hasMetadata("no_block")) {
                //event.setCancelled(true);
            }
        }
    }*/
    }
}
