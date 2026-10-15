package me.astropvp.astroscoreboard.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.astropvp.astroscoreboard.AstroScoreboardAPI;

import java.util.HashMap;
import java.util.Map;

public class PlayerDisplayManager {


	public static void setUserPrefix(Player player, Player viewer, String prefix) {
		if(prefix.length() >= 16) {
			prefix = prefix.substring(0, 16);
		}
		if(viewer == null || player == null || prefix == null) {
			return;
		}
		
		Scoreboard board = viewer.getScoreboard();
		if(board == null) {
			Bukkit.getLogger().info("Not updating " + player.getName() + "'s prefix -> " + viewer.getName());
			return;
		}
		
		String teamName = AstroScoreboardAPI.getTeamName(player);
		Team team = board.getTeam(teamName);
		if(team == null) {
			team = board.registerNewTeam(teamName);
		}
		team.setPrefix(prefix);
		if(!team.hasEntry(player.getName())) {
			team.addEntry(player.getName());
		}


	}
	
	public static void setUserSuffix(Player player, Player viewer, String suffix) {
		if(suffix.length() >= 16) {
			suffix = suffix.substring(0, 16);
		}
		if(viewer == null || player == null || suffix == null) {
			return;
		}
		
		Scoreboard board = viewer.getScoreboard();
		if(board == null) {
			Bukkit.getLogger().info("Not updating " + player.getName() + "'s prefix -> " + viewer.getName());
			return;
		}
		
		String teamName = AstroScoreboardAPI.getTeamName(player);
		Team team = board.getTeam(teamName);
		if(team == null) {
			team = board.registerNewTeam(teamName);
		}
		team.setSuffix(suffix);
		if(!team.hasEntry(player.getName())) {
			team.addEntry(player.getName());
		}
	}
}
