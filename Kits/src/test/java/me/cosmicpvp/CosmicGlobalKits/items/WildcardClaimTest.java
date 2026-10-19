package me.cosmicpvp.CosmicGlobalKits.items;

import me.cosmicpvp.CosmicGlobalKits.permissions.GrantResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class WildcardClaimTest {
    private final List<String> calls = new ArrayList<>();
    private boolean claimedElsewhere;
    private final List<Consumer<GrantResult>> pendingUnlocks = new ArrayList<>();
    private WildcardClaim claim;

    @Before
    public void setUp() {
        claim = new WildcardClaim(new WildcardClaim.Actions() {
            public boolean isItemAlreadyClaimed() { return claimedElsewhere; }
            public void markItemClaimed() { calls.add("consume"); }
            public void returnItem() { calls.add("return"); }
            public void closeAfterUnlock() { calls.add("close"); }
            public void closeClaimedElsewhere() { calls.add("closeElsewhere"); }
        });
    }

    private void click() {
        claim.click(pendingUnlocks::add);
    }

    private void finish(GrantResult r) {
        pendingUnlocks.remove(0).accept(r);
    }

    @Test
    public void grantedConsumesItemAndCloses() {
        click();
        assertTrue(claim.isPending());
        finish(GrantResult.GRANTED);
        assertEquals(Arrays.asList("consume", "close"), calls);
        claim.inventoryClosed();
        assertEquals("closing afterwards does not return the item", 2, calls.size());
    }

    @Test
    public void failedKeepsItemAndMenuOpen() {
        click();
        finish(GrantResult.FAILED);
        assertTrue(calls.isEmpty());
        assertFalse(claim.isFinished());
        claim.inventoryClosed();
        assertEquals(Collections.singletonList("return"), calls);
    }

    @Test
    public void alreadyGrantedDoesNotConsume() {
        click();
        finish(GrantResult.ALREADY_GRANTED);
        assertFalse(calls.contains("consume"));
    }

    @Test
    public void closedWhileSavingThenGrantedConsumesWithoutReturning() {
        click();
        claim.inventoryClosed();
        assertTrue("no return while the save is pending", calls.isEmpty());
        finish(GrantResult.GRANTED);
        assertEquals(Collections.singletonList("consume"), calls);
    }

    @Test
    public void closedWhileSavingThenFailedReturnsItem() {
        click();
        claim.inventoryClosed();
        finish(GrantResult.FAILED);
        assertEquals(Collections.singletonList("return"), calls);
    }

    @Test
    public void secondClickWhileSavingIsIgnored() {
        click();
        click();
        assertEquals(1, pendingUnlocks.size());
    }

    @Test
    public void itemClaimedElsewhereNeverStartsUnlock() {
        claimedElsewhere = true;
        click();
        assertTrue(pendingUnlocks.isEmpty());
        assertEquals(Collections.singletonList("closeElsewhere"), calls);
    }

    @Test
    public void closingWithoutClickReturnsItemOnce() {
        claim.inventoryClosed();
        claim.inventoryClosed();
        assertEquals(Collections.singletonList("return"), calls);
    }
}
