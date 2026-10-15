package com.rit.sucy.Anvil;

import com.rit.sucy.Anvil.AnvilView;
import java.lang.reflect.Field;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.ContainerAnvil;
import net.minecraft.server.v1_8_R3.IInventory;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryAnvil;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MainAnvil
implements AnvilView {
    final Player player;
    final Plugin plugin;
    CraftInventoryAnvil inv;
    ContainerAnvil anvil;
    int repairCost;

    public MainAnvil(Plugin plugin, Inventory anvil, Player player) {
        this.player = player;
        this.plugin = plugin;
        this.inv = (CraftInventoryAnvil)anvil;
        try {
            Field container = ContainerAnvil.class.getDeclaredField("a");
            container.setAccessible(true);
            this.anvil = (ContainerAnvil)container.get((Object)this.inv.getInventory());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getNameText() {
        try {
            for (net.minecraft.server.v1_8_R3.ItemStack itemStack : this.inv.getIngredientsInventory().getContents()) {
                ItemStack i = CraftItemStack.asBukkitCopy(itemStack);
                if (i.hasItemMeta() && i.getItemMeta().hasDisplayName() && !i.getItemMeta().getDisplayName().equals(ChatColor.stripColor(i.getItemMeta().getDisplayName())))
                    return null;
            }
            Field textField = ContainerAnvil.class.getDeclaredField("n");
            textField.setAccessible(true);
            String name = (String)textField.get(this.anvil);
            if (name == null)
                return null;
            Field g = ContainerAnvil.class.getDeclaredField("h");
            g.setAccessible(true);
            net.minecraft.server.v1_8_R3.ItemStack item = ((IInventory)g.get(this.anvil)).getItem(0);
            if (item == null)
                return null;
            if (name.equals(item.getName()))
                return null;
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ItemStack[] getInputSlots() {
        ItemStack[] input = new ItemStack[]{CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack)this.inv.getIngredientsInventory().getItem(0)), CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack)this.inv.getIngredientsInventory().getItem(1))};
        return input;
    }

    @Override
    public ItemStack[] getInputSlots(int slot, ItemStack newItem) {
        ItemStack[] input = new ItemStack[]{slot == 0 ? newItem : CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack)this.inv.getIngredientsInventory().getItem(0)), slot == 1 ? newItem : CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack)this.inv.getIngredientsInventory().getItem(1))};
        return input;
    }

    @Override
    public int getInputSlotID(int input) {
        return input - 1;
    }

    @Override
    public void setResultSlot(final ItemStack result) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable(){

            @Override
            public void run() {
                if (result == null) {
                    MainAnvil.this.inv.getResultInventory().setItem(0, null);
                } else {
                    MainAnvil.this.inv.getResultInventory().setItem(0, CraftItemStack.asNMSCopy((ItemStack)result));
                }
                ((CraftPlayer)MainAnvil.this.player).getHandle().setContainerData((Container)MainAnvil.this.anvil, 0, MainAnvil.this.anvil.a);
            }
        });
    }

    @Override
    public ItemStack getResultSlot() {
        return CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack)this.inv.getResultInventory().getItem(0));
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setRepairCost(final int repairCost) {
        this.repairCost = repairCost;
        this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable(){

            @Override
            public void run() {
                try {
                    MainAnvil.this.anvil.a = repairCost;
                    ((CraftPlayer)MainAnvil.this.player).getHandle().setContainerData((Container)MainAnvil.this.anvil, 0, MainAnvil.this.anvil.a);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getRepairCost() {
        return this.repairCost;
    }

    @Override
    public boolean isInputSlot(int slot) {
        return slot == 0 || slot == 1;
    }

    @Override
    public int getResultSlotID() {
        return 2;
    }

    @Override
    public void clearInputs() {
        this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable(){

            @Override
            public void run() {
                MainAnvil.this.inv.getIngredientsInventory().setItem(0, null);
                MainAnvil.this.inv.getIngredientsInventory().setItem(1, null);
                ((CraftPlayer)MainAnvil.this.player).getHandle().setContainerData((Container)MainAnvil.this.anvil, 0, MainAnvil.this.anvil.a);
            }
        });
    }

    @Override
    public void close() {
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}

