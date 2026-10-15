package org.demonicpvp.demonicruins.command;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.demonicpvp.demonicruins.manager.RuinsRewardManager;

@CommandAlias("ruins")
public class RuinsCommand extends BaseCommand {
    private final RuinsRewardManager ruinsRewardManager;
    private final RuinsGUI ruinsGUI;

    public RuinsCommand(RuinsRewardManager rewardManager) {
        this.ruinsGUI = new RuinsGUI();
        this.ruinsRewardManager = rewardManager;
    }

    @Default
    public void onDefault(Player player) {
        this.ruinsGUI.getInventory().open(player);
    }

/*    @Subcommand("reload")
    @CommandPermission("demonicruins.reloadloot")
    public void onReload(CommandSender sender) {
        this.ruinsRewardManager.reloadLootboxes();
        CommonsUtil.sendMessage(sender, "&a&l(!) &aLoot tables reloaded!");
    }*/

    private static class RuinsGUI implements InventoryProvider {
        private final SmartInventory inventory = SmartInventory.builder()
                .title("Cosmic Ruins")
                .size(1, 9)
                .provider(this)
                .manager(CosmicUtils.get().getInventoryManager())
                .type(InventoryType.CHEST)
                .id("ruins-gui")
                .build();

        @Override
        public void init(Player player, InventoryContents contents) {
            contents.fill(ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&r", 1, 7)));

            contents.set(0, 4, ClickableItem.of(
                    CommonsUtil.createItem(
                            Material.PRISMARINE_SHARD,
                            "&3&lRuins",
                            "&7The Ruins is an ancient, dangerous &cPvP-enabled",
                            "&7underworld scattered with biblical sea creatures",
                            "&7and is the site of ocean legends.",
                            "",
                            "&7Guardians, Elite Guardians and Elder Elite",
                            "&7Guardians naturally spawn throughout the Ruins",
                            "&7powerful abilities that must be mastered in",
                            "&7order to obtain rare and exclusive items.",
                            "",
                            "&7Obtain a &3&lLeviathan Boss &7summon egg by placing",
                            "&79x Ancient Scrolls into a crafting table.",
                            "&7Locate the ancient temple in the ruins to summon",
                            "&7this formidable sea monster, which has some of",
                            "&7the best loot, including pieces of the infamous",
                            "&7\"Ancient\" armor set.",
                            "",
                            "&7Click to teleport to /ruins."
                    ), event -> {
                        Bukkit.dispatchCommand(player, "warp ruins");
                    }
            ));
        }

        public SmartInventory getInventory() {
            return this.inventory;
        }
    }
}
