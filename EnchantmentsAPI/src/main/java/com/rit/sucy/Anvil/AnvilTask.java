/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.rit.sucy.Anvil;

import com.rit.sucy.Anvil.AnvilMechanics;
import com.rit.sucy.Anvil.AnvilView;
import com.rit.sucy.Anvil.MainAnvil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AnvilTask
extends BukkitRunnable {
    private ItemStack[] contents;
    private AnvilView anvil;

    public AnvilTask(Plugin plugin, AnvilView view) {
        this.anvil = view;
        this.contents = view.getInputSlots();
        this.runTaskTimer(plugin, 2L, 2L);
    }

    public AnvilView getView() {
        return this.anvil;
    }

    public void run() {
        ItemStack[] input = this.anvil.getInputSlots();
        if (input[0] != this.contents[0] || input[1] != this.contents[1]) {
            if (this.anvil instanceof MainAnvil) {
                AnvilMechanics.updateResult(this.anvil, input, ((MainAnvil)this.anvil).getNameText());
            } else {
                AnvilMechanics.updateResult(this.anvil, input);
            }
            this.contents = input;
        }
    }
}

