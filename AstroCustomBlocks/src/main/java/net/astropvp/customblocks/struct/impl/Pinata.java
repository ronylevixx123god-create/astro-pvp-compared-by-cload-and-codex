package net.astropvp.customblocks.struct.impl;

import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.ChunkAble;
import org.bukkit.entity.Player;

public class Pinata extends CustomBlock implements ChunkAble {

    public Pinata() {
        super(CustomBlockType.SPRING_2021_LOOTINATA);
    }

    @Override
    public void onClick(Player clicker) {
    }
}