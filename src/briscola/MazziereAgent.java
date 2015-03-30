package briscola;

import briscola.behaviours.GetChatMessage;
import briscola.behaviours.mazziere.GetGiocataComment;
import briscola.behaviours.mazziere.OpenTable;
import static briscola.common.Names.MAZZIERE;
import briscola.memory.mazziere.GameMemory;
import briscola.objects.Card;
import briscola.objects.Hand;
import briscola.objects.Rank;
import static briscola.objects.Role.GIAGUARO;
import static briscola.objects.Role.SOCIO;
import static briscola.objects.Role.VILLANO;
import briscola.objects.Suit;
import briscola.objects.Table;
import com.opencsv.CSVWriter;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
 */
public class MazziereAgent extends GeneralAgent {

    private static final long serialVersionUID = 1L;

    private Table table;
    private DFAgentDescription dfd;
    private ServiceDescription sd;
    private static final String rulesFile = "briscola/reasoner/mazziere.clp";
    private Player giaguaro;
    private Player socio;
    private Card briscola;
    private CSVWriter csvWriter;
    private String csvFile;
    private boolean writeCSV;
    private GameMemory gameMem;
    private GetGiocataComment getGiocataComment;
    private GetChatMessage getChatMessage;
    private OpenTable openTable;

    public MazziereAgent() {
        players = new ArrayList<>();
        table = new Table();
        this.gameMem = new GameMemory(this);
    }

    @Override
    protected void setup() {
        this.mazziereAID = this.getAID();
        Object[] args = getArguments();

        this.behaviours = new LinkedList<>();

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

        if (args != null && args.length > 2) {
            csvFile = ((String) args[2]);
            writeCSV = true;
            try {
                csvWriter = new CSVWriter(new FileWriter(csvFile, true), '\t');
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            writeCSV = false;
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

        startNewGame();

    }

    public void startNewGame() {
        openTable = new OpenTable(this);
        addBehaviour(openTable);
        getGiocataComment = new GetGiocataComment(this);
        addBehaviour(getGiocataComment);
        this.gameMem = new GameMemory(this);
    }

    public void resetGameMemory() {
        players = new ArrayList<>();
        table = new Table();
        giaguaro = null;
        socio = null;
        briscola = null;
//            gui.clean();

        gameMem = null;
        clearMessageQueue();
        removeAllBehaviours();
    }

    public String getRealName() {
        return name;
    }

    public void setBriscola(Suit r, Rank rank) throws JessException {
        table.setBriscola(r);
        this.briscola = new Card(rank, r);
        Defglobal g = new Defglobal("*briscola*", new Value(r));
        rete.addDefglobal(g);
        gameMem.setBriscola(briscola);

        for (Player p : players) {
            Hand h = gameMem.getHand(p);
            if (h.contains(briscola)) {
                this.socio = p;
                say("Il socio è " + p);
                gameMem.setRole(p, SOCIO);
            } else {
                if (!p.equals(giaguaro)) {
                    gameMem.setRole(p, VILLANO);
                }
            }
        }
    }

    synchronized public void addPlayer(AID agente, String name) {
        if (players.size() >= 5) {
            say("siamo già a 5, non possiamo accettarne altri");
        } else {
            Player player = new Player(agente, name);
            players.add(player);
            if (graphic) {
                gui.addPlayer(player);
            }
        }
    }

    @Override
    protected void takeDown() {

        say("Felice di aver giocato con voi. Addio!");
        dfd.removeServices(sd);

        if (graphic) {
            gui.dispose();
        }
        if (writeCSV) {
            try {
                csvWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    synchronized public List<AID> getPlayersAID() {
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
        gameMem.setRole(giaguaro, GIAGUARO);
    }

    public Player getGiaguaro() {
        return giaguaro;
    }

    public void setHand(Player p, Hand h) {
        gameMem.setHand(p, h);
    }

    public void logCSV(String[] values) throws IOException {
        csvWriter.writeNext(values);
    }

    /*    public void csvWriteScore(String name, int partialScore) throws
     IOException {
     String[] values = new String[3];
     values[0] = Integer.toString(partialScore);
     values[1] = name;
     logCSV(values);
     }*/
    public CSVWriter getCsvWriter() {
        return csvWriter;
    }

    public GameMemory getMemory() {
        return gameMem;
    }

    public boolean writeCSV() {
        return writeCSV;
    }

    public String getCsvFile() {
        return csvFile;
    }

    public void setChatBehaviour() {
        this.getChatMessage = new GetChatMessage(this);
        addBehaviour(getChatMessage);
    }

}
