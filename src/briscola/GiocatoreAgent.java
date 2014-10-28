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
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author mat
 */
public class GiocatoreAgent extends Agent {

    private Short state = 0;
    private String name;
    private GiocatoreGUI gui;

    @Override
    protected void setup() {

        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            name = (String) args[0];
        } else {
            name = "Random Name";
        }

        gui = new GiocatoreGUI(this);
        gui.setVisible(true);

        say("Giocatore " + getAID().getName() + " iscritto alla piattaforma");
        addBehaviour(new IscrivitiBehaviour(state));
    }

    public String getRealName() {
        return name;
    }

    public void say(String s) {
        gui.say(s);
    }

    /**
     * The player looks for a table that is missing players and registers to it
     * by its name
     */
    private class IscrivitiBehaviour extends Behaviour {

        /**
         * STATI. 0 nessun tavolo libero. 1 trovato tavolo libero. 2 richiesta
         * spedita a tavolo libero. 3 partecipazione confermata
         *
         */
        private Short state;

        public IscrivitiBehaviour(Short state) {
            this.state = state;
        }
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        AID[] mazzieri;

        /**
         * get list of mazzieri from yellow pages sends all a "can I join?"
         * request
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
                confirmMsg.setContent(name);
                confirmMsg.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                myAgent.send(confirmMsg);
                state = 2;
                say("Reply spedita. State " + state);
            } catch (Exception e) {
                e.printStackTrace();
                state = 0;
            }
        }

        public void getReply(ACLMessage responseMsg, ACLMessage confirmMsg) {
            say("Cercando risposte...");
            String content = myAgent.getName() + briscola.common.Messages.CONFIRM_TABLE;

            MessageTemplate confirmation = MessageTemplate.MatchContent(content);
            confirmMsg = myAgent.receive(confirmation);
            if (confirmMsg != null) {
                say(getAID().getName() + " è definitivamente iscritto");
                state = 3;
            } else {
                say("Messaggio conferma non trovato");
                state = 2;
                block();
            }
        }

        @Override
        public void action() {

            ACLMessage responseMsg = null;
            ACLMessage confirmMsg = null;

            switch (state) {
                case 0:
                    requestAll();
                    MessageTemplate response = MessageTemplate.MatchContent(briscola.common.Messages.YOU_CAN_PLAY);
                    responseMsg = myAgent.receive(response);
                    if (responseMsg != null) {
                        say(getAID().getName() + " ha trovato un tavolo libero");
                        state = 1;
                        sendReply(responseMsg, confirmMsg);
                    } else {
                        say(getAID().getName() + " nessun tavolo libero. Riprovo.");
                        block();
                    }
                    break;
                case 1:
                    sendReply(responseMsg, confirmMsg);
                    break;
                case 2:
                    getReply(responseMsg, confirmMsg);
                    break;
                default:
                    break;
            }
        }

    }
}
