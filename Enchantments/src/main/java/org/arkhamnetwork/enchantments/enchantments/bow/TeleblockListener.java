/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.CosmicArmorSetsAPI
 *  com.compilex.CosmicArmorSets.enums.ArmorSet
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.MaskUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.bow;


import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import me.ifamasssxd.cosmicmasks.MaskType;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Map;

class TeleblockListener
implements Listener {
    TeleblockListener() {
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("teleblockEnchantment") && e.getDamager() instanceof Projectile) {
            int teleblockLevel = ((MetadataValue)e.getDamager().getMetadata("teleblockEnchantment").get(0)).asInt();
            if (e.getEntity() instanceof Player) {
                Player pHurt = (Player)e.getEntity();

                if (MaskType.hasEquipped((Player)pHurt, (String)"GLITCH")) {
                    return;
                }

                if (AstroArmorSetsAPI.getCurrentArmorSet(pHurt) != null) {
                    if (AstroArmorSetsAPI.getCurrentArmorSet(pHurt) == ArmorSetType.ENGINEER) {
                        pHurt.sendMessage(CC.translate("&6&l(Engineer Armorset) &6Blocked Teleblock enchant."));
                        return;
                    }
                }

                if (AstroArmorSetsAPI.getCurrentArmorSet(pHurt) != null) {
                    if (AstroArmorSetsAPI.getCurrentArmorSet(pHurt) == ArmorSetType.valueOf("RANGER")) {
                        pHurt.sendMessage(CC.translate("&a&l(Ranger Armorset) &aBlocked Teleblock enchant."));
                        return;
                    }
                }

                pHurt.setMetadata("teleblock", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)(System.currentTimeMillis() + (long)(5 + teleblockLevel * 3000))));
                PlayerInventory pi = pHurt.getInventory();
                int pearlsToDestroy = teleblockLevel * 3;
                for (int i = 0; i < pi.getSize(); ++i) {
                    if (!(pi.getItem(i) == null || pi.getItem(i).getType() != Material.ENDER_PEARL || pi.getItem(i).hasItemMeta() && pi.getItem(i).getItemMeta().hasDisplayName())) {
                        int amount = pi.getItem(i).getAmount();
                        if (amount <= pearlsToDestroy) {
                            pearlsToDestroy -= amount;
                            pi.setItem(i, new ItemStack(Material.AIR));
                        } else {
                            ItemStack invPearl = pi.getItem(i);
                            invPearl.setAmount(invPearl.getAmount() - pearlsToDestroy);
                            pi.setItem(i, invPearl);
                            pearlsToDestroy = 0;
                        }
                    }
                    if (pearlsToDestroy <= 0) break;
                }
                pHurt.sendMessage((Object)ChatColor.DARK_PURPLE + "** TELEBLOCK [" + 5 + teleblockLevel * 3 + "s] [-" + pearlsToDestroy + "ep] **");
                pHurt.playSound(pHurt.getLocation(), Sound.ENDERMAN_HIT, 0.75f, 0.6f);
            }
        }
    }
}

