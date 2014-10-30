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
import briscola.behaviours.SendMessage;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import java.io.Serializable;

/**
 *
 * @author mat
 */
public class BeginGame extends Behaviour {

    private MazziereAgent mazziere;
    private boolean sent;

    public BeginGame(MazziereAgent mazziere) {
        this.mazziere = mazziere;
        this.sent = false;
    }

    @Override
    public void action() {
        //  send info to all players
        if (!sent) {
            myAgent.addBehaviour(new SendMessage(mazziere.getPlayers(), ACLMessage.INFORM, (Serializable) mazziere.getPlayers()));
            mazziere.say("Inviate info giocatori");
            sent = true;
        }
        // receive confirmation from all players
    }

    @Override
    public boolean done() {
        return false;
    }

}
