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
    private String chatID;

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

    public void setChatID(String uniqueKey) {
        this.chatID = uniqueKey;
    }
}
