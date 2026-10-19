package net.astropvp.armorsets.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EssentialsUtils;
import me.astropvp.astropvpskins.armorequip.ArmorEquipEvent;
import me.astropvp.proxypach.ProxyPatch;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.armorsets.struct.antiMkit.AntiMkit;
import net.astropvp.armorsets.struct.cache.ArmorSetCache;
import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArmorSetApplyListener implements Listener {

    public ArmorSetApplyListener(AstroArmorSets plugin) {
        plugin = AstroArmorSets.getInstance();
    }


/*    @EventHandler
    public void walkSpeedChangeFrostySet(PlayerUpdateSpeedEvent event) {
        if(event.getPlayer() == null) return;
        if (AstroCore.getInstance().getFreezeCommand().isFrozen(event.getPlayer())) return;
        if (AstroArmorSetsAPI.getCurrentArmorSet(event.getPlayer()) != ArmorSetType.FROSTY) return;
        if (!AstroArmorSetsAPI.getBonusCrystalsEquipped(event.getPlayer()).containsKey(ArmorSetType.FROSTY)) return;
        if(!EssentialsUtils.isVanished(event.getPlayer())) return;
        if(event.getNewSpeed() != 0.2F) {
            event.setCancelled(true);
        }
    }*/

    @EventHandler
    public void onArmorChange(ArmorEquipEvent e) {
        AstroArmorSetsAPI.refreshArmorSets(e.getPlayer(), true);

        Bukkit.getScheduler().runTaskLater(AstroArmorSets.getInstance(), () -> {
            AstroArmorSetsAPI.refreshArmorSets(e.getPlayer(), false);
        }, 10L);

    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity() != null) {
            Bukkit.getScheduler().runTaskLater(AstroArmorSets.getInstance(), () -> {
                AstroArmorSetsAPI.refreshArmorSets(e.getEntity(), true);
            }, 30L);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (Bukkit.getPluginManager().isPluginEnabled("ProxyPatch") && ProxyPatch.playerNames.contains(e.getPlayer().getName())) return;
        if (e.getPlayer() != null) {
            Bukkit.getScheduler().runTaskLater(AstroArmorSets.getInstance(), () -> {
                AstroArmorSetsAPI.refreshArmorSets(e.getPlayer(), true);
            }, 30L);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        if (e.getPlayer() != null) {
            Bukkit.getScheduler().runTaskLater(AstroArmorSets.getInstance(), () -> {
                AstroArmorSetsAPI.refreshArmorSets(e.getPlayer(), true);
            }, 30L);
        }
    }

    @EventHandler
    public void headlessClickAbility(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.ARMOR && AstroArmorSets.getInstance().getHeadlessAbilityManager().getHeadless().containsKey(event.getWhoClicked().getUniqueId())) {
            event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c You cannot switch armor while you are trick or treated"));
            event.setCancelled(true);
        }
    }
}
