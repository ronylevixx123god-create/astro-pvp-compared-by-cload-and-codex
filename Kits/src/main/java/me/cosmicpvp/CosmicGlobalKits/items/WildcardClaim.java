package me.cosmicpvp.CosmicGlobalKits.items;

import me.cosmicpvp.CosmicGlobalKits.permissions.GrantResult;

import java.util.function.Consumer;

/**
 * Lifecycle of one wildcard /gkit unlock item while its menu is open.
 *
 * The item is consumed only when a grant reports {@link GrantResult#GRANTED}. While a grant is
 * still being saved, closing the menu does not hand the item back (that would allow duplication);
 * the pending result decides instead. Results must be delivered on the main thread.
 */
public final class WildcardClaim {
    public interface Actions {
        boolean isItemAlreadyClaimed();

        /** Consumes the unlock item. */
        void markItemClaimed();

        void returnItem();

        void closeAfterUnlock();

        void closeClaimedElsewhere();
    }

    private final Actions actions;
    private boolean finished;
    private boolean pending;
    private boolean closedWhilePending;

    public WildcardClaim(Actions actions) {
        this.actions = actions;
    }

    /** @param unlock starts an unlock and must later deliver its result to the given callback. */
    public void click(Consumer<Consumer<GrantResult>> unlock) {
        if (this.finished || this.pending) {
            return;
        }
        if (this.actions.isItemAlreadyClaimed()) {
            this.finished = true;
            this.actions.closeClaimedElsewhere();
            return;
        }
        this.pending = true;
        unlock.accept(this::complete);
    }

    private void complete(GrantResult result) {
        this.pending = false;
        if (result == GrantResult.GRANTED) {
            this.finished = true;
            this.actions.markItemClaimed();
            if (!this.closedWhilePending) {
                this.actions.closeAfterUnlock();
            }
            return;
        }
        if (this.closedWhilePending) {
            this.finished = true;
            this.actions.returnItem();
        }
    }

    public void inventoryClosed() {
        if (this.finished) {
            return;
        }
        if (this.pending) {
            this.closedWhilePending = true;
            return;
        }
        this.finished = true;
        this.actions.returnItem();
    }

    public boolean isPending() {
        return this.pending;
    }

    public boolean isFinished() {
        return this.finished;
    }
}
