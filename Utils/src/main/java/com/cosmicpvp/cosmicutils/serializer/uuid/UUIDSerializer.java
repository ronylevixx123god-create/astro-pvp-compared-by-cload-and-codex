package com.cosmicpvp.cosmicutils.serializer.uuid;


import java.util.UUID;

public class UUIDSerializer  {

    public static String serialize(UUID obj) {
        if(obj == null) return "";
        return obj.toString();
    }

    public static UUID deserialize(String s) {
        try {
            return UUID.fromString(s);
        }catch(Exception e) {
            return null;
        }
    }

}
