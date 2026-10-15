package me.ifamasssxd.cosmicmysterychest.guis;


import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import de.tr7zw.nbtapi.NBTItem;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import me.ifamasssxd.cosmicmysterychest.utils.TimeUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LootboxesMenu {
    private final PaginatedGui lootboxMenu;

    public PaginatedGui getExpShopUI() {
        return this.lootboxMenu;
    }


    Map<String, MysteryChest> chests = CosmicMysteryChest.get().getChestManager().getMysteryChestMapSorted();


    public LootboxesMenu(Player player) {

        this.lootboxMenu = new PaginatedGui(6, 45, "Lootbox Menu");
        lootboxMenu.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        lootboxMenu.getFiller().fillBottom(new GuiItem(createFillerItem()));

        ItemStack prevPage = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prevPage.getItemMeta();
        prevMeta.setDisplayName(CC.translate("&c<- Previous Page"));
        List<String> prevLore = new ArrayList<>();
        prevLore.add(CC.translate("&7Right-Click&7 to go to the previous page."));
        prevMeta.setLore(prevLore);
        prevPage.setItemMeta(prevMeta);

        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextPage.getItemMeta();
        nextMeta.setDisplayName(CC.translate("&aNext Page ->"));
        List<String> nextLore = new ArrayList<>();
        nextLore.add(CC.translate("&7Right-Click&7 to go to the next page."));
        nextMeta.setLore(nextLore);
        nextPage.setItemMeta(nextMeta);

        lootboxMenu.setItem(6, 1, new GuiItem(prevPage, event -> {
            lootboxMenu.previous();
            event.setCancelled(true);
        }));

        lootboxMenu.setItem(6, 9, new GuiItem(nextPage, event -> {
            lootboxMenu.next();
            event.setCancelled(true);
        }));


        for (MysteryChest chest : chests.values()) {
            ItemStack item = chest.createClaimableItem(false);

            lootboxMenu.addItem(new GuiItem(item, event -> {
                if (event.isRightClick() && event.isShiftClick()) {
                    player.sendMessage(CC.translate("&c&l(!)&c Deleted &c" + chest.getInternalName() + "&c."));
                    player.getInventory().addItem(item.clone());
                    CosmicMysteryChest.getInstance().getChestManager().removeMysteryChest(chest);
                    LootboxesMenu lootboxesMenu = new LootboxesMenu(player);
                    lootboxesMenu.getExpShopUI().open(player, this.getExpShopUI().getCurrentPageNum());
                    return;

                }
                player.getInventory().addItem(addMetadataStringsToLootbox(item, player));
                player.sendMessage(CC.translate("&a&l(!)&a Given you &a" + chest.getInternalName() + "&a."));
                sendWebHook(player.getName(), chest.getInternalName());
                }
            ));
        }



    }

    private ItemStack addMetadataStringsToLootbox(ItemStack itemStack, Player player) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("lootboxCreatedBy", player.getName());
        return nbtItem.getItem();
    }




    public void sendWebHook(String playerName, String itemName) {
        String webhookUrl = me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest.getInstance().getConfig().getString("discord-webhooks.lootboxes", "");
        if (webhookUrl.isEmpty()) {
            return;
        }
        WebhookClientBuilder builder = new WebhookClientBuilder(webhookUrl); // or id, token

        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Taken Lootbox");
            thread.setDaemon(true);
            return thread;
        });

        builder.setWait(true);

        WebhookClient client = builder.build();


        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0Xff7575)
                .setTitle(new WebhookEmbed.EmbedTitle("**Taken Lootbox - " + itemName + "**", null))
                .setFooter(new WebhookEmbed.EmbedFooter("At: | " + TimeUtils.formatTimeMillis(System.currentTimeMillis()), null))
                .setDescription("**Player:** `" + playerName + "`\n" + "**Lootbox:** `" + itemName + "`\n")
                .build();

        WebhookMessageBuilder idBuilder = new WebhookMessageBuilder();
        idBuilder.setUsername("Lootbox Given");
        idBuilder.setAvatarUrl("https://cdn.discordapp.com/avatars/1202888199903641640/cad565e0a6bcd051ce0fd36b70c1f023.webp");
        idBuilder.addEmbeds(embed);

        client.send(idBuilder.build());
    }



    public ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&f"));
        item.setItemMeta(meta);
        return item;
    }

}
