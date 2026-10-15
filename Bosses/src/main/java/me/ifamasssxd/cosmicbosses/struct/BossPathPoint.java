package me.ifamasssxd.cosmicbosses.struct;

import com.cosmicpvp.cosmicutils.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.PathPoint;

import java.lang.reflect.Field;

public class BossPathPoint extends PathPoint {
    private final Field d;
    private final Field e;
    private final Field f;
    private final Field g;
    private final Field h;
    private BossPathPoint previous;

    public BossPathPoint(final int i, final int i1, final int i2) throws NoSuchFieldException {
        super(i, i1, i2);
        final Class<?> superClass = this.getClass().getSuperclass();
        this.d = ReflectionUtils.getField((Class) superClass, "d");
        this.e = ReflectionUtils.getField((Class) superClass, "e");
        this.f = ReflectionUtils.getField((Class) superClass, "f");
        this.g = ReflectionUtils.getField((Class) superClass, "g");
        this.h = ReflectionUtils.getField((Class) superClass, "h");
    }

    public int getX() {
        return this.a;
    }

    public int getY() {
        return this.b;
    }

    public int getZ() {
        return this.c;
    }

    public int getIndex() {
        try {
            return (int) this.d.get(this);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
            return -1;
        }
    }

    public void setIndex(final int index) {
        try {
            this.d.set(this, index);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    public float getTotalPathPointDistance() {
        try {
            return (float) this.e.get(this);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
            return 0.0f;
        }
    }

    public void setTotalPathDistance(final float value) {
        try {
            this.e.set(this, value);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    public float getDistanceToNext() {
        try {
            return (float) this.f.get(this);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
            return 0.0f;
        }
    }

    public void setDistanceToNext(final float distance) {
        try {
            this.f.set(this, distance);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    public float getDistanceToTarget() {
        try {
            return (float) this.g.get(this);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
            return 0.0f;
        }
    }

    public void setDistanceToTarget(final float distance) {
        try {
            this.g.set(this, distance);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    public BossPathPoint getPrevious() {
        return this.previous;
    }

    public void setPrevious(final BossPathPoint point) {
        this.previous = point;
    }

    public boolean isVisited() {
        return this.i;
    }

    public void setVisited(final boolean val) {
        this.i = val;
    }

    public boolean isAssigned() {
        return this.a();
    }

    public float distanceTo(final PathPoint point) {
        return this.a(point);
    }

    public float distanceToSquared(final PathPoint point) {
        return this.b(point);
    }
}
