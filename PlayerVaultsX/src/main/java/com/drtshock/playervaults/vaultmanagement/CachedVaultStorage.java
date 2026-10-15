package com.drtshock.playervaults.vaultmanagement;

import com.drtshock.playervaults.PlayerVaults;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Used to store vaults temporarily after they've been opened.
 */
public class CachedVaultStorage {
    private static final long TIME_UNTIL_EXPIRY = 30000L;

    private final Map<UUID, VaultStorage> vaultCache;

    private BukkitTask expiryTask;

    public CachedVaultStorage()
    {
        this.vaultCache = Maps.newHashMap();

        this.runExpiryTask();
    }

    public void shutdown()
    {
        expiryTask.cancel();
    }

    public synchronized void cacheVault(UUID owner, int vaultNum, Inventory vault)
    {
        VaultStorage vaultStorage = this.vaultCache.computeIfAbsent(owner, (key) -> new VaultStorage(owner));

        vaultStorage.getVaultStorage().put(vaultNum, vault);

        vaultStorage.updateLastActiveTime();
    }

    public synchronized Optional<Inventory> getCachedVault(UUID owner, int vaultNum)
    {
        if(!vaultCache.containsKey(owner))
            return Optional.empty();

        VaultStorage storage = vaultCache.get(owner);

        if(!storage.getVaultStorage().containsKey(vaultNum))
            return Optional.empty();

        return Optional.of(storage.getVaultStorage().get(vaultNum));
    }

    private void runExpiryTask()
    {
        //This is initialized outside of the async method to CPU time.
        List<UUID> toRemoveList = Lists.newArrayList();

        expiryTask = new BukkitRunnable(){
            @Override
            public void run()
            {
                if(vaultCache.isEmpty())
                    return;

                long timeNow = System.currentTimeMillis();

                synchronized (CachedVaultStorage.this)
                {
                    vaultCache.forEach((key, value) -> {
                        if(timeNow - value.getLastActiveTime() > TIME_UNTIL_EXPIRY)
                            toRemoveList.add(key);
                    });

                    toRemoveList.forEach(vaultCache::remove);
                }

                toRemoveList.clear();
            }
        }.runTaskTimerAsynchronously(PlayerVaults.getInstance(), 0, 300);
    }

    private static class VaultStorage {
        private final UUID owner;

        private final Map<Integer, Inventory> vaultStorage;

        private long lastActiveTime;

        public VaultStorage(UUID player)
        {
            this.owner = player;

            this.vaultStorage = Maps.newHashMap();

            this.lastActiveTime = System.currentTimeMillis();
        }

        public UUID getOwner() {
            return owner;
        }

        public Map<Integer, Inventory> getVaultStorage() {
            return vaultStorage;
        }

        public long getLastActiveTime() {
            return lastActiveTime;
        }

        public void updateLastActiveTime() {
            this.lastActiveTime = System.currentTimeMillis();
        }
    }
}
