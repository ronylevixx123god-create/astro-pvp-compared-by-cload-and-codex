package me.astropvp.astropvpskins.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.armorequip.ArmorEquipEvent;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.utils.ItemSkinUtils;
import me.ifamasssxd.cosmicduel.DuelAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemSkinListener implements Listener {

    private final Map<Player, UUID> cooldownPlayers;


    public ItemSkinListener() {
        this.cooldownPlayers = new HashMap<>();
    }



    @EventHandler
    private void onClick(InventoryClickEvent event) {
        ItemStack cursor = event.getCursor();
        ItemStack clicked = event.getCurrentItem();
        if (event.getClickedInventory() != event.getView().getBottomInventory())
            return;
        if (clicked == null || clicked.getType() == Material.AIR)
            return;
        if (AstroItemSkinsAPI.isItemSkin(cursor) && event.getClick().isLeftClick()) {
            List<SkinType> typeList = AstroItemSkinsAPI.getItemSkin(cursor);
            if (AstroItemSkinsAPI.hasItemSkin(clicked, cursor))
                return;
            if (typeList.get(0) == null || !AstroItemSkinsAPI.isCorrectType(typeList.get(0), clicked))
                return;
            if (cooldownPlayers.containsKey((Player) event.getWhoClicked())) {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c Please slow down when applying skins!"));
                return;
            }

            if (cursor.getAmount() > 1) {
                event.getWhoClicked().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot apply stacked itemskins!");
            }

            if (typeList.get(0).getItemSkinType() == ItemSkinType.BACKPACK && !AstroItemSkinsAPI.hasBackpackSocket(clicked)) {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!) &cYou need a Backpack socket to apply this backpack skin!"));
                event.setCancelled(true);
                return;
            }


            if (event.getSlotType() == InventoryType.SlotType.ARMOR)
                for (SkinType type : typeList)
                    type.getItemSkin().onEquip((Player) event.getWhoClicked());
            if (typeList.size() == 1) {
                if (typeList.get(0).getItemSkinType() == ItemSkinType.BACKPACK) {
                    event.setCurrentItem(typeList.get(0).applyBackpack(clicked));
                    event.setCurrentItem(AstroItemSkinsAPI.applyBackpackSkin(event.getCurrentItem(), typeList.get(0)));
                } else {

                    event.setCurrentItem(typeList.get(0).apply(clicked));
                }

            } else {
                event.setCurrentItem(typeList.get(0).apply(clicked, typeList.get(1)));
            }
            if (event.getCursor().getAmount() > 1) {
                event.getCursor().setAmount(event.getCursor().getAmount() - 1);
            } else {
                event.setCursor(null);
            }
            event.setCancelled(true);
            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BAT_TAKEOFF, 1.0F, 10.0F);
            cooldownPlayers.put((Player) event.getWhoClicked(), event.getWhoClicked().getUniqueId());

            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                cooldownPlayers.remove((Player) event.getWhoClicked());
            }, 10L);

        } else if (event.getClick().

                isRightClick() && AstroItemSkinsAPI.hasItemSkin(clicked) &&

                isArmorOrWeapon(clicked)) {

            List<SkinType> skinTypes = AstroItemSkinsAPI.getItemSkinByItem(clicked);
            assert skinTypes != null;

            if (cooldownPlayers.containsKey((Player) event.getWhoClicked())) {
                event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c Please slow down when applying skins!"));
                return;
            }

            for (int i = 0; i < skinTypes.size(); i++) {
                for (int j = 1; j < skinTypes.size(); j++) {
                    if (skinTypes.get(i).getItemSkinType() == skinTypes.get(j).getItemSkinType() && skinTypes.get(i) != skinTypes.get(j)) {

                        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                            skinTypes.get(i).getItemSkin().onUnequip((Player) event.getWhoClicked());
                            skinTypes.get(j).getItemSkin().onUnequip((Player) event.getWhoClicked());
                        }


                        if (event.getCursor().getType() != null && event.getCursor().getType() != Material.AIR) {
                            return;
                        }

                        event.setCurrentItem(AstroItemSkinsAPI.removeMultiSkinFromItem(clicked, skinTypes.get(i), skinTypes.get(j)));

                        event.setCursor(skinTypes.get(i).getMultiSkin(skinTypes.get(j)));
                        event.setCancelled(true);
                        ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BAT_TAKEOFF, 1.0F, 10.0F);
                        cooldownPlayers.put((Player) event.getWhoClicked(), event.getWhoClicked().getUniqueId());
                        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                            cooldownPlayers.remove((Player) event.getWhoClicked());
                        }, 10L);

                        return;
                    }
                }
            }

            if (event.getSlotType() == InventoryType.SlotType.ARMOR)
                skinTypes.get(0).getItemSkin().onUnequip((Player) event.getWhoClicked());

            if (event.getCursor().getType() != null && event.getCursor().getType() != Material.AIR) {
                return;
            }

            if (skinTypes.get(0).getItemSkinType() == ItemSkinType.BACKPACK) {
                event.setCurrentItem(removeBackpackSkin(clicked));
            }
            event.setCurrentItem(AstroItemSkinsAPI.removeItemSkinFromItem(clicked));
            event.setCursor(skinTypes.get(0).getItem());
            event.setCancelled(true);
            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BAT_TAKEOFF, 1.0F, 10.0F);
            cooldownPlayers.put((Player) event.getWhoClicked(), event.getWhoClicked().getUniqueId());
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroItemSkinsPlugin.getInstance(), () -> {
                cooldownPlayers.remove((Player) event.getWhoClicked());
            }, 10L);


        }


    }

    public static ItemStack removeBackpackSkin(final ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        final List<String> lore = meta.getLore();
        for (final String line : lore) {
            if (line.startsWith(CC.translate("&3&l+ BACKPACK ["))) {
                lore.set(lore.indexOf(line), CC.translate("&3&l+ BACKPACK [&8N/A&3&l]"));
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public boolean isArmorOrWeapon(ItemStack item) {
        String type = item.getType().name();
        return type.endsWith("_HELMET") || type.endsWith("_LEGGINGS") || type.endsWith("_CHESTPLATE") || type.endsWith("_BOOTS")
                || type.endsWith("_SWORD") || type.endsWith("AXE");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();
        if (event.isCancelled()) return;
        if (damager instanceof Player) {
            if (DuelAPI.isSpectator((Player) damager)) return;
            if (damaged instanceof Player) {
                if (!ItemSkinUtils.canPvP((Player) damager, (Player) damaged, event)) return;
            }
            if (damager.getWorld().getName().equals("world_outpost_pluto")) {
                return;
            }
            List<SkinType> damagerSkins = AstroItemSkinsAPI.getItemSkinsOnPlayer((Player) damager);
            for (SkinType skin : damagerSkins)
                skin.getItemSkin().onAttack((Player) damager, damaged, event);
            if (damaged instanceof Player) {
                List<SkinType> damagedSkins = AstroItemSkinsAPI.getItemSkinsOnPlayer((Player) damaged);
                for (SkinType skin : damagedSkins)
                    skin.getItemSkin().onAttacked((Player) damaged, damager, event);
            }
        }
    }

        @EventHandler
        public void onSneak (PlayerToggleSneakEvent event){
            if (event.isCancelled()) return;
            if (DuelAPI.isSpectator(event.getPlayer())) return;
            if (!WorldGuardUtils.isPvPEnabled(event.getPlayer().getLocation())) return;
            List<SkinType> damagedSkins = AstroItemSkinsAPI.getItemSkinsOnPlayer(event.getPlayer());
            if (event.getPlayer().getWorld().getName().equals("world_outpost_pluto")) {
                return;
            }
            for (SkinType skin : damagedSkins) {
                skin.getItemSkin().onSneak(event.getPlayer(), event);
            }


        }

        @EventHandler
        private void onInteract (PlayerInteractEvent event){
            ItemStack item = event.getPlayer().getItemInHand();
            if (item == null || item.getType() == Material.AIR)
                return;
            if (AstroItemSkinsAPI.isItemSkin(item)) {
                event.setCancelled(true);
                event.getPlayer().updateInventory();
            }
        }

        @EventHandler
        public void onJoin (PlayerJoinEvent event){
            AstroItemSkinsAPI.updateSkins(event.getPlayer(), true, null);
            GeneralUtil.healPlayer(event.getPlayer(), event.getPlayer().getMaxHealth());
        }

    @EventHandler
    public void onJoin (PlayerRespawnEvent event){
        AstroItemSkinsAPI.updateSkins(event.getPlayer(), true, null);
        GeneralUtil.healPlayer(event.getPlayer(), event.getPlayer().getMaxHealth());
    }

        @EventHandler
        public void onWorldChange (PlayerChangedWorldEvent event){
            AstroItemSkinsAPI.updateSkins(event.getPlayer(), true, null);
            GeneralUtil.healPlayer(event.getPlayer(), event.getPlayer().getMaxHealth());
        }

        @EventHandler
        public void onArmorChange (ArmorEquipEvent event){
            if (event.getPlayer().getWorld().getName().equals("world_outpost_pluto")) {
                return;
            }
            System.out.println(event.getMethod().name());
            if (event.getMethod() == ArmorEquipEvent.EquipMethod.HOTBAR) {
                return;
            }
            if (event.getOldArmorPiece() != null && event.getOldArmorPiece().getType() != Material.AIR && AstroItemSkinsAPI.hasItemSkin(event.getOldArmorPiece())) {
                List<SkinType> skins = AstroItemSkinsAPI.getItemSkinByItem(event.getOldArmorPiece());
                assert skins != null;
                for (SkinType skin : skins) {
                    System.out.println("just unequipped a " + skin.name());
                    skin.getItemSkin().onUnequip(event.getPlayer());
                }
            }
            if (event.getNewArmorPiece() != null && event.getNewArmorPiece().getType() != Material.AIR && AstroItemSkinsAPI.hasItemSkin(event.getNewArmorPiece())) {
                List<SkinType> skins = AstroItemSkinsAPI.getItemSkinByItem(event.getNewArmorPiece());
                assert skins != null;
                for (SkinType skin : skins) {
                    System.out.println("just equipped a " + skin.name());
                    skin.getItemSkin().onEquip(event.getPlayer());
                }
            }
        }

        @EventHandler
        public void onDeath (EntityDeathEvent event){
            LivingEntity killed = event.getEntity();
            Player player = killed.getKiller();
            if (player != null) {
                List<SkinType> killerSkins = AstroItemSkinsAPI.getItemSkinsOnPlayer(player);
                for (SkinType skin : killerSkins)
                    skin.getItemSkin().onKill(player, killed, event);
            }
            if (killed instanceof Player) {
                List<SkinType> damagedSkins = AstroItemSkinsAPI.getItemSkinsOnPlayer((Player) killed);
                for (SkinType skin : damagedSkins)
                    skin.getItemSkin().onKilled((Player) killed, player, event);
            }
        }
    }
