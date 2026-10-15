package me.astropvp.factionlogs.struct;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.astropvp.factionlogs.enums.LogType;

@RequiredArgsConstructor
@Getter

public abstract class FactionLog implements Comparable<FactionLog> {
    private final LogType logtype;
    @Setter
    private Long time = System.currentTimeMillis();

    public abstract String serialize();

    @Override
    public int compareTo(FactionLog e) {
        return this.getTime().compareTo(e.getTime());
    }

}
