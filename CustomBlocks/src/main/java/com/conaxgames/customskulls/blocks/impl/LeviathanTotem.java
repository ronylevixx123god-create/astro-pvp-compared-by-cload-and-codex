package com.conaxgames.customskulls.blocks.impl;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.CustomBlock;
import com.conaxgames.customskulls.data.DeleteCustomBlocks;
import com.conaxgames.customskulls.data.MakeCustomBlocks;
import com.conaxgames.customskulls.enums.AbilityType;
import com.conaxgames.customskulls.util.CC;
import com.conaxgames.customskulls.util.ItemBuilderUtils;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import de.inventivegames.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class LeviathanTotem extends CustomBlock {

    public LeviathanTotem() {
        super("LEVIATHAN_TOTEM", CC.translate("&3&lLeviathan Totem"), Material.NOTE_BLOCK, AbilityType.LEVIATHAN_TOTEM);
    }

    @Override
    public List<String> addLore() {
        return Arrays.asList(
                CC.translate("&3Cost to Mine: &f$" + NumberUtils.formatMoney(AbilityType.LEVIATHAN_TOTEM.getPickupPrice())),
                "",
                CC.translate("&7Chance to double mob spawner"),
                CC.translate("&7drops in the chunk it's placed in."),
                "",
                CC.translate("&7Any mob spawner that is lucky enough"),
                CC.translate("&7to be in the orbit of this sacred holy"),
                CC.translate("&7object will have its mobs imbued with"),
                CC.translate("&7inner looting."),
                "",
                CC.translate("&7Place custom block to use!")
        );
    }

    @Override
    public void leftClickAirAction(Player p0, ItemStack p1) {
    }

    @Override
    public void leftClickBlockAction(Player p0, PlayerInteractEvent p1, Block p2, ItemStack p3) {
    }

    @Override
    public void rightClickAirAction(Player p0, ItemStack p1) {
    }

    @Override
    public void rightClickBlockAction(Player p0, PlayerInteractEvent p1, Block p2, ItemStack p3) {
        p0.sendMessage(CC.translate("&e&l(!)&e You will get the Leviathan Totem Buffs when standing in this chunk."));
        try {
            ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(),p1.getClickedBlock().getLocation(), 2,2,2,0.05f,40);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shiftLeftClickAirAction(Player p0, ItemStack p1) {
    }

    @Override
    public void shiftLeftClickBlockAction(Player p0, PlayerInteractEvent p1, Block p2, ItemStack p3) {
    }

    @Override
    public void shiftRightClickAirAction(Player p0, ItemStack p1) {
    }

    @Override
    public void shiftRightClickBlockAction(Player p0, PlayerInteractEvent p1, Block p2, ItemStack p3) {
    }

    @Override
    public void middleClickAction(Player p0, ItemStack p1) {
    }

    @Override
    public void hitEntityAction(Player p0, EntityDamageByEntityEvent p1, Entity p2, ItemStack p3) {
    }

    @Override
    public void placeBlockAction(Player p0, BlockPlaceEvent p1, Block p2, ItemStack p3) {
        // check if the player has a faction.
        // check if the player is in faction land
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(p0);
        if (fPlayer == null) {
            p0.sendMessage(CC.RED + "You must have a faction to place a block of this type.");
            return;
        }
        if (FactionUtils.getFactionAt(p1.getBlock().getLocation()) == fPlayer.getFaction()) {
            MakeCustomBlocks.newFileItem(p0, AbilityType.LEVIATHAN_TOTEM, p1.getBlock(), this);
        } else {
            p0.sendMessage(CC.RED + "You must be in your own claim to place a block of this type");
            p1.setCancelled(true);
        }
    }

    @Override
    public void breakBlockAction(Player player, BlockBreakEvent p1, Block p2, ItemStack p3, CustomBlock cb, AbilityData data) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Player breaker = p1.getPlayer();
        if (fPlayer == null) {
            // The block must be in the land to remove it.
            return;
        }
        if (EconomyUtils.hasBalance(breaker, AbilityType.LEVIATHAN_TOTEM.getPickupPrice())) {
            EconomyUtils.withdrawBalance(breaker, AbilityType.LEVIATHAN_TOTEM.getPickupPrice());
            breaker.sendMessage(CC.B_RED + "-$" + NumberUtils.formatMoney(AbilityType.LEVIATHAN_TOTEM.getPickupPrice()));

            ItemBuilderUtils item = new ItemBuilderUtils(this.getItem()).setLore(this.addLore());
            p1.getBlock().getDrops().clear(); // clears the events item stack

            //p2.breakNaturally(new ItemBuilderUtils(Material.AIR).toItemStack()); // break the block
            p1.getBlock().setType(Material.AIR);
            p2.getWorld().dropItemNaturally(p2.getLocation(), item.toItemStack()); // dropping our item stack
            DeleteCustomBlocks.func(p2.getLocation(), breaker, this); // delete file and memory.
        } else {
            breaker.sendMessage(CC.RED + "You do not have the required $" + AbilityType.LEVIATHAN_TOTEM.getPickupPrice() + " to pick this block up.");
            p1.setCancelled(true);
        }
    }


    @Override
    public void clickedInInventoryAction(Player p0, InventoryClickEvent p1) {
    }

    @Override
    public void damageGivenEvent(Player p0, EntityDamageByEntityEvent p1, Chunk p2, AbilityData abilityData) {

    }


    @Override
    public void mobDeathEvent(Player p0, EntityDeathEvent p1b) {
    }
}
