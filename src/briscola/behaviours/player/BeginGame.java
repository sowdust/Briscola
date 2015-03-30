/*
 */
package briscola.behaviours.player;

import briscola.Player;
import briscola.PlayerAgent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.List;

public class BeginGame extends Behaviour {

    private static final long serialVersionUID = 1L;

    private int received;
    PlayerAgent player;

    public BeginGame(PlayerAgent player) {
        this.player = player;
        this.received = 0;
    }

    synchronized private void augmentReceived() {
        ++received;
    }

    synchronized private int getReceived() {
        return received;
    }

    @Override
    public void action() {

        //  template for message containing players list
        MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchPerformative(
                briscola.common.ACLCodes.ACL_SEND_PLAYERS),
            MessageTemplate.MatchSender(player.getMazziereAID()));
        ACLMessage infoPlayersMsg = myAgent.receive(mt);

        // template for message containing chat conversation-id
        MessageTemplate mc = MessageTemplate.and(
            MessageTemplate.MatchPerformative(
                briscola.common.ACLCodes.ACL_SEND_CHAT_ID),
            MessageTemplate.MatchSender(player.getMazziereAID()));
        ACLMessage infoChatMsg = myAgent.receive(mc);

        if (infoPlayersMsg != null) {
            try {
                List<Player> players = (List<Player>) infoPlayersMsg.getContentObject();
                if (players.size() < 5) {
                    throw new RuntimeException(
                        "Non ho ricevuto tutti i giocatori!");
                }
                player.setPlayers(players);
                player.say("Ricevute info giocatori. Gioco con:");
                for (Player p : players) {
                    player.say(p.toString());
                }
                augmentReceived();
                infoPlayersMsg = null;
            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }
        } else {
            player.say("attendendo info giocatori ");
            block();
        }

        if (infoChatMsg != null) {
            player.setChatID(infoChatMsg.getContent());
            player.say("Settate info chat " + player.getChatID());
            player.startChat();
            augmentReceived();
        } else {
            player.say("attendendo info chat ");
            block();
        }
    }

    @Override
    public boolean done() {
        if (getReceived() == 2) {
            //  send confirm : we have received all info
            player.sendMessage(player.getMazziereAID(), ACLMessage.CONFIRM,
                               briscola.common.Messages.INFO_RECEIVED);
            player.say("Spedendo conferma");
            player.addBehaviour(new ReceiveHand(player));
            return true;
        }
        return false;
    }
}
