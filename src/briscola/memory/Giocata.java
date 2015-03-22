package briscola.memory;

import briscola.Player;
import briscola.objects.Card;
import briscola.objects.Role;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Giocata implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Player player;
    private final int mano;
    private final int turno;
    private final Card card;
    private final Role role;
    private List<String> comments;

    public Giocata(int mano, int turno, Player player, Role role, Card card) {
        this.mano = mano;
        this.turno = turno;
        this.card = card;
        this.player = player;
        this.role = role;
        this.comments = new ArrayList<>();
    }

    public String[] toStringArray() {
        String[] s = new String[5 + comments.size()];
        s[0] = Integer.toString(mano);
        s[1] = Integer.toString(turno);
        s[2] = player.getName();
        if (role != null) {
            s[3] = role.toString();
        } else {
            s[3] = "";
        }
        s[4] = card.toString();
        for (int i = 0; i < comments.size(); ++i) {
            s[5 + i] = comments.get(i);
        }
        return s;
    }

    public void setComment(String comment) {
        comments.add(comment);
    }

    @Override
    public boolean equals(Object e) {
        if (!(e instanceof briscola.memory.Giocata)) {
            return false;
        }
        Giocata g = (Giocata) e;
        return mano == g.getMano() && turno == g.getTurno();
        //&& card.equals(            g.getCard()) && player.equals(g.getPlayer());
    }

    public int getMano() {
        return mano;
    }

    public int getTurno() {
        return turno;
    }

    public Card getCard() {
        return card;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return Integer.toString(mano) + " " + Integer.toString(turno) + " " + player.getName() + " " + card.toString();
    }

}
