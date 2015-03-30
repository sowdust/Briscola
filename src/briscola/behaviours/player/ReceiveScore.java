package briscola.behaviours.player;

import briscola.PlayerAgent;
import briscola.messages.ScoreMessage;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveScore extends Behaviour {

    private static final long serialVersionUID = 1L;

    private final PlayerAgent agent;
    private boolean done;
    private boolean received = false;
    private String convID;

    public ReceiveScore(PlayerAgent agent) {
        this.agent = agent;
        this.done = false;
        this.convID = "";
        agent.setEndGameButton(true);
    }

    @Override
    public void action() {

        if (!received) {
            MessageTemplate info1 = MessageTemplate.MatchSender(
                agent.getMazziereAID());
            MessageTemplate info2 = MessageTemplate.MatchPerformative(
                briscola.common.ACLCodes.ACL_SCORE_MESSAGE);
            MessageTemplate info = MessageTemplate.and(info1, info2);

            ACLMessage myScoreMessage = myAgent.receive(info);
            convID = myScoreMessage.getConversationId();

            try {
                ScoreMessage h = (ScoreMessage) myScoreMessage.getContentObject();

                agent.say("### Punteggi:");
                for (int i = 0; i < h.players.size(); ++i) {
                    agent.say(
                        h.players.get(i).getName() + "\t" + h.points.get(i));
                }

            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }

            received = true;

        } else {

            if (!agent.graphic() || agent.gameOver()) {
                //  send confirmation message to mazziere
                ACLMessage confirm = new ACLMessage(
                    briscola.common.ACLCodes.ACL_MESSAGE_RECEIVED);
                confirm.addReceiver(agent.getMazziereAID());
                confirm.setConversationId(convID);
                myAgent.send(confirm);
                agent.say("Messaggio di fine partita spedito " + convID);

                done = true;
            }
        }

    }

    @Override
    public boolean done() {
        if (done) {
            agent.endGame();
            return true;
        }
        return false;
    }

}
