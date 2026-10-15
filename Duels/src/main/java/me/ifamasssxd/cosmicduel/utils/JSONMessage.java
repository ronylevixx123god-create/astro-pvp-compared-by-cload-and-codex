/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.ChatSerializer
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutChat
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.craftbukkit.libs.com.google.gson.JsonArray
 *  org.bukkit.craftbukkit.libs.com.google.gson.JsonElement
 *  org.bukkit.craftbukkit.libs.com.google.gson.JsonObject
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.json.simple.JSONArray
 */
package me.ifamasssxd.cosmicduel.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONMessage {
    private JsonObject json;

    public JSONMessage() {
        this.initiateData();
    }

    public JSONMessage(String text) {
        this.initiateData();
        this.json.addProperty("text", text);
    }

    public JSONMessage(String text, ChatColor color) {
        this.initiateData();
        this.json.addProperty("text", text);
        this.json.addProperty("color", color.name().toLowerCase());
    }

    private void initiateData() {
        this.json = new JsonObject();
        this.json.add("extra", (JsonElement)new JsonArray());
    }

    private JsonArray getExtra() {
        if (!this.json.has("extra")) {
            this.json.add("extra", (JsonElement)new JsonArray());
        }
        return (JsonArray)this.json.get("extra");
    }

    public void addText(String text) {
        this.addText(text, ChatColor.WHITE);
    }

    public void addText(String text, ChatColor color, ChatColor second, Player p) {
        JsonObject data = new JsonObject();
        data.addProperty("text", text);
        data.addProperty("color", second.name().toLowerCase());
        data.addProperty("color", color.name().toLowerCase());
        this.getExtra().add((JsonElement)data);
    }

    public void addText(String text, ChatColor color) {
        JsonObject data = new JsonObject();
        data.addProperty("text", text);
        data.addProperty("color", color.name().toLowerCase());
        this.getExtra().add((JsonElement)data);
    }

    public void addText(String text, ChatColor color, Player p) {
        JsonObject data = new JsonObject();
        data.addProperty("text", text);
        data.addProperty("color", color.name().toLowerCase());
        this.getExtra().add((JsonElement)data);
    }

    public void addInsertionText(String text, ChatColor color, String insertion) {
        JsonObject o = new JsonObject();
        o.addProperty("text", text);
        o.addProperty("color", color.name().toLowerCase());
        o.addProperty("insertion", insertion);
        this.getExtra().add((JsonElement)o);
    }

    public void addURL(String text, ChatColor color, String url) {
        JsonObject o = new JsonObject();
        o.addProperty("text", text);
        o.addProperty("color", color.name().toLowerCase());
        JsonObject u = new JsonObject();
        u.addProperty("action", "open_url");
        u.addProperty("value", url);
        o.add("clickEvent", (JsonElement)u);
        this.getExtra().add((JsonElement)o);
    }

    public void addItem(ItemStack item, String text) {
        this.addItem(item, text, ChatColor.WHITE);
    }

    public void addHoverData(String text, String data) {
        JsonObject o = new JsonObject();
        o.addProperty("text", text);
        o.addProperty("color", ChatColor.RESET.name().toLowerCase());
        JsonObject a = new JsonObject();
        a.addProperty("action", "show_text");
        a.addProperty("value", data);
        o.add("hoverEvent", (JsonElement)a);
        this.getExtra().add((JsonElement)o);
    }

    public void addItem(ItemStack item, String text, ChatColor color) {
        if (item == null) {
            return;
        }
        JsonObject o = new JsonObject();
        o.addProperty("text", text);
        o.addProperty("color", color.name().toLowerCase());
        JsonObject a = new JsonObject();
        a.addProperty("action", "show_item");
        JsonObject i = new JsonObject();
        i.addProperty("id", (Number)item.getTypeId());
        i.addProperty("Damage", (Number)item.getDurability());
        if (item.getItemMeta() != null && (item.getItemMeta().getDisplayName() != null || item.getItemMeta().getLore() != null && item.getItemMeta().getLore().size() > 0) || item.getEnchantments() != null) {
            JsonObject x = new JsonObject();
            JsonObject v = new JsonObject();
            ItemMeta m = item.getItemMeta();
            if (m.hasEnchants()) {
                // empty if block
            }
            if (m.getDisplayName() != null) {
                v.addProperty("Name", m.getDisplayName());
            }
            if (m.getLore() != null || item.getEnchantments() != null) {
                v.addProperty("Lore", "%LORE%");
            }
            x.add("display", (JsonElement)v);
            i.add("tag", (JsonElement)x);
        }
        String is = i.toString();
        if ((is = is.replace("\"", "")).contains("%LORE%")) {
            ArrayList<String> lor = new ArrayList<String>();
            if (item.getItemMeta().hasEnchants()) {
                lor.addAll(this.getEnchantsAsList(item.getEnchantments()));
            }
            if (item.getItemMeta().getLore() != null) {
                lor.addAll(item.getItemMeta().getLore());
            }
            String lore = JSONArray.toJSONString(lor);
            lore = lore.replace(":", "|");
            lore = lore.replace("\\", "");
            is = is.replace("%LORE%", lore);
        }
        a.addProperty("value", is);
        o.add("hoverEvent", (JsonElement)a);
        this.getExtra().add((JsonElement)o);
    }

    public List<String> getEnchantsAsList(Map<Enchantment, Integer> map) {
        ArrayList<String> lore = new ArrayList<String>();
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            lore.add((Object)ChatColor.GRAY + StringUtils.capitalize((String)this.getDisplayEnchantName(entry.getKey().getName())) + " " + this.getNumber(entry.getValue()));
        }
        return lore;
    }

    public String getDisplayEnchantName(String name) {
        String lowerCase;
        switch (lowerCase = name.toLowerCase()) {
            case "protection_environmental": {
                return "Protection";
            }
            case "protection_explosions": {
                return "Blast Protection";
            }
            case "protection_fall": {
                return "Feathing Falling";
            }
            case "protection_fire": {
                return "Fire Protection";
            }
            case "fire_aspect": {
                return "Fire Aspect";
            }
            case "durability": {
                return "Unbreaking";
            }
            case "dig_speed": {
                return "Efficiency";
            }
            case "arrow_infinite": {
                return "Infinity";
            }
            case "arrow_damage": {
                return "Power";
            }
            case "arrow_fire": {
                return "Flame";
            }
            case "loot_bonus_blocks": {
                return "Fortune";
            }
            case "silk_touch": {
                return "Silk Touch";
            }
            case "knockback": {
                return "Knockback";
            }
            case "damage_all": {
                return "Sharpness";
            }
            case "damage_undead": {
                return "Smite";
            }
            case "damage_arthropods": {
                return "Bane of Arthropods";
            }
            case "protection_projectile": {
                return "Projectile Protection";
            }
            case "oxygen": {
                return "Resperation";
            }
            case "water_worker": {
                return "Aqua Affinity";
            }
            case "thorns": {
                return "Thorns";
            }
        }
        return "";
    }

    public String getNumber(int i) {
        if (i > 10) {
            return i + "";
        }
        if (i == 1) {
            return "I";
        }
        if (i == 2) {
            return "II";
        }
        if (i == 3) {
            return "III";
        }
        if (i == 4) {
            return "IV";
        }
        if (i == 5) {
            return "V";
        }
        if (i == 6) {
            return "VI";
        }
        if (i == 7) {
            return "VII";
        }
        if (i == 8) {
            return "VIII";
        }
        if (i == 9) {
            return "IX";
        }
        if (i == 10) {
            return "X";
        }
        return i + "";
    }

    public void addSuggestCommand(String text, ChatColor color, String cmd) {
        JsonObject o = new JsonObject();
        o.addProperty("text", text);
        o.addProperty("color", color.name().toLowerCase());
        JsonObject u = new JsonObject();
        u.addProperty("action", "suggest_command");
        u.addProperty("value", cmd);
        o.add("clickEvent", (JsonElement)u);
        this.getExtra().add((JsonElement)o);
    }

    public void addRunCommand(String text, ChatColor color, String cmd, String hoverData) {
        JsonObject o = new JsonObject();
        o.addProperty("text", text);
        o.addProperty("color", color.name().toLowerCase());
        JsonObject u = new JsonObject();
        u.addProperty("action", "run_command");
        u.addProperty("value", cmd);
        o.add("clickEvent", (JsonElement)u);
        JsonObject a = new JsonObject();
        a.addProperty("action", "show_text");
        a.addProperty("value", hoverData);
        o.add("hoverEvent", (JsonElement)a);
        this.getExtra().add((JsonElement)o);
    }

    public String toString() {
        return this.json.toString();
    }

    public void sendToPlayer(Player p) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a((String)this.json.toString())));
    }

    public void setColor(ChatColor color) {
        this.json.addProperty("color", color.name().toLowerCase());
    }

    public void setText(String text) {
        this.json.addProperty("text", text);
    }
}

