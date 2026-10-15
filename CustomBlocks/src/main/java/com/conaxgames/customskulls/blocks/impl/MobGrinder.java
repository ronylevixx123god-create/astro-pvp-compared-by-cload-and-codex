package com.conaxgames.customskulls.blocks.impl;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.CustomBlock;
import com.conaxgames.customskulls.data.DeleteCustomBlocks;
import com.conaxgames.customskulls.data.MakeCustomBlocks;
import com.conaxgames.customskulls.enums.AbilityType;
import com.conaxgames.customskulls.guis.GrinderCustomBlockGUI;
import com.conaxgames.customskulls.util.CC;
import com.conaxgames.customskulls.util.ItemBuilderUtils;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import lombok.Getter;
import lombok.Setter;
import me.astropvp.cratekeys.enums.CrateType;
import org.bukkit.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MobGrinder extends CustomBlock {




    public MobGrinder() {
        super("BLAZE_STONE_TOTEM", CC.translate("&c&lAuto Mob Grinder"), Material.NOTE_BLOCK, AbilityType.BLAZE_STONE_TOTEM);

    }

    @Override
    public List<String> addLore() {
        return Arrays.asList(
                CC.translate("&cCost to Mine: &f$" + NumberUtils.formatMoney(AbilityType.BLAZE_STONE_TOTEM.getPickupPrice())),
                CC.translate("&cActivate Price: &f$" + NumberUtils.formatMoney(AbilityType.BLAZE_STONE_TOTEM.getPricePerAction())),
                "",
                CC.translate("&7Automatically grinds Blazes"),
                CC.translate("&7in a 1 block radius from the block."),
                "",
                CC.translate("&7Dug up from the deep depths of"),
                CC.translate("&7the gold mines, this custom block is"),
                CC.translate("&7essential for grinding Blazes."),
                CC.translate(""),
                CC.translate("&7You must place a &nChest&7 next to the Custom"),
                CC.translate("&7Grinder block for it to start grinding!"),
                CC.translate(""),
                CC.translate("&c&lNOTE: &cRight-Click the Chest to"),
                CC.translate("&cclaim grinding lootboxes and EXP in it."),
                CC.translate("&cAlso, a player &lMUST&c be in the chunk for"),
                CC.translate("&cBlazes to be automatically grinded."),
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
        GrinderCustomBlockGUI mainCustomBlockGUI = new GrinderCustomBlockGUI(AbilityData.getByUUID(AbilityData.getUUID(p1.getClickedBlock().getLocation())), p1.getClickedBlock());
        mainCustomBlockGUI.getCustomBlockGui().open(p1.getPlayer());
        p1.getPlayer().sendMessage(CC.translate("&a&l(!)&a Opening the Custom Block menu..."));
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
            MakeCustomBlocks.newFileItem(p0, AbilityType.BLAZE_STONE_TOTEM, p1.getBlock(), this);
            fPlayer.getPlayer().sendMessage(CC.translate("&c&l* &cIt can take up to 30 seconds for this block to enable."));
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
            return;
        }
        if (FactionUtils.isWilderness(p2.getLocation()) || (fPlayer.getRelationToLocation().isMember())) {
            if (EconomyUtils.hasBalance(breaker, AbilityType.BLAZE_STONE_TOTEM.getPickupPrice())) {
                EconomyUtils.withdrawBalance(breaker, AbilityType.BLAZE_STONE_TOTEM.getPickupPrice());
                breaker.sendMessage(CC.B_RED + "-$" + NumberUtils.formatMoney(AbilityType.BLAZE_STONE_TOTEM.getPickupPrice()));

                ItemBuilderUtils item = new ItemBuilderUtils(this.getItem()).setLore(this.addLore());
                p1.getBlock().getDrops().clear(); // clears the events item stack

                //p2.breakNaturally(new ItemBuilderUtils(Material.AIR).toItemStack()); // break the block
                p1.getBlock().setType(Material.AIR);
                p2.getWorld().dropItemNaturally(p2.getLocation(), item.toItemStack()); // dropping our item stack
                CoreSkulls.instance.getMobGrinderManager().deActivate(data.getUuid());
                DeleteCustomBlocks.func(p2.getLocation(), breaker, this); // delete file and memory.
            } else {
                breaker.sendMessage(CC.RED + "You do not have the required $" + AbilityType.BLAZE_STONE_TOTEM.getPickupPrice() + " to pick this block up.");
                p1.setCancelled(true);
            }
        } else {
            breaker.sendMessage(CC.RED + "You can not break this block as it is not in your claim or wilderness.");
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
