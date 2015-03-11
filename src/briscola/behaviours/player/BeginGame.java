/*
 */
package briscola.behaviours.player;

import briscola.Player;
import briscola.PlayerAgent;
import briscola.behaviours.GetChatMessage;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.List;

public class BeginGame extends Behaviour {

    private static final long serialVersionUID = 1L;

    short received;
    PlayerAgent player;

    public BeginGame(PlayerAgent player) {
        this.player = player;
        this.received = 0;
    }

    @Override
    public void action() {

        //  template for message containing players list
        ACLMessage infoPlayersMsg = myAgent.receive(MessageTemplate.and(
            MessageTemplate.MatchPerformative(
                briscola.common.ACLCodes.ACL_SEND_PLAYERS),
            MessageTemplate.MatchSender(player.getMazziereAID())));

        // template for message containing chat conversation-id
        ACLMessage infoChatMsg = myAgent.receive(MessageTemplate.and(
            MessageTemplate.MatchPerformative(
                briscola.common.ACLCodes.ACL_SEND_CHAT_ID),
            MessageTemplate.MatchSender(player.getMazziereAID())));

        if (infoPlayersMsg != null) {
            try {
                List<Player> players = (List<Player>) infoPlayersMsg.getContentObject();
                if (players.size() < 5) {
                    throw new RuntimeException(
                        "Non ho ricevuto tutti i giocatori!");
                }
                player.setPlayers(players);
                player.say("Ricevute info giocatori");
                ++received;
            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }
        } else {
            block();
        }

        if (infoChatMsg != null) {
            player.setChatID(infoChatMsg.getContent());
            player.say("Ricevute info chat");
            myAgent.addBehaviour(new GetChatMessage(player));
            ++received;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        if (received == 2) {
            //  send confirm : we have received all info
            player.sendMessage(player.getMazziereAID(), ACLMessage.CONFIRM,
                               briscola.common.Messages.INFO_RECEIVED);
            myAgent.addBehaviour(new ReceiveHand(player));
            return true;
        }
        return false;
    }
}
