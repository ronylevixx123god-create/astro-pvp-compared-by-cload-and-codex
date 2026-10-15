/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.CosmicUtils
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.JSONMessage
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.google.common.collect.Lists
 *  org.arkhamnetwork.permissions.ArkhamPermissions
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.cosmicpvp.CosmicGlobalKits.libs;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.JSONMessage;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKit;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitData;
import me.cosmicpvp.CosmicGlobalKits.struct.gkits.HeroicArenaChampion;
import me.cosmicpvp.CosmicGlobalKits.struct.gkits.HeroicCannoner;
import me.cosmicpvp.CosmicGlobalKits.struct.gkits.HeroicCosmonaut;
import me.cosmicpvp.CosmicGlobalKits.struct.gkits.HeroicGrimReaper;
import me.cosmicpvp.CosmicGlobalKits.struct.gkits.HeroicLoki;
import me.cosmicpvp.CosmicGlobalKits.utils.GlobalCooldowns;
import me.cosmicpvp.CosmicGlobalKits.utils.ItemUtils;
import org.arkhamnetwork.permissions.ArkhamPermissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public enum GlobalKits {
    Spooky("spooky", 259200000L, Material.PUMPKIN, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lSpooky Kit", "", "&f&l* &6&lHeroic Headless Blade of Horror", "&f&l* &6&lHeroic Grim Reaper Shroud", "&f&l* &eHeroic Headless Chestplate of Nightmares", "&f&l* &eHeroic Headless Leggings of Nightmares", "&f&l* &eHeroic Headless Boots of Nightmares", "", "&7Petrify your enemies in fear.", "&6* Halloween 2015"),
    HYPER("hyper", 259200000L, Material.FEATHER, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lHyperdrive Kit", "", "&f&l* &6&lHeroic Hermes Blade of Swiftness", "&f&l* &6&lHeroic Hyper Helmet of Hermes", "&f&l* &eHeroic Hyper Chestplate of Mercury", "&f&l* &eHeroic Hyper Leggings of Nike", "", "&7The most agile equipment in the galaxy.", "&6* Thanksgiving 2015"),
    DESTROYER("destroyer", 259200000L, Material.BLAZE_POWDER, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lDestroyer Kit", "", "&f&l* &6&lHeroic Destructive Bow of Oblivion", "&f&l* &6&lHeroic Demon Stalker Greathelm", "&f&l* &eHeroic Demon Stalker Harness", "&f&l* &eHeroic Demon Stalker Greaves", "&f&l* &eHeroic Boots of the Destroyer", "", "&7Wreak havoc and destruction on the galaxy.", "&6* Heart of Darkness"),
    GUARDIAN("guardian", 259200000L, Material.EYE_OF_ENDER, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lGuardian Kit", "", "&f&l* &6&lHeroic Divine Bow of Valhalla", "&f&l* &6&lHeroic Halo of the Guardian", "&f&l* &eHeroic Sandals of the Divine", "&f&l* &eHeroic Divine Guardian Chestplate", "&f&l* &eHeroic Guardian Leggings", "", "&7Protect and serve the galaxy you love.", "&6* Heart of Darkness"),
    DIABLO("diablo", 259200000L, Material.ENDER_PORTAL_FRAME, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lDiablo Kit", "", "&f&l* &6&lHeroic Executioner's Hood", "&f&l* &6&lHeroic Curse of Raven", "&f&l* &eHeroic Death Cloak", "&f&l* &eHeroic Death Greaves", "&f&l* &eHeroic Death Grave Stompers", "", "&7Summon fourth the power of the void.", "&6* Void Planet Release"),
    VALENTINE("valentine", 259200000L, Material.RED_ROSE, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lValentines Kit", "", "&f&l* &6&lHeroic Knife of Love", "&f&l* &6&lHeroic Short shorts", "&f&l* &6&lHeroic 6-pack Abs", "&f&l* &eHeroic Valtine Day Hat", "&f&l* &eHeroic Bunny Slippers", "", "&7The ulitimate support kit!", "&6* Valentines 2015"),
    ARCANE("arcane", 259200000L, Material.ENCHANTMENT_TABLE, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lArcane Kit", "", "&f&l* &6&lApollo's Longbow of the Abyss", "&f&l* &6&lHeroic Arcanist Hat", "&f&l* &6&lHeroic Arcanist Elf Shoes", "&f&l* &eArcanist Robeset", "&f&l* &eArcanist Pantaloons", "", "&7Embrace the power of magick!", "&6* Magic Planet Release"),
    GLADIATOR("gladiator", 259200000L, Material.DIAMOND_SWORD, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lGladiator Kit", "", "&f&l* &6&lAres Greatsword of Conquering", "&f&l* &6&lArena Champion Mask", "&f&l* &6&lHeroic Gladiator Platemail", "&f&l* &eHeroic Gladiator Platelegs", "&f&l* &eHeroic Gladiator Warboots", "", "&7Become a ruthless warlord!", "&6* Arena Update"),
    TRICKSTER("trickster", 259200000L, Material.APPLE, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lTrickster Kit", "", "&f&l* &6&lHand of Harambe", "&f&l* &6&lFarcasting Bow of Nootaikok", "", "&7Troll the galaxy... with Harambe.", "&6* April Fools 2015"),
    TEMPLAR("templar", 259200000L, Material.GOLDEN_APPLE, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lTemplar Kit", "", "&f&l* &6&lHeroic Platemail of Aegis the Protector", "&f&l* &6&lHeroic Blade of the Knights Order", "&f&l* &eHeroic Halo of Light", "&f&l* &eHeroic Leggings of Replenishment", "&f&l* &eHeroic Sandals of the Angelic", "", "&7Protect your allies to your last breath.", "&6* Easter 2015"),
    VIKING("viking", 259200000L, Material.DIAMOND_AXE, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lViking Kit", "", "&f&l* &6&lHeroic Viking Skull Chainmail", "&f&l* &6&lAxe of the Dwarvish Gods", "&f&l* &eHeroic Spiked Viking Helmet", "&f&l* &eHeroic Nordic Iron Forged Leggings", "&f&l* &eHeroic Steeltoe Leather Boots", "", "&7Bloodthirsty, ruthless, axe-loving vikings.", "&6* Earth Day 2015"),
    Tinkermaster("tinkermaster", 259200000L, Material.BREWING_STAND_ITEM, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lTinkermaster Kit", "", "&f&l* &6&lGlycon's Bow", "&f&l* &6&lHeroic Clockwork Armor", "&f&l* &e&lHeroic Gnomish Tinkerer Hat", "&f&l* &e&lHeroic Pants of Steel", "&f&l* &e&lHeroic Gnome Escapist Shoes", "", "&7Cunning, mischievous, legendary puppet master.", "&6* Bounty Hunter Update"),
    Admiral("admiral", 259200000L, Material.NETHER_STAR, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lCosmic Admiral Kit", "", "&f&l* &6&lExcalibur", "&f&l* &6&lHeroic Cosmonaut Biotic Legs", "&f&l* &6&lHeroic Boots of the Void Galaxy", "&f&l* &eHeroic Cosmic Admiral Military Jacket", "&f&l* &eHeroic Cosmic Admiral Military Hat", "", "&7The most feared military in the universe.", "&6* Galaxy Update"),
    Party("partyanimal", 259200000L, Material.CAKE, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lParty Animal Kit", "", "&f&l* &6&lThe Jug of Dionysus", "&f&l* &6&lCosmic Year #2 Party Hat", "", "&7Fight hard, party even harder.", "&6* Cosmic #1 Year Anniversary"),
    Enchantment("enchantment", 259200000L, Material.BOOK, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lEnchantment Kit", "", "&7Build your own godly custom sets!", "&6* Dream Planet Release"),
    GrandMaster("grandmaster", 259200000L, Material.EXP_BOTTLE, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lGrandmaster Kit", "", "&f&l* &6&lHeroic Nightmare Killer", "&f&l* &6&lHeroic Shroud of Judgement", "&f&l* &6&lHeroic Sandals of Silence", "&f&l* &eHeroic Cloak of the Grandmaster", "&f&l* &eHeroic Leather Combat Leggings", "&f&l* &e10-15k XP Bottle", "&f&l* &eBlack Scroll &nOR&e Ult. Magic Dust", "", "&7Steal the knowledge of your enemies!", "&6* Castaway Planet Release"),
    Voidwalker("voidwalker", 259200000L, Material.ENDER_PEARL, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lVoidwalker Kit", "", "&f&l* &6&lHeroic Chaos Executioner", "&f&l* &6&lHeroic Erebus' Sandals of the Void", "", "&7Walk the path of the true void.", "&6* Demon Planet Release"),
    Masterbuilder("masterbuilder", 259200000L, Material.DIAMOND_PICKAXE, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lMaster Builder Kit", "", "&f&l* &6&lHeroic Pickaxe of Hephaestus", "&f&l* &6&lHeroic Shiva's Excavator", "", "&7Construct citadels worthy of gods.", "&6* Tool Enchant Update"),
    Independence("independence", 259200000L, Material.FIREWORK, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lIndependence Kit", "", "&f&l* &6&lRuneblade of Liber", "&f&l* &6&lGuy Fawkes Mask", "&f&l* &eAnarchy Boots", "&f&l* &eChestplate of Chaos", "", "&7Break free from your chains.. heroically!", "&6* July 4th 2015/2017"),
    Duelist(new HeroicArenaChampion(), "duelist", 259200000L, Material.DIAMOND, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lArena Champion Kit", "&f&l* &6&lCrown of the Colosseum King", "&f&l* &6&lShadowstone Rapier of Kronos", "&f&l* &e&lA chance of...", "   &f- &c&lPhantom Scythe", "", "&7Prove that no mortal can defeat you.", "&6* Cosmic Dueling"),
    Butcher("butcher", 259200000L, Material.RAW_BEEF, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lButcher Kit", "", "&f&l* &c&lThe Butcher's Cleaver", "&f&l* &6&lBlood-soaked Apron of the Butcher", "&f&l* &e64x Halloween Candy", "", "&7A monster from the depths of the Nether.", "&6* Halloween 2016"),
    Cannoner(new HeroicCannoner(), "cannoner", 259200000L, Material.TNT, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lCannoner Kit", "", "&f&l* &6&lHeroic Crystal Bow of Athena", "&f&l* &6&lCustom TNT", "&f&l* &6&lCustom Creepers", "&f&l* &6&lRanger Bow", "&f&l* &eSponges", "&f&l* &eDispensers", "", "&7No amount of defenses can stop you.", "&6* Castle Planet Resest"),
    Cosmonaut(new HeroicCosmonaut(), "cosmonaut", 324000000L, Material.DIAMOND_CHESTPLATE, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &b&lCosmonaut &d&lKit", "&f&l* &6&lHeroic Armor Pieces", "&f&l* &6&lHeroic Enchantments", "", "&7Reinventing the definition of a \"godset\".", "&6* Immortal Enchant Release"),
    Paladin("paladin", 324000000L, Material.GOLD_HELMET, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lPaladin Kit", "", "&f&l* &6&lHeroic Divine Paladin Chestplate", "&f&l* &6&lHeroic Platelegs of Athena", "&f&l* &eHeroic Shroud of the Holy Order", "&f&l* &eHeroic Boots of the Holy Order", "&f&l* &e2-3x Randomization Scrolls", "&f&l* &e0-1x Enchantment Orb", "", "&7The best offense is a perfect defense.", "&6* Ice Planet Reset"),
    Warlock("warlock", 324000000L, Material.DRAGON_EGG, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lWarlock Kit", "", "&f&l* &c&lThe Fiend", "&f&l* &6&lMaster Warlock Hat", "&f&l* &6&lGhostly Call of the Banshee", "&f&l* &eHeroic Warlock Robe", "&f&l* &eHeroic Warlock Bottoms", "&f&l* &e32x Enderpearls", "&f&l* &e9x Mystery Dust", "", "&7Become a master of the dark arts.", "&6* Enchantment Tracking"),
    Loki(new HeroicLoki(), "loki", 324000000L, Material.GOLD_AXE, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lLoki Kit", "&7The Norse god of trickery and deceit.", "&6* Soul Trap Release"),
    GrimReaper(new HeroicGrimReaper(), "grimreaper", 324000000L, Material.SKULL_ITEM, "&b&k!!!&r&f&l&nHeroic&d&k!!!&r &f&lGrim Reaper Kit", "", "&f&l* &c&lGrim Reaper Scythe", "&f&l* &6&lHood of the Grim Reaper", "&f&l* &6&lTattered Robes of the Reaper", "&f&l* &e100-500x Souls", "&f&l* &e16-32x Halloween Candy", "&f&l* &e&lA chance of...", "   &f- &ePhantom Armor Piece", "   &f- &eDungeon Portal", "   &f- &eHoly White Scroll", "   &f- &eSoul Pearl", "", "&7Become the master of life and death.", "&6* Lost Kingdoms Release 2018");

    private String kit_name;
    private long cooldown_ms;
    private Material item_icon;
    private String item_name;
    private List<String> item_lore;
    private GlobalKit newKitInstance;

    private GlobalKits(GlobalKit instance, String name, long cooldown, Material icon, String kit_name, String ... kit_lore) {
        this.newKitInstance = instance;
        this.kit_name = name;
        this.cooldown_ms = cooldown;
        this.item_icon = icon;
        this.item_name = ChatColor.translateAlternateColorCodes((char)'&', (String)kit_name);
        this.item_lore = new ArrayList<String>();
        for (String s : kit_lore) {
            this.item_lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)s));
        }
    }

    private GlobalKits(String name, long cooldown, Material icon, String kit_name, String ... kit_lore) {
        this.kit_name = name;
        this.cooldown_ms = cooldown;
        this.item_icon = icon;
        this.item_name = ChatColor.translateAlternateColorCodes((char)'&', (String)kit_name);
        this.item_lore = new ArrayList<String>();
        for (String s : kit_lore) {
            this.item_lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)s));
        }
    }

    public String getKitName() {
        return this.kit_name;
    }

    public long getCooldown(Player pl) {
        return this.cooldown_ms;
    }

    public String getItemName() {
        return this.item_name;
    }

    public List<String> getItemLore() {
        return this.item_lore;
    }

    public Material getItemIcon() {
        return this.item_icon;
    }

    public static GlobalKits getGKit(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        if (!(item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore())) {
            return null;
        }
        NBTWrapper wrapper = new NBTWrapper(item);
        String gkitString = wrapper.getString("globalKit");
        if (gkitString != null && !gkitString.isEmpty()) {
            try {
                return GlobalKits.valueOf(gkitString);
            }
            catch (Exception e) {
                Bukkit.getLogger().info("Unable to parse GlobalKit from nbt: " + gkitString);
            }
        }
        return null;
    }

    public boolean unlockOrLevelUp(Player player) {
        if (this.hasUnlockCooldown(player)) {
            return false;
        }
        if (!this.hasAccess(player)) {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("pex user " + player.getName() + " add " + this.getPermissionString() + " " + CosmicUtils.get().getServerName()));
            player.sendMessage(CC.AquaB + "(!) You've UNLOCKED the " + this.getItemName() + CC.AquaB + " GKIT!");
            player.sendMessage((Object)ChatColor.GRAY + "Redeem your new kit in the /gkit menu!");
            Bukkit.getScheduler().runTaskLater((Plugin)CosmicGlobalKits.getPlugin(), () -> ArkhamPermissions.getInstance().getPermissionManager().reloadPlayer(player), 15L);
            Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(player, "GKIT_LEVELUP", new Object[]{this, 0}));
            return true;
        }
        player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You already have the " + this.getItemName() + (Object)ChatColor.RED + " unlocked!");
        return false;
    }

    public boolean hasUnlockCooldown(Player player) {
        String localKey = "gkit_unlocked_" + this.name();
        if (player.hasMetadata(localKey) && ((MetadataValue)player.getMetadata(localKey).get(0)).asLong() > System.currentTimeMillis()) {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "Please wait for your previously applied /gkit to fully apply!");
            return true;
        }
        return false;
    }

    public ItemStack createMenuItem(Map<GlobalKits, GlobalKitData> kitData, Player pl, boolean unlockString) {
        GlobalKitData globalKitData;
        long cd = GlobalCooldowns.getCooldownMS(pl, this);
        String cd_string = "";
        if (cd > 0L) {
            cd_string = GlobalCooldowns.getCooldownString((int)(cd / 1000L));
        }
        String kit_status = GlobalKits.canUseKit(pl, this) ? ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "UNLOCKED" : ChatColor.RED.toString() + ChatColor.BOLD.toString() + "LOCKED";
        String cooldown_line = "";
        if (cd > 0L) {
            cooldown_line = ChatColor.YELLOW.toString() + "Cooldown over in .. " + cd_string;
        } else if (unlockString) {
            cooldown_line = GlobalKits.canUseKit(pl, this) ? (Object)ChatColor.GRAY + "Click to receive kit." : (Object)ChatColor.GRAY + "Unlock at " + (Object)ChatColor.UNDERLINE + CosmicGlobalKits.getPlugin().getConfig().getString("store_url");
        }
        ItemStack is = ItemUtils.buildItem(cd > 0L ? Material.STAINED_GLASS_PANE : this.getItemIcon(), cd > 0L ? (short)4 : 0, this.getItemName(), "");
        int kitTier = 0;
        if (kitData != null && (globalKitData = kitData.get((Object)this)) != null) {
            kitTier = globalKitData.getKitTier();
        }
        ArrayList lore = Lists.newArrayList(new String[]{CC.YellowB + "Kit Tier: " + CC.Yellow + JSONMessage.getNumber((int)kitTier) + CC.YellowB + " / " + CC.Yellow + "III"});
        List<String> gkitLore = this.getItemLore();
        if (gkitLore.size() == 0 || !gkitLore.get(0).trim().isEmpty()) {
            lore.add("");
        }
        lore.addAll(gkitLore);
        lore.add("");
        lore.add(kit_status);
        lore.add(cooldown_line);
        if (!GlobalKits.canUseKit(pl, this) && unlockString) {
            lore.add((Object)ChatColor.GRAY + "Right-Click to preview kit.");
        }
        ItemMeta im = is.getItemMeta();
        im.setLore((List)lore);
        is.setItemMeta(im);
        NBTWrapper wrapper = new NBTWrapper(is);
        if (is.getItemMeta().getDisplayName().contains("Heroic")) {
            wrapper.setGlowing();
        }
        return wrapper.setString("globalKit", this.name()).build();
    }

    public String getPermissionString() {
        return "cosmickits.kit." + this.getKitName();
    }

    public boolean hasAccess(Player player) {
        return player.hasPermission(this.getPermissionString());
    }

    public static boolean canUseKit(Player pl, GlobalKits gkit) {
        return gkit.hasAccess(pl);
    }

    public GlobalKit getNewKitInstance() {
        return this.newKitInstance;
    }
}

