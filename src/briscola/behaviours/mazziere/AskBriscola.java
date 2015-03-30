package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import briscola.Player;
import briscola.behaviours.SendAndWait;
import briscola.behaviours.SendMessage;
import static briscola.common.ACLCodes.ACL_ASK_BRISCOLA;
import static briscola.common.ACLCodes.ACL_COMMUNICATE_BRISCOLA;
import static briscola.common.ACLCodes.ACL_TELL_BRISCOLA;
import briscola.objects.Rank;
import briscola.objects.Suit;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jess.JessException;

/**
 *
 * once the bid is over, mazziere needs to ask the winner for the briscola seed.
 */
public class AskBriscola extends Behaviour {

    private static final long serialVersionUID = 1L;

    private final MazziereAgent mazziere;
    private final Player vincitore;
    private final Rank rank;
    private boolean sent;
    private boolean received;
    private boolean done;
    private Suit briscolaSuit;

    public AskBriscola(MazziereAgent mazziere, Player vincitore, Rank rank) {
        this.rank = rank;
        this.mazziere = mazziere;
        this.vincitore = vincitore;
        this.sent = false;
        this.received = false;
        this.done = false;
        this.briscolaSuit = null;

    }

    @Override
    public void action() {

        if (!sent) {

            //  se non l'abbiamo ancora chiesta al vincitore dell'asta
            SendMessage b = new SendMessage(vincitore, ACL_ASK_BRISCOLA, null);
            mazziere.addBehaviour(b);
            sent = true;
            block();
        }

        if (sent && !received) {
            //  se l'abbiamo chiesta e stiamo aspettando la risposta.

            MessageTemplate mt = MessageTemplate.MatchSender(
                vincitore.getAID());
            MessageTemplate mu = MessageTemplate.MatchPerformative(
                ACL_TELL_BRISCOLA);
            MessageTemplate m = MessageTemplate.and(mt, mu);
            ACLMessage briscolaM = myAgent.receive(m);
            if (briscolaM != null) {
                try {
                    briscolaSuit = (Suit) briscolaM.getContentObject();
                    mazziere.setBriscola(briscolaSuit, rank);
                    mazziere.say("Ricevuta briscola: " + briscolaSuit);
                    received = true;

                } catch (UnreadableException | JessException ex) {
                    ex.printStackTrace();
                }

            } else {
                mazziere.say(
                    "Aspettando che " + vincitore + " comunichi la sua briscola");
            }
        }
        if (sent && received) {
            //  se dobbiamo solo più comunicarla a tutti

            SendAndWait send = new SendAndWait(mazziere.getPlayers(),
                                               ACL_COMMUNICATE_BRISCOLA,
                                               briscolaSuit);
            mazziere.addBehaviour(send);
            done = true;
        }
    }

    @Override
    public boolean done() {
        if (done) {
            mazziere.addBehaviour(new PlayGame(mazziere));
            return true;
        }
        return false;
    }

}
