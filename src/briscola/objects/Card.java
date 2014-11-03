/*
 */
package briscola.objects;

public class Card {

    private Rank rank;
    private Suit suit;

    public Card(Rank r, Suit s) {
        rank = r;
        suit = s;
    }

    public String toString() {
        return rank + " of " + suit;
    }
}
