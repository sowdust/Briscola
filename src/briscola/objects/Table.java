/*
 */
package briscola.objects;

import briscola.Player;

/**
 * Class that represents the table
 *
 * @author mat
 */
public class Table {

    private final Deck deck;
    private Suit briscola;
    private Player giaguaro;

    public Table() {
        deck = new Deck();
    }

    public void setBriscola(Suit r) {
        briscola = r;
    }

    public void setGiaguaro(Player giaguaro) {
        this.giaguaro = giaguaro;
    }

    public Player getGiaguaro() {
        return giaguaro;
    }

    public Deck getDeck() {
        return deck;
    }

}
