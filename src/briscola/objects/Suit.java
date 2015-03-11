/*
 */
package briscola.objects;

import static briscola.common.Names.CARD_CLUBS;
import static briscola.common.Names.CARD_DIAMONDS;
import static briscola.common.Names.CARD_HEARTS;
import static briscola.common.Names.CARD_SPADES;
import java.io.Serializable;

public enum Suit implements Serializable {

    CLUBS(1, CARD_CLUBS),
    DIAMONDS(2, CARD_DIAMONDS),
    SPADES(3, CARD_SPADES),
    HEARTS(4, CARD_HEARTS);

    private int order;
    private String name;

    Suit(int order, String name) {
        this.order = order;
        this.name = name;
    }

    public int getN() {
        return order;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean equals(Suit s) {
        return s.getN() == this.order;
    }
}
