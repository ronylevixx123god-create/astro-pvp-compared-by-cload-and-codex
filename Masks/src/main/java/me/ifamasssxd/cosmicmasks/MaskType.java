/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.google.common.collect.Lists
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicmasks;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicmasks.items.MultiMaskItem;
import me.ifamasssxd.cosmicmasks.listeners.MaskListener;
import me.ifamasssxd.cosmicmasks.struct.*;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public enum MaskType {
    HEADLESS(new MaskData("456de2b44c66a6b039b1a2e46561af76e829faa27626aaf8b948a5065c81262", "eyJ0aW1lc3RhbXAiOjE1Mzk2ODUyODcwNjgsInByb2ZpbGVJZCI6IjU5ZjAzOTljZWVlMzQwOTViYzJmOTBkZTMwNGM5OGMxIiwicHJvZmlsZU5hbWUiOiJCYW5kaWl0byIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85YWJkNjA4ZTdkYWNlMmU2OTkyZTQ3ZmJiMmRjZjY2NDhkOTc1Y2FlOGQzMjYxNWM2Y2YwM2VkYzdkYjMyODY4IiwibWV0YWRhdGEiOnsibW9kZWwiOiJzbGltIn19fX0=", "HALLOWEEN", true, UUID.randomUUID(), CC.GoldB + "Headless Mask", Lists.newArrayList(CC.Gray + "A terrifying Mask from the", CC.Gray + "grave of the Headless Horseman.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    PURGE(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDAzNTU5NDg0MDksInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85ODYzMWU5ODZlMDEwNWMwNmY2ZDIxNDJlYjI3MmRmMTQ1OTM2NmI3ZGRkY2MxMTk4OTEzNDI2MzZjMjhhMTdlIn19fQ==", "MERCHANT", true, UUID.randomUUID(), CC.RedB + "Purge Mask", CC.Red + "+2.5% DMG", Lists.newArrayList(CC.Red + "+2.5% DMG", CC.Gray + "A great evil is contained within this", CC.Gray + "horrifying mask. Who knows what inner", CC.Gray + "demons it will unleash.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    PUMPKIN_MONSTER(new MaskData("", "eyJ0aW1lc3RhbXAiOjE1NDA1MTIzNjcyMDksInByb2ZpbGVJZCI6IjZjYTU0MGI0MmU4MjRlMjM4ZDc5NzkxMmQ2Y2U4NTgzIiwicHJvZmlsZU5hbWUiOiJIYWxsb3dlZW4iLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGYwZDY0NTdhMmIwMTViYTVkYWZiZTFiYWYyNmU2YWQ2YzM1MGVjMTUwODRmY2E2YTg2ZjUwYmU3NTNiNjY3NyJ9fX0=", "Halloween", false, UUID.randomUUID(), CC.GoldB + "Pumpkin Monster", Lists.newArrayList(CC.Gray + "The severed head of a fabled Halloween", CC.Gray + "nightmare known as the Pumpkin Monster", CC.GoldB + " * " + CC.Gold + "Halloween Quests"))),
    GHOST(new MaskData("", "eyJ0aW1lc3RhbXAiOjE1NDAzNTY0MjgwMjAsInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xMDVjNDg3NjllY2MzMjg4YWJkYjcyMThlYmZmMzEyZGNhMmEyOGJjZmNkMjlkMGFjMjk5YWNjMjMyM2VhNzhjIn19fQ==", "Ghost", true, UUID.randomUUID(), CC.DarkAquaB + "Ghost Mask", Lists.newArrayList(CC.Gray + "The deviant mask of the spectre,", CC.Gray + "a metaphysical monstrosity.", CC.GoldB + " * " + CC.Gold + "Ghost Mastery Kit"))),
    SCARECROW(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDA4NTQwNTU0NzAsInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mMWVlYzg3NTdiYTgyZmY5MTY3OTNmZmUzNWRlMjI1NDhjMWZhZjViYmUwNWNjMzg4YjMyYTA5NDgxNGY1OTgzIn19fQ==", "Scarecrow", true, UUID.randomUUID(), CC.YellowB + "Scarecrow Mask", CC.Red + "Infinite Food", Lists.newArrayList(CC.Red + "Infinite Food", CC.Gray + "An empty husk of a", CC.Gray + "once tortured soul left to rot.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    TURKEY(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDI3MDEwMDY0NzMsInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lN2Q5NzQ1N2Q2ODhkZWRjN2ZlZDRkNTU5OWExNGMyODBmM2RjYWIyOTM3YjJiMDcxYWZkOGZlZWFjMGViZmFmIn19fQ==", "Turkey", true, UUID.randomUUID(), CC.YellowB + "Turkey Mask", CC.Red + "+2% Dodge", Lists.newArrayList(CC.Red + "+2% Dodge", CC.Gray + "Stay nimble and fast, or the", CC.Gray + "Pilgrims will catch and cook you!", CC.GoldB + " * " + CC.Gold + "Map One"))),
    PILGRIM(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDI3MDA5NzIxMzIsInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82M2I2M2FjYTI1NWU1YWExYTU5MWI3Yjc1YjdiNWU0NzQzZDQxOTRlZmY1MmFmZTEzOTA3YWU5M2Y3OTMwYmJhIn19fQ==", "PILGRIM", true, UUID.randomUUID(), CC.YellowB + "Pilgrim Mask", CC.Red + "+25% XP/Drops", Lists.newArrayList(CC.Red + "+25% XP/Drops", CC.Gray + "tHiS iS oUr lAnD nOw!", CC.GoldB + " * " + CC.Gold + "Thanksgiving 2018"))),
    DRAGON_MASK(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDQ3MjUxMDk0ODUsInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80ZGYxOGZhOWRmZWE1NjNiYWM3NjVhMTdlNjU0Nzc3MjhkNTM2ZjhlOWVlODZlMTU5YzNjNjM4NTI2YTMyNDFjIn19fQ==", "Dragon", true, UUID.randomUUID(), CC.DarkRedB + "Dragon Mask", CC.Red + "+5% DMG", Lists.newArrayList(CC.Red + "+5% DMG", CC.Red + "Immune to Fire and Lava damage", CC.Gray + "The decapitated skull of a slain", CC.Gray + "Timeless Dragon from the Ender Dimension.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    SANTA(new MaskData(new SantaMask(), "", "eyJ0aW1lc3RhbXAiOjE1Mzk2ODUyNDc4MjgsInByb2ZpbGVJZCI6ImM3ZmRkYjlhYWRmZDRiZTNiMzI2ZDcwMGRmMjUyZTg4IiwicHJvZmlsZU5hbWUiOiJTYW50YSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zZjg2NjgyMWJmYmY1MWMwNWZmNzk4YWYxMDk1NzVhZmZhMThjNzc5MTUxODEwYjUyZTk5YzU0N2MzMzc5ZWI3In19fQ==", "SANTA", true, UUID.randomUUID(), CC.AquaB + "Santa", CC.Red + "+2 Max Hearts", Lists.newArrayList(CC.Red + "+2 Max Hearts", CC.Gray + "An eerie mask imbued with", CC.Gray + "Christmas Joy that knows", CC.Gray + "who is naughty or nice.", CC.GoldB + "* " + CC.Gold + "Christmas 2018"))),
    REINDEER(new MaskData((CosmicMask)new ReindeerMask(), "", "eyJ0aW1lc3RhbXAiOjE1NDUxOTQ2NjQ4ODMsInByb2ZpbGVJZCI6ImQ4ZmQzYjUxYzZiNzRlNmY4NmExY2FlM2ZiNjI3NzYwIiwicHJvZmlsZU5hbWUiOiJNeXN0ZXJ5TWVyY2hhbnQiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjY4MjQ3YTVmYjI5M2Q3ODU1MjAwOTc4NTRjZmFhNWQxNWY1ZjcxZDg0YTZlNjNhOTZiNjI2OGU3ZWQxM2UyNiJ9fX0=", "REINDEER", true, UUID.randomUUID(), CC.GreenB + "Reindeer", CC.Red + "SPEED IV", Lists.newArrayList(CC.Red + "SPEED IV", CC.Red + "Flight regardless of rank", CC.Gray + "The decapitated and stuffed", CC.Gray + "head of one of Santa's", CC.Gray + "magical reindeer... yikes!", CC.GoldB + "* " + CC.Gold + "Map One"))),
    PARTY_HAT(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDUxOTQ1OTkyNzksInByb2ZpbGVJZCI6ImQ4ZmQzYjUxYzZiNzRlNmY4NmExY2FlM2ZiNjI3NzYwIiwicHJvZmlsZU5hbWUiOiJNeXN0ZXJ5TWVyY2hhbnQiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmM0NThjZjBlZDkyNGNhM2VhMzI4ZDg0MTMyODVhYWRlNTM0YTc0ZTQ2ZGQ1NTA0N2IxOWQxNmRlNmRiZjkyYiJ9fX0=", "PARTY_HAT", true, UUID.randomUUID(), CC.WhiteB + "Party Hat", CC.Red + "-5% ENEMY DMG", Lists.newArrayList(CC.Red + "-5% ENEMY DMG", CC.Red + "+4% DMG", CC.Gray + "Everywhere you are is a party.", CC.GoldB + "* " + CC.Gold + "New Years 2018"))),
    DEATH_KNIGHT(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDUyNjI3NjM5MzEsInByb2ZpbGVJZCI6ImQ4ZmQzYjUxYzZiNzRlNmY4NmExY2FlM2ZiNjI3NzYwIiwicHJvZmlsZU5hbWUiOiJNeXN0ZXJ5TWVyY2hhbnQiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Q2ZDkxYWU1ZDQzODlkMDE2NjIzMDQ3ZjRjMDJlZDdiM2E2ZmUyOWEyNDM1OTdjMjYzYTNiZTgxYjFmZDZiNSJ9fX0=", "DEATH_KNIGHT", true, UUID.randomUUID(), CC.BlueB + "Death Knight", CC.Red + "50% chance to negate enemy's Phoenix", Lists.newArrayList(CC.Red + "50% chance to negate enemy's Phoenix", CC.Red + "+2.5% DMG", CC.Gray + "The cursed mask of runeforged", CC.Gray + "Death Knight armor.", CC.GoldB + "* " + CC.Gold + "Map One"))),
    BEANIE(new MaskData("", "eyJ0aW1lc3RhbXAiOjE1NDc1MjgxNzA0MTMsInByb2ZpbGVJZCI6IjEwNTM1MzMwYTNlYTQ5ZjM4YWRlYTcxYWFmNTZkZTY1IiwicHJvZmlsZU5hbWUiOiJCZWFuaWVfXyIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jYjU3NTNiYjVkZDM2ODRlOTdlYTY0MzNjOTI2MGZmYjk3NWQ5ZTYzMzA5Y2JjODM1YTE0ZWQ1YWUxMmQ1OTIwIn19fQ==", "Beanie__", false, UUID.randomUUID(), CC.AquaB + "Beanie__ Mask", Lists.newArrayList(CC.Gray + "A beanie to keep your head warm.", CC.GoldB + " * " + CC.Gold + "Snow Day Lootbox Release"))),
    RIFT(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDc1Mjg5MTc3MTcsInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yMzg5NjgzYmQ4YTFhM2RlZGViMjIyNTEwMzEwMTQ5YzYxZjk0NmU1N2U4YTE5MzYwMTkxM2IwNzg2YjVjYzIifX19==", "Rift", true, UUID.randomUUID(), CC.DarkPurpleB + "Rift Mask", CC.Red + "50% Mastery Enchant Negation.", Lists.newArrayList(CC.Red + "50% Mastery Enchant Negation", CC.Gray + "A mysterious mask with a strange", CC.Gray + "aura of power found scattered among", CC.Gray + "a select few random lootboxes.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    LOVER(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NTAwNzc1MTY3MzMsInByb2ZpbGVJZCI6ImQ4ZmQzYjUxYzZiNzRlNmY4NmExY2FlM2ZiNjI3NzYwIiwicHJvZmlsZU5hbWUiOiJNeXN0ZXJ5TWVyY2hhbnQiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmFlZjY4MjJkZWVlY2ViYzg4Y2NlYTdmMTBlNGYzYWU0ZjRlNDczYzBkODA1OTdiMTQ4NDVlNWYxZTQ1ZDg4NSIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19", "Lover", true, UUID.randomUUID(), CC.RedB + "Lover Mask", CC.Red + "Immune to Mortal Coil", Lists.newArrayList(CC.Red + "Immune to Mortal Coil", CC.Gray + "Make love, not Minecraft...", CC.Gray + "or maybe its the other way around?", CC.GoldB + " * " + CC.Gold + "Map One"))),
    SPECTRAL(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NTA2OTcxNjQ1ODYsInByb2ZpbGVJZCI6ImQ4ZmQzYjUxYzZiNzRlNmY4NmExY2FlM2ZiNjI3NzYwIiwicHJvZmlsZU5hbWUiOiJNeXN0ZXJ5TWVyY2hhbnQiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2FhMWY4YjMwMmMwN2RlYTJlNTA1M2FkNTVjOGQ4MTFkNjBjNTdlZWQyNjY5MzY5MGQzNTNiMzgxMTliZDk1MSIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19", "Spectral", true, UUID.randomUUID(), CC.DarkAquaB + "Spectral Mask", CC.Red + "Zombie Auto-disguise at y>200 (in combat)", Lists.newArrayList(CC.Red + "Zombie Auto-disguise at y>200 (in combat)", CC.Gray + "As silent as the night, as mystic", CC.Gray + "as the full moon: The Spectre", CC.GoldB + " * " + CC.Gold + "Map One"))),
    GLITCH(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NTI0ODc0MDI5OTEsInByb2ZpbGVJZCI6ImQ4ZmQzYjUxYzZiNzRlNmY4NmExY2FlM2ZiNjI3NzYwIiwicHJvZmlsZU5hbWUiOiJNeXN0ZXJ5TWVyY2hhbnQiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI2NDdjYTVkYzRkNTE5Y2VhYjg3MjgyZWI5MDMxNzQ3NmRjNDU2MWZjZTVlZWM2YTk4Mzk2YTBmZGJkNWZiYyIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19", "Glitch", true, UUID.randomUUID(), CC.WhiteB + "Glitch Mask", CC.Red + "Immune to Teleblock, Bidirectional Teleport", Lists.newArrayList(CC.Red + "Immune to Teleblock, Bidirectional Teleport", CC.Gray + "The aura around this mask", CC.Gray + "is electrified and encoded.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    ZEUS(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NTQ4ODk2MzY1MzgsInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80OGNjNDE2NGM4NTNjODliYmU3NjU2YjgyOGMzOWYxMDMwNGMzMGQ5MmVmMjEzYmNjZmVmMDlhNDEzMWQ5ZTQ4In19fQ==", "Zeus", true, UUID.randomUUID(), CC.AquaB + "Zeus Mask", CC.Red + "Immune to Natures Wrath", Lists.newArrayList(CC.Red + "Immune to Natures Wrath", CC.Gray + "Channel the powers of the", CC.Gray + "King of the Greek Gods", CC.GoldB + " * " + CC.Gold + "April Showers Lootbox Release"))),
    BUNNY(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NTU2MDIyMzEwNDUsInByb2ZpbGVJZCI6ImQ4ZmQzYjUxYzZiNzRlNmY4NmExY2FlM2ZiNjI3NzYwIiwicHJvZmlsZU5hbWUiOiJNeXN0ZXJ5TWVyY2hhbnQiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2IzMDg3MGVmYWI1ZjMzNTk4YjEwNzg3MzRhNDRjMzM1NjJiZGU4M2I2YTM5ODc0NmRmZmNlMDZjN2QxOThmNiIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19", "Bunny", true, UUID.randomUUID(), CC.LightPurpleB + "Bunny Mask", CC.Red + "1.65x Mobs from Spawners in Chunk", Lists.newArrayList(CC.Red + "1.65x Mobs from Spawners in Chunk", CC.Gray + "And so the gods declared to all", CC.Gray + "the easter bunnies: be fruitful", CC.Gray + "and multiply.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    JOKER(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NTc4NDYxODU2MTMsInByb2ZpbGVJZCI6ImQ4ZmQzYjUxYzZiNzRlNmY4NmExY2FlM2ZiNjI3NzYwIiwicHJvZmlsZU5hbWUiOiJNeXN0ZXJ5TWVyY2hhbnQiLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQzZDc0MzA1ZGZlNWY2OTExZjA2MmQ4NjJiYjg5ZTVhNjFiNGNmNzQ0YzJiMDgxNmU1YzI1NGQzODJjODRhNCIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19", "Joker", true, UUID.randomUUID(), CC.DarkPurpleB + "Joker Mask", CC.Red + "Increase Combat Tag on players by 4s", Lists.newArrayList(CC.Red + "+4s Combat Tag on enemy players", CC.Red + "-3s Combat Tag on you", CC.Gray + "Everyone takes everything so", CC.Gray + "very seriously, you're just trying", CC.Gray + "to.. haHahaHahaHahaHahaHahaHa!", CC.GoldB + " * " + CC.Gold + "Map One"))),
    DUNGEON(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NTk4MzQzNDcyMDUsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83ZWRhMzY0M2Q2ODZkMDE0OGIwOWE5ZGFlMGJlZDUzODA4ZjkyNTg1ZjJhZjRkY2FhOWNiYzA5MDQ4NzEwZDFiIn19fQ==", "Dungeon Mask", true, UUID.randomUUID(), CC.GoldB + "Dungeon Mask", CC.Red + "10% chance to not use /dungeon key", Lists.newArrayList(CC.Red + "10% chance to not use /dungeon key", CC.Gray + "You take dungeon running extremely", CC.Gray + "seriously, you solo most dungeons", CC.Gray + "faster than other groups start them.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    OUTPOST(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NjIwOTcxMDIzMjksInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84ZmZmNDFlMWFmYzU5N2IxNGY3N2I4ZTQ0ZTJhMTM0ZGFiZTE2MWExNTI2YWRlODBlNjI5MGYyZGYzMzFkYzExIn19fQ==", "Outpost Mask", true, UUID.randomUUID(), CC.AquaB + "Outpost Mask", CC.Red + "Capture, destroy /outpost caps 2x faster", Lists.newArrayList(CC.Red + "Capture, destroy /outpost caps 2x faster", CC.Gray + "Your very presence me.astropvp.cratekeys.net.astropvp.armorsets.commands unquestioning,", CC.Gray + "unwaivering admiration and respect.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    STRONGHOLD(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NjI2MjcxODEwMDksInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jZjY2NTk2ZDdlOGIwMzFkN2Q2M2U3ZWQ0ZDJiMTdiNWNmMjQ4OTVhNGU4YTczN2ZkYzJiODMwZTJhMmE1OTY4In19fQ==", "Stronghold Mask", true, UUID.randomUUID(), CC.DarkAquaB + "Stronghold Mask", CC.Red + "Capture, destroy /stronghold caps 2x faster", Lists.newArrayList(CC.Red + "Capture, destroy /stronghold caps 2x faster", CC.Gray + "Your very presence terrifies all,", CC.Gray + "who dare stand before you.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    BOSS(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NjI2MjcyNjA5OTMsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kOGU2ZWMzMTY3MmZmNTljNWYzMzJiOGY3OTIxMDM0NWJhNTZiMDdkOTRkNmJjOGY1ODljYmUzYWZjM2U3OTY5In19fQ==", "Boss Mask", true, UUID.randomUUID(), CC.RedB + "Boss Mask", CC.Red + "-25% incoming Boss DMG, +10% outgoing Boss DMG", Lists.newArrayList(CC.Red + "-25% incoming DMG, +10% outgoing DMG to Bosses", CC.Gray + "A seasoned monster hunter, you pride", CC.Gray + "yourself on your boss slaying record.", CC.GoldB + " * " + CC.Gold + "Map Onee"))),
    WITCHER(new MaskData(null, "", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBlMTNkMTg0NzRmYzk0ZWQ1NWFlYjcwNjk1NjZlNDY4N2Q3NzNkYWMxNmY0YzNmODcyMmZjOTViZjlmMmRmYSJ9fX0=", "Witcher Mask", true, UUID.randomUUID(), CC.LightPurpleB + "Witcher Mask", CC.Red + "Immune to Golden Admirer Skin", Lists.newArrayList( CC.Red + "Immune to Golden Admirer Skin", CC.Gray + "Protecting you from the lordy", CC.Gray + "golden admirer itemskin"))),

    MULTI_MASK(new MaskData(new ReindeerMask(), "", "eyJ0aW1lc3RhbXAiOjE1NjIwODg5MjU2NDUsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83M2UxYWQ0ZmY0ZDhlMjM5YmJlYTUyNGIyNGM0ZDRkNWQ4NjIzZTUyZDdkOGVlNzllNmE1ZjhjN2FkZWRiOWEzIn19fQ==", "Multi-Mask", false, UUID.randomUUID(), CC.WhiteB + "Multi-Mask", "", Lists.newArrayList(""))),

    NECROMANCER(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDI4MTkzNjA5MTIsInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84ZmMzMmMwN2U0ZjJiZWFkZTM5ZWFjM2YxYWFmMzJkYjQ4ZjI0MTEwZDM2YmIzMzM0MGU1ZTUzZWQ2Y2YyN2ViIn19fQ==", "Necromancer", true, UUID.randomUUID(), CC.DarkGreenB + "Necromancer Mask", CC.Red + "Immune to Lifesteal", Lists.newArrayList(CC.Red + "Immune to Lifesteal", CC.Gray + "An enchanted skull conjured from", CC.Gray + "the depths of the underworld.", CC.GoldB + " * " + CC.Gold + "Necromancer Mastery Kit"))),
    CRANKED(new MaskData(null, "", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE4N2NkNzUyM2RjYzRjYmVhZWFlYzljOGJlYjg5MmExMDNiOTUzOTVlMDY1ZjI1YmI0ZmM3YTZiZDRiOTAyYyJ9fX0=", "CRANKED", true, UUID.randomUUID(), CC.DarkRedB + "Cranked Mask", CC.DarkRed + "50% To Block Util Belt Skin, 5% Extra Damage", Lists.newArrayList(CC.Red + "50% To Block Util Belt Skin", CC.Red + "5% Extra Damage" , CC.Gray + "A great evil is contained within this", CC.Gray + "horrifying mask. Who knows what inner", CC.Gray + "demons it will unleash.", CC.GoldB + " * " + CC.Gold + "Map Five"))),
    MONOPOLY(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDI3MDEwNDEwNjMsInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hM2M0ZDA5ZGViNzkwNDkyYTA0OTIxNTA3OTc3Y2FiOTI2MzViODgxODRkNDg5ZjgzYzM0YjYwZTdkOGE0MjViIn19fQ==", "Monopoly", true, UUID.randomUUID(), CC.AquaB + "Monopoly Mask", CC.Red + "33% Holy White Scroll negation", Lists.newArrayList(CC.Red + "33% Holy White Scroll negation", CC.Red + "-5% ENEMY DMG", CC.Gray + "The mask of a man who has it all,", CC.Gray + "a truly powerful entity to contest with.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    TERMINATOR(new MaskData((CosmicMask)new TerminatorMask(), "", "eyJ0aW1lc3RhbXAiOjE1ODU3MTk3NTAxMTEsInByb2ZpbGVJZCI6ImIzM2Q1MzEyZGU3ZDQxODA4YTIzNjZhZjllNDc3YzBjIiwicHJvZmlsZU5hbWUiOiJCdWplIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lZmU3ZmQwYzJmNjk3MmVkZTA2YmVhM2UyNjRkM2U5M2QwMzYwNGQ2ZGQwYTc0NTI3MjE1YTg0Y2ZjZmYxMjE1In19fQ==", "Terminator Mask", true, UUID.randomUUID(), CC.DarkRedB + "Terminator Mask", CC.DarkRed + "Blocks Escapes, Teleblock (6s), SPEED IV", Lists.newArrayList(CC.DarkRed + "Negate 50% Shockwave, Ragdoll, Rocket Escape", CC.DarkRed + "% chance to apply Teleblock (6s)", CC.DarkRed + "Speed IV", CC.Gray + "A T-70 hunter-killer robot engineered", CC.Gray + "to exterminate all living entities.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    THANOS(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1ODU3ODAwMDYzMjIsInByb2ZpbGVJZCI6ImIzM2Q1MzEyZGU3ZDQxODA4YTIzNjZhZjllNDc3YzBjIiwicHJvZmlsZU5hbWUiOiJCdWplIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82ZGVkMTExZjBjNDc5YTQzMzhlMGI4ZmE2NjEwN2Y2ZmI4NzhiYTI3ZDE1YTViZjZiNjI0ZDQ1YmMyZjUxOTNiIn19fQ==", "Thanos Mask", true, UUID.randomUUID(), CC.GoldB + "Thanos Mask", CC.Red + "+5% DMG, Immune to Natures Wrath", Lists.newArrayList(CC.Red + "+5% DMG", CC.Red + "Lava Strider I", CC.Red + "Web Walker I", CC.Red + "Immune to Natures Wrath", CC.Red + "100% chance to negate enemy's Phoenix", CC.Red + "15% Holy White Scroll negation", CC.Gray + "Thanos? Oh yes... *SNAP*", CC.GoldB + " * " + CC.Gold + "Map One"))),
    ANIMAL_CONTROL(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NzQxMzU1OTc0NTMsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85NTZjZWNlYTA5MzU3NDhiMTA2ZTM2ZjhlZmE2NmNjOGExN2IxODdiNjgzMzU5YWQ1MWEwNmM3MzJlYjg5YWE2In19fQ==", "Animal Control Mask", true, UUID.randomUUID(), CC.AquaB + "Animal Control Mask", CC.Red + "33% Pet Leash Negation", Lists.newArrayList(CC.Red + "33% Pet Leash Negation", CC.Gray + "Hide your cats, dogs, and pets -", CC.Gray + "animal control is coming for you!", CC.GoldB + " * " + CC.Gold + "Map One"))),
    HOLY(new MaskData(null, "", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI2Njk0NmY4NGIyNzc5NmExYzdiMWI2MDY4ZWZiNzc0ODFiOGFkOGJhNGZjMWVlZjM1Y2M5NzVlMDZhZmI0MiJ9fX0=", "Holy Mask", true, UUID.randomUUID(), CC.GoldB + "Holy Mask", CC.Red + "Immune to (Deep) Bleed slowness", Lists.newArrayList( CC.Red + "Immune to (Deep) Bleed slowness", CC.Gray + "Protecting you from the curse", CC.Gray + "of slowness, this mask has a", CC.Gray + " * " + CC.Gray + "divine aura surrounding it."))),
    ELF(new MaskData(null, "", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmNlNjg1YmVhOGY2ZDk2NmU1MzNlMmFmNWJhYTU1NzdlMWE5OGY2Y2FkMTFiOGYwZjdmMjAxYmJlZjZlODIyIn19fQ===", "Elf Mask", true, UUID.randomUUID(), CC.GreenB + "Elf Mask", CC.Red + "25% Negate Trap Blocker, 1.5x Pet Duration, 25% immune to Monopoly Effect, 15% to reheal when under 5HP", Lists.newArrayList(CC.Red + "1.5x Pet Duration", CC.Red + "25% Chance to negate Trap Blocker", CC.Red + "25% Chance to be immune to Monopoly Effect", CC.Red + "15% Chance to reheal when under 5HP",  CC.Gray + " * " + CC.Gray + "Christmas 2021 Edition."))),
    EQUALIZER(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NjgwNjMzMjAzMzMsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84YWU4NGI5Mjg1ZjIyYmI1MmUzNjEzYjFkNTc5MmY1YTQ3OWI3NWQ1MjhlNGZlNjQyZGVmNmNkYjlkYTQwYjVjIn19fQ==", "Equalizer Mask", true, UUID.randomUUID(), CC.AquaB + "Equalizer Mask", CC.Red + "Immune to Stitches Pet", Lists.newArrayList(CC.Red + "Immune to Stitches Pet", CC.Gray + "You will be assimilated.", CC.Gray + "Resistance is futile.", CC.GoldB + " * " + CC.Gold + "Map One"))),
    KILLER_CLOWN(new MaskData(null, "", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODEzN2FjY2E3NmRlYWZhMGMzZTFjMTZiZTE4ZDg0YTA5ZGMzM2QzYzZjODc2ZDM2NzA1Y2RmYTJlMzQxMzFiYyJ9fX0====", "Killer Clown Mask", true, UUID.randomUUID(), CC.RedB + "Killer Clown Mask", CC.Red + "Immune to critical hits", Lists.newArrayList(CC.Red + "Immune to critical hits", CC.Gray + "Halloween is around the corner and", CC.Gray + "that means a lot of scary new features...", CC.GoldB + " * " + CC.Gold + "Astro 4.0 - Map I"))),

    M_ANIMAL_CONTROL(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NzQxMzU1OTc0NTMsInByb2ZpbGVJZCI6IjdjODk1YWQwMTFkMDQzNTA5YWU1ZjJiYjFjZjZjOGVhIiwicHJvZmlsZU5hbWUiOiJCYXNpY0JBRSIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85NTZjZWNlYTA5MzU3NDhiMTA2ZTM2ZjhlZmE2NmNjOGExN2IxODdiNjgzMzU5YWQ1MWEwNmM3MzJlYjg5YWE2In19fQ==",
            "Mutated Animal Control Mask", true, UUID.randomUUID(),
            CC.DarkGreenB + CC.Underline + "Mutated" + CC.Reset + CC.AquaB + " Animal Control Mask",
            CC.DarkGreen + "66% Pet Leash Negation",
            Lists.newArrayList(CC.DarkGreen + "66% Pet Leash Negation", CC.Gray + "Hide your cats, dogs, and pets -", CC.Gray + "animal control is coming for you!", CC.GoldB + " * " + CC.Gold + "Map One"))),

    M_MONOPOLY(new MaskData(null, "", "eyJ0aW1lc3RhbXAiOjE1NDI3MDEwNDEwNjMsInByb2ZpbGVJZCI6ImEzOWVmNTdhMzg3YzRjZDE5NzQwNTliNzMyZWJjN2E1IiwicHJvZmlsZU5hbWUiOiJNZXJjaGFudCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hM2M0ZDA5ZGViNzkwNDkyYTA0OTIxNTA3OTc3Y2FiOTI2MzViODgxODRkNDg5ZjgzYzM0YjYwZTdkOGE0MjViIn19fQ==",
            "Mutated Monopoly", true, UUID.randomUUID(),
            CC.DarkGreenB + CC.Underline + "Mutated" + CC.Reset + CC.AquaB + " Monopoly Mask",
            CC.DarkGreen + "66% Holy White Scroll negation",
            Lists.newArrayList(CC.DarkGreen + "66% Holy White Scroll negation", CC.DarkGreen + "-5% ENEMY DMG", CC.Gray + "The mask of a man who has it all,", CC.Gray + "a truly powerful entity to contest with.", CC.GoldB + " * " + CC.Gold + "Map One"))),

    M_CRANKED(new MaskData(null, "", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE4N2NkNzUyM2RjYzRjYmVhZWFlYzljOGJlYjg5MmExMDNiOTUzOTVlMDY1ZjI1YmI0ZmM3YTZiZDRiOTAyYyJ9fX0=",
            "Mutated Cranked Mask", true, UUID.randomUUID(),
            CC.DarkGreenB + CC.Underline + "Mutated" + CC.Reset + CC.DarkRedB + " Cranked Mask",
            CC.DarkGreen + "75% To Block Util Belt Skin, 7% Extra Damage",
            Lists.newArrayList(CC.DarkGreen + "75% To Block Util Belt Skin", CC.DarkGreen + "7% Extra Damage" , CC.Gray + "A great evil is contained within this", CC.Gray + "horrifying mask. Who knows what inner", CC.Gray + "demons it will unleash.", CC.GoldB + " * " + CC.Gold + "Map Five"))),

    M_KILLER_CLOWN(new MaskData(null, "", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODEzN2FjY2E3NmRlYWZhMGMzZTFjMTZiZTE4ZDg0YTA5ZGMzM2QzYzZjODc2ZDM2NzA1Y2RmYTJlMzQxMzFiYyJ9fX0====",
            "Mutated Killer Clown Mask", true, UUID.randomUUID(),
            CC.DarkGreenB + CC.Underline + "Mutated" + CC.Reset + CC.RedB + " Killer Clown Mask",
            CC.DarkGreen + "Immune to critical hits, 1.1x Critical Hit Damage",
            Lists.newArrayList(CC.DarkGreen + "Immune to critical hits", CC.DarkGreen + "1.1x Critical Hit Damage", CC.Gray + "Halloween is around the corner and", CC.Gray + "that means a lot of scary new features...", CC.GoldB + " * " + CC.Gold + "Astro 4.0 - Map V"))),

    ;


    public static Map<UUID, MaskType> equippedMasks;
    private final MaskData maskData;

    public boolean isActive() {
        return this.getMaskData().isActive();
    }

    public String getURL() {
        return "http://textures.minecraft.net/texture/" + this.getMaskData().getBase64();
    }

    public static boolean hasEquipped(Player player, String masktype) {
        if (masktype == null) {
            return false;
        }
        MaskType type = MaskType.getFromName(masktype.toUpperCase());
        return type != null && type.hasEquipped(player);
    }

    public static ItemStack createRandomMaskItem(boolean applyIID) {
        ArrayList lore = Lists.newArrayList("", CC.Gray + "Click to generate one of", CC.Gray + "the following player masks:");
        for (MaskType type : MaskType.values()) {
            if (!MaskListener.availableMasks.contains(type) && !MaskListener.rareMasks.contains(type)) continue;
            lore.add(CC.GoldB + " * " + CC.Gray + ChatColor.stripColor(type.getMaskData().getDisplayName()));
        }
        ItemStack retr = new NBTWrapper(new ItemBuilder(Material.CHEST, CC.GoldB + "Mask Generator " + CC.Gray + "(Right Click)").lore(lore).build()).setString("cosmicType", "RANDOM_MASK").build();
        if (applyIID && ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
            retr = ItemID.assignItemID(null, retr);
        }
        return retr;
    }

    public boolean hasEquipped(Player player) {
        if (player == null) {
            return false;
        }
        if (player.getInventory() == null) {
            return false;
        }
        ItemStack item = player.getInventory().getHelmet();
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        MaskType found = MaskType.fromItem(item);
        if (found == MULTI_MASK) {
            return MultiMaskItem.getMasks(new NBTWrapper(item)).contains(this);
        }
        return found != null && found == this;
    }

    public static MaskType fromPlayer(Player player) {
        ItemStack item = player.getInventory().getHelmet();
        if (item != null && item.getType() != Material.AIR) {
            return MaskType.fromItem(item);
        }
        return null;
    }

    public static MaskType fromItem(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            return MaskType.fromWrapper(new NBTWrapper(item));
        }
        return null;
    }

    public static MaskType fromWrapper(NBTWrapper wrapper) {
        NBTTagCompoundBuilder cosmicData = wrapper.getCompound("conaxData");
        if (cosmicData == null) {
            return null;
        }
        return cosmicData.hasKey("maskOverride") ? MaskType.getFromName(cosmicData.getString("maskOverride").toUpperCase()) : null;
    }

    public ItemStack createMask(boolean applyIID) {
        ItemStack retr = new ItemBuilder(Material.SKULL_ITEM, this.getMaskData().getDisplayName()).lore(this.getMaskData().getLore()).data((short)3).build();
        if (applyIID && ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
            retr = ItemID.assignItemID(null, retr);
        }
        return this.applyNbt(new NBTWrapper(retr).setString("cosmicType", "COSMIC_MASK").setNBTBase("conaxData", new NBTTagCompoundBuilder().setString("maskType", this.name()).get())).build();
    }

    public NBTWrapper applyNbt(NBTWrapper wrapper) {
        wrapper.setShort("id", Short.valueOf((short)Material.SKULL_ITEM.getId()));
        wrapper.setShort("Damage", Short.valueOf((short)3));
        return wrapper.setNBTBase("SkullOwner", this.getMaskData().getSkullOwnerBuilder().get());
    }

    public ItemStack applyMaskNBT(NBTWrapper maskWrapper, NBTWrapper itemWrapper) {
        ItemStack retr;
        ItemMeta im;
        NBTTagCompoundBuilder builder = new NBTTagCompoundBuilder(itemWrapper.getNBTBase("conaxData"));
        builder.setString("maskOverride", this.name());
        if (this == MULTI_MASK) {
            List<MaskType> masks = MultiMaskItem.getMasks(maskWrapper);
            builder.setString("multiMask", MultiMaskItem.listToString(masks));
        }
        List<String> lore = (im = (retr = itemWrapper.setNBTBase("conaxData", builder.get()).build()).getItemMeta()).getLore() == null ? Lists.newArrayList() : im.getLore();
        for (String str : lore) {
            if (!str.startsWith(CC.GrayB + "ATTACHED:")) continue;
            return retr;
        }
        if (this == MULTI_MASK) {
            String[] rawMasks = builder.getString("multiMask").split(",");
            String masks2 = "";
            for (String s : rawMasks) {
                masks2 = masks2 + MaskType.valueOf(s).getMaskData().getDisplayName() + CC.White + ", ";
            }
            masks2 = masks2.substring(0, masks2.lastIndexOf(","));
            lore.add(CC.GrayB + "ATTACHED: " + this.getMaskData().getDisplayName() + CC.White + " (" + masks2 + CC.White + ")");
        } else {
            lore.add(CC.GrayB + "ATTACHED: " + this.getMaskData().getDisplayName() + (this.getMaskData().getSpecialAbility() != null ? CC.White + " (" + this.getMaskData().getSpecialAbility() + CC.White + ")" : ""));
        }
        im.setLore(lore);
        retr.setItemMeta(im);
        return retr;
    }

    public ItemStack removeMaskNBT(NBTWrapper wrapper, Material helmet) {
        ItemStack retr;
        ItemMeta im;
        List<String> lore;
        NBTTagCompoundBuilder builder = new NBTTagCompoundBuilder(wrapper.getNBTBase("conaxData"));
        String current = builder.getString("maskOverride");
        if (current != null && !current.isEmpty()) {
            builder.remove("maskOverride");
            wrapper.setNBTBase("conaxData", builder.get());
        }
        if (helmet == Material.DIAMOND_HELMET && wrapper.hasKey("conaxData")) {
            builder.remove("durability");
            builder.remove("maxDurability");
            if (builder.get() != null && builder.get().isEmpty()) {
                wrapper.remove("conaxData");
            }
        }
        if ((lore = (im = (retr = wrapper.build()).getItemMeta()).getLore()) != null) {
            lore.removeIf(next -> next.startsWith(CC.GrayB + "ATTACHED:"));
            im.setLore(lore);
            retr.setItemMeta(im);
            return retr;
        }
        return retr;
    }

    public static MaskType getFromName(String name) {
        for (MaskType type : MaskType.values()) {
            if (!type.name().equals(name)) continue;
            return type;
        }
        return null;
    }

    public static void scheduleRescan(Player player) {
        if (CosmicMasks.get().isEnabled()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicMasks.get(), () -> MaskType.rescanMask(player, false), 1L);
        }
    }

    public static void rescanMask(Player player, boolean update) {
        ItemStack helmet = player.getEquipment().getHelmet();
        MaskType equippedMask = MaskType.fromItem(helmet);
        MaskType currentlyStored = equippedMasks.get(player.getUniqueId());
        if (currentlyStored != equippedMask) {
            if (currentlyStored != null && currentlyStored.getMaskData().getInternalMask() != null) {
                currentlyStored.getMaskData().getInternalMask().onUnequip(player);
            }
            if (equippedMask == null) {
                equippedMasks.remove(player.getUniqueId());
            } else {
                equippedMasks.put(player.getUniqueId(), equippedMask);
                if (equippedMask.getMaskData().getInternalMask() != null) {
                    equippedMask.getMaskData().getInternalMask().onEquip(player);
                }
            }
        } else if (update && equippedMask != null && equippedMask.getMaskData().getInternalMask() != null) {
            equippedMask.getMaskData().getInternalMask().onUpdate(player);
        }
    }

    MaskType(MaskData maskData) {
        this.maskData = maskData;
    }

    public MaskData getMaskData() {
        return this.maskData;
    }

    public static Map<UUID, MaskType> getEquippedMasks() {
        return equippedMasks;
    }

    static {
        equippedMasks = new ConcurrentHashMap<UUID, MaskType>();
    }
}

