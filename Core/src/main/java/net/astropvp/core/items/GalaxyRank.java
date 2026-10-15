package net.astropvp.core.items;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import me.ily2bb.slotbot.utils.Utils;
import net.astropvp.core.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GalaxyRank extends CosmicItem implements InventoryInteractable {
    public GalaxyRank() {
        super("galaxy_rank");
    }

    public ItemStack createItem(Player player, Object ... objects) {
        ItemStack itemStack = this.applyItemData((new ItemBuilder(Material.NETHER_STAR, CC.translate("&f&l<&f&k;&r&c&lG&6&lA&e&lL&a&lA&b&lX&d&lY&f&k;&f&l>&c&l RANK &f&l&oVoucher &7(Right Click)")
                , CC.Gray
                , CC.translate("&7&nRight-Click&7 to unlock"),
                CC.translate("&7the &f&l<&f&k;&r&c&lG&6&lA&e&lL&a&lA&b&lX&d&lY&f&k;&f&l> &7rank on this server!"),
                CC.Gray,
                CC.translate("&c&lGalaxy Perks"),
                CC.translate("&c&l* &fAll Nebula Perks"),
                CC.translate("&c&l* &fNebula shop is FREE"),
                CC.translate("&c&l* &fAccess to Galaxy /kit &7(3 Days Cooldown)"),
                CC.translate("&c&l* &fAccess to private giveaways"),
                CC.translate("&c&l* &f10% Chance To Keep Lootbox"),
                CC.translate("&c&l* &fGalaxy Prefix"),
                CC.translate("&c&l* &fNo Teleport Timer"),
                CC.translate("&c&l* &fTop Queue priority"),
                CC.translate("&c&l* &fand much more..."),
                "",
                CC.translate("&8[&c✪&8] &cobtained from &f&nbuy.astropvp.net&7 (&n/buy&7)")

        ).build())).build();

        return Utils.addSkin("INTERSTELLAR_ORB", itemStack);
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CLICKED;
    }

    public void onInventoryClick(Player player, ItemStack cursor, NBTWrapper wrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent e) throws CosmicException {

    }

    public void onItemUse(Player player, ItemStack item, NBTWrapper wrapper, PlayerInteractEvent event) throws CosmicException {
        if (item == null) {
            return;
        }
        if (wrapper == null) {
            return;
        }
        if (item.getType() == Material.AIR) {
            return;
        }

        if (item.getType() != Material.NETHER_STAR) {
            return;
        }

        if (!player.getLocation().getWorld().getName().equals("world") && !player.getLocation().getWorld().getName().equals("flat")) {
            throw new PrefixedException("You cannot claim this rank in this world!");
        }

        if (item.getAmount()>1) {
            throw new PrefixedException("You cannot apply multiple ranks at once!");
        }


        if (player.hasPermission("astrocore.galaxy")) {
            throw new PrefixedException("You cannot redeem lower tier ranks.");
        }

        NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("conaxData");

        cosmicData.setInt("galaxyRank", 1);
        event.setCancelled(true);
        player.setItemInHand(null);
        wrapper.setNBTBase("conaxData", cosmicData.get());
        item = wrapper.build();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent set galaxy");
        player.sendMessage("");
        player.sendMessage(CC.translate("&9&l(!)&9 Congratulations on redeeming the Galaxy rank!"));
        player.sendMessage(CC.translate("&7You now have access to all the Galaxy perks."));
        player.sendMessage("");

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(CC.translate("&8[&6&lAstro&f&lPvP&8] &c" + player.getName() + "&f has unlocked the &f&l<&f&k;&r&c&lG&6&lA&e&lL&a&lA&b&lX&d&lY&f&k;&f&l>&c&l Rank&f!"));
        Bukkit.broadcastMessage(CC.translate("&7Purchase this rank on &f&nbuy.astropvp.net&7! (/buy)"));
        Bukkit.broadcastMessage("");

        sendWebHook(player.getName(), System.currentTimeMillis(), "Galaxy Rank");
    }



    public void sendWebHook(String playerName, long dupedAt, String itemName) {
        String webhookUrl = net.astropvp.core.AstroCore.getInstance().getConfig().getString("discord-webhooks.galaxy-rank", "");
        if (webhookUrl.isEmpty()) {
            return;
        }
        WebhookClientBuilder builder = new WebhookClientBuilder(webhookUrl); // or id, token

        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Rank Claim");
            thread.setDaemon(true);
            return thread;
        });

        builder.setWait(true);

        WebhookClient client = builder.build();


        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0X610000)
                .setTitle(new WebhookEmbed.EmbedTitle("**Rank Claim - " + itemName + "**", null))
                .setFooter(new WebhookEmbed.EmbedFooter("Claimed At: | " + TimeUtils.formatTimeMillis(dupedAt), null))
                .setDescription("**Player:** `" + playerName + "`\n" + "**Rank:** `" + itemName)
                .build();

        WebhookMessageBuilder idBuilder = new WebhookMessageBuilder();
        idBuilder.setUsername("Rank Claim");
        idBuilder.setAvatarUrl("https://cdn.discordapp.com/avatars/1202888199903641640/cad565e0a6bcd051ce0fd36b70c1f023.webp");
        idBuilder.addEmbeds(embed);

        client.send(idBuilder.build());
    }
}

