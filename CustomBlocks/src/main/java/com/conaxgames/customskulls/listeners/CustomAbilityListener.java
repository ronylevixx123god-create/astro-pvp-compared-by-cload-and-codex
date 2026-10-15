package com.conaxgames.customskulls.listeners;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.CustomBlock;
import com.conaxgames.customskulls.util.CustomBlockUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class CustomAbilityListener implements Listener {

    @EventHandler
    public void pvpEvent(EntityDamageByEntityEvent event) {
/*        if (!(event.getDamager() instanceof Player))
            return;
        Player player = (Player) event.getDamager();
        if (CustomBlockUtil.chunkContainsABlock(player)) {
            for (AbilityData data : CustomBlockUtil.getBlockDatasInChunk(player)) {
                if (data == null) return;

                CustomBlock customBlock = CoreSkulls.customBlocks.get(data.getAbilityType().getName());

                if (customBlock == null) return;
                // This is when a player hits another player, we can multiply their damage, or reduce it!
                customBlock.damageGivenEvent(player, event, player.getLocation().getChunk(), data);
            }
        }*/
    }

    @EventHandler
    public void MobDeathEvent(EntityDeathEvent event) {
/*        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            for (AbilityData data : CustomBlockUtil.getBlockDatasInChunk(player)) {
                if (data == null) {
                    return;
                }

                CustomBlock customBlock = CoreSkulls.customBlocks.get(data.getAbilityType().getName());

                if (customBlock == null) {
                    return;
                }

                // This is when a mob dies, we can double the exp dropped for example!
                customBlock.mobDeathEvent(player, event);
            }
        }*/
    }
}
