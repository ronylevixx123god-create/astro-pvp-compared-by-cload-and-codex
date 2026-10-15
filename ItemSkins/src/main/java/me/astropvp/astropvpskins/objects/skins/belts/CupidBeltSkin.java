package me.astropvp.astropvpskins.objects.skins.belts;

import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.listeners.AutoBlessListener;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class CupidBeltSkin extends ItemSkin {

    public void onEquip(Player player) {
        Blessed.blessPlayer(player);
        AutoBlessListener.blessedPlayers.add(player.getUniqueId());
    }

    public void onUnEquip(Player player) {
        AutoBlessListener.blessedPlayers.remove(player.getUniqueId());
    }


    //TODO: ON UPDATE HERE
}


