package briscola.memory.mazziere;

import briscola.MazziereAgent;
import briscola.Player;
import briscola.behaviours.SendAndWait;
import static briscola.common.ACLCodes.ACL_ILLEGAL_MOVE;
import briscola.objects.Bid;
import static briscola.objects.Rank.DEUCE;
import jade.lang.acl.ACLMessage;
import java.util.List;

public class AuctionStatus {

    private final MazziereAgent mazziere;
    private final List<Player> bidders;
    private Bid bestBid;
    private Player bestBidder;
    private Bid justBid;
    private Player justBidder;
    //private Player next;
    private short inGame;
    private int counter;
    private int turn;
    private SendAndWait lastMessageSent;

    public AuctionStatus(MazziereAgent mazziere, List<Player> bidders) {
        this.bestBid = null;
        this.bestBidder = null;
        this.justBid = null;
        //this.next = null;
        this.justBidder = null;
        this.bidders = bidders;
        this.counter = 0;
        this.mazziere = mazziere;
        this.inGame = 5;
        this.turn = 0;
    }

    /**
     * iterates through the bidders list until
     * there is a player that has not passed (player != null)
     *
     * @return
     */
    public Player getNext() {
        return bidders.get(turn);
    }

    public void setLastMessageSent(SendAndWait b) {
        this.lastMessageSent = b;
    }

    public void computeNext() {

        int c = 1;
        while ((bidders.get((turn + c) % 5) == null) && c < 5) {
            ++c;
        }
        turn = (turn + c) % 5;

        if (bidders.get(turn) == null) {
            throw new RuntimeException("next è null, ingame " + inGame);
        }
    }

    public Bid getBestBid() {
        return bestBid;
    }

    public Player getBestBidder() {
        return bestBidder;
    }

    public Bid getJustBid() {
        return justBid;
    }

    public Player getJustBidder() {
        return justBidder;
    }

    public int getCounter() {
        return counter;
    }

    public boolean done() {
        return inGame == 1 || (bestBid != null && bestBid.rank() == DEUCE);
    }

    public boolean addBid(Player bidder, Bid bid) {
        if (bidder != getNext()) {
            String error = "Deve giocare " + getNext() + " ma ha giocato " + bidder;
            //mazziere.say(error);
            ACLMessage m = new ACLMessage(ACL_ILLEGAL_MOVE);
            m.addReceiver(bidder.getAID());
            m.setContent(error);
            mazziere.send(m);
            mazziere.addBehaviour(lastMessageSent);
            System.out.println("Invalida da " + bidder);
            return false;

        }
        if (bestBid != null && !bid.passo() && bestBid.rank().getValue() <= bid.rank().getValue()) {
            String error = "Offera non valida! Offerto: " + bid + " corrente: " + bestBid;
            //mazziere.say(error);
            ACLMessage m = new ACLMessage(ACL_ILLEGAL_MOVE);
            m.addReceiver(bidder.getAID());
            m.setContent(error);
            mazziere.send(m);
            mazziere.addBehaviour(lastMessageSent);
            System.out.println("Invalida da " + bidder);
            return false;
        }

        //  in case Bid is valid
        if (!bid.passo()) {
            bestBid = justBid = bid;
            bestBidder = justBidder = bidder;
            ++counter;
            computeNext();
            return true;
        }

        // finally, in case it is a pass
        justBid = bid;
        justBidder = bidder;
        ++counter;
        --inGame;
        for (int i = 0; i < bidders.size(); ++i) {
            if (bidders.get(i) != null && bidders.get(i).getAID().equals(
                bidder.getAID())) {
                bidders.set(i, null);
            }
        }
        if (inGame > 0) {
            computeNext();
        }
        return true;

    }

}
