/*
 */
package briscola.objects;

public enum Suit {

    CLUBS(1),
    DIAMONDS(2),
    SPADES(3),
    HEARTS(4);

    private int order;

    Suit(int order) {
        this.order = order;
    }

    public int getN() {
        return order;
    }
}
