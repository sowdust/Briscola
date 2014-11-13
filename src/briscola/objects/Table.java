/*
 */
package briscola.objects;

/**
 * Class that represents the table
 *
 * @author mat
 */
public class Table {

    private final Deck deck;
    private Suit briscola;

    public Table() {
        deck = new Deck();
    }

    public void setBriscola(Suit r) {
        briscola = r;
    }

    public Deck getDeck() {
        return deck;
    }

}
