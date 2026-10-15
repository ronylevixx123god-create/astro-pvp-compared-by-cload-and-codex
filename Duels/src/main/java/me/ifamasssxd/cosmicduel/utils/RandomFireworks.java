/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.FireworkEffect
 *  org.bukkit.FireworkEffect$Type
 *  org.bukkit.Location
 *  org.bukkit.entity.Firework
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.FireworkMeta
 */
package me.ifamasssxd.cosmicduel.utils;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class RandomFireworks {
    private static RandomFireworks fireWorks = new RandomFireworks();
    public static ArrayList<Color> colors = new ArrayList();
    public static ArrayList<FireworkEffect.Type> types = new ArrayList();

    public static RandomFireworks getManager() {
        if (colors == null || colors.size() == 0) {
            RandomFireworks.addColors();
        }
        if (types == null || types.size() == 0) {
            RandomFireworks.addTypes();
        }
        return fireWorks;
    }

    public static void addColors() {
        colors = new ArrayList<Color>();
        colors.add(Color.WHITE);
        colors.add(Color.PURPLE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.AQUA);
        colors.add(Color.BLUE);
        colors.add(Color.FUCHSIA);
        colors.add(Color.GRAY);
        colors.add(Color.LIME);
        colors.add(Color.MAROON);
        colors.add(Color.YELLOW);
        colors.add(Color.SILVER);
        colors.add(Color.TEAL);
        colors.add(Color.ORANGE);
        colors.add(Color.OLIVE);
        colors.add(Color.NAVY);
        colors.add(Color.BLACK);
    }

    public static void addTypes() {
        types.add(FireworkEffect.Type.BURST);
        types.add(FireworkEffect.Type.BALL);
        types.add(FireworkEffect.Type.BALL_LARGE);
        types.add(FireworkEffect.Type.CREEPER);
        types.add(FireworkEffect.Type.STAR);
    }

    public FireworkEffect.Type getRandomType() {
        int size = types.size();
        Random ran = new Random();
        FireworkEffect.Type theType = types.get(ran.nextInt(size));
        return theType;
    }

    public Color getRandomColor() {
        int size = colors.size();
        Random ran = new Random();
        Color color = colors.get(ran.nextInt(size));
        return color;
    }

    public void launchRandomFirework(Location loc) {
        Firework fw = (Firework)loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fm = fw.getFireworkMeta();
        fm.setPower(1);
        fm.addEffects(new FireworkEffect[]{FireworkEffect.builder().with(this.getRandomType()).withColor(this.getRandomColor()).build()});
        fw.setFireworkMeta(fm);
    }
}

