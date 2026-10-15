package me.ifamasssxd.cosmicbosses.struct;

import com.cosmicpvp.cosmicutils.utils.ItemSerialization;
import com.cosmicpvp.cosmicutils.utils.JSONUtils;
import com.sk89q.worldedit.internal.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RewardInventory {
    private static HashMap<UUID, RewardInventory> bossRewards;
    private static File bossRewardsFile;
    private static Type token;
    private List<ItemStack> items;
    private long expirationTime;
    private UUID uuid;

    public static void loadBossRewards(final JavaPlugin plugin) throws IOException {
        RewardInventory.bossRewardsFile = JSONUtils.getOrCreateFile(plugin.getDataFolder(), "pendingPlayerBossRewards.json");
        final Map<UUID, RewardInventorySave> maps = (Map<UUID, RewardInventorySave>) JSONUtils.fromJson(RewardInventory.bossRewardsFile, RewardInventory.token);
        if (maps != null) {
            maps.forEach((uuid, rewardSave) -> {
                if (rewardSave.getExpiration() > 0L && System.currentTimeMillis() >= rewardSave.getExpiration()) {
                    Bukkit.getLogger().info("Expiring me.astropvp.cratekeys.items for " + uuid.toString() + " Expiration: " + rewardSave.getExpiration());
                    return;
                } else {
                    RewardInventory inventory = new RewardInventory(uuid);
                    inventory.setExpirationTime(rewardSave.getExpiration());
                    List<ItemStack> items = (List<ItemStack>) ItemSerialization.itemListFromBase64(rewardSave.getRewardsString());
                    if (items != null) {
                        inventory.setItems(items);
                    }
                    if (inventory.getItems().size() > 0) {
                        RewardInventory.bossRewards.put(uuid, inventory);
                    }
                    return;
                }
            });
        }
        try {
            Class.forName("com.cosmicpvp.cosmicutils.utils.ItemSerialization");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveBossRewards() {
        try {
            final Map<UUID, RewardInventorySave> maps = new HashMap<UUID, RewardInventorySave>();
            RewardInventory.bossRewards.forEach((uuid, inv) -> maps.put(uuid, new RewardInventorySave(ItemSerialization.itemListToBase64((List) inv.getItems()), inv.getExpirationTime())));
            JSONUtils.saveJSONToFile(RewardInventory.bossRewardsFile, (Object) maps, (Object) RewardInventory.token);
            Bukkit.getLogger().info("[CosmicBosses] Saving " + maps.size() + " Boss Rewards");
        } catch (Exception e) {
            Bukkit.getLogger().info("Error saving boss rewards!");
            e.printStackTrace();
        }
    }

    public RewardInventory(final Player pl) {
        this(pl.getUniqueId());
    }

    public RewardInventory(final UUID uuid) {
        this.items = new ArrayList<ItemStack>();
        this.expirationTime = -1L;
        this.uuid = uuid;
    }

    public static RewardInventory getRewardInventory(final Player pl) {
        return RewardInventory.bossRewards.get(pl.getUniqueId());
    }

    public static RewardInventory removeRewardInventory(final Player pl) {
        return RewardInventory.bossRewards.remove(pl.getUniqueId());
    }

    public static void addReward(final Player player, final ItemStack clone) {
        final RewardInventory inventory = RewardInventory.bossRewards.computeIfAbsent(player.getUniqueId(), e -> new RewardInventory(player));
        final boolean expired = inventory.getExpirationTime() > 0L && inventory.getExpirationTime() < System.currentTimeMillis();
        if (expired) {
            Bukkit.getLogger().info("Expiring previous collect me.astropvp.cratekeys.items for " + player.getName() + "!");
            inventory.getItems().clear();
        }
        if (inventory.getExpirationTime() <= 0L || expired) {
            inventory.setExpirationTime(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24L));
        }
        inventory.getItems().add(clone);
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public void setItems(final List<ItemStack> items) {
        this.items = items;
    }

    public long getExpirationTime() {
        return this.expirationTime;
    }

    public void setExpirationTime(final long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    static {
        RewardInventory.bossRewards = new HashMap<UUID, RewardInventory>();
        RewardInventory.token = new TypeToken<HashMap<UUID, RewardInventorySave>>() {
        }.getType();
    }

    public static class RewardInventorySave {
        private String rewardsString;
        private long expiration;

        public RewardInventorySave(final String rewardsString, final long expiration) {
            this.rewardsString = rewardsString;
            this.expiration = expiration;
        }

        public String getRewardsString() {
            return this.rewardsString;
        }

        public long getExpiration() {
            return this.expiration;
        }
    }
}
