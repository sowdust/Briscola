package briscola;

import jade.core.AID;
import java.io.Serializable;

public class Player implements Serializable {

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
}
