package briscola.messages;

import briscola.Player;
import java.io.Serializable;
import java.util.List;

public class ScoreMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Player> players;
    public List<Integer> points;

    public ScoreMessage(List<Player> players, List<Integer> points) {
        this.players = players;
        this.points = points;
    }

}
