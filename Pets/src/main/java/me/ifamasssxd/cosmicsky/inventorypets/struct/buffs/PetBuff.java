package me.ifamasssxd.cosmicsky.inventorypets.struct.buffs;

public abstract class PetBuff {
   private double multiplier;
   private long expiration;

   public boolean isExpired() {
      return this.expiration <= System.currentTimeMillis();
   }

   public PetBuff(double multiplier, long expiration) {
      this.multiplier = multiplier;
      this.expiration = expiration;
   }

   public double getMultiplier() {
      return this.multiplier;
   }

   public long getExpiration() {
      return this.expiration;
   }
}
