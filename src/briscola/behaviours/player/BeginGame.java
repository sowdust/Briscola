/*
 * Copyright (C) 2014 mat
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

    short received;
    PlayerAgent player;

    public BeginGame(PlayerAgent player) {
        this.player = player;
        this.received = 0;
    }

    @Override
    public void action() {

        //  template for message containing players list
        ACLMessage infoPlayersMsg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(briscola.common.Names.ACL_SEND_PLAYERS), MessageTemplate.MatchSender(player.getMazziereAID())));

        // template for message containing chat conversation-id
        ACLMessage infoChatMsg = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchPerformative(briscola.common.Names.ACL_SEND_CHAT_ID), MessageTemplate.MatchSender(player.getMazziereAID())));

        if (infoPlayersMsg != null) {
            try {
                List<Player> players = (List<Player>) infoPlayersMsg.getContentObject();
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
            player.say("Ricevute info chat: " + infoChatMsg.getContent());
            ++received;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        if (received == 2) {
            //  send confirm : we have received all info
            player.sendMessage(player.getMazziereAID(), ACLMessage.CONFIRM, briscola.common.Messages.INFO_RECEIVED);
            return true;
        }
        return false;
    }
}
