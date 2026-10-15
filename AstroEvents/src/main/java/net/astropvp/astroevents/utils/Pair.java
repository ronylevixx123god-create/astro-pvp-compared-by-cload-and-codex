package net.astropvp.astroevents.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pair<L, R> {

    private L left;
    private R right;

    public boolean contains(L value) {
        return left.equals(value) || right.equals(value);
    }

    public void clear() {
        this.setLeft(null);
        this.setRight(null);
    }
}

