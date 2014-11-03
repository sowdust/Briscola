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
package briscola;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class GeneralAgent extends Agent {

    protected String name;
    protected List<Player> players;
    protected GeneralGUI gui;
    protected AID mazziereAID;
    private String chatID;

    public void setChatID(String uniqueKey) {
        this.chatID = uniqueKey;
    }

    public String getChatID() {
        return chatID;
    }

    public void say(String s) {
        gui.say(s);
    }

    public void printChat(AID player, String s) {
        int i = 0;
        for (Player p : players) {
            if (p.getAID().equals(player)) {
                gui.appendChat(p.getName(), s, i);
                return;
            }
            ++i;
        }
        if (player.equals(mazziereAID)) {
            gui.appendChat(briscola.common.Names.MAZZIERE, s, i);

            return;
        }
        gui.appendChat(briscola.common.Names.UNKNOWN, s, ++i);
    }

    /**
     * sends to all players a message with CFP: ACL_CHAT CONV.ID: As sent by
     * mazziere
     *
     * @param s message to be sent
     */
    public void sendChat(String s) {
        ACLMessage m = new ACLMessage(briscola.common.Names.ACL_CHAT);
        m.setConversationId(getChatID());
        m.setContent(s);
        if (players == null) {
            say("Nessun giocatore in lista");
        } else {
            for (Player p : players) {
                m.addReceiver(p.getAID());
            }
            send(m);
        }
    }

    public void sendMessage(List<Player> rcp, int type, String content) {
        ACLMessage m = new ACLMessage(type);
        for (Player p : rcp) {
            m.addReceiver(p.getAID());
        }
        m.setContent(content);
        m.setPerformative(type);
        send(m);
    }

    public void sendMessage(Player rcp, int type, String content) {
        ACLMessage m = new ACLMessage(type);
        m.addReceiver(rcp.getAID());
        m.setContent(content);
        m.setPerformative(type);
        send(m);
    }

    public void sendMessage(AID rcp, int type, String content) {
        ACLMessage m = new ACLMessage(type);
        m.addReceiver(rcp);
        m.setContent(content);
        m.setPerformative(type);
        send(m);
    }

    public void sendMessage(List<Player> rcp, int type, Serializable content) throws IOException {
        ACLMessage m = new ACLMessage(type);
        for (Player p : rcp) {
            m.addReceiver(p.getAID());
        }
        m.setContentObject((Serializable) content);
        m.setPerformative(type);
        send(m);
    }

    public void sendMessage(Player rcp, int type, Serializable content) throws IOException {
        ACLMessage m = new ACLMessage(type);
        m.addReceiver(rcp.getAID());
        m.setContentObject((Serializable) content);
        m.setPerformative(type);
        send(m);
    }
}
