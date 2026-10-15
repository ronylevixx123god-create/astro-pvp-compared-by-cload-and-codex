package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeathAndTaxesSkin extends ItemSkin {
    private HashMap<Player, Double> markList;

    public DeathAndTaxesSkin() {
        this.markList = new HashMap<>();
    }

    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation()) || !WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (DuelAPI.isSpectator(damager)) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if(damaged instanceof Player && this.markList.containsKey(damaged) && !event.isCancelled())
            this.markList.put((Player) damaged, this.markList.get(damaged) + event.getDamage());
        if(damaged instanceof Player && Math.random() <= 0.02 && !markList.containsKey(damaged)) {
            damager.sendMessage(CC.GoldB + " ** MARKED " + damaged.getName() + " WITH DEATH AND TAXES **");
            damaged.sendMessage(CC.GoldB + " ** DEATH AND TAXES MARK " + CC.Gray + "[" + damager.getName() + "]" + CC.GoldB + " **");
            this.markList.put((Player) damaged, 0.0);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                if(!WorldGuardUtils.isPvPEnabled(damaged.getLocation()))
                    return;
                double amount = markList.get(damaged)/8;
                if (amount > 10) {
                    amount = 10;
                }
                ((Player) damaged).damage(amount);
                damager.sendMessage(CC.GoldB + " ** MARKED DEALT " + amount + "HP **");
                damaged.sendMessage(CC.GoldB + " ** DEATH AND TAXES DEALT " + CC.Gray + "[" + amount + " DMG]" + CC.GoldB + " **");
                this.markList.remove(damaged);
            }, 120L);
        }
    }
}
