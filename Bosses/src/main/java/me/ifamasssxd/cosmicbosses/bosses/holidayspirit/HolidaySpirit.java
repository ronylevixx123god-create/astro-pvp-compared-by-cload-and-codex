package me.ifamasssxd.cosmicbosses.bosses.holidayspirit;

import com.compilex.CosmicArmorSets.CosmicArmorSetsAPI;
import com.compilex.CosmicArmorSets.enums.ArmorSet;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.entities.HolidaySpiritEntity;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossMessage;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HolidaySpirit extends Boss {
    Freeze freezeAbility;
    private int hitCounter;
    List<String> msg;

    public HolidaySpirit(final Location location, final BossType bossType) {
        super(location, bossType);
        this.hitCounter = 0;
        this.msg = (List<String>) Lists.newArrayList(new String[]{"Feel the spirit of the holidays consume you!", "This is so much FUN! Are you having fun [NAME]?!", "You're in for a real treat!", "Happy Halloween! Thankful Thanksgiving! Merry Christmas! Buhahaha!"});
        (this.freezeAbility = new Freeze(this)).activateCooldown();
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.LEGENDARY));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.ULTIMATE));
        this.getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.ULTIMATE));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SNOWMAN;
    }

    @Override
    public String getCustomName() {
        return ChatColor.GOLD + ChatColor.BOLD.toString() + this.getName();
    }

    public LivingEntity getTarget() {
        final Creature creature = (Creature) this.getEntity();
        return creature.getTarget();
    }

    @Override
    public LivingEntity spawnBoss() {
        final CraftWorld world = (CraftWorld) this.location.getWorld();
        final HolidaySpiritEntity entity = new HolidaySpiritEntity((World) world.getHandle(), this);
        entity.setPosition(this.location.getX(), this.location.getY(), this.location.getZ());
        world.getHandle().addEntity((Entity) entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        final LivingEntity ent = (LivingEntity) entity.getBukkitEntity();
        this.setupBoss(ent);
        return ent;
    }

    @Override
    public String getName() {
        return "Holiday Spirit";
    }

    @Override
    public void cleanup() {
        CosmicBosses.getInstance().getBlockChangeManager().restoreAllBlocks(this.getEntity().getEntityId());
        this.removeMinions();
    }

    @Override
    public void onUpdate() {
        ++this.loops;
        final LivingEntity ent = this.getEntity();
        final Location loc = ent.getLocation();
        if (this.freezeAbility.canUse()) {
            this.freezeAbility.onAbility();
            this.sendMessage(ChatColor.GOLD + "You look like you need to CHILL out!");
            for (final org.bukkit.entity.Entity enti : this.getNearbyEntities(20.0, EntityType.PLAYER)) {
                final Player player = (Player) enti;
                player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 10.0f, 0.8f);
            }
        }
        if (this.loops % this.getSeconds(10) == 0) {
            final String msg = this.getRandomMessage();
            for (final org.bukkit.entity.Entity et : this.getEntity().getNearbyEntities(32.0, 32.0, 32.0)) {
                if (et instanceof Player) {
                    final Player player2 = (Player) et;
                    this.sendMessage(player2, ChatColor.GOLD + msg);
                }
            }
        }
        final Block under = loc.getBlock().getRelative(BlockFace.DOWN);
        final Block current = loc.getBlock();
        if (current.getType() == Material.SNOW && under.getType() == Material.ICE) {
            ent.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 2), true);
        } else {
            ent.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
        for (final org.bukkit.entity.Entity minion : this.getMinions()) {
            if (minion.isValid() && minion.getLocation().distanceSquared(this.getEntity().getLocation()) > 300.0) {
                final Location location = BossUtils.getRandomLocationNearby(this.getEntity().getLocation(), 5, 1, 3, true);
                minion.teleport(location.add(0.0, 1.0, 0.0));
            }
        }
        if (this.loops % this.getSeconds(4) == 0) {
            this.updateScoreboard();
        }
    }

    @Override
    public String getTitle() {
        return "Spirit";
    }

    @Override
    public void onPhaseChange(final Phase newPhase) {
    }

    @Override
    public org.bukkit.entity.Entity spawnMinion(final EntityType type, final Location l) {
        final org.bukkit.entity.Entity entity = super.spawnMinion(type, l);
        final LivingEntity livingEntity = (LivingEntity) entity;
        if (livingEntity instanceof Zombie) {
            ((Zombie) livingEntity).setBaby(true);
        }
        livingEntity.setCustomName(ChatColor.RED + ChatColor.BOLD.toString() + "Holiday Ghoul");
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        final ItemStack minWeapon = new ItemStack(Material.DIAMOND_SWORD, 1);
        minWeapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        livingEntity.getEquipment().setItemInHand(minWeapon);
        livingEntity.getEquipment().setHelmet(new ItemStack(Material.JACK_O_LANTERN, 1));
        livingEntity.setMaxHealth(65.0);
        livingEntity.setHealth(65.0);
        return entity;
    }

    @Override
    public void onMinionUpdate(final org.bukkit.entity.Entity minion, final Event e) {
        if (e instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (event.getDamager() instanceof Player) {
                final Player damager = (Player) event.getDamager();
                if (ThreadLocalRandom.current().nextBoolean()) {
                    damager.damage(((EntityDamageByEntityEvent) e).getFinalDamage());
                    damager.playSound(damager.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.2f, 2.0f);
                }
            } else if (event.getDamager().equals(minion) && event.getEntity() instanceof Player) {
                final Player player = (Player) event.getEntity();
                if (ThreadLocalRandom.current().nextInt(10) <= 4) {
                    final ItemStack handItem = player.getItemInHand();
                    final int inHandSlot = player.getInventory().getHeldItemSlot();
                    final ItemStack held = player.getInventory().getItem(inHandSlot);
                    if (held == null || held.getType() == Material.AIR) {
                        return;
                    }
                    int slotToSwap;
                    for (slotToSwap = ThreadLocalRandom.current().nextInt(9); slotToSwap == inHandSlot; slotToSwap = ThreadLocalRandom.current().nextInt(9)) {
                    }
                    final ItemStack itemInNewSlot = player.getInventory().getItem(slotToSwap);
                    if (itemInNewSlot != null && itemInNewSlot.getType() != Material.AIR) {
                        player.setItemInHand(itemInNewSlot);
                    } else {
                        player.setItemInHand((ItemStack) null);
                    }
                    player.getInventory().setItem(slotToSwap, handItem);
                    player.updateInventory();
                    player.playSound(player.getLocation(), Sound.GHAST_SCREAM2, 1.0f, 1.9f);
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Holiday Ghoul >> " + ChatColor.RED + "You wanna see a trick?");
                }
            } else if (!(event.getDamager() instanceof Player)) {
                event.setCancelled(true);
            }
        } else if (e instanceof EntityDamageEvent) {
            final EntityDamageEvent event2 = (EntityDamageEvent) e;
            if (event2.getCause().name().startsWith("FIRE") || event2.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                event2.setCancelled(true);
            } else if (event2.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event2.setCancelled(true);
            }
        } else if (e instanceof EntityChangeBlockEvent) {
            ((EntityChangeBlockEvent) e).setCancelled(true);
        }
        super.onMinionUpdate(minion, e);
    }

    @Override
    public void onAttackPlayer(final EntityDamageByEntityEvent event, final Player player) {
        if (event.getDamager() instanceof Snowball) {
            event.setDamage((double) (7 + this.random.nextInt(2)));
            if (this.random.nextBoolean()) {
                player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.1f);
                //ParticleEffect.sendToLocation(ParticleEffect.LARGE_EXPLODE, player.getLocation().clone(), 1.0f, 1.0f, 1.0f, 0.01f, 5);
                try {
                    de.inventivegames.particle.ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), entity.getLocation(), 1f, 1f, 1f, 0.01f, 5);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
            if (!event.isCancelled()) {
                for (final org.bukkit.entity.Entity ent : player.getNearbyEntities(5.0, 3.0, 5.0)) {
                    if (ent instanceof Player) {
                        final Player other = (Player) ent;
                        if (!BossUtils.canAffectPlayer(other, true)) {
                            continue;
                        }
                        other.damage(5.0);
                    }
                }
                player.setVelocity(event.getDamager().getLocation().getDirection().multiply(1.65).add(new Vector(0.0, 0.3, 0.0)));
                if (ThreadLocalRandom.current().nextInt(5) <= 1 && this.getMinions().size() < 10) {
                    this.spawnMinion(EntityType.PIG_ZOMBIE, event.getDamager().getLocation().clone().add(0.0, 1.0, 0.0));
                }
            }
        } else if (event.getDamager() instanceof WitherSkull) {
            if (this.random.nextBoolean()) {
                player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 0.9f);
                //ParticleEffect.sendToLocation(ParticleEffect.LARGE_EXPLODE, player.getLocation().clone(), 1.0f, 1.0f, 1.0f, 0.01f, 5);
                try {
                    de.inventivegames.particle.ParticleEffect.LARGE_EXPLODE.sendToPlayer(player, player.getLocation().clone(), 1f, 1f, 1f, 0.01f, 5);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
            for (final org.bukkit.entity.Entity ent : event.getDamager().getNearbyEntities(10.0, 10.0, 10.0)) {
                if (ent instanceof Player) {
                    final Player other = (Player) ent;
                    if (!BossUtils.canAffectPlayer(other, true)) {
                        continue;
                    }
                    other.damage(10.0);
                    other.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1));
                    final Location loc = this.getEntity().getLocation();
                    Vector kb = player.getLocation().toVector().subtract(loc.toVector());
                    if (kb.length() != 0.0) {
                        kb = kb.normalize();
                    }
                    kb.multiply(3);
                    if (kb.getY() < 1.5) {
                        kb.setY(1.5);
                    }
                    other.setVelocity(kb);
                }
            }
        }
        super.onAttackPlayer(event, player);
    }

    @Override
    public void onDeath(final EntityDeathEvent event) {
        if (event != null) {
            this.sendMessage(ChatColor.GOLD + "You may have defeated me, but you will never be rid of holiday spirit.", Sound.ENDERDRAGON_DEATH, 1.3f);
            //ParticleEffect.sendToLocation(ParticleEffect.HUGE_EXPLOSION, event.getEntity().getLocation(), 5.0f, 5.0f, 5.0f, 0.01f, 100);
            try {
                de.inventivegames.particle.ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), event.getEntity().getLocation(), 1f, 1f, 1f, 0.01f, 5);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.EXPLODE, 10.0f, 1.0f);
            event.getEntity().getNearbyEntities(100.0, 50.0, 100.0).stream().filter(ent -> ent instanceof Player).forEach(ent -> {
                Player player = (Player) ent;
                if (BossUtils.canAffectPlayer(player, true)) {
                    player.setVelocity(player.getVelocity().add(new Vector(0, 3, 0)));
                }
                return;
            });
            final Location location = event.getEntity().getLocation();
            for (int i = 0; i < 100; ++i) {
                final int inStack = this.random.nextInt(6) + 10;
                i += inStack;

            }
            for (int cakes = 50 + this.random.nextInt(51), j = 0; j < cakes; ++j) {
                final int inStack2 = this.random.nextInt(6) + 10;
                j += inStack2;

            }
            if (ThreadLocalRandom.current().nextInt(100) == 0) {
                location.getWorld().dropItemNaturally(location, CosmicArmorSetsAPI.getArmorSet(ArmorSet.PHANTOM)[ThreadLocalRandom.current().nextInt(4)]);
                Bukkit.getLogger().info("Dropping Phantom Piece at " + location);
            }
            if (ThreadLocalRandom.current().nextInt(100) == 0) {
                Bukkit.getLogger().info("Dropping Halloween 2017 CC at " + location);
            }
        }
        super.onDeath(event);
    }

    private ItemStack addUniqueLore(final ItemStack item) {
        final ItemMeta im = item.getItemMeta();
        final List<String> lore = (List<String>) (im.hasLore() ? im.getLore() : Lists.newArrayList());
        lore.add(this.random.nextInt(1000000) + "");
        im.setLore((List) lore);
        item.setItemMeta(im);
        return item;
    }

    @Override
    public void onDamageTaken(final EntityDamageEvent event) {
        if (event.getCause().name().startsWith("FIRE") || event.getCause() == EntityDamageEvent.DamageCause.DROWNING || event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
        super.onDamageTaken(event);
    }

    @Override
    public void onDamageTaken(final EntityDamageByEntityEvent event, final org.bukkit.entity.Entity damager) {
        if (event.getDamager() instanceof Projectile && damager instanceof Player) {
            event.setCancelled(true);
            return;
        }
        if (damager instanceof Player && !((Player) damager).isOp()) {
            final Player player = (Player) damager;
            ++this.hitCounter;
            if (this.hitCounter >= 4) {
                this.hitCounter = 0;
            }
            double percentToReflect = 10.0;
            final double healthMissing = this.getMaxHealth() - this.getEntity().getHealth();
            final double percentageMissing = healthMissing / this.getMaxHealth() * 100.0;
            final double mult = percentageMissing / 15.0;
            percentToReflect = Math.min(90.0, percentToReflect * mult);
            player.damage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) * (percentToReflect * 0.01), (org.bukkit.entity.Entity) this.getEntity());
        }
        event.setDamage(event.getDamage(EntityDamageEvent.DamageModifier.BASE) / 2.0);
        super.onDamageTaken(event, damager);
    }

    @Override
    public void onMiscUpdate(final Event event) {
    }

    @Override
    public DamageType getDamageType() {
        return DamageType.FIRE;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.LEGENDARY_PLUS;
    }

    @Override
    public int getMaxHealth() {
        return 2000;
    }

    public String getRandomMessage() {
        return this.msg.get(this.random.nextInt(this.msg.size()));
    }

    @Override
    public List<BossMessage> getGeneralMessages() {
        return (List<BossMessage>) Lists.newArrayList(new BossMessage[]{new BossMessage("I've brought you something good to eat!", false, -1)});
    }
}
