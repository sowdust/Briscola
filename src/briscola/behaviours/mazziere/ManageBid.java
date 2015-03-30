package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import briscola.Player;
import briscola.behaviours.SendAndWait;
import static briscola.common.ACLCodes.ACL_BID_STATUS;
import static briscola.common.Messages.AUCTION_CONV_ID;
import briscola.memory.mazziere.AuctionStatus;
import briscola.messages.AuctionStatusMessage;
import briscola.objects.Bid;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.ArrayList;
import java.util.List;

/**
 * Auction phase of the game.
 * Mazziere keeps track of current auction status.
 * Based upon that memory, it sends a message to update all bidders.
 * Waits for the next player to bid, updates its status and goes back to prev.
 * point.
 */
public class ManageBid extends Behaviour {

    private static final long serialVersionUID = 1L;

    /**
     * @field counter keeps track of # of bids. STARTS WITH 1 !!!
     * @field bidders is a list of all players active in the auction.
     * who passes becomes null
     * @field waiting tells me if I am waiting for a bid or I need to send
     * info/close auction
     */
    private final MazziereAgent mazziere;
    private final AuctionStatus status;
    private final MessageTemplate matchPerf;
    private MessageTemplate mt;
    private MessageTemplate matchSender;
    private boolean waiting;
    private Player next;
    List<Player> bidders;
    private int counterSent;

    public ManageBid(MazziereAgent mazziere) {
        this.mazziere = mazziere;

        bidders = new ArrayList<>();
        for (Player p : mazziere.getPlayers()) {
            bidders.add(p);
        }
        this.status = new AuctionStatus(mazziere, bidders);
        waiting = false;
        counterSent = 0;

        matchPerf = MessageTemplate.MatchPerformative(
            briscola.common.ACLCodes.ACL_BID_OFFER);
        mazziere.sendChat("Benvenuti a tutti e buon divertimento.");

    }

    @Override
    public void action() {
        //  in case we are not waiting for any bid
        if (!waiting && (true || status.getCounter() == counterSent)) {

            next = status.getNext();
            AuctionStatusMessage m = new AuctionStatusMessage(
                status.getBestBid(),
                status.getBestBidder(),
                status.getJustBid(),
                status.getJustBidder(),
                next, status.getCounter(), status.done());

            SendAndWait b = new SendAndWait(mazziere.getPlayers(),
                                            ACL_BID_STATUS, m);
            b.setConvId(
                AUCTION_CONV_ID + status.getCounter() + "a" + mazziere.getChatID().substring(
                    0, 5));
            //  sends "blocking" message to everyone to update about current situation
            mazziere.addBehaviour(b);
            status.setLastMessageSent(b);

            waiting = true;
            ++counterSent;
            mazziere.say("Spedita offerta #" + status.getCounter());

            block();

        } else if (waiting) {
            //  if we are waiting for a bid

            matchSender = MessageTemplate.MatchSender(next.getAID());
            mt = MessageTemplate.and(matchSender, matchPerf);
            ACLMessage confM = myAgent.receive(mt);
            if (confM != null) {
                try {
                    Bid bid = (Bid) confM.getContentObject();
                    if (status.addBid(next, bid)) {
                        waiting = false;
                        mazziere.say(
                            "Offerta #" + status.getCounter() + ": " + bid);
                        mazziere.getMemory().addBid(bid);
                    }
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                }

            } else {
                block();
            }

        } else {
            block();
        }

    }

    @Override
    public boolean done() {
        if (status.done()) {
            next = null;
            AuctionStatusMessage m = new AuctionStatusMessage(
                status.getBestBid(),
                status.getBestBidder(),
                status.getJustBid(),
                status.getJustBidder(),
                next, status.getCounter(), true);
            SendAndWait b = new SendAndWait(mazziere.getPlayers(),
                                            ACL_BID_STATUS, m);
            b.setConvId(
                AUCTION_CONV_ID + status.getCounter() + "a" + mazziere.getChatID().substring(
                    0, 5));
            mazziere.say(
                AUCTION_CONV_ID + status.getCounter() + "a" + mazziere.getChatID().substring(
                    0, 5));
            mazziere.addBehaviour(b);

            mazziere.addBehaviour(new AskBriscola(mazziere,
                                                  status.getBestBidder(),
                                                  status.getBestBid().rank()));

            mazziere.say("Asta conclusa");
            mazziere.setGiaguaro(status.getBestBidder());
            return true;
        } else {
            return false;
        }

    }

}
