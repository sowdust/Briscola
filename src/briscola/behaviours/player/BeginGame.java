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

    boolean done;
    PlayerAgent player;

    public BeginGame(PlayerAgent player) {
        this.player = player;
        this.done = false;
    }

    @Override
    public void action() {

        MessageTemplate info = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage infoMsg = myAgent.receive(info);
        if (infoMsg != null) {
            try {
                List<Player> players = (List<Player>) infoMsg.getContentObject();
                player.setPlayers(players);
                player.say("Ricevute info giocatori");
                done = true;
            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return done;
    }

}
