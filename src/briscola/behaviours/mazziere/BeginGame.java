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
package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
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

    private final MazziereAgent mazziere;
    private boolean sent;
    private ACLMessage[] confMsg;

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

            try {
                //  send players list
                mazziere.sendMessage(mazziere.getPlayers(), briscola.common.Names.ACL_SEND_PLAYERS, (Serializable) mazziere.getPlayers());
                //  send chat_id list
                mazziere.sendMessage(mazziere.getPlayers(), briscola.common.Names.ACL_SEND_CHAT_ID, uniqueKey);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            mazziere.say("Inviate info giocatori");
            sent = true;
        }

        // get info from all players
        confMsg = new ACLMessage[5];
        int i = 0;
        for (AID p : mazziere.getPlayersAID()) {
            confMsg[i++] = myAgent.receive(MessageTemplate.and(MessageTemplate.MatchContent(briscola.common.Messages.INFO_RECEIVED), MessageTemplate.MatchSender(p)));

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
        return true;
    }

}
