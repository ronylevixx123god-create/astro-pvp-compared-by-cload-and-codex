import org.arkhamnetwork.Arkkit.libs.SpawnerCompat;
import org.bukkit.block.CreatureSpawner;
import java.lang.reflect.Proxy;

public class CompatTest {
    public static void main(String[] a) throws Exception {
        CreatureSpawner cs = (CreatureSpawner) Proxy.newProxyInstance(CompatTest.class.getClassLoader(),
            new Class[]{CreatureSpawner.class}, (p, m, args) -> {
                switch (m.getName()) {
                    case "getSpawnerAmount": return 7;
                    case "getSpawnerCount": return m.getReturnType() == short.class ? (Object) (short) 5 : (Object) 5;
                    default: throw new UnsupportedOperationException(m.getName());
                }
            });
        System.out.println("spawnerStack=" + SpawnerCompat.getStackSize(cs));
        Class<?> cis = Class.forName("org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack");
        boolean has = false;
        try { cis.getMethod("modifyDurability", org.bukkit.inventory.ItemStack.class, int.class); has = true; } catch (NoSuchMethodException e) {}
        System.out.println("serverHasModifyDurability=" + has);
        if (!has) {
            org.bukkit.inventory.ItemStack it = new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_HELMET);
            it.setDurability((short) 10);
            org.bukkit.inventory.ItemStack out = com.cosmicpvp.cosmicutils.utils.ItemUtils.subtractDurability(it, 5);
            org.bukkit.inventory.ItemStack out2 = com.cosmicpvp.cosmicutils.utils.ItemUtils.addDurability(it, (short) 5);
            System.out.println("unchanged=" + (out == it && out2 == it && it.getDurability() == 10));
        }
    }
}
