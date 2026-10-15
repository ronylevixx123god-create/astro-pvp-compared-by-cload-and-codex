
package net.astropvp.lootboxes.listener;

import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;

import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.gui.LootboxPlayerViewGui;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public class LootboxListener
        implements Listener {
    private final LootboxManager lootboxManager;
    private final JavaPlugin plugin;
    private final LootboxPlayerViewGui playerViewGui;
    private final HashSet<Material> noStackedOpenMaterials = Sets.newHashSet();

    public LootboxListener(LootboxManager lootboxManager, JavaPlugin plugin, InventoryManager inventoryManager) {
        this.lootboxManager = lootboxManager;
        this.plugin = plugin;
        this.playerViewGui = new LootboxPlayerViewGui(inventoryManager, this.lootboxManager);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        NBTTagCompoundBuilder compound;
        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            return;
        }
        if (!this.lootboxManager.getLootboxMaterials().contains(event.getItem().getType())) {
            return;
        }
        try {
            NBTWrapper nbtWrapper = new NBTWrapper(event.getItem());
            compound = nbtWrapper.getCompoundOrCreate(this.lootboxManager.getNBTCompound());
            if (!compound.getBoolean(this.lootboxManager.getNBTIsLootbox()).booleanValue()) {
                return;
            }
        } catch (NullPointerException e) {
            return;
        }
        this.lootboxManager.getLootbox(compound.getString(this.lootboxManager.getNBTLootboxType())).ifPresent(lootbox -> {
            Player player = event.getPlayer();
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                this.playerViewGui.getPlayerViewMap().put(player.getUniqueId(), lootbox);
                this.playerViewGui.getInventory().open(player);
                event.setCancelled(true);
            } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (player.getWorld().getName().contains("duel_")) {
                    player.sendMessage(CommonsUtil.color((String) "&c&l(!) &cYou cannot open lootboxes in this world."));
                    return;
                }
                if (this.isInventoryFull(player, lootbox.getTotalRewardCount())) {
                    player.sendMessage(CommonsUtil.color((String) "&c&l(!) &cYou cannot open lootboxes with a full inventory."));
                    event.setCancelled(true);
                    return;
                }
                ItemStack item = player.getItemInHand();
                int itemAmount = item.getAmount();
                int amount = 0;
                if (player.getItemInHand().getAmount() > 1) {
                    amount = itemAmount - 1;
                    /*if (player.hasMetadata("noLootboxUse")) {
                        if (Math.random() < 0.1) {
                            amount = itemAmount;
                            player.sendMessage(CC.translate((String) "&6&l[Halloween Candles] &6You have kept your lootbox!"));
                        }
                        player.removeMetadata("noLootboxUse", (Plugin) DecimateCustomBlocksPlugin.getInstance());
                    }*/
                    item.setAmount(amount);
                    player.setItemInHand(item);
                } else {
                    boolean lootboxKept = false;
                    /*if (player.hasMetadata("noLootboxUse")) {
                        if (Math.random() < 0.1) {
                            lootboxKept = true;
                            player.sendMessage(CC.translate((String) "&6&l[Halloween Candles] &6You have kept your lootbox!"));
                        }
                        player.removeMetadata("noLootboxUse", (Plugin) DecimateCustomBlocksPlugin.getInstance());
                    }*/
                    ItemStack itemSet = lootboxKept ? item : null;
                    player.setItemInHand(itemSet);
                }
                player.playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
                lootbox.executeLootbox(player, this.plugin, this.lootboxManager);
                event.setCancelled(true);
            }
        });
    }

    public boolean isInventoryFull(Player player, int size) {
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() != Material.AIR || --size != 0) continue;
            return false;
        }
        return true;
    }
}

