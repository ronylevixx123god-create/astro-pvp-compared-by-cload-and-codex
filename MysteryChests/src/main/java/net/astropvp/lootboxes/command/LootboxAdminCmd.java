/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  co.aikar.commands.BaseCommand
 *  co.aikar.commands.annotation.CommandAlias
 *  co.aikar.commands.annotation.CommandPermission
 *  co.aikar.commands.annotation.Default
 *  co.aikar.commands.annotation.Subcommand
 *  com.cosmicpvp.cosmicutils.utils.CC
 *  com.cosmicpvp.cosmicutils.utils.NBTWrapper
 *  com.decimatemc.decimatecommons.util.CommonsUtil
 *  org.bukkit.command.CommandSender
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.javatuples.Pair
 */
package net.astropvp.lootboxes.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.astropvp.lootboxes.AstroLootboxes;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.api.PluginLootboxManager;
import net.astropvp.lootboxes.api.RewardItem;
import net.astropvp.lootboxes.pojo.PluginInformation;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@CommandAlias(value = "alootbox|alb|amc")
public class LootboxAdminCmd
        extends BaseCommand {
    private static final String ADMIN_PERMISSION = "decimatelootboxes.admin";
    private static final List<String> HELP_MESSAGE = Arrays.asList("&c&m----------&c&lAstro&f&lLootboxes&c&m----------", "&c/alb refresh &8- Refreshes lootboxes", "&c&m--------------------------------------");
    private final AstroLootboxes plugin;
    private final LootboxManager lootboxManager;

    public LootboxAdminCmd(AstroLootboxes plugin, LootboxManager lootboxManager) {
        this.plugin = plugin;
        this.lootboxManager = lootboxManager;
    }

    @Subcommand(value = "refresh")
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onRefreshCommand(CommandSender sender) {
        for (Pair<PluginInformation, PluginLootboxManager> manager : AstroLootboxes.getForeignLootboxManager().getDescribedPluginManagers()) {
            ArrayList<Lootbox> lootboxes = new ArrayList<Lootbox>(((PluginLootboxManager) manager.getValue1()).getLootboxes());
            lootboxes.addAll(AstroLootboxes.getInstance().getLootboxManager().getLootboxes());
            for (int i = lootboxes.size() - 1; i >= 0; --i) {
                Lootbox lootbox = lootboxes.get(i);
                List<RewardItem> items = lootbox.getRandomRewards();
                for (int j = items.size() - 1; j >= 0; --j) {
                    if (items.get(j).isCommand()) continue;
                    ItemStack stack = items.get(j).getItem();
                    if (stack == null) {
                        items.remove(j);
                        continue;
                    }
                    Optional<Lootbox> l = this.lootboxManager.getLootboxFrom(stack);
                    if (l != null && l.isPresent()) {
                        Lootbox lo = l.get();
                        RewardItem rItem = items.get(j);
                        if (lo.getDisplayItem().getItemMeta().hasDisplayName()) {
                            rItem.setDisplayName(lo.getDisplayItem().getItemMeta().getDisplayName());
                        }
                        if (!lo.getDisplayItem().getItemMeta().hasLore()) continue;
                        ItemStack reward = rItem.getReward();
                        ItemMeta meta = reward.getItemMeta();
                        meta.setLore(lo.getDisplayItem().getItemMeta().getLore());
                        reward.setItemMeta(meta);
                        continue;
                    }
                    if (!new NBTWrapper(stack).getCompoundOrCreate(this.lootboxManager.getNBTCompound()).getBoolean(this.lootboxManager.getNBTIsLootbox()).booleanValue() || l != null && l.isPresent())
                        continue;
                    items.remove(j);
                }
            }
        }
        sender.sendMessage(CC.translate((String) "&a&l(!) &aYou have refreshed all the lootboxes!"));
    }

    @Default
    @CommandPermission(value = "decimatelootboxes.admin")
    public void onDefaultCommand(CommandSender sender) {
        CommonsUtil.sendMessage((CommandSender) sender, HELP_MESSAGE);
    }
}

