package me.ifamasssxd.cosmicsky.inventorypets.struct;

import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.astropvp.astropvpskins.tasks.UndertowFieldTask;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PrefixedPetException;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicPersistentItem;
import me.ifamasssxd.cosmicskycustoms.struct.PlayerInteractable;
import me.kingsavaqe.cagepvp.AstroCagePvP;
import net.minecraft.server.v1_8_R3.GameProfileSerializer;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class InventoryPetItem extends CosmicPersistentItem implements PlayerInteractable {
    private InventoryPets petType;
    private long lastUsed;
    private int level;
    private boolean leashed;

    public InventoryPetItem(final String internalName) {
        super(internalName);
    }

    public InventoryPetItem() {
        this("inventory_pet");
    }

    public InventoryPetItem(final ItemStack stack, final NBTWrapper wrapper, final String internal) {
        super(stack, wrapper, internal);
        this.loadNBT();
    }

    public NBTWrapper getWrapper() {
        return this.wrapper;
    }

    public void loadNBT() {
        if (this.wrapper != null) {
            NBTTagCompoundBuilder builder = this.wrapper.getCompound("cosmicData");
            if (builder != null && builder.hasKey("petType")) {
                this.petType = InventoryPets.fromName(builder.getString("petType"));
                this.level = 5;
                this.lastUsed = builder.getLong("lastUsed");
                this.leashed = builder.getBoolean("leashed");
            }
        }

        if (this.petType != null) {
            this.petType.getInternalClass().onItemLoad(this.wrapper, this);
        }

    }


    public ItemStack build() {
        return this.createItem(null, this.petType, this.lastUsed);
    }


    public ItemStack createItem(final Player player, final Object... objects) {
        if (objects == null || objects.length < 1) {
            return null;
        }
        final InventoryPets pet = (InventoryPets)((objects[0] instanceof InventoryPets) ? objects[0] : InventoryPets.fromName(objects[0].toString()));
        Number lastUsed = (Number)((objects.length > 1) ? ((objects[1] instanceof Number) ? objects[1] : NumberUtils.parseNumberOrNull(objects[1].toString())) : null);
        if (lastUsed == null || lastUsed.longValue() < 0L) {
            lastUsed = 0;
        }
        final List<String> lore = pet.getInternalClass().getPetDescription();
        NBTTagCompoundBuilder builder = pet.getBuilder();
        if (pet.getRedSkinBlob() != null) {
            final long delay = TimeUnit.SECONDS.toMillis(pet.getInternalClass().getCooldown());
            final long timeTillCanUse = delay - (System.currentTimeMillis() - this.lastUsed);
            final boolean isOnCooldown = this.lastUsed > 0L && timeTillCanUse > 0L;
            final GameProfile profile = new GameProfile(UUID.randomUUID(), pet.name().toLowerCase());
            profile.getProperties().put("textures", new Property("textures", isOnCooldown ? pet.getRedSkinBlob() : pet.getSkinBlob()));
            builder = new NBTTagCompoundBuilder();
            GameProfileSerializer.serialize(builder.get(), profile);
        }
        if (this.leashed) {
            lore.add(ChatColor.GOLD + ChatColor.BOLD.toString() + "LEASHED");
        }
        final NBTTagCompoundBuilder base = new NBTTagCompoundBuilder().setString("petType", pet.name()).setLong("lastUsed", lastUsed.longValue()).setBoolean("leashed", this.leashed);
        final NBTWrapper tempWrapper = this.applyItemData(new ItemBuilder(Material.SKULL_ITEM, String.valueOf(pet.getItemName())).lore(lore).data((short)3).build()).setString("cosmicType", this.getInternalName()).setNBTBase("cosmicData", base.get()).setNBTBase("SkullOwner", builder.get());

        return pet.getInternalClass().onItemCreate(tempWrapper, this).assignIID().build();
    }


    @SneakyThrows
    public void onPlayerInteractable(Player player, ItemStack itemStack, NBTWrapper nbtWrapper, PlayerInteractEvent playerInteractEvent) throws CosmicException {
        playerInteractEvent.setCancelled(true);

        final InventoryPetItem item = (InventoryPetItem)CosmicPersistentItem.constructItem(itemStack, nbtWrapper, (Class)InventoryPetItem.class);

        if (item != null) {
            InventoryPets type = item.getPetType();

            if (type == null) {
                throw new PrefixedException("Corrupted Inventory Pet found?");
            }



            if (player.hasMetadata("stitchesPet") && player.hasMetadata("stitchesPetTime")) {
                final long delay = player.getMetadata("stitchesPetTime").get(0).asLong();
                final long timeTillCanUse = delay - (System.currentTimeMillis() - player.getMetadata("stitchesPet").get(0).asLong());
                if (timeTillCanUse > 0) {
                    player.sendMessage(CC.translate("&c&lPET: &cYour pets are silenced by a player's stitches pet for: " + (int) (timeTillCanUse / 1000) + "s"));
                    return;
                }
            }

            if (player.hasMetadata("transEffect")) {
                player.sendMessage(CC.translate("&c&lPET: &cYour pets are silenced by a player's Transcendence Effect."));
                return;
            }



            if (UndertowFieldTask.affected.contains(player)) {
                player.sendMessage(CC.translate("&c&lPET: &cYour pets are silenced by a player's UnderTow Field."));
                return;
            }

            if (AstroCagePvP.getInstance().getImmuneManager().hasImmune(player)) {
                player.sendMessage(CC.translate("&c&lPET: &cYou cannot use pets while you are in immunity."));
                return;
            }

            if (AstroCagePvP.getInstance().getWildPvPManager().hasWildPvP(player) && AstroCagePvP.getInstance().getWildPvPManager().getWildPvP(player).isGrace()) {
                player.sendMessage(CC.translate("&c&lPET: &cYou cannot use pets before the match has started."));
                return;
            }

            if (player.hasMetadata("hazmatBoots")) {
                long lastUse = player.getMetadata("hazmatBoots").get(0).asLong();
                long delay = TimeUnit.SECONDS.toMillis(12);
                long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);
                if (timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking())) {
                    throw new NonPrefixedPetException(CC.translate("&cYour pets are currently silenced by a player's hazmat boots for " + TimeUtils.formatSeconds(TimeUnit.MILLISECONDS.toSeconds(timeTillCanUse))));
                } else {
                    player.removeMetadata("hazmatBoots", CosmicSkyInventoryPets.get());
                }
            }
            if (player.hasMetadata("spectator")) {
                throw new PrefixedException(CC.Red + "You cannot use that ability while spectating!");
            }


            if (!WorldGuardUtils.isPvPEnabled(player.getLocation())) {
                playerInteractEvent.setCancelled(true);
                throw new PrefixedPetException(CC.Red + "You cannot use that pet here (Safezone)!");
            }

            if (player.getLocation().getWorld().getName().equals("nebula")) {
                playerInteractEvent.setCancelled(true);
                throw new PrefixedPetException(CC.Red + "You cannot use that pet here (Safezone)!");
            }

            if (player.getLocation().getWorld().getName().equalsIgnoreCase("world_arcade")) {
                playerInteractEvent.setCancelled(true);
                throw new PrefixedPetException(CC.Red + "You cannot use pets at arcade!");
            }

            if (player.getLocation().getWorld().getName().equalsIgnoreCase("tournament_world")) {
                playerInteractEvent.setCancelled(true);
                throw new PrefixedPetException(CC.Red + "You cannot use pets in tournaments!");
            }


            if (player.getItemInHand().getAmount() > 1) {
                playerInteractEvent.setCancelled(true);
                throw new PrefixedPetException(CC.Red + "You cannot use stacked pets!");
            }

            if (CooldownUtils.isCooldownActive(player, type.name())) {
                String cd =  CooldownUtils.getFormattedCooldown(player, type.name());
                player.sendMessage(CC.translate("&c&lPET: &cYour " + type.getItemName() + " &cis still on cooldown for &7" + cd + "&c!"));
                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
                return;
            }

            if (type.getInternalClass().onPetItemInteract(player, itemStack, item, playerInteractEvent)) {
                player.getInventory().setItemInHand(item.build());
                player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 0.25f);
                player.sendMessage(CC.translate("&a&lPET: &aYour " + type.getItemName() + " &ais now on a &7" + type.getInternalClass().getCooldown()/60 + "m &acooldown!"));
                player.updateInventory();
            }
        }

    }


}
