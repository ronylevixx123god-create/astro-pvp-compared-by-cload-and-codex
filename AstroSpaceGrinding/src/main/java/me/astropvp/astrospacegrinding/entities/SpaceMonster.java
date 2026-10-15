package me.astropvp.astrospacegrinding.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.astropvp.astrospacegrinding.enums.MonsterType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import me.astropvp.astrospacegrinding.data.SpacePlayerData;

public class SpaceMonster {

	public LivingEntity spawn(Location location) {
		return null;
	}

	public List<ItemStack> getRewards(Player player, LivingEntity entity) {
		return Lists.newArrayList();
	}

	public Map<ItemStack, Double> getLoot(MonsterType type) {
		return new HashMap<>();
	}

	public void onDeath(EntityDeathEvent event, SpacePlayerData killer) {

	}

	public void onPlayerDamageMob(EntityDamageByEntityEvent event, Player attacker) {

	}
}
