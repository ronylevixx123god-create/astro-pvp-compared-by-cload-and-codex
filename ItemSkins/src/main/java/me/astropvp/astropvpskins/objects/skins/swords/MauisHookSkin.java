package me.astropvp.astropvpskins.objects.skins.swords;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MauisHookSkin extends ItemSkin {

    public static Map<Player, Integer> mauiEffect;

    public MauisHookSkin() {
        this.mauiEffect = new HashMap<>();
    }

    public void onAttack(Player damager, Entity damaged, EntityDamageByEntityEvent event) {
        if (!damager.getWorld().getName().equals("world") && !damager.getWorld().getName().equals("flat")) return;
        if (DuelAPI.isSpectator(damager)) return;
        if (damager.hasMetadata("frozenSkrrt")) return;

        if (!WorldGuardUtils.isPvPEnabled(damager.getLocation())) return;
        if (!WorldGuardUtils.isPvPEnabled(damaged.getLocation())) return;
        if (event.isCancelled()) return;

        if (!(damaged instanceof Player))
            return;
        if (Math.random() > 0.06D)
            return;
        if (mauiEffect.containsKey(damager) && mauiEffect.get(damager) >= 10 && Math.random() < 0.3D) {
            //if (damager.hasMetadata("mauiEffect") && ((MetadataValue)damager.getMetadata("mauiEffect").get(0)).asInt() >= 10 && Math.random() < 0.3D) {
            damager.sendMessage(CC.translate("&c&l* MAUI ABILITY is Ready for Use &7(Sneak to activate) &c&l*"));
            return;
        }
        if (!mauiEffect.containsKey(damager)) {
            //damager.setMetadata("mauiEffect", (MetadataValue)new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), 1));
            mauiEffect.put(damager, 1);
        } else {
            //damager.setMetadata("mauiEffect", (MetadataValue)new FixedMetadataValue(AstroItemSkinsPlugin.getInstance(), ((MetadataValue) damager.getMetadata("mauiEffect").get(0)).asInt() + 1));
            mauiEffect.put(damager, mauiEffect.get(damager)+1);
        }
        damager.sendMessage(CC.translate("&c&l* MAUI'S HOOK &7[&fStack x" + mauiEffect.get(damager) + "&7] &c&l*"));
    }

    //Sneka me.astropvp.outpost.listener is in the me.astropvp.cratekeys.net.astropvp.armorsets.listeners package



}
