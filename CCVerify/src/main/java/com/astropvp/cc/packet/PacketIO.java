package com.astropvp.cc.packet;

import com.astropvp.cc.CCVerify;
import com.astropvp.cc.Debug;
import com.astropvp.cc.config.Config;
import com.astropvp.cc.packet.encryption.PacketEncryptor;
import com.astropvp.cc.packet.encryption.PacketSecurity;
import com.astropvp.cc.packet.handler.PacketHandler;
import com.astropvp.cc.packet.type.Packet;
import com.astropvp.cc.packet.type.in.PacketIn;
import com.astropvp.cc.packet.type.out.*;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import com.astropvp.cc.util.Chat;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gson.JsonSyntaxException;
import io.netty.buffer.Unpooled;
import me.astropvp.proxypach.ProxyPatch;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class PacketIO {
    private static final long timeInit = System.currentTimeMillis();

    private static PacketIO instance;

    private BukkitTask sender;

    private BukkitTask receiver;

    private final PacketAdapter adapter;

    private final Map<String, List<String>> headers;

    private static PacketHandler packetHandler;

    private static PacketEncryptor encryptor;

    public static PacketIO get() {
        return instance;
    }

    public static Map<UUID, List<Short>> receivedIDS = new HashMap<>();

    private static final Map<UUID, ConcurrentLinkedDeque<FutureOutPacket>> outPacketQueue = new ConcurrentHashMap<>();

    private static final Map<UUID, ConcurrentLinkedDeque<FutureInPacket>> inPacketQueue = new ConcurrentHashMap<>();

    public static PacketIO create() {
        if (instance != null)
            return null;
        instance = new PacketIO();
        return instance;
    }

    public void shutdown() {
        if (this.sender != null)
            this.sender.cancel();
        if (this.receiver != null)
            this.receiver.cancel();
        if (this.adapter != null)
            ProtocolLibrary.getProtocolManager().removePacketListener(this.adapter);
    }

    private boolean verifyQueue(ConcurrentLinkedDeque<FutureInPacket> var1) {
        if (!var1.isEmpty()) {
            FutureInPacket var2 = var1.getLast();
            PacketContainer var3;
            try {
                var3 = PacketContainer.GSON.fromJson(new String(var2.getData(), StandardCharsets.UTF_8), PacketContainer.class);
            } catch (IllegalStateException | JsonSyntaxException exception) {
                return false;
            }
            if (var3 == null) return false;
            if (var1.size() >= 1000000) {
                var1.clear();
                return false;
            }
            return !var3.isSplitNext();
        }
        return true;
    }

    private PacketIO() {
        createSendTask();
        createReceiveTask();
        packetHandler = new PacketHandler();
        this.headers = new HashMap<>();
        byte[] array = Base64.getDecoder().decode("13c9esdEOtLC+JPE+GaWFcF86+MqlLoLaY6yRedTwUp3Fx1NFWDwPnBYjCb3Y83yPV8rujPeaCkARIabBx5Ca+J7FstlvlUN2+j5WpCSFUFDvqjw2KG0k6/WoABk1aHQi0n/sypTUB6ZkG2JnyuaNTQh2ebhc5yMIdeAViwIcyW4t3I5p7+VYjcq9YBX4CfKRJjSZaVyJIFBHouCndqlNicc4PvKcmTnYwj66h6rI4t5Iq97ticrbvXdZifre9IGcVLVhOIpCtiXoLBA06t6beLFhOF8OatYVu86CXvT0TBUaidS2NO+l7Qqk4m9k2fDENna2zpOe237XLBm7uKviA==");
        array = PacketSecurity.a(array);
        encryptor = new PacketEncryptor(array, true);
        this.adapter = new PacketAdapter(CCVerify.get(), ListenerPriority.LOWEST, PacketType.Play.Client.CUSTOM_PAYLOAD) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() != PacketType.Play.Client.CUSTOM_PAYLOAD)
                    return;
                PacketPlayInCustomPayload customPayload = (PacketPlayInCustomPayload) event.getPacket().getHandle();
                String channelName = customPayload.a();
                if (!channelName.equals("cosmic"))
                    return;
                byte[] packetArray = customPayload.b().array();
                FutureInPacket received = new FutureInPacket(packetArray, event.getPlayer());
                Debug.log("Received packet ID " + received.getMyID() + " at " + (System.currentTimeMillis() - PacketIO.timeInit) + " from " + event.getPlayer() + "!");

                synchronized (PacketIO.inPacketQueue) {
                    if (PacketIO.inPacketQueue.containsKey(event.getPlayer().getUniqueId())) {
                        PacketIO.inPacketQueue.get(event.getPlayer().getUniqueId()).add(received);
                    } else {
                        PacketIO.inPacketQueue.put(event.getPlayer().getUniqueId(), new ConcurrentLinkedDeque(Lists.newArrayList((Object[]) new FutureInPacket[]{received})));
                    }


                }
            }

        };
        ProtocolLibrary.getProtocolManager().addPacketListener(this.adapter);

    }

    public void queuePacket(Player recipient, Packet container) {
        synchronized (outPacketQueue) {
            if (outPacketQueue.containsKey(recipient.getUniqueId())) {
                outPacketQueue.get(recipient.getUniqueId()).add(new FutureOutPacket(container, recipient));
            } else {
                outPacketQueue.put(recipient.getUniqueId(), new ConcurrentLinkedDeque<>(Lists.newArrayList(new FutureOutPacket(container, recipient))));
            }
        }
    }

    private Set<String> filterPlayerEmotes(Player player) {
        Set<String> emotes = new HashSet<>(Config.getInst().getEmotes().getAllowedEmotes());
        Set<String> filteredEmotes = emotes.stream().filter(emote -> player.hasPermission("savagecosmicemote."+emote)).collect(Collectors.toSet());
        return filteredEmotes;
    }

    public void addPlayer(Player player) {
        if (ProxyPatch.playerNames.contains(player.getName())) return;
        registerPlayer(player);
        CosmicPlayers.get().wrapPlayer(player);
        queuePacket(player, new PacketOutSessionKey("13c9esdEOtLC+JPE+GaWFcF86+MqlLoLaY6yRedTwUp3Fx1NFWDwPnBYjCb3Y83yPV8rujPeaCkARIabBx5Ca+J7FstlvlUN2+j5WpCSFUFDvqjw2KG0k6/WoABk1aHQi0n/sypTUB6ZkG2JnyuaNTQh2ebhc5yMIdeAViwIcyW4t3I5p7+VYjcq9YBX4CfKRJjSZaVyJIFBHouCndqlNicc4PvKcmTnYwj66h6rI4t5Iq97ticrbvXdZifre9IGcVLVhOIpCtiXoLBA06t6beLFhOF8OatYVu86CXvT0TBUaidS2NO+l7Qqk4m9k2fDENna2zpOe237XLBm7uKviA=="));
        queuePacket(player, new PacketOutFeatureSet("COSMICPVP"));
        queuePacket(player, new PacketOutAllowedEmotes(filterPlayerEmotes(player)));

/*        Map<String, List<Double>> cooldowns = new HashMap<>();
        for (InventoryPets type : InventoryPets.values()) {
            List<Double> current = new ArrayList<>();
            for (int i = 1; i < 5; i++) {
                current.add((double) 360);
            }
            cooldowns.put(type.name(), current);
        }
        queuePacket(player, new SItemCooldownsListPacket(cooldowns));*/

        if (Config.getInst().getTooltip().isSendDiscordTooltipOnJoin())
            queuePacket(player, new PacketOutTooltip(Chat.c(Config.getInst().getTooltip().getDiscordTooltipTitle()),
                    Chat.c(Config.getInst().getTooltip().getDiscordTooltipText()), null,

                    Config.getInst().getTooltip().getDiscordTooltipItem(), Config.getInst().getTooltip().getDiscordTooltipItemDamage()));
        addPlayerToHeader(player);
    }

    public void addPlayerToHeader(Player player) {
       /*  Map<String, List<String>> map = Config.getInst().getHeaders().getAllHeaders();
       for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (player.hasPermission("ccverify." + entry.getKey().toLowerCase())) {

                this.headers.put(player.getName(), entry.getValue());
                break;
            }
        }*/
        if (!player.hasPermission("astrocore.apollo")) return;
        this.headers.put(player.getName(), Collections.singletonList(CC.translate(LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix())));
        PacketOutPlayerHeaders playerHeaders = new PacketOutPlayerHeaders(this.headers, false);
        for (CosmicPlayer cp : CosmicPlayers.get().getAll())
            queuePacket(cp.getPlayer(), playerHeaders);
    }

    public void removePlayerFromHeader(Player player) {
        this.headers.remove(player.getName());
        PacketOutPlayerHeaders playerHeaders = new PacketOutPlayerHeaders(this.headers, false);
        for (CosmicPlayer cp : CosmicPlayers.get().getAll())
            cp.sendPacket(playerHeaders);
    }

    public void removePlayer(Player player) {
        CosmicPlayers.get().unwrapPlayer(player);
        synchronized (inPacketQueue) {
            inPacketQueue.remove(player.getUniqueId());
        }
        synchronized (outPacketQueue) {
            outPacketQueue.remove(player.getUniqueId());
        }
        removePlayerFromHeader(player);

        synchronized (receivedIDS) {
            receivedIDS.remove(player.getUniqueId());
        }
    }

    private void registerPlayer(Player player) {
        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());
        serializer.writeBytes("cosmic".getBytes(StandardCharsets.UTF_8));
        PacketPlayOutCustomPayload customPayload = new PacketPlayOutCustomPayload("REGISTER", serializer);
        (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(customPayload);
    }

    public void pumpOutQueue() {
        synchronized (outPacketQueue) {
            for (UUID uuid : outPacketQueue.keySet()) {
                Iterator<FutureOutPacket> packetIter = outPacketQueue.get(uuid).iterator();
                while (packetIter.hasNext()) {
                    FutureOutPacket packet = packetIter.next();
                    packetIter.remove();
                    String json = PacketContainer.GSON.toJson(packet.getPacket());
                    Iterable<String> splitter = Splitter.fixedLength(3000).split(json);
                    Iterator<String> spliterator = splitter.iterator();
                    while (spliterator.hasNext()) {
                        byte[] sent;
                        String content = spliterator.next();
                        PacketContainer container = new PacketContainer(packet.getPacket().getID(), spliterator.hasNext(), content);
                        if (container.getPacketID() == 33) {
                            sent = PacketContainer.GSON.toJson(container).getBytes(StandardCharsets.UTF_8);
                        } else {
                            String data = Base64.getEncoder().encodeToString(encryptor.a(container.getContent().getBytes(StandardCharsets.UTF_8), true));
                            sent = PacketContainer.GSON.toJson(new PacketContainer(container.getPacketID(), spliterator.hasNext(), data)).getBytes(StandardCharsets.UTF_8);
                        }
                        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());
                        serializer.writeBytes(sent);
                        PacketPlayOutCustomPayload customPayload = new PacketPlayOutCustomPayload("cosmic", serializer);
                        (((CraftPlayer) packet.getRecipient()).getHandle()).playerConnection.sendPacket(customPayload);
                    }
                }
            }
            outPacketQueue.clear();
        }
    }

    public void pumpInQueue() {
        synchronized (inPacketQueue) {
            for (UUID uuid : inPacketQueue.keySet()) {
                ConcurrentLinkedDeque<FutureInPacket> packetIter = inPacketQueue.get(uuid);
                Player player = Bukkit.getPlayer(uuid);
                if (player == null)
                    continue;
                while (!packetIter.isEmpty() && verifyQueue(packetIter)) {
                    FutureInPacket packet = packetIter.removeFirst();
                    PacketContainer container = PacketContainer.GSON.fromJson(new String(packet.getData(), StandardCharsets.UTF_8), PacketContainer.class);
                    short packetID = container.getPacketID();
                    Class<? extends PacketIn> packetClass = (Class) PacketCommons.getPacketClass(packetID);
                    if (packetClass == null) {
                        Debug.log("Unknown packet ID: " + packetID);
                        continue;
                    }
                    Debug.log("Received packet: " + packetClass.getSimpleName() + " (ID: " + packet.getMyID() + ")");
                    synchronized (PacketIO.receivedIDS) {

                        if (receivedIDS.containsKey(uuid)) {
                            if (!receivedIDS.get(uuid).contains(packetID)) {
                                receivedIDS.get(uuid).add(packetID);
                            }
                        } else {
                            List<Short> ids = new ArrayList<>();
                            ids.add(packetID);
                            receivedIDS.put(uuid, ids);
                        }
                    }
                    String content = container.getContent();
                    if (packetID != 7)
                        content = new String(encryptor.b(Base64.getDecoder().decode(content), true), StandardCharsets.UTF_8);
                    Debug.log("Initial content: " + content);
                    if (container.isSplitNext()) {
                        int nextSplit = 2;
                        do {
                            FutureInPacket nextPacket = packetIter.removeFirst();
                            container = PacketContainer.GSON.fromJson(new String(nextPacket.getData(), StandardCharsets.UTF_8), PacketContainer.class);
                            if (packetID != container.getPacketID())
                                throw new IllegalStateException("Split packet ID mismatch: " + packetID + ", " + container.getPacketID());
                            String extra = container.getContent();
                            if (packetID != 7)
                                extra = new String(encryptor.b(Base64.getDecoder().decode(extra), true), StandardCharsets.UTF_8);
                            content = content + extra;
                            Debug.log("Found split part " + nextSplit++ + " in packet queue!");
                        } while (container.isSplitNext());
                    }
                    try {
                        PacketIn received = PacketContainer.GSON.fromJson(content, packetClass);
                        received.handle(packetHandler, player);
                        CosmicPlayer cosmicPlayer = CosmicPlayers.get().getCosmicPlayer(player);
                        if (cosmicPlayer == null)
                            cosmicPlayer = CosmicPlayers.get().wrapPlayer(player);
                        cosmicPlayer.setOnCosmicClient(true);
                    } catch (IllegalStateException exc) {
                        Bukkit.getLogger().info("Illegal formatting in JSON: \"" + content + "\"!");
                    }
                }
            }
        }
    }

    private void createSendTask() {
        this.sender = (new BukkitRunnable() {
            public void run() {
                PacketIO.this.pumpOutQueue();
            }
        }).runTaskTimerAsynchronously(CCVerify.get(), 0L, 1L);
    }

    private void createReceiveTask() {
        this.receiver = (new BukkitRunnable() {
            public void run() {
                PacketIO.this.pumpInQueue();
            }
        }).runTaskTimerAsynchronously(CCVerify.get(), 0L, 1L);
    }
}
