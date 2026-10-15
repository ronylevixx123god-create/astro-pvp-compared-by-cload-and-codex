package net.astropvp.giftcardshop.data;

import net.astropvp.giftcardshop.GiftcardShopPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class DeleteGiftcards {
    public static void func(UUID uuid, String code) {
        File file = new File(GiftcardShopPlugin.getInstance().getDataFolder().toString() + "/data/" + uuid + ".yml");



    }
}
