public class BananaDura {
    public static void main(String[] a) {
        org.bukkit.inventory.ItemStack it = new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_HELMET);
        it.setDurability((short) 10);
        org.bukkit.inventory.ItemStack out = com.cosmicpvp.cosmicutils.utils.ItemUtils.subtractDurability(it, 3);
        System.out.println("after subtract 3: " + (out == null ? "null" : out.getDurability()));
    }
}
