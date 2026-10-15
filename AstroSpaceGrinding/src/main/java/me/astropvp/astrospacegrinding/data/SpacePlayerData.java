package me.astropvp.astrospacegrinding.data;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import me.astropvp.astrospacegrinding.enums.MonsterType;

public class SpacePlayerData {
	
	private static final Set<SpacePlayerData> data = Sets.newHashSet();
	
	private final UUID uuid;
	private final Map<MonsterType, Integer> kills;
	private final long when_created;
	private double moneyMade;
	private double xpMade;

	public SpacePlayerData(Player player) {
		this.uuid = player.getUniqueId();
		this.kills = Maps.newHashMap();
		this.when_created = System.currentTimeMillis();
		this.moneyMade = 0;
		this.xpMade = 0;
		data.add(this);
	}
	
	public String getPlayerName() {
		return Bukkit.getPlayer(this.uuid).getName();
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}

	public UUID getUuid() {
		return uuid;
	}

	public int getKills(MonsterType type) {
		return kills.getOrDefault(type, 0);
	}
	
	public void increment(MonsterType type) {
		setKills(type, getKills(type) + 1);
	}
	
	public void clearKills(MonsterType type) {
		setKills(type, 0);
	}
	
	public void setKills(MonsterType type, int value) {
		kills.put(type, value);
	}
	
	public int getTimeInSpace() {
		return (int) ((System.currentTimeMillis() - when_created) / 1000);
	}
	
	public void remove() {
		remove(this.getPlayer());
	}

	public double getMoneyMade() {
		return moneyMade;
	}

	public double getXpMade() {
		return xpMade;
	}

	public void addMoney(double money) {
		moneyMade += money;
	}




	public void addXp(double xp) {
		xpMade += xp;
	}

	public static void remove(Player player) {
		UUID uuid = player.getUniqueId();
		for(SpacePlayerData pdata: data) {
			if(pdata.getUuid() == uuid) {
				data.remove(pdata);
				return;
			}
		}
	}
	
	public static SpacePlayerData findPlayer(Player player) {
		UUID uuid = player.getUniqueId();
		for(SpacePlayerData pdata: data) {
			if(pdata.getUuid() == uuid) {
				return pdata;
			}
		}
		return new SpacePlayerData(player);
	}
}
