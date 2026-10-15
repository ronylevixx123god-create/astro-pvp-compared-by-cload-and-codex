package com.massivecraft.factions.gui;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.material.MaterialDb;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

public class PermissibleRelationGUI extends GUI<Permissible> {
    private static final Map<Permissible, SimpleItem> items;
    public static SimpleItem offlineSwitch = SimpleItem.builder().setName(TL.GUI_PERMS_TOGGLE.toString()).setMaterial(MaterialDb.get("LEVER")).build();

    static {
        items = new LinkedHashMap<>();
        SimpleItem.Builder starter = SimpleItem.builder().setName("&8[{relation-color}{relation}&8]");

        SimpleItem recruit = starter.build();
        recruit.setName("§6" + StringUtils.capitalize(Role.RECRUIT.getTranslation().toString() + " Permissions"));
        recruit.setMaterial(MaterialDb.get("WOODEN_SWORD"));
        items.put(Role.RECRUIT, recruit);

        SimpleItem normal = starter.build();
        normal.setName("§6" + StringUtils.capitalize(Role.NORMAL.getTranslation().toString() + " Permissions"));
        normal.setMaterial(MaterialDb.get("STONE_SWORD"));
        items.put(Role.NORMAL, normal);

        SimpleItem moderator = starter.build();
        moderator.setName("§6" + StringUtils.capitalize(Role.MODERATOR.getTranslation().toString() + " Permissions"));
        moderator.setMaterial(MaterialDb.get("IRON_SWORD"));
        items.put(Role.MODERATOR, moderator);

        SimpleItem coleader = starter.build();
        coleader.setName("§6" + StringUtils.capitalize(Role.COLEADER.getTranslation().toString() + " Permissions"));
        coleader.setMaterial(MaterialDb.get("DIAMOND_SWORD"));
        items.put(Role.COLEADER, coleader);

        SimpleItem ally = starter.build();
        ally.setName("§6" + StringUtils.capitalize(Relation.ALLY.getTranslation() + " Permissions"));
        ally.setMaterial(MaterialDb.get("GOLD_SWORD"));
        items.put(Relation.ALLY, ally);

        SimpleItem truce = starter.build();
        truce.setName("§6" + StringUtils.capitalize(Relation.TRUCE.getTranslation() + " Permissions"));
        truce.setMaterial(MaterialDb.get("IRON_AXE"));
        items.put(Relation.TRUCE, truce);

        SimpleItem neutral = starter.build();
        neutral.setName("§6" + StringUtils.capitalize(Relation.NEUTRAL.getTranslation() + " Permissions"));
        neutral.setMaterial(MaterialDb.get("STONE_HOE"));
        items.put(Relation.NEUTRAL, neutral);

        SimpleItem enemy = starter.build();
        enemy.setName("§6" + StringUtils.capitalize(Relation.ENEMY.getTranslation() + " Permissions"));
        enemy.setMaterial(MaterialDb.get("STONE_AXE"));
        items.put(Relation.ENEMY, enemy);
    }

    private final boolean online;

    public PermissibleRelationGUI(boolean online, FPlayer user) {
        super(user, FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms() ? 2 : 1);
        this.online = online;
        build();
    }

    @Override
    protected String getName() {
        String bit = FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms() ?
                TL.GUI_PERMS_RELATION_ONLINEOFFLINEBIT.format(online ? TL.GUI_PERMS_ONLINE.toString() : TL.GUI_PERMS_OFFLINE)
                : "";
        return TL.GUI_PERMS_RELATION_NAME.format(bit);
    }

    @Override
    protected String parse(String toParse, Permissible permissible) {
        // Uppercase the first letter
        String name = permissible.toString().substring(0, 1).toUpperCase() + permissible.toString().substring(1);

        toParse = toParse.replace("{relation-color}", permissible.getColor().toString());
        toParse = toParse.replace("{relation}", name);
        return toParse;
    }

    @Override
    public void click(int slot, ClickType clickType) {
        if (FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms() && slot == 13) {
            new PermissibleRelationGUI(!online, user).open();
        } else {
            super.click(slot, clickType);
        }
    }

    @Override
    protected void onClick(Permissible permissible, ClickType clickType) {
        new PermissibleActionGUI(online, user, permissible).open();
    }

    @Override
    protected Map<Integer, Permissible> createSlotMap() {
        Map<Integer, Permissible> map = new HashMap<>();
        if (online) {
            map.put(0, Role.RECRUIT);
            map.put(1, Role.NORMAL);
            map.put(2, Role.MODERATOR);
            map.put(3, Role.COLEADER);
        }
        map.put(5, Relation.ALLY);
        map.put(6, Relation.NEUTRAL);
        map.put(7, Relation.TRUCE);
        map.put(8, Relation.ENEMY);
        return map;
    }

    @Override
    protected SimpleItem getItem(Permissible permissible) {
        return items.get(permissible);
    }

    @Override
    protected Map<Integer, SimpleItem> createDummyItems() {
        return FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms() ? Collections.singletonMap(13, offlineSwitch) : Collections.emptyMap();
    }
}
