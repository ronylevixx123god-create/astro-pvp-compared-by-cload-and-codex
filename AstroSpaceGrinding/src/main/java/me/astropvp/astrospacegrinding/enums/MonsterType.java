package me.astropvp.astrospacegrinding.enums;
import me.astropvp.astrospacegrinding.entities.SpaceMonster;
import me.astropvp.astrospacegrinding.entities.mobs.SpaceAlien;

public enum MonsterType {
	
	SPACE_ALIEN("Space Alien", new SpaceAlien());
	private final String name;
	private final SpaceMonster instance;
	
	MonsterType(String name, SpaceMonster instance) {
		this.name = name;
		this.instance = instance;
	}
	
	public SpaceMonster getInstance() {
		return instance;
	}
	
	public String getName() {
		return this.name;
	}
}
