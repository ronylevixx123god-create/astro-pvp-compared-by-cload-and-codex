//
// Decompiled by Procyon v0.5.36
//

package net.astropvp.auction;

import java.io.InputStream;
import org.bukkit.util.io.BukkitObjectInputStream;
import java.io.ByteArrayInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import java.io.OutputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class PastAuction implements Serializable {
    private boolean didPlayersBid;
    private String winningBidder;
    private double winningBid;
    private String item;
    private long dateSold;

    public PastAuction(final String winningBidder, final double winningBid, final String item, final long dateSold) {
        this.didPlayersBid = (winningBidder != null);
        this.winningBidder = winningBidder;
        this.winningBid = winningBid;
        this.item = item;
        this.dateSold = dateSold;
    }

    public boolean didPlayersBid() {
        return this.didPlayersBid;
    }

    public String getWinningBidder() {
        return this.winningBidder;
    }

    public double getWinningBid() {
        return this.winningBid;
    }

    public long getDateSold() {
        return this.dateSold;
    }

    public String getItem() {
        return this.item;
    }

    @Override
    public String toString() {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream((OutputStream)outputStream);
            dataOutput.writeObject((Object)this);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e) {
            return null;
        }
    }

    public static PastAuction fromString(final String data) {
        try {
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            final BukkitObjectInputStream dataInput = new BukkitObjectInputStream((InputStream)inputStream);
            final PastAuction past = (PastAuction)dataInput.readObject();
            dataInput.close();
            return past;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
