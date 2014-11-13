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

    private final PlayerAgent agent;
    private final AuctionMemory status;
    private boolean myTurn;
    private final MessageTemplate mt;
    private MessageTemplate finalMt;
    private boolean done;

    PlayAuction(PlayerAgent agent) {
        this.agent = agent;
        this.status = new AuctionMemory(agent);
        myTurn = false;
        done = false;
        mt = MessageTemplate.and(MessageTemplate.MatchPerformative(
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
            agent.say("waiting for offer # "
                + AUCTION_CONV_ID + (status.counter()));
            ACLMessage statusMsg = myAgent.receive(finalMt);
            if (statusMsg == null) {
                block();
            } else {
                try {

                    AuctionStatusMessage last = (AuctionStatusMessage) statusMsg.getContentObject();
                    agent.say(
                        "ricevuta: " + last.justBid + ". Next: " + last.next);

                    if (last.justBid != null) {
                        agent.addBid(last.justBid);
                    }

                    status.increaseCounter();

                    /**
                     * TODO: aggiungere qui la logica della memoria dell'asta
                     */
                    if (last.justBid != null && !last.justBid.passo()) {
                        status.setBest(last.justBid);
                    }

                    if (last.next.getAID().equals(myAgent.getAID())) {

                        myTurn = true;
                    }

                    done = last.done;

                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean done() {
        if (done) {
            agent.say("asta conclusa");
        }
        return done;
    }

}
