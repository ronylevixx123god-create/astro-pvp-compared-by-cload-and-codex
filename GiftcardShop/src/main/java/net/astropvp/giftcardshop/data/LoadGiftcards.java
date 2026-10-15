package net.astropvp.giftcardshop.data;

import net.astropvp.giftcardshop.GiftcardShopPlugin;
import net.astropvp.giftcardshop.struct.Giftcard;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LoadGiftcards {
    public static void load() {

        // Looping through all the files in a directory.
        File directory = new File(GiftcardShopPlugin.getInstance().getDataFolder() + File.separator + "data");
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file == null) { // Null check because those pesky files!
                return;
            }
            // Load the file into YAML because we need to get some values from it!
            YamlConfiguration ymlFile = YamlConfiguration.loadConfiguration(file);


            String code = ymlFile.getString("code");
            UUID uuid = UUID.fromString(ymlFile.getString("uuid"));
            String date = ymlFile.getString("creationDate");
            double amount = ymlFile.getDouble("amount");

            Giftcard giftcard = new Giftcard(uuid, code, date, amount);
            GiftcardShopPlugin.getInstance().addGiftcardToList(giftcard);

        }
    }

}

