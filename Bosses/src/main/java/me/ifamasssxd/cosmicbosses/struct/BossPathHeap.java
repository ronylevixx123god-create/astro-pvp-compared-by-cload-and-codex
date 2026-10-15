package me.ifamasssxd.cosmicbosses.struct;

public class BossPathHeap {
    private BossPathPoint[] pathPoints;
    private int count;

    public BossPathHeap() {
        this.pathPoints = new BossPathPoint[128];
    }

    public BossPathPoint addPoint(final BossPathPoint point) {
        if (point.getIndex() >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        }
        if (this.count == this.pathPoints.length) {
            final BossPathPoint[] apathpoint = new BossPathPoint[this.count << 1];
            System.arraycopy(this.pathPoints, 0, apathpoint, 0, this.count);
            this.pathPoints = apathpoint;
        }
        (this.pathPoints[this.count] = point).setIndex(this.count);
        this.sortBack(this.count++);
        return point;
    }

    public void clearPath() {
        this.count = 0;
    }

    public BossPathPoint dequeue() {
        final BossPathPoint pathpoint = this.pathPoints[0];
        final BossPathPoint[] pathPoints = this.pathPoints;
        final int n = 0;
        final BossPathPoint[] pathPoints2 = this.pathPoints;
        final int count = this.count - 1;
        this.count = count;
        pathPoints[n] = pathPoints2[count];
        this.pathPoints[this.count] = null;
        if (this.count > 0) {
            this.sortForward(0);
        }
        pathpoint.setIndex(-1);
        return pathpoint;
    }

    public void changeDistance(final BossPathPoint point, final float distance) {
        final float f = point.getDistanceToTarget();
        point.setDistanceToTarget(distance);
        if (distance < f) {
            this.sortBack(point.getIndex());
        } else {
            this.sortForward(point.getIndex());
        }
    }

    private void sortBack(int index) {
        final BossPathPoint pathpoint = this.pathPoints[index];
        final float f = pathpoint.getDistanceToTarget();
        while (index > 0) {
            final int i = index - 1 >> 1;
            final BossPathPoint pathpoint2 = this.pathPoints[i];
            if (f >= pathpoint2.getDistanceToTarget()) {
                break;
            }
            (this.pathPoints[index] = pathpoint2).setIndex(index);
            index = i;
        }
        (this.pathPoints[index] = pathpoint).setIndex(index);
    }

    private void sortForward(int index) {
        final BossPathPoint pathpoint = this.pathPoints[index];
        final float f = pathpoint.getDistanceToTarget();
        while (true) {
            final int i = 1 + (index << 1);
            final int j = i + 1;
            if (i >= this.count) {
                break;
            }
            final BossPathPoint pathpoint2 = this.pathPoints[i];
            final float f2 = pathpoint2.getDistanceToTarget();
            BossPathPoint pathpoint3;
            float f3;
            if (j >= this.count) {
                pathpoint3 = null;
                f3 = Float.POSITIVE_INFINITY;
            } else {
                pathpoint3 = this.pathPoints[j];
                f3 = pathpoint3.getDistanceToTarget();
            }
            if (f2 < f3) {
                if (f2 >= f) {
                    break;
                }
                (this.pathPoints[index] = pathpoint2).setIndex(index);
                index = i;
            } else {
                if (f3 >= f) {
                    break;
                }
                (this.pathPoints[index] = pathpoint3).setIndex(index);
                index = j;
            }
        }
        (this.pathPoints[index] = pathpoint).setIndex(index);
    }

    public boolean isPathEmpty() {
        return this.count == 0;
    }
}
