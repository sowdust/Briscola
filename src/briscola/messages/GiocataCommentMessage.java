package briscola.messages;

import briscola.Player;
import briscola.memory.Giocata;
import java.io.Serializable;

public class GiocataCommentMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    public final Player from;
    public final Giocata giocata;
    public final String commento;

    public GiocataCommentMessage(Player from, Giocata giocata, String commento) {
        this.from = from;
        this.giocata = giocata;
        this.commento = commento;
    }

}
