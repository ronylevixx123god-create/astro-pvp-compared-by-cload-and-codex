package me.ifamasssxd.cosmicbosses.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.SneakyThrows;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.utils.BossEggUtils;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public class CommandBoss implements CommandExecutor {
    public static ItemStack getDimensionalCore() {
        return new NBTWrapper(new ItemBuilder(Material.BEACON, CC.LightPurpleB + "Interdimensional Core", CC.Gray + "A strange crystal emanating a powerful", CC.Gray + "ominous aura. You sense you will need other", CC.Gray + "components for this object to be useful", CC.Gray + "to you in your /dungeon runs.").build()).setString("cosmicType", "dimensionalCore").build();
    }

    @SneakyThrows
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        Player p = (sender instanceof Player) ? (Player) sender : null;
        if (args.length == 1 && args[0].equalsIgnoreCase("core")) {
            p.getInventory().addItem(getDimensionalCore());
            return true;
        }
        if (args.length != 2 && args.length != 3) {
            if (args.length == 1) {

                if (args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage("");
                    for (final Boss boss : Boss.bosses.values()) {
                        sender.sendMessage(boss.getColor(true) + boss.getName());
                        final LivingEntity ent = boss.getEntity();
                        sender.sendMessage(boss.getColor(true) + "Coords: " + ent.getLocation().getBlockX() + " " + ent.getLocation().getBlockY() + " " + ent.getLocation().getBlockZ());
                        sender.sendMessage(boss.getColor(true) + "Health: " + ent.getHealth());
                        sender.sendMessage(boss.getColor(true) + "Stuck: " + boss.getAntiFarm().isStuck());
                        sender.sendMessage("");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    CosmicBosses.getInstance().saveBosses();
                    CosmicBosses.getInstance().loadBosses();
                }
            }
            sender.sendMessage(ChatColor.RED + "/boss spawn <boss>");
            return false;
        }
        if (args.length == 3 && !args[1].equalsIgnoreCase("yijkirunner")) {
            p = Bukkit.getPlayer(args[2]);
            if (p == null) {
                sender.sendMessage(ChatColor.RED + "Invalid Player: " + args[2]);
                return true;
            }
        }
        final boolean value = args[1].equalsIgnoreCase("true");
        if (args[0].equalsIgnoreCase("toggleegg")) {
            CosmicBosses.getInstance().setEggEnabled(value);
            CosmicBosses.getInstance().getConfig().set("eggEnabled", (Object) value);
            CosmicBosses.getInstance().saveConfig();
            sender.sendMessage("egg enabled: " + CosmicBosses.getInstance().isEggEnabled());
            return true;
        }

        if (args[0].equalsIgnoreCase("togglespawn")) {
            CosmicBosses.getInstance().setNaturalSpawnEnabled(value);
            CosmicBosses.getInstance().getConfig().set("naturalSpawn", (Object) value);
            CosmicBosses.getInstance().saveConfig();
            sender.sendMessage("spawns enabled: " + CosmicBosses.getInstance().isNaturalSpawnEnabled());
            return true;
        }
        if (args[0].equalsIgnoreCase("togglescoreboard")) {
            CosmicBosses.getInstance().setScoreboardEnabled(value);
            CosmicBosses.getInstance().getConfig().set("scoreboardEnabled", (Object) value);
            CosmicBosses.getInstance().saveConfig();
            sender.sendMessage("scoreboard enabled: " + CosmicBosses.getInstance().isScoreboardEnabled());
            return true;
        }
        if (args[1].equalsIgnoreCase("yijkirunner")) {
            try {
                if (args.length >= 3) {
                    try {
                        final String[] worldArgs = args[2].split(":");
                        final Location loc = new Location(Bukkit.getWorld(worldArgs[0]), Double.parseDouble(worldArgs[1]), Double.parseDouble(worldArgs[2]), Double.parseDouble(worldArgs[3]));
                        BossUtils.createDungeonGhost(p.getLocation().clone().add(0.0, 1.0, 0.0), loc, 15);
                        sender.sendMessage(ChatColor.RED + "Yijki Runner Animation spawned and should be moving towards " + args[2]);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "Invalid target location format given! Desired: world:x:y:z");
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        final BossType targetType = BossType.getBossType(args[1]);
        if (targetType == null) {
            sender.sendMessage(ChatColor.RED + "Invalid Boss Type: " + args[1]);
            sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Valid Bosses:");
            for (final BossType bossType : BossType.values()) {
                sender.sendMessage(bossType.name());
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("spawn") && sender instanceof Player) {
            final HashSet<Byte> map = null;
            try {
                final Block target = p.getTargetBlock((HashSet<Byte>) map, 6);
                if (target == null) {
                    p.sendMessage(ChatColor.RED + "Invalid loc");
                    return true;
                }
                final Boss boss2 = targetType.createInstance(target.getLocation());
                if (boss2 == null) {
                    sender.sendMessage(ChatColor.RED + "Unable to create new boss instance!");
                    return true;
                }
                boss2.onBossSpawn();
                p.sendMessage("Spawning boss..");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (args[0].equalsIgnoreCase("egg")) {
            ItemStack is = BossEggUtils.getBossEgg(targetType);
            try {
                if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                    is = ItemID.setItemID(is, ItemID.generateNewItemID());
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            final Boss instance = BossEggUtils.getBossInstance(targetType);
            if (p != null) {
                p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "A " + instance.getColor(true) + instance.getName() + ChatColor.GREEN + " Boss Egg has been added to your me.astropvp.cratekeys.inventory.");
                if (p.getInventory().firstEmpty() == -1) {
                    p.getWorld().dropItem(p.getLocation(), is);
                } else {
                    p.getInventory().addItem(new ItemStack[]{is});
                }
                p.updateInventory();
            }
            sender.sendMessage(ChatColor.RED + "Egg Added.");
        }
        return true;
    }
}
