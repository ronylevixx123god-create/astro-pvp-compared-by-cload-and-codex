package net.crankedpvp.arcade.utils;

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

    public static <A,B> Pair<A,B> with(final A value0, final B value1) {
        return new Pair<A,B>(value0,value1);
    }
}

