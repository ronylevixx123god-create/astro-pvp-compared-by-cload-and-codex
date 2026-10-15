package com.cosmicpvp.cosmicutils.discord;

import com.google.gson.JsonObject;

public class DiscordChannel {
    private String username;
    private String avatarURL;
    private String tokenId;
    private String channelId;
    private String channelRank;

    public DiscordChannel(String username, String avatarURL, String tokenId, String channelId, String channelRank) {
        this.username = username;
        this.avatarURL = avatarURL;
        this.tokenId = tokenId;
        this.channelId = channelId;
        this.channelRank = channelRank;
    }

    public String getURL() {
        return "https://discordapp.com/api/webhooks/" + this.channelId + "/" + this.tokenId;
    }

    public JsonObject getPostData() {
        JsonObject obj = new JsonObject();
        if (this.getAvatarURL() != null) {
            obj.addProperty("avatar_url", this.getAvatarURL());
        }
        if (this.getUsername() != null) {
            obj.addProperty("username", this.getUsername());
        }
        return obj;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAvatarURL() {
        return this.avatarURL;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getChannelRank() {
        return this.channelRank;
    }
}

