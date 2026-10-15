/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  net.minecraft.server.v1_7_R4.Block
 *  net.minecraft.server.v1_7_R4.Item
 *  org.arkhamnetwork.CosmicCustomTNT.enums.TNTTypes
 *  org.arkhamnetwork.CosmicCustomTNT.net.astropvp.armorsets.listeners.TNTListener
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockEvent
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.enchantments.enchantments.tools;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.Item;
import org.arkhamnetwork.CosmicCustomTNT.enums.TNTTypes;
import org.arkhamnetwork.CosmicCustomTNT.listeners.TNTListener;
import org.arkhamnetwork.enchantments.libs.EnchantBreakBlockEvent;
import org.arkhamnetwork.enchantments.mastery.MasteryAutoSell;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Telepathy
extends CustomEnchantment {
    private List<Material> fortuneBlocks = new ArrayList<Material>(Arrays.asList(new Material[]{Material.COAL_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.QUARTZ_ORE, Material.LAPIS_ORE}));

    public Telepathy() {
        super("Telepathy", GeneralUtil.tools, 6);
        this.max = 4;
        this.base = 10.0;
        this.interval = 10.0;
    }

    public void applyToolEffect(Player player, org.bukkit.block.Block block, int enchantLevel, BlockEvent event) {
        if (event instanceof BlockBreakEvent) {
            BlockBreakEvent bbe = (BlockBreakEvent)event;
            Material m = bbe.getBlock().getType();
            if (m == Material.SPONGE) {
                return;
            }
            double chance = (double)enchantLevel * 0.25;
            if (Math.random() < chance) {
                if (bbe.getBlock().getDrops().size() < 0 || (bbe.getBlock().getType() == Material.IRON_ORE || bbe.getBlock().getType() == Material.GOLD_ORE) && EnchantmentAPI.itemHasEnchantment((ItemStack)player.getItemInHand(), (String)"Auto Smelt")) {
                    return;
                }
                if (bbe.getBlock().getDrops(player.getItemInHand()).size() < 0) {
                    return;
                }
                Collection drops = bbe.getBlock().getDrops(player.getItemInHand());
                if (drops.size() <= 0) {
                    return;
                }
                EnchantBreakBlockEvent enchantBreakEvent = new EnchantBreakBlockEvent(player, bbe.getBlock(), m);
                if (!enchantBreakEvent.callEvent()) {
                    return;
                }
                ItemStack drop = (ItemStack)drops.toArray()[0];
                boolean hasSilkTouch = player.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH);
                boolean hasAutoSell = EnchantmentAPI.itemHasEnchantment((ItemStack)player.getItemInHand(), (String)"Auto Sell");
                if (hasAutoSell && MasteryAutoSell.getSellPrice(drop.getData()) > 0.0) {
                    return;
                }
                boolean silkTouchedBlock = false;
                if (hasSilkTouch) {
                    int amount = drop.getAmount();
                    Item item = Item.getItemOf((Block) Block.getById((int)bbe.getBlock().getTypeId()));
                    short durability = bbe.getBlock().getData();
                    Material mm = Material.getMaterial((int)Item.getId((Item)item));
                    if (mm == Material.ANVIL) {
                        durability = 0;
                    }
                    if (mm != drop.getType()) {
                        silkTouchedBlock = true;
                    }
                    drop = new ItemStack(mm, amount, durability);
                }
                if (bbe.getBlock().getType() == Material.TNT && TNTListener.tnt_tracker.containsKey((Object)bbe.getBlock().getLocation())) {
                    TNTTypes tntType = (TNTTypes)TNTListener.tnt_tracker.get((Object)bbe.getBlock().getLocation());
                    drop = TNTTypes.getTNTItem((TNTTypes)tntType);
                }
                if (bbe.getBlock().getType() == Material.DOUBLE_STEP) {
                    drop = new ItemStack(Material.STEP, 2);
                }
                if (!silkTouchedBlock) {
                    int fortuneDropAmount = this.getBlockDropsFromEnchantedTool(block, player.getItemInHand());
                    if (drop.getAmount() < fortuneDropAmount) {
                        drop.setAmount(fortuneDropAmount);
                    }
                }
                bbe.getBlock().setTypeIdAndData(0, (byte)0, true);
                player.giveExp(bbe.getExpToDrop());
                if (bbe.getBlock().getType() == Material.RAILS) {
                    return;
                }
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(new ItemStack[]{drop});
                } else {
                    player.getWorld().dropItem(player.getLocation(), drop);
                }
            }
        }
    }

    private int getBlockDropsFromEnchantedTool(org.bukkit.block.Block b, ItemStack tool) {
        Material m = b.getType();
        if (tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) > 0) {
            int fortune = tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            if (this.fortuneBlocks.contains((Object)b.getType())) {
                if (fortune == 1) {
                    return 1 + (Math.random() < 0.25 ? 1 : 0);
                }
                if (fortune == 2) {
                    return 1 + (Math.random() < 0.25 ? 1 : 0) + (Math.random() < 0.25 ? 1 : 0) + (Math.random() < 0.25 ? 1 : 0);
                }
                if (fortune >= 3) {
                    return 1 + (Math.random() < 0.2 ? 1 : 0) + (Math.random() < 0.2 ? 1 : 0) + (Math.random() < 0.2 ? 1 : 0) + (Math.random() < 0.2 ? 1 : 0);
                }
            }
        }
        return 1;
    }
}

