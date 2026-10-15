package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.listeners.CustomEnchantListener;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.arkhamnetwork.enchantments.util.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MasteryPermafrost extends MasteryCustomEnchantment {
    private Map<Block, PlayerMaterialData> blocksToRestore = new ConcurrentHashMap<>();

    public MasteryPermafrost() {
        super("Permafrost", GeneralUtil.armor, 10);
        this.max = 6;
        this.armorSet = "DEATH_KNIGHT";
        Bukkit.getPluginManager().registerEvents((Listener) new PermaFrostListener(this), (Plugin) Enchantments.getInstance());
    }

    public void onDisable() {
        this.blocksToRestore.forEach((block, type) -> block.setTypeIdAndData(type.getData().getItemTypeId(), type.getData().getData(), false));
        this.blocksToRestore.clear();
    }

    public void applyDefenseEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageEvent event) {
        /*double chance = 0.01D + enchantLevel / 3.0D * 0.005D;
        if (Math.random() <= chance) {
            int radius = Math.min(2 + enchantLevel, 5);
            Player damaged = (Player) user;
            List<Block> blocks = Lists.newArrayList();
            int duration = (int) Math.ceil(4.0D + enchantLevel / 3.0D);
            Block meBlock = damaged.getLocation().getBlock();
            Location bl = meBlock.getLocation();
            int sq = radius * radius;
            int endX = meBlock.getX() + radius, endY = meBlock.getY(), endZ = meBlock.getZ() + radius;
            int startX = meBlock.getX() - radius, startY = meBlock.getY() - 2, startZ = meBlock.getZ() - radius;
            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    for (int z = startZ; z < endZ; z++) {
                        Location loc = new Location(meBlock.getWorld(), x, y, z);
                        if (loc.distanceSquared(bl) <= sq) {
                            Block block = loc.getBlock();
                            Material type = block.getType();
                            if (type.isBlock() && type.isSolid() && block.getRelative(0, 1, 0).isEmpty() &&
                                    !this.blocksToRestore.containsKey(block)) {
                                Faction faction = FactionUtils.getFactionAt(block.getLocation());
                                if (!WorldGuardUtils.isPvPDisabled(block.getLocation()) && faction != null && faction.isWarZone())
                                    blocks.add(block);
                            }
                        }
                    }
                }
            }
            for (Block block : blocks) {
                MaterialData current = new MaterialData(block.getType(), block.getData());
                this.blocksToRestore.put(block, new PlayerMaterialData(current, damaged.getUniqueId(), enchantLevel));
                Material material = Material.SNOW_BLOCK;
                block.setTypeId(material.getId(), false);
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Enchantments.getInstance(), () -> {
                for (Block block : blocks) {
                    PlayerMaterialData data = this.blocksToRestore.remove(block);
                    if (data != null)
                        block.setTypeIdAndData(data.getData().getItemTypeId(), data.getData().getData(), false);
                }
            }, (20 * duration));
            if (!blocks.isEmpty()) {
                damaged.sendMessage(CC.RedB + "* PERMAFROST [" + CC.Gray + duration + "s" + CC.RedB + "] *");
                damaged.getWorld().playSound(damaged.getLocation(), Sound.GLASS, 3.0F, 1.1F);
            }
            Bukkit.getLogger().info("Permafrosting near " + damaged.getName() + " affected: " + blocks.size());
        }*/
    }

    class PlayerMaterialData {
        private MaterialData data;

        private UUID owner;

        private int level;

        public PlayerMaterialData(MaterialData data, UUID owner, int level) {
            this.data = data;
            this.owner = owner;
            this.level = level;
        }

        public MaterialData getData() {
            return this.data;
        }

        public UUID getOwner() {
            return this.owner;
        }

        public int getLevel() {
            return this.level;
        }
    }

    class PermaFrostListener extends CustomEnchantListener<MasteryPermafrost> {
        public PermaFrostListener(MasteryPermafrost enchant) {
            super(enchant);
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onEntityDamage(EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player) {
                Player damager = null;
                if (event.getDamager() instanceof Player) {
                    damager = (Player) event.getDamager();
                } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    damager = (Player) ((Projectile) event.getDamager()).getShooter();
                }
                if (damager != null) {
                    Player damaged = (Player) event.getEntity();
                    Block underneath = damaged.getLocation().getBlock().getRelative(0, -1, 0);
                    MasteryPermafrost.PlayerMaterialData stored = (MasteryPermafrost.PlayerMaterialData) ((MasteryPermafrost) this.customEnchant).blocksToRestore.get(underneath);
                    if (stored != null && stored.getOwner().equals(damaged.getUniqueId())) {
                        double toReduce = (14 + stored.getLevel());
                        event.setDamage(event.getDamage() * (1.0D - 0.01D * toReduce));
                        //damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 50, 1), true);
                        Bukkit.getLogger().info("Reducing damage for " + damaged.getName() + " by " + toReduce + "% damaged by " + damager.getName());
                    }
                }
            }
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onBlockMelt(BlockFadeEvent event) {
            if (((MasteryPermafrost) this.customEnchant).blocksToRestore.containsKey(event.getBlock()))
                event.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onBlockBreak(BlockBreakEvent event) {
            if (((MasteryPermafrost) this.customEnchant).blocksToRestore.containsKey(event.getBlock()))
                event.setCancelled(true);
        }
    }
}
