//--- licenza ---
package briscola.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Hand implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Card> cards;
    private final int CARDS_IN_HAND = 8;
    private int[] distribution;
    private List<Suit> semeLungo;

    public Hand() {
        this.cards = new LinkedList<>();
        this.distribution = null;
        this.semeLungo = null;
    }

    public void addCard(Card card) {
        if (cards.size() >= CARDS_IN_HAND) {
            throw new RuntimeException("Mano piena");
        }
        this.cards.add(card);
    }

    public boolean contains(Card card) {
        for (Card c : cards) {
            if (c.equals(card)) {
                return true;
            }
        }
        return false;
    }

    public Card get(int i) {
        return cards.get(i);
    }

    public int size() {
        return cards.size();
    }

    @Override
    public String toString() {
        String h = new String();
        for (Card c : cards) {
            h += "  " + c;
        }
        return h;
    }

    public Card drawRandom() {
        Random r = new Random();
        return cards.remove(r.nextInt(cards.size()));
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public List<Card> getCards(Suit s) {
        List<Card> list = new LinkedList<>();
        for (Card c : cards) {
            if (c.getSuit().equals(s)) {
                list.add(c);
            }
        }
        return list;
    }

    public int[] getDistribution() {
        if (null == this.distribution) {
            int[] distr = new int[4];
            for (Card c : cards) {
                switch (c.getSuit()) {
                    case SPADES:
                        distr[0]++;
                        break;
                    case HEARTS:
                        distr[1]++;
                        break;
                    case CLUBS:
                        distr[2]++;
                        break;
                    case DIAMONDS:
                        distr[3]++;
                        break;
                    default:
                        return null;
                }
            }
            Arrays.sort(distr);

            for (int i = 0; i < distr.length / 2; i++) {
                int tmp = distr[i];
                distr[i] = distr[distr.length - i - 1];
                distr[distr.length - i - 1] = tmp;
            }

            this.distribution = distr;

        }
        return this.distribution;
    }

    public int getPoints() {
        int p = 0;
        for (Card c : cards) {
            p += c.getRank().getValue();
        }
        return p;
    }

    public List<Suit> getSemeLungo() {

        if (null == this.semeLungo) {

            List<Suit> list = new ArrayList<>();
            HashMap<Suit, Integer> map1 = new HashMap<>();
            final HashMap<Suit, Integer> map2 = new HashMap<>();
            int max = 0;
            int maxpunti = 0;

            for (Card c : cards) {
                Integer n = map1.get(c.getSuit());
                Integer p = map1.get(c.getSuit());
                if (null == n) {
                    map1.put(c.getSuit(), 1);
                    map2.put(c.getSuit(), c.getRank().getValue());
                    if (c.getRank().getValue() > maxpunti) {
                        maxpunti = c.getRank().getValue();
                    }
                } else {
                    map1.put(c.getSuit(), n + 1);
                    map2.put(c.getSuit(), p + c.getRank().getValue());
                    if (n + 1 > max) {
                        max = n + 1;
                    }
                    if (p + c.getRank().getValue() > maxpunti) {
                        maxpunti = p + c.getRank().getValue();
                    }
                }
            }
            for (Suit s : Suit.values()) {
                Integer n = map1.get(s);
                if (n != null && n == max) {
                    list.add(s);
                }
            }
            Comparator<Suit> cmp = new Comparator<Suit>() {
                @Override
                public int compare(Suit t, Suit t1) {
                    Integer n = map2.get(t);
                    Integer m = map2.get(t1);
                    return n.compareTo(m);
                }
            };
            Collections.sort(list, cmp);
            this.semeLungo = list;
        }
        return Collections.unmodifiableList(this.semeLungo);
    }

    public void removeCard(Card c) {
        cards.remove(c);
    }

    public void sort(Suit briscola) {
        Collections.sort(cards, new CardComparator(briscola));
    }

    class CardComparator implements Comparator {

        private Suit briscola;

        public CardComparator(Suit briscola) {
            this.briscola = briscola;
        }

        @Override
        public int compare(Object t, Object t1) {
            if (!(t instanceof briscola.objects.Card && t1 instanceof briscola.objects.Card)) {
                throw new IllegalArgumentException(
                    "Provando a comparare due oggetti non carte");
            }
            Card c = (Card) t;
            Card d = (Card) t1;

            if (c.equals(d)) {
                return 0;
            }
            if (c.getSuit().equals(briscola) && !d.getSuit().equals(briscola)) {
                return 1;
            }
            if (d.getSuit().equals(briscola) && !c.getSuit().equals(briscola)) {
                return -1;
            }
            if (d.getSuit() == c.getSuit()) {
                return c.getValue() - d.getValue();
            }
            return c.getSuit().getN() - d.getSuit().getN();
        }
    }
}
