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

import briscola.behaviours.player.SubscribeBehaviour;
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
public class PlayerAgent extends Agent {

    private String name;
    private PlayerGiocatoreGUI gui;

    @Override
    protected void setup() {

        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            name = (String) args[0];
        } else {
            name = "Random Name";
        }

        gui = new PlayerGiocatoreGUI(this);
        gui.setVisible(true);

        say("Giocatore " + getAID().getName() + " iscritto alla piattaforma");
        addBehaviour(new SubscribeBehaviour(this));
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
}
