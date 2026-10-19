package me.ifamasssxd.cosmiccontests.struct;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.struct.prizes.Prize;
import me.ifamasssxd.cosmiccontests.utils.FactionPointsHook;
import me.ifamasssxd.cosmiccontests.struct.prizes.RankingPrize;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import me.ifamasssxd.cosmiccontests.utils.InventoryUtils;
import me.ifamasssxd.cosmiccontests.utils.ItemBuilder;
import me.ifamasssxd.cosmiccontests.utils.StringUtils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PrizeManager {
    private File file;

    private FileConfiguration configuration;

    private Map<UUID, List<RankingPrize>> cachedPrizes = new HashMap<>();

    public Map<UUID, List<RankingPrize>> getCachedPrizes() {
        return this.cachedPrizes;
    }

    public List<Prize> getPrizes() {
        return this.prizes;
    }

    List<Prize> prizes = Lists.newArrayList(new Prize(3, ChatColor.GREEN + "15,000 EXP", Material.PAPER, "15,000 EXP", null, ExpBottles.getXpBottle(15000, "Contests"), 1), new Prize(2, ChatColor.GREEN + "30,000 EXP", Material.PAPER, "30,000 EXP", null,

            ExpBottles.getXpBottle(30000, "Contests"), 1), new Prize(1, ChatColor.GREEN + "75,000 EXP", Material.PAPER, "75,000 EXP", null,

            ExpBottles.getXpBottle(75000, "Contests"), 1));

    public void loadPrizes() throws IOException {
        try {
            this.file = new File(CosmicContests.get().getDataFolder(), "pendingPrizes.yml");
            if (!this.file.exists())
                this.file.createNewFile();
            this.configuration = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
            ConfigurationSection section = this.configuration.getConfigurationSection("pending");
            if (section != null)
                for (String uuid : section.getKeys(false)) {
                    List<RankingPrize> found = Lists.newArrayList();
                    List<String> prizes = section.getStringList(uuid + ".prizes");
                    for (String prizeName : prizes) {
                        int rank = org.apache.commons.lang3.StringUtils.isNumeric(prizeName) ? Integer.parseInt(prizeName) : (prizeName.contains("#") ? Integer.parseInt(prizeName.split("#")[0]) : 0);
                        if (rank == 0) {
                            System.out.println("Prize rank null: " + prizeName);
                            continue;
                        }
                        RankingPrize prize = new RankingPrize(rank);
                        found.add(prize);
                    }
                    this.cachedPrizes.put(UUID.fromString(uuid), found);
                }
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    public List<Prize> getPossiblePrizes(int ranking) {
        List<ItemStack> done = Lists.newArrayList();
        if (ranking == 3) {
            done.addAll(Lists.newArrayList(CosmicSpaceChestsAPI.getItems(ItemRarity.LEGENDARY, 3)));
        } else if (ranking == 2) {
            done.addAll(Lists.newArrayList(CosmicSpaceChestsAPI.getItems(ItemRarity.LEGENDARY, 6)));
        } else if (ranking == 1) {
            done.addAll(Lists.newArrayList(CosmicSpaceChestsAPI.getItems(ItemRarity.LEGENDARY, 8)));
            done.addAll(Lists.newArrayList(CosmicSpaceChestsAPI.getItems(ItemRarity.GOD, 1)));
        }
        ItemStack pointItem = FactionPointsHook.getFactionPointsItem("contest_prize", ranking);
        if (pointItem != null)
            done.add(pointItem);
        ArrayList prizes = Lists.newArrayList((Iterable)this.prizes.stream().filter(pri -> (pri.getRankingRequired() == ranking)).collect(Collectors.toList()));
        for (ItemStack is : done) {
            String name = (is.getItemMeta().getDisplayName() != null) ? is.getItemMeta().getDisplayName() : is.getType().name().toLowerCase().replace("_", " ");
            Prize prize = new Prize(name, is.getType(), "prize" + this.prizeID++, null, is, 1, ranking);
            prizes.add(prize);
        }
        return prizes;
    }

    private int prizeID = 0;

    public void removePrize(Player player, RankingPrize prize) throws IOException {
        List<RankingPrize> prizes = this.cachedPrizes.get(player.getUniqueId());
        if (prizes == null)
            return;
        prizes.remove(prize);
        List<String> toSave = Lists.newArrayList();
        prizes.forEach(p -> toSave.add(p.getRanking() + "# Contest"));
        this.configuration.set("pending." + player.getUniqueId().toString() + ".prizes", toSave);
        savePendingFile();
    }

    public void addPrize(ContestUser user, int ranking) throws IOException {
        List<RankingPrize> prizes = this.cachedPrizes.computeIfAbsent(user.getUuid(), k -> Lists.newArrayList());
        prizes.add(new RankingPrize(ranking));
        List<String> toSAve = Lists.newArrayList();
        prizes.forEach(p -> toSAve.add(p.getRanking() + "# Contest"));
        this.configuration.set("pending." + user.getUuid().toString() + ".prizes", toSAve);
        savePendingFile();
    }

    public void openPrizeClaimMenu(Player player) {
        List<RankingPrize> prizes = getCachedPrizes().get(player.getUniqueId());
        if (prizes == null || prizes.isEmpty()) {
            player.sendMessage(ChatColor.RED + "You have no prizes to claim!");
            return;
        }
        Inventory inv = Bukkit.createInventory(null, InventoryUtils.getSize(prizes.size()), "Challenge Prizes");
        int slot = 0;
        for (RankingPrize prize : prizes) {
            if (slot >= inv.getSize())
                break;
            ItemStack ranking = ItemBuilder.buildItem(Material.CHEST, ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Challenge Prize (" + ChatColor.GRAY + StringUtils.getFormattedNumber(prize.getRanking()) + ChatColor.GREEN + ChatColor.BOLD + ")", new String[] { ChatColor.GRAY + "Click to unlock and claim this prize." });
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(ranking);
            NBTTagCompound comp = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
            comp.setInt("contestPrize", prize.getRanking());
            nmsItem.setTag(comp);
            inv.setItem(slot++, (ItemStack)CraftItemStack.asCraftMirror(nmsItem));
        }
        player.openInventory(inv);
    }

    private void savePendingFile() throws IOException {
        try {
            this.configuration.save(this.file);
        } catch (Throwable $ex) {
            throw $ex;
        }
    }
}
