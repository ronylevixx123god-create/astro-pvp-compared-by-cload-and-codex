package org.demonicpvp.demonicruins.manager;


import io.netty.util.internal.ThreadLocalRandom;
import me.matthawkk.chat.CC;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import net.crankedpvp.shards.MobcoinAPI;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.demonicpvp.demonicruins.DemonicRuins;
import org.demonicpvp.demonicruins.mob.MobUtil;

public class RuinsRewardManager implements Listener {
    private final DemonicRuins plugin;

    private LootTable ruinsLoot;

    //private LootTable leviathanBossLoottable;

    public RuinsRewardManager(DemonicRuins plugin) {
        this.plugin = plugin;

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (MobUtil.isRuinMob(entity)) {
            if (event.getEntity().getKiller() == null) return;
            event.getDrops().clear();
            //boolean controllingOutpost = false;//entity.getKiller() != null && HCFOutpostAPI.isControllingOutpost(entity.getKiller(), OutpostType.RUINS);
            int mobcoins = ThreadLocalRandom.current().nextInt(1, 10 + 1);
            MobcoinAPI.addMobcoins(entity.getKiller(), mobcoins);
            entity.getKiller().sendMessage(CC.translate("&a&l+ &a" + mobcoins + " &7(Ruins Mob)"));
            LootFilterAPI.giveItem(event.getEntity().getKiller(), LootTable.SPACE, LootTableAPI.getRandomLootItem(LootTable.SPACE));
            if (Math.random() < 0.35) {
                LootFilterAPI.giveItem(event.getEntity().getKiller(), LootTable.SPACE, LootTableAPI.getRandomLootItem(LootTable.SPACE));
            }
            if (Math.random() < 0.1) {
                LootFilterAPI.giveItem(event.getEntity().getKiller(), LootTable.SPACE, LootTableAPI.getRandomLootItem(LootTable.SPACE));
            }
        }
    }
}
