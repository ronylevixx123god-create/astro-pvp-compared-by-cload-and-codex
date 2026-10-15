package org.demonicpvp.demonictrials.item;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.demonicpvp.demonictrials.trial.trinket.ITrialTrinket;
import org.demonicpvp.demonictrials.trial.trinket.TrialTrinket;
import org.javatuples.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class TrialTrinketCustomItem extends CosmicItem implements InventoryInteractable {
    public final static String TRIAL_TRINKET_LIST = "trialTrinketList";
    public final static String TRIAL_TRINKET_TYPE_KEY = "trialTrinketType";
    public final static String TRIAL_TRINKET_AMPLIFIER_KEY = "trialTrinketAmplifier";

    public TrialTrinketCustomItem() {
        super("trial_trinket");
    }

    @Override
    public ItemStack createItem(Player player, Object... objects) {
        ITrialTrinket trinketType = TrialTrinket.valueOf(objects[0].toString().toUpperCase()).getInstance();
        int amplifier = Integer.parseInt(objects[1].toString());

        List<String> lore = trinketType.getLore(amplifier);
        lore.add("");
        lore.add("&7Drag n' drop onto a Trial Portal to apply.");

        if (trinketType.equals(TrialTrinket.SKIP_ROOMS.getInstance()) && amplifier > 5) {
            amplifier = 5;
        }

        return this.applyItemData(
                CommonsUtil.createItem(
                        Material.INK_SACK,
                        "&6&lTrial Trinket (" + trinketType.getFormattedName(amplifier) + "&6&l)",
                        1,
                        14,
                        lore
                )
        )
                .setString(TRIAL_TRINKET_TYPE_KEY, objects[0].toString().toUpperCase())
                .setInteger(TRIAL_TRINKET_AMPLIFIER_KEY, amplifier)
                .build();
    }

    @Override
    public boolean acceptsInventoryClickType(InventoryItemType type) {
        return type == InventoryItemType.CURSOR;
    }

    @Override
    public void onInventoryClick(Player player, ItemStack cursor, NBTWrapper cursorWrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent event) throws CosmicException {
        if (clicked == null || clickedWrapper == null || clicked.getType() != Material.EYE_OF_ENDER) return;

        if (clickedWrapper.hasKey("cosmicType") && clickedWrapper.getString("cosmicType").equals("trial_portal")) {
            if (clicked.getAmount() > 1) {
                CommonsUtil.sendMessage(player, "&c&l(!) &cYou cannot apply Trial Trinkets to stacked portals!");
                event.setCancelled(true);
                return;
            }

            if (!clickedWrapper.hasKey(TRIAL_TRINKET_LIST)) {
                clickedWrapper.setString(TRIAL_TRINKET_LIST, "");
            }

            ITrialTrinket addingTrinket = TrialTrinket.valueOf(cursorWrapper.getString(TRIAL_TRINKET_TYPE_KEY)).getInstance();
            int addingAmplifier = cursorWrapper.getInt(TRIAL_TRINKET_AMPLIFIER_KEY);

            List<Pair<ITrialTrinket, Integer>> trinkets = getTrinkets(clicked);

            if (trinkets.stream().anyMatch(t -> t.getValue0().equals(addingTrinket))) {
                CommonsUtil.sendMessage(player, "&c&l(!) &cThis Trial portal already has this trinket!");
                event.setCancelled(true);
                return;
            }

            trinkets.add(Pair.with(addingTrinket, addingAmplifier));

            clickedWrapper.setString(TRIAL_TRINKET_LIST, trinkets.stream().map(pair -> pair.getValue0().getId() + "|" + pair.getValue1()).collect(Collectors.joining(",")));

            if (event.getCursor().getAmount() > 1) {
                event.getCursor().setAmount(event.getCursor().getAmount() - 1);
            } else {
                event.setCursor(null);
            }

            ItemStack built = clickedWrapper.build();

            ItemMeta itemMeta = built.getItemMeta();
            List<String> lore = itemMeta.getLore();

            if (lore.get(lore.size() - 1).endsWith("information.")) {
                lore.add("");
                lore.add(CommonsUtil.color("&6&lTrinkets:"));
            }

            lore.add(CommonsUtil.color("&6&l* " + addingTrinket.getFormattedName(addingAmplifier)));

            List<String> addingTrinketLore = addingTrinket.getLore(addingAmplifier);

            for (int i = 0; i < addingTrinketLore.size(); i++) {
                String line = addingTrinketLore.get(i);

                lore.add(CommonsUtil.color("&7 " + (i == 0 ? "(" : "") + line));

                if (i == addingTrinketLore.size() - 1) {
                    lore.set(lore.size() - 1, CommonsUtil.color(lore.get(lore.size() - 1) + ")"));
                }
            }

            itemMeta.setLore(lore);
            built.setItemMeta(itemMeta);

            event.setCurrentItem(built);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);

            event.setCancelled(true);
        }
    }

    public static List<Pair<ITrialTrinket, Integer>> getTrinkets(ItemStack portal) {
        NBTWrapper wrapper = new NBTWrapper(portal);

        if (!wrapper.hasKey(TRIAL_TRINKET_LIST)) {
            wrapper.setString(TRIAL_TRINKET_LIST, "");
            wrapper.build();
        }

        List<Pair<ITrialTrinket, Integer>> trinkets = Lists.newArrayList();

        for (String trinketInfo : wrapper.getString(TRIAL_TRINKET_LIST).split(",")) {
            if (trinketInfo.isEmpty()) continue;

            String trinketName = trinketInfo.split("\\|")[0];
            int trinketAmplifier = Integer.parseInt(trinketInfo.split("\\|")[1]);

            trinkets.add(Pair.with(TrialTrinket.valueOf(trinketName).getInstance(), trinketAmplifier));
        }

        return trinkets;
    }
}
