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
package briscola.behaviours;

import briscola.Player;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendMessage extends OneShotBehaviour {

    private final List<Player> rcp;
    private final int type;
    private String content = null;
    private Object ob = null;

    public SendMessage(List<Player> rcp, String content) {
        this.rcp = rcp;
        this.type = ACLMessage.UNKNOWN;
        this.content = content;
    }

    public SendMessage(List<Player> rcp, int type, String content) {
        this.rcp = rcp;
        this.type = type;
        this.content = content;
    }

    public SendMessage(List<Player> rcp, int type, Serializable content) {
        this.rcp = rcp;
        this.type = type;
        this.ob = content;
    }

    public SendMessage(Player rcp, String content) {
        List<Player> l = new ArrayList<>();
        l.add(rcp);
        this.rcp = l;
        this.type = ACLMessage.UNKNOWN;
        this.content = content;
    }

    public SendMessage(Player rcp, int type, String content) {
        List<Player> l = new ArrayList<>();
        l.add(rcp);
        this.rcp = l;
        this.type = type;
        this.content = content;
    }

    @Override
    public void action() {
        ACLMessage m = new ACLMessage(type);
        for (Player p : rcp) {
            m.addReceiver(p.getAID());
        }
        if (ob == null) {
            m.setContent(content);
        } else {
            try {
                m.setContentObject((Serializable) ob);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        myAgent.send(m);
    }
}
