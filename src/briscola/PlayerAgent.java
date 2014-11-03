/*
 */
package briscola;

import briscola.behaviours.player.SubscribeBehaviour;
import jade.core.AID;
import java.util.List;

/**
 *
 * @author mat
 */
public class PlayerAgent extends GeneralAgent {

    private boolean visible;

    @Override
    protected void setup() {

        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            name = (String) args[0];
            visible = true;
        } else {
            visible = false;
            name = briscola.common.Names.randomName();
        }

        gui = new PlayerGUI(this);
        gui.setVisible(visible);

        say("Giocatore " + getAID().getName() + " iscritto alla piattaforma");
        addBehaviour(new SubscribeBehaviour(this));
    }

    public String getRealName() {
        return name;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        for (Player p : players) {
            gui.addPlayer(p);
        }
    }

    public void setMazziereAID(AID sender) {
        this.mazziereAID = sender;
    }

    public AID getMazziereAID() {
        return mazziereAID;
    }
}
