package com.astropvp.cc.voice;

import com.astropvp.cc.packet.type.in.PacketInVoice;
import com.astropvp.cc.util.Chat;
import com.astropvp.cc.packet.type.out.PacketOutTeamVoiceStatus;
import com.astropvp.cc.packet.type.out.PacketOutVoice;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class GlobalChannel {
    private static final GlobalChannel i = new GlobalChannel();
    public static GlobalChannel get(){return i;}

    @Getter
    @Setter
    private boolean locked = false;

    @Getter
    private Set<String> speakingGlobalMembers;

    private GlobalChannel(){
        speakingGlobalMembers = Sets.newConcurrentHashSet();
    }

    public void updatePlayerInfo(){
        speakingGlobalMembers.clear();

        for(CosmicPlayer cosmicPlayer : CosmicPlayers.get().getAll()){
            if(cosmicPlayer.getCurrentChannel() == EnumChannel.GLOBAL && cosmicPlayer.getPlayer().hasPermission("ccverify.voice.global")){
                speakingGlobalMembers.add(cosmicPlayer.getPlayer().getName());
            }
        }

        PacketOutTeamVoiceStatus status = new PacketOutTeamVoiceStatus(speakingGlobalMembers, speakingGlobalMembers);

        for(CosmicPlayer cosmicPlayer : CosmicPlayers.get().getAll()){
            if(locked){
                cosmicPlayer.sendPacket(new PacketOutTeamVoiceStatus(Sets.newHashSet("Voice Chat Is Locked!"), new HashSet()));
                continue;
            }
            cosmicPlayer.sendPacket(status);
        }
    }

    public void playerSpeak(PacketInVoice voice, CosmicPlayer cosmicPlayer){
        if(!cosmicPlayer.getPlayer().hasPermission("ccverify.voice.global")){
            if(System.currentTimeMillis() - cosmicPlayer.getLastErrorSpeaking() > 5000L){
                cosmicPlayer.notify(Chat.c("&7You do &cNOT &7have permission to speak globally!"), 100);
            }

            cosmicPlayer.setLastErrorSpeaking(System.currentTimeMillis());
            return;
        }

        PacketOutVoice outVoice = new PacketOutVoice(cosmicPlayer.getPlayer().getName(), "local", voice.getAudio());

        for(CosmicPlayer cp : CosmicPlayers.get().getAll()){
            if(cp == cosmicPlayer)continue;

            cp.sendPacket(outVoice);
        }
    }
}
