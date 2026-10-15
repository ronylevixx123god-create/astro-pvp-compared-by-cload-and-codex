package net.astropvp.customblocks.struct.impl;

import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.ChunkAble;
import org.bukkit.entity.Player;

public class SoulLure extends CustomBlock implements ChunkAble {

    public SoulLure() {
        super(CustomBlockType.SOUL_LURE);
    }

    @Override
    public void onClick(Player clicker) {
    }
}