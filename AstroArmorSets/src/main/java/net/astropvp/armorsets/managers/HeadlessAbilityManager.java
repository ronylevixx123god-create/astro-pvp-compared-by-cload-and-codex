package net.astropvp.armorsets.managers;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import lombok.Getter;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.HeadlessAbilityCooldown;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class  HeadlessAbilityManager implements Listener {
    @Getter
    private final Map<UUID, ItemStack> headless;

    public HeadlessAbilityManager() {
        this.headless = new HashMap<>();
    }

    public void activateAbility(Player attacker, Player attacked) {
        if (CooldownUtils.isCooldownActive(attacked, "headlessAbility")) return;
        CooldownUtils.registerCooldown(attacked, new HeadlessAbilityCooldown("headlessAbility", 300));
        attacker.sendMessage(CC.translate("&b&l** FROZEN TROPICS &7[" + attacked.getName() + "] &b&l**"));
        attacked.sendMessage(CC.translate("&b&l** FROZEN TROPICS &7[&c" + attacker.getName() + "&7] &6&l**"));

        if (attacked.getInventory().getHelmet() != null) {
            headless.put(attacked.getUniqueId(), attacked.getInventory().getHelmet().clone());
        } else {
            headless.put(attacked.getUniqueId(), null);

        }
        ItemStack pumpkin = new ItemBuilder(Material.ICE).name(CC.translate("&6&lTrick or Treat Ability")).addLoreLine(CC.translate("&7&o" + attacker.getName() + "'s Spooky Armorset Ability.")).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build();
        pumpkin = AstroArmorSetsAPI.addWineArmorOverride(pumpkin, 3);
        attacked.getInventory().setHelmet(pumpkin);
        Bukkit.getScheduler().runTaskLater(AstroArmorSets.getInstance(), () -> {
            if(!headless.containsKey(attacked.getUniqueId())) return;
            attacked.getInventory().setHelmet(headless.get(attacked.getUniqueId()));
            attacked.sendMessage(CC.translate("&6&l** TRICK OR TREAT ABILITY &7(Expired) &6&l**"));
            headless.remove(attacked.getUniqueId());
        }, 5 * 20L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageWhileHeadlessed(EntityDamageByEntityEvent event) {
        if(event.getEntity() == null) return;
        if(!(event.getEntity() instanceof Player)) return;

        Player victim = ((Player) event.getEntity());
        if(this.headless.containsKey(victim.getUniqueId())) {
            event.setDamage(event.getDamage() * 1.3);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {

        if (headless.containsKey(event.getEntity().getUniqueId())) {
            event.getDrops().add(headless.get(event.getEntity().getUniqueId()));
            event.getDrops().removeIf(item -> item.getType() == Material.JACK_O_LANTERN);
            event.getEntity().getInventory().setHelmet(null);
            headless.remove(event.getEntity().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent event) {
        if (headless.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().getInventory().setHelmet(headless.get(event.getPlayer().getUniqueId()));
            headless.remove(event.getPlayer().getUniqueId());

        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        if (headless.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().getInventory().setHelmet(headless.get(event.getPlayer().getUniqueId()));
            headless.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArmorSwitch(PlayerInteractEvent event) {
        if (headless.containsKey(event.getPlayer().getUniqueId()) ) {
            if (event.getItem() != null && event.getItem().getType().name().endsWith("_HELMET")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClickHelmet(InventoryClickEvent event) {
        if (headless.containsKey(event.getWhoClicked().getUniqueId())) {
            if (event.getClickedInventory() == event.getWhoClicked().getInventory() && event.getSlot() == 39) {
                event.setCancelled(true);
            }
        }
    }
}
