package me.ifamasssxd.cosmicsky.inventorypets.managers;

import org.bukkit.Material;

public class NewBlockData {
   Material material;
   byte data;

   public NewBlockData(Material material, byte data) {
      this.material = material;
      this.data = data;
   }

   public Material getMaterial() {
      return this.material;
   }

   public byte getData() {
      return this.data;
   }
}
