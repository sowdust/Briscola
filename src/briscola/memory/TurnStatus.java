package briscola.memory;

import briscola.Player;
import briscola.objects.Card;
import jade.core.AID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TurnStatus {

    private List<Player> players;
    private List<Mano> giocate;
    //  # of people who have played in this turn
    private int[] counter;
    //  # of turn in the game [1..8]
    private int mano;
    //  index of next player in list
    private int next;

    public TurnStatus(List<Player> players, Player first) {
        this.players = players;
        this.giocate = new ArrayList<>();
        this.counter = new int[8];
        this.mano = -1;

        for (int i = 0; i < 8; ++i) {
            this.giocate.add(new Mano(i));
        }

        for (int i = 0; i < players.size(); ++i) {
            if (players.get(i).getAID().equals(first.getAID())) {
                next = i;
                break;
            }
        }
        initMano(next);
    }

    public boolean initMano(Player p) {
        for (int i = 0; i < players.size(); ++i) {
            if (p.getAID().equals(players.get(i).getAID())) {
                return initMano(i);
            }
        }
        throw new IllegalArgumentException();
    }

    public boolean initMano(int next) {
        if (mano == 7) {
            return false;
        }
        this.next = next;
        ++mano;
        this.counter[mano] = 0;
        return true;
    }

    public synchronized Player getNext() {
        if (next < 0) {
            return null;
        }
        if (next >= players.size()) {
            throw new RuntimeException();
        }
        return players.get(next);
    }

    /**
     * @return the giocate
     */
    public List<Mano> getGiocate() {
        return Collections.unmodifiableList(giocate);
    }

    public int getCounter() {
        return counter[mano];
    }

    public int getMano() {
        return mano;
    }

    public Mano getCurrentMano() {
        return giocate.get(mano);
    }

    public void addGiocata(Player player, Card card, int mano) {
        if (counter[mano] >= 5) {
            throw new IllegalArgumentException("Già 5 giocate");
        }
        giocate.get(mano).add(player, card);
        ++counter[mano];
        if (counter[mano] < 5 && player.getAID().equals(
            players.get(next).getAID())) {
            computeNext();
        }
    }

    public void computeNext() {
        if (counter[mano] < 5) {
            int i = 1;
            do {
                next = (next + i++) % 5;
            } while (giocate.get(mano).get(players.get(next)) != null && i < 5);
        } else {
            next = -1;
        }
    }

    public synchronized void setNext(Player next) {
        if (next == null) {
            this.next = -1;
        } else {
            for (int i = 0; i < players.size(); ++i) {
                if (players.get(i).getAID().equals(next.getAID())) {
                    this.next = i;
                }
            }
        }
    }

    public class Mano {

        private final int id;
        private final HashMap<AID, Card> giocate;
        //private final HashMap<Card, Player> giocatori;
        //private List<Card> carteGiocate;

        Mano(int id) {
            this.giocate = new HashMap<>();
            //this.giocatori = new HashMap<>();
            //this.carteGiocate = new LinkedList<>();
            this.id = id;
        }

        public void add(Player p, Card c) {
            if (giocate.get(p) != null) {
                throw new IllegalArgumentException(p + " ha già giocato");
            }
            giocate.put(p.getAID(), c);
            //giocatori.put(c, p);
            //carteGiocate.add(c);
        }

        public Card get(Player p) {
            return giocate.get(p);
        }

        /*
         public List<Card> getCarteGiocate() {
         return null;
         }*/
    }

}
