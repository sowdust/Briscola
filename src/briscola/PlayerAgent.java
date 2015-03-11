/*
 */
package briscola;

import briscola.behaviours.player.Subscribe;
import briscola.memory.player.AuctionMemory;
import briscola.objects.Bid;
import briscola.objects.Card;
import briscola.objects.Deck;
import briscola.objects.Hand;
import jade.core.AID;
import java.util.List;
import jess.Fact;
import jess.JessException;
import jess.Rete;
import jess.Value;

public class PlayerAgent extends GeneralAgent {

    private static final long serialVersionUID = 1L;

    private AuctionMemory auctionMemory;
    private boolean visible;
    private Hand myHand;
    private static final String rulesFile = "/home/mat/school/Tesi/src/briscola/reasoner/common.clp";

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

        //  SETTING UP THE RETE INSTANCE FOR JESS RULE PROCESSING
        rete = new Rete();
        try {
            rete.batch(rulesFile);
            rete.reset();
        } catch (JessException ex) {
            ex.printStackTrace();
        }

        gui = new PlayerGUI(this);
        gui.setVisible(visible);

        say("Giocatore " + getAID().getName() + " iscritto alla piattaforma");
        addBehaviour(new Subscribe(this));
    }

    public String getRealName() {
        return name;
    }

    public AuctionMemory getAuctionMemory() {
        if (auctionMemory == null) {
            auctionMemory = new AuctionMemory(this);
        }
        return auctionMemory;
    }

    public PlayerGUI gui() {
        return (PlayerGUI) gui;
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

    /**
     * Stores the cards received from mazziere in memory
     * Partitions a deck of card in cards in hand, cards covered
     * and asserts relevants facts in JESS reasoner
     *
     * @param hand
     * @throws jess.JessException
     */
    public void setHand(Hand hand) throws JessException {
        this.myHand = hand;
        Deck tempDeck = new Deck();
        Fact f;
        for (Card c : tempDeck.getCards()) {
            if (hand.getCards().contains(c)) {
                f = new Fact("in-mano", rete);
            } else {
                f = new Fact("in-mazzo", rete);
            }
            f.setSlotValue("card", new Value(c));
            f.setSlotValue("rank", new Value(c.getRank()));
            f.setSlotValue("suit", new Value(c.getSuit()));
            rete.assertFact(f);
        }
        ((PlayerGUI) gui).setHand(hand);
        rete.eval("(facts)");
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

    public void addGiocata(int counter, Player justPlayer, Card justCard) {
        ((PlayerGUI) gui).addGiocata(counter, justPlayer, justCard);
    }
}
