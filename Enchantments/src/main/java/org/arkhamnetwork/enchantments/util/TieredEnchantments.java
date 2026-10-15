package org.arkhamnetwork.enchantments.util;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicCustomEnchantment;
import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicLuck;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public enum TieredEnchantments {
    blackout(8),
    treasure(8),
    neutralize(8),
    webwalker(8),
    markofthebeast(8),
    horrify(8),
    feigndeath(8),
    poltergeist(8),
    demonicgateway(8),
    keygrind(8),
    coins(8),
    mortalcoil(8),
    rotanddecay(8),
    soulsiphon(8),
    tray(5),
    permafrost(8),
    deathpact(8),
    tombstone(8),
    chainlifesteal(8),
    discombobulate(8),
    autosell(8),
    explosivesexpert(8),
    lavastrider(8),
    planetarydeathbringer(7),
    divineenlighted(7),
    godlyoverload(7),
    masterinquisitive(7),
    masterblacksmith(7),
    nocturnalinsomnia(7),
    martyrvalor(7),
    holyaegis(7),
    maliciouslycorrupt(7),
    perfectsolitude(7),
    roboticruse(7),
    vampiricdevour(7),
    bewitchedhex(7),
    deadlydisarmor(7),
    deathcoffin(5),
    unrestrainedenrage(7),
    brutalbarbarian(7),
    bloodydeepwounds(7),
    lethalsniper(7),
    vengefuldiminish(7),
    shadowassassin(7),
    atomicdetonate(7),
    alienimplants(7),
    demoniclifesteal(7),
    etherealdodge(7),
    paladinarmored(7),
    infiniteluck(7),
    deepbleed(7),
    mightycleave(7),
    guidedrocketescape(7),
    bidirectionalteleportation(7),
    realityinversion(7),
    titantrap(7),
    reflectiveblock(7),
    permanentexecute(7),
    ghostlyghost(7),
    heroicenchantreflect(7),
    polymorphicmetaphysical(7),
    epidemiccarrier(7),
    mightycactus(7),
    reinforcedtank(7),
    extremeinsanity(7),
    naturewrath(6),
    divineimmolation(6),
    immortal(6),
    teleblock(6),
    paradox(6),
    soultrap(6),
    phoenix(6),
    herokiller(6),
    sabotage(6),
    rogue(6),
    enchantreflect(5),
    deathbringer(5),
    enlighted(5),
    gears(5),
    overload(5),
    disarmor(5),
    lifesteal(5),
    rage(5),
    shiva(5),
    drunk(5),
    clarity(5),
    devour(5),
    bloodlust(5),
    destruction(5),
    protection(5),
    sniper(5),
    inquisitive(5),
    killaura(5),
    exterminator(5),
    barbarian(5),
    insanity(5),
    inversion(5),
    doublestrike(5),
    bloodlink(5),
    blacksmith(5),
    aegis(5),
    deathgod(5),
    silence(5),
    leadership(5),
    armored(5),
    antigank(5),
    diminish(5),
    bossslayer(5),
    hex(5),
    avengingangel(4),
    obsidianshield(4),
    bleed(4),
    cleave(4),
    piercing(4),
    detonate(4),
    arrowlifesteal(4),
    lucky(4),
    guardians(4),
    blessed(4),
    enderwalker(4),
    hellfire(4),
    tank(4),
    arrowdeflect(4),
    block(4),
    creeperarmor(4),
    unfocus(4),
    longbow(4),
    heavy(4),
    enrage(4),
    spirits(4),
    sticky(4),
    pacify(4),
    dimensionrift(4),
    marksman(4),
    arrowbreak(4),
    assassin(4),
    dominate(4),
    ghost(4),
    eagleeye(4),
    disintegrate(4),
    fuse(4),
    valor(4),
    iceaspect(4),
    implants(4),
    dodge(4),
    metaphysical(4),
    angelic(4),
    ragdoll(4),
    corrupt(4),
    snare(3),
    voodoo(3),
    springs(3),
    antigravity(3),
    shockwave(3),
    cactus(3),
    vampire(3),
    teleportation(3),
    infernal(3),
    poisoned(3),
    execute(3),
    trap(3),
    stormcaller(3),
    smokebomb(3),
    undeadruse(3),
    trickster(3),
    rocketescape(3),
    pummel(3),
    spiritlink(3),
    frozen(3),
    poison(3),
    solitude(3),
    hijack(3),
    blind(3),
    wither(3),
    venom(3),
    paralyze(3),
    repairguard(3),
    greatsword(3),
    demonforged(3),
    hardened(3),
    shackle(3),
    farcast(3),
    resilience(3),
    reforged(3),
    molten(2),
    curse(2),
    endershift(2),
    commander(2),
    plaguecarrier(2),
    lifebloom(2),
    deepwounds(2),
    ravenous(2),
    berserk(2),
    featherweight(2),
    cowification(2),
    explosive(2),
    revoke(8),
    obsidiandestroyer(2),
    selfdestruct(2),
    telepathy(2),
    nimble(2),
    nutrition(2),
    virus(2),
    skillswipe(2),
    training(2),
    skilling(2),
    targettracking(1),
    glowing(1),
    lightning(1),
    confusion(1),
    insomnia(1),
    thunderingblow(1),
    obliterate(1),
    healing(1),
    headless(1),
    decapitation(1),
    haste(1),
    epicness(1),
    oxygenate(1),
    autosmelt(1),
    experience(1),
    combatmedic(4),
    overwhelm(5),
    righteousantigank(7),
    divinelight(8),
    soulmaster(8),
    openingcombo(8),
    bitteroverwhelm(7),
    aquatic(1);

    private static List<CustomEnchantment> commonEnchants;
    private static List<CustomEnchantment> uncommonEnchants;
    private static List<CustomEnchantment> rareEnchants;
    private static List<CustomEnchantment> mutatedEnchants;
    private static List<CustomEnchantment> ultraRareEnchants;
    private static List<CustomEnchantment> legendaryEnchants;
    private static List<CustomEnchantment> soulEnchants;
    private static List<CustomEnchantment> heroicEnchants;
    private static HashSet<CustomEnchantment> retiredEnchantments;
    private static HashSet<CustomEnchantment> heroicEnchantments;
    private static List<CustomEnchantment> masteryEnchants;

    static {
        TieredEnchantments.commonEnchants = new ArrayList<CustomEnchantment>();
        TieredEnchantments.uncommonEnchants = new ArrayList<CustomEnchantment>();
        TieredEnchantments.rareEnchants = new ArrayList<CustomEnchantment>();
        TieredEnchantments.ultraRareEnchants = new ArrayList<CustomEnchantment>();
        TieredEnchantments.legendaryEnchants = new ArrayList<CustomEnchantment>();
        TieredEnchantments.mutatedEnchants = new ArrayList<CustomEnchantment>();
        TieredEnchantments.soulEnchants = new ArrayList<CustomEnchantment>();
        TieredEnchantments.heroicEnchants = new ArrayList<CustomEnchantment>();
        TieredEnchantments.masteryEnchants = new ArrayList<CustomEnchantment>();
        TieredEnchantments.retiredEnchantments = new HashSet<CustomEnchantment>(Arrays.asList(EnchantmentAPI.getEnchantment("Confusion"), EnchantmentAPI.getEnchantment("Nutrition"), EnchantmentAPI.getEnchantment("Molten"), EnchantmentAPI.getEnchantment("Infernal"), EnchantmentAPI.getEnchantment("Ravenous"), EnchantmentAPI.getEnchantment("Snare"), EnchantmentAPI.getEnchantment("Infernal"), EnchantmentAPI.getEnchantment("Frozen"), EnchantmentAPI.getEnchantment("Cowification"), EnchantmentAPI.getEnchantment("Oxygenate")));
        TieredEnchantments.heroicEnchantments = new HashSet<CustomEnchantment>(Arrays.asList(EnchantmentAPI.getEnchantment("Godly Overload"), EnchantmentAPI.getEnchantment("Divine Enlighted"), EnchantmentAPI.getEnchantment("Planetary Deathbringer"), EnchantmentAPI.getEnchantment("Master Inquisitive"), EnchantmentAPI.getEnchantment("Master Blacksmith"), EnchantmentAPI.getEnchantment("Lethal Sniper"), EnchantmentAPI.getEnchantment("Vengeful Diminish"), EnchantmentAPI.getEnchantment("Shadow Assassin"), EnchantmentAPI.getEnchantment("Atomic Detonate")));
        for (final CustomEnchantment enchant : EnchantmentAPI.getEnchantments()) {
            if (enchant instanceof HeroicCustomEnchantment) {
                TieredEnchantments.heroicEnchantments.add(enchant);
            }
        }
    }

    int enchantTier;

    private TieredEnchantments(final int tier) {
        this.enchantTier = tier;
    }

    public static int getEnchantmentTier(final String enchantmentName) {
        try {
            return valueOf(enchantmentName.toLowerCase().replace("_", "").replace(" ", "")).enchantTier;
        } catch (Exception err) {
            return 0;
        }
    }

    public static List<String> getAllEnchantmentNamesInTier(final int tier) {
        final List<String> enchantmentNames = new ArrayList<String>();
        for (final CustomEnchantment ce : EnchantmentAPI.getEnchantments()) {
            if (getEnchantmentTier(ce.name()) == tier) {
                enchantmentNames.add(ce.name());
            }
        }
        return enchantmentNames;
    }

    private static List<CustomEnchantment> getEnchantmentsTierCacheList(final int tier) {
        if (tier == 1) {
            return TieredEnchantments.commonEnchants;
        }
        if (tier == 2) {
            return TieredEnchantments.uncommonEnchants;
        }
        if (tier == 3) {
            return TieredEnchantments.rareEnchants;
        }
        if (tier == 4) {
            return TieredEnchantments.ultraRareEnchants;
        }
        if (tier == 5) {
            return TieredEnchantments.legendaryEnchants;
        }
        if (tier == 6) {
            return TieredEnchantments.soulEnchants;
        }
        if (tier == 7) {
            return TieredEnchantments.heroicEnchants;
        }
        if (tier == 8) {
            return TieredEnchantments.masteryEnchants;
        }
        if (tier == 9) {
            return TieredEnchantments.mutatedEnchants;

        }
        return new ArrayList<CustomEnchantment>();
    }

    public static List<CustomEnchantment> getAllEnchantmentsInTier(final int tier) {
        final List<CustomEnchantment> enchantments = getEnchantmentsTierCacheList(tier);
        if (enchantments.size() > 0) {
            return enchantments;
        }
        for (final CustomEnchantment ce : EnchantmentAPI.getEnchantments()) {
            if (getEnchantmentTier(ce.name()) == tier && !TieredEnchantments.retiredEnchantments.contains(ce) && (tier == 7 || !TieredEnchantments.heroicEnchantments.contains(ce))) {
                enchantments.add(ce);
            }
        }
        return enchantments;
    }

    public static CustomEnchantment getRandomEnchantment(final int tier) {
        final List<CustomEnchantment> enchants = getAllEnchantmentsInTier(tier);
        final CustomEnchantment ce = enchants.get(new Random().nextInt(enchants.size()));
        if (TieredEnchantments.retiredEnchantments.contains(ce) || (TieredEnchantments.heroicEnchantments.contains(ce) && tier != 7)) {
            return getRandomEnchantment(tier);
        }
        if (tier == 7 && ce instanceof HeroicLuck && Math.random() <= 0.5) {
            return getRandomEnchantment(tier);
        }
        return ce;
    }

    public static ItemStack getRandomEnchantmentBook(final int tier) {
        final CustomEnchantment ce = getRandomEnchantment(tier);
        if (TieredEnchantments.retiredEnchantments.contains(ce) || (tier != 7 && TieredEnchantments.heroicEnchantments.contains(ce))) {
            return getRandomEnchantmentBook(tier);
        }
        if (ce.equals(EnchantmentAPI.getEnchantment("Revoke")) || ce.equals(EnchantmentAPI.getEnchantment("Rogue"))) {
            return getRandomEnchantmentBook(tier);
        }
        return EnchantmentBook.getEnchantmentBook(ce, ce.getMaxLevel(), 100, 0);
    }

    public static int getBookTier(ItemStack i) {
        if (i == null || !i.hasItemMeta() || !i.getItemMeta().hasDisplayName())
            return 0;
        String displayName = i.getItemMeta().getDisplayName();
        if (displayName.startsWith(ChatColor.WHITE.toString()))
            return 1;
        if (displayName.startsWith(ChatColor.GREEN.toString()))
            return 2;
        if (displayName.startsWith(ChatColor.AQUA.toString()))
            return 3;
        if (displayName.startsWith(ChatColor.YELLOW.toString()))
            return 4;
        if (displayName.startsWith(ChatColor.GOLD.toString()))
            return 5;
        if (displayName.startsWith(ChatColor.RED.toString()))
            return 6;
        if (displayName.startsWith(ChatColor.LIGHT_PURPLE.toString()))
            return 7;
        if (displayName.startsWith(ChatColor.DARK_RED.toString()))
            return 8;
        return 0;
    }

    public static ChatColor getTierColor(CustomEnchantment ce) {
        int tier = getEnchantmentTier(ce.name());
        if (tier == 1)
            return ChatColor.GRAY;
        if (tier == 2)
            return ChatColor.GREEN;
        if (tier == 3)
            return ChatColor.AQUA;
        if (tier == 4)
            return ChatColor.YELLOW;
        if (tier == 5)
            return ChatColor.GOLD;
        if (tier == 6)
            return ChatColor.RED;
        if (tier == 7)
            return ChatColor.LIGHT_PURPLE;
        if (tier == 8)
            return ChatColor.DARK_RED;
        return ChatColor.DARK_RED;
    }

    public static boolean isMysteryEnchantmentBook(ItemStack i) {
        return (i != null && i.getType() == Material.BOOK && i.hasItemMeta() && i.getItemMeta().hasLore() && ((String) i.getItemMeta().getLore().get(0)).equals(ChatColor.GRAY + "Examine to recieve a random"));
    }

    public static ItemStack getMysteryEnchantmentBook(int tier) {
        if (tier == 1)
            return ItemUtils.buildItem(Material.BOOK, (short) 0, ChatColor.WHITE + "Simple Enchantment Book" + ChatColor.GRAY + " (Right Click)", new String[]{ChatColor.GRAY + "Examine to recieve a random", ChatColor.WHITE + "simple" + ChatColor.GRAY + " enchantment book."});
        if (tier == 2)
            return ItemUtils.buildItem(Material.BOOK, (short) 0, ChatColor.GREEN + "Unique Enchantment Book" + ChatColor.GRAY + " (Right Click)", new String[]{ChatColor.GRAY + "Examine to recieve a random", ChatColor.GREEN + "unique" + ChatColor.GRAY + " enchantment book."});
        if (tier == 3)
            return ItemUtils.buildItem(Material.BOOK, (short) 0, ChatColor.AQUA + "Elite Enchantment Book" + ChatColor.GRAY + " (Right Click)", new String[]{ChatColor.GRAY + "Examine to recieve a random", ChatColor.AQUA + "elite" + ChatColor.GRAY + " enchantment book."});
        if (tier == 4)
            return ItemUtils.buildItem(Material.BOOK, (short) 0, ChatColor.YELLOW + "Ultimate Enchantment Book" + ChatColor.GRAY + " (Right Click)", new String[]{ChatColor.GRAY + "Examine to recieve a random", ChatColor.YELLOW + "ultimate" + ChatColor.GRAY + " enchantment book."});
        if (tier == 5)
            return ItemUtils.buildItem(Material.BOOK, (short) 0, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Legendary Enchantment Book" + ChatColor.GRAY + " (Right Click)", new String[]{ChatColor.GRAY + "Examine to recieve a random", ChatColor.GOLD.toString() + "legendary" + ChatColor.GRAY + " enchantment book."});
        if (tier == 6)
            return ItemUtils.buildItem(Material.BOOK, (short) 0, ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Soul Enchantment Book" + ChatColor.GRAY + " (Right Click)", new String[]{ChatColor.GRAY + "Examine to recieve a random", ChatColor.RED.toString() + "soul" + ChatColor.GRAY + " enchantment book."});
        if (tier == 7)
            return ItemUtils.buildItem(Material.BOOK, (short) 0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Heroic Enchantment Book" + ChatColor.GRAY + " (Right Click)", new String[]{ChatColor.GRAY + "Examine to recieve a random", ChatColor.LIGHT_PURPLE.toString() + "heroic" + ChatColor.GRAY + " enchantment book."});
        if (tier == 8)
            return ItemUtils.buildItem(Material.BOOK, (short) 0, ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "Mastery Enchantment Book" + ChatColor.GRAY + " (Right Click)", new String[]{ChatColor.GRAY + "Examine to recieve a random", ChatColor.DARK_RED.toString() + "mastery" + ChatColor.GRAY + " enchantment book."});
        return null;
    }
}
