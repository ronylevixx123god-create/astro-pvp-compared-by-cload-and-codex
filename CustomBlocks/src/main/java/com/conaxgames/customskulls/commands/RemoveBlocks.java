package com.conaxgames.customskulls.commands;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.CustomBlock;
import com.conaxgames.customskulls.data.DeleteCustomBlocks;
import com.conaxgames.customskulls.util.CC;
import com.conaxgames.customskulls.util.CustomBlockUtil;
import com.conaxgames.customskulls.util.ItemBuilderUtils;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveBlocks implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) return false;
        Player player = ((Player) sender).getPlayer();
        if (!CustomBlockUtil.chunkContainsABlock(player)) {
            player.sendMessage(CC.translate("&c&l(!)&c There are no customblocks in this chunk!"));
        }

        if (!FactionUtils.isInsideOwnFaction(player) && !player.isOp())  {
            player.sendMessage(CC.translate("&c&l(!)&c You can only type this command in your own faction land!"));
        }

        for (AbilityData data : CustomBlockUtil.getBlockDatasInChunk(player)) {
            CustomBlock customBlock = CoreSkulls.customBlocks.get(data.getAbilityType().getName());
            ItemBuilderUtils item = new ItemBuilderUtils(customBlock.getItem()).setLore(customBlock.addLore());

            Block block = data.getLocation().getBlock();

            block.getDrops().clear(); // clears the events item stack
            block.getWorld().dropItemNaturally(block.getLocation(), item.toItemStack()); // dropping our item stack

            //p2.breakNaturally(new ItemBuilderUtils(Material.AIR).toItemStack()); // break the block
            block.setType(Material.AIR);
            DeleteCustomBlocks.func(block.getLocation(), null, customBlock); // delete file and memory.
            player.sendMessage(CC.translate("&a&l(!)&a Deleted all customblocks in your chunk"));
        }
        return true;
    }
}
