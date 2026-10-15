package me.venir.astrocollectors.listeners.collectorlisteners;

import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.utils.MobStackerUtils;
import me.astropvp.astrospawners.utils.StackedMobDeathEvent;
import me.venir.astrocollectors.AstroCollectors;
import me.venir.astrocollectors.collector.Collector;
import me.venir.astrocollectors.collector.CollectorManager;
import net.techcable.tacospigot.event.entity.SpawnerPreSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;


public class StackedMobDeathListener implements Listener {

    AstroCollectors plugin;
    public StackedMobDeathListener(AstroCollectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onStackedEntityDeath(StackedMobDeathEvent e) {
/*        Entity entity = e.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        if (entity instanceof Blaze) return;
        int stackSize = AstroSpawners.getInstance().getMobStackerUtils().getStackSize(entity);
        if (!(e.getDamager() instanceof Player)) {
            Location location = entity.getLocation();
            if (!CollectorManager.DELEGATE.isCollectorContained(location)) return;
            Collector collector = CollectorManager.DELEGATE.getCollector(location);
            ItemStack itemStack = e.getDrops(entity.getType());
            if (itemStack == null) return;

            if (collector.getTotalMobDropAmount() + (stackSize*4) >= Integer.MAX_VALUE) return;

            collector.incrementAmount(itemStack.getType(), stackSize);
            AstroSpawners.getInstance().getMobStackerUtils().removeStackedMob(entity);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Creature)) return;
        if (e.getEntity().getWorld().getName().contains("end")) return;
        StackedMobDeathEvent stackedMobDeathEvent = new StackedMobDeathEvent(e.getEntity(), null);
        Bukkit.getPluginManager().callEvent(stackedMobDeathEvent);
        e.getDrops().clear();
        e.setDroppedExp(0);

 */
    }
    @EventHandler
    public void stopMobSpawn(SpawnerSpawnEvent e) {
        if (e.getEntityType() == EntityType.BLAZE) return;
        e.setCancelled(true);
    }
}



/*
public class StackedMobDeathListener implements Listener {

    AstroCollectors plugin;
    List<EntityType> noCollectorMobs;
    public StackedMobDeathListener(AstroCollectors plugin) {
        this.plugin = plugin;
        noCollectorMobs = new ArrayList<>();
        noCollectorMobs.add(EntityType.BLAZE);
    }

    @EventHandler
    public void onStackedEntityDeath(StackedMobDeathEvent e) {
        Entity entity = e.getEntity();
        if (!(e.getDamager() instanceof Player)) {
            Location location = entity.getLocation();
            int amount = AstroSpawners.getInstance().getMobStackerUtils().getStackSize(entity);
            if (!CollectorManager.DELEGATE.isCollectorContained(location)) return;
            Collector collector = CollectorManager.DELEGATE.getCollector(location);
            ItemStack itemStack = e.getDrops(entity.getType());
            itemStack.setAmount(itemStack.getAmount()*4);
            if (itemStack == null) return;
            collector.incrementAmount(itemStack.getType(), amount * 200);
            AstroSpawners.getInstance().getMobStackerUtils().removeStackedMob(entity);

        }

    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntitySpawn(SpawnerSpawnEvent e) {
        if (noCollectorMobs.contains(e.getEntityType())) {
            return;
        }

        if (e.getEntity().hasMetadata("end_grinding_mob") || e.getEntity().hasMetadata("end_skeleton_mob") || e.getEntity().hasMetadata("end_creeper_mob")) {
            return;
        }

        if (!(e.getEntity() instanceof Creature)) return;

        //if (!AstroSpawners.getInstance().getMobStackerUtils().isStackedMob(e.getEntity())) return;
        final StackedMobDeathEvent stackedMobDeathEvent = new StackedMobDeathEvent(e.getEntity(), null);
        Bukkit.getPluginManager().callEvent(stackedMobDeathEvent);
        e.setCancelled(true);
    }

}
*/