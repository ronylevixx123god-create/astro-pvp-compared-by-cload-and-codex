/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.discord;

public enum CosmicDiscordChannels {
    ADMIN_LOGS(new DiscordChannel("PvP Admin Bot", "https://i.gyazo.com/111aeaee30e78af4e88f871739f2eb67.png", "XkLZ51Qy-g-hujJoj_h4ThdybBfMjr_RkAcf5brYhl73uEo_2Q0ic9u2sOZ8WT_P-26J", "573241329035771955", null)),
    MISC_PLAYER_LOGS(new DiscordChannel("PvP Planet Bot", "https://i.gyazo.com/111aeaee30e78af4e88f871739f2eb67.png", "yAk7q-431I65Unsk7VyvgMJut0vvWCY_QYrnI69Nn1VX12vC5UJ8kpv1lU-0GPZ_aW61", "567759118136246296", null));

    private DiscordChannel channel;

    private CosmicDiscordChannels(DiscordChannel channel) {
        this.channel = channel;
    }

    public DiscordChannel getChannel() {
        return this.channel;
    }
}

