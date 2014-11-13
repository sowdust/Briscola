/*
 */
package briscola.behaviours;

import briscola.GeneralAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GetChatMessage extends CyclicBehaviour {

    GeneralAgent agent;

    public GetChatMessage(GeneralAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {

        MessageTemplate info1 = MessageTemplate.MatchConversationId(
            agent.getChatID());
        MessageTemplate info2 = MessageTemplate.MatchPerformative(
            briscola.common.ACLCodes.ACL_CHAT);
        MessageTemplate info = MessageTemplate.and(info1, info2);

        ACLMessage chatMsg = myAgent.receive(info);
        if (chatMsg != null) {
            agent.printChat(chatMsg.getSender(), chatMsg.getContent());
        } else {
            block();
        }
    }

}
