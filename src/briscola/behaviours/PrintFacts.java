package briscola.behaviours;

import briscola.GeneralAgent;
import briscola.PlayerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jess.JessException;

public class PrintFacts extends OneShotBehaviour {

    private static final long serialVersionUID = 1L;

    private GeneralAgent agent;

    public PrintFacts(GeneralAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        System.out.println("Printing facts for agent " + agent);
        if (agent instanceof PlayerAgent) {
            System.out.println(
                "Situa alla mano " + ((PlayerAgent) agent).getTurnStatus().getMano());
        }

        try {
            agent.getRete().eval("(facts)");
        } catch (JessException ex) {
            ex.printStackTrace();
        }
    }

}
