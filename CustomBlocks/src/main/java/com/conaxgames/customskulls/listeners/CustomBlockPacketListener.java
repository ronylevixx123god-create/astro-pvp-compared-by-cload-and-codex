package com.conaxgames.customskulls.listeners;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.CustomBlock;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutTileEntityData;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public class CustomBlockPacketListener extends PacketAdapter {
    private Field tagField;
    private Field posField;

    public CustomBlockPacketListener() {
        super(CoreSkulls.instance, ListenerPriority.HIGHEST, PacketType.Play.Server.TILE_ENTITY_DATA, PacketType.Play.Server.MAP_CHUNK);

        try{
            tagField = PacketPlayOutTileEntityData.class.getDeclaredField("c");
            tagField.setAccessible(true);

            posField = PacketPlayOutTileEntityData.class.getDeclaredField("a");
            posField.setAccessible(true);
        }catch(NoSuchFieldException exc)
        {
            exc.printStackTrace();
        }

        Bukkit.getLogger().info(String.format("[%s] - Registered packet me.astropvp.outpost.listener!", CoreSkulls.instance.getName()));
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        try {
            PacketPlayOutTileEntityData data = (PacketPlayOutTileEntityData) event.getPacket().getHandle();
            //Nothing we can do.
            if(tagField == null)
                return;

            NBTTagCompound compound;
            BlockPosition pos;
            try{
                compound = (NBTTagCompound) tagField.get(data);
                pos = (BlockPosition) posField.get(data);
            }catch(IllegalAccessException exc)
            {
                exc.printStackTrace();
                return;
            }

            //Not a noteblock.
            if(!compound.hasKey("note"))
                return;

            AbilityData abilityData = AbilityData.getAbilityDataMap().get(AbilityData.getUUID(pos.getX(), pos.getY(), pos.getZ()));

            if(abilityData == null)
                return;

            CustomBlock customBlock = CoreSkulls.customBlocks.get(abilityData.getAbilityType().getName());

            if(customBlock == null)
                return;

            NBTTagCompound insertTag = new NBTTagCompound();
            NBTTagCompound customData = new NBTTagCompound();
            insertTag.set("customBlockData", customData);

            customData.setString("type", customBlock.getIdentifier());
            customData.setString("facing", EnumDirection.fromAngle(event.getPlayer().getLocation().getYaw()).opposite().name());

            compound.set("tag", insertTag);
        } catch (ClassCastException ignored) {

        }


    }
}