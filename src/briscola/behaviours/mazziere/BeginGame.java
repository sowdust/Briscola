package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import briscola.Player;
import briscola.behaviours.SendAndWait;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
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
    private final Map<AID, Integer> conferme;
    private boolean sent;
    private int received;

    public BeginGame(MazziereAgent mazziere) {
        this.mazziere = mazziere;
        this.sent = false;
        this.received = 0;
        this.conferme = new HashMap<>();
    }

    synchronized private void augmetReceived() {
        ++received;
    }

    synchronized private int getReceived() {
        return received;
    }

    @Override
    public void action() {
        //  send info to all players
        if (!sent) {

            //  compute a unique ID as Chat ID
            UUID uniqueKey = UUID.randomUUID();
            mazziere.setChatID(uniqueKey.toString());
            mazziere.say("Chat id: " + uniqueKey);
            mazziere.setChatBehaviour();

            //  send players list
            mazziere.addBehaviour(new SendAndWait(mazziere.getPlayers(),
                                                  briscola.common.ACLCodes.ACL_SEND_PLAYERS,
                                                  (Serializable) mazziere.getPlayers()));
            //  send chat_id list
            mazziere.addBehaviour(new SendAndWait(mazziere.getPlayers(),
                                                  briscola.common.ACLCodes.ACL_SEND_CHAT_ID,
                                                  uniqueKey.toString()));

            mazziere.say("Inviate info giocatori");
            for (Player p : mazziere.getPlayers()) {
                mazziere.say(p.toString());
            }
            sent = true;
        }

        ACLMessage ms = myAgent.receive(MessageTemplate.MatchContent(
            briscola.common.Messages.INFO_RECEIVED));
        if (ms != null) {
            if (conferme.get(ms.getSender()) == null) {
                mazziere.say("Ricevuta conferma da " + ms.getSender());
                conferme.put(ms.getSender(), 1);
                augmetReceived();
            } else {
                mazziere.say("Conferma di " + ms.getSender() + " già ricevuta");
            }
        } else {
            block();
        }

        /*

         // get info from all players
         for (AID p : mazziere.getPlayersAID()) {
         ACLMessage ms = myAgent.receive(MessageTemplate.and(
         MessageTemplate.MatchContent(
         briscola.common.Messages.INFO_RECEIVED),
         MessageTemplate.MatchSender(p)));
         if (ms != null) {
         conferme.put(p, 1);
         augmetReceived();
         mazziere.say("Ricevuta conferma da " + p);
         }
         }*/
    }

    @Override
    public boolean done() {
        if (getReceived() == 5) {
            mazziere.say(
                "Tutti hanno mandato le proprie info. Possiamo procedere.");
            mazziere.addBehaviour(new DistributeHands(mazziere));
            return true;
        } else {
            mazziere.say(
                "Siamo a " + getReceived() + " conferme su 5. Attendendo:");
            for (Player p : mazziere.getPlayers()) {
                if (conferme.get(p) == null) {
                    mazziere.say(p.toString());
                }
            }
            return false;

        }

    }
}
