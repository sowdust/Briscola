//--- licenza ---
package briscola.objects;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Hand implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Card> cards;
    private final int CARDS_IN_HAND = 8;

    public Hand() {
        this.cards = new LinkedList<>();
    }

    public void addCard(Card card) {
        if (cards.size() >= CARDS_IN_HAND) {
            throw new RuntimeException("Mano piena");
        }
        this.cards.add(card);
    }

    public Card get(int i) {
        return cards.get(i);
    }

    public int size() {
        return cards.size();
    }

    @Override
    public String toString() {
        String h = new String();
        for (Card c : cards) {
            h += " " + c;
        }
        return h;
    }

    public Card drawRandom() {
        Random r = new Random();
        return cards.remove(r.nextInt(cards.size()));
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
