/*
 */
package briscola.objects;

import java.io.Serializable;
import static briscola.common.Names.CARD_IMG_EXTENSION;
import static briscola.common.Names.CARD_IMG_FOLDER;

public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Rank rank;
    private final Suit suit;

    public Card(Rank r, Suit s) {
        rank = r;
        suit = s;
    }

    public String getImage() {
        return CARD_IMG_FOLDER + rank.getN() + "-" + suit.getN() + CARD_IMG_EXTENSION;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank + " " + briscola.common.Names.CARD_OF + " " + suit;
    }
}
