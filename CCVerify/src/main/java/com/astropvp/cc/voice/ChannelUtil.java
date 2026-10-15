package com.astropvp.cc.voice;

import com.astropvp.cc.config.Config;
import com.astropvp.cc.packet.type.out.PacketOutTeamVoiceStatus;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

import java.util.*;

public class ChannelUtil {

    public static void recalculateGlobal(){
        GlobalChannel.get().updatePlayerInfo();
    }

    public static void recalculateProximity(){
        Collection<CosmicPlayer> all = CosmicPlayers.get().getAll();
        for(CosmicPlayer cp1 : all){
            cp1.setCurrentlySpeakingTo(Lists.newArrayList());
            if(!cp1.isOnCosmicClient() || (cp1.getCurrentChannel() != EnumChannel.PROXIMITY))continue;

            List<CosmicPlayer> speakingTo = new ArrayList<>();

            for(CosmicPlayer cp2 : all){
                if(cp1 == cp2 || !cp2.isOnCosmicClient() || (cp2.getCurrentChannel() != EnumChannel.PROXIMITY))continue;

                if(cp1.getPlayer().getWorld() != cp2.getPlayer().getWorld())continue;

                if(cp1.getPlayer().getLocation().distance(cp2.getPlayer().getLocation()) > Config.getInst().getVoice().getVoiceProximity())continue;

                speakingTo.add(cp2);
            }

            cp1.setCurrentlySpeakingTo(speakingTo);

            Set<String> allSpeaking = new HashSet<>();

            speakingTo.forEach(cp -> allSpeaking.add(cp.getPlayer().getName()));

            allSpeaking.addAll(GlobalChannel.get().getSpeakingGlobalMembers());

            PacketOutTeamVoiceStatus status = new PacketOutTeamVoiceStatus(allSpeaking, allSpeaking);

            cp1.sendPacket(status);
        }
    }

    public static void recalculateFaction(){
        Collection<CosmicPlayer> all = CosmicPlayers.get().getAll();
        for(CosmicPlayer cp1 : all){
            cp1.setCurrentlySpeakingTo(Lists.newArrayList());
            if(!cp1.isOnCosmicClient() || cp1.getCurrentChannel() != EnumChannel.FACTION)continue;

            List<CosmicPlayer> speakingTo = new ArrayList<>();

            Faction tpf = FPlayers.getInstance().getByPlayer(cp1.getPlayer()).getFaction();

            if(tpf == null)continue;

            for(CosmicPlayer cp2 : all){
                if(cp1 == cp2 || !cp2.isOnCosmicClient() || cp2.getCurrentChannel() != EnumChannel.FACTION)continue;

                Faction thatPF = FPlayers.getInstance().getByPlayer(cp2.getPlayer()).getFaction();

                if(tpf != thatPF)continue;

                speakingTo.add(cp2);
            }

            cp1.setCurrentlySpeakingTo(speakingTo);

            Set<String> allSpeaking = new HashSet<>();

            speakingTo.forEach(cp -> allSpeaking.add(cp.getPlayer().getName()));

            allSpeaking.addAll(GlobalChannel.get().getSpeakingGlobalMembers());

            PacketOutTeamVoiceStatus status = new PacketOutTeamVoiceStatus(allSpeaking, allSpeaking);

            cp1.sendPacket(status);
        }
    }
}
