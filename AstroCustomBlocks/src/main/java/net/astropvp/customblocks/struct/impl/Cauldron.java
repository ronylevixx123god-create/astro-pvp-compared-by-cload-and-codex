package net.astropvp.customblocks.struct.impl;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.ChunkAble;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class Cauldron extends CustomBlock implements ChunkAble {

    public Cauldron() {
        super(CustomBlockType.HALLOWEEN_CAULDRON);
    }

    @Override
    public void onClick(Player clicker) {
        clicker.sendMessage(CC.translate("&e&l(!)&e Stand in the same chunk as this Custom Block to unlock it's powers."));
    }
}
