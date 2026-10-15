package net.crankedpvp.hypeboxes.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import net.crankedpvp.hypeboxes.inventory.AdminHypeboxInventory;
import net.crankedpvp.hypeboxes.inventory.HypeboxQueueInventory;
import net.crankedpvp.hypeboxes.language.Language;
import net.crankedpvp.hypeboxes.struct.HypeBox;
import net.crankedpvp.hypeboxes.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Command("hypebox")
@Alias({"hb"})
public class HypeBoxCommand extends CommandBase {

    private final HypeboxesPlugin plugin;
    public HypeBoxCommand(HypeboxesPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void defaultCommand(CommandSender commandSender) {
        Player player = (Player) commandSender;

        if(player.hasPermission("astro.admin")) {
            sendAdminHelpMessage(player);
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
            return;
        }
    }

    @SubCommand("help")
    @WrongUsage("&eUsage: /hb help")
    @Permission("astro.admin")
    public void helpCommand(CommandSender commandSender) {
        Player player = (Player) commandSender;

        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
        sendAdminHelpMessage(player);
    }

    @SubCommand("create")
    @Permission("astro.admin")
    @WrongUsage("&eUsage: /hb create <name> [amount of rewards]")
    public void createCommand(CommandSender commandSender, String lootbox) {
        Player player = (Player) commandSender;
        if(plugin.getHypeBoxRegister().isHypebox(lootbox)) {
            player.sendMessage(Language.PREFIX.getMessage() + Language.ALREADY_HYPEBOX.getMessage().replace("%lootbox%", lootbox));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        if(player.getItemInHand() == null) {
            plugin.getHypeBoxManager().createHypebox(lootbox);
        } else {
            plugin.getHypeBoxManager().createHypeboxFromItemStack(player.getItemInHand(), lootbox);
        }
        player.sendMessage(Language.PREFIX.getMessage() + Language.CREATED_HYPEBOX.getMessage().replace("%lootbox%", lootbox));
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
        plugin.registerCommandCompletion();
    }

    @SubCommand("delete")
    @Permission("astro.admin")
    @WrongUsage("&eUsage: /hb delete <lootbox>")
    public void deleteCommand(CommandSender commandSender, @Completion("#hypeboxes") String lootbox) {
        Player player = (Player) commandSender;
        if(!plugin.getHypeBoxRegister().isHypebox(lootbox)) {
            player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_INVALID.getMessage().replace("%lootbox%", lootbox));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        player.sendMessage(Language.PREFIX.getMessage() + Language.DELETED_HYPEBOX.getMessage().replace("%lootbox%", lootbox));
        player.playSound(player.getLocation(), Sound.LAVA_POP, 2.0F, 2.0F);
        plugin.getHypeBoxManager().deleteConfig(lootbox);
        plugin.getHypeBoxRegister().removeHypebox(lootbox);
    }

    @SubCommand("disable")
    @Permission("astro.admin")
    @WrongUsage("&eUsage: /hb disable <lootbox>")
    public void disableCommand(CommandSender commandSender, @Completion("#hypeboxes") String lootbox) {
        Player player = (Player) commandSender;

        if (!this.plugin.getHypeBoxRegister().isHypebox(lootbox)) {
            player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_INVALID.getMessage().replace("%lootbox%", lootbox));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        File file = new File(this.plugin.getDataFolder() + "/Hypeboxes/" + lootbox + ".yml");
        if (file.renameTo(new File(this.plugin.getDataFolder() + "/Hypeboxes/-" + lootbox + ".yml"))) {
            this.plugin.getHypeBoxRegister().removeHypebox(lootbox);
            player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_DISABLED.getMessage().replace("%lootbox%", lootbox));
            player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 2.0F, 2.0F);
        }
    }

    @SubCommand("enable")
    @Permission("astro.admin")
    @WrongUsage("&eUsage: /hb enable <lootbox>")
    public void enableCommand(CommandSender commandSender, @Completion("#hypeboxes") String lootbox) {
        Player player = (Player) commandSender;

        if (this.plugin.getHypeBoxRegister().isHypebox(lootbox)) {
            player.sendMessage(Language.PREFIX.getMessage() + Language.ALREADY_HYPEBOX.getMessage().replace("%lootbox%", lootbox));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }
        File file = new File(this.plugin.getDataFolder() + "/Hypeboxes/-" + lootbox + ".yml");
        if (file.renameTo(new File(this.plugin.getDataFolder() + "/Hypeboxes/" + lootbox + ".yml"))) {
            this.plugin.getHypeBoxManager().initialize(lootbox);
            player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_ENABLED.getMessage().replace("%lootbox%", lootbox));
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
        }
    }

    @SubCommand("reload")
    @Permission("astro.admin")
    @WrongUsage("&eUsage: /hb reload [lootbox]")
    public void reloadCommand(CommandSender commandSender, @Optional @Completion("#hypeboxes") String lootbox) {
        Player player = (Player) commandSender;

        if(lootbox == null) {
            player.sendMessage(Language.PREFIX.getMessage() + Language.RELOADED_ALL.getMessage());
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
            this.plugin.getHypeBoxRegister().clearHypeboxMap();
            this.plugin.getHypeBoxManager().initializeAll();
            this.plugin.getFileHandler().load();
            return;
        }
        if (this.plugin.getHypeBoxRegister().getLootboxes().containsKey(lootbox)) {
            player.sendMessage(Language.PREFIX.getMessage() + Language.RELOADED_SINGULAR.getMessage().replace("%lootbox%", lootbox));
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
            this.plugin.getHypeBoxRegister().removeHypebox(lootbox);
            this.plugin.getHypeBoxManager().initialize(lootbox);
            return;
        }
        player.sendMessage(Language.HYPEBOX_INVALID.getMessage().replace("%lootbox%", lootbox));
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
    }

    @SubCommand("list")
    @Permission("astro.admin")
    @WrongUsage("&eUsage: /hb list")
    public void listCommand(CommandSender commandSender) {
        Player player = (Player) commandSender;

        AdminHypeboxInventory adminLootboxInventory = new AdminHypeboxInventory(player, this.plugin);
        adminLootboxInventory.getUI().open(player);
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2.0F, 2.0F);
    }

    @SubCommand("give")
    @Permission("astro.admin")
    @WrongUsage("&eUsage: /hb give <player> <lootbox> [amount]")
    public void giveCommand(CommandSender commandSender, @Completion("#players")Player player, @Completion("#hypeboxes") String lootbox, @Optional Integer amount) {
        if(amount == null) amount = 1;

        if(!player.isOnline()) {
            player.sendMessage(Language.PREFIX.getMessage() + CC.translate("&cThat player is not online"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        if(!this.plugin.getHypeBoxRegister().isHypebox(lootbox)) {
            commandSender.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_INVALID.getMessage().replace("%lootbox%", lootbox));
            //sender.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        this.plugin.getHypeBoxManager().giveHypebox(player, this.plugin.getHypeBoxRegister().getHypebox(lootbox), amount);
        commandSender.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_GIVEN.getMessage().replace("%lootbox%", lootbox));
        //sender.playSound(sender.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
        //player.playSound(sender.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
        player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_RECEIVED.getMessage().replace("%lootbox%", CC.translate(this.plugin.getHypeBoxRegister().getHypebox(lootbox).getDisplayName())));
    }

    @SubCommand("giveall")
    @Permission("astro.admin")
    @WrongUsage("&eUsage: /hb giveall <lootbox> [amount]")
    public void giveCommand(CommandSender commandSender, @Completion("#hypeboxes") String lootbox, @Optional Integer amount) {
        if(amount == null) amount = 1;

        if(!this.plugin.getHypeBoxRegister().isHypebox(lootbox)) {
            commandSender.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_INVALID.getMessage().replace("%lootbox%", lootbox));
            SoundUtils.tryPlayErrorSound(commandSender);
            return;
        }

        commandSender.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_GIVEN.getMessage().replace("%lootbox%", lootbox));
        SoundUtils.tryPlaySuccessSound(commandSender);

        final Map<String, Integer> ipMap = new HashMap<>();
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if(commandSender instanceof Player) {
                if(p.getUniqueId().toString().equals(((Player) commandSender).getUniqueId().toString())) {
                    continue;
                }
            }
            final String ip = p.getAddress().getAddress().toString();
            if (ipMap.containsKey(ip) && ipMap.get(ip) > 0) {
                continue;
            }

            this.plugin.getHypeBoxManager().giveHypebox(p, this.plugin.getHypeBoxRegister().getHypebox(lootbox), amount);
            p.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_RECEIVED.getMessage().replace("%lootbox%",  CC.translate(this.plugin.getHypeBoxRegister().getHypebox(lootbox).getDisplayName())));

            p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
            ipMap.put(ip, ipMap.getOrDefault(ip, 0) + 1);
        }
    }


    @SubCommand("addreward")
    @Alias({"additem", "addloot"})
    @Permission("astro.admin")
    @WrongUsage("&eUsage: /hb addreward <lootbox> <chance> <value>")
    public void addItemCommand(CommandSender commandSender, @Completion("#hypeboxes") String lootbox, Double chance, Double value) {
        this.plugin.getHypeBoxManager().addItem((Player)commandSender, lootbox, chance, value);
    }

    @SubCommand("removereward")
    @Alias({"removeitem", "removeloot"})
    @Permission("astro.admin")
    public void removeItemCommand(CommandSender commandSender, @Completion("#hypeboxes") String lootbox) {
        Player player = (Player)commandSender;
        this.plugin.getHypeBoxManager().removeItemFromHypebox(player, player.getItemInHand(), lootbox);
    }

    @SubCommand("clearrewardsinallhypeboxes")
    @Permission("*")
    public void clear(CommandSender commandSender) {
        if(commandSender instanceof Player) return;
        this.plugin.getHypeBoxRegister().getLootboxes().forEach((lootboxName, lootbox) -> {
            lootbox.getRewards().clear();

            File file = new File(this.plugin.getDataFolder() + "/Hypeboxes/" + lootboxName + ".yml");
            if (file.exists()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                yamlConfiguration.set("rewards", null);
                try {
                    yamlConfiguration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        commandSender.sendMessage(CC.translate("&aSuccessfully cleared all lootbox rewards"));

    }

    @SubCommand("half")
    @Alias({"leftorright", "halfgen", "randomhalf"})
    @WrongUsage("/hb half <lootbox> [left/right]")
    @Permission("astro.admin")
    public void giveHalf(final Player player, String lootbox, @Optional String half) {

        if(!this.plugin.getHypeBoxRegister().isHypebox(lootbox)) {
            player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_INVALID.getMessage().replace("%lootbox%", lootbox));
            SoundUtils.tryPlayErrorSound(player);
            return;
        }

        HypeBox lootboxObject = this.plugin.getHypeBoxRegister().getHypebox(lootbox);

        if(half == null) {
            player.getInventory().addItem(lootboxObject.buildLeftOrRightHalf());
            return;
        }
        half = half.toLowerCase(Locale.ROOT).equals("left") ? "left" : "right";
        player.getInventory().addItem(lootboxObject.buildHalf(half));
        player.sendMessage(CC.translate("&aGiven " + half + " half for " + lootbox));
    }

    @SubCommand("battle")
    public void battle(final Player player) {
        HypeboxQueueInventory hypeboxQueueInventory = new HypeboxQueueInventory(player);
        hypeboxQueueInventory.getUI().open(player);
    }

    private void sendAdminHelpMessage(Player player) {
        player.sendMessage("");
        player.sendMessage(CC.translate("&6Astro&fPvP &8| &cHypeboxes"));
        player.sendMessage(CC.translate("&e[] &8- &fOptional Argument"));
        player.sendMessage(CC.translate("&6<> &8- &fRequired Argument"));
        player.sendMessage("");
        player.sendMessage(CC.translate(" &6* &f/hb create <name>"));
        player.sendMessage(CC.translate(" &6* &f/hb delete <name>"));
        player.sendMessage(CC.translate(" &6* &f/hb enable <name>"));
        player.sendMessage(CC.translate(" &6* &f/hb disable <name>"));
        player.sendMessage(CC.translate(" &6* &f/hb additem <name> <chance> <value>"));
        player.sendMessage(CC.translate(" &6* &f/hb removeitem <name>"));
        player.sendMessage(CC.translate(" &6* &f/hb reload [lootbox]"));
        player.sendMessage(CC.translate(" &6* &f/hb list"));
        player.sendMessage(CC.translate(" &6* &f/hb give <player> <lootbox> [amount]"));
        player.sendMessage(CC.translate(" &6* &f/hb giveall <lootbox> [amount]"));
        player.sendMessage("");
    }
}
