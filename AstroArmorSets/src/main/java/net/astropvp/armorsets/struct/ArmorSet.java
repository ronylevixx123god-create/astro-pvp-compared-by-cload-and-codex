package net.astropvp.armorsets.struct;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ArmorSet implements Listener {
    public abstract ItemStack getHelmet();
    public abstract ItemStack getChestplate();
    public abstract ItemStack getLeggings();
    public abstract ItemStack getBoots();
    public abstract int getLadderTier();

    public abstract List<String> getArmorLore();

    public abstract List<String> getArmorCrystalLore();


    public abstract List<String> getArmorEquipMessage();

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
    }

    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
    }

    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) throws Exception {
    }

    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) throws Exception {
    }

    public void onEquip(Player player) {
    }

    public void onUnequip(Player player) {
    }



}
