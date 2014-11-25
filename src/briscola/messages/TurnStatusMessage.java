package briscola.messages;

import briscola.Player;
import briscola.objects.Card;
import java.io.Serializable;

public class TurnStatusMessage implements Serializable {

    public final int counter;
    public final int mano;
    public final Player justPlayer;
    public final Card justCard;
    public final Player next;

    public TurnStatusMessage(int counter, int mano, Player justPlayer,
                             Card justCard,
                             Player next) {
        this.counter = counter;
        this.justPlayer = justPlayer;
        this.justCard = justCard;
        this.next = next;
        this.mano = mano;
    }

    @Override
    public String toString() {
        return "[ " + mano + "-" + counter + " ]"
            + justPlayer + " has just played " + justCard + " "
            + "Next: " + next;
    }

}
