package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import jade.core.behaviours.OneShotBehaviour;
import java.io.IOException;

public class EndGame extends OneShotBehaviour {

    private static final long serialVersionUID = 1L;

    private final MazziereAgent mazziere;

    public EndGame(MazziereAgent mazziere) {
        this.mazziere = mazziere;
    }

    @Override
    public void action() {

        if (mazziere.writeCSV()) {
            mazziere.say("writing csv summary on file " + mazziere.getCsvFile());
            try {
                mazziere.getMemory().write();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        mazziere.say("Resetting memory");
        mazziere.resetGameMemory();
    }
}
