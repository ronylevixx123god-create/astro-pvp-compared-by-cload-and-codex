/*
 * Decompiled with CFR 0.150.
 */
package org.arkhamnetwork.Arkkit.patches;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

final class UUIDStorage {
    private long assigned;
    private UUID uuid;

    UUIDStorage() {
    }

    public UUID getUUID() {
        if (this.uuid == null || this.assigned == 0L || this.assigned < System.currentTimeMillis()) {
            this.generateUUID();
        }
        return this.uuid;
    }

    public UUID generateUUID() {
        this.uuid = UUID.randomUUID();
        this.assigned = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5L);
        return this.uuid;
    }

    public long getAssigned() {
        return this.assigned;
    }
}

