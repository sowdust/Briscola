package briscola.memory.mazziere;

import briscola.MazziereAgent;
import briscola.Player;
import briscola.memory.Giocata;
import briscola.memory.Presa;
import briscola.objects.Bid;
import briscola.objects.Card;
import briscola.objects.Hand;
import briscola.objects.Role;
import static briscola.objects.Role.GIAGUARO;
import static briscola.objects.Role.SOCIO;
import jade.core.AID;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameMemory implements Serializable {

    private static final long serialVersionUID = 1L;
    private final MazziereAgent mazziere;
    private List<Player> players;
    private List<String> points;
    private final Map<AID, Role> roles;
    private final Map<Role, Player> playerRoles;
    private final Map<Player, Hand> hands;
    private final List<Bid> bids;
    private Card briscola;
    private final List<Giocata> giocate;
    private final List<Presa> prese;
    private int mano;

    public GameMemory(MazziereAgent mazziere) {
        this.mazziere = mazziere;
        this.roles = new HashMap<>();
        this.playerRoles = new HashMap<>();
        this.hands = new HashMap<>();
        this.bids = new LinkedList<>();
        this.giocate = new LinkedList<>();
        this.prese = new LinkedList<>();
        this.points = new ArrayList<>();
        this.mano = 0;
    }

    public void setPoints(List<Integer> points) {
        for (Integer p : points) {
            this.points.add(p.toString());
        }
    }

    synchronized public void setPlayers(List<Player> players) {
        this.players = new LinkedList<>(players);
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public void setBriscola(Card b) {
        this.briscola = b;
    }

    public void setRole(Player giaguaro, Role role) {
        roles.put(giaguaro.getAID(), role);
        playerRoles.put(role, giaguaro);
        mazziere.say("Ruolo settato " + role + " per " + giaguaro);
    }

    public Hand getHand(Player p) {
        return hands.get(p);
    }

    public void setHand(Player p, Hand h) {
        hands.put(p, h);
    }

    public void addGiocata(int mano, int counter, Player player, Card card) {
        giocate.add(new Giocata(mano, counter, player,
                                roles.get(player.getAID()), card));
        if (roles.get(player.getAID()) == null) {
            mazziere.say("ruolo nullo per " + player);
        }
    }

    public void addPresa(Player prossimo, int partialScore) {
        prese.add(new Presa(this.mano++, prossimo, roles.get(prossimo.getAID()),
                            partialScore));
    }

    public void write() throws IOException {
        String[] separator = new String[1];
        separator[0] = "";

        //  LOG PLAYERS AND THEIR ROLES
        List<String> playerNames = new LinkedList<>();
        List<String> rolesList = new LinkedList<>();
        int socioIndex = -1;
        int giaguaroIndex = -1;
        String ruoloMartinuzza = "V";
        String ruoloMattia = "V";
        List<Integer> villIndex = new LinkedList<>();
        Role r;
        Player p;
        for (int i = 0; i < players.size(); ++i) {
            p = players.get(i);
            r = this.roles.get(p.getAID());
            playerNames.add(p.getName());
            rolesList.add(r.toString());
            if (r.equals(GIAGUARO)) {
                giaguaroIndex = i;
                /*if (p.getName().equals("martinuzza")) {
                 ruoloMartinuzza = "G";
                 }
                 if (p.getName().equals("mattia")) {
                 ruoloMattia = "G";
                 }*/
            } else if (r.equals(SOCIO)) {
                socioIndex = i;
                /*                if (p.getName().equals("martinuzza")) {
                 ruoloMartinuzza = "S";
                 }
                 if (p.getName().equals("mattia")) {
                 ruoloMattia = "S";
                 }*/
            } else {
                villIndex.add(i);
            }
        }

        List<String> resultPlayers = new LinkedList<>();
        List<String> resultPoints = new LinkedList<>();

        resultPoints.add(points.get(giaguaroIndex));
        resultPoints.add(points.get(socioIndex));
        for (int i : villIndex) {
            resultPoints.add(points.get(i));
        }
//        resultPoints.add(ruoloMartinuzza);
//        resultPoints.add(ruoloMattia);

        mazziere.logCSV(resultPoints.toArray(new String[resultPoints.size()]));

        /*
         mazziere.logCSV(playerNames.toArray(new String[playerNames.size()]));
         mazziere.logCSV(rolesList.toArray(new String[roles.size()]));
         mazziere.logCSV(points.toArray(new String[points.size()]));

         //  LOG BIDS HISTORY (skip pass)
         separator[0] = "Asta";
         mazziere.logCSV(separator);
         String[] bidss = new String[2];
         for (Bid b : bids) {
         if (!b.passo()) {
         bidss[0] = b.getPlayer().getName();
         bidss[1] = b.rank().toString();
         mazziere.logCSV(bidss);
         }
         }

         //  LOG GIOCATE HISTORY
         separator[0] = "Giocate";
         mazziere.logCSV(separator);
         for (Giocata g : giocate) {
         mazziere.logCSV(g.toStringArray());
         }

         //  LOG HISTORY PRESE
         separator[0] = "Prese";
         mazziere.logCSV(separator);
         for (Presa p : prese) {
         mazziere.logCSV(p.toStringArray());
         }
         */
        mazziere.getCsvWriter().flushQuietly();
    }

    public void addGiocataComment(Giocata giocata, String commento) {
        int i = giocate.indexOf(giocata);
        giocate.get(i).setComment(commento);
    }
}
