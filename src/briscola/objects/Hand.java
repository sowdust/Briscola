//--- licenza ---
package briscola.objects;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Hand implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Card> cards;
    private final int CARDS_IN_HAND = 8;

    public Hand()
    {
        this.cards = new LinkedList<>();
    }

    public void addCard(Card card)
    {
        if (cards.size() >= CARDS_IN_HAND)
        {
            throw new RuntimeException("Mano piena");
        }
        this.cards.add(card);
    }

    @Override
    public String toString()
    {
        String h = new String();
        for (Card c : cards)
        {
            h += " " + c;
        }
        return h;
    }

}
