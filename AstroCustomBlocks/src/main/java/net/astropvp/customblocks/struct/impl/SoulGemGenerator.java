package net.astropvp.customblocks.struct.impl;

import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.ChunkAble;
import org.bukkit.entity.Player;

public class SoulGemGenerator extends CustomBlock implements ChunkAble {

    public SoulGemGenerator() {
        super(CustomBlockType.SOUL_GEM_GENERATOR);
    }

    @Override
    public void onClick(Player clicker) {
    }
}