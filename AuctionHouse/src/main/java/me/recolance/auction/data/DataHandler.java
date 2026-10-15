// Decompiled with: FernFlower
// Class Version: 8
package me.recolance.auction.data;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.recolance.auction.Auction;
import me.recolance.auction.auction.Listing;
import me.recolance.auction.auction.UnclaimedListing;
import me.recolance.auction.menu.categories.AuctionCategory;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

public class DataHandler {
    private static List<AuctionCategory> auctionCategories = Lists.newLinkedList();
    private static long nextCategoryCalculation;
    private static List<Material> subCategoryItems = null;

    public static void generateTables() {
        Connection connection = Database.getConnection();

        try {
            PreparedStatement listings = connection.prepareStatement("CREATE TABLE IF NOT EXISTS listings(id VARCHAR(255) NOT NULL,listed_by VARCHAR(255) NOT NULL,item TEXT NOT NULL,price INT NOT NULL,end_time BIGINT NOT NULL,PRIMARY KEY (id))");
            listings.execute();
            PreparedStatement expired = connection.prepareStatement("CREATE TABLE IF NOT EXISTS expired(id VARCHAR(255) NOT NULL,player VARCHAR(255) NOT NULL,item TEXT NOT NULL,expire_time BIGINT NOT NULL,PRIMARY KEY (id))");
            expired.execute();
            generateIndex();
            listings.close();
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

    }

    private static void generateIndex() {
        Connection connection = Database.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("CREATE INDEX IDX_EXPIRED ON expired (player)");
            statement.execute();
        } catch (SQLException var2) {
        }

    }

    public static void loadListings() {
        Connection connection = Database.getConnection();

        try {
            PreparedStatement load = connection.prepareStatement("SELECT * FROM listings");
            ResultSet set = load.executeQuery();

            while(set.next()) {
                UUID playerId = UUID.fromString(set.getString("listed_by"));
                Listing listing = new Listing(UUID.fromString(set.getString("id")), set.getLong("end_time"), playerId, fromString(set.getString("item")), set.getBigDecimal("price").doubleValue());
                DataHolder.addListing(listing);
                DataHolder.addPlayerListing(playerId, listing);
            }

            List<Listing> listings = DataHolder.getListings();
            Collections.sort(listings, new Comparator<Listing>() {
                public int compare(Listing l1, Listing l2) {
                    return Long.valueOf(l1.getEndTime()).compareTo(l2.getEndTime());
                }
            });
            Collections.reverse(listings);
            load.close();
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

    }

    public static List<Material> getSubCategoryItems() {
        if (subCategoryItems == null) {
            subCategoryItems = Lists.newArrayList(GeneralUtil.armor);
            subCategoryItems.addAll(Arrays.asList(GeneralUtil.weapons));
            subCategoryItems.addAll(Arrays.asList(GeneralUtil.tools));
            subCategoryItems.addAll(Arrays.asList(Material.BOOK, Material.MOB_SPAWNER, Material.TNT, Material.PAPER));
        }

        return subCategoryItems;
    }

    private static boolean isSubCategoryItem(MaterialData data, ItemStack item) {
        Material type = data.getItemType();
        boolean contains = getSubCategoryItems().contains(type);
        Material dataType = data.getItemType();
        String display = item.getItemMeta().getDisplayName();
        return !contains && dataType == Material.EMERALD && item.getItemMeta().hasDisplayName() && display.startsWith(CC.RedB + "Soul Gem [") ? true : contains;
    }

    public static List<AuctionCategory> getAuctionCategories() {
        if (nextCategoryCalculation == 0L || nextCategoryCalculation <= System.currentTimeMillis()) {
            auctionCategories.clear();
            long start = System.currentTimeMillis();
            Map<Material, AuctionCategory> subCategoryCategories = new HashMap();
            new HashMap();
            Map<Integer, AuctionCategory> bookCategories = new HashMap();
            Map<String, AuctionCategory> matchingItemNames = new HashMap();
            DataHolder.getListings().forEach((item) -> {
                if (item.isValid() && !item.isExpired()) {
                    ItemStack selling = item.getItem().clone();
                    String name = ItemUtils.getDisplayName(selling);
                    if (name == null) {
                        name = StringUtils.capitaliseAllWords(item.toString().toLowerCase().replace("_", " "));
                    }

                    if (selling.getEnchantments().size() > 0) {
                        selling.getEnchantments().forEach((ench, level) -> {
                            selling.removeEnchantment(ench);
                        });
                    }

                    selling.setAmount(1);
                    MaterialData type = selling.getData();
                    Material mat = type.getItemType();
                    if (isSubCategoryItem(type, selling)) {
                        String subCategoryName = CC.White + StringUtils.capitaliseAllWords(mat.name().toLowerCase().replace("_", " "));
                        if (name.startsWith(CC.RedB + "Soul Gem") && mat == Material.EMERALD) {
                            subCategoryName = CC.RedB + "Soul Gems";
                        } else if (item.getBookRarity() != null) {
                            subCategoryName = CC.White + "Enchant Books";
                        }

                        String finalSubCategoryName = subCategoryName;
                        String finalSubCategoryName1 = subCategoryName;
                        AuctionCategory category = (AuctionCategory)subCategoryCategories.computeIfAbsent(mat, (e) -> {
                            return new AuctionCategory(finalSubCategoryName, ChatColor.stripColor(finalSubCategoryName1), (new ItemBuilder(selling)).name(finalSubCategoryName1).build(), Lists.newArrayList(), (Listing)null, Lists.newLinkedList(), 10);
                        });
                        if (item.getBookRarity() != null) {
                            String bookName = item.getBookTierColor() + item.getBookTierName() + " Enchant Books";
                            AuctionCategory bookCategory = (AuctionCategory)bookCategories.computeIfAbsent(item.getBookRarity(), (e) -> {
                                return new AuctionCategory(bookName, ChatColor.stripColor(bookName), (new ItemBuilder(selling)).name(bookName).build(), Lists.newArrayList(), (Listing)null, Lists.newLinkedList(), item.getBookRarity());
                            });
                            bookCategory.getCategoryItems().add(item);
                            if (!category.getSubCategories().contains(bookCategory)) {
                                category.getSubCategories().add(bookCategory);
                            }

                            return;
                        }

                        category.getCategoryItems().add(item);
                        return;
                    }

                    String finalName = name;
                    String finalName1 = name;
                    ((AuctionCategory)matchingItemNames.computeIfAbsent(name, (e) -> {
                        return new AuctionCategory(finalName, ChatColor.stripColor(finalName1), selling, Lists.newArrayList(), (Listing)null, Lists.newLinkedList(), 10);
                    })).getCategoryItems().add(item);
                }

            });
            auctionCategories.addAll(matchingItemNames.values());
            auctionCategories.addAll(subCategoryCategories.values());
            auctionCategories.forEach(AuctionCategory::sortCategoryItems);
            auctionCategories.forEach(AuctionCategory::sortSubCategories);
            Collections.sort(auctionCategories, AuctionCategory::compareTo);
            Auction.getPlugin().getLogger().info("Took " + (System.currentTimeMillis() - start) + "ms to calculate auction categories " + auctionCategories.size() + " Listings: " + DataHolder.getListings().size());
        }

        auctionCategories.removeIf((cat) -> {
            cat.getCategoryItems().removeIf((item) -> {
                return item.isExpired() || !item.isValid();
            });
            return cat.getCategoryItems().isEmpty() && cat.getSubCategories().isEmpty();
        });
        return auctionCategories;
    }

    public static void loadUnclaimedListings(UUID playerId) {
        Connection connection = Database.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id, item, expire_time FROM expired WHERE player=?");
            statement.setString(1, playerId.toString());
            ResultSet set = statement.executeQuery();

            while(set.next()) {
                UnclaimedListing uL = new UnclaimedListing(UUID.fromString(set.getString("id")), playerId, fromString(set.getString("item")), set.getLong("expire_time"));
                DataHolder.addUnclaimedListing(playerId, uL);
            }

            statement.close();
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

    }

    public static void saveAllListings(boolean async) {
        Connection connection = Database.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("REPLACE INTO listings(id, listed_by, item, price, end_time) VALUES (?,?,?,?,?)");
            Iterator var3 = DataHolder.getListings().iterator();

            while(var3.hasNext()) {
                Listing l = (Listing)var3.next();
                statement.setString(1, l.getId().toString());
                statement.setString(2, l.getWhoListed().toString());
                statement.setString(3, toString(l.getItem()));
                statement.setBigDecimal(4, new BigDecimal(l.getPrice()));
                statement.setLong(5, l.getEndTime());
                statement.addBatch();
            }

            if (async) {
                asyncExecuteBatch(statement);
            } else {
                statement.executeBatch();
                statement.close();
            }
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

    }

    public static void saveListing(Listing listing, boolean async) {
        Connection connection = Database.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("REPLACE INTO listings(id, listed_by, item, price, end_time) VALUES (?,?,?,?,?)");
            statement.setString(1, listing.getId().toString());
            statement.setString(2, listing.getWhoListed().toString());
            statement.setString(3, toString(listing.getItem()));
            statement.setBigDecimal(4, new BigDecimal(listing.getPrice()));
            statement.setLong(5, listing.getEndTime());
            if (async) {
                asyncExecute(statement);
            } else {
                statement.execute();
                statement.close();
            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }

    public static void saveUnclaimedListing(UnclaimedListing listing, boolean async) {
        Connection connection = Database.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("REPLACE INTO expired(id, player, item, expire_time) VALUES (?,?,?,?)");
            statement.setString(1, listing.getId().toString());
            statement.setString(2, listing.getListedBy().toString());
            statement.setString(3, toString(listing.getItem()));
            statement.setLong(4, listing.getExpireTime());
            if (async) {
                asyncExecute(statement);
            } else {
                statement.execute();
                statement.close();
            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }

    public static void deleteListing(Listing listing, boolean async) {
        Connection connection = Database.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM listings WHERE id=?");
            statement.setString(1, listing.getId().toString());
            if (async) {
                asyncExecute(statement);
            } else {
                statement.execute();
                statement.close();
            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }

    public static void deleteUnclaimedListing(UnclaimedListing listing, boolean async) {
        Connection connection = Database.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM expired WHERE id=?");
            statement.setString(1, listing.getId().toString());
            if (async) {
                asyncExecute(statement);
            } else {
                statement.execute();
                statement.close();
            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }

    public static void deleteOverExpiredUnclaimedListing(boolean async) {
        Connection connection = Database.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM expired WHERE expire_time<?");
            statement.setLong(1, System.currentTimeMillis());
            if (async) {
                asyncExecute(statement);
            } else {
                statement.execute();
                statement.close();
            }
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

    }

    private static void asyncExecute(final PreparedStatement statement) {
        if (Auction.stoppingServer) {
            try {
                statement.execute();
                statement.close();
            } catch (SQLException var2) {
                var2.printStackTrace();
            }

        } else {
            (new BukkitRunnable() {
                public void run() {
                    try {
                        statement.execute();
                        statement.close();
                    } catch (SQLException var2) {
                        var2.printStackTrace();
                    }

                }
            }).runTaskAsynchronously(Auction.plugin);
        }
    }

    private static void asyncExecuteBatch(final PreparedStatement statement) {
        if (Auction.stoppingServer) {
            try {
                statement.execute();
            } catch (SQLException var2) {
                var2.printStackTrace();
            }

        } else {
            (new BukkitRunnable() {
                public void run() {
                    try {
                        statement.executeBatch();
                    } catch (SQLException var2) {
                        var2.printStackTrace();
                    }

                }
            }).runTaskAsynchronously(Auction.plugin);
        }
    }

    public static String toString(ItemStack item) {
        if (item == null) {
            return "null";
        } else {
            NBTTagCompound tagCompound = new NBTTagCompound();
            CraftItemStack.asNMSCopy(item).save(tagCompound);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try {
                NBTCompressedStreamTools.a(tagCompound, outputStream);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return BaseEncoding.base64().encode(outputStream.toByteArray());
        }
    }

    public static ItemStack fromString(String string) {
        if (string.equals("null")) {
            return null;
        } else {
            InputStream is = new ByteArrayInputStream(BaseEncoding.base64().decode(string));
            NBTTagCompound nbttagcompound = null;

            try {
                nbttagcompound = NBTCompressedStreamTools.a(is);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_8_R3.ItemStack.createStack(nbttagcompound));
        }
    }
}
