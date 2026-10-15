package net.crankedpvp.warzonechests.struct;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.Getter;
import lombok.Setter;
import net.astropvp.loottable.struct.LootTable;
import net.astropvp.storage.utils.KeyToSave;
import net.crankedpvp.warzonechests.inventories.LootGUI;
import net.crankedpvp.warzonechests.utils.LocationSerializerDeserializer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class WarzoneChest implements KeyToSave {

    @Getter
    private static final LootTable LOOT_TABLE_TYPE = LootTable.WARZONE_CHESTS;
    private static final long TIME_UNTIL_CHEST_REFILL_IN_SECONDS = 60 * 15;

    private String location;
    private transient final LoadingCache<UUID, LootGUI> playerChestLoot;

    public WarzoneChest(String location) {
        this.location = location;
        this.playerChestLoot = this.initCache();
    }

    // do not use this constructor its so that gson initialises the cache when its load the class
    private WarzoneChest() {
        this.playerChestLoot = this.initCache();
    }

    public void openChest(Player player) {
        LootGUI lootGUI = this.playerChestLoot.get(player.getUniqueId());

        assert lootGUI != null;
        String time = TimeUtils.formatSeconds((lootGUI.getTimeOfCreation() + TIME_UNTIL_CHEST_REFILL_IN_SECONDS * 1000 - System.currentTimeMillis()) / 1000);
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1F, 0.1F);
        player.openInventory(lootGUI.getGui());

        if (lootGUI.isFirstOpen()) {
            player.playSound(LocationSerializerDeserializer.deserialize(this.location), Sound.FIREWORK_BLAST, 1F, 0.1F);
            Firework firework = LocationSerializerDeserializer.deserialize(this.location).getWorld().spawn(LocationSerializerDeserializer.deserialize(this.location).clone().add(0.5D, 0.0D, 0.5D), Firework.class);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.WHITE)
                    .with(FireworkEffect.Type.BALL_LARGE).flicker(true).withTrail().build());
            firework.setFireworkMeta(fireworkMeta);
            fireworkMeta.setPower(5);
            lootGUI.setFirstOpen(false);
        } else {
            player.sendMessage(CC.translate("&e&lWarzone Chests &8➥ &7You have &e" + time + " left&7 until this chest resets."));
        }
    }

    public void resetCache() {
        this.playerChestLoot.invalidateAll();
    }

    @Override
    public String getKey() {
        return this.location;
    }

    @Override
    public void setKey(String location) {
        this.location = location;
    }

    private LoadingCache<UUID, LootGUI> initCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(TIME_UNTIL_CHEST_REFILL_IN_SECONDS, TimeUnit.SECONDS)
                .build(key -> new LootGUI());
    }
}
