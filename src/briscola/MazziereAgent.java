/*
 */
package briscola;

import briscola.behaviours.mazziere.OpenTable;
import static briscola.common.Names.MAZZIERE;
import briscola.objects.Card;
import briscola.objects.Hand;
import briscola.objects.Rank;
import briscola.objects.Suit;
import briscola.objects.Table;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jess.Defglobal;
import jess.JessException;
import jess.Rete;
import jess.Value;

/**
 * Agent that manages the whole game without taking part in it
 * His duties are:
 * collecting 5 players assigning them an order around the table assigning cards
 * declaring the bid open managing the bid collect a bid or a pass checking they
 * are valid declaring the bid closed declaring the winner ask the winner the
 * briscola declaring the briscola managing the game assign an order for the
 * hand collect cards declare winner of the hand counting points declaring the
 * winner
 *
 * @author mat
 */
public class MazziereAgent extends GeneralAgent {

    private static final long serialVersionUID = 1L;

    private Table table;
    //private MazziereGUI gui;
    private DFAgentDescription dfd;
    private ServiceDescription sd;
    private static final String rulesFile = "briscola/reasoner/mazziere.clp";
    private Player giaguaro;
    private Player socio;
    private Card briscola;
    private Map<Player, Hand> hands;

    @Override
    protected void setup() {
        this.mazziereAID = this.getAID();
        this.hands = new HashMap<>();
        Object[] args = getArguments();
        players = new ArrayList<>();
        table = new Table();

        //  SETTING UP THE RETE INSTANCE FOR JESS RULE PROCESSING
        rete = new Rete();
        try {
            rete.batch(rulesFile);
            rete.reset();
        } catch (JessException ex) {
            System.out.println("Impossibile aprire il file " + rulesFile);
            ex.printStackTrace();
        }

        if (args != null && args.length > 0) {
            name = (String) args[0];
        } else {
            name = "Gianni";
        }
        if (args != null && args.length > 1) {
            graphic = ((String) args[1]).equals("true");
        } else {
            graphic = false;
        }

        if (graphic) {
            gui = new MazziereGUI(this);
            gui.setVisible(true);
        }

        say("Mazziere " + name + " al suo servizio");

        //  REGISTER TO YELLOW PAGES
        dfd = new DFAgentDescription();
        dfd.setName(getAID());
        sd = new ServiceDescription();
        sd.setType(MAZZIERE);
        sd.setName(name);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            say("Errore durante la registrazione alle pagine gialle");
            fe.printStackTrace();
        }
        addBehaviour(new OpenTable(this));
    }

    public String getRealName() {
        return name;
    }

    public void setBriscola(Suit r, Rank rank) throws JessException {
        table.setBriscola(r);
        this.briscola = new Card(rank, r);
        Defglobal g = new Defglobal("*briscola*", new Value(r));
        rete.addDefglobal(g);
        for (Player p : players) {
            Hand h = hands.get(p);
            if (h.contains(briscola)) {
                this.socio = p;
                say("Il socio è " + p);
            }
        }
    }

    public void addPlayer(AID agente, String name) {
        Player player = new Player(agente, name);
        players.add(player);
        if (graphic) {
            gui.addPlayer(player);
        }
    }

    @Override
    protected void takeDown() {
        say("Felice di aver giocato con voi. Addio!");
        if (graphic) {
            gui.dispose();
        }
        dfd.removeServices(sd);
    }

    public List<AID> getPlayersAID() {
        List<AID> r = new ArrayList<>();
        for (Player p : players) {
            r.add(p.getAID());
        }
        return r;
    }

    public DFAgentDescription getDFA() {
        return dfd;
    }

    public ServiceDescription getServiceDesc() {
        return sd;
    }

    public Table getTable() {
        return table;
    }

    public void setGiaguaro(Player bestBidder) {
        say("Il giaguaro è " + bestBidder);
        giaguaro = bestBidder;
    }

    public Player getGiaguaro() {
        return giaguaro;
    }

    public void setHand(Player p, Hand h) {
        hands.put(p, h);
    }

}
