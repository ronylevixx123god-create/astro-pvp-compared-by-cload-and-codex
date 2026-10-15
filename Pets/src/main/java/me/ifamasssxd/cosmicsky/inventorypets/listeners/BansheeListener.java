package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.axes.GoldenAdmirer;
import me.astropvp.astropvpskins.objects.skins.swords.SpinalTab;
import me.ifamasssxd.cosmicsky.inventorypets.struct.pets.InventoryBansheePet;
import net.astropvp.armorsets.struct.sets.LunaArmorSet;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BansheeListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(event.getEntity().getLocation()) || !WorldGuardUtils.isPvPEnabled(event.getDamager().getLocation()))
            return;
        if (event.getEntity().getType().equals(EntityType.PLAYER) && event.getDamager().getType().equals(EntityType.PLAYER)) {
            Player damager = (Player) event.getDamager();
            if (damager.hasMetadata("mutated_banshee")) {
                if (LunaArmorSet.isLunaStriked((Player) event.getEntity())) return;
                if (!event.getEntity().hasMetadata("bloodHoundNoBless") && !event.getDamager().hasMetadata("bloodHoundNoBless")) {
                    event.setDamage(event.getDamage() * 1.4);
                }
                damager.sendMessage(CC.translate("&a&l** &2&l&nMUTATED&r &a&lBANSHEE PET &8➥ &22x DMG &a&l**"));
            } else if (damager.hasMetadata("banshee_active")) {
                if (LunaArmorSet.isLunaStriked((Player) event.getEntity())) return;
                if (!event.getEntity().hasMetadata("bloodHoundNoBless") && !event.getDamager().hasMetadata("bloodHoundNoBless")) {
                    event.setDamage(event.getDamage() * 1.25);
                }
                damager.sendMessage(CC.translate("&4&l** BANSHEE PET &8➥ &c1.5x DMG &4&l**"));
            }
        }
    }


    @EventHandler
    public void onEntityDamage2(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(event.getEntity().getLocation()) || !WorldGuardUtils.isPvPEnabled(event.getDamager().getLocation()))
            return;
        if (event.getEntity().getType().equals(EntityType.PLAYER) && event.getDamager().getType().equals(EntityType.PLAYER)) {
            Player damager = (Player) event.getDamager();
            if (event.getEntity().hasMetadata("banshee_Scrolls")) return;
            if ((((SpinalTab) SkinType.HALLOWEEN_2021_SWORD.getItemSkin())).getWitchSilencedPlayers().contains(damager.getName()))
                return;
            if (AstroItemSkinsAPI.hasSkinEquipped((Player) event.getEntity(), SkinType.THANKSGIVING_HELMET_WITCH) && (!GoldenAdmirer.isAdmirerSilenced((Player) event.getEntity())) && Math.random() < 0.002) {
                event.getEntity().sendMessage(CC.translate("&e&l* WITCH HAT &7Activated &4Banshee Pet &7on&f " + damager.getName() + " &e&l*"));
                damager.sendMessage(CC.translate("&e&l* &f" + event.getEntity().getName() + " &e&lWITCH HAT &4Banshee Pet &7Ability &e&l*"));
                InventoryBansheePet.startBansheeEffect((Player) event.getEntity(), damager, 8);
            }
        }
    }
}
