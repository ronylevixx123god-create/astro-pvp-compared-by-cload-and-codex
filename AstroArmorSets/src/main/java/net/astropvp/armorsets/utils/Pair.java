package net.astropvp.armorsets.utils;

import java.util.Objects;

public final class Pair<FIRST, SECOND> {
    public final FIRST first;


    public final SECOND second;


    public Pair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

    public int hashCode() {
        return 17 * (first != null ? first.hashCode() : 0) + 17 * (second != null ? second.hashCode() : 0);
    }


    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }

        Pair<?, ?> that = (Pair) o;
        return (equal(first, first)) && (equal(second, second));
    }

    private static boolean equal(Object a, Object b) {
        return Objects.equals(a, b);
    }

    public String toString() {
        return String.format("{%s,%s}", first, second);
    }


    public SECOND getValue() {
        return second;
    }

    public FIRST getKey() {
        return first;
    }

}