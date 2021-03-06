package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import briscola.Player;
import briscola.behaviours.SendAndWait;
import briscola.behaviours.SendMessage;
import static briscola.common.ACLCodes.ACL_BOUNCE_GIOCATA;
import static briscola.common.ACLCodes.ACL_SCORE_MESSAGE;
import static briscola.common.ACLCodes.ACL_TELL_FIRST_TURN;
import briscola.memory.TurnStatus;
import briscola.messages.GiocataMessage;
import briscola.messages.ScoreMessage;
import briscola.messages.TurnStatusMessage;
import briscola.objects.Card;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import static jade.core.behaviours.ParallelBehaviour.WHEN_ALL;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.ArrayList;
import java.util.List;
import jess.Funcall;
import jess.JessException;
import jess.RU;
import jess.Value;
import jess.ValueVector;

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

    private static final long serialVersionUID = 1L;

    private final MazziereAgent mazziere;
    private final List<Player> players;
    private TurnStatus status;
    private boolean done;
    private ManageTurn manageTurn;

    public PlayGame(MazziereAgent mazziere) {
        this.mazziere = mazziere;
        this.players = mazziere.getPlayers();
        //this.status = new TurnStatus(mazziere.getPlayers(), 0);
        this.done = false;
        this.manageTurn = null;
    }

    @Override
    synchronized public void action() {

        //  in case it is a new turn, start it
        if (status == null) {

            int next = 0;
            for (int i = 0; i < players.size(); ++i) {
                if (players.get(i).getAID().equals(
                    mazziere.getGiaguaro().getAID())) {
                    next = i; //(i + 1) % 5;
                    break;
                }
            }

            status = new TurnStatus(players, players.get(next));
            TurnStatusMessage msg = new TurnStatusMessage(0, 0, null, null,
                                                          status.getNext());
            mazziere.addBehaviour(new SendMessage(players, ACL_TELL_FIRST_TURN,
                                                  msg));
            mazziere.say(
                "Comunicato il primo giocatore: " + status.getNext());

        }

        if (manageTurn == null) {
            manageTurn = new ManageTurn();
            mazziere.addBehaviour(manageTurn);
        }

//  in case the turn has finished, communicate who scores
        if (status.getCounter() == 5) {

            //  recuperiamo le carte giocate
            Player prossimo = null;

            mazziere.say("Finito turno " + status.getMano());
            try {
                List<Card> carte = status.getCurrentMano().getCarteGiocate();

                ValueVector vv = new ValueVector();
                for (Card tc : carte) {
                    vv.add(tc);
                }

                Value v = new Funcall("calcola_presa", mazziere.getRete()).arg(
                    new Value(
                        vv, RU.LIST)).execute(
                        mazziere.getRete().getGlobalContext());

                Card winningCard = (Card) v.javaObjectValue(
                    mazziere.getRete().getGlobalContext());

                prossimo = status.getCurrentMano().getPlayerOfCard(winningCard);

            } catch (JessException ex) {
                ex.printStackTrace();
            }
            int partialScore = status.getCurrentMano().getPoints();
            int totalScore = status.updateScore(prossimo, partialScore);
            mazziere.say(
                "Prende " + prossimo.getName() + " che fa " + partialScore + " punti per un totale di " + totalScore);
            mazziere.getMemory().addPresa(prossimo, partialScore);

            if (status.initMano(prossimo)) {
                TurnStatusMessage msg = new TurnStatusMessage(0,
                                                              status.getMano(),
                                                              null, null,
                                                              prossimo);
                mazziere.addBehaviour(new SendMessage(players,
                                                      ACL_TELL_FIRST_TURN,
                                                      msg));
                manageTurn = new ManageTurn();
                mazziere.addBehaviour(manageTurn);

            } else {
                mazziere.say("Partita conclusa. Punteggi dei singoli:");
                List<Integer> points = new ArrayList<>();
                for (Player p : players) {
                    mazziere.say(p.getName() + "\t " + status.getScore(p));
                    points.add(status.getScore(p));
                }
                mazziere.getMemory().setPoints(points);
                ScoreMessage scoreMessage = new ScoreMessage(players, points);
                SendAndWait b = new SendAndWait(players, ACL_SCORE_MESSAGE,
                                                scoreMessage);
                mazziere.say("Spedendo punteggi...");
                SequentialBehaviour bb = new SequentialBehaviour();
                bb.addSubBehaviour(b);
                bb.addSubBehaviour(new EndGame(mazziere));
                mazziere.addBehaviour(bb);
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

        private static final long serialVersionUID = 1L;

        ManageTurn() {
            super(WHEN_ALL);
            for (Player p : players) {
                this.addSubBehaviour(new ReceiveGiocata(p));
            }
        }
    }

    class ReceiveGiocata extends Behaviour {

        private static final long serialVersionUID = 1L;

        private final Player player;
        private boolean received;
        //private final Card giocata;

        ReceiveGiocata(Player player) {
            this.player = player;
            this.received = false;
            //  this.giocata = null;
        }

        @Override
        synchronized public void action() {

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
                    // add card to game memory
                    mazziere.getMemory().addGiocata(status.getMano(),
                                                    status.getCounter(),
                                                    msg.player, msg.card);
                    mazziere.addBehaviour(new SendMessage(players,
                                                          ACL_BOUNCE_GIOCATA,
                                                          ms));

                    received = true;
                    block();
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                }
            } else {

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
