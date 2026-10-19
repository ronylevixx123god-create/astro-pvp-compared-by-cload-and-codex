//
// Decompiled by Procyon v0.5.36
//

package net.astropvp.util;

import com.cosmicpvp.cosmicutils.utils.StringUtils;
import org.bukkit.ChatColor;
import java.io.OutputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import java.io.ByteArrayOutputStream;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import java.io.InputStream;
import org.bukkit.util.io.BukkitObjectInputStream;
import java.io.ByteArrayInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.bukkit.inventory.Inventory;
import java.text.NumberFormat;
import java.util.Locale;
import org.bukkit.inventory.meta.ItemMeta;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Utils
{
    public static ItemStack getFillerItem() {
        final ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
        final ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName(CC.translate("&r"));
        filler.setItemMeta(meta);
        return filler;
    }

    public static <T> String formatMoney(final T money) {
        final String str = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(money);
        return str.substring(1, str.length() - 3);
    }

    public static Inventory fromBase64(final String data) {
        try {
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            final BukkitObjectInputStream dataInput = new BukkitObjectInputStream((InputStream)inputStream);
            final Inventory inventory = Bukkit.getServer().createInventory((InventoryHolder)null, dataInput.readInt());
            for (int i = 0; i < inventory.getSize(); ++i) {
                inventory.setItem(i, (ItemStack)dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String toBase64(final Inventory inventory, final int size) {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream((OutputStream)outputStream);
            dataOutput.writeInt(size);
            for (int i = 0; i < inventory.getSize(); ++i) {
                dataOutput.writeObject((Object)inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e) {
            throw new IllegalStateException("Cannot into itemstacks!", e);
        }
    }

    public static String toBase64(final ItemStack[] is, final int size) {
        final Inventory inventory = Bukkit.createInventory((InventoryHolder)null, size);
        inventory.setContents(is);
        return toBase64(inventory, size);
    }

    public static ItemStack getItem(final String line) {
        return fromBase64(line).getContents()[0];
    }

    public static void sendCenteredMessage(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        for (final char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            }
            else if (previousCode) {
                previousCode = false;
                isBold = (c == 'l' || c == 'L');
            }
            else {
                final StringUtils.DefaultFontInfo dFI = StringUtils.DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += (isBold ? dFI.getBoldLength() : dFI.getLength());
                ++messagePxSize;
            }
        }
        final int halvedMessageSize = messagePxSize / 2;
        final int toCompensate = 154 - halvedMessageSize;
        final int spaceLength = StringUtils.DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        final StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        Bukkit.broadcastMessage(sb.toString() + message);
    }

    public static int clamp(final int val, final int min, final int max) {
        return (val < min) ? min : ((val > max) ? max : val);
    }
}
