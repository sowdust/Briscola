/*
 */
package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import briscola.objects.Deck;
import briscola.objects.Hand;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;

/**
 * In this part of the game the Mazziere distributes the cards to all players
 * and then starts collecting their offer starting from the first in the list.
 *
 * @author mat
 */
public class ManageBid extends Behaviour {

    private MazziereAgent mazziere;
    private Deck deck;
    //private final boolean visto = false;

    public ManageBid(MazziereAgent mazziere)
    {
        this.mazziere = mazziere;
    }

    @Override
    public void action()
    {

    }

    @Override
    public boolean done()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
