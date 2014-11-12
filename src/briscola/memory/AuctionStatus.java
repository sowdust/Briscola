package briscola.memory;

import briscola.Player;
import briscola.objects.Card;

public class AuctionStatus {

    private Card bestBid;
    private Player bestBidder;
    private Card justBid;
    private Player justBidder;
    private Player next;
    private short state;

    public AuctionStatus() {
        this.bestBid = null;
        this.bestBidder = null;
        this.justBid = null;
        this.next = null;
        this.justBidder = null;
        this.state = -1;
    }

    public void setBidder(Player bidder) {
        next = bidder;
    }

    public void begin() {
        state = 0;
    }

    public void close() {
        state = 1;
    }

}
