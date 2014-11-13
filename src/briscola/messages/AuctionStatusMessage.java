package briscola.messages;

import briscola.Player;
import briscola.objects.Bid;
;
import java.io.Serializable;

/**
 * Message sent by Mazziere after every bid (and before starting it)
 * to update all players about the auction situation
 * (current bid, its owner, who bids next)
 *
 */


public class AuctionStatusMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Variables used to deduce current auction status
     * In some cases (beginning of bid, if last bid is best bid)
     * some of them are redundant: when so, they are set to null
     * and the player needs to be able to deduce what's missing.
     *
     * @field bestBid can be null if == justBid,or beginning of auction
     * @field bestBidder can be null if == whoBid, or beginning of auction
     * @field justBid last card/action bid
     * @field justBidder last player to bid
     * @field next tells who's next to bid
     *
     */
    public final Bid bestBid;
    public final Player bestBidder;
    public final Bid justBid;
    public final Player justBidder;
    public final Player next;
    public final int counter;
    public final boolean done;

    public AuctionStatusMessage(Bid currentBid, Player currentWinner,
                                Bid justBid,
                                Player whoBid, Player next, int counter) {

        this.bestBid = currentBid;
        this.bestBidder = currentWinner;
        this.justBid = justBid;
        this.justBidder = whoBid;
        this.next = next;
        this.counter = counter;
        this.done = false;
    }

    public AuctionStatusMessage(Bid currentBid, Player currentWinner,
                                Bid justBid,
                                Player whoBid, Player next, int counter,
                                boolean done) {

        this.bestBid = currentBid;
        this.bestBidder = currentWinner;
        this.justBid = justBid;
        this.justBidder = whoBid;
        this.next = next;
        this.counter = counter;
        this.done = done;
    }

    @Override
    public String toString() {
        return bestBid + " "
            + bestBidder + " "
            + justBid + " "
            + justBidder + " "
            + next + " "
            + counter + " ";
    }
}
