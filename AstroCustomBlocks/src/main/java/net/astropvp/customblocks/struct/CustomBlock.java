package net.astropvp.customblocks.struct;

import com.cosmicpvp.cosmicutils.utils.LocationUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.astropvp.storage.utils.KeyToSave;
import org.bukkit.Location;
import org.bukkit.entity.Player;


@Getter
@RequiredArgsConstructor
@Setter
public abstract class CustomBlock implements KeyToSave {
    private Location location;
    private final CustomBlockType type;

    @Override
    public String getKey() {
        return LocationUtils.getStringFromLocation(location);
    }

    @Override
    public void setKey(String key) {
        this.location = LocationUtils.getLocationFromString(key, false);
    }

    public void onClick(Player clicker) {

    }


}
