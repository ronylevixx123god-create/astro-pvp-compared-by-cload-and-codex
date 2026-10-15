package net.astropvp.giftcardshop.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.SneakyThrows;
import net.astropvp.giftcardshop.GiftcardShopPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

public class ResetGiftcards implements CommandExecutor {

    @SneakyThrows
    public void voidGiftcard(String giftcardID) {
        System.out.println("VOIDING: " + giftcardID);
        URL urlObj = new URL("https://plugin.tebex.io/gift-cards/" + giftcardID);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)urlObj.openConnection();
        httpsURLConnection.setRequestProperty("X-Buycraft-Secret", "37fafff7553327e8b9e5b7502b5a9de604da817f");
        httpsURLConnection.setRequestProperty("Content-Type", "text/plain");
        httpsURLConnection.setRequestProperty("Accept", "application/json");
        httpsURLConnection.setRequestMethod("DELETE");
        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.connect();

        System.out.println("Status Code Void: "+ httpsURLConnection.getResponseCode());
    }

    @SneakyThrows
    public void voidAllGiftcardsOver10() {
        URL urlObj = new URL("https://plugin.tebex.io/gift-cards");
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)urlObj.openConnection();
        httpsURLConnection.setRequestProperty("X-Buycraft-Secret", "37fafff7553327e8b9e5b7502b5a9de604da817f");
        httpsURLConnection.setRequestProperty("Content-Type", "text/plain");
        httpsURLConnection.setRequestProperty("Accept", "application/json");
        httpsURLConnection.setRequestMethod("GET");
        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.connect();

        System.out.println("Status Code Get: " + httpsURLConnection.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            String finalInputLine = inputLine;
            Bukkit.getScheduler().runTaskLaterAsynchronously(GiftcardShopPlugin.getInstance(), () -> {

                for(String string : finalInputLine.split("null")) {
                System.out.println("FULL LINE: " + string);
                System.out.println(":");
                String id = hasOver10GetID(string);
                if(!id.equals("no")) {
                    System.out.println("Not over 10: " + string);
                        voidGiftcard(id);

                    }
                }
            }, 4*20);
        }
    }

    private String hasOver10GetID(String inputLine) {
        String getID = "Error";
        String getAmount = "1";
        String getMonth = "1";


        getID = inputLine;
        getID = getID.split("\"id\":")[1];
        getID = getID.split(",")[0].replace("\"", "");
        getID = Arrays.toString(getID.split("(?<=\\G.{4})"));
        getID = getID.replace("[", "").replace("]", "").replace(", ", "");

        getAmount = inputLine;
        getAmount = getAmount.split("\"starting\":")[1];
        getAmount = getAmount.split(",")[0].replace("\"", "");
        getAmount = Arrays.toString(getAmount.split("(?<=\\G.{4})"));
        getAmount = getAmount.replace("[", "").replace("]", "").replace(", ", "");

        getMonth = inputLine;
        getMonth = getMonth.split("\"created_at\":")[1];
        getMonth = getMonth.split(",")[0].replace("\"", "");
        getMonth = Arrays.toString(getMonth.split("(?<=\\G.{4})"));
        getMonth = getMonth.replace("[", "").replace("]", "").replace(", ", "");

        System.out.println("Amount: " + getAmount);
        System.out.println("Float: " + Float.parseFloat(getAmount));
        if(Float.parseFloat(getAmount) >= 0.01) {
            System.out.println("Returns: " + getID);
            return getID;
        }
        return "no";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!sender.isOp()) {
            sender.sendMessage(CC.Red + "No perms!");
            return false;
        }
        if (!sender.getName().equals("KingSavaqe")) return false;
        voidAllGiftcardsOver10();
        sender.sendMessage(CC.Red + "Reset all gc's over 0.01");
        return true;
    }
}
