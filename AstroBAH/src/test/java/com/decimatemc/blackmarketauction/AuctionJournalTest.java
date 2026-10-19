package com.decimatemc.blackmarketauction;

import net.astropvp.auction.AuctionJournal;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class AuctionJournalTest {
    @Rule public TemporaryFolder temporary = new TemporaryFolder();
    @Test public void noJournalMeansNoRecoveredEscrow() {
        assertNull(new AuctionJournal(temporary.getRoot()).restore());
    }
    @Test public void interruptedMoneyAndRewardOperationsNeverReplayAutomatically() throws Exception {
        for(String phase : new String[]{"bid-pending","shutdown-refund-pending","reward-pending"}) {
            File directory=temporary.newFolder();
            Files.write(new File(directory,"active-auction.yml").toPath(),("phase: " + phase + "\nincoming-amount: 100000\n").getBytes(StandardCharsets.UTF_8));
            AuctionJournal journal=new AuctionJournal(directory);
            try { journal.restore();fail("Ambiguous " + phase + " must require reconciliation"); }
            catch(IllegalStateException expected){assertTrue(journal.isPending());}
            assertTrue(new File(directory,"active-auction.yml").exists());
        }
    }
    @Test public void damagedJournalIsPreservedInsteadOfSilentlyDiscardingFunds() throws Exception {
        File record=new File(temporary.getRoot(),"active-auction.yml");
        Files.write(record.toPath(),"phase: active\nitem: invalid-base64\n".getBytes(StandardCharsets.UTF_8));
        try { new AuctionJournal(temporary.getRoot()).restore();fail("Damaged escrow must not be ignored"); }
        catch(IllegalStateException expected){assertTrue(record.exists());}
    }
}
