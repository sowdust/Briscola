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
import static jade.lang.acl.MessageTemplate.MatchPerformative;

/**
 * Behaviours used by Mazziere.
 *
 * @author mat
 */
public class WaitForSubscriptionConfirmation extends Behaviour {

    private final long start;
    private boolean done;
    private Integer requests;
    private final AID agent;
    private final MazziereAgent mazziere;

    public WaitForSubscriptionConfirmation(MazziereAgent mazziere, Integer requests, AID agent) {
        this.start = System.currentTimeMillis();
        this.requests = requests;
        this.mazziere = mazziere;
        this.agent = agent;
    }

    @Override
    public void action() {
        mazziere.say("In attesa di conferma da " + agent.getName());
        MessageTemplate confirmation = MessageTemplate.and(MessageTemplate.MatchSender(agent), MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));

        ACLMessage confirmationMsg = myAgent.receive(confirmation);
        if (confirmationMsg != null) {
            --requests;
            ((MazziereAgent) myAgent).addPlayer(agent, confirmationMsg.getContent());
            ACLMessage confirmTable = confirmationMsg.createReply();
            String content = agent.getName() + briscola.common.Messages.CONFIRM_TABLE;
            confirmTable.setContent(content);
            myAgent.send(confirmTable);
            mazziere.say("Aggiunto giocatore # " + ((MazziereAgent) myAgent).getPlayersAID().size() + ": " + agent.getName());
            done = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        if (done) {
            return true;
        }
        if ((System.currentTimeMillis() - start) > briscola.common.Names.WAIT_FOR_CONFIRMATION) {
            --requests;
            return true;
        }
        return false;
    }

}
