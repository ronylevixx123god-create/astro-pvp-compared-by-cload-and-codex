/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.utils.NBTWrapper
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.bukkit.Material
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.javatuples.Pair
 */
package net.astropvp.lootboxes.manager;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.astropvp.lootboxes.AstroLootboxes;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.api.PluginLootboxManager;
import net.astropvp.lootboxes.pojo.PluginInformation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ForeignLootboxManager
        implements Listener {
    private final AstroLootboxes plugin;
    private final LootboxManager lootboxManager;
    private final Map<String, Class<? extends JavaPlugin>> pluginClassMap;
    private final Map<Class<? extends JavaPlugin>, PluginLootboxManager> pluginLootboxManagers;
    private final Map<Class<? extends JavaPlugin>, PluginInformation> pluginDescriptorMap;

    public ForeignLootboxManager(AstroLootboxes plugin, LootboxManager lootboxManager) {
        this.plugin = plugin;
        this.lootboxManager = lootboxManager;
        this.pluginClassMap = Maps.newHashMap();
        this.pluginLootboxManagers = Maps.newConcurrentMap();
        this.pluginDescriptorMap = Maps.newHashMap();
    }

    public void registerManager(JavaPlugin plugin, PluginLootboxManager lootboxManager) {
        this.pluginClassMap.put(plugin.getName(), plugin.getClass());
        this.pluginLootboxManagers.put(plugin.getClass(), lootboxManager);
        this.pluginDescriptorMap.put(plugin.getClass(), new PluginInformation(plugin));
    }

    public void deregisterManager(JavaPlugin plugin) {
        this.pluginLootboxManagers.remove(plugin.getClass());
        this.pluginDescriptorMap.remove(plugin.getClass());
        this.pluginClassMap.remove(plugin.getName());
    }

    public List<PluginLootboxManager> getPluginManagers() {
        return this.pluginDescriptorMap.values().stream().sorted(Comparator.comparing(PluginInformation::getPluginName)).map(PluginInformation::getPluginClass).map(this.pluginLootboxManagers::get).collect(Collectors.toList());
    }

    public List<Pair<PluginInformation, PluginLootboxManager>> getDescribedPluginManagers() {
        return this.getPluginManagers().stream().map(lootboxManager -> Pair.with(this.pluginDescriptorMap.get(lootboxManager.getManagingPlugin().getClass()), lootboxManager)).collect(Collectors.toList());
    }

    private List<LootboxManager> getAllManagers() {
        ArrayList managers = Lists.newArrayList(this.lootboxManager);
        managers.addAll(this.getPluginManagers());
        return managers;
    }

    public Optional<Lootbox> getLootboxFromItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return Optional.empty();
        }
        NBTWrapper wrapper = new NBTWrapper(itemStack);
        for (LootboxManager lootboxManager : this.getAllManagers()) {
            boolean is = wrapper.getCompoundOrCreate(lootboxManager.getNBTCompound()).getBoolean(lootboxManager.getNBTIsLootbox());
            if (!is) continue;
            String type = wrapper.getCompoundOrCreate(lootboxManager.getNBTCompound()).getString(lootboxManager.getNBTLootboxType());
            return lootboxManager.getLootbox(type);
        }
        return Optional.empty();
    }
}

