package me.astropvp.astropvpskins.cache;

import lombok.Getter;
import me.astropvp.astropvpskins.enums.SkinType;
import net.astropvp.core.utils.cache.Cache;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemSkinCache extends Cache {
    private List<SkinType> currentSkins;

    private Player player;

    public ItemSkinCache(Player player) {
        super("item_skin");
        this.player = player;
        this.currentSkins = new ArrayList<>();
    }

    public void addItemSkin(SkinType type) {
        this.currentSkins.add(type);
        type.getItemSkin().onEquip(this.player);
    }

    public void removeItemSkin(SkinType type) {
        this.currentSkins.remove(type);
        type.getItemSkin().onUnequip(this.player);
    }
}
