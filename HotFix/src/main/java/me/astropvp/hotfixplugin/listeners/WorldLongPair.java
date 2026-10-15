package me.astropvp.hotfixplugin.listeners;

import java.util.Objects;
import org.bukkit.World;

public class WorldLongPair {
    private final World world;
    private final int x;
    private final int z;
    private final int realX;
    private final int realZ;

    public WorldLongPair(World world, int x, int z, int realX, int realZ) {
        this.world = world;
        this.x = x;
        this.z = z;
        this.realX = realX;
        this.realZ = realZ;
    }

    public World getWorld() {
        return this.world;
    }

    public int getRealX() {
        return this.realX;
    }

    public int getRealZ() {
        return this.realZ;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        WorldLongPair that = (WorldLongPair)o;
        return this.x == that.x && this.z == that.z && Objects.equals(this.world, that.world);
    }

    public int hashCode() {
        return Objects.hash(this.world, this.x, this.z);
    }
}
