package com.compilex.CosmicKOTH.tasks;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.compilex.CosmicKOTH.tasks.LauncherTask;
import com.compilex.CosmicKOTH.utils.TimeUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DateWatcherTask
        implements Runnable {
    boolean ranThisSession = false;
    public static List<Calendar> events;
    private Calendar mondayEvent1;
    private Calendar mondayEvent2;
    private Calendar mondayEvent3;
    private Calendar mondayEvent4;
    private Calendar mondayEvent5;
    private Calendar mondayEvent6;
    private Calendar mondayEvent7;
    private Calendar mondayEvent8;
    private Calendar mondayEvent9;
    private Calendar mondayEvent10;
    private Calendar mondayEvent11;
    private Calendar mondayEvent12;

    public DateWatcherTask() {
        events = new ArrayList<Calendar>();
        this.mondayEvent1 = Calendar.getInstance();
        this.mondayEvent1.set(11, 0);
        this.mondayEvent2 = Calendar.getInstance();
        this.mondayEvent2.set(11, 2);
        this.mondayEvent3 = Calendar.getInstance();
        this.mondayEvent3.set(11, 4);
        this.mondayEvent4 = Calendar.getInstance();
        this.mondayEvent4.set(11, 6);
        this.mondayEvent5 = Calendar.getInstance();
        this.mondayEvent5.set(11, 8);
        this.mondayEvent6 = Calendar.getInstance();
        this.mondayEvent6.set(11, 10);
        this.mondayEvent7 = Calendar.getInstance();
        this.mondayEvent7.set(11, 12);
        this.mondayEvent8 = Calendar.getInstance();
        this.mondayEvent8.set(11, 14);
        this.mondayEvent9 = Calendar.getInstance();
        this.mondayEvent9.set(11, 16);
        this.mondayEvent10 = Calendar.getInstance();
        this.mondayEvent10.set(11, 18);
        this.mondayEvent11 = Calendar.getInstance();
        this.mondayEvent11.set(11, 20);
        this.mondayEvent12 = Calendar.getInstance();
        this.mondayEvent12.set(11, 22);
        events.add(this.mondayEvent1);
        events.add(this.mondayEvent2);
        events.add(this.mondayEvent3);
        events.add(this.mondayEvent4);
        events.add(this.mondayEvent5);
        events.add(this.mondayEvent6);
        events.add(this.mondayEvent7);
        events.add(this.mondayEvent8);
        events.add(this.mondayEvent9);
        events.add(this.mondayEvent10);
        events.add(this.mondayEvent11);
        events.add(this.mondayEvent12);
    }

    @Override
    public void run() {
        if (this.ranThisSession) {
            return;
        }

        Calendar c = Calendar.getInstance();
        for (Calendar event : events) {
            if (c.get(7) != event.get(7) || c.get(11) != event.get(11)) continue;
            //Bukkit.getScheduler().runTaskTimer((Plugin)CosmicKOTHPlugin.get_(), (Runnable)new LauncherTask(901), 20L, 20L);
            this.ranThisSession = true;
        }
    }

    public static long getMilliUntilNextKOTH() {
        long nearestEvent = System.currentTimeMillis();
        for (Calendar event : events) {
            Calendar dayInMonth = DateWatcherTask.nextDayOfWeek(event.get(7));
            dayInMonth.set(11, event.get(11));
            dayInMonth.set(12, 15);
            dayInMonth.set(13, 0);
            long msUntilEvent = dayInMonth.getTimeInMillis() - System.currentTimeMillis();
            if (msUntilEvent >= nearestEvent || msUntilEvent <= 0L) continue;
            nearestEvent = msUntilEvent;
        }
        return nearestEvent;
    }

    public static String getTimeUntilNextKOTH() {
        long l = DateWatcherTask.getMilliUntilNextKOTH() / 1000L;
        return TimeUtils.getSecondsAsString(l);
    }

    private static Calendar nextDayOfWeek(int dow) {
        Calendar date = Calendar.getInstance();
        int diff = dow - date.get(7);
        if (diff <= 0 && diff != 0) {
            diff += 7;
        }
        date.add(5, diff);
        return date;
    }
}
