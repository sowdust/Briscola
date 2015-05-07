package briscola;

import jade.core.AID;
import java.io.Serializable;

public class Player implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    AID agentID;
    String name;

    public Player(AID agent, String name) {
        this.agentID = agent;
        this.name = name;
    }

    public AID getAID() {
        return agentID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " [" + agentID.getName() + "]";
    }

    public boolean equals(Player p) {
        return p.name.equals(name) && p.getAID().equals(agentID);
    }

    public boolean equals(Object p) {
        if (!(p instanceof briscola.Player)) {
            return false;
        }
        try {
            return this.equals(p);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public int compareTo(Object t) {
        if (t instanceof briscola.Player) {
            if (this.equals((Player) t)) {
                return 0;
            }
        }
        return -1;
    }
}
