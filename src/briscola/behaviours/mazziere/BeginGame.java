package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import briscola.behaviours.GetChatMessage;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

/**
 * INIT GAME. sends useful info to all players before starting the actual game.
 * creates, sets and sends a unique ID used as conversation-id in the chat
 * messages. sends to all players the players info waits for their confirmation.
 *
 * @author mat
 */
public class BeginGame extends Behaviour {

    private static final long serialVersionUID = 1L;

    private final MazziereAgent mazziere;
    private boolean sent;
    private ACLMessage[] confMsg;
    private int received;

    public BeginGame(MazziereAgent mazziere) {
        this.mazziere = mazziere;
        this.sent = false;
    }

    @Override
    public void action() {
        //  send info to all players
        if (!sent) {

            //  compute a unique ID as Chat ID
            UUID uniqueKey = UUID.randomUUID();
            mazziere.setChatID(uniqueKey.toString());
            mazziere.say("Chat id: " + uniqueKey);
            myAgent.addBehaviour(new GetChatMessage(mazziere));

            try {
                //  send players list
                mazziere.sendMessage(mazziere.getPlayers(),
                                     briscola.common.ACLCodes.ACL_SEND_PLAYERS,
                                     (Serializable) mazziere.getPlayers());
                //  send chat_id list
                mazziere.sendMessage(mazziere.getPlayers(),
                                     briscola.common.ACLCodes.ACL_SEND_CHAT_ID,
                                     uniqueKey.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            mazziere.say("Inviate info giocatori");
            sent = true;
            confMsg = new ACLMessage[5];
            received = 0;

        }

        // get info from all players
        for (AID p : mazziere.getPlayersAID()) {
            ACLMessage ms = myAgent.receive(MessageTemplate.and(
                MessageTemplate.MatchContent(
                    briscola.common.Messages.INFO_RECEIVED),
                MessageTemplate.MatchSender(p)));
            if (ms != null) {
                confMsg[received++] = ms;
            }
        }
    }

    @Override
    public boolean done() {
        for (ACLMessage a : confMsg) {
            if (null == a) {
                return false;
            }
        }
        mazziere.say("Tutti hanno mandato le proprie info. Possiamo procedere.");
        mazziere.addBehaviour(new DistributeHands(mazziere));
        return true;
    }

}
