package net.astropvp.core.titles.utils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.JSONUtils;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Clickable {
    private final List<TextComponent> components = new ArrayList<>();
    private static final Class<?> CraftItemStackClass = NMSUtil.getCraftClass("inventory.CraftItemStack");
    private static final Class<?> ItemStackClass = NMSUtil.getNMSClass("ItemStack");
    private static final Class<?> NBTTagCompoundClass = NMSUtil.getNMSClass("NBTTagCompound");

    public Clickable(String msg) {
        TextComponent message = new TextComponent(msg);
        this.components.add(message);
    }

    public Clickable(String msg, String hoverMsg) {
        this(msg, hoverMsg, "");
    }

    public Clickable(String msg, String hoverMsg, String clickString) {
        add(msg, hoverMsg, clickString);
    }

    public TextComponent add(String msg, String hoverMsg) {
        return add(msg, hoverMsg, "");
    }

    public TextComponent add(String msg, String hoverMsg, String clickString) {
        TextComponent message = new TextComponent(CC.translate(msg));
        if (hoverMsg != null && !hoverMsg.isEmpty())
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(CC.translate(hoverMsg)).create())));
        if (clickString != null && !clickString.isEmpty())
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickString));
        this.components.add(message);
        return message;
    }

    public void addItem(String msg, ItemStack itemStack) {
        TextComponent message = new TextComponent(CC.translate((msg)));
        if (itemStack != null && itemStack.getType() != Material.AIR)
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(convertItemStackToJson(itemStack))}));
        this.components.add(message);
    }

    public void add(String message) {
        this.components.add(new TextComponent(CC.translate(message)));
    }

    public void sendToPlayer(Player player) {
        player.spigot().sendMessage(this.asComponents());
    }

    public TextComponent[] asComponents() {
        return this.components.toArray(new TextComponent[0]);
    }

    public static String convertItemStackToJson(ItemStack itemStack){
        try {
            assert CraftItemStackClass != null;
            Method asNMSCopyMethod = CraftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
            Method saveMethod = ItemStackClass.getMethod("save", NBTTagCompoundClass);
            assert NBTTagCompoundClass != null;
            Object NBTTagCompound = NBTTagCompoundClass.newInstance();
            Object NMSItemStack = asNMSCopyMethod.invoke(null, itemStack);
            Object Json = saveMethod.invoke(NMSItemStack, NBTTagCompound);
            return Json.toString();
        } catch (IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e){
            e.printStackTrace();
            return null;
        }
    }
}