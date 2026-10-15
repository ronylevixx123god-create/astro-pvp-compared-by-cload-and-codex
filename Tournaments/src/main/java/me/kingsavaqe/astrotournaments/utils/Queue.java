package me.kingsavaqe.astrotournaments.utils;

import java.util.LinkedList;

public class Queue<E> {

    private final LinkedList<E> list = new LinkedList<E>();

    public void enqueue(E item) {
        list.addLast(item);
    }

    public E dequeue() {
        return list.poll();
    }

    public boolean hasItems() {
        return !list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public void addItems(Queue<? extends E> q) {
        while (q.hasItems()) list.addLast(q.dequeue());
    }

    public void removeFromQueue(E item) {
        list.remove(item);
    }

    public boolean contains(E item) {
        return list.contains(item);
    }

    public int getPosition(E item) {
        for (int i = 0; i < this.size(); i++) {
            if (list.get(i) == item) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        list.clear();
    }
}