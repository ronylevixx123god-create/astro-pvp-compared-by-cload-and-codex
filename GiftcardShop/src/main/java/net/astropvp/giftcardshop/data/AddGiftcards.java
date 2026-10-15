package net.astropvp.giftcardshop.data;


import net.astropvp.giftcardshop.GiftcardShopPlugin;
import net.astropvp.giftcardshop.commands.GiftcardsCommand;
import net.astropvp.giftcardshop.struct.Giftcard;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AddGiftcards {

    public static void addNewGiftcard(UUID uuid, String codeAndAmount) {
        File file = new File(GiftcardShopPlugin.getInstance().getDataFolder().toString() + "/data/" + uuid + "-" + new Date().toString() + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        String code = codeAndAmount.split(":")[0];
        double amount = Double.parseDouble(codeAndAmount.split(":")[1]);
        Giftcard giftcard = new Giftcard(uuid, code, new Date().toString(), amount);
        GiftcardShopPlugin.getInstance().addGiftcardToList(giftcard);
        YamlConfiguration ymlFile = YamlConfiguration.loadConfiguration(file);

        ymlFile.set("uuid", giftcard.getUuid().toString());
        ymlFile.set("code", giftcard.getCode());
        ymlFile.set("amount", giftcard.getAmount());
        ymlFile.set("creationDate", giftcard.getCreationDate());


        try {
            ymlFile.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
