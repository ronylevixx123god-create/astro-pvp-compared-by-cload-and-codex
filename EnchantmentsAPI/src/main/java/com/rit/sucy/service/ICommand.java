/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package com.rit.sucy.service;

import com.rit.sucy.EnchantmentAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ICommand {
    public boolean execute(EnchantmentAPI var1, CommandSender var2, Command var3, String var4, String[] var5);
}

