package com.astropvp.cc.packet.type.out;

import com.astropvp.cc.packet.type.Packet;
import com.google.gson.annotations.SerializedName;

public class SSetEntityProperties implements Packet {


    public static class EntityProperties {
        private final double scale;
        private final boolean boss;
        private final boolean spooky;
        private final boolean showNametags;
        private final String customNametag;
        private final int golemType;
        private final int type;
        private final boolean variant;
        private final String key;

        public EntityProperties(double scale, boolean boss, boolean spooky, boolean showNametags, String customNametag, int golemType, int type, boolean variant, String key) {
            this.scale = scale;
            this.boss = boss;
            this.spooky = spooky;
            this.showNametags = showNametags;
            this.customNametag = customNametag;
            this.golemType = golemType;
            this.type = type;
            this.variant = variant;
            this.key = key;
        }
    }

    private final int entityID;
    private final EntityProperties properties;
    private final int protocol = 0;

    public SSetEntityProperties(int entityID, EntityProperties entityProperties) {
        this.entityID = entityID;
        this.properties = entityProperties;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public EntityProperties getProperties() {
        return this.properties;
    }

    @Override
    public short getID() {
        return 61;
    }

}