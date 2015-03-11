/*
 */
package briscola.objects;

import static briscola.common.Names.*;

public enum Rank {

    ACE(1, CARD_ACE, 11, 10),
    DEUCE(2, CARD_DEUCE, 0, 1),
    THREE(3, CARD_THREE, 10, 9),
    FOUR(4, CARD_FOUR, 0, 2),
    FIVE(5, CARD_FIVE, 0, 3),
    SIX(6, CARD_SIX, 0, 4),
    SEVEN(7, CARD_SEVEN, 0, 5),
    JACK(8, CARD_JACK, 2, 6),
    QUEEN(9, CARD_QUEEN, 3, 7),
    KING(10, CARD_KING, 4, 8);

    private final int order;
    private final String name;
    private final int value;
    private final int position;

    Rank(int order, String name, int value, int position) {
        this.order = order;
        this.name = name;
        this.value = value;
        this.position = position;
    }

    public int getN() {
        return order;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getPosition() {
        return position;
    }

    public boolean equals(Rank r) {
        return r.getPosition() == position;
    }
}
