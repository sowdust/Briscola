/*
 */
package briscola;

import briscola.behaviours.player.Subscribe;
import briscola.objects.Bid;
import briscola.objects.Hand;
import jade.core.AID;
import java.util.List;

public class PlayerAgent extends GeneralAgent {

    private static final long serialVersionUID = 1L;

    private boolean visible;
    private Hand myHand;
    //private PlayerGUI gui;

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
        addBehaviour(new Subscribe(this));
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

    public void setHand(Hand hand) {
        this.myHand = hand;
        ((PlayerGUI) gui).setHand(hand);
    }

    public Hand getHand() {
        return this.myHand;
    }

    public AID getMazziereAID() {
        return mazziereAID;
    }

    public Player getPlayer() {
        return new Player(getAID(), name);
    }

    public void addBid(Bid justBid) {
        //  eventually add Bid to bids history
        //  (the object auctionMemory can be allargato and its reference stored in PlayerAgent
        //  and not in its behaviour
        ((PlayerGUI) gui).addBid(justBid);

    }
}
