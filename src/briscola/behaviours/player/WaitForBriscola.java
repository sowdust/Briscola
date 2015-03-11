package briscola.behaviours.player;

import briscola.PlayerAgent;
import static briscola.common.ACLCodes.ACL_COMMUNICATE_BRISCOLA;
import briscola.objects.Card;
import briscola.objects.Rank;
import briscola.objects.Suit;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jess.Fact;
import jess.JessException;
import jess.Value;

public class WaitForBriscola extends Behaviour {

    private static final long serialVersionUID = 1L;

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
                Suit briscolaSuit = (Suit) msg.getContentObject();
                Rank briscolaRank = player.getAuctionMemory().getBest().rank();
                Card briscolaCard = new Card(briscolaRank, briscolaSuit);
                //  comunichiamo a log cos'è successo
                player.say(
                    "Il giaguaro è " + player.getAuctionMemory().getBest().getPlayer().getName() + "; ha chiamato " + briscolaRank + " " + briscolaSuit);

                /*  AGGIORNIAMO MEMORIA REASONER
                 -  fact ruolo giaguaro
                 -  fact briscola
                 -  [fact socio] se siamo noi
                 */
                Fact f = new Fact("giaguaro", player.getRete());
                f.setSlotValue("player", new Value(
                               player.getAuctionMemory().getBest().getPlayer()));
                Fact g = new Fact("briscola", player.getRete());
                g.setSlotValue("suit", new Value(briscolaSuit));
                g.setSlotValue("rank", new Value(briscolaRank));
                g.setSlotValue("card", new Value(briscolaCard));
                player.assertFact(f);
                player.assertFact(g);

                if (player.hasCard(briscolaCard)) {
                    Fact s = new Fact("socio", player.getRete());
                    s.setSlotValue("player", new Value(player.getPlayer()));
                    player.assertFact(s);
                }

                done = true;

            } catch (UnreadableException | JessException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public boolean done() {
        if (done) {
            myAgent.addBehaviour(new PlayGame(player));
            return true;
        }
        return false;
    }

}
