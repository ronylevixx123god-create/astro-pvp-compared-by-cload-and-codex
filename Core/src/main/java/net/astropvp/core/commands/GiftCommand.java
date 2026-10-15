package net.astropvp.core.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;

import net.astropvp.core.AstroCore;
import net.astropvp.core.managers.AstroPlayerManager;
import net.astropvp.core.struct.AstroPlayer;
import net.astropvp.core.utils.TimeUtils;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.bukkit.entity.Player;

@Command("gift")
public class GiftCommand extends CommandBase {

    private final AstroPlayerManager crankedUserManager;

    public GiftCommand(AstroCore plugin) {
        this.crankedUserManager = plugin.getAstroPlayerManager();
    }

    @Default
    @WrongUsage("&c/gift <player> <message>")
    public void giftToPlayer(Player sender, @Completion("#players") Player receiver, @Optional String[] message) {
        if (message == null) {
            message = new String[]{""};
        }
        if (sender.equals(receiver)) {
            sender.sendMessage(CC.translate("&c&l(!)&c You cannot send yourself gifts!"));
            return;
        }


        if (ItemUtils.isAirOrNull(sender.getItemInHand())) {
            sender.sendMessage(CC.translate("&c&l(!)&c You need to hold an item!"));
            return;
        }

        AstroPlayer crankedUser = this.crankedUserManager.getAstroPlayer(receiver.getUniqueId());

        if (crankedUser == null) {
            sender.sendMessage(CC.translate("&c&l(!)&c Player is invalid or not online!"));
            return;
        }
        if (CombatLog.inCombat(crankedUser.getPlayer())) {
            sender.sendMessage(CC.translate("&c&l(!)&c Not allowed in combat!"));
            return;
        }

        if (crankedUser.alreadyHasOver5GiftsFromPlayer(sender)) {
            sender.sendMessage(CC.translate("&c&l(!)&c You already have a sent over 5  gifts to that player, let them know they should claim them first!"));
            return;
        }

        sendWebHook(sender.getName(), crankedUser.getPlayer().getName(), System.currentTimeMillis(), CC.strip(sender.getItemInHand().getItemMeta().getDisplayName()));

        crankedUser.addGift(sender, sender.getItemInHand(), message);
        crankedUser.getPlayer().sendMessage(CC.translate("&a&l(!)&a You have received a gift from &f" + sender.getName() + "&a! Type &f/gifts&a to view all your gifts."));
        sender.sendMessage(CC.translate("&a&l(!)&a You have sent a gift to &f" + crankedUser.getPlayer().getName() + "&a!"));
        sender.setItemInHand(null);
    }



    public void sendWebHook(String playerName, String givenTo, long dupedAt, String itemName) {
        String webhookUrl = net.astropvp.core.AstroCore.getInstance().getConfig().getString("discord-webhooks.gifts", "");
        if (webhookUrl.isEmpty()) {
            return;
        }
        WebhookClientBuilder builder = new WebhookClientBuilder(webhookUrl); // or id, token

        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Gifts");
            thread.setDaemon(true);
            return thread;
        });

        builder.setWait(true);

        WebhookClient client = builder.build();


        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0X610000)
                .setTitle(new WebhookEmbed.EmbedTitle("**Gift - " + itemName + "**", null))
                .setFooter(new WebhookEmbed.EmbedFooter("Gifted At: | " + TimeUtils.formatTimeMillis(dupedAt), null))
                .setDescription("**Sender:** `" + playerName + "`\n" + "**Receiver:** `" + givenTo + "`\n" + "**Item:** `" + itemName + "`\n")
                .build();

        WebhookMessageBuilder idBuilder = new WebhookMessageBuilder();
        idBuilder.setUsername("Gifts");
        idBuilder.setAvatarUrl("https://cdn.discordapp.com/avatars/1202888199903641640/cad565e0a6bcd051ce0fd36b70c1f023.webp");
        idBuilder.addEmbeds(embed);

        client.send(idBuilder.build());
    }
}
