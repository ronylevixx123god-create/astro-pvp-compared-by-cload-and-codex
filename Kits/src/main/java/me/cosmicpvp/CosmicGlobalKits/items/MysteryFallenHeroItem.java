package me.cosmicpvp.CosmicGlobalKits.items;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import lombok.SneakyThrows;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.libs.FallenHero;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.utils.WorldGuardUtils;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.PlayerInteractable;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MysteryFallenHeroItem
extends CosmicItem
implements PlayerInteractable {
    private final List<GlobalKits> commonKits = Lists.newArrayList(new GlobalKits[]{GlobalKits.HYPER, GlobalKits.DESTROYER, GlobalKits.GUARDIAN, GlobalKits.ARCANE, GlobalKits.TRICKSTER, GlobalKits.VIKING, GlobalKits.Tinkermaster, GlobalKits.Admiral, GlobalKits.Paladin});
    private final List<GlobalKits> uncommonKits = Lists.newArrayList(new GlobalKits[]{GlobalKits.Spooky, GlobalKits.GLADIATOR, GlobalKits.TEMPLAR, GlobalKits.Party, GlobalKits.GrandMaster, GlobalKits.Independence, GlobalKits.Masterbuilder, GlobalKits.Butcher});
    private final List<GlobalKits> rareKits = Lists.newArrayList(new GlobalKits[]{GlobalKits.Cosmonaut, GlobalKits.DIABLO, GlobalKits.Enchantment, GlobalKits.Voidwalker, GlobalKits.Warlock, GlobalKits.Loki, GlobalKits.Cannoner});

    public MysteryFallenHeroItem() {
        super("mystery_fallen_hero");
    }

    public ItemStack createItem(Player player, Object ... objects) {
        String displayName = CC.WhiteB + CC.Underline + "Fallen Hero:" + CC.Reset + " ???";
        ArrayList lore = Lists.newArrayList(new String[]{(Object)ChatColor.GRAY + "Summons a mystery fallen hero equipped with", (Object)ChatColor.GRAY + "a " + CC.WhiteU + "RANDOM" + CC.Gray + " /gkit equipment loadout.", "", (Object)ChatColor.GRAY + "Defeat this hero for a " + (Object)ChatColor.UNDERLINE + "guaranteed", (Object)ChatColor.GRAY + "drop of its " + (Object)ChatColor.WHITE + "/gkit item set" + (Object)ChatColor.GRAY + " and a ", (Object)ChatColor.WHITE + "" + (Object)ChatColor.UNDERLINE + "50% chance" + (Object)ChatColor.GRAY + " to get a " + (Object)ChatColor.WHITE + "/gkit Redemption Gem", (Object)ChatColor.GRAY + "that can be used to " + (Object)ChatColor.UNDERLINE + "unlock the /gkit", (Object)ChatColor.GRAY + "on your own account for regular use!", "", ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "USE: " + (Object)ChatColor.GRAY + "To summon this fallen hero", (Object)ChatColor.WHITE + "Right-Click" + (Object)ChatColor.GRAY + " this item inside the Warzone.", "", ChatColor.RED.toString() + ChatColor.BOLD.toString() + "WARNING: " + (Object)ChatColor.GRAY + "Whoever defeats the Fallen Hero", (Object)ChatColor.GRAY + "will receive its legendary treasures.", (Object)ChatColor.GRAY + "Do " + (Object)ChatColor.RED + "" + (Object)ChatColor.UNDERLINE + "not" + (Object)ChatColor.GRAY + " place in confined spaces,", (Object)ChatColor.GRAY + "as minecraft mobs tend to block glitch."});
        return this.applyItemData(new ItemBuilder(Material.BONE, displayName, new String[0]).lore((List)lore).build()).assignIID().build();
    }

    @SneakyThrows
    public void onPlayerInteractable(Player player, ItemStack itemStack, NBTWrapper nbtWrapper, PlayerInteractEvent playerInteractEvent) throws CosmicException {
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK || playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR) {
            playerInteractEvent.setCancelled(true);
            playerInteractEvent.setUseItemInHand(Event.Result.DENY);
            playerInteractEvent.setUseInteractedBlock(Event.Result.DENY);
            playerInteractEvent.setCancelled(true);
            playerInteractEvent.setUseItemInHand(Event.Result.DENY);
            Player p = playerInteractEvent.getPlayer();
            FLocation fl = new FLocation(p.getLocation());
            if (!Board.getInstance().getFactionAt((FLocation)fl).isWarZone()) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot summon a Fallen Hero outside the Warzone.");
                return;
            }
            if (com.cosmicpvp.cosmicutils.utils.WorldGuardUtils.isInRegionsContainsFast((Location)p.getLocation(), (List)Lists.newArrayList(new String[]{"sell_room"}))) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot summon a Fallen Hero at this location.");
                return;
            }
            if (WorldGuardUtils.isPvPDisabled(p.getLocation())) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot summon a Fallen Hero inside a Safezone.");
                return;
            }
            if (p.getWorld().getEnvironment() == World.Environment.NETHER && p.getLocation().getY() >= 128.0) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot summon a Fallen Hero at this location.");
                return;
            }
            if (p.getWorld().getName().contains("world_duels")) {
                p.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot summon a fallen hero inside a duel!");
                return;
            }
            GlobalKits gkit = this.getRandomGlobalKit();
            if (gkit != null) {
                ItemStack i = playerInteractEvent.getItem();
                if (i.getAmount() > 1) {
                    i.setAmount(i.getAmount() - 1);
                    p.setItemInHand(i);
                } else {
                    p.setItemInHand(null);
                }
                Location l = p.getTargetBlock((HashSet<Byte>) null, 6).getLocation().add(0.0, 1.0, 0.0);
                LivingEntity fallenHero = FallenHero.spawnFallenHero(gkit, l);
                fallenHero.setMetadata("fallenHeroSummoner", (MetadataValue)new FixedMetadataValue((Plugin)CosmicGlobalKits.getPlugin(), (Object)p.getName()));
                fallenHero.setMetadata("mysteryFallenHero", (MetadataValue)new FixedMetadataValue((Plugin)CosmicGlobalKits.getPlugin(), (Object)true));
                String itemName = gkit.getItemName().replace(" Kit", "");
                p.sendMessage("");
                p.sendMessage((Object)ChatColor.WHITE + "" + (Object)ChatColor.BOLD + "*** " + (Object)ChatColor.UNDERLINE + "Fallen Hero:" + (Object)ChatColor.RESET + " " + itemName + (Object)ChatColor.WHITE + "" + (Object)ChatColor.BOLD + " ***");
                p.sendMessage((Object)ChatColor.GRAY + "You have summoned a Fallen Hero! Defeat it to claim its treasures and a chance to " + (Object)ChatColor.UNDERLINE + "unlock its /gkit!");
                p.sendMessage("");
                for (Entity ent : p.getNearbyEntities(32.0, 32.0, 32.0)) {
                    if (!(ent instanceof Player)) continue;
                    Player pNear = (Player)ent;
                    pNear.sendMessage((Object)ChatColor.YELLOW + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.YELLOW + p.getName() + " has summoned a " + itemName + (Object)ChatColor.YELLOW + " Fallen Hero!");
                    pNear.sendMessage((Object)ChatColor.GRAY + "Locate and slay the Fallen Hero to steal its loot for yourself!");
                }
                p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.2f);

            }
        }
    }

    private GlobalKits getRandomGlobalKit() {
        double d = Math.random();
        if (d < 0.7) {
            return this.commonKits.get(new Random().nextInt(this.commonKits.size()));
        }
        if (d < 0.9) {
            return this.uncommonKits.get(new Random().nextInt(this.uncommonKits.size()));
        }
        return this.rareKits.get(new Random().nextInt(this.rareKits.size()));
    }
}

