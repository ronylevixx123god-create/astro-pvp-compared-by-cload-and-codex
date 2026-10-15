package me.astropvp.objects;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import lombok.SneakyThrows;
import me.astropvp.AstroRevivePlugin;
import me.astropvp.utils.Base64Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeathInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID player;

    private Long time;

    private String items;

    private String armor;

    private String damage;

    private String killer;

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void setArmor(String armor) {
        this.armor = armor;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public void setKiller(String killer) {
        this.killer = killer;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setInst(AstroRevivePlugin inst) {
        this.inst = inst;
    }

    public void setReviver(String reviver) {
        this.reviver = reviver;
    }

    public void setRevived(boolean revived) {
        this.revived = revived;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public Long getTime() {
        return this.time;
    }

    public String getItems() {
        return this.items;
    }

    public String getArmor() {
        return this.armor;
    }

    public String getDamage() {
        return this.damage;
    }

    public String getKiller() {
        return this.killer;
    }

    private int ID = 0;

    private AstroRevivePlugin inst;

    public AstroRevivePlugin getInst() {
        return this.inst;
    }

    private String reviver = "N/A";

    private boolean revived;

    public String getReviver() {
        return this.reviver;
    }

    public boolean isRevived() {
        return this.revived;
    }

    public DeathInventory(AstroRevivePlugin inst, Player player, String killer, String damage) {
        this.player = player.getUniqueId();
        this.time = System.currentTimeMillis();
        this.armor = Base64Utils.playerInventoryToBase64(player.getInventory())[1];

        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack == null) continue;
            if (itemStack.getType() == null) continue;
            if (itemStack.getType() == Material.POTION) {
                player.getInventory().remove(itemStack);
            }
        }

        this.items = Base64Utils.playerInventoryToBase64(player.getInventory())[0];
        this.inst = inst;
        setDamage(damage);
        setKiller(killer);
        inst.getDeathInventories().add(this);
    }

    public DeathInventory(AstroRevivePlugin inst, String uuid, long time, String armor, String items, String killer, String damage) {
        this.player = UUID.fromString(uuid);
        this.time = Long.valueOf(time);
        this.armor = armor;
        this.items = items;
        this.inst = inst;
        setDamage(damage);
        setKiller(killer);
        inst.getDeathInventories().add(this);
    }

    public int getTotal() {
        int total = 0;
        try {
            for (ItemStack item : Base64Utils.itemStackArrayFromBase64(getArmor())) {
                if (item == null) continue;
                ++total;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            for (ItemStack item : Base64Utils.itemStackArrayFromBase64(getItems())) {
                if (item == null) continue;
                ++total;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return total;
    }

    public void revive(Player whoRevived) {
        Player player = Bukkit.getPlayer(getPlayer());
        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null && i.getType() != Material.AIR)
                player.getWorld().dropItem(player.getLocation(), i);
        }
        for (ItemStack i : player.getInventory().getArmorContents()) {
            if (i != null && i.getType() != Material.AIR)
                player.getWorld().dropItem(player.getLocation(), i);
        }
        try {
            player.getInventory().setArmorContents(Base64Utils.itemStackArrayFromBase64(getArmor()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        whoRevived.sendMessage("Revive");
        try {
            player.getInventory().setContents(Base64Utils.itemStackArrayFromBase64(getItems()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        setRevived(true);
        setReviver(whoRevived.getName());
    }



    public int getID() {
        return (int)(this.time / 1000L);
    }
}
