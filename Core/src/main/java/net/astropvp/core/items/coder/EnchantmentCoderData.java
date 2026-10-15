package net.astropvp.core.items.coder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.astropvp.storage.utils.KeyToSave;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class EnchantmentCoderData implements KeyToSave {
    private UUID uuid;
    private Map<Material, List<String>> typeEnchants;

    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public void setKey(String s) {
        this.uuid = UUID.fromString(s);
    }
}
