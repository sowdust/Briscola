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

import briscola.PlayerAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SubscribeBehaviour extends Behaviour {

    /**
     * STATI. 0 nessun tavolo libero. 1 trovato tavolo libero. 2 richiesta
     * spedita a tavolo libero. 3 partecipazione confermata
     *
     */
    private Short state;
    private final PlayerAgent player;

    public SubscribeBehaviour(PlayerAgent player) {
        state = 0;
        this.player = player;
    }
    DFAgentDescription template = new DFAgentDescription();
    ServiceDescription sd = new ServiceDescription();
    AID[] mazzieri;
    FindTable findBeh;
    StopLooking stopL;
    ACLMessage responseMsg = null;
    ACLMessage confirmMsg = null;

    /**
     * get list of mazzieri from yellow pages sends all a "can I join?" request
     */
    public void requestAll() {
        ACLMessage request = new ACLMessage(ACLMessage.QUERY_IF);
        request.setContent(briscola.common.Messages.CAN_I_PLAY);

        sd.setType(briscola.common.Names.MAZZIERE);
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            for (int i = 0; i < result.length; ++i) {
                request.addReceiver(result[i].getName());
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        myAgent.send(request);
    }

    @Override
    public boolean done() {
        return state == 3;
    }

    public void sendReply(ACLMessage responseMsg, ACLMessage confirmMsg) {
        try {

            confirmMsg = responseMsg.createReply();
            confirmMsg.setContent(player.getRealName());
            confirmMsg.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            myAgent.send(confirmMsg);
            state = 2;
            player.say("Reply spedita. State " + state);
        } catch (Exception e) {
            e.printStackTrace();
            state = 0;
        }
    }

    public void getReply(ACLMessage responseMsg, ACLMessage confirmMsg) {
        player.say("Cercando risposte...");
        String content = myAgent.getName() + briscola.common.Messages.CONFIRM_TABLE;

        MessageTemplate confirmation = MessageTemplate.MatchContent(content);
        confirmMsg = myAgent.receive(confirmation);
        if (confirmMsg != null) {
            player.say(player.getAID().getName() + " è definitivamente iscritto");
            state = 3;
        } else {
            player.say("Messaggio conferma non trovato");
            state = 2;
            block();
        }
    }

    @Override
    public void action() {

        switch (state) {
            case 0:

                if (findBeh == null) {
                    findBeh = new FindTable(myAgent, briscola.common.Names.RETRY_EVERY, state);
                    player.addBehaviour(findBeh);
                }
                break;
            case 1:
                player.say("111!!!");
                findBeh.stop();
                sendReply(responseMsg, confirmMsg);
                break;
            case 2:
                getReply(responseMsg, confirmMsg);
                break;
            default:
                break;
        }
    }

    private class StopLooking extends Behaviour {

        /**
         * process to stop
         */
        FindTable process;
        /**
         * variable to check
         */
        Short state;

        public StopLooking(FindTable process, Short state) {
            this.state = state;
            this.process = process;
        }

        @Override
        public void action() {
            System.out.println("stop looking...");
        }

        @Override
        public boolean done() {
            if (state != 0) {
                System.out.println("STOPPED!");

                process.stop();
                return true;
            }
            return false;
        }

    }

    private class FindTable extends TickerBehaviour {

        Short state;
        ACLMessage responseMsg;
        ACLMessage confirmMsg = null;
        MessageTemplate response;

        public FindTable(Agent a, long period, Short state) {
            super(a, period);
            this.state = state;
            response = MessageTemplate.MatchContent(briscola.common.Messages.YOU_CAN_PLAY);
        }

        @Override
        public void onStart() {
            requestAll();
        }

        @Override
        protected void onTick() {

            responseMsg = myAgent.receive(response);
            if (responseMsg != null) {
                player.say(player.getAID().getName() + " ha trovato un tavolo libero");
                state = 1;
                sendReply(responseMsg, confirmMsg);
                this.stop();
            } else {
                player.say(player.getAID().getName() + " nessun tavolo libero. Riprovo.");
                block();
            }

            requestAll();
        }

    }

}
