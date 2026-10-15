package net.astropvp.core.struct;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.storage.utils.KeyToSave;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AstroPlayer implements KeyToSave {
    private UUID uuid;
    private boolean factionsLandTitleEnabled;
    private boolean killTitleEnabled;
    private boolean potsDropEnabled;
    private boolean bossMessagesEnabled;
    private List<Gift> gifts;

    private ChatColor chatColor;
    private boolean lootTableMessageEnabled;

    public AstroPlayer(UUID uuid) {
        this.uuid = uuid;
        this.gifts = new ArrayList<>();
        this.factionsLandTitleEnabled = true;
        this.killTitleEnabled = true;
        this.lootTableMessageEnabled = true;
        this.potsDropEnabled = true;
        this.bossMessagesEnabled = false;
        this.chatColor = ChatColor.WHITE;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public void setKey(String s) {
        this.uuid = UUID.fromString(s);
    }

    public boolean alreadyHasOver5GiftsFromPlayer(Player sender) {
        if (gifts == null) {
            gifts = new ArrayList<>();
        }
        int giftsAmount = 0;
        for (Gift gift : gifts) {
            if (gift.getSender() == sender.getUniqueId()) {
                giftsAmount++;
            }
        }
        return giftsAmount >= 5;
    }

    public void addGift(Player sender, ItemStack itemStack, String[] message) {
        if (gifts == null) {
            gifts = new ArrayList<>();
        }
        this.getGifts().add(new Gift(sender, itemStack, Arrays.stream(message).reduce("", (firstString, secondString) -> firstString + " " + secondString)));
    }
}
