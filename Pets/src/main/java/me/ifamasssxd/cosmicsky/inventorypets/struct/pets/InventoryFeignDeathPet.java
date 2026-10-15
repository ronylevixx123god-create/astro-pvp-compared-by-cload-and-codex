package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.PetCooldown;
import me.ifamasssxd.cosmicsky.inventorypets.utils.DamageIndicatorUtils;
import me.ifamasssxd.cosmicsky.inventorypets.utils.PetUtils;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.arkhamnetwork.enchantments.mastery.MasteryFeignDeath;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryFeignDeathPet extends InventoryPet {
    public InventoryPets getOwner() {
        return null;
    }

    public List getPetDescription() {
        return Lists.newArrayList(CC.Gray + "A ghastly pet that can", CC.Gray + "control death itself.", "", CC.WhiteB + "Ability", CC.Gray + "Fake your own death.", CC.Gray + "[" + CC.DarkRed + "Feign Death" + CC.Gray + "]", "", CC.WhiteB + "Cooldown", CC.Gray + " 10 Minutes");
    }

    public int getExpForLevel(int level) {
        return 5 * 750;
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCooldown() {
        return (int) TimeUnit.MINUTES.toSeconds(10L);
    }

    public boolean onPetItemInteract(Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws Exception {
/*        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis((long) this.getCooldown());
        long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);


        if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
            throw new PrefixedPetException("Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int) (timeTillCanUse / 1000L)));

        if (PetUtils.isOnCooldown(player, petItem.getPetType()) && (!player.isOp() || !player.isSneaking()))
            throw new PrefixedPetException(CC.Red + "Ability on cooldown: " + CC.RedU +
                    TimeUtils.formatSeconds(PetUtils.getSecondsOnCooldown(player, petItem.getPetType())));
        PetUtils.addPetCooldown(player, petItem.getPetType(), (getCooldown() * 1000L));*/

        MasteryFeignDeath.vanishPlayer(player, 5);
        //petItem.setLastUsed(System.currentTimeMillis());
        CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));

        player.playSound(player.getLocation(), Sound.DIG_SNOW, 1.1F, 3.0F);
        player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.1F, 3.0F);
        return true;

    }

    public boolean onTickInHotBar(Player player, int inventorySlot, InventoryPetItem item) {
        return false;
    }
}
