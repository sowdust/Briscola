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
import briscola.objects.Card;
import briscola.objects.Deck;
import jade.core.behaviours.Behaviour;
import java.util.List;

/**
 *
 * @author mat
 */
public class ManageBid extends Behaviour {

    MazziereAgent mazziere;
    Deck deck;
    boolean visto = false;

    public ManageBid(MazziereAgent mazziere) {
        this.mazziere = mazziere;
    }

    @Override
    public void action() {

        deck = mazziere.getTable().getDeck();
        deck.shuffle();
    }

    public void distrDeck() {
        List<Card>[] p = new List[5];
    }

    @Override
    public boolean done() {
        throw new RuntimeException();
    }

}
