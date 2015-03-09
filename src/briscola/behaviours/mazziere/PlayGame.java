package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import briscola.Player;
import briscola.behaviours.SendMessage;
import static briscola.common.ACLCodes.ACL_BOUNCE_GIOCATA;
import static briscola.common.ACLCodes.ACL_TELL_FIRST_TURN;
import briscola.memory.TurnStatus;
import briscola.messages.GiocataMessage;
import briscola.messages.TurnStatusMessage;
import briscola.objects.Card;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import static jade.core.behaviours.ParallelBehaviour.WHEN_ALL;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.List;

/**
 * this behaviour should incapsulate all things a mazziere needs to do while
 * managing a turn. These things are:
 *
 * 1) say who is next
 * 2) receive cards from everyone (in order? or parallel?)
 * 3) communicate who scores. go to 1
 *
 * @author mat
 */
public class PlayGame extends Behaviour {

    private final MazziereAgent mazziere;
    private final List<Player> players;
    private TurnStatus status;
    private int counter;
    private boolean done;
    private ManageTurn manageTurn;

    public PlayGame(MazziereAgent mazziere) {
        this.mazziere = mazziere;
        this.players = mazziere.getPlayers();
        //this.status = new TurnStatus(mazziere.getPlayers(), 0);
        this.counter = 0;
        this.done = false;
        this.manageTurn = null;

    }

    @Override
    public void action() {

        //  in case it is a new turn, start it
        if (status == null) {

            int next = 0;
            for (int i = 0; i < players.size(); ++i) {
                if (players.get(i).getAID().equals(
                    mazziere.getTable().getGiaguaro().getAID())) {
                    next = (i + 1) % 5;
                    break;
                }
            }

            status = new TurnStatus(players, players.get(next));
            TurnStatusMessage msg = new TurnStatusMessage(0, 0, null, null,
                                                          status.getNext());
            myAgent.addBehaviour(new SendMessage(players, ACL_TELL_FIRST_TURN,
                                                 msg));
            mazziere.say(
                "Comunicato il primo giocatore: " + status.getNext());

        }

        if (manageTurn == null) {
            manageTurn = new ManageTurn();
            myAgent.addBehaviour(manageTurn);
        }

//  in case the turn has finished, communicate who scores
        if (status.getCounter() == 5) {
            mazziere.say("Finito turno " + status.getMano());

            mazziere.say("A questo punto dovrei calcolare chi prende ma... ");

            Player prossimo = players.get(0);

            //  se la partita è ancora in corso
            if (status.initMano(prossimo)) {
                TurnStatusMessage msg = new TurnStatusMessage(0,
                                                              status.getMano(),
                                                              null, null,
                                                              prossimo);
                myAgent.addBehaviour(new SendMessage(players,
                                                     ACL_TELL_FIRST_TURN,
                                                     msg));
                manageTurn = new ManageTurn();
                myAgent.addBehaviour(manageTurn);

            } else {
                mazziere.say("Partita conclusa");
                done = true;
            }
        }

        //  otherwise stay waiting for messages to update turn status
    }

    @Override
    public boolean done() {
        return done;
    }

    class ManageTurn extends ParallelBehaviour {

        ManageTurn() {
            super(WHEN_ALL);
            for (Player p : players) {
                this.addSubBehaviour(new ReceiveGiocata(p));
            }
        }
    }

    class ReceiveGiocata extends Behaviour {

        private Player player;
        private boolean received;
        private Card giocata;

        ReceiveGiocata(Player player) {
            this.player = player;
            this.received = false;
            this.giocata = null;
        }

        @Override
        public void action() {

            MessageTemplate info1 = MessageTemplate.MatchSender(player.getAID());
            MessageTemplate info2 = MessageTemplate.MatchPerformative(
                briscola.common.ACLCodes.ACL_TELL_GIOCATA);
            MessageTemplate info = MessageTemplate.and(info1, info2);
            ACLMessage confM = myAgent.receive(info);
            if (confM != null) {
                try {
                    GiocataMessage msg = (GiocataMessage) confM.getContentObject();
                    mazziere.say("Ricevuta giocata: " + msg);
                    status.addGiocata(msg.player, msg.card, msg.mano);
                    TurnStatusMessage ms = new TurnStatusMessage(
                        status.getCounter(), status.getMano(), msg.player,
                        msg.card,
                        status.getNext());
                    myAgent.addBehaviour(new SendMessage(players,
                                                         ACL_BOUNCE_GIOCATA,
                                                         ms));
                    received = true;
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                }
            } else {
                mazziere.say("attendendo la giocata di " + status.getNext());
                block();
            }
        }

        @Override
        public boolean done() {
            //System.out.println("waiting for " + player);
            return received;
        }

    }

}
