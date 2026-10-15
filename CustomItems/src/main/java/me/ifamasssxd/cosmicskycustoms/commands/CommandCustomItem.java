package me.ifamasssxd.cosmicskycustoms.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItems;
import me.ifamasssxd.cosmicskycustoms.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CommandCustomItem
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.isOp()) {
            return true;
        }
        if (args.length >= 3 && args[0].equalsIgnoreCase("give")) {
            ItemStack is;
            Player pl = Bukkit.getPlayer(args[1]);
            if (pl == null) {
                commandSender.sendMessage(CC.RedB + "(!) " + CC.Red + "No player found for " + args[1]);
                return true;
            }
            CosmicItem item = CosmicItems.getCustomItem(args[2]);
            if (item == null) {
                commandSender.sendMessage("Unable to find CosmicItem for name " + args[2] + ", valid: " + Arrays.toString(CosmicSkyCustomItems.get().getItemManager().getCustomRegisteredItems().keySet().stream().sorted().toArray()));
                return true;
            }
            if (args.length > 3) {
                Object[] data = new Object[args.length - 3];
                int index = 0;
                for (int i = 3; i < args.length; ++i) {
                    data[index++] = args[i];
                }
                is = item.createItem(null, data);
            } else {
                is = item.createItem(null);
            }
            if(args[2] != "nebula_rank") {

                sendWebHook(commandSender.getName(), pl.getName(), System.currentTimeMillis(), args[2]);
            }
            PlayerUtil.giveOrDropProtectedItem(pl, is, -1);
            commandSender.sendMessage(CC.Green + "Item given!");
            return true;
        }
        commandSender.sendMessage(CC.Red + "/customitems give <player> <custom>");
        return false;
    }




    public void sendWebHook(String playerName, String givenTo, long dupedAt, String itemName) {
        String webhookUrl = me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems.get().getConfig().getString("discord-webhooks.custom-items", "");
        if (webhookUrl.isEmpty()) {
            return;
        }
        WebhookClientBuilder builder = new WebhookClientBuilder(webhookUrl); // or id, token

        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Custom Item");
            thread.setDaemon(true);
            return thread;
        });

        builder.setWait(true);

        WebhookClient client = builder.build();


        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0X610000)
                .setTitle(new WebhookEmbed.EmbedTitle("**Custom Item - " + itemName + "**", null))
                .setFooter(new WebhookEmbed.EmbedFooter("Given At: | " + TimeUtils.formatTimeMillis(dupedAt), null))
                .setDescription("**Player:** `" + playerName + "`\n" + "**Given To:** `" + givenTo + "`\n" + "**Custom Item:** `" + itemName + "`\n")
                .build();

        WebhookMessageBuilder idBuilder = new WebhookMessageBuilder();
        idBuilder.setUsername("/customitems");
        idBuilder.setAvatarUrl("https://cdn.discordapp.com/avatars/1202888199903641640/cad565e0a6bcd051ce0fd36b70c1f023.webp");
        idBuilder.addEmbeds(embed);

        client.send(idBuilder.build());
    }
}

