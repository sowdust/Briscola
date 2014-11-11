/*
 */
package briscola.objects;

import static briscola.common.Names.*;

public enum Rank {

    ACE(1, CARD_ACE, 11),
    DEUCE(2, CARD_DEUCE, 0),
    THREE(3, CARD_THREE, 10),
    FOUR(4, CARD_FOUR, 0),
    FIVE(5, CARD_FIVE, 0),
    SIX(6, CARD_SIX, 0),
    SEVEN(7, CARD_SEVEN, 0),
    JACK(8, CARD_JACK, 2),
    QUEEN(9, CARD_QUEEN, 3),
    KING(10, CARD_KING, 4);

    private int order;
    private String name;
    private int value;

    Rank(int order, String name, int value) {
        this.order = order;
        this.name = name;
        this.value = value;
    }

    public int getN() {
        return order;
    }

    @Override
    public String toString() {
        return name;
    }
}
