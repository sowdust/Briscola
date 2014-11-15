package briscola.behaviours.player;

import briscola.PlayerAgent;
import static briscola.common.ACLCodes.ACL_COMMUNICATE_BRISCOLA;
import briscola.objects.Suit;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class WaitForBriscola extends Behaviour {

    private PlayerAgent player;
    private boolean done;

    public WaitForBriscola(PlayerAgent player) {
        this.player = player;
        this.done = false;
    }

    @Override
    public void action() {

        MessageTemplate mt = MessageTemplate.MatchSender(player.getMazziereAID());
        MessageTemplate mu = MessageTemplate.MatchPerformative(
            ACL_COMMUNICATE_BRISCOLA);
        MessageTemplate m = MessageTemplate.and(mt, mu);
        ACLMessage msg = myAgent.receive(m);
        if (msg == null) {
            block();
        } else {
            try {
                Suit r = (Suit) msg.getContentObject();
                player.say(
                    "Il giaguaro è " + player.getAuctionMemory().getBest().getPlayer().getName() + "; ha chiamato " + player.getAuctionMemory().getBest().rank() + " " + r);
                done = true;
            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public boolean done() {
        return done;
    }

}
