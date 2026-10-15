/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.rit.sucy.Anvil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface AnvilView {
    public ItemStack[] getInputSlots();

    public ItemStack[] getInputSlots(int var1, ItemStack var2);

    public int getInputSlotID(int var1);

    public void setResultSlot(ItemStack var1);

    public ItemStack getResultSlot();

    public Player getPlayer();

    public void setRepairCost(int var1);

    public int getRepairCost();

    public boolean isInputSlot(int var1);

    public int getResultSlotID();

    public void clearInputs();

    public void close();

    public Inventory getInventory();
}

