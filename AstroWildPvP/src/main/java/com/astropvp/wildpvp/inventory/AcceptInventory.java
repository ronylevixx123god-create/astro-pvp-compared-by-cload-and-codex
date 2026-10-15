package com.astropvp.wildpvp.inventory;

import co.aikar.commands.lib.expiringmap.ExpiringMap;
import com.astropvp.wildpvp.AstroWildPvP;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryListener;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import com.astropvp.wildpvp.queue.QueueState;
import com.astropvp.wildpvp.queue.WildQueue;
import com.astropvp.wildpvp.util.KrizzUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AcceptInventory  implements InventoryProvider {
    private final SmartInventory inventory;
    private final Player owner;
    private final Player joiner;
    private final WildQueue queue;
    private final ExpiringMap<Player, Instant> expiringMap;

    public AcceptInventory(Player owner, Player joiner, WildQueue queue) {
        this.owner = owner;
        this.joiner = joiner;
        this.queue = queue;
        this.queue.setState(QueueState.ACCEPTING);
        AstroWildPvP.getInstance().getQueueManager().getAcceptingPlayers().add(this.joiner.getUniqueId());
        this.inventory = SmartInventory.builder().manager(AstroWildPvP.getInstance().getInventoryManager()).provider(this).title("PvP Accept Menu").size(4, 9).id(UUID.randomUUID().toString()).closeable(false).type(InventoryType.CHEST).build();
        this.inventory.open(owner);
        this.expiringMap = ExpiringMap.builder().expiration(30L, TimeUnit.SECONDS).expirationListener((player, time) -> {
            AstroWildPvP.getInstance().getQueueManager().getAcceptingPlayers().remove(this.joiner.getUniqueId());
            KrizzUtil.sendMessage((CommandSender) player, "&c&l(!) &cYour acceptance time ran out, request ignored.");
            KrizzUtil.sendMessage(this.joiner, "&cYour PvP match request to " + this.owner.getName() + " has been &4&lIGNORED&c.");
            this.queue.setState(QueueState.IDLE);
            this.inventory.close((Player) player);
        }).build();
        this.expiringMap.put(owner, Instant.now());
    }

    public void init(Player player, InventoryContents contents) {
        ClickableItem denyItem = ClickableItem.of(KrizzUtil.createItem(Material.STAINED_GLASS_PANE, "&cDeny match", 1, 14, new String[]{"&7Deny the offer and keep looking", "&7for another opponent."}), (event) -> {
            KrizzUtil.sendMessage(this.joiner, "&cYour PvP match request to " + this.owner.getName() + " has been &4&lDENIED&c.");
            AstroWildPvP.getInstance().getQueueManager().getAcceptingPlayers().remove(this.joiner.getUniqueId());
            this.expiringMap.remove(player);
            this.queue.getDenied().add(this.joiner.getUniqueId());
            this.queue.setState(QueueState.IDLE);
            this.inventory.close(player);
        });
        ClickableItem acceptItem = ClickableItem.of(KrizzUtil.createItem(Material.STAINED_GLASS_PANE, "&aAccept match", 1, 5, new String[]{"&7Accept the offer and allow", "&7the opponent to PvP with you."}), (event) -> {
            AstroWildPvP.getInstance().getQueueManager().getAcceptingPlayers().remove(this.joiner.getUniqueId());
            this.expiringMap.remove(player);
            this.queue.setState(QueueState.IDLE);
            this.queue.addOpponentToQueue(this.joiner);
            this.inventory.close(player);
        });
        String factionTag = ((FPlayer)FPlayers.getInstance().getByPlayer(this.joiner)).getFaction().getTag();
        int factionSize = ((FPlayer)FPlayers.getInstance().getByPlayer(this.joiner)).getFaction().getOnlinePlayers().size();
        ItemStack headItem = KrizzUtil.createPlayerSkull(this.joiner.getName(), "&a" + this.joiner.getName(), "&a&lPlayer HP&a: &a&n" + (new Double(this.joiner.getHealth())).intValue(), "", "&a&lFaction", factionTag + " &7(&a" + factionSize + "&7 online)", "", "&7Click to view their inventory.");
        contents.fillRect(0, 0, 0, 3, denyItem);
        contents.fillRect(0, 5, 0, 8, acceptItem);
        contents.set(new SlotPos(0,4), ClickableItem.of(headItem, (event) -> {
            new AcceptInventory.InventoryDisplayCallback(this.owner, this.joiner);
        }));
    }

    public void update(Player player, InventoryContents contents) {
    }

    private class InventoryDisplayCallback implements InventoryProvider {
        private final Player invsee;

        InventoryDisplayCallback(Player opener, Player invsee) {
            this.invsee = invsee;
            SmartInventory.builder().type(InventoryType.CHEST).size(1, 9).title("WildPvP View Inventory").manager(AstroWildPvP.getInstance().getInventoryManager()).provider(this).id(UUID.randomUUID().toString()).listener(new InventoryListener(InventoryCloseEvent.class, (event) -> {
                Bukkit.getScheduler().runTaskLater(AstroWildPvP.getInstance(), () -> {
                    AcceptInventory.this.inventory.open(opener);
                }, 1L);
            })).build().open(opener);
        }

        public void init(Player player, InventoryContents contents) {
            ItemStack[] var3 = this.invsee.getInventory().getContents();
            int var4 = var3.length;

            for (ItemStack content : var3) {
                contents.add(ClickableItem.empty(content));
            }

/*            ListIterator slotIter = Lists.newArrayList(new Integer[]{51, 50, 48, 47}).listIterator();

            while(slotIter.hasNext()) {
                contents.set((Integer)slotIter.next(), ClickableItem.empty(this.invsee.getInventory().getArmorContents()[slotIter.nextIndex() - 1]));
            }*/

        }

        public void update(Player player, InventoryContents inventoryContents) {
        }
    }
}
