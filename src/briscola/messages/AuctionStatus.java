package briscola.messages;

import briscola.Player;
import briscola.objects.Card;
import java.io.Serializable;

/**
 * Message sent by Mazziere after every bid (and before starting it)
 * to update all players about the auction situation
 * (current bid, its owner, who bids next)
 *
 */
public class AuctionStatus implements Serializable {

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
    public final Card bestBid;
    public final Player bestBidder;
    public final Card justBid;
    public final Player justBidder;
    public final Player next;

    public AuctionStatus(Card currentBid, Player currentWinner, Card justBid,
                         Player whoBid, Player next) {

        this.bestBid = currentBid;
        this.bestBidder = currentWinner;
        this.justBid = justBid;

        this.justBidder = whoBid;
        this.next = next;

    }

}
