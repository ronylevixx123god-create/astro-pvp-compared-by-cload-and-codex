package me.astropvp.astropvpskins.objects.skins.backpacks;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GrimCloakSkin extends ItemSkin {

    @Override
    public void onAttacked(Player damaged, Entity damager, EntityDamageByEntityEvent event) {

    }

    @Override
    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (isPlayerBehind(damager, damaged)) {
            event.setDamage(event.getDamage()*1.25);
            damager.sendMessage(CC.translate("&4&l* GRIM CLOAK &7[+25% DMG from Behind] &4&l*"));
        }
    }

    public boolean isPlayerBehind(final Player player, final Entity theEntity) {
        Location playerLoc = player.getLocation();
        Location targetLoc = theEntity.getLocation();
        double pvecy = -Math.sin(Math.toRadians(playerLoc.getPitch()));
        double pvecx = -Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.sin(Math.toRadians(playerLoc.getYaw()));
        double pvecz = Math.cos(Math.toRadians(playerLoc.getPitch())) * Math.cos(Math.toRadians(playerLoc.getYaw()));
        double tvecy = -Math.sin(Math.toRadians(targetLoc.getPitch()));
        double tvecx = -Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.sin(Math.toRadians(targetLoc.getYaw()));
        double tvecz = Math.cos(Math.toRadians(targetLoc.getPitch())) * Math.cos(Math.toRadians(targetLoc.getYaw()));
        double dot = tvecx * pvecx + tvecy * pvecy + tvecz * pvecz;
        return dot > 0.6;
    }
}