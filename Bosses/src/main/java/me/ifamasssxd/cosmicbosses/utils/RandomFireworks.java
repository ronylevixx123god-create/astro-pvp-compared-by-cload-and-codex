package me.ifamasssxd.cosmicbosses.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Random;

public class RandomFireworks {
    private static RandomFireworks fireWorks;
    public static ArrayList<Color> colors;
    public static ArrayList<FireworkEffect.Type> types;

    public static RandomFireworks getManager() {
        if (RandomFireworks.colors == null || RandomFireworks.colors.size() == 0) {
            addColors();
        }
        if (RandomFireworks.types == null || RandomFireworks.types.size() == 0) {
            addTypes();
        }
        return RandomFireworks.fireWorks;
    }

    public static void addColors() {
        (RandomFireworks.colors = new ArrayList<Color>()).add(Color.WHITE);
        RandomFireworks.colors.add(Color.PURPLE);
        RandomFireworks.colors.add(Color.RED);
        RandomFireworks.colors.add(Color.GREEN);
        RandomFireworks.colors.add(Color.AQUA);
        RandomFireworks.colors.add(Color.BLUE);
        RandomFireworks.colors.add(Color.FUCHSIA);
        RandomFireworks.colors.add(Color.GRAY);
        RandomFireworks.colors.add(Color.LIME);
        RandomFireworks.colors.add(Color.MAROON);
        RandomFireworks.colors.add(Color.YELLOW);
        RandomFireworks.colors.add(Color.SILVER);
        RandomFireworks.colors.add(Color.TEAL);
        RandomFireworks.colors.add(Color.ORANGE);
        RandomFireworks.colors.add(Color.OLIVE);
        RandomFireworks.colors.add(Color.NAVY);
        RandomFireworks.colors.add(Color.BLACK);
    }

    public static void addTypes() {
        RandomFireworks.types.add(FireworkEffect.Type.BURST);
        RandomFireworks.types.add(FireworkEffect.Type.BALL);
        RandomFireworks.types.add(FireworkEffect.Type.BALL_LARGE);
        RandomFireworks.types.add(FireworkEffect.Type.CREEPER);
        RandomFireworks.types.add(FireworkEffect.Type.STAR);
    }

    public FireworkEffect.Type getRandomType() {
        final int size = RandomFireworks.types.size();
        final Random ran = new Random();
        final FireworkEffect.Type theType = RandomFireworks.types.get(ran.nextInt(size));
        return theType;
    }

    public Color getRandomColor() {
        final int size = RandomFireworks.colors.size();
        final Random ran = new Random();
        final Color color = RandomFireworks.colors.get(ran.nextInt(size));
        return color;
    }

    public void launchRandomFirework(final Location loc) {
        final Firework fw = (Firework) loc.getWorld().spawn(loc, (Class) Firework.class);
        final FireworkMeta fm = fw.getFireworkMeta();
        fm.setPower(1);
        fm.addEffects(new FireworkEffect[]{FireworkEffect.builder().with(this.getRandomType()).withColor(this.getRandomColor()).build()});
        fw.setFireworkMeta(fm);
    }

    static {
        RandomFireworks.fireWorks = new RandomFireworks();
        RandomFireworks.colors = new ArrayList<Color>();
        RandomFireworks.types = new ArrayList<FireworkEffect.Type>();
    }
}
