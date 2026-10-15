package me.astropvp.astropvpskins.enums.temp;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GlintUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.Getter;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.astropvp.astropvpskins.objects.skins.amulets.*;
import me.astropvp.astropvpskins.objects.skins.axes.*;
import me.astropvp.astropvpskins.objects.skins.backpacks.*;
import me.astropvp.astropvpskins.objects.skins.belts.*;
import me.astropvp.astropvpskins.objects.skins.boots.*;
import me.astropvp.astropvpskins.objects.skins.bows.CupidBowSkin;
import me.astropvp.astropvpskins.objects.skins.helmets.*;
import me.astropvp.astropvpskins.objects.skins.pickaxes.RubberChickenSkin;
import me.astropvp.astropvpskins.objects.skins.swords.*;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public enum incaseneedrevert {
    /*
    CUPID_HELMET(CC.RedB + "Cupid's Crown", ItemSkinType.HELMET, new MeatCleaverAxeSkin(), Collections.singletonList(CC.Red + "5% to steal enemy health if below half health."), true),
    MEAT_CLEAVER_AXE(CC.RedB + "Meat Cleaver " + CC.DarkRedB + "Axe", ItemSkinType.AXE, new MeatCleaverAxeSkin(), Collections.emptyList(), true),
    CUPID_BELT(CC.RedB + "Divine Sash", ItemSkinType.BELT, new CupidBeltSkin(), Collections.singletonList(CC.Red + "Auto Bless"), true),
    SEXY_BELT(CC.LightPurpleB + "Gucci Belt", ItemSkinType.BELT, new SexyBeltSkin(), Arrays.asList(CC.Red + "-25% Incoming Projectile Damage", CC.Red + "All pearls act as escapist pearls"), true),
    CORRUPT_BELT_POWER(CC.DarkPurpleB + "Corrupt Belt", ItemSkinType.BELT, new CorruptPowerBeltSkin(), Collections.singletonList(CC.Red + "Godly Overload V"), true),
    CORRUPT_BELT_UTILITY(CC.DarkPurpleB + "Utility Belt", ItemSkinType.BELT, new CorruptUtilityBeltSkin(), Arrays.asList(CC.Red + "Phoenix cannot be blocked", CC.Red + "Auto Feign Death (with Phoenix)"), true),
    CUPID_BOW(CC.RedB + "Cupid's Bow", ItemSkinType.BOW, new CupidBowSkin(), Arrays.asList(CC.Red + "Bypass enemy Arrow Deflect", CC.Red + "Lethal Sniper VI", CC.Red + "Chance to shoot double arrows"), true),
    CUPID_BOOTS(CC.RedB + "Cupid's Winged Boots", ItemSkinType.BOOTS, new CupidBootsSkin(), Collections.singletonList(CC.Red + "Chance to push attacker away."), true),
    CORRUPT_BOOTS(CC.DarkPurpleB + "Rocket Boots", ItemSkinType.BOOTS, new CorruptBootsSkin(), Arrays.asList(CC.Red + "Immune to Sabotage", CC.Red + "Gears IV"), true),
    SEXY_BOOTS(CC.LightPurpleB + "Bunny Slippers", ItemSkinType.BOOTS, new SexyBootsSkin(), Arrays.asList(CC.Red + "Web/Lava Walker", CC.Red + "2% Dodge"), true),
    DEATH_KNIGHT_SKULL_BLADE(CC.AquaB + "Death Knight " + CC.BlueB + "Skull Blade", ItemSkinType.SWORD, new DeathKnightSkullBladeSkin(), Collections.singletonList(CC.Red + "Equipped weapon can not get anti-hollied."), true),
    JOLLY_CANDY_SWORD(CC.RedB + "Jolly " + CC.WhiteB + "Candy " + CC.RedB + "Sword", ItemSkinType.SWORD, new JollyCandySwordSkin(), Collections.singletonList(CC.Red + "+7.5% DMG"), true),
    FLAMING_HALO(CC.YellowB + "Flaming " + CC.GoldB + "Halo", ItemSkinType.HELMET, new FlamingHaloSkin(), Collections.singletonList(CC.Red + "+4% Outgoing Damage"), true),
    REINDEER_ANTLERS(CC.GrayB + "Reindeer Antlers", ItemSkinType.HELMET, new ReindeerAntlersSkin(), Collections.singletonList(CC.Red + "Chance to double active pet duration."), true),
    SANTA_HAT(CC.RedB + "Santa Hat", ItemSkinType.HELMET, new SantaHatSkin(), Arrays.asList(CC.Red + "+5% DMG", CC.Red + "-5% DMG"), true),
    KOALA(CC.GrayB + "Koala", ItemSkinType.HELMET, new KoalaSkin(), Collections.singletonList(CC.Red + "Immune to Fire/Blast DMG"), true),
    CORRUPT_AXE(CC.DarkPurpleB + "Corrupt Greataxe", ItemSkinType.AXE, new CorruptAxeSkin(), Collections.singletonList(CC.Red + "Bypass Defensive Armor Set Bonuses"), true),
    CORRUPT_SWORD(CC.DarkPurpleB + "Corrupt Greatsword", ItemSkinType.SWORD, new CorruptSwordSkin(), Arrays.asList(CC.Red + "2x Chance to Neutralize enemies", CC.Red + "2x Blackout duration on enemies."), true),
    SEXY_SWORD(CC.LightPurpleB + "Selfie Stick", ItemSkinType.SWORD, new SelfieStickSkin(), Collections.emptyList(), true),
    CUPID_SWORD(CC.RedB + "Blade of Love", ItemSkinType.SWORD, new CupidSwordSkin(), Collections.singletonList(CC.Red + "2% Chain Lifesteal"), true),
    SEXY_HELMET(CC.LightPurpleB + "Chad Sunglasses", ItemSkinType.HELMET, new SexyHelmetSkin(), Collections.singletonList(CC.Red + "50% Chance to Block World Destroyer"), true),
    CORRUPT_HELMET(CC.DarkPurpleB + "Corrupt Cosmonaut Visor", ItemSkinType.HELMET, new CorruptHelmetSkin(), Arrays.asList(CC.Red + "50% Chance to block Neutralize", CC.Red + "50% Silence Duration"), true),
    CUPID_AMULET(CC.RedB + "Amulet of Angels", ItemSkinType.AMULET, new CupidAmuletSkin(), Arrays.asList(CC.Red + "Immune to Nature's Wrath", CC.Red + "Immune to Revenge of Yijki"), true),
    SEXY_AMULET(CC.LightPurpleB + "40K Gold Chain", ItemSkinType.AMULET, new SexyHelmetSkin(), Collections.singletonList(CC.Red + "+2% /sell prices"), true),
    CORRUPT_AMULET_CORRUPTION(CC.DarkPurpleB + "Amulet of Corruption", ItemSkinType.AMULET, new CorruptAmuletCorruptionSkin(), Collections.singletonList(CC.Red + "Immune to enemy armor crystals of same type on attached item."), true),
    CORRUPT_AMULET_DESTRUCTION(CC.DarkPurpleB + "Amulet of Destruction", ItemSkinType.AMULET, new CorruptAmuletDestructionSkin(), Collections.singletonList(CC.Red + "+7.5% Outgoing Damage"), true),
    VIRUS_SWORD(CC.DarkGreenB + "Corona Beer Bottle", ItemSkinType.SWORD, new VirusSwordSkin(), Arrays.asList(CC.Red + "+3.5% Outgoing Damage", CC.Red + "Mighty Cleave V"), true),
    SPRING_2020_SWORD(CC.GreenB + "Multi-Colored " + CC.YellowB + "Popsicle", ItemSkinType.SWORD, new SpringSwordSkin(), Collections.singletonList(CC.Red + "10% Chance to bypass pet cooldown."), true),
    VIRUS_BOOTS(CC.DarkGreenB + "Hazmat Boots", ItemSkinType.BOOTS, new VirusBootsSkin(), Arrays.asList(CC.Red + "Auto Bless", CC.Red + "Chance to activate Silence Pet", CC.Gray + "  (7x7 area for 12 seconds)"), true),
    SPRING_2020_BOOTS(CC.GreenB + "Rainbow " + CC.YellowB + "Slides", ItemSkinType.BOOTS, new RainbowSlidesSkin(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Jump Boost 1 in all /dungeon parkour regions.", CC.Red + "5% chance to not consume /dungeon key."), true),
    SPRING_2020_AMULET(CC.GreenB + "Staff " + CC.YellowB + "Pass", ItemSkinType.AMULET, new StaffPassAmuletSkin(), Collections.singletonList(CC.Red + "Immune to arrows"), true),
    SPRING_2020_AXE(CC.GreenB + "Spiked " + CC.YellowB + "Baseball Bat", ItemSkinType.AXE, new SpikedBaseballBatSkin(), Arrays.asList(CC.Red + "-5% Incoming Damage from enemies wielding Swords", CC.Red + "+10% Outgoing Damage to enemies wielding Swords"), true),
    VIRUS_BELT(CC.DarkGreenB + "First Aid Fannypack", ItemSkinType.BELT, new VirusBeltSkin(), Collections.singletonList(CC.Red + "50% chance to negate Blackout proc"), true),
    VIRUS_HELMET_HAZMAT(CC.DarkGreenB + "Hazmat Helmet", ItemSkinType.HELMET, new HazmatHelmetSkin(), Collections.singletonList(CC.Red + "Immune to Freezes"), true),
    VIRUS_HELMET_N95(CC.DarkGreenB + "Anti Corona Mask", ItemSkinType.HELMET, new AntiCoronaMaskSkin(), Arrays.asList(CC.Red + "Immune to Teleblock", CC.Red + "2% chance to proc Regeneration 2 for 25s"), true),
    PEPE_WEAPON(CC.BlueB + "Pepe Clapper", ItemSkinType.WEAPON, new PepeWeaponSkin(), Collections.singletonList(CC.Red + "Convert hit players into a rare pepe."), true),
    EASTER_HELMET(CC.RedB + "Bunny Ears", ItemSkinType.HELMET, new EasterHelmetSkin(), Collections.singletonList(CC.Red + "Ethereal Dodge VI"), true),
    EASTER_AMULET(CC.RedB + "Rabbit's Foot", ItemSkinType.AMULET, new RabitsFootAmuletSkin(), Collections.singletonList(CC.Red + "+10% Outgoing Damage"), true),
    EASTER_BELT(CC.RedB + "Rainbow Belt", ItemSkinType.BELT, new EasterBeltSkin(), Collections.singletonList(CC.Red + "-7.5% Incoming DMG"), true),
    EASTER_SWORD(CC.RedB + "Jumbo Chocolate Sword", ItemSkinType.SWORD, new EasterSwordSkin(), Arrays.asList(CC.Red + "+15% Outgoing PvE DMG", CC.Red + "Devour V"), true),
    EASTER_PICKAXE(CC.RedB + "Easter Basket", ItemSkinType.AXE, new EasterBasketSkin(), Collections.singletonList(CC.Red + "-10% Incoming DMG"), true),
    SPRING_2020_PICKAXE(CC.GreenB + "Rubber " + CC.YellowB + "Chicken", ItemSkinType.PICKAXE, new RubberChickenSkin(), Collections.emptyList(), true),

    PEPE_HELMET(CC.BlueB + "Pepe Helmet", ItemSkinType.HELMET, new PepeHelmetSkin(), Collections.emptyList(), true),
    SCYTHE1(CC.RedB + "Skull Scythe", ItemSkinType.SWORD, new SkullScytheSkin(), Arrays.asList(CC.Red + "+2.5% Outgoing Damage", CC.Red + "10% chance to silence amulet of corruption for 10s"), true),
    DOOMSDAY_SWORD(CC.DarkGreenB + "Doomsday Machete", ItemSkinType.SWORD, new DoomsdayMacheteSkin(), Collections.singletonList(CC.Red + "Increases outgoing damage from soul enchantments by 1.25x"), true),
    RUINS_SWORD(CC.DarkAquaB + "Trident of the Deep", ItemSkinType.SWORD, new TridentSkin(), Arrays.asList(CC.Red + "Blessed IV", CC.Red + "Hex V", CC.Red + "Mark of the Beast VI", CC.Red + "25% chance to bypass Full Armor Set", CC.Red + "or Armor Crystal defensive bonus"), true),
    COSMIC_CITIZEN_BOOTS(CC.AquaB + "Moon Boots", ItemSkinType.BOOTS, new MoonBootsSkin(), Collections.singletonList(CC.Red + "Increases armor set bonus/crystal damage by 1.2x"), true),
    MEDIEVAL_BOOTS_KNIGHT(CC.GrayB + "Knight's Boots", ItemSkinType.BOOTS, new KnightBootsSkin(), Collections.singletonList(CC.Red + "Immune to all but vanilla knockback"), true),
    DOOMSDAY_BOOTS(CC.DarkGreenB + "Camouflage Yeezys", ItemSkinType.BOOTS, new CamouflageBootsSkin(), Arrays.asList(CC.Red + "40% chance to negate soul trap", CC.Red + "Demonic Gateway V"), true),
    HALLOWEEN_BOOTS(CC.DarkGreenB + "Retaliation Boots", ItemSkinType.BOOTS, new RetaliationBoots(), Arrays.asList(CC.Red + "+15% Outgoing Damage to Bosses", CC.Red + "+10% Incoming Damage"), true),
    COSMIC_CITIZEN_AMULET(CC.GoldB + "Starquest Amulet", ItemSkinType.AMULET, new StarquestAmuletSkin(), Collections.singletonList(CC.Red + "4% chance to earn extra dungeon lootbag"), true),
    HALLOWEEN_HELMET(CC.GoldB + "Candy Collector", ItemSkinType.HELMET, new CandyCollectorSkin(), Arrays.asList(CC.Red + "-20% incoming damage from enemies wielding swords", CC.Red + "1% chance to activate witcher ability"), true),
    RUINS_HELMET(CC.translate("&3&lLeviathan Trophy"), ItemSkinType.HELMET, new LeviathanTrophySkin(), Arrays.asList(CC.translate("&cIncreased chance to drop Ancient Scrolls"), CC.translate("&cwhen killing Guardians in /ruins"), CC.translate("&cImmune to enemy Amulet of Corruption"), CC.translate("&c+1 Max Heart"), CC.translate("&c3% chance to silence enemy Soul Enchants"), CC.translate("  &7(7x7 blocks for 7s)")), true),
    DOOMSDAY_HELMET(CC.translate("&2&lDoomsday Helmet"), ItemSkinType.HELMET, new DoomsDayHelmetSkin(), Arrays.asList(CC.translate("&cAbility to see invisible players"), CC.translate("&7(Feign Death, Undead Ruse)")), true),
    VALENTINES_2021_HELMET(CC.translate("&c&lHeart Halo"), ItemSkinType.HELMET, new HearthHaloSkin(), Arrays.asList(CC.translate("&cPassively removes 1 Bleedstack every 2s"), CC.translate("&cImmune to Soul Harvester"), CC.translate("&c1.25% (Divine) Enlighted Healing")), true),
    CHRISTMAS_HELMET_STAR(CC.translate("&a&lNorth Star"), ItemSkinType.HELMET, new NorthStarSkin(), Arrays.asList(CC.translate("&c33% Chance to negate Monopoly Mask"), CC.translate("&c-33% enemy Blackout duration"), CC.translate("&cBlocks all enemy (Guided) Rocket Escape"), CC.translate("&cDivine Enlighted IV")), true),
    SUMMER_2021_HELMET(CC.translate("&6&lTiki Mask"), ItemSkinType.HELMET, new TikiMaskSkin(), Arrays.asList(CC.translate("&cLimites enemy amulet of corruption to 1 set type"), CC.translate("&c25% chance to block enemy Silence"), CC.translate("&cLimits enemy Silence to 60% duration")), true),
    NYE_HELMET(CC.translate("&d&lNeon Emotions"), ItemSkinType.HELMET, new NeonEmotionsSkin(), Arrays.asList(CC.translate("&c+6% Outgoing Damage"), CC.translate("&c-2% Incoming Damage"), CC.translate("&cImmune to Slowness Potion Effect")), true),
    THANKSGIVING_HELMET_WITCH(CC.translate("&e&lWitch Hat"), ItemSkinType.HELMET, new WitchHatSkin(), Arrays.asList(CC.translate("&c-25% Silence duration"), CC.translate("&cImmune to offensive Titan Attributes"), CC.translate("&7(Atlas, Ouranos and Prometheus)"), CC.translate("&cImmune to Trap"), CC.translate("&cImmune to Bloodhound & Banshee Pet"), CC.translate("&c10% chance to deflect incoming damage"), CC.translate("&conto nearby enemy"), CC.translate("&c2% chance to enable a LVL 10"), CC.translate("&cBanshee Pet effect when damaged")), true),
    THANKSGIVING_AMULET_AUTUMN(CC.translate("&e&lAutumn Amulet"), ItemSkinType.AMULET, new AutumnAmuletSkin(), Arrays.asList(CC.translate("&c+33% healing from Divine Enlighted"), CC.translate("&cEach time you enter combat:"), CC.translate("&c* No cooldown on 1st Phoenix Proc"), CC.translate("&c* -15% Incoming Damage for 15s"), CC.translate("&c* Absorption V for 20s")), true),
    SUMMER_2021_AMULET(CC.translate("&c&lLuau Lei"), ItemSkinType.AMULET, new LuauLei(), Arrays.asList(CC.translate("&c+25% Outgoing Soul Enchant damage"), CC.translate("&cChance to block Soul Enchant Damage Modifiers")), true),
    VALENTINES_2021_BELT(CC.translate("&c&lCupids Heart"), ItemSkinType.BELT, new CupidsHeart(), Arrays.asList(CC.translate("&cBypass enemy (Paladin) Armored"), CC.translate("&cHoly Aegis VII"), CC.translate("&c+10% Outgoing Damage to Silenced enemies")), true),
    CHRISTMAS_BOOTS_SLIPPERS(CC.translate("&c&lSnowflake Slippers"), ItemSkinType.BOOTS, new SnowflakeSlippersSkin(), Arrays.asList(CC.translate("&cGears IV"), CC.translate("&cImmune to Teleblock"), CC.translate("&cImmune to Slowness Potion Effect"), CC.translate("&cSnowflake Storm Ability"), CC.translate("&7(10% chance to spawn 4x4 snow floor for 10s."), CC.translate("&7While stood on the snow floor:"), CC.translate("&7* You will receive -20% Incoming Damage"), CC.translate("&7* Your allies will receive -5% Incoming Damage"), CC.translate("&7* Your enemies will receive Slowness II)")), true),
    THANKSGIVING_SWORD_HARVESTER(CC.translate("&e&lSoul Harvester"), ItemSkinType.SWORD, new SoulHarvesterSkin(), Arrays.asList(CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.3x"), CC.translate("&c+10% Outgoing Damage to enemies with Soul Mode active"), CC.translate("&c50% chance to block Divine Immolation"), CC.translate("&c-25% Soul Enchant Costs")), true),
    VALENTINES_2021_BOOTS(CC.translate("&c&lTeddy Slippers"), ItemSkinType.BOOTS, new TeddySlippersSkin(), Arrays.asList(CC.translate("&cNerf enemy special Armor Set weapon Outgoing Damage bonuses by 50%"), CC.translate("&c33% Chance to reflect enemy (Titan) Trap for 1.25x duration"), CC.translate("&c-2% Incoming Damage"), CC.translate("&c+1% Outgoing Damage")), true),
    CHRISTMAS_SWORD_ORNAMENTAL(CC.translate("&a&lOrnamental Carnage"), ItemSkinType.SWORD, new OrnamentalCarnageSkin(), Arrays.asList(CC.translate("&c-5% Incoming Damage"), CC.translate("&c-15% Incoming Damage from enemies with Soul Mode active"), CC.translate("&cTitan Trap V"), CC.translate("&c+10% Outgoing Damage to trapped players"), CC.translate("&c+2 blocks Divine Immolation radius"), CC.translate("&c2% chance for Dimensional Shift Ability")), true),
    VALENTINES_2021_SWORD(CC.translate("&c&lHeartbreaker"), ItemSkinType.SWORD, new HeartBreakerSkin(), Arrays.asList(CC.translate("&cBypass enemy (Vengeful) Diminish"), CC.translate("&c+1% Damage for each enemy level of (Planetary) Deathbringer"), CC.translate("&cMartyr Valor V"), CC.translate("&cReality Inversion V")), true),
    SUMMER_2021_SWORD(CC.translate("&c&lMaui's Hook"), ItemSkinType.SWORD, new MauisHookSkin(), Arrays.asList(CC.translate("&c+15% Outgoing Soul Enchant Damage"), CC.translate("&cBlackout V"), CC.translate("&cNeutralize VI"), CC.translate("&cTranscendence ability"), CC.translate("&7(For each soul enchant that is"), CC.translate("&7blocked (not including immunity)"), CC.translate("&7gain +1 stack. At 10 stacks crouch"), CC.translate("&7to activate Transcendence massively"), CC.translate("&7damaging and silencing nearby"), CC.translate("&7players and pets.)")), true),
    RUINS_AMULET(CC.translate("&3&lLost Amulet"), ItemSkinType.AMULET, new LostAmulet(), Arrays.asList(CC.translate("&c50% Neutralize Negation"), CC.translate("&c-5% Incoming Damage"), CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.1x")), true),
    DOOMSDAY_BACKPACK_BUGOUT(CC.GreenB + "Bug-Out Bag", ItemSkinType.BACKPACK, new BugOutBagSkin(), Collections.singletonList(CC.translate("&c10% chance to keep each equipped item on death.")), true),
    DOOMSDAY_BACKPACK_RAINBOW(CC.GreenB + "Rainbow Backpack", ItemSkinType.BACKPACK, new RainbowBackpackSkin(), Collections.singletonList(CC.translate("&c+1 Max Heart.")), true),
    DEEP_SPACE_BACKPACK(CC.DarkPurpleB + "Deep Space Backpack", ItemSkinType.BACKPACK, new DeepSpaceBackpackSkin(), Arrays.asList(CC.translate("&c+3% Outgoing Damage."), CC.translate("&cImmune to Soul Trap")), true),
    HALLOWEEN_BACKPACK(CC.GoldB + "Cursed Wings", ItemSkinType.BACKPACK, new CursedWingsSkin(), Arrays.asList(CC.translate("&c-75% Silence Duration"), CC.translate("&cBlocks all enemy Rocket Escape"), CC.translate("&c-3% Incoming Damage")), true),
    VALENTINES_2021_BACKPACK(CC.RedB + "Teddy Backpack", ItemSkinType.BACKPACK, new TeddyBackpackSkin(), Arrays.asList(CC.Red + "Immune to (Lethal) Sniper", CC.Red + "Immune to Hero Killer", CC.Red + "+2% Outgoing Damage"), true),
    MEDIEVAL_HELMET_KING(CC.GoldB + "Royal Crown", ItemSkinType.HELMET, new RoyalCrown(), Arrays.asList(CC.Red + "25% Faster cap time at Citadel Assault", CC.translate("&c-5% Incoming Damage"), CC.translate("&cImmune to enemy Guardians/Undead Ruse")), true),
    MEDIEVAL_HELMET_KNIGHT(CC.GrayB + "Knight's Helmet", ItemSkinType.HELMET, new KnightHelmet(), Arrays.asList(CC.Red + "Immune to Teleblock", CC.translate("&cAquatic"), CC.translate("&cAll pearls act as Escapist Pearls"), CC.translate("&c2% Chance to prox Arcane Creeper when damaged")), true),
    COSMIC_CITIZEN_HELMET_DRINKING(CC.translate("&b&lDrinking Helmet"), ItemSkinType.HELMET, new DrinkingHelmet(), Arrays.asList(CC.Red + "5-20s shorter /dungeon portal time", CC.translate("&cGhostly Ghost III")), true),
    COSMIC_CITIZEN_HELMET_CG(CC.translate("&b&lCosmicGames Hat"), ItemSkinType.HELMET, new CosmicGamesHat(), Collections.singletonList(CC.Red + "5% chance to not consume Slotbot Ticket on Spin"), true),
    CHRISTMAS_HELMET_ICE(CC.translate("&c&lIce Crown"), ItemSkinType.HELMET, new IceCrown(), Arrays.asList(CC.Red + "-5% Incoming Damage", CC.translate("&cReflects (Titan) Trap effects on enemy")), true),
    MEDIEVAL_BELT_KING(CC.translate("&6&lRoyal Belt"), ItemSkinType.BELT, new RoyalBelt(), Collections.singletonList(CC.Red + "25% chance to not proc Pet Cooldown"), true),
    MEDIEVAL_BELT_KNIGHT(CC.translate("&7&lChain Belt"), ItemSkinType.BELT, new ChainBelt(), Arrays.asList(CC.Red + "Auto Bless", CC.translate("&c200% damage to Citadel Siege blocks"), CC.translate("&c+2.5% outgoing Damage")), true),
    COSMIC_CITIZEN_BELT(CC.translate("&b&lSpace Belt"), ItemSkinType.BELT, new SpaceBelt(), Arrays.asList(CC.Red + "Auto Bless", CC.translate("&c+5% Blackscroll Bonus"), CC.translate("&c+2% to not consume Book on apply")), true),
    DOOMSDAY_BELT(CC.translate("&2&lTactical Belt"), ItemSkinType.BELT, new TacticalBelt(), Arrays.asList(CC.translate("&cIncreases pet ability duration by 1.5x"), CC.translate("&7 (Shackled Golem, Stitches, Sell Bot, Worlock"), CC.translate("&7 Stronghold, Tesla, XP Booster, Anti-Teleblock)")), true),
    HALLOWEEN_BELT(CC.translate("&6&lCandy Sorting Belt"), ItemSkinType.BELT, new HalloweenBelt(), Arrays.asList(CC.translate("&c5% Chance to negate Infinity Luck"), CC.translate("&cper Heroic Armor piece equipped"), CC.translate("&cBypasses Candy Collector's Sugar Rush")), true),
    THANKSGIVING_BELT(CC.translate("&e&lPie Belt"), ItemSkinType.BELT, new ThanksGivingBelt(), Arrays.asList(CC.translate("&c+0.5% Outgoing Damage for each enemy Hunger missing"), CC.translate("&c5% chance to absorb and convert Incoming"), CC.translate("&cDamage into a Pie Hypnosis stack"), CC.translate("&7 (Each \"Pie Hypnosis\" stack will deal"), CC.translate("&7 +25% durability damage and increases"), CC.translate("&7 outgoing damage by 0.5%)"), CC.translate("&cPie Explosion Ability"), CC.translate("&7 (Eat a Pumpkin Pie when \"Pie Hypnosis\""), CC.translate("&7 stack reaches 10 to trigger \"Pie Explosion\","), CC.translate("&7 healing you for 33% of total HP, fixing"), CC.translate("&7 your armor for 25% per piece, slowing all"), CC.translate("&7 nearby enemies and knocking them away)")), true),
    CHRISTMAS_BELT_CANDY(CC.translate("&a&lCandy Buckle"), ItemSkinType.BELT, new CandyBuckle(), Arrays.asList(CC.translate("&cDisable enemy's (Alien) Implants for 10s each hit"), CC.translate("&c+1% Outgoing Damage for each Food level"), CC.translate("&cunder 20 that the enemy is missing (max +10%)"), CC.translate("&c10% chance for Speed IV for 4s when damaged")), true),

    CHRISTMAS_BELT_CRYSTALINE(CC.translate("&c&lCrystalline Band"), ItemSkinType.BELT, new CrystallineBand(), Arrays.asList(
            CC.translate("&cImmune to Icy Wrath Ability"),
            CC.translate("&cObsidian Guardians X"),
            CC.translate("&c20% Chance to negate Blackout proc")), true),
    SPRING_2021_BELT(CC.translate("&a&lParty Belt"), ItemSkinType.BELT, new PartyBelt(), Arrays.asList(
            CC.translate("&cReduce freeze/slowness effects on walk speed by 50%"),
            CC.translate("&c25% Chance to reduce enemy Rage Stacks by 1 when hit"),
            CC.translate("&cImmune to Deep Bleed Slowness")), true),
    TRIALS_BELT(CC.translate("&6&lCinder Wolf Belt"), ItemSkinType.BELT, new CinderWolf(), Arrays.asList(
            CC.translate("&cCinder Ability"),
            CC.translate("&7 (5% chance when hit to:"),
            CC.translate("&7 * Spawn 4 Fireballs in each direction,"),
            CC.translate("&7 which upon impact with a player"),
            CC.translate("&7 deal 33% of their current health"),
            CC.translate("&7 * Receive Gears IV for 7s"),
            CC.translate("&7 * Deal +5 Outgoing Damage for 7s%"),
            CC.translate("&7 * AoE damage every 1s for 7s)")
    ), true),
    SUMMER_2021_BELT(CC.translate("&c&lHula Skirt"), ItemSkinType.BELT, new HulaSkirt(), Arrays.asList(
            CC.translate("&cImmune to Lifesteal"),
            CC.translate("&c+30% more chance to get loot in /warp space")
    ), true),
    SUMMER_2021_BOOTS(CC.translate("&6&lRoller Skates"), ItemSkinType.BOOTS, new RollerSkates(), Arrays.asList(
            CC.translate("&cGears IV"),
            CC.translate("&cAuto Bless"),
            CC.translate("&cImmune to Skrrt Pet"),
            CC.translate("&c+10% Monopoly Chance")
    ), true),
    INFINITY_BELT(CC.translate("&9&lInfinity Belt"), ItemSkinType.BELT, new InfinityBelt(), Arrays.asList(
            CC.translate("&cEquipped armor will regain to 10% durability"),
            CC.translate("&cinstead of breaking (10m cooldown)"),
            CC.translate("&cImmune to Soul Trap"),
            CC.translate("&cImmune to (Obsidian) Guardians"),
            CC.translate("&cNegates enemy (Reinforced) Tank")
    ), true),

    COSMIC_CITIZEN_AXE(CC.translate("&b&lFirework Rocket"), ItemSkinType.AXE, new FireworkRocket(), Collections.singletonList(
            CC.translate("&cTitan Trap III")
    ), true),
    SCYTHE2(CC.RedB + "Skull Scythe", ItemSkinType.AXE, new SkullScytheAxeSkin(), Arrays.asList(CC.Red + "+2.5% Outgoing Damage", CC.Red + "10% chance to silence amulet of corruption for 10s"), true),

    HALLOWEEN_AXE(CC.translate("&6&lCorpse Grinder"), ItemSkinType.AXE, new CorpseGrinder(), Arrays.asList(
            CC.translate("&cDouble Strike II"),
            CC.translate("&cBypasses Dodge"),
            CC.translate("&c2x Outgoing Durability Damage"),
            CC.translate("&c+15% Outgoing Damage for 30s"),
            CC.translate("&cwhenever you kill an Enemy Player"),
            CC.translate("&cDeath Coffin III")
    ), true),
    CHRISTMAS_AXE_SNOWFLAKE(CC.translate("&a&lSnowflake"), ItemSkinType.AXE, new Snowflake(), Arrays.asList(
            CC.translate("&cSnowflake Snowball Ability"),
            CC.translate("&7 (Right-Click&7 to launch snowflake snowball."),
            CC.translate("&7 Any enemy players hit by the snowflake"),
            CC.translate("&7 snowball will receive Slowness II for 2s"),
            CC.translate("&7 and will take +8% Incoming Damage for 3s,"),
            CC.translate("&7 8s cooldown)"),
            CC.translate("&cIcy Wrath Ability"),
            CC.translate("&7 (4% chance to \"Deep Freeze\" targets,"),
            CC.translate("&7 slowing them for 2s and increasing their"),
            CC.translate("&7 incoming damage by +5%"),
            CC.translate("&7 33% chance to AoE Silence enemy enchants"),
            CC.translate("&7 including active soul enchants for 2s?"),
            CC.translate("&7 Pulls nearby enemies towards you."),
            CC.translate("&7 Reduces outgoing enemy damage by 1%"),
            CC.translate("&7 each hit for 10s up to max of 10%)"),
            CC.translate("&c Icy Blood Ability"),
            CC.translate("&7 (Reduces enemy health regeneration by"),
            CC.translate("&7 1% for 6s for each bleed stack"),
            CC.translate("&7 they have up to a max of 20%)")
    ), true),

    VALENTINES_2021_AXE(CC.translate("&c&lGolden Admirer"), ItemSkinType.AXE, new GoldenAdmirer(), Arrays.asList(
            CC.translate("&cDisables enemy Cursed Wings and Witch Hat for 10s"),
            CC.translate("&cOutgoing damage has a 50% chance to remove any Trap/Slowness effect on you")
    ), true),
    MEDIEVAL_AMULET_KING(CC.GoldB + "Amulet of Kings", ItemSkinType.AMULET, new AmuletOfKingsSkin(), Collections.singletonList(CC.Red + "15% chance for /end envoy chest loot to auto-/collect"), true),
    MEDIEVAL_AMULET_KNIGHT(CC.GrayB + "Diamond Necklace", ItemSkinType.AMULET, new DiamondNecklaceSkin(), Collections.singletonList(CC.Red + "Increased view distance by 1 chunk"), true),
    CHRISTMAS_AMULET_SNOWFLAKE(CC.RedB + "Preserved Snowflake", ItemSkinType.AMULET, new PreservedSnowflakeSkin(), Arrays.asList(CC.Red + "Immune to Soul Tether, Soul Trap", CC.Red + "Immune to Nature's Wrath", CC.Red + "Able to see invisible player", CC.Gray + " (Feign Death, Undead Ruse)"), true),
    NYE_AMULET(CC.LightPurpleB + "Radioactive Amulet", ItemSkinType.AMULET, new RadioactiveAmulet(), Arrays.asList(CC.Red + "15% chance for double loot from Mobs in /warp space", CC.Red + "-50% Incoming PvE Damage in /warp space"), true),
    CHRISTMAS_AMULET_SANTA(CC.GreenB + "Secret Santa", ItemSkinType.AMULET, new SecretSantaSkin(), Arrays.asList(CC.Red + "Jolly Suprise ability", CC.Gray + " (5% chance to Feign Death for 2.5s, spawning", CC.Gray + " a \"Demonic Gateway\" where you vanished. On", CC.Gray + " your next attack, you will deal 2x damage", CC.Gray + " if dealt from behind, 30s Cooldown)", CC.Red + "Negates enemy Feign Death"), true),
    HALLOWEEN_AMULET(CC.GoldB + "Silent Death", ItemSkinType.AMULET, new SilentDeathSkin(), Arrays.asList(CC.Red + "5% chance to afflict enemy", CC.Red + "with Silent Death ability", CC.Gray + " (Enemy will receive 20% Incoming DMG", CC.Gray + " until caster has been hit 5 times", CC.Gray + " or 15 seconds has elapsed.)", CC.Red + "Immune to Silent Death ability", CC.Red + "+3% Outgoing Damage"), true),
    VALENTINES_2021_AMULET(CC.RedB + "Rose Amulet", ItemSkinType.AMULET, new RoseAmuletSkin(), Arrays.asList(CC.Red + "Immune to Snowflake Axe skin abilities", CC.Red + "-50% damage from enemy Rage", CC.Red + "Automatic Phoenix Feather (30m cooldown)"), true),
    CHRISTMAS_BACKPACK_GOODIES(CC.GreenB + "Sack of Goodies", ItemSkinType.BACKPACK, new SackOfGoodiesSkin(), Arrays.asList(CC.Red + "-2% Incoming Damage", CC.Red + "+2% Outgoing Damage", CC.Red + "1% chance on incoming damage to:", CC.Gray + " * Activate \"Witcher\" pet effect", CC.Gray + " * Activate Titan \"Unbreakable\" pet effect", CC.Gray + " * Activate \"Skrrt\" pet effect", CC.Red + "1% chance on outgoing damage to:", CC.Gray + " * Activate Bloodhound \"Tag\" pet effect", CC.Gray + " * Activate Banshee \"RKO\" pet effect"), true),
    CHRISTMAS_BACKPACK_REINDEER(CC.RedB + "Baby Reindeer", ItemSkinType.BACKPACK, new BabyReindeerSkin(), Arrays.asList(CC.Red + "Incoming damage has a 33% chance", CC.Red + "to break enemy Silence, Blackout", CC.Red + "Blood Pact ability", CC.Gray + " (50% chance for incoming damage that would", CC.Gray + " normally kill you or bring your health to <=6HP", CC.Gray + " to be redirected at a nearby in-combat ally", CC.Gray + " within 8 blocks for 2x the damage. If no", CC.Gray + " allies are within 8 blocks, you will receive", CC.Gray + " -25% incoming damage when <=6HP)", CC.Red + "Furry Friend ability", CC.Gray + " (Receive Absorbtion V for 8s if an incoming", CC.Gray + " attack brings your health to <=20HP, 30s", CC.Gray + " cooldown)"), true),
    NYE_BACKPACK(CC.LightPurpleB + "Power Core", ItemSkinType.BACKPACK, new PowerCoreSkin(), Arrays.asList(CC.Red + "Aquatic I", CC.Red + "Lava Strider I", CC.Red + "Ghostly Ghost III", CC.Red + "Obsidianshield I", CC.Red + "Gears IV", CC.Red + "Obsidian you place spawns with 1 bonus", CC.Red + "durability until server reboot"), true),
    SUMMER_2021_BACKPACK(CC.GoldB + "Surfboard", ItemSkinType.BACKPACK, new SurfboardSkin(), Collections.singletonList(CC.Red + "Chance to not activate custom block cooldown"), true),
    THANKSGIVING_BACKPACK_PICNIC(CC.YellowB + "Portable Picnic", ItemSkinType.BACKPACK, new PortablePicnicSkin(), Arrays.asList(CC.translate("&cNo Food loss"), CC.translate("&c-4% Incoming Damage"), CC.translate("&c+1 Food to nearby allies within"), CC.translate("&c10 blocks every 10s")), true),
    CHRISTMAS_SWORD_CANDY(CC.RedB + "Candy Blade", ItemSkinType.SWORD, new CandyBladeSkin(), Arrays.asList(CC.Red + "Chain Lifesteal X", CC.Red + "Sugar Burst Ability", CC.Gray + " (10% chance to deal damage to", CC.Gray + " enemies 20% more often for 3s)"), true),
    TRIALS_SWORD(CC.GoldB + "Death and Taxes", ItemSkinType.SWORD, new DeathAndTaxesSkin(), Arrays.asList(CC.Red + "Death Toll ability", CC.Gray + " (8% chance to mark a player with \"Death Toll\",", CC.Gray + " which will track all damage you deal to", CC.Gray + " to them with Death and Taxes and damage", CC.Gray + " the player by 25% of damage dealt after 6s.)"), true),
    MEDIEVAL_SWORD_KING(CC.GoldB + "King Sceptre", ItemSkinType.SWORD, new KingSceptreSkin(), Arrays.asList(CC.Red + "Master Inquisitive V", CC.Red + "2x mob drops from melee'd mobs", CC.Red + "2x vanilla EXP from melee'd mobs", CC.Red + "Auto /sell mob drops"), true),
    SPRING_2021_SWORD(CC.GreenB + "Party Blade", ItemSkinType.SWORD, new PartyBladeSkin(), Arrays.asList(CC.Red + "Bypass enemy (Heroic) Aegis", CC.Red + "Immune to enemy Leadership", CC.Red + "+20% Outgoing damage when (Titan) Trapped/Frozen", CC.Red + "Inverted Knockback", CC.Gray + " (your enemy gets pulled to you each time you hit them)", CC.Red + "Silence enemy Amulet of Corruption for +3s per hit, max 30s"), true),
    NYE_SWORD(CC.LightPurpleB + "Party Killer", ItemSkinType.SWORD, new PartyKillerSkin(), Arrays.asList(CC.Red + "Anti Gank II", CC.Red + "+1% Outgoing Damage per combat tagged", CC.Red + "enemy player in your chunk (max +5%)", CC.Red + "Attacks have a 20% chance to pull target", CC.Red + "slightly towards you and push nearby enemies", CC.Red + "within a 3x3 radius of target away", CC.Red + "Attacks combat tag all enemies", CC.Red + "within a 4x64x4 radius of a target", CC.Red + "50% Pet Leash Negation"), true),
    NYE_2021_AXE(CC.translate("&9&lCosmic Key"), ItemSkinType.AXE, new CosmicKey(), Arrays.asList(CC.Red + "Up to 10% chance to dodge Deathbringer", CC.Red + "when health is below 10 HP", CC.Red + "Divine Immolation IV", CC.Red + "Titan Attribute: Atlas 3", CC.Red + "Up to +10% Outgoing Damage when health is below 15 HP"), true),
    THANKSGIVING_2021_AXE(CC.translate("&6&lWhisk Taker"), ItemSkinType.AXE, new WhiskTaker(), Arrays.asList(CC.Red + "3% chance when hit to proc an AoE", CC.Red + "explosion which deals 5HP to all", CC.Red + "enemies in a 5x5 radius and makes", CC.Red + "deal +20% Outgoing Damage to", CC.Red + "all affected enemies for 6s", CC.Red + "+2.5 Outgoing Damage", CC.Red + "15% chance when your Rogue procs", CC.Red + "to make your enemy 45% slower for 1s", CC.Red + "5% chance to Teleblock", CC.Red + "4% chance to silence your enemies", CC.Red + "Divine Immolation for 8s"), true),

    BACK_TO_SCHOOL_2021_AXE(CC.translate("&8&lBoosted Chainsaw"), ItemSkinType.AXE, new BoostedChainsaw(), Arrays.asList(CC.Red + "50% chance to bypass (Marty) Valor", CC.Red + "Double Strike III", CC.Red + "2.5% chance to silence enemy Soul Enchants for 8s", CC.Red + "Negate enemy Preserved Snowflake"), true),
    JULY_2021_AXE(CC.translate("&f&lStormbreaker"), ItemSkinType.AXE, new StormBreaker(), Arrays.asList(CC.Red + "Blackout IV", CC.Red + "Divine Immolation I", CC.Red + "Titan Attribute: Kronos 8"), true),
    CHRISTMAS_2021_SWORD(CC.translate("&4&lSeason's Beatings"), ItemSkinType.SWORD, new SeasonsBeatings(), Arrays.asList(CC.Red + "+7% Outgoing Damage to players wielding weapons with Hero Killer", CC.Red + "+10% Outgoing Damage to enemies with Soul Mode Activated", CC.Red + "3.5% chance to silence enemy (Reflective) Block for 8s", CC.Red + "Outgoing Damage while Frozen or Slowed has a 12.5% chance", CC.Red + "to remove the Freeze or Slow effect", CC.Red + "+10% Rage Damage"), true),
    THANKSGIVING_2021_SWORD(CC.translate("&e&lCarver"), ItemSkinType.SWORD, new Carver(), Arrays.asList(CC.Red + "15% chance to deal 2x Divine Immolation tick damage", CC.Gray + "  (costs 2x extra souls on proc)", CC.Red + "33% chance to bypass Heroic Armor", CC.Red + "Damage you deal has a 3% chance to deal", CC.Red + "+50% Outgoing Soul Enchant Damage for 5s", CC.Red + "4% chance to silence your enemy's Rogue for 8s"), true),
    HALLOWEEN_2021_SWORD(CC.translate("&6&lSpinal Tap"), ItemSkinType.SWORD, new SpinalTab(), Arrays.asList(CC.Red + "Hysteria Ability", CC.Gray + " (Every player you hit will grant you 1 Hysteria", CC.Gray + " stack up to a max of 5 stacks for 4s. For every", CC.Gray + " stack you have, you will receive +2% Outgoing", CC.Gray + " On your first hit at 5 stacks, you are", CC.Gray + " guaranteed to proc Blackout 4)", CC.Red + "2% chance to proc Rogue II", CC.Red + "when attacking enemies from behind", CC.Red + "4% chance to negate enemy Witch Hats for 10s"), true),
    COSMIC_CITIZEN_SWORD(CC.AquaB + "Sparkler", ItemSkinType.SWORD, new SparklerSkin(), Collections.singletonList(CC.Red + "Transforms any creeper you hit into a Raid Creeper"), true),
    HALLOWEEN_SWORD(CC.GoldB + "Blade of Darkness", ItemSkinType.SWORD, new BladeOfDarknessSkin(), Arrays.asList(CC.Red + "2% chance to proc Revenge of Yijki", CC.Red + "4% Chain Lifesteal V", CC.Red + "33% chance to reflect Traps", CC.Red + "Soul Siphon III", CC.Red + "Headless III", CC.Red + "Immune to Blindness"), true),
    JULY_2021_BACKPACK(CC.translate("&f&lCosmic Cape"), ItemSkinType.BACKPACK, new CosmicCapeSkin(), Arrays.asList(CC.Red + "Immune to Hero Killer", CC.Red + "Decreases incoming damage from Soul Enchants by 15%", CC.Red + "2.5% chance to negate enemy Shockwave and (Guided) Rocket", CC.Red + "Escape on outgoing damage for 10s", CC.Red + "Increases outgoing damage by up to 15% when under 50% HP"), true),
    THANKSGIVING_SWORD_FEAST(CC.YellowB + "The Feast", ItemSkinType.SWORD, new TheFeastSkin(), Arrays.asList(CC.Red + "Soul Tether V", CC.Red + "-1 Food on enemy on Outgoing Damage", CC.Red + "20% chance to gain 1HP as you feed on your enemy", CC.Red + "20% chance to afflict enemies with Hunger IV for 3s"), true),

    THANKSGIVING_2021_BOOTS(CC.translate("&8&lLoafers"), ItemSkinType.BOOTS, new LoafersSkin(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "3.5% chance to make enemy (Vampiric) Devour heal", CC.Red + "instead of hurt you by the same % for 5s", CC.Red + "2.5% chance to silence enemy Pets in a 6x6 radius for 10s", CC.translate("&cGears V for 6s when affected by an enemy's Heroic Pet")), true),


    CHRISTMAS_2021_BOOTS(CC.translate("&2&lSleigh Riders"), ItemSkinType.BOOTS, new SleighRiders(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Limit enemy Amulet of Corruption to 1 armor set type", CC.Red + "When affected by any Slowness, you will receive -1%", CC.Red + "Incoming Damage (up to a max of 8%) and +1% Outgoing", CC.translate("&cDamage (up to a max of 15%) per 5% HP lost"), CC.translate("&c3% chance to clear your enemy's rage stacks and"), CC.translate("&csilence their ability to gain more for 7s"), CC.translate("&c7% chance to negate enemy Rogue for 8s when their"), CC.translate("&chealth is below 20 HP")), true),


    JULY_2021_BELT(CC.translate("&9&lInfinity Belt"), ItemSkinType.BELT, new InfinityBeltSkin(), Arrays.asList(CC.Red + "Equipped armor will regain to 10% durability", CC.Red + "instead of breaking (10m cooldown)", CC.Red + "Immune to Soul trap", CC.Red + "Immune to (Obsidian) Guardians", CC.translate("&cNegates enemy (Reinforced) Tank")), true),

    JULY_2021_BOOTS(CC.translate("&c&lBarry's Bolts"), ItemSkinType.BOOTS, new BarrysBoltsSkin(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Immune to Slowness Potion Effect", CC.Red + "-15% damage from enemy (Bloody) Deep Wounds", CC.Red + "Flash Ability", CC.translate("&7 (10% chance when damaged to activate. When"), CC.translate("&7 active, all enemies you hit will have their"), CC.translate("&7 pets and Phoenix enchantment silenced, your"), CC.translate("&7 Soul Tether will do +20% damage and incoming"), CC.translate("&7 soul damage to your allies within a 4x4"), CC.translate("&7radius is reduced by -5%")), true),

    BACK_TO_SCHOOL_2021_BOOTS(CC.translate("&8&lBig Timbs"), ItemSkinType.BOOTS, new BigTimbsSkin(), Arrays.asList(CC.Red + "35% chance on incoming damage to", CC.Red + "negate enemy Rage stack multipliers", CC.Red + "Immune to Hero Killer", CC.Red + "Immune to enemy outgoing Heroic Pet abilities", CC.translate("&cGears IV")), true),
    HALLOWEEN_2021_BOOTS(CC.translate("&8&lBloodstained Galoshes"), ItemSkinType.BOOTS, new BloodstainedGaloshesSkin(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Immune to World Destroyer Cage", CC.Red + "25% chance to reflect cage back at the user", CC.Red + "Immune to Slownesses/Freezes", CC.Red + "Increases Execute HP threshold to proc by 1.3x", CC.translate("&cPsychotic Pursuit Ability"), CC.translate("&7 (+5% chance to proc Dodge +7 Outgoing"), CC.translate("&7 Damage on successful (Ethereal) Dodge procs"), CC.translate("&7 for a short duration")), true),


    BACK_TO_SCHOOL_2021_HELMET(CC.translate("&6&lPylon Head"), ItemSkinType.HELMET, new PylonHeadSkin(), Arrays.asList(CC.Red + "Reduces the duration of active Silence", CC.Red + "or Blackout by 40% on outgoing damage.", CC.Red + "Limits enemy Amulet of Corruption so that it", CC.Red + "affects defensive Armor Crystal attributes only"), true),
    HALLOWEEN_2021_HELMET(CC.translate("&6&lMineman Muzzle"), ItemSkinType.HELMET, new MinemanMuzzleSkin(), Arrays.asList(CC.Red + "25% chance to curse enemies with +33% Incoming", CC.Red + "Damage when their Phoenix procs for 10s", CC.Gray + " (Can only proc once every 10m)", CC.Red + "3.5% chance to negate enemy Cosmic Cape for 10s", CC.Red + "Immune to Banshee RKO", CC.Red + "10% chance to reflect damage to enemies in a 3x3 radius"), true),
    THANKSGIVING_2021_HELMET(CC.translate("&6&lBread Head"), ItemSkinType.HELMET, new BreadHatSkin(), Arrays.asList(CC.Red + "1.5% chance to deal 10% of your", CC.Red + "enemy's current HP when hit", CC.Red + "1% chance to silence enemy Soul", CC.Red + "Enchants for 7s when you're hit", CC.Red + "25% chance to reflect (Titan) Trap", CC.Red + "effects back onto your attacker", CC.Red + "-15% Incoming Titan Attribute Damage"), true),
    CHRISTMAS_2021_HELMET(CC.translate("&2&lKrampus Horns"), ItemSkinType.HELMET, new KrampusHornsSkin(), Arrays.asList(CC.Red + "-10% Incoming Soul Enchant Damage", CC.Red + "Increases Deadly Disarmor health", CC.Red + "proc threshold by 4 HP", CC.Red + "-45% Silence Reduction", CC.Red + "75% chance to block enemy Ragdoll", CC.Red + "Immune to (Lethal) Sniper"), true),

    THANKSGIVING_2021_AMULET(CC.translate("&e&lThe Crumble"), ItemSkinType.AMULET, new TheCrumbleSkin(), Arrays.asList(CC.Red + "7.5% chance to receive -2.5% Incoming", CC.Red + "Damage (up to a max of -15%) for 8s", CC.Red + "when receiving Divine Immolation damage", CC.Red + "Immune to (Obsidian) Guardians", CC.Red + "5% chance to silence your enemy's", CC.Red + "Phoenix for 6s when they're under 15 HP", CC.Red + "Immune to enemy Amulet of Corruption"), true),
    CHRISTMAS_2021_AMULET(CC.translate("&4&lCranked Amulet"), ItemSkinType.AMULET, new CrankedAmuletSkin(), Arrays.asList(CC.Red + "Immune to Hero Killer", CC.Red + "8% chance to double proc offensive Heroic Enchants", CC.Red + "3% chance to silence enemy Soul Enchants for 6s", CC.Red + "Escape Hatch Ability", CC.Gray + " (If your health  is below 8HP and a player uses a", CC.Gray + " Heroic Pet on you, they are launched into the air", CC.Gray + " Cooldown: 2 minutes)"), true),


    BACK_TO_SCHOOL_2021_BELT(CC.translate("&8&lBandolier Belt"), ItemSkinType.BELT, new BandolierBeltSkin(), Arrays.asList(CC.Red + "Bypass enemy Witch Hat", CC.Red + "Immune to Deep Bleed Slowness", CC.Red + "Limits an enemy stackable enchant level by 50%"), true),

    THANKSGIVING_2021_BACKPACK(CC.translate("&6&lTommy Tail"), ItemSkinType.BACKPACK, new TommyTail(), Arrays.asList(CC.Red + "+5% Outgoing PvP Damage when using a sword", CC.Red + "Immune to Hero Killer", CC.Red + "7.5% chance to block Soul Enchant Damage", CC.Red + "2% chance to activate a LVL 10", CC.Red + "Banshee Pet effect when damaged", CC.Red + "2x Outgoing Damage on any hit that makes", CC.Red + "your enemy's (Guided) Rocket Escape", CC.Red + "Immune to (Lethal) Sniper"), true),

    BACK_TO_SCHOOL_2021_AMULET(CC.translate("&8&lWatchman Amulet"), ItemSkinType.AMULET, new WatchmanAmuletSkin(), Arrays.asList(CC.Red + "Pied Piper Effect", CC.Gray + " (Convert summoned enemy Guardians into your", CC.Gray + " own when you hit them. You will also not", CC.Gray + " be hurt by enemy (Obsidian) Guardians.)", CC.Red + "Innmune to World Destroyer Pet", CC.Red + "Immune to Slownesses/Freezes"), true),


    VALENTINES_2022_HELMET(CC.translate("&d&lSSF Helmet"), ItemSkinType.HELMET, new SSFHelmet(), Arrays.asList(CC.Red + "25% chance to have Banshee RKO affect your", CC.Red + "enemy instead of you when proc'd on you", CC.Gray + " (Cooldown: 6 minutes)", CC.Red + "15% chance to bypass enemy (Ehtereal) Dodge", CC.Red + "-15% Incoming Soul Enchant Damage for 15s", CC.Red + "when your health is below 25%", CC.Gray + " (Cooldown: 2 minutes)", CC.Red + "Sacrilege Ability", CC.Gray + " (4% chance when dealing damage to invert your", CC.Gray + "enemy's (Divine) Englighted for 8s, causing them", CC.Gray + " to receive damage instead of healing.)"), true),
    VALENTINES_2022_MUTATED_HELMET(CC.translate("&2&l&nMutated&r &d&lSSF Helmet"), ItemSkinType.HELMET, new MutatedSSFHelmet(), Arrays.asList(CC.Red + "25% chance to have Banshee RKO affect your", CC.Red + "enemy instead of you when proc'd on you", CC.Gray + " (Cooldown: 6 minutes)", CC.Red + "15% chance to bypass enemy (Ehtereal) Dodge", CC.Red + "-15% Incoming Soul Enchant Damage for 15s", CC.Red + "when your health is below 25%", CC.Gray + " (Cooldown: 2 minutes)", CC.Red + "Sacrilege Ability", CC.Gray + " (4% chance when dealing damage to invert your", CC.Gray + "enemy's (Divine) Englighted for 8s, causing them", CC.Gray + " to receive damage instead of healing.)", CC.DarkGreen + "Sacrifice Ability", CC.Gray + " (2.5% chance when taking damage to invert", CC.Gray + " Divine Immolation tick damage for 8s, causing", CC.Gray + " it to heal instead of hurt you.)"), true),

    VALENTINES_2022_AMULET(CC.translate("&c&lBlood Diamond Amulet"), ItemSkinType.AMULET, new ValentinesAmulet(), Arrays.asList(CC.Red + "50% chance to reduce Blacksmith Pet's EXP", CC.Red + "cost by 25%", CC.Red + "Immune to World Destroyer Cage tick damage", CC.Red + "Immune to Hero Killer", CC.Red + "Dulled Strikes Ability", CC.Gray + " (2.5% chance to increasingly reduce incoming", CC.Gray + " damage per incoming attack up to a max of", CC.Gray + " -15% for 8s. Each hit received during this", CC.Gray + " time will be converted to increased outgoing", CC.Gray + " damage up to a max of +7.5% and increased", CC.Gray + " Soul Enchant damage by +50% for 8s", CC.Gray + " following the initial proc)"), true),

    VALENTINES_2022_BACKPACK(CC.translate("&d&lBank Roll Backpack"), ItemSkinType.BACKPACK, new ValentinesBackpack(), Arrays.asList(
            CC.translate("&c+50% Opening Combo Damage"),
            CC.translate("&c-25% Incoming Damage from Rogue"),
            CC.translate("&c+10% (Divine) Enlighted Chance")

    ), true),




    VALENTINES_2022_BELT(CC.translate("&c&lBling Belt"), ItemSkinType.BELT, new ValentinesBelt(), Arrays.asList(CC.Red + "+8 % Outgoing Damage", CC.Red + "+15% Pet Leash Negation", CC.Red + "10% Chance to block Rogue"), true),
    VALENTINES_2022_MUTATED_BELT(CC.translate("&2&l&nMutated&r &c&lBling Belt"), ItemSkinType.BELT, new MutatedValentinesBelt(), Arrays.asList(CC.Red + "+8 % Outgoing Damage", CC.Red + "+15% Pet Leash Negation", CC.Red + "10% Chance to block Rogue", CC.DarkGreen + "Soul Master VI"), true),

    VALENTINES_2022_BOOTS(CC.translate("&d&lGolden Boots"), ItemSkinType.BOOTS, new ValentinesBoots(), Arrays.asList(
            CC.Red + "Gears IV",
            CC.Red + "1.1x Critical Damage",
            CC.Red + "10% Chance to negate Incoming Knockback",
            CC.Red + "Immune to Witcher Pet"
    ), true),


    VALENTINES_2022_MUTATED_BOOTS(CC.translate("&2&l&nMutated&r &d&lGolden Boots"), ItemSkinType.BOOTS, new MutatedValentinesBoots(), Arrays.asList(
            CC.Red + "Gears IV",
            CC.Red + "1.1x Critical Damage",
            CC.Red + "10% Chance to negate Incoming Knockback",
            CC.Red + "Immune to Witcher Pet",
            CC.DarkGreen + "1.25x World Destroyer Duration"
    ), true),


    VALENTINES_2022_SWORD(CC.translate("&c&lSkeleton Key"), ItemSkinType.SWORD, new ValentinesSword(), Arrays.asList(
            CC.translate("&c+10% Rage Damage"),
            CC.translate("&c+1 Max Rage Stack"),
            CC.translate("&c+10% Outgoing Damage to players on Phoenix CD")), true),

    VALENTINES_2022_MUTATED_SWORD(CC.translate("&2&l&nMutated&r &c&lSkeleton Key"), ItemSkinType.SWORD, new MutatedValentinesSword(), Arrays.asList(
            CC.translate("&c+10% Rage Damage"),
            CC.translate("&c+1 Max Rage Stack"),
            CC.translate("&c+10% Outgoing Damage to players on Phoenix CD"),
            CC.translate("&2+25% Chance to Block Utility Belt")
    ), true),


    SPRING_BREAK_2022_BOOTS(CC.translate("&a&lSink or Swim Flippers"), ItemSkinType.BOOTS, new SinkOrSwipFlippers(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Inverts enemy Ragdoll", CC.Red + "Immune to Freezes", CC.Red + "When wielding a sword:", CC.Gray + " * Deal up to +25% damage if hitting your", CC.Gray + " enemy from behind", CC.Gray + " * 6% chance to silence enemy (Extreme)", CC.Gray + " Insanity and (Vampiric) Devour for 4s.", CC.Red + "Sink Effect", CC.Gray + " * Gain a stack when you negate a freeze.", CC.Gray + " * At 7 stacks, of the next 5 hits affecting", CC.Gray + " your enemy, they will receive Sink Effect", CC.Gray + " tick damage for 15s."), true),
    SPRING_BREAK_2022_MUTATED_BOOTS(CC.translate("&2&l&nMutated&r &a&lSink or Swim Flippers"), ItemSkinType.BOOTS, new MutatedSinkOrSwipFlippers(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Inverts enemy Ragdoll", CC.Red + "Immune to Freezes", CC.Red + "When wielding a sword:", CC.Gray + " * Deal up to +25% damage if hitting your", CC.Gray + " enemy from behind", CC.Gray + " * 6% chance to silence enemy (Extreme)", CC.Gray + " Insanity and (Vampiric) Devour for 4s.", CC.Red + "Sink Effect", CC.Gray + " * Gain a stack when you negate a freeze.", CC.Gray + " * At 7 stacks, of the next 5 hits affecting", CC.Gray + " your enemy, they will receive Sink Effect", CC.Gray + " tick damage for 15s.", CC.DarkGreen + "Swim Effect", CC.Gray + " * Receive the following effects when hitting", CC.Gray + " enemies with your Sing Effect tick damage:", CC.Gray + "  * +12.5% Outgoing Damage", CC.Gray + "  * Absolute Bypass of enemy Holy White", CC.Gray + "  Scrolls (for 1 piece only)"), true),
    SPRING_BREAK_2022_SWORD_YELLOW(CC.translate("&e&lWet Noodle Sword"), ItemSkinType.SWORD, new WetNoodleSword(), Arrays.asList(
            CC.Red + "For every 14HP you lose, you will deal +33%",
            CC.Red + "Outgoing Damage for 2s.",
            CC.Red + "2.5% chance when dealing damage to activate",
            CC.Red + "Wet Noodle Ability for 10s:",
            CC.Gray + " * All enemies you hit will receive +1 stack",
            CC.Gray + " * After 10s, all affected enemies will receive",
            CC.Gray + " -0.25HP for each stack they have (up to a",
            CC.Gray + " max of 50% of their current health)",
            CC.Gray + " * The enemy you hit to proc this ability will",
            CC.Gray + " also receive the following effects:",
            CC.Gray + "  * Mutated Crystal Silence for 10s",
            CC.Gray + "  * +7.5% Incoming Mutated Crystal Damage",
            CC.Gray + "  for 10s"), true),

    SPRING_BREAK_2022_HELMET(CC.translate("&a&lUndertow Helmet"), ItemSkinType.HELMET, new UndertowHelmet(), Arrays.asList(
            CC.translate("&c1.5% chance when taking damage to summon a"),
            CC.translate("&c5 block Undertow Field for 15s. You receive:"),
            CC.translate(" &7* Immune to Pets"),
            CC.translate(" &7* +3% Outgoing Soul Damage per enemy stacks"),
            CC.translate("&cAll enemies in the field will receive:"),
            CC.translate(" &7* +1 stack per second for 5s (max 3 stacks)"),
            CC.translate(" &7For each active stack:"),
            CC.translate("  &7* -1HP per second"),
            CC.translate("  &7* +3% Incoming Damage"),
            CC.translate(" &7* Phoenix Silence"),
            CC.translate(" &7* Inventory Pet Silence")
    ), true),

    SPRING_BREAK_2022_AMULET(CC.translate("&e&lCannonball!!! Amulet"), ItemSkinType.AMULET, new CannonballAmulet(), Arrays.asList(
            CC.translate("&cCrouch to activate Cannonball Ability in 3s."),
            CC.translate("&cWhen activated, you will receive immunity to"),
            CC.translate("&cenemy Armor Set Crystal outgoing abilities"),
            CC.translate("&cfor 8s. Enemies within 8 blocks will receive"),
            CC.translate("&c-3HP and the following effects for 12s:"),
            CC.translate(" &7* Soul and Mastery Enchant Silence"),
            CC.translate(" &7* Limits enemy stackable enchants by 75%"),
            CC.translate(" &7* Ender Pearl Block"),
            CC.translate(" &7(Cooldown: 5 minutes")
    ), true),
    SPRING_BREAK_2022_BACKPACK(CC.translate("&a&lSpray and Pray Backpack"), ItemSkinType.BACKPACK, new SprayPrayBackpack(), Arrays.asList(
            CC.translate("&c2% chance when dealing damage to inflict the"),
            CC.translate("&cfollowing Spray and Pray Ability for 10s:"),
            CC.translate(" &7* -6% Outgoing Damage"),
            CC.translate(" &7* +6% Incoming Damage"),
            CC.translate(" &7* -2HP tick damage per second"),
            CC.translate(" &7* When receiving this tick damage, your enemy"),
            CC.translate(" &7 will infect a nearby enemy, within 7 blocks, with"),
            CC.translate(" &7* the same effects and remaining time.")
    ), true),

    SPRING_BREAK_2022_BELT(CC.translate("&e&lSplash Zone Belt"), ItemSkinType.BELT, new SplashZoneBelt(), Arrays.asList(
            CC.translate("&cAuto Bless"),
            CC.translate("&c-20% Silence duration"),
            CC.translate("&c+10% Outgoing Damage to Silenced enemies")
    ), true),

    EASTER_2022_HELMET(CC.translate("&d&lMad Hat"), ItemSkinType.HELMET, new MadHat(), Arrays.asList(
            CC.translate("&c1.25x Banshee Duration"),
            CC.translate("&cImmune to opening combo enchant"),
            CC.translate("&cBypass Utility Belt Skin")
    ), true),

    EASTER_2022_BACKPACK(CC.translate("&d&lDoor to Wonderland"), ItemSkinType.BACKPACK, new DoorWonderland(), Arrays.asList(
            CC.translate("&cImmune to Hero Killer"),
            CC.translate("&cImmune to trap blockers"),
            CC.translate("&c-35% Silence duration")
    ), true),


    EASTER_2022_BELT(CC.translate("&e&lMischievous Tail"), ItemSkinType.BELT, new Tail(), Arrays.asList(
            CC.translate("&c100% chance to monopoly atleast 1 piece"),
            CC.translate("&cImmune to (Demonic) Lifesteal"),
            CC.translate("&c50% chance to bypass Bloodstained Galoshes")
    ), true),


    EASTER_2022_BOOTS(CC.translate("&d&lMary Janes"), ItemSkinType.BOOTS, new MaryJanes(), Arrays.asList(
            CC.translate("&cGears IV"),
            CC.translate("&cImmune to Amulet of Corruption"),
            CC.translate("&c1.25x Critical Damage"),
            CC.translate("&cImmune to slowness and freezes")
    ), true),

    EASTER_2022_SWORD(CC.translate("&e&lQueen's Verdict"), ItemSkinType.SWORD, new QueensVerdict(), Arrays.asList(
            CC.translate("&cBypass enemy (Paladin) Armored"),
            CC.translate("&cIncrease outgoing soul enchant damage by 1.3x"),
            CC.translate("&c15% chance to bypass enemy Ancient Crystals"),
            CC.translate("&cDevour VI"),
            CC.translate("&c20% chance to block enemy Divine Immolation")
    ), true),

    EASTER_2022_AMULET(CC.translate("&e&lCheshire Grin"), ItemSkinType.AMULET, new Grin(), Arrays.asList(
            CC.translate("&c-30% Incoming Rage Damage"),
            CC.translate("&cImmune to Divine Light Enchant"),
            CC.translate("&c-5% Incoming damage")
    ), true),


    JULY4TH_2022_SWORD(CC.translate("&a&lPrickle Pear"), ItemSkinType.SWORD, new PricklePear(), Arrays.asList(
            CC.translate("&c15% Chance to break enemy Rage Stacks"),
            CC.translate("&c+4% Outgoing Damage"),
            CC.translate("&cSilence V"),
            CC.translate("&cPrickle Chaos"),
            CC.translate("&7 Chance to struck enemy with a"),
            CC.translate("&7 prickle and make them bleed for 5s.")
    ), true),

    JULY4TH_2022_BELT(CC.translate("&6&lCowboy Chaps"), ItemSkinType.BELT, new CowboyChaps(), Arrays.asList(
            CC.translate("&cImmune to Stitches Pet Ability"),
            CC.translate("&c+6% Outgoing Damage"),
            CC.translate("&cGodly Overload IV"),
            CC.translate("&cAuto Bless")
    ), true),


    PRISONS_DARKSTAR_PICKAXE_BLUE(CC.translate("&3&lDarkstar Chaser"), ItemSkinType.AXE, new DarkstarAxe(), Arrays.asList(
            CC.translate("&cBypass enemy Cupids Heart"),
            CC.translate("&cOpening Combo VI"),
            CC.translate("&cPerfect Solitude IV"),
            CC.translate("&c+15% Outgoing Damage to players wielding Swords"),
            CC.translate("&cBypass enemy (Heroic) Tank"),
            CC.translate("&c15% Chance to block Divine Immolation")

    ), true),


    //=================
    //NEW
    //=================

    BACK_TO_SCHOOL_2022_HELMET(CC.translate("&2&lLamp Shade"), ItemSkinType.HELMET, new LampShadeSkin(), Arrays.asList(
            CC.translate("&cImmune to Amulet of Corruption"),
            CC.translate("&cImmune to World Destroyer Cage"),
            CC.translate("&c3% chance when taking damage to make you"),
            CC.translate("&cand all allies within a 7 block radius"),
            CC.translate("&cImmune to Soul Enchant Damage for 10s")
    ), true),

    BACK_TO_SCHOOL_2022_BOOTS(CC.translate("&2&lCrocs \"Gators\""), ItemSkinType.BOOTS, new CrocsGatorsSkin(), Arrays.asList(
            CC.translate("&c33% chance to reflect cage back at the enemy"),
            CC.translate("&cImmune to Slownesses/Freezes"),
            CC.translate("&c2% chance when Cactus procs to deal +10% Outgoing"),
            CC.translate("&cDamage and receive -3% Incoming Damage for 5s")), true),

    BACK_TO_SCHOOL_2022_BACKPACK(CC.translate("&8&lKeg"), ItemSkinType.BACKPACK, new KegSkin(), Arrays.asList(
            CC.translate("&c-75% Silence Duration"),
            CC.translate("&cBlocks all enemy Ragdoll and Rocket Escape"),
            CC.translate("&c-15% Incoming Soul Enchant Damage")
    ), true),

    BACK_TO_SCHOOL_2022_AXE(CC.translate("&8&lCalculator"), ItemSkinType.AXE, new CalculatorSkin(), Arrays.asList(
            CC.translate("&cOpening Combo V"),
            CC.translate("&cSoul Master IV"),
            CC.translate("&c50% chance to block Divine Immolation"),
            CC.translate("&c3% chance when dealing damage to silence"),
            CC.translate("&cyour enemy's Heroic Weapon Enchants for 5s"),
            CC.translate("&c2.5% chance when dealing damage to make your"),
            CC.translate("&cenemy Soul Enchants cost 10x more for 5s")
    ), true),



    HALLOWEEN_2022_HELMET(CC.translate("&4&lGrim Hood"), ItemSkinType.HELMET, new GrimHoodSkin(), Arrays.asList(
            CC.translate("&cAlien Implants III"),
            CC.translate("&cImmune to (Titan) Trap"),
            CC.translate("&c+10% Outgoing Damage to players wearing:"),
            CC.translate("&7* Grim Hood /skin"),
            CC.translate("&7* Grim Cloak /skin"),
            CC.translate("&7* Momento Mori /skin")
    ), true),
    HALLOWEEN_2022_AMULET(CC.translate("&4&lMomento Mori"), ItemSkinType.AMULET, new MomentoMoriSkin(), Arrays.asList(
            CC.translate("&cImmune to Rogue Enchant"),
            CC.translate("&c30 Chance to block Champion Ability"),
            CC.translate("&c+5% Outgoing Damage to players wielding Diamond Weapons")
    ), true),
    HALLOWEEN_2022_BACKPACK(CC.translate("&4&lGrim Cloak"), ItemSkinType.BACKPACK, new GrimCloakSkin(), Arrays.asList(
            CC.translate("&c-50% Incoming Rogue Damage"),
            CC.translate("&c1.25x Rage Damage Multiplier"),
            CC.translate("&c+25% Extra Damage from behind")
    ), true),
    HALLOWEEN_2022_AXE(CC.translate("&4&lGrim Axe"), ItemSkinType.AXE, new GrimAxeSkin(), Arrays.asList(
            CC.translate("&cOpening Combo V"),
            CC.translate("&cSoul Master VI"),
            CC.translate("&c5% chance to silence enemy Soul Harvester for 3s"),
            CC.translate("&c+15% Outgoing Damage to enemies with Soul Mode active")
    ), true),



    THANKSGIVING_2022_HELMET(CC.translate("&6&lDaft Helmet"), ItemSkinType.HELMET, new DaftHelmetSkin(), Arrays.asList(
            CC.translate("&cImmune to enemy Amulet of Corruption"),
            CC.translate("&c2% chance to silence enemy Soul Enchants for 5s"),
            CC.translate("&c2% chance when taking damage to invert"),
            CC.translate("&cDivine Immolation tick damage for 5s"),
            CC.translate("&ccausing it to heal instead of hurt you.")), true),

    THANKSGIVING_2022_BELT(CC.translate("&e&lMatrix Master Belt"), ItemSkinType.BELT, new MatrixMasterSkin(), Arrays.asList(
            CC.translate("&cLimits enemy stackable enchant level by 50%"),
            CC.translate("&cImmune to World Destroyer Pet"),
            CC.translate("&c10% chance to block Soul Enchant Damage Multipliers"),
            CC.translate("&c25% chance to block Ultra Rage")), true),

    THANKSGIVING_2022_BOOTS(CC.translate("&6&lMainframe Boots"), ItemSkinType.BOOTS, new MainframeSkin(), Arrays.asList(
            CC.translate("&cGears IV"),
            CC.translate("&c5% chance to silence enemy Heroic Enchant Reflect for 5s"),
            CC.translate("&cImmune to Deep Bleed slowness and Soul Trap"),
            CC.translate("&c5% chance to negate enemy Rogue for 5s when their health is below 20 HP")), true),

    THANKSGIVING_2022_SWORD_GREEN(CC.translate("&e&lRay Sabre"), ItemSkinType.SWORD, new RaySabreSkin(), Arrays.asList(
            CC.translate("&cSoul Master VI"),
            CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.25x"),
            CC.translate("&c33% chance to block Divine Immolation"),
            CC.translate("&c3% chance to block enemy Defensive Crystals for 7s")), true),


    CHRISTMAS_2022_HELMET_ACTIVE(CC.translate("&9&lFrost Monster Trophy"), ItemSkinType.HELMET, new FrostMonsterSkin(), Arrays.asList(
            CC.translate("&cImmune to Banshee Pet"),
            CC.translate("&c50% chance to block Divine Immolation"),
            CC.translate("&c-15% Incoming Damage from enemies wielding Axes"),
            CC.translate("&c+5% Outgoing Damage to enemies wielding Swords"),
            CC.translate("&c50% chance to dodge offensive Titan Attributes")), true),

    CHRISTMAS_2022_AMULET_ACTIVE(CC.translate("&b&lIcicle Amulet"), ItemSkinType.AMULET, new IcicleSkin(), Arrays.asList(
            CC.translate("&cImmune to enemy Amulet of Corruption"),
            CC.translate("&cImmune to Divine Immolation"),
            CC.translate("&cImmune to Hero Killer"),
            CC.translate("&c+15% Outgoing Soul Enchant Damage"),
            CC.translate("&c-25% damage from enemy Rage")), true),

    CHRISTMAS_2022_SWORD_ACTIVE(CC.translate("&b&lFrostbound Sword"), ItemSkinType.SWORD, new FrostBoundSkin(), Arrays.asList(
            CC.translate("&cOpening Combo VI"),
            CC.translate("&c2.5% chance to silence enemy (Demonic) Lifesteal for 3s"),
            CC.translate("&c1% chance to silence enemy Titan Attributes for 3s"),
            CC.translate("&c+5% Outgoing Damage"),
            CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.25x")), true),

    CHRISTMAS_2022_BACKPACK(CC.translate("&9&lArctic Excursion Pack"), ItemSkinType.BACKPACK, new ArcticExcursionSkin(), Arrays.asList(
            CC.translate("&c5% chance to silence enemy Ragdoll for 5s"),
            CC.translate("&c10% chance to block Soul Enchant Damage Multipliers"),
            CC.translate("&c+5% Outgoing PvP Damage when using a sword"),
            CC.translate("&cImmune to enemy Rogue when below 10 HP")), true),


    VALENTINES_2023_BACKPACK(CC.translate("&5&lLucifer Wings"), ItemSkinType.BACKPACK, new LuciferBackpack(), Arrays.asList(
            CC.translate("&c5% chance to silence enemy Ragdoll for 5s"),
            CC.translate("&c10% chance to block Soul Enchant Damage Multipliers"),
                    CC.translate("&c+5% Outgoing PvP Damage when using a sword"),
                    CC.translate("&cImmune to enemy Rogue when below 10 HP")), true),


    VALENTINES_2023_AMULET(CC.translate("&5&lLucifer Amulet"), ItemSkinType.AMULET, new LuciferAmulet(), Arrays.asList(
            CC.translate("&c5% chance to silence enemy Ragdoll for 5s"),
            CC.translate("&c10% chance to block Soul Enchant Damage Multipliers"),
                    CC.translate("&c+5% Outgoing PvP Damage when using a sword"),
                    CC.translate("&cImmune to enemy Rogue when below 10 HP")), true),

    JULY4TH_2022_HELMET(CC.translate("&6&lCowboy Hat"), ItemSkinType.HELMET, new CowboyHat(), Arrays.asList(
            CC.translate("&c+15% Outgoing Crystal Damage"),
            CC.translate("&cImmune to Hero Killer"),
            CC.translate("&cImmune to (Heroic) Lifesteal"),
            CC.translate("&c-20% Damage from enemy (Bloody) Deep Wounds"),
            CC.translate("&c15% Pet Leash Negation")
    ), true),



    EASTER_2022_MUTATED_BACKPACK(CC.translate("&2&l&nMutated&r &d&lDoor to Wonderland"), ItemSkinType.BACKPACK, new MutatedDoorWonderland(), Arrays.asList(
            CC.translate("&cImmune to Hero Killer"),
            CC.translate("&cImmune to trap blockers"),
            CC.translate("&c-35% Silence duration"),
            CC.translate("&2Escape Ability"),
            CC.translate("&7 When sneaking, remove all (trap) doors, fence"),
            CC.translate("&7 gates and ladders in a 6x6 radius (15m cooldown)")
    ), true),

    EASTER_2022_MUTATED_BELT(CC.translate("&2&l&nMutated&r &e&lMischievous Tail"), ItemSkinType.BELT, new MutatedTail(), Arrays.asList(
            CC.translate("&c100% chance to monopoly atleast 1 piece"),
            CC.translate("&cImmune to (Demonic) Lifesteal"),
            CC.translate("&c50% chance to bypass Bloodstained Galoshes"),
            CC.translate("&2Stun Ability"),
            CC.translate("&7 Chance to stun nearby enemies upon being hit")
    ), true),

    EASTER_2022_MUTATED_BOOTS(CC.translate("&2&l&nMutated&r &d&lMary Janes"), ItemSkinType.BOOTS, new MutatedMaryJanes(), Arrays.asList(
            CC.translate("&cGears IV"),
            CC.translate("&cImmune to Amulet of Corruption"),
            CC.translate("&c1.25x Critical Damage"),
            CC.translate("&cImmune to slowness and freezes"),
            CC.translate("&2-33% Rogue Enchant damage")
    ), true),

    EASTER_2022_MUTATED_SWORD(CC.translate("&2&l&nMutated&r &e&lQueen's Verdict"), ItemSkinType.SWORD, new MutatedQueensVerdict(), Arrays.asList(
            CC.translate("&cBypass enemy (Paladin) Armored"),
            CC.translate("&cIncrease outgoing soul enchant damage by 1.3x"),
            CC.translate("&c15% chance to bypass enemy Ancient Crystals"),
            CC.translate("&cDevour VI"),
            CC.translate("&c20% chance to block enemy Divine Immolation"),
            CC.translate("&2Double Strike IV")
    ), true),

    EASTER_2022_MUTATED_AMULET(CC.translate("&2&l&nMutated&r &e&lCheshire Grin"), ItemSkinType.AMULET, new MutatedGrin(), Arrays.asList(
            CC.translate("&c-30% Incoming Rage Damage"),
            CC.translate("&cImmune to Divine Light Enchant"),
            CC.translate("&c-5% Incoming damage"),
            CC.translate("&2Immune to Stitches & Skrrt Pet")
    ), true),

    SPRING_BREAK_2022_MUTATED_SWORD_YELLOW(CC.translate("&2&l&nMutated&r &e&lWet Noodle Sword"), ItemSkinType.SWORD, new MutatedWetNoodleSword(), Arrays.asList(CC.Red + "For every 14HP you lose, you will deal +33%",
            CC.Red + "Outgoing Damage for 2s.",
            CC.Red + "2.5% chance when dealing damage to activate",
            CC.Red + "Wet Noodle Ability for 10s:",
            CC.Gray + " * All enemies you hit will receive +1 stack",
            CC.Gray + " * After 10s, all affected enemies will receive",
            CC.Gray + " -0.25HP for each stack they have (up to a",
            CC.Gray + " max of 50% of their current health)",
            CC.Gray + " * The enemy you hit to proc this ability will",
            CC.Gray + " also receive the following effects:",
            CC.Gray + "  * Mutated Crystal Silence for 10s",
            CC.Gray + "  * +7.5% Incoming Mutated Crystal Damage",
            CC.Gray + "  for 10s",
            CC.DarkGreen + "For each player hit while your Wet Noodle effect",
            CC.DarkGreen + "is active, the duration will increase by 2s.",
            CC.Gray + " (up to a maximum of increase of 16s)"), true),
    SPRING_BREAK_2022_MUTATED_HELMET(CC.translate("&2&l&nMutated&r &a&lUndertow Helmet"), ItemSkinType.HELMET, new MutatedUndertowHelmet(), Arrays.asList(
            CC.translate("&c1.5% chance when taking damage to summon a"),
            CC.translate("&c5 block Undertow Field for 15s. You receive:"),
            CC.translate(" &7* Immune to Pets"),
            CC.translate(" &7* +3% Outgoing Soul Damage per enemy stacks"),
            CC.translate(" &2* No EXP Pet Costs"),
            CC.translate("&cAll enemies in the field will receive:"),
            CC.translate(" &7* +1 stack per second for 5s (max 3 stacks)"),
            CC.translate(" &7For each active stack:"),
            CC.translate("  &7* -1HP per second"),
            CC.translate("  &7* +3% Incoming Damage"),
            CC.translate(" &7* Phoenix Silence"),
            CC.translate(" &7* Inventory Pet Silence"),
            CC.translate(" &2* Ragdoll Silence"),
            CC.translate(" &2* Custom Block Effect Silence:"),
            CC.translate("  &7(Cauldron, Frosted Snowman, Heart Cookies,"),
            CC.translate("  &7Pumpkin Pie, Roast Turkey, Teddy Bear")
    ), true),
    SPRING_BREAK_2022_MUTATED_BACKPACK(CC.translate("&2&l&nMutated&r &a&lSpray and Pray Backpack"), ItemSkinType.BACKPACK, new MutatedSprayPrayBackpack(), Arrays.asList(
            CC.translate("&c2% chance when dealing damage to inflict the"),
            CC.translate("&cfollowing Spray and Pray Ability for 10s:"),
            CC.translate(" &7* -6% Outgoing Damage"),
            CC.translate(" &7* +6% Incoming Damage"),
            CC.translate(" &7* -2HP tick damage per second"),
            CC.translate(" &7 When receiving this tick damage, your enemy"),
            CC.translate(" &7 will infect a nearby enemy, within 7 blocks, with"),
            CC.translate(" &7 the same effects and remaining time."),
            CC.translate(" &2* A nearby ally, within 7 blocks, will also"),
            CC.translate(" &2receive Regeneration I for the remaining time.")
    ), true),
    SPRING_BREAK_2022_MUTATED_BELT(CC.translate("&2&l&nMutated&r &e&lSplash Zone Belt"), ItemSkinType.BELT, new MutatedSplashZoneBelt(), Arrays.asList(
            CC.translate("&cAuto Bless"),
            CC.translate("&c-20% Silence duration"),
            CC.translate("&c+10% Outgoing Damage to Silenced enemies"),
            CC.translate("&2Immune to Lifesteal"),
            CC.translate("&2Immune to Opening Combo")

    ), true),

    EASTER_2022_MUTATED_HELMET(CC.translate("&2&l&nMutated&r &d&lMad Hat"), ItemSkinType.HELMET, new MutatedMadHat(), Arrays.asList(
            CC.translate("&c1.25x Banshee Duration"),
            CC.translate("&cImmune to opening combo enchant"),
            CC.translate("&cBypass Utility Belt Skin"),
            CC.translate("&2Chance to fully heal your hp when below 14HP")
    ), true),

    THANKSGIVING_2021_MUTATED_AXE(CC.translate("&2&l&nMutated&r &6&lWhisk Taker"), ItemSkinType.AXE, new MutatedWhiskTaker(), Arrays.asList(CC.Red + "3% chance when hit to proc an AoE", CC.Red + "explosion which deals 5HP to all", CC.Red + "enemies in a 5x5 radius and makes", CC.Red + "deal +20% Outgoing Damage to", CC.Red + "all affected enemies for 6s", CC.Red + "+2.5 Outgoing Damage", CC.Red + "15% chance when your Rogue procs", CC.Red + "to make your enemy 45% slower for 1s", CC.Red + "5% chance to Teleblock", CC.Red + "4% chance to silence your enemies", CC.Red + "Divine Immolation for 8s", CC.DarkGreen + "Immune to Soul Tether", CC.DarkGreen + "+3% Outgoing Damage to enemies", CC.DarkGreen + "with Soul Mode activated."), true),
    CHRISTMAS_2021_MUTATED_HELMET(CC.translate("&2&l&nMutated&r &2&lKrampus Horns"), ItemSkinType.HELMET, new MutatedKrampusHornsSkin(), Arrays.asList(CC.Red + "-10% Incoming Soul Enchant Damage", CC.Red + "Increases Deadly Disarmor health", CC.Red + "proc threshold by 4 HP", CC.Red + "-45% Silence Reduction", CC.Red + "75% chance to block enemy Ragdoll", CC.Red + "Immune to (Lethal) Sniper", CC.DarkGreen + "Bypass enemy (Holy) Aegis"), true),
    CHRISTMAS_2021_MUTATED_AMULET(CC.translate("&2&l&nMutated&r &4&lCranked Amulet"), ItemSkinType.AMULET, new MutatedCrankedAmuletSkin(), Arrays.asList(CC.Red + "Immune to Hero Killer", CC.Red + "8% chance to double proc offensive Heroic Enchants", CC.Red + "3% chance to silence enemy Soul Enchants for 6s", CC.Red + "Escape Hatch Ability", CC.Gray + " (If your health  is below 8HP and a player uses a", CC.Gray + " Heroic Pet on you, they are launched into the air", CC.Gray + " Cooldown: 2 minutes)", CC.DarkGreen + "Mage Thief Ability", CC.Gray + " (3.5% chance to block nearby enemy's Soul Enchant", CC.Gray + " Damage Multipliers for 10s in 7x7 area. Gain +5%", CC.Gray + " Soul Enchant Damage (up to max +35%) per", CC.Gray + " enemy blocked with Mage Thief.", CC.Gray + " Cooldown: 2 minutes)"), true),

    HALLOWEEN_2021_MUTATED_SWORD(CC.translate("&2&l&nMutated&r &6&lSpinal Tap"), ItemSkinType.SWORD, new MutatedSpinalTab(), Arrays.asList(CC.Red + "Hysteria Ability", CC.Gray + " (Every player you hit will grant you 1 Hysteria", CC.Gray + " stack up to a max of 5 stacks for 4s. For every", CC.Gray + " stack you have, you will receive +2% Outgoing", CC.Gray + " On your first hit at 5 stacks, you are", CC.Gray + " guaranteed to proc Blackout 4)", CC.Red + "2% chance to proc Rogue II", CC.Red + "when attacking enemies from behind", CC.Red + "4% chance to negate enemy Witch Hats for 10s", CC.DarkGreen + "10% chance to block Divine Immolation"), true),
    CHRISTMAS_2021_MUTATED_SWORD(CC.translate("&2&l&nMutated&r &4&lSeason's Beatings"), ItemSkinType.SWORD, new MutatedSeasonsBeatings(), Arrays.asList(CC.Red + "+7% Outgoing Damage to players wielding weapons with Hero Killer", CC.Red + "+10% Outgoing Damage to enemies with Soul Mode Activated", CC.Red + "3.5% chance to silence enemy (Reflective) Block for 8s", CC.Red + "Outgoing Damage while Frozen or Slowed has a 12.5% chance", CC.Red + "to remove the Freeze or Slow effect", CC.Red + "+10% Rage Damage", CC.DarkGreen + "Soul Master VI"), true),
    THANKSGIVING_2021_MUTATED_BACKPACK(CC.translate("&2&l&nMutated&r &6&lTommy Tail"), ItemSkinType.BACKPACK, new MutatedTommyTail(), Arrays.asList(CC.Red + "+5% Outgoing PvP Damage when using a sword", CC.Red + "Immune to Hero Killer", CC.Red + "7.5% chance to block Soul Enchant Damage", CC.Red + "2% chance to activate a LVL 10", CC.Red + "Banshee Pet effect when damaged", CC.Red + "2x Outgoing Damage on any hit that makes", CC.Red + "your enemy's (Guided) Rocket Escape", CC.Red + "Immune to (Lethal) Sniper", CC.DarkGreen + "Increases outgoing damage by up to 10%", CC.DarkGreen + "when under 60% HP"), true),
    VALENTINES_2022_MUTATED_BACKPACK(CC.translate("&2&l&nMutated&r &d&lBank Roll Backpack"), ItemSkinType.BACKPACK, new MutatedValentinesBackpack(), Arrays.asList(
            CC.translate("&c+50% Opening Combo Damage"),
            CC.translate("&c-25% Incoming Damage from Rogue"),
            CC.translate("&c+10% (Divine) Enlighted Chance"),
            CC.translate("&2Immune to Divine Ticks"),
            CC.translate("&2Immune to Lifesteal")


    ), true),
    VALENTINES_2022_MUTATED_HELMET(CC.translate("&2&l&nMutated&r &d&lSSF Helmet"), ItemSkinType.HELMET, new MutatedSSFHelmet(), Arrays.asList(CC.Red + "25% chance to have Banshee RKO affect your", CC.Red + "enemy instead of you when proc'd on you", CC.Gray + " (Cooldown: 6 minutes)", CC.Red + "15% chance to bypass enemy (Ehtereal) Dodge", CC.Red + "-15% Incoming Soul Enchant Damage for 15s", CC.Red + "when your health is below 25%", CC.Gray + " (Cooldown: 2 minutes)", CC.Red + "Sacrilege Ability", CC.Gray + " (4% chance when dealing damage to invert your", CC.Gray + "enemy's (Divine) Englighted for 8s, causing them", CC.Gray + " to receive damage instead of healing.)", CC.DarkGreen + "Sacrifice Ability", CC.Gray + " (2.5% chance when taking damage to invert", CC.Gray + " Divine Immolation tick damage for 8s, causing", CC.Gray + " it to heal instead of hurt you.)"), true),
    VALENTINES_2022_MUTATED_AMULET(CC.translate("&2&l&nMutated&r &c&lBlood Diamond Amulet"), ItemSkinType.AMULET, new MutatedValentinesAmulet(), Arrays.asList(CC.Red + "50% chance to reduce Blacksmith Pet's EXP", CC.Red + "cost by 25%", CC.Red + "Immune to World Destroyer Cage tick damage", CC.Red + "Immune to Hero Killer", CC.Red + "Dulled Strikes Ability", CC.Gray + " (2.5% chance to increasingly reduce incoming", CC.Gray + " damage per incoming attack up to a max of", CC.Gray + " -15% for 8s. Each hit received during this", CC.Gray + " time will be converted to increased outgoing", CC.Gray + " damage up to a max of +7.5% and increased", CC.Gray + " Soul Enchant damage by +50% for 8s", CC.Gray + " following the initial proc)", CC.DarkGreen + "World Destroyer Pet tick damage", CC.DarkGreen + "heals instead of hurts you", CC.DarkGreen + "50% chance to reduce Blacksmith Pet's EXP", CC.DarkGreen + "cost by a total of 50%"), true),

    MMM_JULY4TH_2022_HELMET(CC.translate("&2&l&nMutated&r &6&lCowboy Hat"), ItemSkinType.HELMET, new MutatedCowboyHat(), Arrays.asList(
            CC.translate("&c+15% Outgoing Crystal Damage"),
            CC.translate("&cImmune to Hero Killer"),
            CC.translate("&cImmune to (Heroic) Lifesteal"),
            CC.translate("&c-20% Damage from enemy (Bloody) Deep Wounds"),
            CC.translate("&c15% Pet Leash Negation"),
            CC.translate("&2Chance to proc Mutated Banshee on enemies")
    ), true),

     */


    SANTA_HAT(CC.RedB + "Santa Hat", ItemSkinType.HELMET, new SantaHatSkin(), Arrays.asList(CC.Red + "+5% DMG", CC.Red + "-5% DMG"), true),
    CORRUPT_HELMET(CC.DarkPurpleB + "Corrupt Cosmonaut Visor", ItemSkinType.HELMET, new CorruptHelmetSkin(), Arrays.asList(CC.Red + "50% Chance to block Neutralize", CC.Red + "50% Silence Duration"), true),
    HALLOWEEN_HELMET(CC.GoldB + "Candy Collector", ItemSkinType.HELMET, new CandyCollectorSkin(), Arrays.asList(CC.Red + "-20% incoming damage from enemies wielding swords", CC.Red + "1% chance to activate witcher ability"), true),
    RUINS_HELMET(CC.translate("&3&lLeviathan Trophy"), ItemSkinType.HELMET, new LeviathanTrophySkin(), Arrays.asList(CC.translate("&cIncreased chance to drop Ancient Scrolls"), CC.translate("&cwhen killing Guardians in /ruins"), CC.translate("&cImmune to enemy Amulet of Corruption"), CC.translate("&c+1 Max Heart"), CC.translate("&c3% chance to silence enemy Soul Enchants"), CC.translate("  &7(7x7 blocks for 7s)")), true),
    VALENTINES_2021_HELMET(CC.translate("&c&lHeart Halo"), ItemSkinType.HELMET, new HearthHaloSkin(), Arrays.asList(CC.translate("&cPassively removes 1 Bleedstack every 2s"), CC.translate("&cImmune to Soul Harvester"), CC.translate("&c1.25% (Divine) Enlighted Healing")), true),
    BACK_TO_SCHOOL_2021_HELMET(CC.translate("&6&lPylon Head"), ItemSkinType.HELMET, new PylonHeadSkin(), Arrays.asList(CC.Red + "Reduces the duration of active Silence", CC.Red + "or Blackout by 40% on outgoing damage.", CC.Red + "Limits enemy Amulet of Corruption so that it", CC.Red + "affects defensive Armor Crystal attributes only"), true),
    CHRISTMAS_HELMET_STAR(CC.translate("&a&lNorth Star"), ItemSkinType.HELMET, new NorthStarSkin(), Arrays.asList(CC.translate("&c33% Chance to negate Monopoly Mask"), CC.translate("&c-33% enemy Blackout duration"), CC.translate("&cBlocks all enemy (Guided) Rocket Escape"), CC.translate("&cDivine Enlighted IV")), true),
    NYE_HELMET(CC.translate("&d&lNeon Emotions"), ItemSkinType.HELMET, new NeonEmotionsSkin(), Arrays.asList(CC.translate("&c+6% Outgoing Damage"), CC.translate("&c-2% Incoming Damage"), CC.translate("&cImmune to Slowness Potion Effect")), true),
    SUMMER_2021_HELMET(CC.translate("&6&lTiki Mask"), ItemSkinType.HELMET, new TikiMaskSkin(), Arrays.asList(CC.translate("&cLimites enemy amulet of corruption to 1 set type"), CC.translate("&c25% chance to block enemy Silence"), CC.translate("&cLimits enemy Silence to 60% duration")), true),
    HALLOWEEN_2021_HELMET(CC.translate("&6&lMineman Muzzle"), ItemSkinType.HELMET, new MinemanMuzzleSkin(), Arrays.asList(CC.Red + "25% chance to curse enemies with +33% Incoming", CC.Red + "Damage when their Phoenix procs for 10s", CC.Gray + " (Can only proc once every 10m)", CC.Red + "3.5% chance to negate enemy Cosmic Cape for 10s", CC.Red + "Immune to Banshee RKO", CC.Red + "10% chance to reflect damage to enemies in a 3x3 radius"), true),
    THANKSGIVING_HELMET_WITCH(CC.translate("&e&lWitch Hat"), ItemSkinType.HELMET, new WitchHatSkin(), Arrays.asList(CC.translate("&c-25% Silence duration"), CC.translate("&cImmune to offensive Titan Attributes"), CC.translate("&7(Atlas, Ouranos and Prometheus)"), CC.translate("&cImmune to Trap"), CC.translate("&cImmune to Bloodhound & Banshee Pet"), CC.translate("&c10% chance to deflect incoming damage"), CC.translate("&conto nearby enemy"), CC.translate("&c2% chance to enable a LVL 10"), CC.translate("&cBanshee Pet effect when damaged")), true),
    SPRING_BREAK_2022_HELMET(CC.translate("&a&lUndertow Helmet"), ItemSkinType.HELMET, new UndertowHelmet(), Arrays.asList(
            CC.translate("&c1.5% chance when taking damage to summon a"),
            CC.translate("&c5 block Undertow Field for 15s. You receive:"),
            CC.translate(" &7* Immune to Pets"),
            CC.translate(" &7* +3% Outgoing Soul Damage per enemy stacks"),
            CC.translate("&cAll enemies in the field will receive:"),
            CC.translate(" &7* +1 stack per second for 5s (max 3 stacks)"),
            CC.translate(" &7For each active stack:"),
            CC.translate("  &7* -1HP per second"),
            CC.translate("  &7* +3% Incoming Damage"),
            CC.translate(" &7* Phoenix Silence"),
            CC.translate(" &7* Inventory Pet Silence")
    ), false),
    SPRING_BREAK_2022_MUTATED_HELMET(CC.translate("&2&l&nMutated&r &a&lUndertow Helmet"), ItemSkinType.HELMET, new MutatedUndertowHelmet(), Arrays.asList(
            CC.translate("&c1.5% chance when taking damage to summon a"),
            CC.translate("&c5 block Undertow Field for 15s. You receive:"),
            CC.translate(" &7* Immune to Pets"),
            CC.translate(" &7* +3% Outgoing Soul Damage per enemy stacks"),
            CC.translate(" &2* No EXP Pet Costs"),
            CC.translate("&cAll enemies in the field will receive:"),
            CC.translate(" &7* +1 stack per second for 5s (max 3 stacks)"),
            CC.translate(" &7For each active stack:"),
            CC.translate("  &7* -1HP per second"),
            CC.translate("  &7* +3% Incoming Damage"),
            CC.translate(" &7* Phoenix Silence"),
            CC.translate(" &7* Inventory Pet Silence"),
            CC.translate(" &2* Ragdoll Silence"),
            CC.translate(" &2* Custom Block Effect Silence:"),
            CC.translate("  &7(Cauldron, Frosted Snowman, Heart Cookies,"),
            CC.translate("  &7Pumpkin Pie, Roast Turkey, Teddy Bear")
    ), false),
    VALENTINES_2022_HELMET(CC.translate("&d&lSSF Helmet"), ItemSkinType.HELMET, new SSFHelmet(), Arrays.asList(CC.Red + "25% chance to have Banshee RKO affect your", CC.Red + "enemy instead of you when proc'd on you", CC.Gray + " (Cooldown: 6 minutes)", CC.Red + "15% chance to bypass enemy (Ehtereal) Dodge", CC.Red + "-15% Incoming Soul Enchant Damage for 15s", CC.Red + "when your health is below 25%", CC.Gray + " (Cooldown: 2 minutes)", CC.Red + "Sacrilege Ability", CC.Gray + " (4% chance when dealing damage to invert your", CC.Gray + "enemy's (Divine) Englighted for 8s, causing them", CC.Gray + " to receive damage instead of healing.)"), true),
    VALENTINES_2022_MUTATED_HELMET(CC.translate("&2&l&nMutated&r &d&lSSF Helmet"), ItemSkinType.HELMET, new MutatedSSFHelmet(), Arrays.asList(CC.Red + "25% chance to have Banshee RKO affect your", CC.Red + "enemy instead of you when proc'd on you", CC.Gray + " (Cooldown: 6 minutes)", CC.Red + "15% chance to bypass enemy (Ehtereal) Dodge", CC.Red + "-15% Incoming Soul Enchant Damage for 15s", CC.Red + "when your health is below 25%", CC.Gray + " (Cooldown: 2 minutes)", CC.Red + "Sacrilege Ability", CC.Gray + " (4% chance when dealing damage to invert your", CC.Gray + "enemy's (Divine) Englighted for 8s, causing them", CC.Gray + " to receive damage instead of healing.)", CC.DarkGreen + "Sacrifice Ability", CC.Gray + " (2.5% chance when taking damage to invert", CC.Gray + " Divine Immolation tick damage for 8s, causing", CC.Gray + " it to heal instead of hurt you.)"), true),
    PEPE_HELMET(CC.translate("&9&lPepe Mask"), ItemSkinType.HELMET, new PepeHelmetSkin(), Arrays.asList(
            CC.translate("&c+7.5% Outgoing damage to enemies"),
            CC.translate("&cImmune to Slowness Potion Effect")
    ), true),
    MMM_PEPE_HELMET(CC.translate("&2&l&nMutated&r &9&lPepe Mask"), ItemSkinType.HELMET, new PepeHelmetSkin(), Arrays.asList(
            CC.translate("&c+7.5% Outgoing damage to enemies"),
            CC.translate("&cImmune to Slowness Potion Effect")
    ), true),
    CHRISTMAS_2022_HELMET_ACTIVE(CC.translate("&9&lFrost Monster Trophy"), ItemSkinType.HELMET, new FrostMonsterSkin(), Arrays.asList(
            CC.translate("&cImmune to Banshee Pet"),
            CC.translate("&c50% chance to block Divine Immolation"),
            CC.translate("&c+15% Damage to enemies wielding weapons")
    ), true),
    EASTER_2022_HELMET(CC.translate("&d&lMad Hat"), ItemSkinType.HELMET, new MadHat(), Arrays.asList(
            CC.translate("&c1.25x Banshee Duration"),
            CC.translate("&cImmune to opening combo enchant"),
            CC.translate("&cBypass Utility Belt Skin")
    ), true),
    EASTER_2022_MUTATED_HELMET(CC.translate("&2&l&nMutated&r &d&lMad Hat"), ItemSkinType.HELMET, new MutatedMadHat(), Arrays.asList(
            CC.translate("&c1.25x Banshee Duration"),
            CC.translate("&cImmune to opening combo enchant"),
            CC.translate("&cBypass Utility Belt Skin"),
            CC.translate("&2Chance to fully heal your hp when below 14HP")
    ), true),
    JULY4TH_2022_HELMET(CC.translate("&6&lCowboy Hat"), ItemSkinType.HELMET, new CowboyHat(), Arrays.asList(
            CC.translate("&c+15% Outgoing Crystal Damage"),
            CC.translate("&cImmune to Hero Killer"),
            CC.translate("&cImmune to (Heroic) Lifesteal"),
            CC.translate("&c-20% Damage from enemy (Bloody) Deep Wounds"),
            CC.translate("&c15% Pet Leash Negation")
    ), true),
    MMM_JULY4TH_2022_HELMET(CC.translate("&2&l&nMutated&r &6&lCowboy Hat"), ItemSkinType.HELMET, new MutatedCowboyHat(), Arrays.asList(
            CC.translate("&c+15% Outgoing Crystal Damage"),
            CC.translate("&cImmune to Hero Killer"),
            CC.translate("&cImmune to (Heroic) Lifesteal"),
            CC.translate("&c-20% Damage from enemy (Bloody) Deep Wounds"),
            CC.translate("&c15% Pet Leash Negation"),
            CC.translate("&2Chance to proc Mutated Banshee on enemies")
    ), true),






    HALLOWEEN_AMULET(CC.GoldB + "Silent Death", ItemSkinType.AMULET, new SilentDeathSkin(), Arrays.asList(CC.Red + "5% chance to afflict enemy", CC.Red + "with Silent Death ability", CC.Gray + " (Enemy will receive 20% Incoming DMG", CC.Gray + " until caster has been hit 5 times", CC.Gray + " or 15 seconds has elapsed.)", CC.Red + "Immune to Silent Death ability", CC.Red + "+3% Outgoing Damage"), true),
    CORRUPT_AMULET_CORRUPTION(CC.DarkPurpleB + "Amulet of Corruption", ItemSkinType.AMULET, new CorruptAmuletCorruptionSkin(), Collections.singletonList(CC.Red + "Immune to enemy armor crystals of same type on attached item."), true),
    CORRUPT_AMULET_DESTRUCTION(CC.DarkPurpleB + "Amulet of Destruction", ItemSkinType.AMULET, new CorruptAmuletDestructionSkin(), Collections.singletonList(CC.Red + "+7.5% Outgoing Damage"), true),
    THANKSGIVING_2021_AMULET(CC.translate("&e&lThe Crumble"), ItemSkinType.AMULET, new TheCrumbleSkin(), Arrays.asList(CC.Red + "7.5% chance to receive -2.5% Incoming", CC.Red + "Damage (up to a max of -15%) for 8s", CC.Red + "when receiving Divine Immolation damage", CC.Red + "Immune to (Obsidian) Guardians", CC.Red + "5% chance to silence your enemy's", CC.Red + "Phoenix for 6s when they're under 15 HP", CC.Red + "Immune to enemy Amulet of Corruption"), true),
    SUMMER_2021_AMULET(CC.translate("&c&lLuau Lei"), ItemSkinType.AMULET, new LuauLei(), Arrays.asList(CC.translate("&c+25% Outgoing Soul Enchant damage"), CC.translate("&cChance to block Soul Enchant Damage Modifiers")), true),
    RUINS_AMULET(CC.translate("&3&lLost Amulet"), ItemSkinType.AMULET, new LostAmulet(), Arrays.asList(CC.translate("&c50% Neutralize Negation"), CC.translate("&c-5% Incoming Damage"), CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.1x")), true),
    CHRISTMAS_AMULET_SNOWFLAKE(CC.RedB + "Preserved Snowflake", ItemSkinType.AMULET, new PreservedSnowflakeSkin(), Arrays.asList(CC.Red + "Immune to Soul Tether, Soul Trap", CC.Red + "Immune to Nature's Wrath", CC.Red + "Able to see invisible player", CC.Gray + " (Feign Death, Undead Ruse)"), true),
    EASTER_2022_AMULET(CC.translate("&e&lCheshire Grin"), ItemSkinType.AMULET, new Grin(), Arrays.asList(
            CC.translate("&c-30% Incoming Rage Damage"),
            CC.translate("&cImmune to Divine Light Enchant"),
            CC.translate("&c-5% Incoming damage")
    ), true),
    EASTER_2022_MUTATED_AMULET(CC.translate("&2&l&nMutated&r &e&lCheshire Grin"), ItemSkinType.AMULET, new MutatedGrin(), Arrays.asList(
            CC.translate("&c-30% Incoming Rage Damage"),
            CC.translate("&cImmune to Divine Light Enchant"),
            CC.translate("&c-5% Incoming damage"),
            CC.translate("&2Immune to Stitches & Skrrt Pet")
    ), true),
    VALENTINES_2021_AMULET(CC.RedB + "Rose Amulet", ItemSkinType.AMULET, new RoseAmuletSkin(), Arrays.asList(CC.Red + "Immune to Snowflake Axe skin abilities", CC.Red + "-25% damage from enemy Rage", CC.Red + "Automatic Phoenix Feather (30m cooldown)"), true),
    HALLOWEEN_2022_AMULET(CC.translate("&4&lMomento Mori"), ItemSkinType.AMULET, new MomentoMoriSkin(), Arrays.asList(
            CC.translate("&cImmune to Rogue Enchant"),
            CC.translate("&c30 Chance to block Champion Ability"),
            CC.translate("&c+5% Outgoing Damage to players wielding Diamond Weapons")
    ), true),
    CHRISTMAS_2022_AMULET_ACTIVE(CC.translate("&b&lIcicle Amulet"), ItemSkinType.AMULET, new IcicleSkin(), Arrays.asList(
            CC.translate("&cImmune to enemy Amulet of Corruption"),
            CC.translate("&cImmune to Divine Immolation"),
            CC.translate("&cImmune to Hero Killer"),
            CC.translate("&c+15% Outgoing Soul Enchant Damage"),
            CC.translate("&c-25% damage from enemy Rage")), true),

    VALENTINES_2023_AMULET(CC.translate("&5&lLucifer Amulet"), ItemSkinType.AMULET, new LuciferAmulet(), Arrays.asList(
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c")), true),

    MMM_VALENTINES_2023_AMULET(CC.translate("&2&l&nMutated&r &5&lLucifer Amulet"), ItemSkinType.AMULET, new LuciferAmulet(), Arrays.asList(
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c")), true),








    VALENTINES_2021_BACKPACK(CC.RedB + "Teddy Backpack", ItemSkinType.BACKPACK, new TeddyBackpackSkin(), Arrays.asList(CC.Red + "Immune to (Lethal) Sniper", CC.Red + "Immune to Hero Killer", CC.Red + "+2% Outgoing Damage"), true),
    JULY_2021_BACKPACK(CC.translate("&f&lCosmic Cape"), ItemSkinType.BACKPACK, new CosmicCapeSkin(), Arrays.asList(CC.Red + "Immune to Hero Killer", CC.Red + "Decreases incoming damage from Soul Enchants by 15%", CC.Red + "2.5% chance to negate enemy Shockwave and (Guided) Rocket", CC.Red + "Escape on outgoing damage for 10s", CC.Red + "Increases outgoing damage by up to 15% when under 50% HP"), true),
    VALENTINES_2022_BACKPACK(CC.translate("&d&lBank Roll Backpack"), ItemSkinType.BACKPACK, new ValentinesBackpack(), Arrays.asList(
            CC.translate("&c+50% Opening Combo Damage"),
            CC.translate("&c-25% Incoming Damage from Rogue"),
            CC.translate("&c+10% (Divine) Enlighted Chance")

    ), true),
    HALLOWEEN_BACKPACK(CC.GoldB + "Cursed Wings", ItemSkinType.BACKPACK, new CursedWingsSkin(), Arrays.asList(CC.translate("&c-75% Silence Duration"), CC.translate("&cBlocks all enemy Rocket Escape"), CC.translate("&c-3% Incoming Damage")), true),
    VALENTINES_2022_MUTATED_BACKPACK(CC.translate("&2&l&nMutated&r &d&lBank Roll Backpack"), ItemSkinType.BACKPACK, new MutatedValentinesBackpack(), Arrays.asList(
            CC.translate("&c+50% Opening Combo Damage"),
            CC.translate("&c-25% Incoming Damage from Rogue"),
            CC.translate("&c+10% (Divine) Enlighted Chance"),
            CC.translate("&2Immune to Divine Ticks"),
            CC.translate("&2Immune to Lifesteal")


    ), true),
    SPRING_BREAK_2022_BACKPACK(CC.translate("&a&lSpray and Pray Backpack"), ItemSkinType.BACKPACK, new SprayPrayBackpack(), Arrays.asList(
            CC.translate("&c2% chance when dealing damage to inflict the"),
            CC.translate("&cfollowing Spray and Pray Ability for 10s:"),
            CC.translate(" &7* -6% Outgoing Damage"),
            CC.translate(" &7* +6% Incoming Damage"),
            CC.translate(" &7* -2HP tick damage per second"),
            CC.translate(" &7* When receiving this tick damage, your enemy"),
            CC.translate(" &7 will infect a nearby enemy, within 7 blocks, with"),
            CC.translate(" &7* the same effects and remaining time.")
    ), true),
    SPRING_BREAK_2022_MUTATED_BACKPACK(CC.translate("&2&l&nMutated&r &a&lSpray and Pray Backpack"), ItemSkinType.BACKPACK, new MutatedSprayPrayBackpack(), Arrays.asList(
            CC.translate("&c2% chance when dealing damage to inflict the"),
            CC.translate("&cfollowing Spray and Pray Ability for 10s:"),
            CC.translate(" &7* -6% Outgoing Damage"),
            CC.translate(" &7* +6% Incoming Damage"),
            CC.translate(" &7* -2HP tick damage per second"),
            CC.translate(" &7 When receiving this tick damage, your enemy"),
            CC.translate(" &7 will infect a nearby enemy, within 7 blocks, with"),
            CC.translate(" &7 the same effects and remaining time."),
            CC.translate(" &2* A nearby ally, within 7 blocks, will also"),
            CC.translate(" &2receive Regeneration I for the remaining time.")
    ), true),
    THANKSGIVING_2021_BACKPACK(CC.translate("&6&lTommy Tail"), ItemSkinType.BACKPACK, new TommyTail(), Arrays.asList(CC.Red + "+5% Outgoing PvP Damage when using a sword", CC.Red + "Immune to Hero Killer", CC.Red + "7.5% chance to block Soul Enchant Damage", CC.Red + "2% chance to activate a LVL 10", CC.Red + "Banshee Pet effect when damaged", CC.Red + "2x Outgoing Damage on any hit that makes", CC.Red + "your enemy's (Guided) Rocket Escape", CC.Red + "Immune to (Lethal) Sniper"), true),
    THANKSGIVING_2021_MUTATED_BACKPACK(CC.translate("&2&l&nMutated&r &6&lTommy Tail"), ItemSkinType.BACKPACK, new MutatedTommyTail(), Arrays.asList(CC.Red + "+5% Outgoing PvP Damage when using a sword", CC.Red + "Immune to Hero Killer", CC.Red + "7.5% chance to block Soul Enchant Damage", CC.Red + "2% chance to activate a LVL 10", CC.Red + "Banshee Pet effect when damaged", CC.Red + "2x Outgoing Damage on any hit that makes", CC.Red + "your enemy's (Guided) Rocket Escape", CC.Red + "Immune to (Lethal) Sniper", CC.DarkGreen + "Increases outgoing damage by up to 10%", CC.DarkGreen + "when under 60% HP"), true),
    BACK_TO_SCHOOL_2022_BACKPACK(CC.translate("&8&lKeg"), ItemSkinType.BACKPACK, new KegSkin(), Arrays.asList(
            CC.translate("&c-75% Silence Duration"),
            CC.translate("&cBlocks all enemy Ragdoll and Rocket Escape"),
            CC.translate("&c-15% Incoming Soul Enchant Damage")
    ), true),
    HALLOWEEN_2022_BACKPACK(CC.translate("&4&lGrim Cloak"), ItemSkinType.BACKPACK, new GrimCloakSkin(), Arrays.asList(
            CC.translate("&c-50% Incoming Rogue Damage"),
            CC.translate("&c1.25x Rage Damage Multiplier"),
            CC.translate("&c+25% Extra Damage from behind")
    ), true, "-50405"),

    VALENTINES_2023_BACKPACK(CC.translate("&5&lLucifer Wings"), ItemSkinType.BACKPACK, new LuciferBackpack(), Arrays.asList(
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c")), true),

    MMM_VALENTINES_2023_BACKPACK(CC.translate("&d&l&nMagical&r &5&lLucifer Wings"), ItemSkinType.BACKPACK, new LuciferBackpack(), Arrays.asList(
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c")), true, "-295489"),







    EASTER_BELT(CC.RedB + "Rainbow Belt", ItemSkinType.BELT, new EasterBeltSkin(), Collections.singletonList(CC.Red + "-7.5% Incoming DMG"), true),
    VIRUS_BELT(CC.DarkGreenB + "First Aid Fannypack", ItemSkinType.BELT, new VirusBeltSkin(), Collections.singletonList(CC.Red + "50% chance to negate Blackout proc"), true),
    CORRUPT_BELT_POWER(CC.DarkPurpleB + "Corrupt Belt", ItemSkinType.BELT, new CorruptPowerBeltSkin(), Collections.singletonList(CC.Red + "Godly Overload V"), true),
    CORRUPT_BELT_UTILITY(CC.DarkPurpleB + "Utility Belt", ItemSkinType.BELT, new CorruptUtilityBeltSkin(), Arrays.asList(CC.Red + "Phoenix cannot be blocked", CC.Red + "Auto Feign Death (with Phoenix)"), true),
    CUPID_BELT(CC.RedB + "Divine Sash", ItemSkinType.BELT, new CupidBeltSkin(), Collections.singletonList(CC.Red + "Auto Bless"), true),
    BACK_TO_SCHOOL_2021_BELT(CC.translate("&8&lBandolier Belt"), ItemSkinType.BELT, new BandolierBeltSkin(), Arrays.asList(CC.Red + "Bypass enemy Witch Hat", CC.Red + "Immune to Deep Bleed Slowness", CC.Red + "Limits an enemy stackable enchant level by 50%"), true),
    SPRING_BREAK_2022_BELT(CC.translate("&e&lSplash Zone Belt"), ItemSkinType.BELT, new SplashZoneBelt(), Arrays.asList(
            CC.translate("&cAuto Bless"),
            CC.translate("&c-20% Silence duration"),
            CC.translate("&c+10% Outgoing Damage to Silenced enemies")
    ), true),
    SPRING_BREAK_2022_MUTATED_BELT(CC.translate("&2&l&nMutated&r &e&lSplash Zone Belt"), ItemSkinType.BELT, new MutatedSplashZoneBelt(), Arrays.asList(
            CC.translate("&cAuto Bless"),
            CC.translate("&c-20% Silence duration"),
            CC.translate("&c+10% Outgoing Damage to Silenced enemies"),
            CC.translate("&2Immune to Lifesteal"),
            CC.translate("&2Immune to Opening Combo")

    ), true),
    VALENTINES_2022_BELT(CC.translate("&c&lBling Belt"), ItemSkinType.BELT, new ValentinesBelt(), Arrays.asList(CC.Red + "+8 % Outgoing Damage", CC.Red + "+15% Pet Leash Negation", CC.Red + "10% Chance to block Rogue"), true),
    VALENTINES_2022_MUTATED_BELT(CC.translate("&2&l&nMutated&r &c&lBling Belt"), ItemSkinType.BELT, new MutatedValentinesBelt(), Arrays.asList(CC.Red + "+8 % Outgoing Damage", CC.Red + "+15% Pet Leash Negation", CC.Red + "10% Chance to block Rogue", CC.DarkGreen + "Soul Master VI"), true),
    JULY4TH_2022_BELT(CC.translate("&6&lCowboy Chaps"), ItemSkinType.BELT, new CowboyChaps(), Arrays.asList(
            CC.translate("&cImmune to Stitches Pet Ability"),
            CC.translate("&c+6% Outgoing Damage"),
            CC.translate("&cGodly Overload IV"),
            CC.translate("&cAuto Bless")
    ), true),
    THANKSGIVING_2022_BELT(CC.translate("&e&lMatrix Master Belt"), ItemSkinType.BELT, new MatrixMasterSkin(), Arrays.asList(
            CC.translate("&cLimits enemy stackable enchant level by 50%"),
            CC.translate("&cImmune to World Destroyer Pet"),
            CC.translate("&c10% chance to block Soul Enchant Damage Multipliers"),
            CC.translate("&c25% chance to block Ultra Rage")), true),









    JULY_2021_BOOTS(CC.translate("&c&lBarry's Bolts"), ItemSkinType.BOOTS, new BarrysBoltsSkin(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Immune to Slowness Potion Effect", CC.Red + "-15% damage from enemy (Bloody) Deep Wounds", CC.Red + "Flash Ability", CC.translate("&7 (10% chance when damaged to activate. When"), CC.translate("&7 active, all enemies you hit will have their"), CC.translate("&7 pets and Phoenix enchantment silenced, your"), CC.translate("&7 Soul Tether will do +20% damage and incoming"), CC.translate("&7 soul damage to your allies within a 4x4"), CC.translate("&7radius is reduced by -5%")), true),
    CHRISTMAS_BOOTS_SLIPPERS(CC.translate("&c&lSnowflake Slippers"), ItemSkinType.BOOTS, new SnowflakeSlippersSkin(), Arrays.asList(CC.translate("&cGears IV"), CC.translate("&cImmune to Teleblock"), CC.translate("&cImmune to Slowness Potion Effect"), CC.translate("&cSnowflake Storm Ability"), CC.translate("&7(10% chance to spawn 4x4 snow floor for 10s."), CC.translate("&7While stood on the snow floor:"), CC.translate("&7* You will receive -20% Incoming Damage"), CC.translate("&7* Your allies will receive -5% Incoming Damage"), CC.translate("&7* Your enemies will receive Slowness II)")), true),
    COSMIC_CITIZEN_BOOTS(CC.AquaB + "Moon Boots", ItemSkinType.BOOTS, new MoonBootsSkin(), Collections.singletonList(CC.Red + "Increases armor set bonus/crystal damage by 1.2x"), true),
    HALLOWEEN_2021_BOOTS(CC.translate("&8&lBloodstained Galoshes"), ItemSkinType.BOOTS, new BloodstainedGaloshesSkin(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Immune to World Destroyer Cage", CC.Red + "25% chance to reflect cage back at the user", CC.Red + "Immune to Slownesses/Freezes", CC.Red + "Increases Execute HP threshold to proc by 1.3x", CC.translate("&cPsychotic Pursuit Ability"), CC.translate("&7 (+5% chance to proc Dodge +7 Outgoing"), CC.translate("&7 Damage on successful (Ethereal) Dodge procs"), CC.translate("&7 for a short duration")), true),
    SUMMER_2021_BOOTS(CC.translate("&6&lRoller Skates"), ItemSkinType.BOOTS, new RollerSkates(), Arrays.asList(
            CC.translate("&cGears IV"),
            CC.translate("&cAuto Bless"),
            CC.translate("&cImmune to Skrrt Pet"),
            CC.translate("&c+10% Monopoly Chance")
    ), true),
    VALENTINES_2022_BOOTS(CC.translate("&d&lGolden Boots"), ItemSkinType.BOOTS, new ValentinesBoots(), Arrays.asList(
            CC.Red + "Gears IV",
            CC.Red + "1.1x Critical Damage",
            CC.Red + "10% Chance to negate Incoming Knockback",
            CC.Red + "Immune to Witcher Pet"
    ), true),
    CHRISTMAS_2021_BOOTS(CC.translate("&2&lSleigh Riders"), ItemSkinType.BOOTS, new SleighRiders(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Limit enemy Amulet of Corruption to 1 armor set type", CC.Red + "When affected by any Slowness, you will receive -1%", CC.Red + "Incoming Damage (up to a max of 8%) and +1% Outgoing", CC.translate("&cDamage (up to a max of 15%) per 5% HP lost"), CC.translate("&c3% chance to clear your enemy's rage stacks and"), CC.translate("&csilence their ability to gain more for 7s"), CC.translate("&c7% chance to negate enemy Rogue for 8s when their"), CC.translate("&chealth is below 20 HP")), true),
    SPRING_BREAK_2022_BOOTS(CC.translate("&a&lSink or Swim Flippers"), ItemSkinType.BOOTS, new SinkOrSwipFlippers(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Inverts enemy Ragdoll", CC.Red + "Immune to Freezes", CC.Red + "When wielding a sword:", CC.Gray + " * Deal up to +25% damage if hitting your", CC.Gray + " enemy from behind", CC.Gray + " * 6% chance to silence enemy (Extreme)", CC.Gray + " Insanity and (Vampiric) Devour for 4s.", CC.Red + "Sink Effect", CC.Gray + " * Gain a stack when you negate a freeze.", CC.Gray + " * At 7 stacks, of the next 5 hits affecting", CC.Gray + " your enemy, they will receive Sink Effect", CC.Gray + " tick damage for 15s."), true),
    SPRING_BREAK_2022_MUTATED_BOOTS(CC.translate("&2&l&nMutated&r &a&lSink or Swim Flippers"), ItemSkinType.BOOTS, new MutatedSinkOrSwipFlippers(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Inverts enemy Ragdoll", CC.Red + "Immune to Freezes", CC.Red + "When wielding a sword:", CC.Gray + " * Deal up to +25% damage if hitting your", CC.Gray + " enemy from behind", CC.Gray + " * 6% chance to silence enemy (Extreme)", CC.Gray + " Insanity and (Vampiric) Devour for 4s.", CC.Red + "Sink Effect", CC.Gray + " * Gain a stack when you negate a freeze.", CC.Gray + " * At 7 stacks, of the next 5 hits affecting", CC.Gray + " your enemy, they will receive Sink Effect", CC.Gray + " tick damage for 15s.", CC.DarkGreen + "Swim Effect", CC.Gray + " * Receive the following effects when hitting", CC.Gray + " enemies with your Sing Effect tick damage:", CC.Gray + "  * +12.5% Outgoing Damage", CC.Gray + "  * Absolute Bypass of enemy Holy White", CC.Gray + "  Scrolls (for 1 piece only)"), true),
    VALENTINES_2022_MUTATED_BOOTS(CC.translate("&2&l&nMutated&r &d&lGolden Boots"), ItemSkinType.BOOTS, new MutatedValentinesBoots(), Arrays.asList(
            CC.Red + "Gears IV",
            CC.Red + "1.1x Critical Damage",
            CC.Red + "10% Chance to negate Incoming Knockback",
            CC.Red + "Immune to Witcher Pet",
            CC.DarkGreen + "1.25x World Destroyer Duration"
    ), true),
    EASTER_2022_BOOTS(CC.translate("&d&lMary Janes"), ItemSkinType.BOOTS, new MaryJanes(), Arrays.asList(
            CC.translate("&cGears IV"),
            CC.translate("&cImmune to Amulet of Corruption"),
            CC.translate("&c1.25x Critical Damage"),
            CC.translate("&cImmune to slowness and freezes")
    ), true),
    BACK_TO_SCHOOL_2021_BOOTS(CC.translate("&8&lBig Timbs"), ItemSkinType.BOOTS, new BigTimbsSkin(), Arrays.asList(CC.Red + "35% chance on incoming damage to", CC.Red + "negate enemy Rage stack multipliers", CC.Red + "Immune to Hero Killer", CC.Red + "Immune to enemy outgoing Heroic Pet abilities", CC.translate("&cGears IV")), true),
    EASTER_2022_MUTATED_BOOTS(CC.translate("&2&l&nMutated&r &d&lMary Janes"), ItemSkinType.BOOTS, new MutatedMaryJanes(), Arrays.asList(
            CC.translate("&cGears IV"),
            CC.translate("&cImmune to Amulet of Corruption"),
            CC.translate("&c1.25x Critical Damage"),
            CC.translate("&cImmune to slowness and freezes"),
            CC.translate("&2-33% Rogue Enchant damage")
    ), true),









    PEPE_WEAPON(CC.BlueB + "Pepe Clapper", ItemSkinType.SWORD, new PepeWeaponSkin(), Collections.singletonList(CC.Red + "+15% Outgoing damage to enemies"), true),
    DOOMSDAY_SWORD(CC.DarkGreenB + "Doomsday Machete", ItemSkinType.SWORD, new DoomsdayMacheteSkin(), Collections.singletonList(CC.Red + "Increases outgoing damage from soul enchantments by 1.25x"), true),
    SUMMER_2021_SWORD(CC.translate("&c&lMaui's Hook"), ItemSkinType.SWORD, new MauisHookSkin(), Arrays.asList(CC.translate("&c+15% Outgoing Soul Enchant Damage"), CC.translate("&cBlackout V"), CC.translate("&cNeutralize VI"), CC.translate("&cTranscendence ability"), CC.translate("&7(For each soul enchant that is"), CC.translate("&7blocked (not including immunity)"), CC.translate("&7gain +1 stack. At 10 stacks crouch"), CC.translate("&7to activate Transcendence massively"), CC.translate("&7damaging and silencing nearby"), CC.translate("&7players and pets.)")), true),
    CHRISTMAS_SWORD_ORNAMENTAL(CC.translate("&a&lOrnamental Carnage"), ItemSkinType.SWORD, new OrnamentalCarnageSkin(), Arrays.asList(CC.translate("&c-5% Incoming Damage"), CC.translate("&c-15% Incoming Damage from enemies with Soul Mode active"), CC.translate("&cTitan Trap V"), CC.translate("&c+10% Outgoing Damage to trapped players"), CC.translate("&c+2 blocks Divine Immolation radius"), CC.translate("&c2% chance for Dimensional Shift Ability")), true),
    VALENTINES_2021_SWORD(CC.translate("&c&lHeartbreaker"), ItemSkinType.SWORD, new HeartBreakerSkin(), Arrays.asList(CC.translate("&cBypass enemy (Vengeful) Diminish"), CC.translate("&c+1% Damage for each enemy level of (Planetary) Deathbringer"), CC.translate("&cMartyr Valor V"), CC.translate("&cReality Inversion V")), true),
    CHRISTMAS_2021_SWORD(CC.translate("&4&lSeason's Beatings"), ItemSkinType.SWORD, new SeasonsBeatings(), Arrays.asList(CC.Red + "+7% Outgoing Damage to players wielding weapons with Hero Killer", CC.Red + "+10% Outgoing Damage to enemies with Soul Mode Activated", CC.Red + "3.5% chance to silence enemy (Reflective) Block for 8s", CC.Red + "Outgoing Damage while Frozen or Slowed has a 12.5% chance", CC.Red + "to remove the Freeze or Slow effect", CC.Red + "+10% Rage Damage"), true),
    THANKSGIVING_2021_SWORD(CC.translate("&e&lCarver"), ItemSkinType.SWORD, new Carver(), Arrays.asList(CC.Red + "15% chance to deal 2x Divine Immolation tick damage", CC.Gray + "  (costs 2x extra souls on proc)", CC.Red + "33% chance to bypass Heroic Armor", CC.Red + "Damage you deal has a 3% chance to deal", CC.Red + "+50% Outgoing Soul Enchant Damage for 5s", CC.Red + "4% chance to silence your enemy's Rogue for 8s"), true),
    THANKSGIVING_SWORD_HARVESTER(CC.translate("&e&lSoul Harvester"), ItemSkinType.SWORD, new SoulHarvesterSkin(), Arrays.asList(CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.3x"), CC.translate("&c+10% Outgoing Damage to enemies with Soul Mode active"), CC.translate("&c50% chance to block Divine Immolation"), CC.translate("&c-25% Soul Enchant Costs")), true),
    EASTER_2022_SWORD(CC.translate("&e&lQueen's Verdict"), ItemSkinType.SWORD, new QueensVerdict(), Arrays.asList(
            CC.translate("&cBypass enemy (Paladin) Armored"),
            CC.translate("&cIncrease outgoing soul enchant damage by 1.3x"),
            CC.translate("&c15% chance to bypass enemy Ancient Crystals"),
            CC.translate("&cDevour VI"),
            CC.translate("&c20% chance to block enemy Divine Immolation")
    ), true),
    CHRISTMAS_2021_MUTATED_SWORD(CC.translate("&2&l&nMutated&r &4&lSeason's Beatings"), ItemSkinType.SWORD, new MutatedSeasonsBeatings(), Arrays.asList(CC.Red + "+7% Outgoing Damage to players wielding weapons with Hero Killer", CC.Red + "+10% Outgoing Damage to enemies with Soul Mode Activated", CC.Red + "3.5% chance to silence enemy (Reflective) Block for 8s", CC.Red + "Outgoing Damage while Frozen or Slowed has a 12.5% chance", CC.Red + "to remove the Freeze or Slow effect", CC.Red + "+10% Rage Damage", CC.DarkGreen + "Soul Master VI"), true),
    JULY4TH_2022_SWORD(CC.translate("&a&lPrickle Pear"), ItemSkinType.SWORD, new PricklePear(), Arrays.asList(
            CC.translate("&c15% Chance to break enemy Rage Stacks"),
            CC.translate("&c+4% Outgoing Damage"),
            CC.translate("&cSilence V"),
            CC.translate("&cPrickle Chaos"),
            CC.translate("&7 Chance to struck enemy with a"),
            CC.translate("&7 prickle and make them bleed for 5s.")
    ), true),

    EASTER_2022_MUTATED_SWORD(CC.translate("&2&l&nMutated&r &e&lQueen's Verdict"), ItemSkinType.SWORD, new MutatedQueensVerdict(), Arrays.asList(
            CC.translate("&cBypass enemy (Paladin) Armored"),
            CC.translate("&cIncrease outgoing soul enchant damage by 1.3x"),
            CC.translate("&c15% chance to bypass enemy Ancient Crystals"),
            CC.translate("&cDevour VI"),
            CC.translate("&c20% chance to block enemy Divine Immolation"),
            CC.translate("&2Double Strike IV")
    ), true),
    CHRISTMAS_2022_SWORD_ACTIVE(CC.translate("&b&lFrostbound Sword"), ItemSkinType.SWORD, new FrostBoundSkin(), Arrays.asList(
            CC.translate("&cOpening Combo VI"),
            CC.translate("&c2.5% chance to silence enemy (Demonic) Lifesteal for 3s"),
            CC.translate("&c1% chance to silence enemy Titan Attributes for 3s"),
            CC.translate("&c+5% Outgoing Damage"),
            CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.25x")), true),

    /*
    THANKSGIVING_2022_SWORD_GREEN(CC.translate("&e&lRay Sabre"), ItemSkinType.SWORD, new RaySabreSkin(), Arrays.asList(
            CC.translate("&cSoul Master VI"),
            CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.20x"),
            CC.translate("&c33% chance to block Divine Immolation"),
            CC.translate("&c3% chance to block enemy Defensive Crystals for 7s")), true),

     */

    MMM_THANKSGIVING_2022_SWORD_GREEN(CC.translate("&a&lYoda's Sabre"), ItemSkinType.SWORD, new YodasSabre(), Arrays.asList(
            CC.translate("&cDouble Strike IV"),
            CC.translate("&c+7.5% Rage Damage"),
            CC.translate("&cChance to add +1 to your rage stack"),
            CC.translate("&c15% Chance to break enemy rage stacks"),
            CC.translate("&c+5% Outgoing damage to enemies")), true),

    MMM_THANKSGIVING_2022_SWORD_RED(CC.translate("&c&lMaul's Sabre"), ItemSkinType.SWORD, new MaulsSabre(), Arrays.asList(
            CC.translate("&cSoul Master VI"),
            CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.25x"),
            CC.translate("&c37% chance to block Divine Immolation"),
            CC.translate("&c4% chance to block enemy Defensive Crystals for 7s")), true, "-1237440"),

    MMM_THANKSGIVING_2022_SWORD_BLUE(CC.translate("&b&lJedi's Sabre"), ItemSkinType.SWORD, new JedisSabre(), Arrays.asList(
            CC.translate("&cSoul Master VI"),
            CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.30x"),
            CC.translate("&c41% chance to block Divine Immolation"),
            CC.translate("&c5% chance to block enemy Defensive Crystals for 7s")), true, "-16345678"),






    CHRISTMAS_AXE_SNOWFLAKE(CC.translate("&a&lSnowflake"), ItemSkinType.AXE, new Snowflake(), Arrays.asList(
            CC.translate("&cSnowflake Snowball Ability"),
            CC.translate("&7 (Right-Click&7 to launch snowflake snowball."),
            CC.translate("&7 Any enemy players hit by the snowflake"),
            CC.translate("&7 snowball will receive Slowness II for 2s"),
            CC.translate("&7 and will take +8% Incoming Damage for 3s,"),
            CC.translate("&7 8s cooldown)"),
            CC.translate("&cIcy Wrath Ability"),
            CC.translate("&7 (4% chance to \"Deep Freeze\" targets,"),
            CC.translate("&7 slowing them for 2s and increasing their"),
            CC.translate("&7 incoming damage by +5%"),
            CC.translate("&7 33% chance to AoE Silence enemy enchants"),
            CC.translate("&7 including active soul enchants for 2s?"),
            CC.translate("&7 Pulls nearby enemies towards you."),
            CC.translate("&7 Reduces outgoing enemy damage by 1%"),
            CC.translate("&7 each hit for 10s up to max of 10%)"),
            CC.translate("&c Icy Blood Ability"),
            CC.translate("&7 (Reduces enemy health regeneration by"),
            CC.translate("&7 1% for 6s for each bleed stack"),
            CC.translate("&7 they have up to a max of 20%)")
    ), true),

    SPRING_2020_PICKAXE(CC.GreenB + "Rubber " + CC.YellowB + "Chicken", ItemSkinType.PICKAXE, new RubberChickenSkin(), Collections.emptyList(), true),


    ;


    private final String display;

    private final ItemSkinType itemSkinType;

    private final List<String> lore;

    private final ItemSkin skin;

    private final String colour;


    @Getter
    private final boolean enabled;



    public List<String> getLore() {
        return lore;
    }

    incaseneedrevert(String display, ItemSkinType itemSkinType, ItemSkin skin, List<String> extra, boolean enabled) {
        this.lore = new ArrayList<>();
        this.display = display;
        this.itemSkinType = itemSkinType;
        this.skin = skin;
        this.lore.addAll(extra);
        this.enabled = enabled;
        this.colour = "null";
    }

    incaseneedrevert(String display, ItemSkinType itemSkinType, ItemSkin skin, List<String> extra, boolean enabled, String colour) {
        this.lore = new ArrayList<>();
        this.display = display;
        this.itemSkinType = itemSkinType;
        this.skin = skin;
        this.lore.addAll(extra);
        this.enabled = enabled;
        this.colour = colour;
    }


    public String getDisplay() {
        return this.display;
    }

    public String getDisplayName() {
        return CC.WhiteB + "Item Skin (" + this.display + CC.WhiteB + ")";
    }

    public String getName() {
        return this.display;
    }

    public String getAppliedLore() {
        return CC.WhiteB + "ITEM SKIN: " + this.display;
    }

    public Material getMaterial() {
        return Material.EYE_OF_ENDER;
    }

    public ItemSkinType getItemSkinType() {
        return this.itemSkinType;
    }

    public ItemSkin getItemSkin() {
        return this.skin;
    }

    public ItemStack getItem() {

        String name = getCustomName();
        ItemStack item = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.WhiteB + "Item Skin (" + getName() + CC.WhiteB + ")");
        List<String> lore = new ArrayList<>();
        for (String line : getLore()) {
            lore.add(line);
        }
        lore.add("");
        lore.add(CC.GrayI + "Attach this skin to any " + CC.WhiteU + getItemSkinType().getModifiedName());
        lore.add(CC.Gray + "to override its visual appearance.");
        lore.add("");
        lore.add(CC.RedB + "NOTE: " + CC.Red + "Requires the Cosmic Client");
        lore.add(CC.Red + "to render custom item skins.");
        lore.add(CC.Gray + "* Download at " + CC.GrayU + "discord.astropvp.net");
        lore.add("");
        lore.add(CC.Gray + "Drag n' Drop onto item to attach.");
        lore.add(CC.Gray + "Right-Click item to detach skin.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        cosmicData.setString("itemSkinType", name);
        if (shouldBeMutated()) {
            cosmicData.setString("tintColor", "-10237440");
        }
        if(!Objects.equals(this.colour, "null")) {
            cosmicData.setString("tintColor", colour);
        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        wrapper.setString("random", UUID.randomUUID().toString());
        wrapper.setString("cosmicType", "item_skin");

        return wrapper.build();
    }

    public ItemStack applyBackpack(ItemStack item) {
        String name = getCustomName();
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData.hasKey("itemSkinType")) {
            cosmicData.setString("itemSkinType", name);
        } else {
            String[] current = cosmicData.getString("itemSkinType").split(",");
            List<String> strings = new ArrayList<>(Arrays.asList(current));
            strings.add(name);
            String s = Arrays.toString(strings.toArray());
            s = s.replace("[", "");
            s = s.replace("]", "");
            s = s.replace(" ", "");

            cosmicData.setString("itemSkinType", s);
        }
        if (shouldBeMutated()) {
            cosmicData.setString("tintColor", "-10237440");
        }
        if(!Objects.equals(this.colour, "null")) {
            cosmicData.setString("tintColor", colour);
        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        return wrapper.build();
    }

    public ItemStack apply(ItemStack item) {
        String name = getCustomName();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = (meta.getLore() == null) ? new ArrayList<>() : meta.getLore();
        lore.add(CC.WhiteB + "ITEM SKIN: " + getName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData.hasKey("itemSkinType")) {
            cosmicData.setString("itemSkinType", name);
        } else {
            String[] current = cosmicData.getString("itemSkinType").split(",");
            List<String> strings = new ArrayList<>(Arrays.asList(current));
            strings.add(name);
            String s = Arrays.toString(strings.toArray());
            s = s.replace("[", "");
            s = s.replace("]", "");
            s = s.replace(" ", "");
            cosmicData.setString("itemSkinType", s);
        }
        if (shouldBeMutated()) {
            cosmicData.setString("tintColor", "-10237440");
        }
        if(!Objects.equals(this.colour, "null")) {
            cosmicData.setString("tintColor", colour);
        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        return wrapper.build();
    }

    private boolean shouldBeMutated() {
        return name().startsWith("MMM_") || name().contains("MUTATED");
    }

    private String getCustomName() {
        if(name().startsWith("MMM_")) {
            return name().replace("MMM_", "NEXT_MUTATED,");
        }
        return name();
    }

    public ItemStack getMultiSkin(SkinType type) {
        ItemStack item = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.WhiteB + "Multi Item Skin (" + getName() + ", " + type.getName() + CC.WhiteB + ")");
        List<String> lore = new ArrayList<>();
        lore.add(type.getDisplay());
        lore.addAll(type.getLore());
        lore.add("");
        lore.add(display);
        lore.addAll(getLore());
        lore.add("");
        lore.add(CC.GrayI + "Attach this skin to any " + CC.WhiteU + getItemSkinType().getModifiedName());
        lore.add(CC.Gray + "to override its visual appearance.");
        lore.add("");
        lore.add(CC.RedB + "NOTE: " + CC.Red + "Requires the Cosmic Client");
        lore.add(CC.Red + "to render custom item skins.");
        lore.add(CC.Gray + "* Download at " + CC.GrayU + "cosmicclient.com");
        lore.add("");
        lore.add(CC.Gray + "Drag n' Drop onto item to attach.");
        lore.add(CC.Gray + "Right-Click item to detach skin.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        cosmicData.setString("itemSkinType", name() + "," + type.name());
        if (shouldBeMutated()) {
            cosmicData.setString("tintColor", "-10237440");
        }
        if(!Objects.equals(this.colour, "null")) {
            cosmicData.setString("tintColor", colour);
        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        wrapper.setString("random", UUID.randomUUID().toString());
        wrapper.setString("cosmicType", "item_skin");

        return wrapper.build();
    }

    public ItemStack apply(ItemStack item, SkinType type) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = (meta.getLore() == null) ? new ArrayList<>() : meta.getLore();
        lore.add(CC.WhiteB + "ITEM SKIN: " + getName());
        lore.add(CC.WhiteB + "ITEM SKIN: " + type.getName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        String[] current;
        if (cosmicData.hasKey("itemSkinType")) {
            current = cosmicData.getString("itemSkinType").split(",");
        } else {
            current = new String[0];
        }
        List<String> strings = new ArrayList<>(Arrays.asList(current));
        strings.add(name());
        strings.add(type.name());
        String s = Arrays.toString(strings.toArray());
        s = s.replace("[", "");
        s = s.replace("]", "");
        s = s.replace(" ", "");
        cosmicData.setString("itemSkinType", s);
        if (shouldBeMutated()) {
            cosmicData.setString("tintColor", "-10237440");
        }
        if(!Objects.equals(this.colour, "null")) {
            cosmicData.setString("tintColor", colour);
        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        return wrapper.build();
    }

    public ItemStack applyNoDisplay(ItemStack item) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData.hasKey("itemSkinType")) {
            cosmicData.setString("itemSkinType", name());
        } else {
            String[] current = cosmicData.getString("itemSkinType").split(",");
            List<String> strings = new ArrayList<>(Arrays.asList(current));
            strings.add(name());
            String s = Arrays.toString(strings.toArray());
            s = s.replace("[", "");
            s = s.replace("]", "");
            s = s.replace(" ", "");

            cosmicData.setString("itemSkinType", s);
        }
        if (shouldBeMutated()) {
            cosmicData.setString("tintColor", "-10237440");

        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        return wrapper.build();
    }

}