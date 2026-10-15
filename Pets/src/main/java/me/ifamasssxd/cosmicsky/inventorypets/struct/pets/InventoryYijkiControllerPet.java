package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.PetCooldown;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class InventoryYijkiControllerPet extends InventoryPet {
    public int petLevel = 0;

    public InventoryPets getOwner() {
        return InventoryPets.BANSHEE;
    }

    public List<String> getPetDescription() {
        return Lists.newArrayList(String.valueOf(CC.Gray) + "A powerful demonic entity capable",
                String.valueOf(CC.Gray) + "of forcing any nearby /dungeon Yijki",
                String.valueOf(CC.Gray) + "to switch to a different combat state.", (new StringBuilder(
                String.valueOf(CC.Gray))).toString(),
                String.valueOf(CC.WhiteB) + "Ability",
                String.valueOf(CC.Gray) + "Randomizes the current ability + weakness",
                String.valueOf(CC.Gray) + "of any Yijki Boss in your dungeon.", (new StringBuilder(
                String.valueOf(CC.Gray))).toString(), String.valueOf(CC.WhiteB) + "Cooldown",
                String.valueOf(CC.Gray) + " " + TimeUtils.formatSeconds(getCooldown()));
    }

    public int getCooldown() {
        return 20;
    }

    public int getExpForLevel(int level) {
        return 1000 + 250 * level;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean onPetItemInteract(Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis(getCooldown());
        long timeTillCanUse = delay - System.currentTimeMillis() - lastUse;


        if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
            throw new NonPrefixedPetException(String.valueOf(CC.RedB) + "PET: " + CC.Red + "Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int)(timeTillCanUse / 1000L)));
        if (player.hasMetadata("in_duel"))
            return true;
        Location locationClicked = player.getLocation();
        if (!player.getWorld().getName().contains("dungeon"))
            throw new NonPrefixedPetException(String.valueOf(CC.RedB) + "PET: " + CC.Red + "You must be in a dungeon to use this pet!");
        Boss yijki = null;
        for (Boss boss : Boss.bosses.values()) {
            if (boss.getBossType() == BossType.DUNGEON_YIJKI && boss.getLocation().getWorld().getName().equals(player.getLocation().getWorld().getName()))
                yijki = boss;
        }
        if (yijki == null)
            throw new NonPrefixedPetException(String.valueOf(CC.RedB) + "PET: " + CC.Red + "No nearby Yijki to use this pet on!");
        Phase phase = getRandomPhase((DungeonYijki)yijki);
        ((DungeonYijki)yijki).setPhase(phase);
        ((DungeonYijki)yijki).onPhaseChange(phase);
        this.petLevel = petItem.getLevel();
        //petItem.setLastUsed(System.currentTimeMillis());
        petItem.setLastUsed(System.currentTimeMillis());
        int xpGain = 50;
        player.playSound(player.getLocation(), Sound.DIG_SNOW, 1.1F, 3.0F);
        player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.1F, 3.0F);
        return true;
    }

    public Phase getRandomPhase(DungeonYijki yijki) {
        List<Phase> phases = Lists.newArrayList(Phase.ONE, Phase.TWO, Phase.THREE, Phase.FOUR, Phase.FIVE, Phase.SIX);
        List<Phase> available = (List<Phase>)phases.stream().filter(ph -> (ph != yijki.getPhase())).collect(Collectors.toList());
        if (yijki.entity == null) {
            available.remove(Phase.ONE);
            available.remove(Phase.THREE);
        }
        available.remove(Phase.SIX);
        available.remove(Phase.FOUR);
        return available.get((new Random()).nextInt(available.size()));
    }

    public boolean onTickInHotBar(Player var1, int var2, InventoryPetItem var3) {
        return false;
    }
}
