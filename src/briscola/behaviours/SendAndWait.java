package briscola.behaviours;

import briscola.Player;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 *
 * Sends a message with a unique conversation ID to all players in rcp
 * It then waits - blocked - until has received confirmation from all
 * players: a message with the same conversation ID and the
 * ACL_MESSAGE_RECEIVED type
 */
public class SendAndWait extends SendMessage {

    private int confirmations;

    public SendAndWait(List<Player> rcp, int type, Serializable content) {
        super(rcp, type, content);
        confirmations = 0;
        convId = UUID.randomUUID().toString();
    }

    public SendAndWait(Player rcp, int type, Serializable content) {
        super(rcp, type, content);
        confirmations = 0;
        convId = UUID.randomUUID().toString();
    }

    public SendAndWait(AID rcp, int type, Serializable content) {
        super(rcp, type, content);
        confirmations = 0;
        convId = UUID.randomUUID().toString();
    }

    @Override
    public void action() {
        super.action();

        MessageTemplate info1 = MessageTemplate.MatchConversationId(convId);
        MessageTemplate info2 = MessageTemplate.MatchPerformative(
            briscola.common.ACLCodes.ACL_MESSAGE_RECEIVED);
        MessageTemplate info = MessageTemplate.and(info1, info2);
        ACLMessage confM = myAgent.receive(info);
        if (confM != null) {
            confirmations++;
            confM = null;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        if (rcp != null) {
            return confirmations == rcp.size();
        } else {
            return confirmations == 1;
        }
    }

}
