/*
 */
package briscola;

import briscola.behaviours.GetChatMessage;
import briscola.behaviours.player.Subscribe;
import briscola.memory.TurnStatus;
import briscola.memory.player.AuctionMemory;
import briscola.objects.Bid;
import briscola.objects.Card;
import briscola.objects.Deck;
import briscola.objects.Hand;
import briscola.objects.Role;
import briscola.objects.Strategy;
import static briscola.objects.Strategy.MANUAL;
import static briscola.objects.Strategy.NORMAL;
import static briscola.objects.Strategy.RANDOM;
import briscola.objects.Suit;
import jade.core.AID;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import jess.Defglobal;
import jess.Fact;
import jess.Funcall;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

public class PlayerAgent extends GeneralAgent {

    private static final long serialVersionUID = 1L;

    private AuctionMemory auctionMemory;
    private boolean visible;
    private Hand myHand;
    private Role role;
    private String rulesFile;
    private Card briscolaCard;
    private Suit briscolaSuit;
    private TurnStatus status;
    private Strategy strategy;
    private Card cartaDaGiocare;
    private GetChatMessage getChatMessage;
    private boolean gameOver;

    //private PlayerGUI gui;
    @Override
    protected void setup() {

        Object[] args = getArguments();

        if (args != null && args.length == 3) {
            name = (String) args[0];
            String strategyName = (String) args[1];
            switch (strategyName) {
                case "random":
                    this.strategy = RANDOM;
                    break;
                case "normal":
                    this.strategy = NORMAL;
                    break;
                case "manual":
                    this.strategy = MANUAL;
                    break;
                default:
                    this.strategy = RANDOM;
                    break;
            }
            visible = (((String) args[2]).equals("true"));
            graphic = visible;

        } else {
            visible = false;
            name = briscola.common.Names.randomName();
            this.strategy = RANDOM;
        }

        this.behaviours = new LinkedList<>();
        this.gameOver = false;

        rulesFile = strategy.getFile();

        startRete();

        gui = new PlayerGUI(this);
        gui.setVisible(visible);

        say("Giocatore " + getAID().getName() + " iscritto alla piattaforma");
        say("Utilizzerò la strategia " + strategy);
        addBehaviour(new Subscribe(this));
    }
    //  SETTING UP THE RETE INSTANCE FOR JESS RULE PROCESSING

    public void startRete() {
        rete = new Rete();
        try {
            rete.batch(rulesFile);
            rete.reset();
            rete.store("IO", new Value(this.getPlayer()));
            rete.store("AGENT", new Value(this));
        } catch (JessException ex) {
            ex.printStackTrace();
        }
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

    synchronized public void setPlayers(List<Player> players) {
        this.players = players;
        for (Player p : players) {
            gui.addPlayer(p);
        }
    }

    public void setMazziereAID(AID sender) {
        this.mazziereAID = sender;
    }

    public boolean hasCard(Card c) {
        return myHand.getCards().contains(c);
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

    public void initMano(int mano, Player next) throws JessException {

        //  diciamo al reasoner che inizializziamo una nuova mano
        Value v = new Funcall("init-mano", rete).arg(
            new Value(mano, RU.INTEGER)).execute(rete.getGlobalContext());

        //  diamo i turni ai giocatori
        int firstIndex = -1;

        for (int i = 0; i < players.size(); ++i) {
            if (players.get(i).equals(next)) {
                firstIndex = i;
                break;
            }
        }

        for (int i = 0; i < players.size(); ++i) {
            Fact f = new Fact("turno", rete);
            Player p = players.get((i + firstIndex) % 5);
            f.setSlotValue("player", new Value(p));
            f.setSlotValue("posizione", new Value(i, RU.INTEGER));
            rete.assertFact(f);
            if (p.equals(this.getPlayer())) {
                rete.assertString("(mio-turno-numero " + i + ")");
            }

        }
        //  aggiorniamo la gui

        rete.run();
        //rete.eval("(facts)");
        gui().initMano(mano, next);
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
            if (hasCard(c)) {
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

    }

    /**
     * Every time a card is played, we need to update our internal
     * representation.
     * - add the card onto the table
     * - if it was us playing, remove the card from our hand
     * - if it was others playing, remove the card from the hidden deck
     * - if the cards was the briscola, we now know the teams
     *
     * @param counter
     * @param justPlayer
     * @param justCard
     * @throws JessException
     */
    public void addGiocata(int mano, int counter, Player justPlayer,
                           Card justCard) throws
        JessException {

        Fact z = new Fact("nuova-giocata", rete);
        z.setSlotValue("player", new Value(justPlayer));
        z.setSlotValue("card", new Value(justCard));
        z.setSlotValue("rank", new Value(justCard.getRank()));
        z.setSlotValue("suit", new Value(justCard.getSuit()));
        rete.assertFact(z);

        //  TODO: il seguente codice può essere spostato nel reasoner
        if (justCard.equals(briscolaCard)) {
            say("Ta-dah! Il socio è venuto fuori! Il vecchio " + justPlayer.getName() + " sociello");
            Fact s = new Fact("socio", rete);
            s.setSlotValue("player", new Value(justPlayer));
            rete.assertFact(s);
        }

        if (justPlayer.equals(this.getPlayer())) {
            //  non viene fatto dal reasoner perchè a volte se il reasoner non riesce
            //  a computare una carta, ne gioca una a caso
            Fact f = new Fact("in-mano", rete);
            f.setSlotValue("card", new Value(justCard));
            f.setSlotValue("rank", new Value(justCard.getRank()));
            f.setSlotValue("suit", new Value(justCard.getSuit()));
            rete.retract(f);
        } else {
            Fact f = new Fact("in-mazzo", rete);
            f.setSlotValue("card", new Value(justCard));
            f.setSlotValue("rank", new Value(justCard.getRank()));
            f.setSlotValue("suit", new Value(justCard.getSuit()));
            rete.retract(f);
        }

        Fact q = new Fact("in-tavolo", rete);
        q.setSlotValue("card", new Value(justCard));
        q.setSlotValue("player", new Value(justPlayer));
        q.setSlotValue("rank", new Value(justCard.getRank()));
        q.setSlotValue("suit", new Value(justCard.getSuit()));
        rete.assertFact(q);

        rete.run();
        ((PlayerGUI) gui).addGiocata(mano, counter, justPlayer, justCard);

    }

    public void setRole(Role role) throws JessException {
        this.role = role;
        rete.assertString("(mio-ruolo " + role + ")");
        say("Il mio ruolo è " + role);

        //  TODO: colorare ruoli agli altri
    }

    public Role getRole() {
        return role;
    }

    /**
     * declares both a fact containing the briscola card
     * and a global variable with its suit (redundant, we know..)
     *
     * @param c
     * @throws JessException
     */
    public void setBriscola(Card c) throws JessException {
        this.briscolaCard = c;
        this.briscolaSuit = c.getSuit();
        Fact g = new Fact("briscola", rete);
        g.setSlotValue("suit", new Value(briscolaSuit));
        g.setSlotValue("rank", new Value(c.getRank()));
        g.setSlotValue("card", new Value(briscolaCard));
        assertFact(g);
        Value v = new Value(c.getSuit());
        Defglobal k = new Defglobal("*briscola*", v);
        rete.addDefglobal(k);

        getHand().sort(briscolaSuit);
        ((PlayerGUI) gui).setHand(getHand());
    }

    /**
     * Sets the turn status object in memory
     * Called by PlayGame behaviour when initializing its
     * internal status:TurnStatus variable
     *
     * @param status
     */
    public void setTurnStatus(TurnStatus status) {
        this.status = status;
    }

    public TurnStatus getTurnStatus() {
        return status;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void computeStrenght() throws JessException {
        List<Card> list = getHand().getCards(briscolaSuit);
        int value = 0;
        for (Card c : list) {
            value += c.getPosition();
        }
        if (list.size() > 3 || value > 20) {
            rete.assertString("(socio-forza 20)");
        } else if (list.size() < 3 && value < 17) {
            rete.assertString("(socio-forza 0)");
        } else {
            rete.assertString("(socio-forza 10)");
        }

    }

    public void setCardToPlay(Card c) {
        cartaDaGiocare = c;
    }

    public Card play() throws JessException, InterruptedException {
        Card c;
        switch (strategy) {
            case RANDOM:
                c = getHand().drawRandom();
                break;
            case NORMAL:
                Value v = getRete().fetch("DA-GIOCARE");
                if (null == v) {

                    say("Giocando a caso");
                    c = getHand().drawRandom();
                } else {
                    c = (Card) v.javaObjectValue(
                        getRete().getGlobalContext());
                    getHand().removeCard(c);
                }
                break;
            case MANUAL:
                ((PlayerGUI) gui).beginGiocata();
                while (cartaDaGiocare == null) {
                    Thread.sleep(1000);
                }
                c = cartaDaGiocare;
                cartaDaGiocare = null;
                break;
            default:
                say("Strategia sconosciuta", true);
                c = getHand().drawRandom();
                break;

        }
        return c;
    }

    @Override
    protected void takeDown() {
        say("È stato bello giocare! Addio!", true);
    }

    public void exit() {
        //gui.dispose();
        takeDown();
    }

    void newGame() {
        say("Iniziando nuova partita...");
        gameOver = false;
        setChatID(null);
        setMazziereAID(null);
        players = new ArrayList<>();
        clearMessageQueue();
        if (graphic) {
            ((PlayerGUI) gui).newGame();
            ((PlayerGUI) gui).setEndGameButton(false);
        }
        startRete();
        addBehaviour(new Subscribe(this));
    }

    public void setEndGameButton(boolean b) {
        ((PlayerGUI) gui).setEndGameButton(b);
    }

    public void endGame() {
        //  enable new game button
        ((PlayerGUI) gui).enableNewGame();

        auctionMemory = null;
        myHand = null;
        role = null;
        briscolaCard = null;
        briscolaSuit = null;
        status = null;
        cartaDaGiocare = null;
        removeAllBehaviours();
        clearMessageQueue();
    }

    public void startChat() {
        getChatMessage = new GetChatMessage(this);
        addBehaviour(getChatMessage);
    }

    public boolean gameOver() {
        return gameOver;
    }

    void setGameOver(boolean b) {
        gameOver = b;
    }

    public void toggleEndGameButton() {
        if (visible) {
            ((PlayerGUI) gui).toggleEndGameButton();
        }
    }

}
