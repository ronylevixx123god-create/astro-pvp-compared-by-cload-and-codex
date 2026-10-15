package me.astropvp.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import me.astropvp.AstroRevivePlugin;
import me.astropvp.inventories.ScrollerInventory;
import me.astropvp.objects.DeathInventory;
import me.astropvp.utils.Base64Utils;
import me.astropvp.utils.SortMapByValue;
import me.astropvp.utils.TimeUtil;
import me.astropvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class ReviveCommand
        implements CommandExecutor,
        Listener {
    AstroRevivePlugin inst = null;

    public ReviveCommand(AstroRevivePlugin inst) {
        this.inst = inst;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        DeathInventory inv = new DeathInventory(this.inst, event.getEntity(), this.getKillerName(event), event.getEntity().getLastDamage() + "");
    }

    public String getKillerName(PlayerDeathEvent event) {
        try {
            return event.getEntity().getKiller().getName();
        }
        catch (Exception e) {
            return "N/A";
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player)((Object)event.getWhoClicked());
        if (event.getInventory().getTitle().startsWith("§cRevive: ")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().getItemMeta().hasDisplayName()) {
                return;
            }
            Player target = Bukkit.getPlayer(event.getInventory().getTitle().split(" ")[1]);
            if (target == null) {
                player.sendMessage("§cThat player has logged out.");
                player.closeInventory();
                return;
            }
            ArrayList<DeathInventory> inventories = this.getInventories(target.getUniqueId());
            int currentPage = Integer.parseInt(((String)event.getInventory().getItem(49).getItemMeta().getLore().get(4)).split(" ")[1]) - 1;
            if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§bNext Inventory")) {
                if (currentPage == inventories.size() - 1) {
                    player.sendMessage("§c§l(!)§c There is no next page avalible.");
                    return;
                }
                this.deathInventory(player, target, currentPage + 1);
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§bPrevious Inventory")) {
                if (currentPage == 0) {
                    player.sendMessage("§c§l(!)§c There is no previous page avalible.");
                    return;
                }
                this.deathInventory(player, target, currentPage - 1);
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aRevive")) {
                DeathInventory deathInventory = this.getInventories(target.getUniqueId()).get(currentPage);
                deathInventory.revive(player);
                player.sendMessage("§eThe player §f" + target.getName() + "§e has been revived.");
                player.closeInventory();
                sendWebHook(target, player, deathInventory);
                return;
            }
        }
    }

    public void sendWebHook(Player target, Player revivedBy, DeathInventory deathInventory) {
        String webhookUrl = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(new java.io.File(this.inst.getDataFolder(), "webhooks.yml")).getString("discord-webhooks.revive", "");
        if (webhookUrl.isEmpty()) {
            return;
        }
        WebhookClientBuilder builder = new WebhookClientBuilder(webhookUrl); // or id, token

        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Revive");
            thread.setDaemon(true);
            return thread;
        });

        builder.setWait(true);

        WebhookClient client = builder.build();


        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0XF8EB2A)
                .setTitle(new WebhookEmbed.EmbedTitle("**Revived - " + target.getName() + "**", null))
                .setFooter(new WebhookEmbed.EmbedFooter("Revived | " + Instant.now(), null))
                .setDescription(
                                "**Player:** `" + target.getName() + "`\n"
                                + "**Revived By:** `" + revivedBy.getName() + "`\n"
                                + "**Item Amount:** `" + deathInventory.getTotal() + "`\n"
                                + "**Killer:** `" + deathInventory.getKiller() + "`\n"
                                + "**Died At:** <t:" + (deathInventory.getTime()/1000) + ">\n"
                                //+ "**Armor:** " + deathInventory.getArmor() + "\n"
                                + "**Damage:** `" + deathInventory.getDamage() + "`\n"
                                + "\n"
                )
                .build();

        WebhookMessageBuilder idBuilder = new WebhookMessageBuilder();
        idBuilder.setUsername("Revive Log");
        idBuilder.setAvatarUrl("https://cdn.discordapp.com/avatars/1202888199903641640/cad565e0a6bcd051ce0fd36b70c1f023.webp");
        idBuilder.addEmbeds(embed);

        client.send(idBuilder.build());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("revive")) {
            return false;
        }
        if (!sender.hasPermission("prorevive.revive")) {
            sender.sendMessage("§c[!] You don't has access to that command.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Error, you are not a player!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§7USAGE: §c/revive <player>");
            sender.sendMessage("§7Revive a player with their most recent death inventory.");
            return true;
        }
        Player player = (Player)((Object)sender);
        Player target = Bukkit.getPlayer(args[0]);
        if (args[0].equalsIgnoreCase("total")) {
            int time = (int)((double)this.inst.getDeathInventories().size() * 1.3);
            sender.sendMessage("§c§l(!)§c Total inventories §f" + this.inst.getDeathInventories().size() + " §cTime to save (aprox) §f" + TimeUtil.formatTime(time));
            return true;
        }
        if (args[0].equalsIgnoreCase("box")) {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            this.inst.getDeathInventories().forEach(di -> items.add(Util.createItem("§6§lDeath of§7 " + Bukkit.getOfflinePlayer(di.getPlayer()).getName(), Material.CHEST, "§cID§f #" + di.getID())));
            new ScrollerInventory(items, "Revive Box", player);
            return true;
        }
        if (target == null) {
            player.sendMessage("§c[!] Player " + args[0] + " is not online!");
            return true;
        }
        if (this.getInventories(target.getUniqueId()).isEmpty()) {
            player.sendMessage("§c§l(!) No death inventories found for player " + target.getName() + ".");
            return true;
        }
        this.deathInventory(player, target, 0);
        return true;
    }

    public void deathInventory(Player player, Player target, int page) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§cRevive: " + target.getName());
        DeathInventory deathInventory = this.getInventories(target.getUniqueId()).get(page);
        ItemStack information = Util.createItem("§6Information", 1, Material.STAINED_GLASS_PANE, "§6Time of death: §f" + this.date(deathInventory.getTime()), "§f (" + TimeUtil.formatTime(System.currentTimeMillis() - deathInventory.getTime()) + " ago )", "§6Total Items: §f" + deathInventory.getTotal(), "§6Total Inventories: §f" + this.getInventories(target.getUniqueId()).size(), "§6Page:§f " + (page + 1), "§6Killer:§f " + deathInventory.getKiller(), "§6Damage:§f " + deathInventory.getDamage());        ItemStack next = Util.createItem("§bNext Inventory", 7, Material.CARPET, "§7§m--------------------------------", "§bClick to go to the next inventory.", "§7§m--------------------------------");
        ItemStack back = Util.createItem("§bPrevious Inventory", 7, Material.CARPET, "§7§m--------------------------------", "§bClick to go to the previous inventory.", "§7§m--------------------------------");
        ItemStack revive = Util.createItem("§aRevive", 5, Material.STAINED_GLASS_PANE, "§7§m--------------------------------", "§aClick here to revive §f" + target.getName(), "§7§m--------------------------------");
        ItemStack remove = Util.createItem("§cDelete Inventory", 14, Material.STAINED_GLASS_PANE, "§7§m--------------------------------", "§cClick here to delete this inventory.", "§7§m--------------------------------");
        try {
            for (ItemStack item : Base64Utils.itemStackArrayFromBase64(deathInventory.getArmor())) {
                if (item == null) continue;
                inventory.addItem(item);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            for (ItemStack item : Base64Utils.itemStackArrayFromBase64(deathInventory.getItems())) {
                if (item == null) continue;
                inventory.addItem(item);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        inventory.setItem(53, next);
        inventory.setItem(45, back);
        inventory.setItem(50, revive);
        inventory.setItem(48, remove);
        inventory.setItem(49, information);
        player.openInventory(inventory);
    }

    public String date(Long dong) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(dong);
        return sdf.format(resultdate);
    }

    public ArrayList<DeathInventory> getInventories(UUID player) {
        ArrayList<DeathInventory> result = new ArrayList<DeathInventory>();
        HashMap<DeathInventory, Long> invs = new HashMap<DeathInventory, Long>();
        for (DeathInventory inv : this.inst.getDeathInventories()) {
            if (!inv.getPlayer().equals(player) || inv.isRevived()) continue;
            result.add(inv);
        }
        for (DeathInventory di : result) {
            invs.put(di, di.getTime());
        }
        Map<DeathInventory, Long> sortedMapAsc = SortMapByValue.sortByComparator(invs, true);
        result.clear();
        result.addAll(sortedMapAsc.keySet());
        Collections.reverse(result);
        return result;
    }

    @EventHandler(ignoreCancelled=true)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player)((Object)event.getWhoClicked());
        if (!ScrollerInventory.users.containsKey(p.getUniqueId())) {
            return;
        }
        ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
        if (event.getCurrentItem() == null) {
            return;
        }
        if (event.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.nextPageName)) {
            event.setCancelled(true);
            if (inv.currpage >= inv.pages.size() - 1) {
                return;
            }
            ++inv.currpage;
            p.openInventory(inv.pages.get(inv.currpage));
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.previousPageName)) {
            event.setCancelled(true);
            if (inv.currpage > 0) {
                --inv.currpage;
                p.openInventory(inv.pages.get(inv.currpage));
            }
        }
    }
}
