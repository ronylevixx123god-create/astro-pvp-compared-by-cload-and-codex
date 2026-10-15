/*
 * Decompiled with CFR 0.150.
 */
package net.astropvp.core.titles.utils;

import java.util.ArrayList;

public class TitleUtil {
    public static ArrayList<String> splitTitleAtColorCodes(String title) {
        ArrayList<String> preretlist = new ArrayList<String>();
        String strbuf = "";
        for (int i = 0; i <= title.length() - 1; ++i) {
            if (title.charAt(i) == '&') {
                if (strbuf != "") {
                    preretlist.add(strbuf);
                }
                strbuf = "";
                if (title.length() > i + 2 && title.substring(i, i + 2) != "") {
                    preretlist.add(title.substring(i, i + 2));
                }
                ++i;
                continue;
            }
            strbuf = strbuf + title.charAt(i);
        }
        if (strbuf != "") {
            preretlist.add(strbuf);
        }
        boolean last_string_was_color = false;
        ArrayList<String> retlist = new ArrayList<String>();
        String strbf = "";
        for (String s : preretlist) {
            if (TitleUtil.isColorCode(s)) {
                if (last_string_was_color) {
                    strbf = strbf + s;
                    continue;
                }
                last_string_was_color = true;
                strbf = s;
                continue;
            }
            last_string_was_color = false;
            retlist.add(strbf);
            strbf = "";
            retlist.add(s);
        }
        return retlist;
    }

    public static String removeColorCodes(String title) {
        StringBuffer newTitle = new StringBuffer();
        for (String s : TitleUtil.splitTitleAtColorCodes(title)) {
            if (TitleUtil.isColorCode(s)) continue;
            newTitle.append(s);
        }
        return newTitle.toString();
    }

    public static boolean isColorCode(String cc) {
        return cc.startsWith("&");
    }

    public static String optimizeTitle(String title) {
        String optimizedTitle = "";
        String lastcolor = "";
        for (String s : TitleUtil.splitTitleAtColorCodes(title)) {
            if (TitleUtil.isColorCode(s)) {
                if (!lastcolor.equalsIgnoreCase(s)) {
                    optimizedTitle = optimizedTitle + s.toLowerCase();
                }
                lastcolor = s;
                continue;
            }
            optimizedTitle = optimizedTitle + s;
        }
        boolean next_char_is_uppercase = false;
        for (String s : optimizedTitle.split("~u")) {
            if (s == "~u") {
                next_char_is_uppercase = true;
                continue;
            }
            if (!next_char_is_uppercase) continue;
            optimizedTitle = optimizedTitle + Character.toUpperCase(s.charAt(0)) + optimizedTitle.substring(1);
        }
        return optimizedTitle;
    }
}

