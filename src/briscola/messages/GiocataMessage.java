package briscola.messages;

import briscola.Player;
import briscola.objects.Card;
import java.io.Serializable;

public class GiocataMessage implements Serializable {

    public Player player;
    public Card card;
    public int counter;
    public int mano;

    public GiocataMessage(Player player, Card card, int mano, int count) {
        this.player = player;
        this.card = card;
        this.counter = count;
        this.mano = mano;
    }

    @Override
    public String toString() {
        return "[" + mano + " # " + counter + 1
            + "]\t" + player.getName() + "\t " + card;
    }

}
