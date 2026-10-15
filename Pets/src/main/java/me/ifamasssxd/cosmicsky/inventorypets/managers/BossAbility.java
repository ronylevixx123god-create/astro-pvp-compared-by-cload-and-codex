package me.ifamasssxd.cosmicsky.inventorypets.managers;

public abstract class BossAbility {
   protected BlockModifier blockModifier;

   public BossAbility(BossAbility boss, BlockModifier modifier) {
      this.blockModifier = modifier;
   }

   public abstract void onAbility();

   public abstract long getCooldown();

   public abstract String getAbilityName();

   public BlockModifier getBlockModifier() {
      return this.blockModifier;
   }
}
