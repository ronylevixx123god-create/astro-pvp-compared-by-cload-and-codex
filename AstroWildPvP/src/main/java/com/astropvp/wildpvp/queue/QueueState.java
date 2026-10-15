package com.astropvp.wildpvp.queue;

public enum QueueState {
    IDLE(true, false),
    ACCEPTING(false, false),
    INVINCIBILITY(false, true),
    END(false, true);

    private boolean teleported;
    private boolean joinable;

    public boolean isJoinable() {
        return this.joinable;
    }

    public boolean isTeleported() {
        return this.teleported;
    }

    private QueueState(boolean joinable, boolean teleported) {
        this.joinable = joinable;
        this.teleported = teleported;
    }
}
