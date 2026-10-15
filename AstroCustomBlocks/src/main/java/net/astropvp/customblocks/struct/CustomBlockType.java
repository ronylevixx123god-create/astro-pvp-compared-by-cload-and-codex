package net.astropvp.customblocks.struct;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import lombok.Getter;
import net.astropvp.customblocks.struct.impl.*;
import net.astropvp.customblocks.struct.impl.sellbots.*;
import net.astropvp.customblocks.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@Getter
public enum CustomBlockType {

    SPRING_2021_LOOTINATA("&d&lPinata", ChatColor.LIGHT_PURPLE, 0, 0,
            Arrays.asList(
                    CC.translate("&7Each hit, some treasure spills"),
                    CC.translate("&7At last, I explode, more treasure!"),
                    CC.translate(""),
                    CC.translate("&c(!) Coming Soon!")
            ), Pinata.class, true),

    SOUL_LURE("test", ChatColor.GOLD, 1000000, 1000000,
            Arrays.asList(""
            ), SoulLure.class, false),

    SOUL_GEM_GENERATOR("test", ChatColor.GOLD, 1000000, 1000000,
            Arrays.asList(""
            ), SoulGemGenerator.class, false),

    DIAMOND_METEORITE("test", ChatColor.GOLD, 1000000, 1000000,
            Arrays.asList(""
            ), DiamondMeteorite.class, false),


    DARK_MATTER("EXP Space Storage", ChatColor.GREEN, 1000000, 1000000,
            Arrays.asList(
                    CC.translate("&7Collects EXP dropped in this chunk"),
                    CC.translate("&7Right-Click to access /xpshop, providing"),
                    CC.translate("&7a &f10% Discount &7off all items."),
                    CC.translate(""),
                    CC.translate("&7You can deposit or withdraw EXP at any time"),
                    CC.translate(""),
                    CC.translate("&c(!) Coming Soon!")
            ), DarkMatter.class, true),

    TRAINING_DUMMY("test", ChatColor.GREEN, 1000000, 1000000,
            Arrays.asList(
                    CC.translate("&7")
            ), TrainingDummy.class, false),


    ENDERMAN_AUTO_SELL_BOT("Enderman Collector Booster", ChatColor.YELLOW, 1000000, 25000000,
            Arrays.asList(
                    CC.translate("&7The chunk collector in the same chunk"),
                    CC.translate("&7as this Collector Upgrade will multiply"),
                    CC.translate("&7all Enderman money accumulation by 1.2x"),
                    CC.translate(""),
                    CC.translate("&c&lNOTE: &cYou can only place 1"),
                    CC.translate("&cEnderman Collector Booster per chunk.")

            ), EndermanSB.class),

    IRON_GOLEM_AUTO_SELL_BOT("Iron Golem Collector Booster", ChatColor.YELLOW, 1000000, 50000000,
            Arrays.asList(
                    CC.translate("&7The chunk collector in the same chunk"),
                    CC.translate("&7as this Collector Upgrade will multiply"),
                    CC.translate("&7all Iron Golem money accumulation by 1.2x"),
                    CC.translate(""),
                    CC.translate("&c&lNOTE: &cYou can only place 1 Iron"),
                    CC.translate("&cGolem Collector Booster per chunk.")

            ), IronGolemSB.class),

    MUSHROOM_COW_AUTO_SELL_BOT("Mooshroom Collector Booster", ChatColor.YELLOW, 1000000, 100000000,
            Arrays.asList(
                    CC.translate("&7The chunk collector in the same chunk"),
                    CC.translate("&7as this Collector Upgrade will multiply"),
                    CC.translate("&7all Mooshroom money accumulation by 1.2x"),
                    CC.translate(""),
                    CC.translate("&c&lNOTE: &cYou can only place 1"),
                    CC.translate("&cMooshroom Collector Booster per chunk.")

            ), MushroomCowSB.class),

    GHAST_AUTO_SELL_BOT("Ghast Collector Booster", ChatColor.YELLOW, 1000000, 250000000,
            Arrays.asList(
                    CC.translate("&7The chunk collector in the same chunk"),
                    CC.translate("&7as this Collector Upgrade will multiply"),
                    CC.translate("&7all Ghast money accumulation by 1.2x"),
                    CC.translate(""),
                    CC.translate("&c&lNOTE: &cYou can only place 1 Ghast"),
                    CC.translate("&cCollector Booster per chunk.")

            ), GhastSB.class),

    SNOWMAN_GOLEM_AUTO_SELL_BOT("Snow Golem Collector Booster", ChatColor.YELLOW, 1000000, 500000000,
            Arrays.asList(
                    CC.translate("&7The chunk collector in the same chunk"),
                    CC.translate("&7as this Collector Upgrade will multiply"),
                    CC.translate("&7all Snow Golem money accumulation by 1.2x"),
                    CC.translate(""),
                    CC.translate("&c&lNOTE: &cYou can only place 1 Snow"),
                    CC.translate("&cGolem Collector Booster per chunk.")

            ), SnowGolemSB.class),



    HALLOWEEN_CANDLES("Halloween Candle", ChatColor.GOLD, 1000000, 1000000,
            Arrays.asList(
                    CC.translate("&7Chance to either not consume a"),
                    CC.translate("&7lootbox or give a guaranteed"),
                    CC.translate("&7item when opening the lootbox"),
                    CC.translate("&7in the chunk this block is placed in."),
                    CC.translate(""),
                    CC.translate("&7By successfully using this fiery"),
                    CC.translate("&7flames emmited from these candles"),
                    CC.translate("&7legend has it that priceless"),
                    CC.translate("&7lootboxes stay with the beholder")
            ), HalloweenCandle.class, false),

    THANKSGIVING_PIE("Pumpkin Pie", ChatColor.YELLOW, 1000000, 1000000, Arrays.asList(CC.translate("&7Receive all of the following"),
            CC.translate("&7buffs when standing in the same chunk."),
            "",
            CC.translate("&e&l* &f+50% Chance to Silence Enemies"),
            CC.translate("&e&l* &f+5% Outgoing Damage"),
            CC.translate("&e&l* &f+25% Monopoly Chance")
    ), ThanksgivingPie.class, false),

    TREASURE_CHEST("Treasure Chest", ChatColor.GOLD, 1000000, 1000000, Arrays.asList(CC.translate("&7Receive all of the following"),
            CC.translate("&7buffs when standing in the same chunk."),
            "",
            CC.translate("&7Increase the percentage of EXP"),
            CC.translate("&7dropped by 50% when mobs are killed"),
            CC.translate("&7in the chunk it's placed in.")
    ), TreasureChest.class, false),

    COSMIC_TROPHY("Cosmic Trophy", ChatColor.GOLD, 1000000, 1000000, Arrays.asList(
            CC.translate("&7Receive all of the following"),
            CC.translate("&7buffs when standing in the same chunk."),
            "",
            CC.translate("&710% chance to roll the Astro-Slot"),
            CC.translate("&7Bot without consuming your ticket.")
    ), CosmicTrophy.class, false),

    LEVIATHAN_TOTEM("Leviathan Totem", ChatColor.DARK_AQUA, 1000000, 1000000, Arrays.asList(
            CC.translate("&7Receive all of the following"),
            CC.translate("&7Chance to double mob spawner"),
            CC.translate("&7drops in the chunk it's placed in."),
            "",
            CC.translate("&7Any mob spawner that is lucky enough"),
            CC.translate("&7to be in the orbit of this sacred holy"),
            CC.translate("&7object will have its mobs imbued with"),
            CC.translate("&7inner looting.")
    ), LeviathanTotem.class, false),

    HALLOWEEN_CAULDRON("Cauldron", ChatColor.GOLD, 1000000, 1000000, Arrays.asList(
            CC.translate("&7Receive all of the following"),
            CC.translate("&7buffs when standing in the same chunk."),
            "",
            CC.translate("&6&l* &ePhantasm: &f+5% Outgoing Damage"),
            CC.translate("&6&l* &eStolen Mind: &f-5% Incoming Damage"),
            CC.translate("&6&l* &eSpooky Aura: &f33% Less Phoenix Cooldown")
    ), Cauldron.class, false),



    COSMIC_TOKEN_GENERATOR("Slotbot Opener", ChatColor.GREEN, 1000000, 150000, Arrays.asList(
            CC.translate("&7Roll all your slotbot tickets"),
            CC.translate("&7at once using the slotbot opener."),
            "",
            CC.translate("&7Place chests around the block"),
            CC.translate("&7to collect all the loot from the bot.")
    ), SlotOpener.class),

    SECURITY_SYSTEM("Key Opener", ChatColor.AQUA, 1000000, 150000, Arrays.asList(
            CC.translate("&7Open all your cratekeys"),
            CC.translate("&7at once using the key opener."),
            "",
            CC.translate("&7Place chests around the block"),
            CC.translate("&7to collect all the loot from the keys.")
    ), KeyOpener.class),

    BLAZE_STONE_TOTEM("Auto Mob Grinder", ChatColor.RED, 1000000, 150000, Arrays.asList(
            CC.translate("&7Automatically grinds Keys"),
            CC.translate("&7while the custom block is placed."),
            "",
            CC.translate("&7Dug up from the deep depths of"),
            CC.translate("&7the gold mines, this custom block is"),
            CC.translate("&7essential for grinding Blazes.")
            /*
            "",
            CC.translate("&c&lWARNING &7You must stay in a &f16x16&7 radius of"),
            CC.translate("&7this auto grinder block for it to keep grinding keys.")

             */
    ), AutoGrinder.class)


    ;

    private final String displayName;
    private final ChatColor color;
    private final double minePrice;
    private final double placePrice;
    private final List<String> lore;
    private final Class<? extends CustomBlock> customBlockClass;
    private final boolean enabled;

    CustomBlockType(String displayName, ChatColor color, double minePrice, double placePrice, List<String> lore, Class<? extends CustomBlock> customBlockClass, boolean enabled) {
        this.displayName = displayName;
        this.color = color;
        this.minePrice = minePrice;
        this.placePrice = placePrice;
        this.lore = lore;
        this.customBlockClass = customBlockClass;
        this.enabled = enabled;
    }

    CustomBlockType(String displayName, ChatColor color, double minePrice, double placePrice, List<String> lore, Class<? extends CustomBlock> customBlockClass) {
        this.displayName = displayName;
        this.color = color;
        this.minePrice = minePrice;
        this.placePrice = placePrice;
        this.lore = lore;
        this.customBlockClass = customBlockClass;
        this.enabled = true;
    }


    public ItemStack build(int level) {
        String title = level != 0 ? getColor() + "&l" + getDisplayName() + " &7(&fLVL " + level + "&7)": getColor() + "&l" + getDisplayName();
        ItemStack block = new ItemBuilder(Material.NOTE_BLOCK)
                .setName(title)
                .addLoreLine(getColor() + "Price to Place: &f$" + NumberUtils.formatMoney(getPlacePrice()))
                .addLoreLine(CC.translate(" &7(money will be instantly taken)"))
                .addLoreLine(getColor() + "Price to Mine: &f$" + NumberUtils.formatMoney(getMinePrice()))
                .addLoreLine(CC.translate(" &7(after a 2 hour grace timer)"))
                .addLoreLine("")
                .addLoreLines(getLore())
                .addLoreLine("")
                .addLoreLine("&7Place custom block to use!")
                .colorize()
                .build();
        NBTWrapper nbtItem = new NBTWrapper(block);
        NBTTagCompoundBuilder cosmicData = nbtItem.hasKey("cosmicData") ? new NBTTagCompoundBuilder(nbtItem.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        NBTTagCompoundBuilder cosmicData2 = cosmicData.hasKey("customBlockData") ? new NBTTagCompoundBuilder(cosmicData.getNBTBase("customBlockData")) : new NBTTagCompoundBuilder();
        nbtItem.setString("cosmicType", "custom_block");
        if (level != 0) {
            cosmicData2.setInt("level", level);
        }
        cosmicData2.setString("type", name());
        cosmicData.setNBTBase("customBlockData", cosmicData2.get());
        nbtItem.setNBTBase("cosmicData", cosmicData.get());
        return nbtItem.build();
    }


}
