package net.astropvp.core.listeners;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import net.astropvp.core.AstroCore;
import net.astropvp.core.items.coder.EnchantmentCoderInventory;
import net.astropvp.core.items.coder.EnchantmentCoderManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnchantmentCoderItemListener implements Listener {
    private final List<CustomEnchantment> helmetEnchants = new ArrayList<>();
    private final List<CustomEnchantment> chestplateEnchants= new ArrayList<>();
    private final List<CustomEnchantment> leggingsEnchants= new ArrayList<>();
    private final List<CustomEnchantment> bootsEnchants= new ArrayList<>();
    private final List<CustomEnchantment> swordEnchants= new ArrayList<>();
    private final List<CustomEnchantment> axeEnchants= new ArrayList<>();

    public EnchantmentCoderItemListener() {
        addHelmetEnchants();
        addChestplateEnchants();
        addLeggingsEnchants();
        addBootsEnchants();
        addSwordEnchants();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        ItemStack item = event.getItem();
        if (!isItem(item))
            return;
        Player player = event.getPlayer();
        if (item.getAmount() > 1) {
            player.sendMessage(CC.translate("&c&l(!) &cYou cannot open stacked Heroic Enchantment Coders."));
            return;
        }
        player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 0.5F, 0.5F);
        player.openInventory(EnchantmentCoderInventory.getInstance().getInventory());
    }

    private boolean isItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return false;
        NBTWrapper wrapper = new NBTWrapper(item);
        if (!wrapper.hasKey("cosmicData"))
            return false;
        NBTTagCompoundBuilder tag = new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData"));
        return (tag.hasKey("enchantmentCoder") && tag.getBoolean("enchantmentCoder"));
    }


    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
        UUID uuid = event.getPlayer().getUniqueId();
        EnchantmentCoderManager manager = AstroCore.getInstance().getEnchantmentCoderManager();
        manager.add(uuid, Material.LEATHER_HELMET, helmetEnchants);
        manager.add(uuid, Material.LEATHER_CHESTPLATE, chestplateEnchants);
        manager.add(uuid, Material.LEATHER_LEGGINGS, leggingsEnchants);
        manager.add(uuid, Material.LEATHER_BOOTS, bootsEnchants);
        manager.add(uuid, Material.GOLD_SWORD, swordEnchants);
        }
    }

    public void addHelmetEnchants() {
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("ender shift"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("frozen"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("resilience"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("smoke bomb"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("dodge"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("angelic"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("drunk"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("death god"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("martyr valor"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("alien implants"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("paladin armored"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("reinforced tank"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("planetary deathbringer"));
        this.helmetEnchants.add(EnchantmentAPI.getEnchantment("divine enlighted"));
    }

    public void addChestplateEnchants() {
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("curse"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("spirit link"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("angelic"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("blood lust"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("holy aegis"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("infinite luck"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("martyr valor"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("godly overload"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("divine enlighted"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("paladin armored"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("reinforced tank"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("vengeful diminish"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("heroic enchant reflect"));
        this.chestplateEnchants.add(EnchantmentAPI.getEnchantment("planetary deathbringer"));
    }

    public void addLeggingsEnchants() {

        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("self destruct"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("voodoo"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("angelic"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("obsidianshield"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("clarity"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("leadership"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("martyr valor"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("mighty cactus"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("divine enlighted"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("paladin armored"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("reinforced tank"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("epidemic carrier"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("heroic enchant reflect"));
        this.leggingsEnchants.add(EnchantmentAPI.getEnchantment("planetary deathbringer"));
    }

    public void addBootsEnchants() {

        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("undead ruse"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("angelic"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("ender walker"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("gears"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("phoenix"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("ghostly ghost"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("martyr valor"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("ethereal dodge"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("divine enlighted"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("paladin armored"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("reinforced tank"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("guided rocket escape"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("polymorphic metaphysical"));
        this.bootsEnchants.add(EnchantmentAPI.getEnchantment("planetary deathbringer"));

    }

    public void addSwordEnchants() {

        this.swordEnchants.add(EnchantmentAPI.getEnchantment("blackout"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("neutralize"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("reflective block"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("demonic lifesteal"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("shadow assassin"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("titan trap"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("perfect solitude"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("permanent execute"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("bloody deep wounds"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("divine immolation"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("hero killer"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("rage"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("silence"));
        this.swordEnchants.add(EnchantmentAPI.getEnchantment("iceaspect"));


    }

}
