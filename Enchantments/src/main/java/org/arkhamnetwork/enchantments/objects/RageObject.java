package org.arkhamnetwork.enchantments.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
@AllArgsConstructor
public class RageObject {

    private int count;
    private long lastHitTime;
    private Player victim;
}