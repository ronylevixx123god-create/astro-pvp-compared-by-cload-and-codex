package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import java.util.LinkedList;
import java.util.List;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

final class CommandFill
        extends AbstractCommand {
    public CommandFill(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        int maxPerDispenser;
        int radius;
        int amount;
        if (!(sender instanceof Player)) {
            return true;
        }
/*        if (!sender.hasPermission("arkkit.command.fill")) {
            sender.sendMessage((Object)((Object)ChatColor.RED) + "You do not have access to this command.");
            sender.sendMessage((Object)((Object)ChatColor.GRAY) + "Unlock this command and more by upgrading your rank today at " + Arkkit.get().getConfig().getString("language.spigot_branding.store_url") + "!");
            return true;
        }*/
        if (args.length < 2) {
            sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "/fill <#> <radius> [maxPerDispenser]");
            sender.sendMessage(ChatColor.GRAY + "Adds <#> to all dispensers within <radius> of the item currently in your hand. No dispenser will end with more than [maxPerDispenser] of the item.");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "EXAMPLE " + ChatColor.YELLOW + "/fill 2 5 10" + ChatColor.GRAY + " all dispensers within a 5 block radius would receive 2x of whatever material you are currently holding in your hand, but no dispenser will end with more than 10x of the material.");
            return true;
        }
        try {
            amount = Integer.parseInt(args[0]);
            radius = Integer.parseInt(args[1]);
            maxPerDispenser = args.length == 3 ? Integer.parseInt(args[2]) : -1;
        }
        catch (NumberFormatException nfe) {
            sender.sendMessage(ChatColor.RED + "Either " + args[0] + " or " + args[1] + " is not a valid number.");
            return true;
        }
        if (amount < 1 || radius < 0 || radius > 40) {
            sender.sendMessage(ChatColor.RED + "You must specify an amount>0 and a positive radius<=40");
            return true;
        }
        if (amount > 64) {
            sender.sendMessage(ChatColor.RED + "You may only distribute up to 64x TNT per dispenser at a time.");
            return true;
        }
        Player p = (Player)sender;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "Equip the material you would like to fill the dispensers with.");
            return true;
        }
        if (!p.getItemInHand().getType().equals(Material.TNT)) {
            sender.sendMessage(CC.translate("&c&l(!) &cYou cannot /tnt fill anything other than tnt."));
            return true;
        }
        if (p.hasPermission("arkkit.fill.restrict") && !p.isOp() && radius > 16) {
            radius = 16;
            p.sendMessage(ChatColor.RED + "Radius reduced to 16 due to fill restriction!");
        }
        FPlayer fp = FPlayers.getInstance().getByOfflinePlayer(p);
        Faction f = Board.getInstance().getFactionAt(new FLocation(p.getLocation()));
        boolean bypass = p.hasPermission("arkkit.fill.bypassclaim");
        if (!bypass && !f.getRelationTo(fp.getFaction()).isAtLeast(Relation.ALLY)) {
            sender.sendMessage(ChatColor.RED + "You cannot use /fill in non-ally territory.");
            return true;
        }
        Material m = p.getItemInHand().getType();
        short metadata = p.getItemInHand().getDurability();
        int totalAdded = 0;
        int dispenserCount = 0;
        boolean useFactionTNT = m == Material.TNT && fp.getFaction().getTNTBank() >= amount && fp.getFaction().hasAccess(fp, PermissibleAction.TNTWITHDRAW);
        for (Block b : this.getDispensers(p.getLocation(), radius)) {
            ItemStack is;
            if (!(b.getState() instanceof Dispenser)) continue;
            Dispenser dp = (Dispenser)b.getState();
            if (!p.getInventory().contains(m)) break;
            FLocation fl = new FLocation(dp.getLocation());
            if (!bypass && !fp.getRelationTo(Board.getInstance().getFactionAt(fl)).isAtLeast(Relation.ALLY)) continue;
            int emptySlots = this.getEmptyDispenserSlots(dp, m, metadata);
            int materialCount = this.getMaterialCount(dp, m, metadata);
            int toAdd = amount;
            if (emptySlots <= 0) continue;
            if (maxPerDispenser != -1) {
                if (materialCount >= maxPerDispenser) continue;
                if (materialCount + toAdd > maxPerDispenser) {
                    toAdd = maxPerDispenser - materialCount;
                }
            }
            boolean bl = useFactionTNT = useFactionTNT && fp.getFaction().getTNTBank() > 0;
            if (useFactionTNT) {
                while (toAdd > 0 && emptySlots > 0) {
                    int tntAmount = Math.min(64, Math.min(emptySlots, toAdd));
                    if (f.getTNTBank() > 0 && f.getTNTBank() >= tntAmount) {
                        is = new ItemStack(Material.TNT, tntAmount);
                        dp.getInventory().addItem(is);
                        Faction faction = f;
                        faction.setTNTBank(faction.getTNTBank()-is.getAmount());
                        toAdd -= is.getAmount();
                        totalAdded += is.getAmount();
                        emptySlots -= is.getAmount();
                        continue;
                    }
                    break;
                }
            } else {
                for (int i = 0; i < 36; ++i) {
                    is = p.getInventory().getItem(i);
                    if (emptySlots > 0 && toAdd > 0) {
                        if (is == null || is.getType() != m || is.getDurability() != metadata || is.hasItemMeta() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().get(0).contains(ChatColor.ITALIC.toString())) continue;
                        if (is.getAmount() <= toAdd) {
                            dp.getInventory().addItem(is);
                            p.getInventory().setItem(i, null);
                            toAdd -= is.getAmount();
                            totalAdded += is.getAmount();
                            emptySlots -= is.getAmount();
                            continue;
                        }
                        if (is.getAmount() <= toAdd) continue;
                        dp.getInventory().addItem(new ItemStack(m, toAdd, metadata));
                        is.setAmount(is.getAmount() - toAdd);
                        p.getInventory().setItem(i, is);
                        totalAdded += toAdd;
                        emptySlots -= toAdd;
                        toAdd = 0;
                        continue;
                    }
                    break;
                }
            }
            ++dispenserCount;
        }
        p.updateInventory();
        p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.YELLOW + " You have filled " + dispenserCount + "x dispensers with " + totalAdded + "x " + m.name() + "!");
        if (dispenserCount == 0) {
            p.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "You can only /fill dispensers in land that is claimed by your faction or your faction's allies.");
        }
        p.playSound(p.getLocation(), Sound.BAT_LOOP, 0.75f, 0.4f);
        return true;
    }

    private LinkedList<Block> getDispensers(Location l, int radius) {
        LinkedList<Block> dispensers = new LinkedList<Block>();
        for (int x = -radius; x < radius; ++x) {
            for (int z = -radius; z < radius; ++z) {
                for (int y = -radius; y < radius; ++y) {
                    Block b = l.clone().add(x, y, z).getBlock();
                    if (b.getType() != Material.DISPENSER) continue;
                    dispensers.add(b);
                }
            }
        }
        return dispensers;
    }

    private int getEmptyDispenserSlots(Dispenser d, Material m, short metadata) {
        int c = 0;
        for (ItemStack i : d.getInventory()) {
            if (i == null || i.getType() == Material.AIR) {
                c += 64;
                continue;
            }
            if (i.getType() != m || i.getDurability() != metadata) continue;
            c += i.getMaxStackSize() - i.getAmount();
        }
        return c;
    }

    private int getMaterialCount(Dispenser d, Material m, short metadata) {
        int c = 0;
        for (ItemStack i : d.getInventory()) {
            if (i == null || i.getType() != m || i.getDurability() != metadata) continue;
            c += i.getAmount();
        }
        return c;
    }
}
