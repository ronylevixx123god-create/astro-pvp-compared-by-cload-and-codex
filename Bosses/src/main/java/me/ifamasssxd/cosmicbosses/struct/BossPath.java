package me.ifamasssxd.cosmicbosses.struct;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.Vec3D;

import javax.annotation.Nullable;

public class BossPath {
    private final BossPathPoint[] points;
    private BossPathPoint[] openSet;
    private BossPathPoint[] closedSet;
    private BossPathPoint target;
    private int currentPathIndex;
    private int pathLength;

    public BossPath(final BossPathPoint[] pathpoints) {
        this.openSet = new BossPathPoint[0];
        this.closedSet = new BossPathPoint[0];
        this.points = pathpoints;
        this.pathLength = pathpoints.length;
    }

    public void incrementPathIndex() {
        ++this.currentPathIndex;
    }

    public boolean isFinished() {
        return this.currentPathIndex >= this.pathLength;
    }

    @Nullable
    public BossPathPoint getFinalPathPoint() {
        return (this.pathLength > 0) ? this.points[this.pathLength - 1] : null;
    }

    public BossPathPoint getPathPointFromIndex(final int index) {
        return this.points[index];
    }

    public void setPoint(final int index, final BossPathPoint point) {
        this.points[index] = point;
    }

    public int getCurrentPathLength() {
        return this.pathLength;
    }

    public void setCurrentPathLength(final int length) {
        this.pathLength = length;
    }

    public int getCurrentPathIndex() {
        return this.currentPathIndex;
    }

    public void setCurrentPathIndex(final int currentPathIndexIn) {
        this.currentPathIndex = currentPathIndexIn;
    }

    public Vec3D getVectorFromIndex(final Entity entityIn, final int index) {
        final double d0 = this.points[index].getX() + (int) (entityIn.width + 1.0f) * 0.5;
        final double d2 = this.points[index].getY();
        final double d3 = this.points[index].getZ() + (int) (entityIn.width + 1.0f) * 0.5;
        return new Vec3D(d0, d2, d3);
    }

    public Vec3D getPosition(final Entity entityIn) {
        return this.getVectorFromIndex(entityIn, this.currentPathIndex);
    }

    public Vec3D getCurrentPos() {
        final BossPathPoint pathpoint = this.points[this.currentPathIndex];
        return new Vec3D((double) pathpoint.getX(), (double) pathpoint.getY(), (double) pathpoint.getZ());
    }

    public boolean isSamePath(final BossPath pathentityIn) {
        if (pathentityIn == null) {
            return false;
        }
        if (pathentityIn.points.length != this.points.length) {
            return false;
        }
        for (int i = 0; i < this.points.length; ++i) {
            if (this.points[i].getX() != pathentityIn.points[i].getX() || this.points[i].getY() != pathentityIn.points[i].getY() || this.points[i].getZ() != pathentityIn.points[i].getZ()) {
                return false;
            }
        }
        return true;
    }

    public BossPathPoint[] getOpenSet() {
        return this.openSet;
    }

    public BossPathPoint[] getClosedSet() {
        return this.closedSet;
    }

    public BossPathPoint getTarget() {
        return this.target;
    }
}
