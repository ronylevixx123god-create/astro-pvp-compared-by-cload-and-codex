package net.astropvp.armorsets.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.cache.ArmorSetCache;
import net.astropvp.armorsets.utils.config.ConfigManager;
import net.astropvp.core.AstroCore;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class HeroicArmorWeaponDamageListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void reduceDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = ((Player) e.getEntity()).getPlayer();
        if (e.getEntity().getWorld().getName().equals("world_outpost_pluto")) {
            return;
        }
        ArmorSetCache cache = (ArmorSetCache) AstroCore.getInstance().getCacheManager().getCachedPlayer(player).getCache("armor_set");
        if (cache != null && cache.getHeroicPieces() != 0) {
            double totalReduction = cache.getHeroicPieces() * AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("heroicDamageDecrease");
            if (((Player) e.getEntity()).getKiller() != null) {

                if (Math.random() < 0.33 && AstroItemSkinsAPI.hasSkinEquipped(((Player) e.getEntity()).getKiller(), SkinType.THANKSGIVING_2021_SWORD)) {
                    e.getEntity().sendMessage(CC.translate("&4&l* HEROIC ARMOR BLOCKED [&7" + ((Player) e.getEntity()).getKiller().getName() + "'s Carver &4&l] *"));
                    return;
                }
            }
            e.setDamage(e.getDamage() * 1 - (totalReduction * 0.01));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamageSword(EntityDamageByEntityEvent event) {
        if(event.getEntity() == null || event.getDamager() == null) return;
        if(!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if(!AstroArmorSetsAPI.isHeroic(player.getItemInHand())) return;
        double increaseAmount = AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("heroicWeaponDamageIncrease");
        event.setDamage(event.getDamage() * 1 + (increaseAmount/100));

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHitP6(EntityDamageByEntityEvent e) {
        if (e.getDamager().getLocation().getWorld().getName().equals("lms_world")) return;
        if (e.getDamager().getLocation().getWorld().getName().equals("world_outpost_pluto")) return;

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player phurt = (Player) e.getEntity();
            double totalReduction = getProtectionReduction(phurt);
            if (totalReduction != 0) {
                e.setDamage(e.getDamage() * (1 - (totalReduction * 0.01)));
            }
        }
    }

    public double getProtectionReduction(Player p) {
        double decrease = 0;
        for (ItemStack armorPiece : p.getInventory().getArmorContents()) {
            if (armorPiece == null) continue;
            if (armorPiece.getType() == Material.AIR) continue;
            if (armorPiece.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                int protLevel = armorPiece.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                if (protLevel == 6) decrease += AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("prot6Reduction");
                if (protLevel == 5) decrease += AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("prot5Reduction");
            }
        }
        return decrease;
    }
}