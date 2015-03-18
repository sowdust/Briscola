package briscola.behaviours.player;

import briscola.Player;
import briscola.PlayerAgent;
import static briscola.common.ACLCodes.ACL_COMMUNICATE_BRISCOLA;
import briscola.objects.Card;
import briscola.objects.Rank;
import static briscola.objects.Role.GIAGUARO;
import static briscola.objects.Role.SOCIO;
import static briscola.objects.Role.VILLANO;
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
                Player giaguaro = player.getAuctionMemory().getBest().getPlayer();
                //  comunichiamo a log cos'è successo
                player.setBriscola(briscolaCard);
                player.say(
                    "Il giaguaro è " + giaguaro.getName() + "; ha chiamato " + briscolaRank + " " + briscolaSuit);

                /*  AGGIORNIAMO MEMORIA REASONER
                 -  fact ruolo giaguaro
                 -  fact briscola
                 -  [fact socio] se siamo noi
                 */
                //  AGGIORNIAMO LE NOSTRE INFO SUI RUOLI
                Fact f = new Fact("giaguaro", player.getRete());
                f.setSlotValue("player", new Value(giaguaro));
                player.assertFact(f);

                if (player.hasCard(briscolaCard)) {
                    player.setRole(SOCIO);
                    try {
                        player.computeStrenght();
                    } catch (JessException e) {
                        e.printStackTrace();
                    }
                } else if (giaguaro.getAID().equals(player.getAID())) {
                    player.setRole(GIAGUARO);
                } else {
                    player.setRole(VILLANO);
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
