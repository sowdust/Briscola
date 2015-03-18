package briscola.objects;

import java.io.Serializable;
import static briscola.common.Names.CARD_IMG_EXTENSION;
import static briscola.common.Names.CARD_IMG_FOLDER;
import java.util.Objects;

public class Card implements Comparable, Serializable {

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

    public int getPosition() {
        return rank.getPosition();
    }

    @Override
    public String toString() {
        return rank + " " + briscola.common.Names.CARD_OF + " " + suit;
    }

    @Override
    public boolean equals(Object c) {
        if (c == null) {
            return false;
        }
        if (!(c instanceof briscola.objects.Card)) {
            System.out.println("Qualcosa di molto strano!");
            System.out.println("Ricevuta " + c + " al posto di card");
            return false;
        }
        return ((Card) c).getRank().equals(this.rank) && ((Card) c).getSuit().equals(
            this.suit);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.rank);
        hash = 59 * hash + Objects.hashCode(this.suit);
        return hash;
    }

    @Override
    public int compareTo(Object t) {
        if (this.equals(t)) {
            return 0;
        }
        return -1;
    }

}
