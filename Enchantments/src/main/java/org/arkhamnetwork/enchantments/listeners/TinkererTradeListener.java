package org.arkhamnetwork.enchantments.listeners;

import org.bukkit.event.Listener;

public class TinkererTradeListener implements Listener {
   /* int randMeta;

    public TinkererTradeListener() {
        this.randMeta = new Random().nextInt(10);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick_HIGH(final InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getInventory().getTitle().contains("Cosmic Tinkerer")) {
            final Player p1 = (Player) e.getWhoClicked();
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            if (e.getWhoClicked().getInventory() == null || e.getCurrentItem() == null) {
                return;
            }
            if (e.getRawSlot() == 0) {
                e.getInventory().setItem(0, TinkererListener.green_button);
                e.getInventory().setItem(8, TinkererListener.green_button);
                this.acceptTinkererTrade(p1);
                return;
            }
            if (e.getRawSlot() == 49) {
                int added = 0;
                final Inventory me.astropvp.cratekeys.inventory = p1.getOpenInventory().getTopInventory();
                for (int i = 0; i < p1.getInventory().getSize(); ++i) {
                    final ItemStack item = p1.getInventory().getItem(i);
                    if (item != null) {
                        if (item.getType() != Material.AIR) {
                            final boolean tinkerable = this.isTinkerable(item);
                            final PlayerAttemptTinkerItem tinker = new PlayerAttemptTinkerItem(p1, new TinkeredItem(item, this.convertItemToXP(item), 1.0, this.convertItemIntoItem(item)));
                            if (!tinkerable) {
                                tinker.setCancelled(true);
                            }
                            if (tinker.callEvent()) {
                                //final int nextOpenSlot = NewTradeListener.getNextOpenTradeSlot(me.astropvp.cratekeys.inventory);
                                if (nextOpenSlot == -1) {
                                    break;
                                }
                                p1.getInventory().setItem(i, (ItemStack) null);
                                //p1.getOpenInventory().getTopInventory().setItem(nextOpenSlot, item);
                                ++added;
                            }
                        }
                    }
                }
                if (added <= 0) {
                    p1.sendMessage(CC.RedB + "(!) " + CC.Red + "The Tinkerer is not interested in any of your me.astropvp.cratekeys.inventory me.astropvp.cratekeys.items!");
                    return;
                }
                p1.sendMessage(CC.GreenB + "(!) " + CC.Green + "Offering " + CC.GreenB + added + " " + CC.Green + "me.astropvp.cratekeys.items to the Tinkerer.");
            } else if (e.getWhoClicked().getInventory().equals(p1.getOpenInventory().getTopInventory())) {
                //if (!NewTrade.getValidSlots().contains(e.getRawSlot())) {
                    //return;
                //}
                final ItemStack j = e.getCurrentItem();
                e.setCurrentItem((ItemStack) null);
                p1.getInventory().setItem(p1.getInventory().firstEmpty(), j);
            } else if (e.getWhoClicked().getInventory().equals(p1.getOpenInventory().getBottomInventory())) {
                final int tradeSlot = NewTradeListener.getNextOpenTradeSlot(p1.getOpenInventory().getTopInventory());
                if (tradeSlot == -1) {
                    return;
                }
                final ItemStack k = e.getCurrentItem().clone();
                final boolean tinkerable2 = this.isTinkerable(k);
                final PlayerAttemptTinkerItem tinkerEvent = new PlayerAttemptTinkerItem(p1, new TinkeredItem(k, tinkerable2 ? this.convertItemToXP(k) : 0.0, 1.0, this.convertItemIntoItem(k)));
                if (!tinkerable2) {
                    tinkerEvent.setCancelled(true);
                }
                if (!tinkerEvent.callEvent()) {
                    p1.sendMessage(ChatColor.RED + "The Astro Tinkerer doesn't want that item.");
                    p1.sendMessage(ChatColor.GRAY + "He will only accept enchanted equipment and non-soul enchantment books.");
                    return;
                }
                e.setCurrentItem((ItemStack) null);
                p1.getOpenInventory().getTopInventory().setItem(tradeSlot, k);
            }
        }
    }

    private boolean isTinkerable(final ItemStack i) {
        if (i != null && i.getType() != Material.AIR && this.convertItemToXP(i) <= 0) {
            final boolean masteryShards = this.convertItemIntoItem(i) != null;
            return (EnchantmentBook.isEnchantmentBook(i) && CommandEnchDust.dustEnabled && TieredEnchantments.getEnchantmentTier(EnchantmentBook.getEnchantment(i).name()) != 6) || masteryShards;
        }
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onInventoryClick_MONITOR(final InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getInventory().getTitle().contains("Cosmic Tinkerer")) {
            if (e.getRawSlot() == 0 || e.getRawSlot() == 8) {
                return;
            }
            if (!e.isCancelled()) {
                e.setCancelled(true);
            }
            if (e.getResult() != Event.Result.DENY) {
                e.setResult(Event.Result.DENY);
            }
            final Player p1 = (Player) e.getWhoClicked();
            double exchangeMod = 1.0;
            if (CosmicOutpostsAPI.controlsVanillaOutpost(p1)) {
                exchangeMod = 2.0;
            }
            final Inventory inv1 = p1.getOpenInventory().getTopInventory();
            for (final int slot : NewTrade.getValidSlots()) {
                final ItemStack slotItem = inv1.getItem(slot);
                if (slotItem != null) {
                    ItemStack convertedShards = this.convertItemIntoItem(slotItem);
                    final int exp = this.convertItemToXP(slotItem);
                    final int success;
                    if (convertedShards == null && exp <= 0 && EnchantmentBook.isEnchantmentBook(slotItem) && (success = EnchantmentBook.getEnchantmentBookSuccessRate(slotItem)) > 0) {
                        if (slotItem.getItemMeta().getDisplayName().startsWith(ChatColor.RED.toString())) {
                            continue;
                        }
                        final CustomEnchantment bookEnchant = EnchantmentBook.getEnchantment(slotItem);
                        if (bookEnchant == null) {
                            continue;
                        }
                        final ItemRarity bookRarity = CosmicSpaceChestsAPI.getItemRarity(TieredEnchantments.getEnchantmentTier(bookEnchant.name()));
                        if (bookRarity != ItemRarity.UNKNOWN) {
                            convertedShards = EnchantmentBook.getSecretDust(bookRarity, success);
                        }
                    }
                    final TinkeredItem tinkeredItem = new TinkeredItem(slotItem, exp, exchangeMod, convertedShards);
                    final PlayerAttemptTinkerItem tinkerItemEvent = new PlayerAttemptTinkerItem(p1, tinkeredItem);
                    if (tinkeredItem.getOfferForItem() == null && tinkeredItem.getXpValue() <= 0.0) {
                        tinkerItemEvent.setCancelled(true);
                    }
                    if (!tinkerItemEvent.callEvent()) {
                        continue;
                    }
                    if (tinkeredItem.getOfferForItem() != null) {
                        inv1.setItem((int) NewTrade.getLinkedSlot(slot), tinkeredItem.getOfferForItem());
                    } else {
                        if (tinkeredItem.getXpValue() <= 0.0) {
                            continue;
                        }
                        inv1.setItem((int) NewTrade.getLinkedSlot(slot), ExpBottles.getXpBottle((int) (tinkeredItem.getXpValue() * tinkeredItem.getExpModifier()), "Cosmic Tinkerer"));
                    }
                } else {
                    if (slot == 0) {
                        continue;
                    }
                    inv1.setItem((int) NewTrade.getLinkedSlot(slot), new ItemStack(Material.AIR));
                }
            }
            p1.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().getTitle().contains("Astro Tinkerer")) {
            final Player p1 = (Player) e.getPlayer();
            if (e.getInventory().getItem(0) != null && e.getInventory().getItem(0).equals((Object) TinkererListener.green_button) && e.getInventory().getItem(8) != null && e.getInventory().getItem(8).equals((Object) TinkererListener.green_button)) {
                return;
            }
            for (final int slot : NewTrade.getValidSlots()) {
                if (e.getInventory().getItem(slot) != null && e.getInventory().getItem(slot).getType() != Material.STAINED_GLASS_PANE) {
                    p1.getInventory().addItem(new ItemStack[]{e.getInventory().getItem(slot)});
                }
            }
            p1.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Trade cancelled.");
            e.getInventory().clear();
            p1.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryCloseTinkerer(final InventoryCloseEvent e) {
        if (e.getInventory().getTitle().contains("Astro Tinkerer")) {
            e.getInventory().clear();
        }
    }

    @EventHandler
    public void onItemPickup(final PlayerPickupItemEvent e) {
        if (e.getPlayer().getOpenInventory() != null && e.getPlayer().getOpenInventory().getTitle().contains("Cosmic Tinkerer")) {
            e.setCancelled(true);
        }
    }

    public ItemStack convertItemIntoItem(final ItemStack armor) {
        if (armor == null) {
            return null;
        }
        final Material type = armor.getType();
        if (Enchantments.getInstance().armorsetEnabled && (type == Material.LEATHER_BOOTS || type == Material.LEATHER_HELMET || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_LEGGINGS)) {
            final NBTWrapper wrapper = new NBTWrapper(armor);
            final NBTTagCompoundBuilder cosmicData = wrapper.getCompound("conaxData");
            if (cosmicData != null && cosmicData.hasKey("abilitySet")) {
                String abilitySet = cosmicData.getString("abilitySet");
                final ItemMeta im = armor.getItemMeta();
                if (im instanceof LeatherArmorMeta && abilitySet.equalsIgnoreCase("NECROMANCER") && ((DeathKnightAbilitySet) AbilityArmorSet.DEATH_KNIGHT.getArmorSetInfo()).getArmorColor().equals((Object) ((LeatherArmorMeta) im).getColor())) {
                    abilitySet = "DEATH_KNIGHT";
                }
                final AbilityArmorSet set = AbilityArmorSet.fromName(abilitySet);
                if (set != null) {
                    return set.createMasteryShard(true);
                }
            }
        }
        return null;
    }

    private int convertItemToXP(final ItemStack i) {
        int xpValue = 0;
        if (i == null || i.getMaxStackSize() > 1) {
            return xpValue;
        }
        final int enchantmentCount = EnchantmentAPI.getAllEnchantments(i).size();
        for (final Map.Entry<CustomEnchantment, Integer> customEnchant : EnchantmentAPI.getAllEnchantments(i).entrySet()) {
            try {
                if (Enchantment.getByName(customEnchant.getKey().name()) != null) {
                    continue;
                }
            } catch (NullPointerException npe) {
                continue;
            }
            final int enchantRarity = TieredEnchantments.getEnchantmentTier(customEnchant.getKey().name());
            xpValue += (int) ((customEnchant.getValue() + enchantRarity) * TinkererListener.modifier);
        }
        if (enchantmentCount > 0) {
            xpValue += enchantmentCount * 5;
        }
        if (xpValue < 20 && xpValue > 0) {
            xpValue = 20 + this.randMeta;
        }
        return xpValue * enchantmentCount * i.getAmount();
    }

    private void acceptTinkererTrade(final Player p) {
        Inventory inv1 = p.getOpenInventory().getTopInventory();
        List<ItemStack> given = new ArrayList<>();
        List<ItemStack> received = new ArrayList<>();
        int xpCount = 0;
        for (final Map.Entry<Integer, Integer> me.astropvp.cratekeys.data : NewTrade.slotConversionMap.entrySet()) {
            int slot = me.astropvp.cratekeys.data.getValue();
            int traderSlot = me.astropvp.cratekeys.data.getKey();
            ItemStack item = inv1.getItem(slot);
            if (item != null && item.getType() != Material.STAINED_GLASS_PANE) {
                ItemStack giving = inv1.getItem(traderSlot);
                Material type = (giving != null) ? giving.getType() : null;
                if (giving != null && type != Material.AIR && type != Material.STAINED_GLASS_PANE) {
                    given.add(giving);
                }
                if (ExpBottles.isXpBottle(item)) {
                    xpCount += ExpBottles.getXpAmount(item);
                    p.getOpenInventory().getTopInventory().setItem(slot, (ItemStack) null);
                    p.getOpenInventory().getTopInventory().setItem(traderSlot, (ItemStack) null);
                } else {
                    p.getOpenInventory().getTopInventory().setItem(slot, (ItemStack) null);
                    p.getOpenInventory().getTopInventory().setItem(traderSlot, (ItemStack) null);
                    p.getInventory().addItem(new ItemStack[]{item});
                    received.add(item);
                }
            }
        }
        if (xpCount > 0) {
            final ItemStack xp_bottle = ExpBottles.getXpBottle(xpCount, "Astro Tinkerer");
            p.getInventory().addItem(new ItemStack[]{xp_bottle});
            received.add(xp_bottle);
        }
        p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Trade accepted.");
        p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.5f);
        p.closeInventory();
        Bukkit.getPluginManager().callEvent((Event) new CustomCosmicPlayerEvent(p, "TINKERER_TRADE", new Object[]{given, received}));
    }*/
}
