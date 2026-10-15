package com.astropvp.cc.packet;

import com.astropvp.cc.packet.type.Packet;
import com.astropvp.cc.packet.type.in.*;
import com.astropvp.cc.packet.type.out.*;
import com.astropvp.cc.Debug;
import com.astropvp.cc.packet.type.*;
import com.astropvp.cc.packet.type.in.*;
import com.astropvp.cc.packet.type.out.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class PacketCommons {
    public static final boolean DEBUG = false;

    public static final String CHANNEL_NAME = "cosmic";

    public static final String SESSIONKEY = "13c9esdEOtLC+JPE+GaWFcF86+MqlLoLaY6yRedTwUp3Fx1NFWDwPnBYjCb3Y83yPV8rujPeaCkARIabBx5Ca+J7FstlvlUN2+j5WpCSFUFDvqjw2KG0k6/WoABk1aHQi0n/sypTUB6ZkG2JnyuaNTQh2ebhc5yMIdeAViwIcyW4t3I5p7+VYjcq9YBX4CfKRJjSZaVyJIFBHouCndqlNicc4PvKcmTnYwj66h6rI4t5Iq97ticrbvXdZifre9IGcVLVhOIpCtiXoLBA06t6beLFhOF8OatYVu86CXvT0TBUaidS2NO+l7Qqk4m9k2fDENna2zpOe237XLBm7uKviA==";

    public static final BiMap<Short, Class<? extends Packet>> packetIDMap = HashBiMap.create();

    public static void bootstrap(){
        packetIDMap.clear();

        packetIDMap.put((short) 33, PacketOutSessionKey.class);
        packetIDMap.put((short) 30, PacketOutFeatureSet.class);
        packetIDMap.put((short) 44, PacketOutSetWorldName.class);
        packetIDMap.put((short) 43, PacketOutTeamVoiceStatus.class);
        packetIDMap.put((short) 46, PacketOutTimeResponse.class);
        packetIDMap.put((short) 36, PacketOutPingAllies.class);
        packetIDMap.put((short) 25, PacketOutAllowedEmotes.class);
        packetIDMap.put((short) 47, PacketOutVoice.class);
        packetIDMap.put((short) 35, PacketOutSetPermission.class);
        packetIDMap.put((short) 34, PacketOutNotification.class);
        packetIDMap.put((short) 45, PacketOutStartEmote.class);
        packetIDMap.put((short) 49, PacketOutHitSplat.class);
        packetIDMap.put((short) 38, PacketOutPlayerHeaders.class);
        packetIDMap.put((short) 31, PacketOutFocusedPlayer.class);
        packetIDMap.put((short) 69, PacketOutPurchaseFeed.class);
        packetIDMap.put((short) 70, PacketOutTooltip.class);
        packetIDMap.put((short) 71, PacketOutClearTooltip.class);
        packetIDMap.put((short) 32, SItemCooldownsListPacket.class);
        packetIDMap.put((short) 20, PacketOutChunkDataList.class);
        packetIDMap.put((short) 72, PacketOutHealthBar.class);
        packetIDMap.put((short) 61, SSetEntityProperties.class);

        packetIDMap.put((short) 11, PacketInRequestPermission.class);
        packetIDMap.put((short) 7, PacketInHandshake.class);
        packetIDMap.put((short) 51, PacketInWorldNameRequest.class);
        packetIDMap.put((short) 17, PacketInTimeRequest.class);
        packetIDMap.put((short) 9, PacketInSetClientStatus.class);
        packetIDMap.put((short) 12, PacketInPingAllies.class);
        packetIDMap.put((short) 15, PacketInSetChannel.class);
        packetIDMap.put((short) 16, PacketInStartEmote.class);
        packetIDMap.put((short) 18, PacketInVoice.class);
        packetIDMap.put((short) 1, PacketInChunkDataRequest.class);
        packetIDMap.put((short) 2, PacketInChunkClaim.class);
    }

    public static short getPacketID(Packet packet) {
        try {
            return packetIDMap.inverse().get(packet.getClass());
        } catch (Exception exception) {
            Debug.log("Didn't recognize: " + packet.getClass().getName() + "!");
            throw exception;
        }
    }

    public static short getPacketID(Class<?> var1) {
        return packetIDMap.inverse().get(var1);
    }

    public static Class<? extends Packet> getPacketClass(short var1) {
        return packetIDMap.get(var1);
    }
}
