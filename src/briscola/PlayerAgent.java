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
import java.util.List;

/**
 *
 * @author mat
 */
public class PlayerAgent extends GeneralAgent {

    private boolean visible;

    @Override
    protected void setup() {

        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            name = (String) args[0];
            visible = true;
        } else {
            visible = false;
            name = briscola.common.Names.randomName();
        }

        gui = new PlayerGUI(this);
        gui.setVisible(visible);

        say("Giocatore " + getAID().getName() + " iscritto alla piattaforma");
        addBehaviour(new SubscribeBehaviour(this));
    }

    public String getRealName() {
        return name;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        for (Player p : players) {
            gui.addPlayer(p);
        }
    }

    public void setMazziereAID(AID sender) {
        this.mazziereAID = sender;
    }

    public AID getMazziereAID() {
        return mazziereAID;
    }
}
