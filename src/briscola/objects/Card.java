/*
 */
package briscola.objects;

import java.io.Serializable;

public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Rank rank;
    private final Suit suit;

    public Card(Rank r, Suit s)
    {
        rank = r;
        suit = s;
    }

    @Override
    public String toString()
    {
        return rank + " of " + suit;
    }
}
