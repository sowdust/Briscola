package briscola.behaviours.player;

import briscola.PlayerAgent;
import static briscola.common.ACLCodes.ACL_BID_OFFER;
import static briscola.common.Messages.AUCTION_CONV_ID;
import briscola.memory.player.AuctionMemory;
import briscola.messages.AuctionStatusMessage;
import briscola.objects.Bid;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;

public class PlayAuction extends Behaviour {

    private static final long serialVersionUID = 1L;

    private final PlayerAgent agent;
    private final AuctionMemory status;
    private boolean myTurn;
    private final MessageTemplate mt;
    private MessageTemplate finalMt;
    private boolean done;
    private boolean winner;

    PlayAuction(PlayerAgent agent) {
        this.agent = agent;
        this.status = agent.getAuctionMemory();
        this.myTurn = false;
        this.done = false;
        this.winner = false;
        this.mt = MessageTemplate.and(MessageTemplate.MatchPerformative(
            briscola.common.ACLCodes.ACL_BID_STATUS),
                                      MessageTemplate.MatchSender(
                                          agent.getMazziereAID()));
    }

    @Override
    public void action() {
        if (myTurn) {

            //  WHEN OFFERING BIDS
            ACLMessage offerBid = new ACLMessage(ACL_BID_OFFER);
            offerBid.addReceiver(agent.getMazziereAID());
            Bid myBid = status.computeBid(agent.getHand());
            try {
                offerBid.setContentObject(myBid);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            myAgent.send(offerBid);
            //agent.say("Offerto " + myBid);
            myTurn = false;
            block();

        } else {

            //  WHEN RECEIVING BIDS
            finalMt = MessageTemplate.and(MessageTemplate.MatchConversationId(
                AUCTION_CONV_ID + (status.counter())), mt);

            ACLMessage statusMsg = myAgent.receive(finalMt);
            if (statusMsg == null) {
                block();
            } else {
                try {

                    AuctionStatusMessage last = (AuctionStatusMessage) statusMsg.getContentObject();
                    String s;
                    if (last.justBid == null) {
                        s = "Comincia l'asta. Tocca a " + last.next.getName();

                    } else {

                        s = "Offerta " + last.justBid + ".";

                        if (last.next != null) {
                            s += " Tocca a: " + last.next.getName();
                        }

                        agent.addBid(last.justBid);
                    }
                    agent.say(s);
                    status.increaseCounter();

                    /**
                     * TODO: aggiungere qui la logica della memoria dell'asta
                     */
                    if (last.justBid != null && !last.justBid.passo()) {
                        status.setBest(last.justBid);
                    }

                    if (last.next != null && last.next.getAID().equals(
                        myAgent.getAID())) {

                        myTurn = true;
                    }

                    done = last.done;
                    if (done && last.bestBidder.getAID().equals(myAgent.getAID())) {
                        winner = true;
                    }

                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean done() {
        if (done) {
            agent.say("Asta conclusa");
            if (winner) {
                myAgent.addBehaviour(new DeclareBriscola(agent));
                myAgent.addBehaviour(new WaitForBriscola(agent));
            } else {
                myAgent.addBehaviour(new WaitForBriscola(agent));
            }
        }
        return done;
    }

}
