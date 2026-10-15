package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

final class ItemIDAutoSaveTask
        extends BukkitRunnable {
    ItemIDAutoSaveTask() {
    }

    public void run() {
        if (Arkkit.get().getConfig().getLong("settings.itemID.next_iid") != ItemID.next_iid) {
            Bukkit.getLogger().info("[Arkkit (ItemID)] Saving next_iid value of " + ItemID.next_iid);
            Arkkit.get().getConfig().set("settings.itemID.next_iid", ItemID.next_iid);
        }
    }
}