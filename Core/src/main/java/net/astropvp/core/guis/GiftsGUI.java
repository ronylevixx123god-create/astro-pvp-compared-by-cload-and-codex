package net.astropvp.core.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;

import net.astropvp.core.AstroCore;
import net.astropvp.core.managers.AstroPlayerManager;
import net.astropvp.core.struct.AstroPlayer;
import net.astropvp.core.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GiftsGUI {

    private static final GuiItem FILLER_ITEM = new GuiItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setColor(DyeColor.GRAY).setName("").build());
    private static final ItemStack PREV_PAGE_ITEM = new ItemBuilder(Material.ARROW, "&c<- Previous Page", "&7Right-Click&7 to go to the previous page.").colorize().build();
    private static final ItemStack NEXT_PAGE_ITEM = new ItemBuilder(Material.ARROW, "&aNext Page ->", "&7Right-Click&7 to go to the next page.").colorize().build();

    @Getter
    private final PaginatedGui gui;

    public GiftsGUI(Player player) {
        AstroPlayerManager crankedUserManager = AstroCore.getInstance().getAstroPlayerManager();
        AstroPlayer crankedUser = crankedUserManager.getAstroPlayer(player.getUniqueId());
        this.gui = Gui.paginated()
                .title(Component.text("Gifts"))
                .pageSize(28)
                .rows(6)
                .disableAllInteractions()
                .create();

        this.gui.getFiller().fillBorder(FILLER_ITEM);
        this.gui.setItem(6, 3, new GuiItem(PREV_PAGE_ITEM, event -> this.getGui().previous()));
        this.gui.setItem(6, 7, new GuiItem(NEXT_PAGE_ITEM, event -> this.getGui().next()));
        if (crankedUser.getGifts() == null) {
            crankedUser.setGifts(new ArrayList<>());
        }
        for (int i = 0; i < crankedUser.getGifts().size(); i++) {
            int finalI = i;
            this.gui.addItem(new GuiItem(crankedUser.getGifts().get(i).createGuiGiftItem(), event -> {
                player.sendMessage(CC.translate("&a&l(!)&a You have opened the gift and received &f" + ItemUtils.getDisplayName(crankedUser.getGifts().get(finalI).getItemStack()) + "&a!"));
                PlayerUtil.giveOrDropProtectedItem(player, crankedUser.getGifts().get(finalI).getItemStack(), 60);
                crankedUser.getGifts().remove(finalI);
                GiftsGUI giftsGUI = new GiftsGUI(player);
                giftsGUI.getGui().open(player);
            }));
        }
    }
}
