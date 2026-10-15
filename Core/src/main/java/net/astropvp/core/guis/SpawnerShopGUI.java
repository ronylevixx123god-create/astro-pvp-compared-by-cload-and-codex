package net.astropvp.core.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import me.astropvp.astrospawners.items.ItemSpawner;
import me.astropvp.astrospawners.utils.EntityUtils;
import net.astropvp.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SpawnerShopGUI {
    private final Gui gui;
    private final Map<EntityType, Double> spawnersMap;
    public SpawnerShopGUI(Player player, int amount) {
        this.gui = new Gui(1, "Spawner Shop (" + amount + ")");
        this.gui.setDefaultClickAction(event -> event.setCancelled(true));
        this.spawnersMap = new HashMap<>();
        spawnersMap.put(EntityType.BLAZE, 750000.0);
        spawnersMap.put(EntityType.IRON_GOLEM, 5000000.0);
        spawnersMap.put(EntityType.MUSHROOM_COW, 12500000.0);
        spawnersMap.put(EntityType.GHAST, 27500000.0);
        spawnersMap.put(EntityType.SNOWMAN, 60000000.0);
        spawnersMap.put(EntityType.CREEPER, 390000.0);
        spawnersMap.put(EntityType.PIG_ZOMBIE, 450000.0);
        spawnersMap.put(EntityType.ENDERMAN, 2000000.0);
        spawnersMap.put(EntityType.ZOMBIE, 115000.0);
        spawnersMap.put(EntityType.SKELETON, 115000.0);
        spawnersMap.put(EntityType.SPIDER, 95000.0);
        spawnersMap.put(EntityType.CAVE_SPIDER, 90000.0);

        for (Map.Entry<EntityType, Double> entry : spawnersMap.entrySet()) {
            EntityType type = entry.getKey();
            Double price = entry.getValue();
            double totalPrice = price * amount;
            ItemStack item = new ItemBuilder(Material.MOB_SPAWNER,1)
                    .setName("&a&l" + EntityUtils.getEntityName(type) + " Spawner &7(x" + amount + ")")
                    .addLore("")
                    .addLore("&aPrice: &f$" + NumberUtils.formatMoney(totalPrice))
                    .colorize()
                    .build();
            gui.addItem(new GuiItem(item, event -> {
                if (EconomyUtils.hasBalance(player, totalPrice)) {
                    PlayerUtil.giveOrDropProtectedItem(player, ItemSpawner.getItem(type, amount), 60);
                    EconomyUtils.withdrawBalance(player, totalPrice);
                    player.sendMessage(CC.translate("&a&l- $" + NumberUtils.formatMoney(totalPrice)));
                } else {
                    player.sendMessage(CC.translate("&c&l(!)&c You do not have enough money to buy this!"));
                }
            }));
        }
    }
}
