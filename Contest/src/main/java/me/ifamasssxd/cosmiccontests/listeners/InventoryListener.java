package me.ifamasssxd.cosmiccontests.listeners;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.UUID;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.struct.ContestManager;
import me.ifamasssxd.cosmiccontests.struct.PrizeManager;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.prizes.Prize;
import me.ifamasssxd.cosmiccontests.struct.prizes.RankingPrize;
import me.ifamasssxd.cosmiccontests.utils.StringUtils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InventoryListener implements Listener {
    public static Set<UUID> contestViewers = new HashSet<>();

    @EventHandler
    public void onInventoryclick(InventoryClickEvent event) throws IOException {
        if (event.getInventory().getName().equals("Daily Challenges")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            if (event.getRawSlot() > event.getInventory().getSize() - 1)
                return;
            ItemStack prize = event.getCurrentItem();
            if (prize == null || prize.getType() == Material.AIR || !prize.hasItemMeta() || !prize.getItemMeta().hasDisplayName())
                return;
            Player player = (Player)event.getWhoClicked();
            ContestManager manager = CosmicContests.get().getContestManager();
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy((ItemStack)prize);
            NBTTagCompound comp = nmsItem.getTag();
            if (comp == null)
                return;
            if (comp.hasKey("contest")) {
                Contests contest = Contests.valueOf(comp.getString("contest"));
                Inventory menu = manager.createTop(contest);
                player.closeInventory();
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CosmicContests.get(), () -> player.openInventory(menu), 1L);
                return;
            }
        } else if (event.getInventory().getName().equals("Challenge Prizes")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            if (event.getRawSlot() > event.getInventory().getSize() - 1)
                return;
            ItemStack prize = event.getCurrentItem();
            if (prize == null || prize.getType() == Material.AIR || !prize.hasItemMeta() || !prize.getItemMeta().hasDisplayName())
                return;
            Player player = (Player)event.getWhoClicked();
            PrizeManager prizeManager = CosmicContests.get().getPrizeManager();
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(prize);
            NBTTagCompound comp = nmsItem.getTag();
            if (comp == null)
                return;
            int prizeLevel = comp.getInt("contestPrize");
            List<RankingPrize> available = (List<RankingPrize>)prizeManager.getCachedPrizes().get(player.getUniqueId());
            if (available == null) {
                player.closeInventory();
                return;
            }
            RankingPrize found = null;
            for (RankingPrize rank : available) {
                if (rank.getRanking() == prizeLevel) {
                    found = rank;
                    break;
                }
            }
            if (found == null) {
                player.sendMessage(ChatColor.RED + "Invalid Prize found from ranking prizes!");
                return;
            }
            int spacesRequired = (prizeLevel == 1) ? 5 : ((prizeLevel == 2) ? 10 : 13);
            spacesRequired++;
            if (getEmptySlots(player) < spacesRequired) {
                player.sendMessage(ChatColor.RED + "You do not have enough space to claim this prize!");
                return;
            }
            List<Prize> prizes = prizeManager.getPossiblePrizes(prizeLevel);
            if (prizes == null) {
                player.sendMessage(ChatColor.RED + "Invalid Prize!");
                return;
            }
            player.sendMessage(ChatColor.GREEN + "You have successfully claimed your " + StringUtils.getFormattedNumber(prizeLevel) + " Place Challenge" + ChatColor.GREEN + " Prize!");
            for (Prize foundPrize : prizes) {
                event.getInventory().setItem(event.getRawSlot(), null);
                event.setCurrentItem(null);
                prizeManager.removePrize(player, found);
                if (available.isEmpty())
                    prizeManager.getCachedPrizes().remove(player.getUniqueId());
                foundPrize.giveToPlayer(player);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            }
        } else if (event.getInventory().getName().equals("Challenge Leaderboard")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    public int getEmptySlots(Player player) {
        int slots = 0;
        for (ListIterator<ItemStack> listIterator = player.getInventory().iterator(); listIterator.hasNext(); ) {
            ItemStack is = listIterator.next();
            if (is == null || is.getType() == Material.AIR)
                slots++;
        }
        return slots;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        contestViewers.remove(event.getPlayer().getUniqueId());
    }
}
