package briscola.behaviours.player;

import briscola.PlayerAgent;
import briscola.objects.Hand;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jess.JessException;

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
            briscola.common.ACLCodes.ACL_YOUR_HAND);
        MessageTemplate info = MessageTemplate.and(info1, info2);

        ACLMessage myHandMsg = myAgent.receive(info);
        if (myHandMsg != null) {
            try {
                Hand h = (Hand) myHandMsg.getContentObject();
                agent.say("Mano ricevuta " + h);
                //  agisce anche su reasoner
                agent.setHand(h);
            } catch (UnreadableException | JessException ex) {
                ex.printStackTrace();
            }
            //  send confirmation message to mazziere
            ACLMessage confirm = new ACLMessage(
                briscola.common.ACLCodes.ACL_MESSAGE_RECEIVED);
            confirm.addReceiver(agent.getMazziereAID());
            confirm.setConversationId(myHandMsg.getConversationId());
            myAgent.send(confirm);
            agent.addBehaviour(new PlayAuction(agent));
            done = true;

        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return done;
    }

}
