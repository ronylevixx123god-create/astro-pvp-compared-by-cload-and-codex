package com.conaxgames.customskulls.listeners;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.CustomBlock;
import com.conaxgames.customskulls.data.DeleteCustomBlocks;
import com.conaxgames.customskulls.util.CC;
import com.conaxgames.customskulls.util.ChunkUtil;
import com.conaxgames.customskulls.util.CustomBlockUtil;
import com.conaxgames.customskulls.util.ItemBuilderUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class CustomBlockListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUse(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) {
            return;
        }
        if (AbilityData.getByUUID(AbilityData.getUUID(event.getClickedBlock().getLocation())) != null) {
            this.interactCustomBlockItem(event);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = player.getItemInHand();
        AbilityData data = AbilityData.getAbilityDataMap().get(AbilityData.getUUID(event.getBlock().getLocation()));
        if (data == null) {
            return;
        }

        if (data.getAbilityType() == null) return;

        CustomBlock customBlock = CoreSkulls.customBlocks.get(data.getAbilityType().getName());

        if (customBlock == null) {
            return;
        }

        customBlock.breakBlockAction(player, event, event.getBlock(), item, customBlock, data);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        List<Block> blockList = event.blockList();

        for (Block block : blockList) {
            if (block.getType() != Material.NOTE_BLOCK) continue;
            AbilityData data = AbilityData.getAbilityDataMap().get(AbilityData.getUUID(block.getLocation()));
            if (data == null) {
                continue;
            }

            if (data.getAbilityType() == null) continue;

            CustomBlock customBlock = CoreSkulls.customBlocks.get(data.getAbilityType().getName());

            if (customBlock == null) {
                continue;
            }

            ItemBuilderUtils item = new ItemBuilderUtils(customBlock.getItem()).setLore(customBlock.addLore());


            block.getDrops().clear(); // clears the events item stack

            //p2.breakNaturally(new ItemBuilderUtils(Material.AIR).toItemStack()); // break the block
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(block.getLocation(), item.toItemStack()); // dropping our item stack
            DeleteCustomBlocks.func(block.getLocation(), null, customBlock); // delete file and memory.

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = player.getItemInHand();
        if (event.getPlayer().getItemInHand() == null || event.getPlayer().getItemInHand().getType() == Material.AIR) {
            return;
        }

        if (event.isCancelled()) return;

        if (!(CustomBlockUtil.isCustomItem(item))) {
            return;
        }

/*        if (!ChunkUtil.isLocationAInSameChunkAsLocationB(event.getPlayer().getLocation(), event.getBlockAgainst().getLocation())) {
            player.sendMessage(CC.translate("&c&l(!)&c You must stand in the same chunk as the block you are placing against."));
            event.setCancelled(true);
            return;
        }*/


        for (AbilityData data : CustomBlockUtil.getBlockDatasInChunkLocation(event.getBlock().getLocation())) {

            if (data.getAbilityType().name().equals(Objects.requireNonNull(CustomBlockUtil.getCustomItem(item)).getIdentifier())) {
                player.sendMessage(CC.translate("&c&l(!)&c This chunk already contains this custom block."));
                player.sendMessage(CC.translate("&cType \"&7/removeblocks&c\" to remove all customblocks in your chunk."));
                event.setCancelled(true);
                return;
            }
        }

        if (CustomBlockUtil.isCustomItem(item)) {
/*            CraftWorld cw = (CraftWorld) player.getWorld();
            TileEntity tileEntity = cw.getTileEntityAt(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("type", CustomBlockUtil.getCustomItem(item).getIdentifier());
            compound.setString("customBlockData", CustomBlockUtil.getCustomItem(item).getIdentifier());
            compound.setString("type", CustomBlockUtil.getCustomItem(item).getIdentifier());*/
            //event.setCancelled(true);

            (Objects.requireNonNull(CustomBlockUtil.getCustomItem(item))).placeBlockAction(player, event, event.getBlock(), item);
        }


    }


    private void interactCustomBlockItem(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.NOTE_BLOCK) return;
        if (AbilityData.getByUUID(AbilityData.getUUID(block.getLocation())) != null) {
            if (AbilityData.getByUUID(AbilityData.getUUID(block.getLocation())).getAbilityType() == null) return;
            CustomBlock customBlock = CoreSkulls.customBlocks.get(AbilityData.getByUUID(AbilityData.getUUID(block.getLocation())).getAbilityType().getName());
            if (customBlock == null) return;
            if (event.getAction() == Action.LEFT_CLICK_AIR) {
                if (!player.isSneaking()) {
                    customBlock.leftClickAirAction(player, customBlock.getItem());
                } else {
                    customBlock.shiftLeftClickAirAction(player, customBlock.getItem());
                }
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (!player.isSneaking()) {
                    customBlock.leftClickBlockAction(player, event, event.getClickedBlock(), customBlock.getItem());
                } else {
                    customBlock.shiftLeftClickBlockAction(player, event, event.getClickedBlock(), customBlock.getItem());
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (!player.isSneaking()) {
                    customBlock.rightClickAirAction(player, customBlock.getItem());
                } else {
                    customBlock.shiftRightClickAirAction(player, customBlock.getItem());
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (!player.isSneaking()) {
                    customBlock.rightClickBlockAction(player, event, event.getClickedBlock(), customBlock.getItem());
                } else {
                    customBlock.shiftRightClickBlockAction(player, event, event.getClickedBlock(), customBlock.getItem());
                }
            }
        }
    }
}
