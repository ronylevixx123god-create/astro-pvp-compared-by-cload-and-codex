package me.astropvp.astropvpskins.utils;

import me.astropvp.astropvpskins.enums.SkinType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

    public static List<String> fixSkinList(String s) {
        s = s.replaceAll(" ", "");
        s = s.replace("[", "");
        s = s.replace("]", "");
        List<String> stringSkins = new ArrayList<>(Arrays.asList(s.split(",")));
        List<String> copy = new ArrayList<>(stringSkins);

        List<SkinType> skins = new ArrayList<>();
        return getSkinTypes(stringSkins, copy, skins).stream().map(skin -> skin.name()).collect(Collectors.toList());
    }

    public static List<SkinType> stringToSkinList(String s) {
        s = s.replaceAll(" ", "");
        s = s.replace("[", "");
        s = s.replace("]", "");
        List<String> stringSkins = new ArrayList<>(Arrays.asList(s.split(",")));
        List<String> copy = new ArrayList<>(stringSkins);

        List<SkinType> skins = new ArrayList<>();
        return getSkinTypes(stringSkins, copy, skins);
    }

    public static List<SkinType> getSkinTypes(List<String> stringSkins, List<String> copy, List<SkinType> skins) {
        for (int i = 0; i < copy.size(); i++) {
            String string = copy.get(i);
            string = string.replace("[", "");
            string = string.replace("]", "");
            string = string.replace(" ", "");
            try {
                if(string.contains("NEXT_MUTATED")) {
                    stringSkins.set(i+1, "MMM_" + stringSkins.get(i+1));
                    stringSkins.remove(i);
                }
            } catch (Exception ignored) {
            }
        }
        for (String stringSkin : stringSkins) {
            if(Arrays.stream(SkinType.values()).map(skin -> skin.name()).collect(Collectors.toList()).contains(stringSkin)) {
                skins.add(SkinType.valueOf(stringSkin));
            }
        }
        return skins;
    }
}
