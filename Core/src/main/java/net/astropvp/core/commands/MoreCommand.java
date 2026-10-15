package net.astropvp.core.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import de.tr7zw.nbtapi.NBTItem;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.arkhamnetwork.Arkkit.utils.TimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command("more")
public class MoreCommand extends CommandBase {

    @Default
    public void moreCommand(Player player) {
        if(player.hasPermission("essentials.more")) {
            if(player.getInventory().getItemInHand() != null) {
                player.getInventory().setItemInHand(addDataToItem(player.getInventory().getItemInHand(), player));
                player.updateInventory();
                sendWebHook(player.getName(), CC.strip(player.getInventory().getItemInHand().getItemMeta().getDisplayName()));
            } else {
                player.sendMessage("hold item !");
            }
        } else {
            player.sendMessage(CC.translate("&cNo permission :3"));
        }
    }

    private ItemStack addDataToItem(ItemStack itemStack, Player player) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("dupedBy", player.getName());
        nbtItem.setLong("dupedAt", System.currentTimeMillis());
        ItemStack itemStack1 = nbtItem.getItem();
        itemStack1.setAmount(64);
        return itemStack1;
    }





    public void sendWebHook(String playerName, String itemName) {
        String webhookUrl = net.astropvp.core.AstroCore.getInstance().getConfig().getString("discord-webhooks.more", "");
        if (webhookUrl.isEmpty()) {
            return;
        }
        WebhookClientBuilder builder = new WebhookClientBuilder(webhookUrl); // or id, token

        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Command: /more");
            thread.setDaemon(true);
            return thread;
        });

        builder.setWait(true);

        WebhookClient client = builder.build();

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0Xff7575)
                .setTitle(new WebhookEmbed.EmbedTitle("**/more - " + itemName + "**", null))
                .setFooter(new WebhookEmbed.EmbedFooter("At: | " + TimeUtils.formatTime(System.currentTimeMillis()), null))
                .setDescription("**Player:** `" + playerName + "`\n" + "**Item:** `" + itemName + "`\n")
                .build();

        WebhookMessageBuilder idBuilder = new WebhookMessageBuilder();
        idBuilder.setUsername("/more");
        idBuilder.setAvatarUrl("https://cdn.discordapp.com/avatars/1202888199903641640/cad565e0a6bcd051ce0fd36b70c1f023.webp");
        idBuilder.addEmbeds(embed);

        client.send(idBuilder.build());
    }
}
