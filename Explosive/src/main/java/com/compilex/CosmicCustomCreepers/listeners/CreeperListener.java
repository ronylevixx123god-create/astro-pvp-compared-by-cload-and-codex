
package com.compilex.CosmicCustomCreepers.listeners;

import com.compilex.CosmicCustomCreepers.enums.CreeperTypes;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.FLocation;
import com.rit.sucy.EnchantmentAPI;
import lombok.SneakyThrows;
import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.configs.DeleteSpawner;
import me.astropvp.astrospawners.data.SpawnerData;
import me.astropvp.astrospawners.items.ItemSpawner;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.arkhamnetwork.CosmicCustomTNT.CosmicCustomTNT;
import org.arkhamnetwork.enchantments.mastery.MasteryRotAndDecay;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreeper;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

public class CreeperListener
        implements Listener {
    private boolean coreProtectEnabled = false;
    private final List<Material> nonSolidBlocks;
    public static final Map<UUID, CreeperTypes> creepers = new HashMap<>();

    public CreeperListener() {
        this.coreProtectEnabled = new File("plugins/CoreProtect.jar").exists();
        this.nonSolidBlocks = new ArrayList<Material>(Arrays.asList(Material.AIR, Material.CHEST, Material.OBSIDIAN));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerUseCreeperEgg(PlayerInteractEvent e) {
        CreeperTypes ct;
        if (e.isCancelled() && e.useItemInHand() != Event.Result.ALLOW) {
            return;
        }
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && (ct = this.getCCType(e.getItem())) != null) {
            this.processCustomCreeperPlaced(e, ct);
        }
    }

    public void processCustomCreeperPlaced(PlayerInteractEvent e, CreeperTypes ct) {
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            Location spawnLoc;
            Block b;
            e.setCancelled(true);
            Player p = e.getPlayer();
            Block chestBlock = null;
            try {
                Material m2;
                try {
                    b = e.getClickedBlock().getY() < 254 ? p.getTargetBlock((HashSet<Byte>) null, 4) : e.getClickedBlock();
                } catch (Exception err) {
                    b = e.getClickedBlock();
                }
                if (e.getClickedBlock().getY() >= 254 && e.getBlockFace() != BlockFace.UP) {
                    return;
                }
                if (WorldGuardUtils.isPvPDisabled(b.getLocation())) {
                    return;
                }
                Material m1 = b.getType();
                m2 = b.getY() >= 254 ? b.getType() : b.getLocation().add(0.0, 1.0, 0.0).getBlock().getType();
                if (b.getY() < 254 && m1 != Material.AIR && m1 != Material.CHEST && m1 != Material.TRAPPED_CHEST && m1 != Material.BEDROCK && m2 != Material.AIR && m2 != Material.CHEST && m2 != Material.TRAPPED_CHEST && m2 != Material.BEDROCK) {
                    Bukkit.getLogger().info("[CosmicCustomCreepers] Blocking cegg due to non air/chest block above spawn location. m=" + m1 + ", m+1=" + m2);
                    return;
                }
                if (m1 == Material.CHEST || m2 == Material.CHEST || m1 == Material.TRAPPED_CHEST || m2 == Material.TRAPPED_CHEST) {
                    chestBlock = m1 == Material.CHEST || m1 == Material.TRAPPED_CHEST ? e.getClickedBlock() : e.getClickedBlock().getLocation().add(0.0, 1.0, 0.0).getBlock();
                }
                spawnLoc = b.getLocation().getY() >= 255.0 ? b.getLocation() : b.getLocation().add(0.0, 1.0, 0.0);
                if (spawnLoc.getBlock().isLiquid() || spawnLoc.clone().add(0.0, 1.0, 0.0).getBlock().isLiquid()) {
                    Bukkit.getLogger().info("[CosmicCustomCreepers] Blocking custom cegg due to LIQUID at y or y+1.");
                    return;
                }
                if (chestBlock == null && spawnLoc.getBlock().getType() == Material.MOB_SPAWNER) {
                    Bukkit.getLogger().info("[CosmicCustomCreepers] Blocking custom cegg due to MOB_SPAWNER at y+1.");
                    return;
                }
                ItemStack egg = e.getItem();
                if (egg.getAmount() > 1) {
                    egg.setAmount(egg.getAmount() - 1);
                    p.setItemInHand(egg);
                } else {
                    p.setItemInHand(null);
                }
                spawnLoc.add(0.5, 0.0, 0.5);
            } catch (Exception err) {
                err.printStackTrace();
                return;
            }
            Location toSpawn = chestBlock != null ? chestBlock.getLocation() : spawnLoc;
            Bukkit.getLogger().info("[CosmicCustomCreeper] (" + ct.name() + ") spawnLocation: " + toSpawn);
            Creeper c = (Creeper) b.getWorld().spawnEntity(toSpawn, EntityType.CREEPER);

            c.setCustomName(ct.getMobName());
            c.setCustomNameVisible(true);

            creepers.put(c.getUniqueId(), ct);
            this.logPlacement(toSpawn.getBlock(), p.getName(), (byte) ct.ordinal());
        }
    }

    private void logPlacement(Block block, String user, byte data) {
        if (!this.coreProtectEnabled) {
            return;
        }
        try {
            CoreProtectAPI api = ((CoreProtect) Bukkit.getPluginManager().getPlugin("CoreProtect")).getAPI();
            if (api != null) {
                api.logPlacement(user, block.getLocation(), Material.MONSTER_EGG.getId(), data);
                Bukkit.getLogger().info("[CustomCreeper] tracking placement location for " + user + "!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SneakyThrows
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomCreeperExplode(EntityExplodeEvent e) throws NoSuchFieldException, IllegalAccessException {
        if (e.getEntity() == null || !creepers.containsKey(e.getEntity().getUniqueId())) {
            return;
        }
        Creeper c = (Creeper) e.getEntity();
        if (creepers.containsKey(c.getUniqueId())) {
            CreeperTypes egg_type = creepers.get(c.getUniqueId());
            Bukkit.getLogger().info("[CosmicCustomCreepers] EntityExplodeEvent for CustomCreeperEgg: " + egg_type.name() + " entity: " + c);

            switch (egg_type) {
                case Huge:
                    Location loc = c.getLocation();
                    this.spawnFastExplosion(loc.clone().add(0.0, 3.0, 0.0));
                    this.spawnFastExplosion(loc.clone().add(0.0, -3.0, 0.0));
                    if (new Random().nextBoolean()) {
                        this.spawnFastExplosion(loc.clone().add(3.0, 0.0, 0.0));
                        this.spawnFastExplosion(loc.clone().add(0.0, 0.0, 3.0));
                    } else {
                        this.spawnFastExplosion(loc.clone().add(-3.0, 0.0, 0.0));
                        this.spawnFastExplosion(loc.clone().add(0.0, 0.0, -3.0));
                    }
                    break;
                case Lucky:
                    //This is handled in the spawners plugin (SpawnerBreakListener)
                    break;
                case Raid:
                    //This is handled in the spawners plugin (SpawnerBreakListener)
                    break;
                default:
                    Bukkit.getLogger().info("[CosmicCustomCreeper] No Creeper type found!");

            }


        }
    }

    private CreeperTypes getCCType(ItemStack creeper_egg) {
        if (!(creeper_egg != null && creeper_egg.hasItemMeta() && creeper_egg.getItemMeta().hasDisplayName() && creeper_egg.getItemMeta().hasLore())) {
            return null;
        }
        CreeperTypes tnt_type = null;
        String name = creeper_egg.getItemMeta().getDisplayName();
        for (CreeperTypes tt : CreeperTypes.values()) {
            if (!name.equals(tt.getItemName())) continue;
            tnt_type = tt;
            break;
        }
        return tnt_type;
    }

    private void spawnFastExplosion(Location l) {
        if (this.nonSolidBlocks.contains(l.getBlock().getType()) || l.getBlock().isLiquid()) {
            return;
        }
        CraftCreeper creeper = l.getWorld().spawn(l, CraftCreeper.class);
        net.minecraft.server.v1_8_R3.Entity nms = ((CraftEntity) creeper).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        nms.c(nbt);
        nbt.setInt("Fuse", 0);
        EntityLiving living = (EntityLiving) nms;
        living.a(nbt);
    }
}

