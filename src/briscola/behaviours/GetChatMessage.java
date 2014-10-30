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

import briscola.GeneralAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author mat
 */
public class GetChatMessage extends CyclicBehaviour {

    GeneralAgent agent;

    public GetChatMessage(GeneralAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {

        MessageTemplate info = MessageTemplate.MatchConversationId(agent.getChatID());
        ACLMessage chatMsg = myAgent.receive(info);
        if (chatMsg != null) {
            agent.say(chatMsg.getContent());
        } else {
            block();
        }
    }

}
