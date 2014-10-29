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

import briscola.behaviours.mazziere.OfferAChair;
import briscola.MazziereAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;

/**
 * Behaviour the mazziere keeps while he's waiting for 5 players to join the
 * table
 */
public class OpenTable extends Behaviour {

    private final Integer requests;
    private final HashMap<AID, Long> requesters;
    private final MazziereAgent mazziere;

    public OpenTable(MazziereAgent mazziere) {
        this.mazziere = mazziere;
        mazziere.say("Preparo e registro subito il tavolo");
        requesters = new HashMap<>();
        requests = 0;
    }

    @Override
    public void action() {

        if (requests + ((MazziereAgent) myAgent).getPlayers().size() < 5) {
            MessageTemplate request = MessageTemplate.MatchContent(briscola.common.Messages.CAN_I_PLAY);
            ACLMessage requestMsg = myAgent.receive(request);

            if (requestMsg != null) {
                AID agentName = requestMsg.getSender();
                if (((MazziereAgent) myAgent).getPlayers().contains(agentName)) {
                    mazziere.say("Agente " + agentName.getName() + " già iscritto");
                    return;
                }
                Long reqTime = requesters.get(agentName);
                //  se agente sconosciuto
                if (reqTime == null || ((System.currentTimeMillis() - reqTime) > briscola.common.Names.WAIT_FOR_CONFIRMATION)) {
                    requesters.put(agentName, System.currentTimeMillis());
                    myAgent.addBehaviour(new OfferAChair(mazziere, requestMsg, agentName, requests));
                }

            } else {
                block();
            }
        } else {
            mazziere.say("Siamo già a 5 richieste..");
        }
    }

    @Override
    public boolean done() {
        if (((MazziereAgent) myAgent).getPlayers().size() == 5) {
            mazziere.getDFA().removeServices(mazziere.getServiceDesc());
            mazziere.say("Tavolo al completo");
            // addBehaviour(iniziaPartita);
            return true;
        }
        return false;
    }

}
