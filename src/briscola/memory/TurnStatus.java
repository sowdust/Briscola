package briscola.memory;

import briscola.Player;
import briscola.objects.Card;
import jade.core.AID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TurnStatus {

    private List<Player> players;
    private List<Mano> giocate;

    //  temp score per player
    private final HashMap<AID, Integer> punteggi;
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
        this.punteggi = new HashMap<>();

        //  init giocate
        for (int i = 0; i < 8; ++i) {
            this.giocate.add(new Mano(i));
        }

        //  init all points to 0
        for (Player p : players) {
            punteggi.put(p.getAID(), 0);
        }

//  find first to play
        for (int i = 0; i < players.size(); ++i) {
            if (players.get(i).getAID().equals(first.getAID())) {
                next = i;
                break;
            }
        }

        initMano(next);
    }

    public int getScore(Player p) {
        return punteggi.get(p.getAID());
    }

    public int updateScore(Player p, int score) {
        Integer newScore = punteggi.get(p.getAID());
        if (newScore == null) {
            newScore = score;
        } else {
            newScore = newScore + score;
        }
        punteggi.put(p.getAID(), newScore);
        return newScore;
    }

    public boolean initMano(Player p) {
        for (int i = 0; i < players.size(); ++i) {
            if (p.getAID().equals(players.get(i).getAID())) {
                return initMano(i);
            }
        }
        throw new IllegalArgumentException(
            "Player " + p + " non nella mia lista");
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

    synchronized public int getCounter() {
        return counter[mano];
    }

    synchronized public int getMano() {
        return mano;
    }

    synchronized public Mano getCurrentMano() {
        return giocate.get(mano);
    }

    synchronized public void addGiocata(Player player, Card card, int mano) {
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

    synchronized public void computeNext() {
        if (counter[mano] < 5) {
            int i = 1;
            do {
                next = (next + i++) % 5;
            } while (giocate.get(mano).get(players.get(next)) != null && i < 5);
        } else {
            next = -1;
        }
    }

    synchronized public void setNext(Player next) {
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
        private final HashMap<Card, Player> giocatori;
        private final List<Card> carteGiocate;

        Mano(int id) {
            this.giocate = new HashMap<>();
            this.giocatori = new HashMap<>();
            this.carteGiocate = new LinkedList<>();
            this.id = id;
        }

        public void add(Player p, Card c) {
            if (giocate.get(p.getAID()) != null) {
                throw new IllegalArgumentException(p + " ha già giocato");
            }
            giocate.put(p.getAID(), c);
            giocatori.put(c, p);
            carteGiocate.add(c);
        }

        public Card get(Player p) {
            return giocate.get(p);
        }

        public List<Card> getCarteGiocate() {
            return Collections.unmodifiableList(carteGiocate);
        }

        public Player getPlayerOfCard(Card c) {
            return giocatori.get(c);
        }

        public int getPoints() {
            int score = 0;
            for (Card c : carteGiocate) {
                score += c.getRank().getValue();
            }
            return score;
        }
    }

}
