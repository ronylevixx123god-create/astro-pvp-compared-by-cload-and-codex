package net.astropvp.customblocks.struct.impl.sellbots;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.SellBot;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SnowGolemSB extends CustomBlock implements SellBot {

    public SnowGolemSB() {
        super(CustomBlockType.SNOWMAN_GOLEM_AUTO_SELL_BOT);
    }

    @Override
    public void onClick(Player clicker) {
        clicker.sendMessage(CC.translate("&e&l(!) &eYour Snow Golem Collector Booster is boosting your profits by 1.20x"));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SNOWMAN;
    }
}