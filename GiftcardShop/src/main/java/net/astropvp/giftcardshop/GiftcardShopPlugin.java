package net.astropvp.giftcardshop;

import lombok.Getter;
import net.astropvp.giftcardshop.commands.CreateGiftcardCommand;
import net.astropvp.giftcardshop.commands.GiftcardsCommand;
import net.astropvp.giftcardshop.commands.ResetGiftcards;
import net.astropvp.giftcardshop.data.LoadGiftcards;
import net.astropvp.giftcardshop.struct.Giftcard;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

@Getter
public class GiftcardShopPlugin extends JavaPlugin {
    private static GiftcardShopPlugin instance;
    private Map<UUID, List<Giftcard>> giftcards;


    @Override
    public void onEnable() {
        instance = this;
        getCommand("creategiftcard").setExecutor(new CreateGiftcardCommand());
        getCommand("giftcards").setExecutor(new GiftcardsCommand());
        getCommand("resetgiftcards").setExecutor(new ResetGiftcards());


        File file = new File(this.getDataFolder() + File.separator + "data");
        if(!file.exists()) {
            file.mkdir();
        }
        this.giftcards = new HashMap<>();
        LoadGiftcards.load();
        System.out.println("[GiftcardPlugin] GiftcardShopPlugin has been Loaded!");
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static GiftcardShopPlugin getInstance() {
        return instance;
    }

    public void addGiftcardToList(Giftcard giftcard) {
        if (giftcards.containsKey(giftcard.getUuid())) {
            giftcards.get(giftcard.getUuid()).add(giftcard);
        } else {
            List<Giftcard> list = new ArrayList<>();
            list.add(giftcard);
            giftcards.put(giftcard.getUuid(),list);
        }
    }

}
