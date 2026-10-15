package net.astropvp.astroevents.utils;


import net.md_5.bungee.api.chat.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class Clickable {
    private final List<TextComponent> components = new ArrayList<>();

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
        TextComponent message = new TextComponent(Utils.color(msg));
        if (hoverMsg != null && !hoverMsg.isEmpty())
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(Utils.color(hoverMsg))).create()));
        if (clickString != null && !clickString.isEmpty())
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickString));
        this.components.add(message);
        return message;
    }

    public void add(String message) {
        this.components.add(new TextComponent(Utils.color(message)));
    }

    public void sendToPlayer(Player player) {
        player.spigot().sendMessage(this.asComponents());
    }

    public TextComponent[] asComponents() {
        return this.components.toArray(new TextComponent[0]);
    }
}
