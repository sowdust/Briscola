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

    public ReceiveScore(PlayerAgent agent) {
        this.agent = agent;
        this.done = false;
    }

    @Override
    public void action() {
        MessageTemplate info1 = MessageTemplate.MatchSender(
            agent.getMazziereAID());
        MessageTemplate info2 = MessageTemplate.MatchPerformative(
            briscola.common.ACLCodes.ACL_SCORE_MESSAGE);
        MessageTemplate info = MessageTemplate.and(info1, info2);

        ACLMessage myHandMsg = myAgent.receive(info);
        if (myHandMsg != null) {

            try {
                ScoreMessage h = (ScoreMessage) myHandMsg.getContentObject();

                agent.say("### Punteggi:");
                for (int i = 0; i < h.players.size(); ++i) {
                    agent.say(
                        h.players.get(i).getName() + "\t" + h.points.get(i));
                }

                //  send confirmation message to mazziere
                ACLMessage confirm = new ACLMessage(
                    briscola.common.ACLCodes.ACL_MESSAGE_RECEIVED);
                confirm.addReceiver(agent.getMazziereAID());
                confirm.setConversationId(myHandMsg.getConversationId());
                myAgent.send(confirm);
                myAgent.addBehaviour(new PlayAuction(agent));
                done = true;

            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return done;
    }

}
