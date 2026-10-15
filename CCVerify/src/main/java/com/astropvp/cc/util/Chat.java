package com.astropvp.cc.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class Chat {

    private Chat(){}

    public static String cnp(String in, String... toParse){
        return c(p(in, toParse));
    }

    public static String p(String in, String... toParse){
        //If the placeholder lengths are invalid, correct them.
        if(toParse.length % 2 != 0){
            String[] replaced = new String[toParse.length - 1];

            if(replaced.length > 0){
                System.arraycopy(toParse, 0, replaced, 0, replaced.length);
            }

            toParse = replaced;
        }

        Map<String, String> placeholders = new HashMap<>();

        for(int i = 0; i < toParse.length; i += 2){
            placeholders.put("{" + toParse[i].toUpperCase().replace("-", "_") + "}", toParse[i + 1]);
        }

        for(Map.Entry<String, String> entry : placeholders.entrySet()){
            in = in.replace(entry.getKey(), entry.getValue());
        }
        return in;
    }

    public static String parseCommas(Integer integer){
        String string = String.valueOf(integer);

        String backwards = new StringBuilder(string).reverse().toString();

        StringBuilder toReturn = new StringBuilder();

        if(string.length() < 4)return string;

        for(int z = 0; z < string.length(); z++){
            if(z % 3 == 0 && z != 0){
                toReturn.append(",");
            }
            toReturn.append(backwards.charAt(z));
        }
        return toReturn.reverse().toString();
    }

    public static String timeFormatted(int duration, boolean dayFirst){
        int seconds = duration % 60;
        int minutes = (duration / 60) % 60;
        int hours = ((duration / 60) / 60) % 24;
        int days = ((duration / 60) / 60) / 24;

        String secondAppend = duration != 0 ? seconds + "s " : "";

        String minuteAppend = minutes != 0 || hours != 0 || days != 0 ? minutes + "m " : "";

        String hourAppend = hours != 0 || days != 0 ? hours + "h " : "";

        String dayAppend = days != 0 ? days + "d " : "";

        return dayFirst ? (dayAppend + hourAppend + minuteAppend + secondAppend).trim() : (secondAppend + minuteAppend + hourAppend + dayAppend).trim();
    }

    public static String c(String in){return ChatColor.translateAlternateColorCodes('&', in);}
}
