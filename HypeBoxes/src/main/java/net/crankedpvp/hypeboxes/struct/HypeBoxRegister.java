package net.crankedpvp.hypeboxes.struct;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.Getter;
import net.crankedpvp.hypeboxes.HypeboxesPlugin;
import org.apache.commons.lang.Validate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class HypeBoxRegister {
    private final HypeboxesPlugin plugin;

    @Getter
    private final Map<String, HypeBox> lootboxes;

    public HypeBoxRegister(HypeboxesPlugin plugin) {
        this.lootboxes = Maps.newConcurrentMap();
        this.plugin = plugin;
    }

    public Map<String, HypeBox> getSortedHypeboxes() {
        return sort(this.lootboxes);
    }


    public Map<String, HypeBox> sort(Map<String, HypeBox> map) {
        List<Map.Entry<String, HypeBox>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByKey());
        Collections.reverse(list);
        Map<String, HypeBox> result = new LinkedHashMap<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map.Entry<String, HypeBox> entry = list.get(i);
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public void register(HypeBox hypebox) {
        try {
            this.lootboxes.put(hypebox.getId(), hypebox);
            this.plugin.getLogger().info("Successfully registered hypebox: " + hypebox.getId());
        } catch (Exception var3) {
            System.err.println("&8(&c!&8) Failed to register hypebox, " + hypebox.getId());
        }
    }

    public boolean isHypebox(String name) {
        return (getHypebox(name) != null);
    }

    public HypeBox getHypebox(String name) {
        Validate.notNull(name);
        Validate.notEmpty(name);
        return this.lootboxes.get(name);
    }

    public void clearHypeboxMap() {
        this.lootboxes.clear();
    }

    public void removeHypebox(String lootbox) {
        this.lootboxes.remove(lootbox);
    }

    public Collection<HypeBox> getHypeboxObjects() {
        return Collections.unmodifiableCollection(this.lootboxes.values());
    }

    public List<HypeBox> getHypeboxesAsList() {
        return new ArrayList<>(this.lootboxes.values());
    }

    public List<String> getHypeboxesAsIDs() {
        return getHypeboxesAsList().stream()
                .map(HypeBox::getId)
                .collect(Collectors.toList());
    }

    public HypeBox select(Collection<HypeBox> lootboxCollection) {
        return (lootboxCollection.size() > 0) ? ImmutableList.copyOf(lootboxCollection).get(ThreadLocalRandom.current().nextInt(lootboxCollection.size())) : null;
    }


}
