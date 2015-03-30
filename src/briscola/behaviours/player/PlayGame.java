package briscola.behaviours.player;

import briscola.Player;
import briscola.PlayerAgent;
import static briscola.common.ACLCodes.ACL_TELL_GIOCATA;
import briscola.memory.TurnStatus;
import briscola.messages.GiocataMessage;
import briscola.messages.TurnStatusMessage;
import briscola.objects.Card;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.List;
import jess.Fact;
import jess.JessException;

public class PlayGame extends Behaviour {

    private static final long serialVersionUID = 1L;

    private final PlayerAgent agent;
    private TurnStatus status;
    private final List<Player> players;
    private int lastPlayed;
    private ReceiveGiocate receiveGiocate;
    private int mano;

    public PlayGame(PlayerAgent agent) {
        this.agent = agent;
        this.players = agent.getPlayers();
        this.mano = -1;
        this.lastPlayed = -1;
        this.receiveGiocate = null;
    }

    @Override
    synchronized public void action() {

        //  se ancora non sappiamo il primo che comincia
        if (status == null) {

            MessageTemplate info1 = MessageTemplate.MatchSender(
                agent.getMazziereAID());
            MessageTemplate info2 = MessageTemplate.MatchPerformative(
                briscola.common.ACLCodes.ACL_TELL_FIRST_TURN);
            MessageTemplate info = MessageTemplate.and(info1, info2);
            ACLMessage confM = myAgent.receive(info);
            if (confM != null) {
                try {
                    TurnStatusMessage msg = (TurnStatusMessage) confM.getContentObject();
                    status = new TurnStatus(players, msg.next);
                    agent.setTurnStatus(status);
                    agent.say("Inizia: " + msg.next.getName());
                    agent.initMano(status.getMano(), msg.next);
                    if (status.getNext().getAID().equals(agent.getAID())) {
                        agent.addBehaviour(new SendGiocata());
                    }
                } catch (UnreadableException | JessException ex) {
                    ex.printStackTrace();
                }
            } else {
                block();
            }
        } else {

            if (status != null) {
                mano = status.getMano();
            }

            //  se non siamo in attesa delle giocate del turno
            if (receiveGiocate == null) {
                receiveGiocate = new ReceiveGiocate();
                agent.addBehaviour(receiveGiocate);
                block();
            }

            //  se la mano è finita
            if (status.getCounter() == 5) {
                agent.say("Mano terminata");
                StringBuilder s = new StringBuilder("");
                for (int i = 30; i > 0; --i) {
                    s.append("=");
                }
                agent.say(s.toString());

                agent.addBehaviour(new ReceiveNext());
                receiveGiocate = null;

                block();
            }
        }
    }

    @Override
    public boolean done() {
        if (mano == 7 && status.getCounter() == 5) {
            agent.addBehaviour(new ReceiveScore(agent));
            agent.say("Partita terminata");
            return true;
        }
        return false;
    }

    class ReceiveGiocate extends Behaviour {

        private static final long serialVersionUID = 1L;

        MessageTemplate info1;
        MessageTemplate info2;
        MessageTemplate info;
        int counter;

        ReceiveGiocate() {
            info1 = MessageTemplate.MatchSender(
                agent.getMazziereAID());
            info2 = MessageTemplate.MatchPerformative(
                briscola.common.ACLCodes.ACL_BOUNCE_GIOCATA);
            info = MessageTemplate.and(info1, info2);
            counter = 0;
        }

        @Override
        public void action() {

            ACLMessage confM = myAgent.receive(info);
            if (confM != null) {
                try {
                    TurnStatusMessage msg = (TurnStatusMessage) confM.getContentObject();
                    agent.say(msg.toString());
                    status.addGiocata(msg.justPlayer, msg.justCard, msg.mano);
                    agent.addGiocata(msg.mano, msg.counter, msg.justPlayer,
                                     msg.justCard);
                    status.setNext(msg.next);
                    //agent.say("[ Next ]\t " + status.getNext());
                    ++counter;
                    if (status.getCounter() < 5 && status.getNext() != null && status.getNext().getAID().equals(
                        agent.getAID()) && status.getMano() > lastPlayed) {
                        agent.addBehaviour(new SendGiocata());

                        /*if (status.getMano() == 0 || status.getMano() == 7) {
                         agent.addBehaviour(new PrintFacts(agent));
                         }*/
                    }
                } catch (UnreadableException | JessException ex) {
                    ex.printStackTrace();
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return counter == 5;
        }
    }

    class SendGiocata extends OneShotBehaviour {

        private static final long serialVersionUID = 1L;

        @Override
        public void action() {
            ACLMessage gMsg = new ACLMessage(ACL_TELL_GIOCATA);
            try {
                Fact f = new Fact("mio-turno", agent.getRete());
                agent.getRete().assertFact(f);
                agent.getRete().run();

                /*
                 Card c;
                 if (agent.getStrategy().random()) {
                 c = agent.getHand().drawRandom();
                 } else {
                 Value v = agent.getRete().fetch("DA-GIOCARE");
                 if (null == v) {

                 agent.say("Giocando a caso");
                 c = agent.getHand().drawRandom();
                 } else {
                 c = (Card) v.javaObjectValue(
                 agent.getRete().getGlobalContext());
                 agent.getHand().removeCard(c);
                 }
                 }*/
                Card c;
                try {
                    c = agent.play();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    c = null;
                }
                agent.setCardToPlay(null);

                GiocataMessage g = new GiocataMessage(agent.getPlayer(), c,
                                                      status.getMano(),
                                                      status.getCounter());
                gMsg.setContentObject(g);
                gMsg.addReceiver(agent.getMazziereAID());
                myAgent.send(gMsg);
                agent.say("* Gioco" + g);
                ++lastPlayed;

            } catch (IOException | JessException ex) {
                ex.printStackTrace();
            }
            block();
        }
    }

    class ReceiveNext extends Behaviour {

        ACLMessage confM = null;

        @Override
        public void action() {
            MessageTemplate info1 = MessageTemplate.MatchSender(
                agent.getMazziereAID());
            MessageTemplate info2 = MessageTemplate.MatchPerformative(
                briscola.common.ACLCodes.ACL_TELL_FIRST_TURN);
            MessageTemplate info = MessageTemplate.and(info1, info2);
            confM = myAgent.receive(info);
            if (confM != null) {
                try {
                    TurnStatusMessage msg = (TurnStatusMessage) confM.getContentObject();
                    agent.say(
                        "Mano #" + (status.getMano() + 1) + " - gioca " + msg.next.getName());

                    status.initMano(msg.next);
                    agent.initMano(status.getMano(), status.getNext());
                    if (status.getNext().getAID().equals(agent.getAID())) {
                        agent.addBehaviour(new SendGiocata());
                    }
                    receiveGiocate = null;
                } catch (UnreadableException | JessException ex) {
                    ex.printStackTrace();
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return confM != null;
        }

    }
}
