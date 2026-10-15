package me.ifamasssxd.cosmicbosses.entities;

import me.ifamasssxd.cosmicbosses.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.*;

import java.util.List;

public class SlimeMinion extends EntitySlime {
    public SlimeMinion(final World world) {
        super(world);
        final List<?> list = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, this.goalSelector, "b");
        list.clear();
        final List<?> c = (List<?>) ReflectionUtils.getObject(PathfinderGoalSelector.class, this.goalSelector, "c");
        c.clear();
        this.goalSelector.a(1, (PathfinderGoal) new PathfinderGoalFloat((EntityInsentient) this));
        this.goalSelector.a(2, (PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) this, (Class) EntityHuman.class, 8.0f));
        this.goalSelector.a(3, (PathfinderGoal) new PathfinderGoalRandomLookaround((EntityInsentient) this));
    }

    protected int getRandomJumpDelay() {
        return this.random.nextInt(10) + 10;
    }

    public void die() {
        super.die();
    }
}
