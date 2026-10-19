package net.astropvp.auction;

import com.decimatemc.blackmarketauction.BlackMarketAuction;
import net.astropvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.lang.reflect.Field;
import java.util.UUID;

/** Durable escrow evidence. Interrupted external economy operations require reconciliation,
 * never guessed replay: Vault has no transactional/idempotency API. */
public final class AuctionJournal {
    private final File file;
    private boolean pending;
    public AuctionJournal(File directory) { file = new File(directory, "active-auction.yml"); }
    public boolean isPending() { return pending; }
    public Auction restore() {
        if (!file.exists()) return null;
        try {
            YamlConfiguration state = new YamlConfiguration();
            state.load(file);
            if (!"active".equals(state.getString("phase"))) {
                pending = true;
                throw new IllegalStateException("Interrupted economy/reward operation: preserve active-auction.yml and reconcile recorded balances before restarting BAH");
            }
            Auction auction = new Auction(Utils.fromBase64(state.getString("item")).getContents()[0], 1);
            auction.auctionUUID = UUID.fromString(state.getString("id"));
            auction.endsAt = state.getLong("ends-at");
            auction.bidIncrement = state.getDouble("increment");
            auction.currentBid = state.getDouble("bid");
            String bidder = state.getString("bidder");
            auction.currentTopBidder = bidder == null ? null : UUID.fromString(bidder);
            auction.topBidderName = state.getString("bidder-name");
            if (!Double.isFinite(auction.currentBid) || auction.currentBid < 0 || !Double.isFinite(auction.bidIncrement) || auction.bidIncrement <= 0)
                throw new IllegalStateException("Invalid saved auction amounts; preserve journal for recovery");
            return auction;
        } catch (Exception error) { throw new IllegalStateException("Cannot safely recover auction journal", error); }
    }
    public void save(Auction auction, String phase, UUID incoming, double amount) {
        try {
            YamlConfiguration state = new YamlConfiguration();
            state.set("phase", phase);state.set("id",auction.auctionUUID.toString());
            state.set("item",Utils.toBase64(new org.bukkit.inventory.ItemStack[]{auction.item},9));
            state.set("ends-at",auction.endsAt);state.set("increment",auction.bidIncrement);
            state.set("bid",auction.currentBid);state.set("bidder",auction.currentTopBidder==null?null:auction.currentTopBidder.toString());
            state.set("bidder-name",auction.topBidderName);
            if(incoming!=null) {
                state.set("incoming",incoming.toString());state.set("incoming-amount",amount);
                state.set("incoming-balance-before",BlackMarketAuction.get().getEconomy().getBalance(Bukkit.getOfflinePlayer(incoming)));
            }
            if(auction.currentTopBidder!=null) state.set("holder-balance",BlackMarketAuction.get().getEconomy().getBalance(Bukkit.getOfflinePlayer(auction.currentTopBidder)));
            file.getParentFile().mkdirs();
            Path temp = new File(file.getParentFile(),"active-auction.tmp").toPath();
            try(FileOutputStream stream=new FileOutputStream(temp.toFile())) {
                stream.write(state.saveToString().getBytes(StandardCharsets.UTF_8));stream.getFD().sync();
            }
            Files.move(temp,file.toPath(),StandardCopyOption.ATOMIC_MOVE,StandardCopyOption.REPLACE_EXISTING);
            pending = !"active".equals(phase);
        } catch(Exception error) { pending=true;throw new IllegalStateException("Auction journal write failed; stop and reconcile escrow",error); }
    }
    public void clear() {
        try { Files.deleteIfExists(file.toPath());pending=false; }
        catch(IOException error){pending=true;throw new IllegalStateException("Cannot clear settled auction journal",error);}
    }
    public void confirmRewardPersistence(me.recolance.auction.auction.UnclaimedListing reward) {
        try(java.sql.PreparedStatement statement=me.recolance.auction.data.Database.getConnection().prepareStatement("SELECT player, item FROM expired WHERE id=?")) {
            statement.setString(1,reward.getId().toString());
            try(java.sql.ResultSet row=statement.executeQuery()) {
                if(!row.next() || !reward.getListedBy().toString().equals(row.getString("player")) ||
                        !me.recolance.auction.data.DataHandler.toString(reward.getItem()).equals(row.getString("item")))
                    throw new java.sql.SQLException("Reward persistence was not confirmed");
            }
        }catch(Exception error){throw new IllegalStateException("Reward save failed; retaining journal for reconciliation",error);}
    }
    /** Verified Essentials 2.18.2's forceSave waits for the queued user-file save.
     * Other providers require their own durability adapter before being supported. */
    public void flushEconomy(UUID uuid) {
        if(uuid==null)return;
        try {
            if(!BlackMarketAuction.get().getEconomy().getName().toLowerCase(java.util.Locale.ROOT).contains("essentials"))
                throw new IllegalStateException("Economy provider lacks a verified durability adapter");
            Plugin essentials=Bukkit.getPluginManager().getPlugin("Essentials");
            Object user=essentials.getClass().getMethod("getUser",UUID.class).invoke(essentials,uuid);
            Class<?> type=user.getClass();Field config=null;
            while(type!=null) {try{config=type.getDeclaredField("config");break;}catch(NoSuchFieldException absent){type=type.getSuperclass();}}
            if(config==null)throw new NoSuchFieldException("Essentials user config");
            config.setAccessible(true);Object value=config.get(user);
            value.getClass().getMethod("forceSave").invoke(value);
            File userFile=(File)value.getClass().getMethod("getFile").invoke(value);
            YamlConfiguration disk=new YamlConfiguration();disk.load(userFile);
            double saved=Double.parseDouble(disk.getString("money"));
            double expected=BlackMarketAuction.get().getEconomy().getBalance(Bukkit.getOfflinePlayer(uuid));
            if(Double.compare(saved,expected)!=0)throw new IOException("Saved economy balance differs from Vault balance");
            try(java.nio.channels.FileChannel channel=java.nio.channels.FileChannel.open(userFile.toPath(),StandardOpenOption.WRITE)){channel.force(true);}
        } catch(Exception error) {throw new IllegalStateException("Cannot confirm economy persistence; preserve journal for reconciliation",error);}
    }
}
