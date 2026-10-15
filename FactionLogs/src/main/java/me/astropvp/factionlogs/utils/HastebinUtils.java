package me.astropvp.factionlogs.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

public class HastebinUtils {

    private static final DateTimeFormatter DATE_FORMATED = DateTimeFormatter.ofPattern("EEE, MMM dd | HH:mm");
    private static Map<String, Long> clickCooldown = new HashMap<>();


    public static String post(String text, boolean raw) throws IOException {
        byte[] postData = text.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;

        String requestURL = "https://hastebin.com/documents";
        URL url = new URL(requestURL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "af9a940c9a9014ae9bc1e47170f9814d602464a6e35895f55ea2a5f9f754a08c1815e7fdcf5221444f0b66ec9c4154b2bc943b1b8c9c43f5f6d27bc3e6f3704c");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);

        String response = null;
        DataOutputStream wr;
        try {
            wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.contains("\"key\"")) {
            response = response.substring(response.indexOf(":") + 2, response.length() - 2);

            String postURL = raw ? "https://hastebin.com/raw" : "https://hastebin.com";
            response = postURL + response;
        }

        return response;
    }

    public static void sendLog(Player sender, String factionId, LogType logType, boolean raw) {
        long msCooldown = 300000;
        if (clickCooldown.containsKey(factionId) && !sender.isOp()) {
            if (clickCooldown.get(factionId) + msCooldown > System.currentTimeMillis()) {
                sender.sendMessage(CC.translate("&6&lFactions &8⎜ &cError&8: &eYour faction recently generated an f log URL, please wait another &7" + TimeDateUtils.fancy((clickCooldown.get(factionId) + msCooldown) - System.currentTimeMillis())));
                return;
            }
        }
        clickCooldown.put(factionId, System.currentTimeMillis());
        sender.sendMessage(CC.translate("&6&lFactions &8⎜ &eGenerating " + logType.getDisplayName() + " faction logs..."));

        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("America/New_York"));
        String date = localDateTime.format(DATE_FORMATED);
        StringBuilder text = new StringBuilder();
        text.append(logType.getDisplayName()).append(" Generated on ").append(date).append(" (EST)").append(" for faction ").append(FactionUtils.getFactionById(factionId).getTag()).append("\n");
        List<FactionLog> factionLogsList = FactionsLogsAPI.getFactionLogs(factionId, logType);
        for (FactionLog factionLog : factionLogsList) {
            text.append(" ").append(factionLog.toString()).append("\n");
        }


        try {
            String url = post(text.toString(), raw);
            sender.sendMessage(CC.translate("&6&lFactions &8⎜ &e" + logType.getDisplayName() + " &7(Logs URL)&e: &a&n" + url));
        } catch (IOException e) {
            sender.sendMessage(CC.translate("&6&lFactions &8⎜ &cError&8: &eAn error occurred, please try again!"));
            e.printStackTrace();
        }
    }

}