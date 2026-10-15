/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.fusesource.jansi.Ansi
 *  org.fusesource.jansi.Ansi$Attribute
 *  org.fusesource.jansi.Ansi$Color
 */
package com.cosmicpvp.CosmicSpaceChests;

import org.fusesource.jansi.Ansi;

public class Logger {
    private final String ansi_green = Ansi.ansi().fg(Ansi.Color.GREEN).boldOff().toString();
    private final String ansi_yellow = Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString();
    private final String ansi_red = Ansi.ansi().fg(Ansi.Color.RED).boldOff().toString();
    private final String ansi_underline = Ansi.ansi().a(Ansi.Attribute.UNDERLINE).boldOff().toString();
    private final String ansi_reset = Ansi.ansi().a(Ansi.Attribute.RESET).boldOff().toString();

    public void notice(String s, Class<?> c) {
        System.out.println(this.ansi_green + "[CosmicSpaceChests (" + c.getSimpleName() + ")] " + s + this.ansi_reset);
    }

    public void debug(String s, Class<?> c) {
        System.out.println(this.ansi_yellow + "(DE) " + this.ansi_reset + "[CosmicSpaceChests (" + c.getSimpleName() + ")] " + s + this.ansi_reset);
    }

    public void log(String s, Class<?> c) {
        System.out.println("[CosmicSpaceChests (" + c.getSimpleName() + ")] " + s);
    }

    public void warning(String s, Class<?> c) {
        System.out.println(this.ansi_yellow + "[CosmicSpaceChests (" + c.getSimpleName() + ")] " + s + this.ansi_reset);
    }

    public void error(String s, Class<?> c) {
        System.out.println(this.ansi_red + "[CosmicSpaceChests (" + c.getSimpleName() + ")] " + s + this.ansi_reset);
    }
}

