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

    public Table() {
        deck = new Deck();
    }

    public Deck getDeck() {
        return deck;
    }

}
