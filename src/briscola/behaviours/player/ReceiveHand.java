package briscola.behaviours.player;

import briscola.PlayerAgent;
import briscola.behaviours.SendMessage;
import briscola.objects.Hand;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveHand extends Behaviour {

    private static final long serialVersionUID = 1L;
    private final PlayerAgent agent;
    private boolean done;

    public ReceiveHand(PlayerAgent agent) {
        this.agent = agent;
        done = false;
    }

    @Override
    public void action() {
        MessageTemplate info1 = MessageTemplate.MatchSender(
            agent.getMazziereAID());
        MessageTemplate info2 = MessageTemplate.MatchPerformative(
            briscola.common.Names.ACL_YOUR_HAND);
        MessageTemplate info = MessageTemplate.and(info1, info2);

        ACLMessage myHandMsg = myAgent.receive(info);
        if (myHandMsg != null) {
            try {
                Hand h = (Hand) myHandMsg.getContentObject();
                agent.say("Mano ricevuta " + h);
                agent.setHand(h);
            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }
            //  send confirmation message to mazziere
            ACLMessage confirm = new ACLMessage(
                briscola.common.Names.ACL_MESSAGE_RECEIVED);
            confirm.addReceiver(agent.getMazziereAID());
            confirm.setConversationId(myHandMsg.getConversationId());
            myAgent.send(confirm);

        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return done;
    }

}
