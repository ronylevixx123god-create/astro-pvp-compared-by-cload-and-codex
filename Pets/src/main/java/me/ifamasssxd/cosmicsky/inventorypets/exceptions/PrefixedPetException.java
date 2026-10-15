package me.ifamasssxd.cosmicsky.inventorypets.exceptions;

import com.cosmicpvp.cosmicutils.utils.CC;

public class PrefixedPetException extends PetException {
   public PrefixedPetException(String msg) {
      super(CC.translate( "&c&lPET: " + CC.Red + msg));
   }
}
