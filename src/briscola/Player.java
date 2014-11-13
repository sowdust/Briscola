package briscola;

import jade.core.AID;
import java.io.Serializable;

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    AID agent;
    String name;

    public Player(AID agent, String name) {
        this.agent = agent;
        this.name = name;
    }

    public AID getAID() {
        return agent;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " [" + agent.getName() + "]";
    }
}
