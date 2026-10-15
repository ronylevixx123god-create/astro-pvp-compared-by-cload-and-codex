package net.astropvp.customblocks.tasks;

import net.astropvp.customblocks.CustomBlocks;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.abilities.TickAble;
import net.astropvp.customblocks.struct.impl.AutoGrinder;
import org.bukkit.scheduler.BukkitRunnable;

public class TickAbleBlocksRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (CustomBlock customBlock : CustomBlocks.getInstance().getCustomBlockManager().getCacheValues()) {
            if (customBlock instanceof TickAble) {
                ((TickAble) customBlock).onTick();
            }
        }
    }
}
