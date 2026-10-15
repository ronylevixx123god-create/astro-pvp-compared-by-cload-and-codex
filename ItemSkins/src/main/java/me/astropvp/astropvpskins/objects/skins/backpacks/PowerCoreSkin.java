package me.astropvp.astropvpskins.objects.skins.backpacks;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.rit.sucy.EnchantmentAPI;
import me.astropvp.astropvpskins.objects.ItemSkin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PowerCoreSkin extends ItemSkin {
    public void onEquip(Player player) {

        if (player.getLocation().getWorld().getName().equals("world_the_end"))
            return;
        String regionName = WorldGuardUtils.getRegionName(player.getLocation());
        if (regionName.contains("parkour") && !regionName.contains("enchants"))
            return;
        EnchantmentAPI.getEnchantment("Aquatic").applyEquipEffect(player, 1);
        EnchantmentAPI.getEnchantment("Lava Strider").applyEquipEffect(player, 1);
        EnchantmentAPI.getEnchantment("Ghostly Ghost").applyEquipEffect(player, 3);
        EnchantmentAPI.getEnchantment("Obsidianshield").applyEquipEffect(player, 1);
        EnchantmentAPI.getEnchantment("Gears").applyEquipEffect(player, 4);
    }

    @Override
    public void onUnequip(Player player) {
        EnchantmentAPI.getEnchantment("Aquatic").applyUnequipEffect(player, 1);
        EnchantmentAPI.getEnchantment("Lava Strider").applyUnequipEffect(player, 1);
        EnchantmentAPI.getEnchantment("Ghostly Ghost").applyUnequipEffect(player, 3);
        EnchantmentAPI.getEnchantment("Obsidianshield").applyUnequipEffect(player, 1);
        EnchantmentAPI.getEnchantment("Gears").applyUnequipEffect(player, 4);

    }
}
