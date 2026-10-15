package net.astropvp.loottable.tasks;

import net.astropvp.loottable.struct.ECO_UPDATE;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AutomaticEcoUpdateTask extends BukkitRunnable {

    private final Map<ECO_UPDATE, Long> timesToUpdateEco = new HashMap<ECO_UPDATE, Long>() {{
        put(ECO_UPDATE.SOTW, Date.UTC(2024, 10, 18, 9, 0, 0));
        put(ECO_UPDATE.FIRST_TUESDAY, Date.UTC(2024, 10, 22, 21, 0, 0));
        put(ECO_UPDATE.FIRST_FRIDAY, Date.UTC(2024, 10, 25, 21, 0, 0));
        put(ECO_UPDATE.SECOND_TUESDAY, Date.UTC(2024, 10, 29, 21, 0, 0));
    }};

    @Override
    public void run() {

    }

}
