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
import net.astropvp.core.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command("hasbeenduped")
public class HasBeenDupedCommand extends CommandBase {

    @Default
    public void hasBeenDuped(Player player) {
        if(!player.hasPermission("admin.admin.admin")) return;

        ItemStack itemStack = player.getInventory().getItemInHand();
        if(itemStack == null || itemStack.getType() == Material.AIR) return;

        NBTItem nbtItem = new NBTItem(itemStack);
        if(!nbtItem.hasKey("dupedBy")) {
            player.sendMessage(CC.translate("&cItem hasn't been duped by anyone"));
        } else {
            player.sendMessage(CC.translate("&aItem has been duped by " + nbtItem.getString("dupedBy")));
            sendWebHook(nbtItem.getString("dupedBy"), nbtItem.getLong("dupedAt"), CC.strip(itemStack.getItemMeta().getDisplayName()));
        }
    }


    public void sendWebHook(String playerName, long dupedAt, String itemName) {
        String webhookUrl = net.astropvp.core.AstroCore.getInstance().getConfig().getString("discord-webhooks.dupe-check", "");
        if (webhookUrl.isEmpty()) {
            return;
        }
        WebhookClientBuilder builder = new WebhookClientBuilder(webhookUrl); // or id, token

        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Duped Item");
            thread.setDaemon(true);
            return thread;
        });

        builder.setWait(true);

        WebhookClient client = builder.build();


        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0X610000)
                .setTitle(new WebhookEmbed.EmbedTitle("**Duped Item - " + itemName + "**", null))
                .setFooter(new WebhookEmbed.EmbedFooter("Duped | " + TimeUtils.formatTimeMillis(dupedAt), null))
                .setDescription("**Player:** `" + playerName + "`\n" + "**Item:** `" + itemName + "`\n")
                .build();

        WebhookMessageBuilder idBuilder = new WebhookMessageBuilder();
        idBuilder.setUsername("Duped Item");
        idBuilder.setAvatarUrl("https://cdn.discordapp.com/avatars/1202888199903641640/cad565e0a6bcd051ce0fd36b70c1f023.webp");
        idBuilder.addEmbeds(embed);

        client.send(idBuilder.build());
    }

}
