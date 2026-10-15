package net.astropvp.customblocks.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.astropvp.customblocks.CustomBlocks;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutTileEntityData;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Field;

public class CustomBlockPacketListener extends PacketAdapter {
    private Field tagField;
    private Field posField;

    public CustomBlockPacketListener() {
        super(CustomBlocks.instance, ListenerPriority.HIGHEST, PacketType.Play.Server.TILE_ENTITY_DATA, PacketType.Play.Server.MAP_CHUNK);

        try {
            tagField = PacketPlayOutTileEntityData.class.getDeclaredField("c");
            tagField.setAccessible(true);

            posField = PacketPlayOutTileEntityData.class.getDeclaredField("a");
            posField.setAccessible(true);
        } catch (NoSuchFieldException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        try {
            PacketPlayOutTileEntityData data = (PacketPlayOutTileEntityData) event.getPacket().getHandle();
            if (tagField == null)
                return;

            NBTTagCompound compound;
            BlockPosition pos;
            try {
                compound = (NBTTagCompound) tagField.get(data);
                pos = (BlockPosition) posField.get(data);
            } catch (IllegalAccessException exc) {
                exc.printStackTrace();
                return;
            }

            if (!compound.hasKey("note"))
                return;

            CustomBlock customBlockData = CustomBlocks.getInstance().getCustomBlockManager().getCustomBlockData(new Location(event.getPlayer().getWorld(), pos.getX(), pos.getY(), pos.getZ()));

            if (customBlockData == null)
                return;


            NBTTagCompound insertTag = new NBTTagCompound();
            NBTTagCompound customData = new NBTTagCompound();
            insertTag.set("customBlockData", customData);

            customData.setString("type", customBlockData.getType().name());
            customData.setString("facing", EnumDirection.fromAngle(event.getPlayer().getLocation().getYaw()).opposite().name());

            compound.set("tag", insertTag);
        } catch (ClassCastException ignored) {

        }


    }
}
