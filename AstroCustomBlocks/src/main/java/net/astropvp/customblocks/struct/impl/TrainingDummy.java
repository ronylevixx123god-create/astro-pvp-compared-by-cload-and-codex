package net.astropvp.customblocks.struct.impl;

import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.ChunkAble;
import org.bukkit.entity.Player;

public class TrainingDummy extends CustomBlock implements ChunkAble {

    public TrainingDummy() {
        super(CustomBlockType.TRAINING_DUMMY);
    }

    @Override
    public void onClick(Player clicker) {
    }
}