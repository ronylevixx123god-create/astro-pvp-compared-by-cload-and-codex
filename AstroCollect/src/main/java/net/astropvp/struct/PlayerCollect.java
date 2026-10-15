package net.astropvp.struct;

import lombok.Getter;
import net.astropvp.storage.utils.KeyToSave;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PlayerCollect implements KeyToSave {

    private UUID uuid;
    private final List<CollectCategory> categories;

    public PlayerCollect(UUID uuid) {
        this.uuid = uuid;
        categories = new ArrayList<>();
    }

    public void add(CollectCategory category) {
        this.categories.add(category);
    }

    public void add(List<CollectCategory> categories) {
        this.categories.addAll(categories);
    }

    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public void setKey(String key) {
        this.uuid = UUID.fromString(key);
    }
}