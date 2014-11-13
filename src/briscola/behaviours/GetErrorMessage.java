package briscola.behaviours;

import briscola.PlayerAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GetErrorMessage extends CyclicBehaviour {

    PlayerAgent agent;

    public GetErrorMessage(PlayerAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {

        MessageTemplate info = MessageTemplate.MatchPerformative(
            briscola.common.ACLCodes.ACL_ILLEGAL_MOVE);

        ACLMessage chatMsg = myAgent.receive(info);
        if (chatMsg != null) {
            agent.say(chatMsg.getContent());
        } else {
            block();
        }
    }

}
