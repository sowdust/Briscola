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
package briscola.objects;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    public static final int CARDS_IN_DECK = 52;
    private final List<Card> cards;

    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.load();
        //deck.show();
    }

    public Deck() {
        this.cards = new ArrayList<>(CARDS_IN_DECK);
    }

    public void show() {
        for (Card c : cards) {
            System.out.println(c);
        }
    }

    public void load() {
        int i = 0;
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                cards.add(new Card(r, s));
            }
        }
    }

    public void mischia() {
        // Collection.shuffle(cards);
    }
}
