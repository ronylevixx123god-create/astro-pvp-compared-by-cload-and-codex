package net.crankedpvp.hypeboxes.struct;

import com.cosmicpvp.cosmicutils.utils.StringUtils;
import de.tr7zw.nbtapi.NBTItem;
import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import net.crankedpvp.hypeboxes.language.Config;
import net.crankedpvp.hypeboxes.language.Language;
import net.crankedpvp.hypeboxes.utils.InventoryUtils;
import net.crankedpvp.hypeboxes.utils.ItemStackSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class HypeBoxManager {

    private final HypeboxesPlugin plugin;
    public HypeBoxManager(HypeboxesPlugin plugin) {
        this.plugin = plugin;
    }

    public static boolean isHypebox(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return false;
        NBTItem nbtItem = new NBTItem(itemStack);
        if(nbtItem.getCompound("cosmicData") == null) return false;
        if(!nbtItem.getCompound("cosmicData").hasKey("customItem")) return false;
        return nbtItem.getCompound("cosmicData").hasKey("hypeboxID");
    }

    public static HypeBox getHypeBoxFromItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;
        NBTItem nbtItem = new NBTItem(itemStack);
        if(nbtItem.getCompound("cosmicData") == null) return null;
        if(!nbtItem.getCompound("cosmicData").hasKey("customItem")) return null;
        return HypeboxesPlugin.getInstance().getHypeBoxRegister().getHypebox(Objects.requireNonNull(nbtItem.getCompound("cosmicData")).getString("hypeboxID"));
    }

    public static boolean isHypeboxHalfGenerator(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return false;
        NBTItem nbtItem = new NBTItem(itemStack);
        return (nbtItem.hasKey("leftOrRightHalf"));
    }

    public static boolean isHypeboxHalf(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return false;
        NBTItem nbtItem = new NBTItem(itemStack);
        if(!nbtItem.hasKey("halfLootbox")) return false;
        return nbtItem.hasKey("side");
    }

    public static boolean doesHypeboxBroadcast(ItemStack itemStack) {
        if(!isHypebox(itemStack)) return false;
        if (itemStack.getType() == Material.AIR) return false;
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.getBoolean("broadcast");
    }

    public void createDefaultConfig(String name) {
        File file = new File(this.plugin.getDataFolder(), "Hypeboxes/" + name + ".yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        String[] colors = new String[]{"&a", "&b", "&c", "&d", "&e", "&2", "&3", "&4", "&5", "&6", "&9"};
        String color = colors[ThreadLocalRandom.current().nextInt(1, 10)];
        yamlConfiguration.set("Item.Display.Name", "&f&ki&r " + color + "&l" + StringUtils.toNiceString(name) + " &f&ki&r &f&lHypebox");
        yamlConfiguration.set("Item.Display.Lore", Config.HYPEBOX_LORE.getValuesAsList());
        yamlConfiguration.set("Item.Material", "" + Config.HYPEBOX_MATERIAL.getValue());
        yamlConfiguration.set("Item.Glowing", true);
        yamlConfiguration.set("Misc.Broadcast.Rewards", true);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDefaultConfigFromItemStack(ItemStack itemStack, String name) {
        File file = new File(this.plugin.getDataFolder(), "Hypeboxes/" + name + ".yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("Item.Display.Name", itemMeta.getDisplayName());
        yamlConfiguration.set("Item.Display.Lore", itemMeta.getLore());
        yamlConfiguration.set("Item.Material", "" + itemStack.getType().name());
        yamlConfiguration.set("Item.Glowing", itemMeta.hasEnchants());
        yamlConfiguration.set("Misc.Broadcast.Rewards", true);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createHypebox(String lootbox) {
        createDefaultConfig(lootbox);
        this.initialize(lootbox);
    }

    public void createHypeboxFromItemStack(ItemStack itemStack, String lootbox) {
        createDefaultConfigFromItemStack(itemStack, lootbox);
        this.initialize(lootbox);
    }



    public void deleteConfig(String name) {
        File file = new File(this.plugin.getDataFolder(), "Hypeboxes/" + name + ".yml");
        if (file.exists())
            file.delete();
    }

    public void reloadHypebox(String hypebox) {
        if (this.plugin.getHypeBoxRegister().isHypebox(hypebox)) {
            this.plugin.getHypeBoxRegister().removeHypebox(hypebox);
            initialize(hypebox);
        } else {
            System.out.println("[-] Failure reloading hypebox, " + hypebox);
        }
    }

    public void initializeAll() {
        File dataFolder = new File(this.plugin.getDataFolder(), "Hypeboxes");
        Arrays.stream(Objects.requireNonNull(dataFolder.listFiles()))
                .filter(file -> !file.isDirectory())
                .filter(file -> !file.getName().startsWith("-"))
                .forEach(file -> {
                    try {
                        initialize(file.getName().replace(".yml", ""));
                    } catch (NullPointerException exception) {
                        System.out.println("[CrankedLootboxes] Failed to register hypebox: " + file.getName().replace(".yml", ""));
                    }
                });
    }

    public void initialize(String fileName) {
        File file = new File(this.plugin.getDataFolder(), "Hypeboxes/" + fileName + ".yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try {
            yamlConfiguration.load(file);
        } catch (IOException |org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
        String name = yamlConfiguration.getString("Item.Display.Name");
        List<String> lore = yamlConfiguration.getStringList("Item.Display.Lore");
        String mater = yamlConfiguration.getString("Item.Material");
        Material material = mater.contains("SKULL") ? Material.SKULL_ITEM : Material.valueOf(mater.toUpperCase());
        String skullValue = mater.contains(":") ? mater.split(":")[1] : "???";
        boolean glowing = yamlConfiguration.getBoolean("Item.Glowing");
        boolean broadcastRewards = yamlConfiguration.contains("Misc.Broadcast.Rewards") && yamlConfiguration.getBoolean("Misc.Broadcast.Rewards");
        List<HypeBoxReward> rewards = getRewards(yamlConfiguration);
        HypeBox lootbox = new HypeBox(file.getName().replace(".yml", ""), name, lore, material, glowing, broadcastRewards, skullValue, rewards);
        this.plugin.getHypeBoxRegister().register(lootbox);
    }

    private List<HypeBoxReward> getRewards(FileConfiguration fileConfiguration) {
        List<HypeBoxReward> rewards = new ArrayList<>();
        if (fileConfiguration.getConfigurationSection("rewards") != null)
            fileConfiguration.getConfigurationSection("rewards").getKeys(false).forEach(key -> {
                ItemStack itemStack = null;
                try {
                    itemStack = ItemStackSerializer.deserialize(fileConfiguration.getConfigurationSection("rewards." + key).getString("item").replace("\r\n", ""));
                } catch (IOException|ClassNotFoundException e) {
                    e.printStackTrace();
                }
                HypeBoxReward reward = new HypeBoxReward(itemStack, fileConfiguration.getConfigurationSection("rewards." + key).getDouble("chance"), fileConfiguration.getConfigurationSection("rewards." + key).getDouble("value"));
                rewards.add(reward);
            });
        return rewards;
    }

    public void addItem(Player player, String lootbox, double chance, double value) {
        try {
            File file = new File(this.plugin.getDataFolder() + "/Hypeboxes/" + lootbox + ".yml");
            if(file.exists()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                String itemName = ChatColor.stripColor(player.getItemInHand().getItemMeta().getDisplayName());
                String toBase64 = ItemStackSerializer.serialize(player.getItemInHand());
                if(!yamlConfiguration.contains("rewards") || !yamlConfiguration.getConfigurationSection("rewards").getKeys(true).contains(itemName + player.getItemInHand().getAmount())) {
                    String name = itemName + player.getItemInHand().getAmount();
                    yamlConfiguration.set("rewards." + name + ".item", toBase64);
                    yamlConfiguration.set("rewards." + name + ".chance", chance);
                    yamlConfiguration.set("rewards." + name + ".value", value);
                    yamlConfiguration.save(file);
                    player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_ADDITEM.getMessage().replace("%lootbox%", lootbox).replace("%item%", player.getItemInHand().getItemMeta().getDisplayName()));
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0F, 2.0F);
                    reloadHypebox(lootbox);
                } else {
                    player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_ITEM_ALREADY_IN.getMessage().replace("%lootbox%", lootbox));
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeItemFromHypebox(Player player, ItemStack itemStack, String lootbox) {
        try {
            File file = new File(this.plugin.getDataFolder() + "/Hypeboxes/" + lootbox + ".yml");
            if (file.exists()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                String itemName = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
                if (!yamlConfiguration.contains("rewards")) {
                    player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_ITEM_NOT_FOUND.getMessage().replace("%lootbox%", lootbox));
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
                    return;
                }
                if (yamlConfiguration.getConfigurationSection("rewards").getKeys(true).contains(itemName + itemStack.getAmount())) {
                    yamlConfiguration.set("rewards." + itemName + itemStack.getAmount(), null);
                    yamlConfiguration.save(file);
                    player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_REMOVE_ITEM.getMessage().replace("%lootbox%", lootbox).replace("%item%", itemStack.getItemMeta().getDisplayName()));
                    player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 2.0F, 2.0F);
                    reloadHypebox(lootbox);
                } else {
                    player.sendMessage(Language.PREFIX.getMessage() + Language.HYPEBOX_ITEM_NOT_FOUND.getMessage().replace("%lootbox%", lootbox));
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void giveHypebox(Player player, HypeBox lootbox, int amount) {
        InventoryUtils.giveItemToPlayer(player, lootbox.createHypeBox(amount));
    }

    public void giveHypebox(Player player, HypeBox lootbox) {
        giveHypebox(player, lootbox, 1);
    }

    public ItemStack getHypebox(HypeBox lootbox) {
        return getHypebox(lootbox, 1);
    }

    public ItemStack getHypebox(HypeBox lootbox, int amount) {
        return lootbox.createHypeBox(amount);
    }

}
