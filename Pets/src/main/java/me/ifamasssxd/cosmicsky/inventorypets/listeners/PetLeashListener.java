package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicPersistentItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetLeashListener implements Listener {
    private final HashMap<String, List<ItemStack>> savedItems = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().getWorld().getName().contains("world_duel"))
            return;
        if (event.getEntity().getWorld().getName().toLowerCase().contains("dungeon"))
            return;
        List<ItemStack> savedItems = new ArrayList<>();
        List<ItemStack> drops = event.getDrops();
        for (int i = 0; i < drops.size(); i++) {
            ItemStack item = drops.get(i);
            NBTWrapper wrapper = new NBTWrapper(item);
            InventoryPetItem petItem = CosmicPersistentItem.constructItem(item, wrapper, InventoryPetItem.class);
            if (petItem != null && petItem.getPetType() != null)
                if (petItem.isLeashed()) {
                    petItem.setLeashed(false);
                    event.getDrops().set(i, petItem.build());
                    if (event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player && MaskType.hasEquipped(event.getEntity().getKiller(), "ANIMAL_CONTROL") && Math.random() < 0.33D) {
                        Player killer = event.getEntity().getKiller();
                        event.getEntity().sendMessage(CC.RedB + "* ANIMAL CONTROL MASK [" + CC.Gray + killer.getName() + " (- " + ItemUtils.getDisplayName(item) + CC.Gray + ")" + CC.RedB + "] *");
                        killer.sendMessage(CC.GreenB + "* ANIMAL CONTROL MASK [" + CC.Gray + event.getEntity().getName() + " (+ " + ItemUtils.getDisplayName(item) + CC.Gray + ")" + CC.GreenB + "] *");
                    } else {
                        savedItems.add(petItem.build());
                    }
                }
        }
        for (ItemStack itemStack : savedItems)
            event.getDrops().remove(itemStack);
        if (savedItems.size() > 0)
            this.savedItems.put(event.getEntity().getUniqueId().toString(), savedItems);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        List<ItemStack> toReturn = this.savedItems.remove(e.getPlayer().getUniqueId().toString());
        if (toReturn != null)
            for (ItemStack i : toReturn) {
                e.getPlayer().getInventory().addItem(i);
            }
    }
}
