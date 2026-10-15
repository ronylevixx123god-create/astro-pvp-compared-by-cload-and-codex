package com.astropvp.cc.packet.handler;

import com.astropvp.cc.config.Config;
import com.astropvp.cc.packet.PacketIO;
import com.astropvp.cc.packet.type.in.*;
import com.astropvp.cc.packet.type.out.*;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import com.astropvp.cc.util.Chat;
import com.astropvp.cc.voice.EnumChannel;
import com.astropvp.cc.voice.GlobalChannel;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.google.common.collect.Lists;
import com.massivecraft.factions.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PacketHandler {

    public void handle(PacketInRequestPermission requestPermission, Player sender){
        switch(requestPermission.getPermission()){
            case "printer":
                PacketIO.get().queuePacket(sender, new PacketOutSetPermission("printer", Config.getInst().getModpermissions().isAllowPrinter()));
                break;
            case "freeroam":
                PacketIO.get().queuePacket(sender, new PacketOutSetPermission("freeroam", Config.getInst().getModpermissions().isAllowRoam()));
                break;
            case "breadcrumbs":
                PacketIO.get().queuePacket(sender, new PacketOutSetPermission("breadcrumbs", Config.getInst().getModpermissions().isAllowBreadcrumbs()));
                break;
            case "op":
                PacketIO.get().queuePacket(sender, new PacketOutSetPermission("op", Config.getInst().getModpermissions().isAllowOp()));
                break;
            case "interstellar":
                PacketIO.get().queuePacket(sender, new PacketOutSetPermission("interstellar", Config.getInst().getModpermissions().isAllowInterstellar()));
                break;
            case "banteam":
                PacketIO.get().queuePacket(sender, new PacketOutSetPermission("banteam", Config.getInst().getModpermissions().isAllowBanteam()));
                break;
        }
    }

    public void handle(PacketInHandshake handshake, Player sender){

    }

    public void handle(PacketInWorldNameRequest worldNameRequest, Player sender){
        PacketIO.get().queuePacket(sender, new PacketOutSetWorldName(sender.getWorld().getName(), new ArrayList(), (byte) 0, (byte) 0));
    }

    public void handle(PacketInTimeRequest timeRequest, Player sender){
        PacketIO.get().queuePacket(sender, new PacketOutTimeResponse(sender.getWorld().getTime()));
    }

    public void handle(PacketInPingAllies pingAllies, Player sender){
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(sender);

        if(fPlayer == null)return;

        Faction faction = fPlayer.getFaction();

        if(faction == null)return;

        PacketOutPingAllies container = new PacketOutPingAllies(sender.getName(), 0, pingAllies.getX(), pingAllies.getY(), pingAllies.getZ(), pingAllies.isAllies());

        if(pingAllies.isAllies()){
            for(Faction ally : Factions.getInstance().getAllFactions()){
                if(faction.getRelationTo(ally).isAlly()){
                    for(FPlayer allyPlayer : ally.getFPlayersWhereOnline(true)){
                        Player player = allyPlayer.getPlayer();

                        if(player.getWorld() != sender.getWorld())continue;

                        PacketIO.get().queuePacket(player, container);

                        player.sendMessage(Chat.cnp(Config.getInst().getPing().getPingMessage(), "NAME", sender.getName(),
                                "LOCATION", (Math.floor(pingAllies.getX())) + "x " + (Math.floor(pingAllies.getY())) + "y " + (Math.floor(pingAllies.getZ())),
                                "DISTANCE", "" + ((int) ((new Location(sender.getWorld(), pingAllies.getX(), pingAllies.getY(), pingAllies.getZ())).distance(player.getLocation())))));
                    }
                }
            }
        }

        for(FPlayer member : faction.getFPlayersWhereOnline(true)){
            Player player = member.getPlayer();

            if(player.getWorld() != sender.getWorld())continue;

            PacketIO.get().queuePacket(player, container);

            player.sendMessage(Chat.cnp(Config.getInst().getPing().getPingMessage(), "NAME", sender.getName(),
                    "LOCATION", ((int) Math.floor(pingAllies.getX())) + "x " + ((int) Math.floor(pingAllies.getY())) + "y " + ((int) Math.floor(pingAllies.getZ())),
                    "DISTANCE", "" + ((int) ((new Location(sender.getWorld(), pingAllies.getX(), pingAllies.getY(), pingAllies.getZ())).distance(player.getLocation())))));
        }
    }

    public void handle(PacketInSetChannel setChannel, Player sender){

    }

    public void handle(PacketInChunkClaim chunkDataRequest, Player sender){
        if(FactionUtils.attemptClaim(FPlayers.getInstance().getByPlayer(sender), FactionUtils.getFaction(sender), new Location(sender.getWorld(), chunkDataRequest.getX(), 0, chunkDataRequest.getZ()), true)) {
            PacketOutChunkData packetOutChunkData = new PacketOutChunkData(chunkDataRequest.getX(), chunkDataRequest.getZ(), FactionUtils.getFactionId(sender), FactionUtils.getFaction(sender).getTag());
            PacketOutChunkDataList packet = new PacketOutChunkDataList(Lists.newArrayList(packetOutChunkData));
            CosmicPlayers.get().getCosmicPlayer(sender).sendPacket(packet);
        }

    }

    public void handle(PacketInChunkDataRequest packetInChunkDataRequest, Player sender) {
    }

    public void handle(PacketInStartEmote startEmote, Player sender){
        if(!sender.hasPermission("savageastroemotes." + startEmote.getEmote())) {
            sender.sendMessage(CC.translate("&c&l(!) &cYou do not have access to this emote"));
            return;
        }
        PacketOutStartEmote packet = new PacketOutStartEmote(sender.getEntityId(), startEmote.getEmote());
        for(CosmicPlayer cosmicPlayer : CosmicPlayers.get().getAll()){
            if(cosmicPlayer.getPlayer() == sender){
                cosmicPlayer.sendPacket(new PacketOutStartEmote(-1, startEmote.getEmote()));
                continue;
            }

            if(sender.getWorld() != cosmicPlayer.getPlayer().getWorld()){
                continue;
            }

            if(sender.getLocation().distance(cosmicPlayer.getPlayer().getLocation()) < Config.getInst().getEmotes().getShowInRadius()){
                cosmicPlayer.sendPacket(packet);
            }
        }
    }

    public void handle(PacketInVoice voice, Player sender){
        byte[] data = voice.getAudio();

        String channel = "local";

        CosmicPlayer cosmicPlayer = CosmicPlayers.get().getCosmicPlayer(sender);

        if(!Config.getInst().getVoice().isEnabled()){
            if(System.currentTimeMillis() - cosmicPlayer.getLastErrorSpeaking() > 5000){
                cosmicPlayer.notify(Chat.c("&7Voice chat is &cNOT &7enabled!"), 100);

                cosmicPlayer.setLastErrorSpeaking(System.currentTimeMillis());
            }
            return;
        }

        if(cosmicPlayer.getCurrentChannel() == null)return;

        if(cosmicPlayer.getCurrentChannel() == EnumChannel.GLOBAL){
            GlobalChannel.get().playerSpeak(voice, cosmicPlayer);
            return;
        }

        PacketOutVoice container = new PacketOutVoice(sender.getName(), channel, data);

        for(CosmicPlayer cp : cosmicPlayer.getCurrentlySpeakingTo()){
            if(cp == cosmicPlayer)continue;

            cp.sendPacket(container);
        }
    }


}
