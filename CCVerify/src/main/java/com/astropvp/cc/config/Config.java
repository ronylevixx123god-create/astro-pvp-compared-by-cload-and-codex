package com.astropvp.cc.config;

import com.astropvp.cc.CCVerify;
import com.astropvp.cc.util.Chat;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    @Getter
    private static Config inst;

    private Config(){}

    public static class ModPermissions {
        @Getter private boolean allowPrinter = true;
        @Getter private boolean allowRoam = true;
        @Getter private boolean allowBreadcrumbs = true;
        @Getter private boolean allowOp = false;
        @Getter private boolean allowInterstellar = false;
        @Getter private boolean allowBanteam = false;
    }

    @Getter private ModPermissions modpermissions;

    public static class Voice {
        @Getter private int voiceProximity = 64;

        @Getter private boolean enabled = true;

        @Getter private String setChannel = "&e&l(!) &aSet active voice channel to : &f&l{CHANNEL} &e&l(!)";
        @Getter private String removeChannel = "&e&l(!) &aTurned off voice activity &e&l(!)";
        @Getter private String lockedVoice = "&e&l(!) &aVoice activity has been halted! &e&l(!)";
        @Getter private String unlockedVoice = "&e&l(!) &aVoice activity is no longer halted! &e&l(!)";
        @Getter private String currentlyLocked = "&e&l(!) &aVoice activity is currently locked! &e&l(!)";

        @Getter private List<String> helpMessageNormal = Lists.newArrayList("&cUsage: /voicechat faction/global/proximity/off");
        @Getter private List<String> helpMessageAdmin = Lists.newArrayList("&cUsage: /voicechat faction/global/proximity/off/lock");
    }

    @Getter private Voice voice;

    public static class Ping {
        @Getter private String pingMessage = "&e&l(!)&r &a{NAME}'s ping coordinates: &f&l{LOCATION} &a({DISTANCE}m) &e&l(!)";
    }

    @Getter private Ping ping;

    public static class Emotes {
        @Getter private List<String> allowedEmotes = Lists.newArrayList(
                "default",
                "thinking",
                "infinite_dab",
                "hype",
                "take_the_l",
                "shrug",
                "twerk",
                "orange_justice",
                "electro_shuffle",
                "poltergeist",
                "gangnam_style",
                "star_power",
                "boneless",
                "stuffed",
                "jig",
                "flirt",
                "clickclack",
                "corona",
                "best_mates",
                "spooky_skeleton",
                "turkey_vibes"
        );

        @Getter private int showInRadius = 80;
    }

    @Getter private Emotes emotes;

    public static class Headers {
        private List<String> formatted = Lists.newArrayList("Apollo:&8&m-----,&3&l<&b&lApollo&3&l>,&8&m-----");

        public Map<String, List<String>> getAllHeaders(){
            Map<String, List<String>> toReturn = new HashMap<>();

            formatted.forEach(str -> {
                String[] split = str.split(":");

                String[] subSplit = split[1].split(",");

                List<String> fullList = Lists.newArrayList(subSplit).stream().map(Chat::c).collect(Collectors.toList());

                toReturn.put(split[0], fullList);
            });

            return toReturn;
        }
    }

    @Getter private Headers headers;

    public static class Popup {
        @Getter private String popupSendAll = "&e&l(!)&r &aSent the notification to everyone on the server! &e&l(!)";
        @Getter private String popupSendOne = "&e&l(!)&r &aSent the notification to &f&l{PLAYER}&a! &e&l(!)";
    }

    @Getter private Popup popup;

    public static class PurchaseFeed {
        @Getter private boolean purchaseFeedEnabled = true;
    }

    @Getter private PurchaseFeed purchasefeed;

    public static class Tooltip {
        @Getter private boolean sendDiscordTooltipOnJoin = true;
        @Getter private String discordTooltipTitle = "&d&lJoin our discord!";
        @Getter private String discordTooltipText = "                   &dMake sure to join our discord at &5discord.gg/ctest&d!";
        @Getter private int discordTooltipItem = 339;
        @Getter private int discordTooltipItemDamage = 0;
    }

    @Getter private Tooltip tooltip;

    public static Config bootStrap(FileConfiguration config, File file){
        Class<Config> clazz = Config.class;

        try{
            inst = clazz.newInstance();

            loadClassMembers(config, clazz, inst, "Settings.");

            config.save(file);
        } catch (IllegalAccessException | InstantiationException | IOException e) {
            Bukkit.getLogger().info("Failed to load the config!");
            e.printStackTrace();
        }
        return inst;
    }

    private static void loadClassMembers(FileConfiguration config, Class<?> clazz, Object currentInst, String currentPath){
        fields: for(Field field : clazz.getDeclaredFields()){
            try{
                field.setAccessible(true);

                if(config.contains(currentPath + field.getName())){
                    Object type = config.get(currentPath + field.getName());

                    field.set(currentInst, type);
                }else{
                    for(Class<?> inner : clazz.getDeclaredClasses()){
                        if(inner == field.getType() || field.getType() == clazz)continue fields;
                    }

                    config.set(currentPath + field.getName(), field.get(currentInst));
                }
            }catch (IllegalAccessException e) {
                Bukkit.getLogger().info("Failed to load variable in config!");
                e.printStackTrace();
            }
        }

        for(Class<?> next : clazz.getDeclaredClasses()){
            try {
                Object nextInst = next.newInstance();

                Field declaredField = clazz.getDeclaredField(next.getSimpleName().toLowerCase());

                declaredField.setAccessible(true);

                declaredField.set(currentInst, nextInst);

                loadClassMembers(config, next, nextInst, currentPath + next.getSimpleName() + ".");
            } catch (IllegalAccessException | InstantiationException | NoSuchFieldException e) {
                Bukkit.getLogger().info("Failed to load the config!");
                e.printStackTrace();
            }
        }
    }
}
