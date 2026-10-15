package org.arkhamnetwork.enchantments.listeners;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.enchanting.EEquip;
import com.rit.sucy.enchanting.EListener;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatTagEvent;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerPotionEffectListener implements Listener {
    private static HashMap<UUID, PlayerPotionEffects> playerPotionEffectTracker;

    static {
        PlayerPotionEffectListener.playerPotionEffectTracker = new HashMap<UUID, PlayerPotionEffects>();
    }

    public static void rescanPlayerArmor(final Player p) {
        if (p.hasMetadata("armorRescanRunning")) {
            return;
        }

        p.setMetadata("armorRescanRunning", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) true));
        final ArrayList<ItemStack> armor = new ArrayList<>();
        for (final ItemStack item : p.getInventory().getArmorContents()) {
            if (item != null) {
                if (item.getType() != Material.AIR) {
                    armor.add(item);
                }
            }
        }
        EEquip.unequipPlayer(p);
        for (final PotionEffect pe : new ArrayList<PotionEffect>(p.getActivePotionEffects())) {
            if (PlayerPotionEffects.getTrackedPotionEffects(p).hasPotionEffect(pe.getType(), pe.getAmplifier())) {
                p.removePotionEffect(pe.getType());
            }
        }
        PlayerPotionEffects.resetPotionTracker(p);
        EEquip.loadPlayer(p);
        for (final Map.Entry<CustomEnchantment, Integer> data : EListener.getValidEnchantments(p, armor).entries()) {
            data.getKey().applyEquipEffect(p, (int) data.getValue());
        }
        PlayerPotionEffects.applyPotionEffects(p, true);
        p.removeMetadata("armorRescanRunning", (Plugin) Enchantments.getInstance());
    }

    public static HashMap<UUID, PlayerPotionEffects> getPlayerPotionEffectTracker() {
        return PlayerPotionEffectListener.playerPotionEffectTracker;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();

        PlayerPotionEffectListener.playerPotionEffectTracker.put(p.getUniqueId(), new PlayerPotionEffects(p));
            rescanPlayerArmor(e.getPlayer());

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final PlayerPotionEffects effects = PlayerPotionEffectListener.playerPotionEffectTracker.remove(e.getPlayer().getUniqueId());
        if (effects != null) {
            effects.setBukkitPlayer(null);
        }
    }

    @EventHandler
    public void onCombatTag(final CombatTagEvent e) {
        if (!e.wasAlreadyInCombat() && e.getPlayer() != null && e.getPlayer().isOnline()) {
            PlayerPotionEffects.applyPotionEffects(e.getPlayer(), true);
        }
    }

    @EventHandler
    public void onPlayerChangeWorld(final PlayerChangedWorldEvent e) {
        e.getPlayer().setMetadata("lastWorldChange", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) true));
        if (!e.getFrom().getName().contains("duel")) {
            Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), (Runnable) new Runnable() {
                @Override
                public void run() {
                    if (!e.getPlayer().isDead() && e.getPlayer().isValid()) {
                        PlayerPotionEffectListener.rescanPlayerArmor(e.getPlayer());
                    }
                }
            }, 11L);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(final PlayerDeathEvent e) {
        e.getEntity().setMetadata("lastPlayerDeath", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) System.currentTimeMillis()));
    }
}
