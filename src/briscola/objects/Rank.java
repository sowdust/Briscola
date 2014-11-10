/*
 */
package briscola.objects;

public enum Rank {

    ACE(1),
    DEUCE(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    JACK(8),
    QUEEN(9),
    KING(10);

    private int order;

    Rank(int order) {
        this.order = order;
    }

    public int getN() {
        return order;
    }
}
