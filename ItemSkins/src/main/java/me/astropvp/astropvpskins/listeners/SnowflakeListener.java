package me.astropvp.astropvpskins.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EssentialsUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.axes.Snowflake;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SnowflakeListener implements Listener {
    private List<Player> snowflake;

    private HashMap<Snowball, Player> snowballs;

    public SnowflakeListener() {
        snowflake = new ArrayList<>();
        snowballs = new HashMap<>();
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR && e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType().name().contains("AXE") && !Snowflake.getSnowflakeCooldown().contains(e.getPlayer())) {
            if (!WorldGuardUtils.isPvPEnabled(e.getPlayer().getLocation())) return;
                if (AstroItemSkinsAPI.hasSkinEquipped(e.getPlayer(), SkinType.CHRISTMAS_AXE_SNOWFLAKE)) {
                Snowflake.addSnowflakeCooldown(e.getPlayer());
                Snowball snowball = e.getPlayer().launchProjectile(Snowball.class);
                snowball.setMetadata("snowflake", new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), e.getPlayer().getName()));
                snowballs.put(snowball, e.getPlayer());
            }
        }
    }



    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && snowflake.contains(e.getEntity()))
            e.setDamage(e.getDamage()*1.08);
        Entity ent = e.getDamager();
        if(ent instanceof Snowball && e.getEntity() instanceof Player) {
            Player hit = (Player) e.getEntity();
            if(!snowballs.containsKey(ent))
                return;
            hit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
            snowflake.add(hit);
            hit.sendMessage(CC.GreenB + "** " + snowballs.get(ent).getName().toUpperCase() + "'S SNOWFLAKE " + CC.Gray + "[Slowness 2 and 8% Incoming Damage] " + CC.GreenB + "**");
            snowballs.get(ent).sendMessage(CC.GreenB + "** SNOWFLAKE " + CC.Gray + "[" + hit.getName() + "] " + CC.GreenB + "**");
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {snowflake.remove(hit);}, 60L);

        }

    }

    
}
