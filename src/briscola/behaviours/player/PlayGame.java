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
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayGame extends Behaviour {

    private PlayerAgent agent;
    private TurnStatus status;
    private List<Player> players;
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
    public void action() {

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
                    agent.say("Inizia: " + msg.next.getName());
                    agent.gui().initMano(status.getMano(), msg.next);
                    if (status.getNext().getAID().equals(agent.getAID())) {
                        myAgent.addBehaviour(new SendGiocata());
                    }
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                }
            } else {
                block();
            }
        } else {

            if (status != null)
                mano = status.getMano();

            //  se non siamo in attesa delle giocate del turno
            if (receiveGiocate == null) {
                receiveGiocate = new ReceiveGiocate();
                myAgent.addBehaviour(receiveGiocate);
                block();
            }

            //  se la mano è finita
            if (status.getCounter() == 5) {
                agent.say("Mano terminata");

                myAgent.addBehaviour(new ReceiveNext());
                receiveGiocate = null;
                block();
            }
        }
    }

    @Override
    public boolean done() {

        return mano == 8;

    }

    class ReceiveGiocate extends Behaviour {

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
                    agent.addGiocata(msg.counter, msg.justPlayer, msg.justCard);
                    status.setNext(msg.next);
                    //agent.say("[ Next ]\t " + status.getNext());
                    ++counter;
                    if (/*status.getCounter() < 5 &&*/status.getNext() != null && status.getNext().getAID().equals(
                            agent.getAID()) && status.getMano() > lastPlayed) {
                        myAgent.addBehaviour(new SendGiocata());
                    }
                } catch (UnreadableException ex) {
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

        @Override
        public void action() {
            ACLMessage gMsg = new ACLMessage(ACL_TELL_GIOCATA);
            try {

                Card c = agent.getHand().drawRandom();
                GiocataMessage g = new GiocataMessage(agent.getPlayer(), c,
                                                      status.getMano(),
                                                      status.getCounter());
                gMsg.setContentObject(g);
                gMsg.addReceiver(agent.getMazziereAID());
                myAgent.send(gMsg);
                agent.say("* Gioco" + g);
                ++lastPlayed;

            } catch (IOException ex) {
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
                    agent.gui().initMano(status.getMano(), status.getNext());
                    if (status.getNext().getAID().equals(agent.getAID())) {
                        myAgent.addBehaviour(new SendGiocata());
                    }
                    receiveGiocate = null;
                } catch (UnreadableException ex) {
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