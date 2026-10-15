package net.crankedpvp.shards.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class MobcoinTransaction implements Serializable {
    private UUID buyer;
    private int price;
    private long time;
    private ItemStack item;
}
