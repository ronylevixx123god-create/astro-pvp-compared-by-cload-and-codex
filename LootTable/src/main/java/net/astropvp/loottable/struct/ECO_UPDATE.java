package net.astropvp.loottable.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
public enum ECO_UPDATE {
    SOTW(0, "SOTW Loot"),
    FIRST_TUESDAY(0.5, "Tuesday after SOTW"),
    FIRST_FRIDAY(1, "First Friday"),
    SECOND_TUESDAY(1.5, "Second Week Tuesday"),
    SECOND_FRIDAY(2, "Second Week Friday"),
    THIRD_TUESDAY(2.5, "Third Week Tuesday"),
    THIRD_FRIDAY(3, "Last Eco Update"),
    CARNAGE(3.5, "Carnage Eco"),
    ;

    private final double week;
    private final String fancy;
}
