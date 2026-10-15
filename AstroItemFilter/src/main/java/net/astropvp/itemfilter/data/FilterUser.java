package net.astropvp.itemfilter.data;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.storage.utils.KeyToSave;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FilterUser implements KeyToSave {
    private UUID uuid;
    private List<String> filter;
    private List<String> pets;

    private boolean enabled;

    public FilterUser(UUID uuid) {
        this.filter = new ArrayList<>();
        this.pets = new ArrayList<>();
        this.uuid = uuid;
        this.enabled = false;
    }


    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public void setKey(String s) {
        this.uuid = UUID.fromString(s);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean canPickup(Material material) {
        return this.filter.contains(material.name());
    }

}
