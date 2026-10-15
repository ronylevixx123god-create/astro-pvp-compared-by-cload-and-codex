package net.astropvp.christmas.managers;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.storage.utils.KeyToSave;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class NebulaPlayer implements KeyToSave {
    private String ip;
    private UUID uuid;
    private boolean hasPurchased;

    public NebulaPlayer(UUID uuid, String ip) {
        this.uuid = uuid;
        this.ip  = ip;
        this.hasPurchased = false;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public void setKey(String s) {
        this.uuid = UUID.fromString(s);
    }
}
