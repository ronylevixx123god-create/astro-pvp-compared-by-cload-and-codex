package net.astropvp.core.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.golfing8.kore.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

public class RemoveKnockbackEnchantListener implements Listener {

    @EventHandler
    public void onPrepareItemEnchant(PrepareItemEnchantEvent event) {
        if(event.getEnchanter().isOp()) return;
        if(ItemUtils.isAirOrNull(event.getItem())) return;
        if(event.getItem().getType().name().endsWith("_SWORD")) {
            event.setCancelled(true);
            event.getEnchanter().closeInventory();
            event.getEnchanter().sendMessage(CC.translate("&cStop trying to enchant your sword please sir"));
        }
    }

}
