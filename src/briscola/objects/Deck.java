/*
 */
package briscola.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    public static final int CARDS_IN_DECK = 40;
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>(CARDS_IN_DECK);
        this.load();
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

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int size() {
        return CARDS_IN_DECK;
    }

    public Card get(int position) {
        return cards.get(position);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

}
