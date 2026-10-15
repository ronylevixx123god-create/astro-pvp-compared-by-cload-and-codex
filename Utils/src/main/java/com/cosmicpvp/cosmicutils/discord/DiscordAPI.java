/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package com.cosmicpvp.cosmicutils.discord;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

public class DiscordAPI {
    public static void sendMessage(DiscordChannel channel, String message) {
        JsonObject postData = channel.getPostData();
        postData.addProperty("content", message);
        DiscordAPI.sendRequest(channel.getURL(), postData.toString());
    }

    public static void sendMessage(CosmicDiscordChannels channel, String message) {
        DiscordAPI.sendMessage(channel.getChannel(), message);
    }

    private static void sendRequest(String url, String postData) {
        Bukkit.getScheduler().runTaskAsynchronously(CosmicUtils.get(), () -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
                con.setReadTimeout(5000);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream out = con.getOutputStream();
                out.write(postData.getBytes());
                out.flush();
                out.close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

