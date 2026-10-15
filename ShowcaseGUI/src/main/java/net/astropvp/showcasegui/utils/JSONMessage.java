package net.astropvp.showcasegui.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
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
import java.util.Iterator;
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
        (this.json = new JsonObject()).add("extra", new JsonArray());
    }

    private JsonArray getExtra() {
        if (!this.json.has("extra")) {
            this.json.add("extra", new JsonArray());
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
        o.add("clickEvent", u);
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
        o.add("hoverEvent", a);
        this.getExtra().add((JsonElement)o);
    }

    public void addItem(ItemStack item, String text, ChatColor color) {
        if (item != null) {
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
                }

                if (m.getDisplayName() != null) {
                    v.addProperty("Name", m.getDisplayName());
                }

                if (m.getLore() != null || item.getEnchantments() != null) {
                    v.addProperty("Lore", "%LORE%");
                }

                x.add("display", v);
                i.add("tag", x);
            }

            String is = i.toString();
            is = is.replace("\"", "");
            if (is.contains("%LORE%")) {
                List<String> lor = new ArrayList();
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
            o.add("hoverEvent", a);
            this.getExtra().add((JsonElement)o);
        }
    }

    public List<String> getEnchantsAsList(Map<Enchantment, Integer> map) {
        List<String> lore = new ArrayList();
        Iterator var3 = map.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<Enchantment, Integer> entry = (Map.Entry)var3.next();
            lore.add(ChatColor.GRAY + StringUtils.capitalize(this.getDisplayEnchantName(((Enchantment)entry.getKey()).getName())) + " " + this.getNumber((Integer)entry.getValue()));
        }

        return lore;
    }

    public String getDisplayEnchantName(String name) {
        String lowerCase = name.toLowerCase();
        byte var4 = -1;
        switch(lowerCase.hashCode()) {
            case -1774590767:
                if (lowerCase.equals("damage_all")) {
                    var4 = 13;
                }
                break;
            case -1729996628:
                if (lowerCase.equals("arrow_fire")) {
                    var4 = 9;
                }
                break;
            case -1267836636:
                if (lowerCase.equals("protection_explosions")) {
                    var4 = 1;
                }
                break;
            case -1002602080:
                if (lowerCase.equals("oxygen")) {
                    var4 = 17;
                }
                break;
            case -874519716:
                if (lowerCase.equals("thorns")) {
                    var4 = 19;
                }
                break;
            case -720514431:
                if (lowerCase.equals("fire_aspect")) {
                    var4 = 4;
                }
                break;
            case -698289393:
                if (lowerCase.equals("protection_projectile")) {
                    var4 = 16;
                }
                break;
            case -601698851:
                if (lowerCase.equals("loot_bonus_blocks")) {
                    var4 = 10;
                }
                break;
            case -476384346:
                if (lowerCase.equals("water_worker")) {
                    var4 = 18;
                }
                break;
            case -439211931:
                if (lowerCase.equals("arrow_damage")) {
                    var4 = 8;
                }
                break;
            case 24678400:
                if (lowerCase.equals("damage_arthropods")) {
                    var4 = 15;
                }
                break;
            case 493198669:
                if (lowerCase.equals("damage_undead")) {
                    var4 = 14;
                }
                break;
            case 620514517:
                if (lowerCase.equals("silk_touch")) {
                    var4 = 11;
                }
                break;
            case 716086281:
                if (lowerCase.equals("durability")) {
                    var4 = 5;
                }
                break;
            case 976288699:
                if (lowerCase.equals("knockback")) {
                    var4 = 12;
                }
                break;
            case 1703059850:
                if (lowerCase.equals("arrow_infinite")) {
                    var4 = 7;
                }
                break;
            case 1791328920:
                if (lowerCase.equals("protection_environmental")) {
                    var4 = 0;
                }
                break;
            case 2002648650:
                if (lowerCase.equals("dig_speed")) {
                    var4 = 6;
                }
                break;
            case 2114147617:
                if (lowerCase.equals("protection_fall")) {
                    var4 = 2;
                }
                break;
            case 2114155484:
                if (lowerCase.equals("protection_fire")) {
                    var4 = 3;
                }
        }

        switch(var4) {
            case 0:
                return "Protection";
            case 1:
                return "Blast Protection";
            case 2:
                return "Feathing Falling";
            case 3:
                return "Fire Protection";
            case 4:
                return "Fire Aspect";
            case 5:
                return "Unbreaking";
            case 6:
                return "Efficiency";
            case 7:
                return "Infinity";
            case 8:
                return "Power";
            case 9:
                return "Flame";
            case 10:
                return "Fortune";
            case 11:
                return "Silk Touch";
            case 12:
                return "Knockback";
            case 13:
                return "Sharpness";
            case 14:
                return "Smite";
            case 15:
                return "Bane of Arthropods";
            case 16:
                return "Projectile Protection";
            case 17:
                return "Resperation";
            case 18:
                return "Aqua Affinity";
            case 19:
                return "Thorns";
            default:
                return "";
        }
    }

    public String getNumber(int i) {
        if (i > 10) {
            return i + "";
        } else if (i == 1) {
            return "I";
        } else if (i == 2) {
            return "II";
        } else if (i == 3) {
            return "III";
        } else if (i == 4) {
            return "IV";
        } else if (i == 5) {
            return "V";
        } else if (i == 6) {
            return "VI";
        } else if (i == 7) {
            return "VII";
        } else if (i == 8) {
            return "VIII";
        } else if (i == 9) {
            return "IX";
        } else {
            return i == 10 ? "X" : i + "";
        }
    }

    public void addSuggestCommand(String text, ChatColor color, String cmd) {
        JsonObject o = new JsonObject();
        o.addProperty("text", text);
        o.addProperty("color", color.name().toLowerCase());
        JsonObject u = new JsonObject();
        u.addProperty("action", "suggest_command");
        u.addProperty("value", cmd);
        o.add("clickEvent", u);
        this.getExtra().add((JsonElement)o);
    }

    public void addRunCommand(String text, ChatColor color, String cmd, String hoverData) {
        JsonObject o = new JsonObject();
        o.addProperty("text", text);
        o.addProperty("color", color.name().toLowerCase());
        JsonObject u = new JsonObject();
        u.addProperty("action", "run_command");
        u.addProperty("value", cmd);
        o.add("clickEvent", u);
        JsonObject a = new JsonObject();
        a.addProperty("action", "show_text");
        a.addProperty("value", hoverData);
        o.add("hoverEvent", a);
        this.getExtra().add((JsonElement)o);
    }

    public String toString() {
        return this.json.toString();
    }

    public void sendToPlayer(Player p) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(this.json.toString())));
    }

    public void setColor(ChatColor color) {
        this.json.addProperty("color", color.name().toLowerCase());
    }

    public void setText(String text) {
        this.json.addProperty("text", text);
    }
}
