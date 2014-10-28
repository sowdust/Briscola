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

import briscola.common.Tavolo;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.MatchPerformative;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Agent that manages the whole game without taking part in it His duties are:
 * collecting 5 players assigning them an order around the table assigning cards
 * declaring the bid open managing the bid collect a bid or a pass checking they
 * are valid declaring the bid closed declaring the winner ask the winner the
 * briscola declaring the briscola managing the game assign an order for the
 * hand collect cards declare winner of the hand counting points declaring the
 * winner
 *
 * @author mat
 */
public class MazziereAgent extends Agent {

    private String name;
    private Tavolo table;
    private List<Player> players;
    private MazziereGUI gui;
    private DFAgentDescription dfd;
    private ServiceDescription sd;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        players = new ArrayList<>();

        if (args != null && args.length > 0) {
            name = (String) args[0];
        } else {
            name = "Gianni";
        }

        gui = new MazziereGUI(this);
        gui.setVisible(true);

        say("Mazziere " + name + " al suo servizio");

        //  REGISTER TO YELLOW PAGES
        dfd = new DFAgentDescription();
        dfd.setName(getAID());
        sd = new ServiceDescription();
        sd.setType(briscola.common.Names.MAZZIERE);
        sd.setName(name);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            say("Errore durante la registrazione alle pagine gialle");
            fe.printStackTrace();
        }
        addBehaviour(new ApriTavoloBehaviour());
    }

    String getRealName() {
        return name;
    }

    void addPlayer(AID agente, String name) {
        Player player = new Player(agente, name);
        players.add(player);
        gui.addPlayer(player);
        if (players.size() == 5) {
            dfd.removeServices(sd);
            // GIOCA PARTITA addBehaviour()
        }
    }

    protected void takeDown() {
        say("Felice di aver giocato con voi. Addio!");
        gui.dispose();
        dfd.removeServices(sd);
        super.takeDown();
    }

    List<AID> getPlayers() {
        List<AID> r = new ArrayList();
        for (Player p : players) {
            r.add(p.getAID());
        }
        return r;
    }

    void say(String s) {
        gui.addLine(s);
    }

    /**
     * Behaviour the mazziere keeps while he's waiting for 5 players to join the
     * table
     */
    private class ApriTavoloBehaviour extends Behaviour {

        private final Integer requests;
        private final HashMap<AID, Long> requesters;

        public ApriTavoloBehaviour() {
            say("Preparo e registro subito il tavolo");
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
                        say("Agente " + agentName.getName() + " già iscritto");
                        return;
                    }
                    Long reqTime = requesters.get(agentName);
                    //  se agente sconosciuto
                    if (reqTime == null || ((System.currentTimeMillis() - reqTime) > briscola.common.Names.WAIT_FOR_CONFIRMATION)) {
                        requesters.put(agentName, System.currentTimeMillis());
                        myAgent.addBehaviour(new OffriPosizioneBehaviour(requestMsg, agentName, requests));
                    }

                } else {
                    block();
                }
            } else {
                say("Siamo già a 5 richieste..");
            }
        }

        @Override
        public boolean done() {
            return ((MazziereAgent) myAgent).getPlayers().size() == 5;
        }

    }

    private class OffriPosizioneBehaviour extends OneShotBehaviour {

        AID requester;
        Integer requests;
        ACLMessage originalMsg;

        public OffriPosizioneBehaviour(ACLMessage originalMsg, AID agent, Integer requests) {
            this.requester = agent;
            this.requests = requests;
            this.originalMsg = originalMsg;
        }

        @Override
        public void action() {
            say("Posizione offerta a " + requester.getName());
            ++requests;
            ACLMessage proposal = originalMsg.createReply();
            proposal.setContent(briscola.common.Messages.YOU_CAN_PLAY);
            myAgent.send(proposal);
            myAgent.addBehaviour(new WaitForConfirmationBehaviour(requests, requester));
        }

    }

    private class WaitForConfirmationBehaviour extends Behaviour {

        private long start;
        private boolean done;
        private Integer requests;
        private AID agent;

        public WaitForConfirmationBehaviour(Integer requests, AID agent) {
            this.start = System.currentTimeMillis();
            this.requests = requests;
            this.agent = agent;
        }

        @Override
        public void action() {
            say("In attesa di conferma da " + agent.getName());
            MessageTemplate confirmation = MessageTemplate.and(MessageTemplate.MatchSender(agent), MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));

            //MessageTemplate confirmation = MessageTemplate.MatchContent(briscola.common.Messages.CONFIRM_PLAYER);
            ACLMessage confirmationMsg = myAgent.receive(confirmation);
            if (confirmationMsg != null) {
                --requests;
                ((MazziereAgent) myAgent).addPlayer(agent, confirmationMsg.getContent());
                ACLMessage confirmTable = confirmationMsg.createReply();
                String content = agent.getName() + briscola.common.Messages.CONFIRM_TABLE;
                confirmTable.setContent(content);
                myAgent.send(confirmTable);
                say("Aggiunto giocatore # " + ((MazziereAgent) myAgent).getPlayers().size() + ": " + agent.getName());
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

}
