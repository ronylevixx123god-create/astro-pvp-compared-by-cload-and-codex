/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.arkhamnetwork.enchantments.Enchantments
 *  org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 */
package me.ifamasssxd.cosmicmasks.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CustomEnchantListener
implements Listener {
    @EventHandler
    public void onCustomEnchantProc(CustomEnchantProcOnEntityEvent event) {
        if (event.getTarget() instanceof Player && event.getEnchant() == Enchantments.getInstance().getNaturesWrathEnchant() && MaskType.ZEUS.hasEquipped((Player)event.getTarget())) {
            event.setCancelled(true);
            event.setEvent((Event)null);
            ((Player)event.getTarget()).sendMessage(CC.LightPurpleB + "* ZEUS MASK [" + CC.Gray + "Natures Wrath Dodged" + CC.LightPurpleB + "] *");
        }
        if (event.getTarget() instanceof Player && event.getEnchant() == Enchantments.getInstance().getNaturesWrathEnchant() && MaskType.THANOS.hasEquipped((Player)event.getTarget())) {
            event.setCancelled(true);
            event.setEvent((Event)null);
            ((Player)event.getTarget()).sendMessage(CC.GoldB + "* THANOS MASK [" + CC.Gray + "Natures Wrath Blocked" + CC.GoldB + "] *");
        }
    }
}

