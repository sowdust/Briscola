/*
 */
package briscola;

import briscola.behaviours.mazziere.OpenTable;
import briscola.objects.Suit;
import briscola.objects.Table;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.ArrayList;
import java.util.List;

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

    private Table table;
    //private MazziereGUI gui;
    private DFAgentDescription dfd;
    private ServiceDescription sd;

    @Override
    protected void setup() {
        this.mazziereAID = this.getAID();
        Object[] args = getArguments();
        players = new ArrayList<>();
        table = new Table();

        if (args != null && args.length > 0) {
            name = (String) args[0];
        } else {
            name = "Gianni";
        }

        gui = new MazziereGUI(this);
        gui.setVisible(true);

        say("Mazziere " + name + " al suo servizio");

        //  REGISTER TO YELLOW PAGES
        dfd = new DFAgentDescription();
        dfd.setName(getAID());
        sd = new ServiceDescription();
        sd.setType(briscola.common.Names.MAZZIERE);
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

    public void setBriscola(Suit r) {
        table.setBriscola(r);
    }

    public void addPlayer(AID agente, String name) {
        Player player = new Player(agente, name);
        players.add(player);
        gui.addPlayer(player);
    }

    @Override
    protected void takeDown() {
        say("Felice di aver giocato con voi. Addio!");
        gui.dispose();
        dfd.removeServices(sd);
        super.takeDown();
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
        table.setGiaguaro(bestBidder);

    }

}
