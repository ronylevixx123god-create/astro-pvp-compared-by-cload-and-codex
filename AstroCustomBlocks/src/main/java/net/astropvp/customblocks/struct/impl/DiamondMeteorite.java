package net.astropvp.customblocks.struct.impl;

import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.ChunkAble;
import org.bukkit.entity.Player;

public class DiamondMeteorite extends CustomBlock implements ChunkAble {

    public DiamondMeteorite() {
        super(CustomBlockType.DIAMOND_METEORITE);
    }

    @Override
    public void onClick(Player clicker) {
    }
}