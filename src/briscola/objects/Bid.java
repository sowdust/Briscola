package briscola.objects;

import briscola.Player;
import java.io.Serializable;

public class Bid implements Serializable {

    private final Rank rank;
    private final boolean passo;
    private final Player player;

    public Bid(Player player, Rank rank) {
        this.player = player;
        this.rank = rank;
        this.passo = false;
    }

    public Bid(Player player, boolean passo) {
        this.player = player;
        this.passo = passo;
        this.rank = null;
    }

    public boolean passo() {
        return passo;
    }

    public Rank rank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        if (passo) {
            return player.getName() + " passa";
        }

        return player.getName() + ": " + rank;
    }

}
