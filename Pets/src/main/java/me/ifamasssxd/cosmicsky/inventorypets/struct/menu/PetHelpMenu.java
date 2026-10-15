package me.ifamasssxd.cosmicsky.inventorypets.struct.menu;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import org.bukkit.entity.Player;

public class PetHelpMenu extends CosmicGUI {
   public PetHelpMenu(Player player) {
      super(player, "Pet Help", 45);
   }

   public void redraw() {
      int index = 0;
      InventoryPets[] var2 = InventoryPets.values();
      InventoryPets[] array = var2;
      int length = var2.length;

      for(int i = 0; i < length; ++i) {
         InventoryPets pet = array[i];
         this.setItem(index++, new InventoryItem(CosmicSkyInventoryPets.get().getPetItem().createItem(null, pet)));
      }

   }
}
