/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.utils.CC
 *  com.cosmicpvp.cosmicutils.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.utils.PlayerUtil
 *  com.decimatemc.decimatecommons.util.CommonsUtil
 *  com.google.common.collect.Lists
 *  me.ifamasssxd.cosmicbosses.struct.RewardInventory
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package net.astropvp.lootboxes.lootbox;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import net.astropvp.lootboxes.AstroLootboxes;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.api.PluginLootboxManager;
import net.astropvp.lootboxes.api.RewardItem;
import net.astropvp.lootboxes.config.RarityConfig;
import net.astropvp.lootboxes.pojo.Rarity;
import net.astropvp.lootboxes.util.NBTUtil;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AstroRewardItem
        implements RewardItem {
    private static final String ONLY_COMMAND = "reward-only-command";
    private static final String COMMAND_NBT = "reward-command";
    public static final String UUID_NBT = "reward-uuid";
    public static final String RARITY_NBT = "rarity-nbt";
    private final UUID uuid;
    private String displayName;
    private final ItemStack reward;
    private final String command;
    private Rarity rarity = RarityConfig.getDefaultRarity();

    public AstroRewardItem(String displayName, ItemStack reward) {
        this(displayName, reward, null, RarityConfig.getDefaultRarity());
    }

    public AstroRewardItem(String displayName, String command) {
        this(displayName, null, command, RarityConfig.getDefaultRarity());
    }

    public AstroRewardItem(String displayName, ItemStack reward, Rarity rarity) {
        this(displayName, reward, null, rarity);
    }

    public AstroRewardItem(String displayName, String command, Rarity rarity) {
        this(displayName, null, command, rarity);
    }

    public AstroRewardItem(String displayName, ItemStack reward, String command, Rarity rarity) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        this.reward = reward;
        this.command = command;
        this.rarity = rarity;
    }

    public AstroRewardItem(ItemStack item) {
        UUID uuid = NBTUtil.getUUID(item, UUID_NBT);
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        this.uuid = uuid;
        this.command = NBTUtil.hasKey(item, COMMAND_NBT) ? NBTUtil.getString(item, COMMAND_NBT) : null;
        if (NBTUtil.hasKey(item, ONLY_COMMAND)) {
            this.reward = null;
        } else {
            item = NBTUtil.removeKey(item, COMMAND_NBT);
            this.reward = item = NBTUtil.removeKey(item, COMMAND_NBT);
        }
        this.rarity = NBTUtil.hasKey(item, RARITY_NBT) ? RarityConfig.getRarityOrDefault(NBTUtil.getString(item, RARITY_NBT)) : RarityConfig.getDefaultRarity();
        this.displayName = item.getItemMeta().getDisplayName();
    }

    @Override
    public boolean isCommand() {
        return this.command != null;
    }

    @Override
    public boolean isItem() {
        return this.reward != null;
    }

    @Override
    public boolean isCommandAndItem() {
        return this.reward != null && this.command != null;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public ItemStack generateViewableStack() {
        List<String> lore;
        ItemMeta itemMeta;
        ItemStack itemStack = this.isItem() ? this.getReward().clone() : new ItemStack(Material.PAPER);
        if (this.getDisplayName() != null) {
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(CommonsUtil.color((String) this.getDisplayName()));
            itemStack.setItemMeta(itemMeta);
        }
        itemStack = AstroRewardItem.removeMetaLines(itemStack, false);
        itemMeta = itemStack.getItemMeta();
        if (this.isCommand()) {
            lore = itemMeta.hasLore() ? itemMeta.getLore() : Lists.newArrayList();
            lore.add("&7&lCOMMAND: &f" + this.getCommand());
            itemMeta.setLore(CommonsUtil.color(lore));
        }
        lore = itemMeta.hasLore() ? itemMeta.getLore() : Lists.newArrayList();
        lore.add("&7&lRARITY: &f" + this.getRarity().getDisplayName());
        itemMeta.setLore(CommonsUtil.color(lore));
        itemStack.setItemMeta(itemMeta);
        NBTWrapper nbtWrapper = new NBTWrapper(itemStack);
        if (this.isCommand()) {
            nbtWrapper.setString(COMMAND_NBT, this.getCommand());
        }
        if (this.isCommand() && !this.isItem()) {
            nbtWrapper.setBoolean(ONLY_COMMAND, Boolean.valueOf(true));
        }
        nbtWrapper.setString(RARITY_NBT, this.getRarity().getId()).setString(UUID_NBT, this.uuid.toString());
        itemStack = nbtWrapper.build();
        return itemStack;
    }

    @Override
    public ItemStack generatePlayerViewableStack(LootboxManager lootboxManager) {
        if (this.isCommandAndItem()) {
            return this.getLootboxItemIfExists(lootboxManager);
        }
        if (this.isItem()) {
            return this.getLootboxItemIfExists(lootboxManager);
        }
        if (this.isCommand()) {
            return CommonsUtil.createItem((Material) Material.PAPER, (String) this.getDisplayName(), (String[]) new String[0]);
        }
        return null;
    }

    public boolean equalsOtherItem(ItemStack itemStack) {
        return NBTUtil.getUUID(itemStack, UUID_NBT).equals(this.uuid);
    }

    @Override
    public ItemStack getItem() {
        LootboxManager manager = AstroLootboxes.getInstance().getLootboxManager();
        if (this.isCommandAndItem()) {
            return AstroRewardItem.removeMetaLines(this.reward, true);
        }
        if (this.isCommand()) {
            return null;
        }
        if (this.isItem()) {
            return AstroRewardItem.removeMetaLines(this.reward, true);
        }
        return null;
    }

    private ItemStack getLootboxItemIfExists(LootboxManager lootboxManager) {
        NBTWrapper wrapper = new NBTWrapper(this.reward.clone());
        if (wrapper.getCompoundOrCreate("cosmicData").hasKey("astro-lootboxes-is")) {
            String lootboxId = wrapper.getCompoundOrCreate("cosmicData").getString("astro-lootboxes-type");
            Optional<Lootbox> lootbox = lootboxManager.getLootbox(lootboxId);
            if (lootbox.isPresent()) {
                ItemStack item = lootbox.get().getCompleteItem(lootboxManager).clone();
                item.setAmount(this.reward.getAmount());
                return item;
            }
            Optional<PluginLootboxManager> foreignLootbox = AstroLootboxes.getForeignLootboxManager().getPluginManagers().stream().filter(l -> l.getLootbox(lootboxId).isPresent()).findFirst();
            if (foreignLootbox.isPresent()) {
                ItemStack item = foreignLootbox.get().getLootbox(lootboxId).get().getCompleteItem(lootboxManager).clone();
                item.setAmount(this.reward.getAmount());
                return item;
            }
        }
        return wrapper.build();
    }

    @Override
    public void execute(Player player, LootboxManager lootboxManager, boolean addItemsToCollect) {
/*        if (this.isCommandAndItem()) {
            if (addItemsToCollect) {
                ItemStack item = DecimateRewardItem.removeMetaLines(new NBTWrapper(this.getLootboxItemIfExists(lootboxManager)).build(), true);
                RewardInventory.addReward((Player) player, (ItemStack) item);
                player.sendMessage(CC.translate((String) ("&a&l(!) " + item.getItemMeta().getDisplayName() + "&a has been added to your /collect!")));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command.replace("%player%", player.getName()));
                return;
            }
            PlayerUtil.giveOrDropProtectedItem((Player) player, (ItemStack) DecimateRewardItem.removeMetaLines(new NBTWrapper(this.getLootboxItemIfExists(lootboxManager)).build(), true), (int) 30);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command.replace("%player%", player.getName()));
        } else if (this.isCommand()) {
            if (addItemsToCollect) {
                player.sendMessage(CC.translate((String) "&c&l(!) &cUnfortunately this Reward Item could not be added to your /collect!"));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command.replace("%player%", player.getName()));
                return;
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command.replace("%player%", player.getName()));
        } else if (this.isItem()) {
            if (addItemsToCollect) {
                ItemStack item = DecimateRewardItem.removeMetaLines(new NBTWrapper(this.getLootboxItemIfExists(lootboxManager)).build(), true);
                RewardInventory.addReward((Player) player, (ItemStack) item);
                player.sendMessage(CC.translate((String) ("&a&l(!) " + item.getItemMeta().getDisplayName() + "&a has been added to your /collect!")));
                return;
            }*/
            PlayerUtil.giveOrDropProtectedItem((Player) player, (ItemStack) AstroRewardItem.removeMetaLines(new NBTWrapper(this.getLootboxItemIfExists(lootboxManager)).build(), true), (int) 30);
        //}
    }

    public static ItemStack removeMetaLines(ItemStack itemStack, boolean removeNbt) {
        ItemMeta meta = itemStack.getItemMeta();
        List lore = meta.getLore();
        if (lore != null && lore.size() > 0) {
            String lastLine = (String) lore.get(lore.size() - 1);
            boolean changed = false;
            if (ChatColor.stripColor(lastLine).startsWith("COMMAND: ") || ChatColor.stripColor(lastLine).startsWith("RARITY: ")) {
                lore.remove(lore.size() - 1);
                changed = true;
            }
            meta.setLore(CommonsUtil.color((List) lore));
            itemStack.setItemMeta(meta);
            if (changed) {
                return AstroRewardItem.removeMetaLines(itemStack, removeNbt);
            }
        }
        if (removeNbt) {
            if (NBTUtil.hasKey(itemStack, COMMAND_NBT)) {
                itemStack = NBTUtil.removeKey(itemStack, COMMAND_NBT);
            }
            if (NBTUtil.hasKey(itemStack, ONLY_COMMAND)) {
                itemStack = NBTUtil.removeKey(itemStack, ONLY_COMMAND);
            }
            if (NBTUtil.hasKey(itemStack, RARITY_NBT)) {
                itemStack = NBTUtil.removeKey(itemStack, RARITY_NBT);
            }
            if (NBTUtil.hasKey(itemStack, UUID_NBT)) {
                itemStack = NBTUtil.removeKey(itemStack, UUID_NBT);
            }
        }
        return itemStack;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public ItemStack getReward() {
        return this.reward;
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public Rarity getRarity() {
        return this.rarity;
    }

    @Override
    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public String toString() {
        return "RewardItem{uuid=" + this.uuid + ", displayName='" + this.displayName + '\'' + ", reward=" + this.reward + ", command='" + this.command + '\'' + ", rarity=" + this.rarity + '}';
    }
}

