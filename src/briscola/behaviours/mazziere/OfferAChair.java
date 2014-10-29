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

import briscola.behaviours.mazziere.WaitForSubscriptionConfirmation;
import briscola.MazziereAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class OfferAChair extends OneShotBehaviour {

    AID requester;
    Integer requests;
    ACLMessage originalMsg;
    MazziereAgent mazziere;

    public OfferAChair(MazziereAgent mazziere, ACLMessage originalMsg, AID agent, Integer requests) {
        this.requester = agent;
        this.requests = requests;
        this.originalMsg = originalMsg;
        this.mazziere = mazziere;
    }

    @Override
    public void action() {
        mazziere.say("Posizione offerta a " + requester.getName());
        ++requests;
        ACLMessage proposal = originalMsg.createReply();
        proposal.setContent(briscola.common.Messages.YOU_CAN_PLAY);
        myAgent.send(proposal);
        myAgent.addBehaviour(new WaitForSubscriptionConfirmation(mazziere, requests, requester));
    }

}
