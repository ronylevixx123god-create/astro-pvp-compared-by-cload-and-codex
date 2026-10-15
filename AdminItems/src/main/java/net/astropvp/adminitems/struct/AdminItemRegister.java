package net.astropvp.adminitems.struct;

import lombok.Getter;
import net.astropvp.adminitems.AdminItemsPlugin;
import net.astropvp.adminitems.items.GrappleHook;
import net.astropvp.adminitems.items.SwitcherBall;
import net.astropvp.adminitems.items.TimeTraveler;
import net.astropvp.adminitems.items.TrapBlocker;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import java.util.*;

public class AdminItemRegister {

    @Getter
    private static AdminItemRegister instance;
    private final Map<String, AdminItem> adminItems;


    public AdminItemRegister() {
        instance = this;
        this.adminItems = new HashMap<>();
    }

    public void initialize() {
        this.registerAll(
            new TrapBlocker(),
                new TimeTraveler(),
                new GrappleHook(),
                new SwitcherBall()
        );
    }

    public void registerAll(AdminItem... Adminitems) {
        for (AdminItem adminItem : Adminitems) {
            this.register(adminItem);
        }
    }

    public void register(AdminItem adminItem) {
        try {
            this.adminItems.put(adminItem.getID(), adminItem);
            AdminItemsPlugin.getInstance().getServer().getPluginManager().registerEvents(adminItem, AdminItemsPlugin.getInstance());
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to register admin item " + adminItem.getID());
            System.out.println(e.getMessage());
        }
    }

    public void unregisterAll() {
        this.adminItems.clear();
    }

    public void unregister(AdminItem adminItem) {
        this.adminItems.remove(adminItem.getID());
    }

    public void reload() {
        this.unregisterAll();
        this.initialize();
    }

    public boolean isAdminItem(String name) {
        return (this.getAdminItemFromID(name) != null);
    }

    public AdminItem getAdminItemFromID(String id) {
        Validate.notNull(id);
        Validate.notEmpty(id);
        return this.adminItems.get(id.toLowerCase());
    }

    public AdminItem getAdminItemFromName(String name) {
        Validate.notNull(name);
        Validate.notEmpty(name);
        name = this.getAdminItemIDFromName(name);
        return this.adminItems.get(name.toLowerCase());
    }

    public String getAdminItemIDFromName(String name) {
        return name.replace(" ", "").toLowerCase(Locale.ROOT);
    }

    public Collection<AdminItem> getAdminItems() {
        return Collections.unmodifiableCollection(this.adminItems.values());
    }

}
