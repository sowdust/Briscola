/*
 */
package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import briscola.objects.Card;
import briscola.objects.Deck;
import jade.core.behaviours.Behaviour;
import java.util.List;

public class ManageBid extends Behaviour {

    MazziereAgent mazziere;
    Deck deck;
    boolean visto = false;

    public ManageBid(MazziereAgent mazziere) {
        this.mazziere = mazziere;
    }

    @Override
    public void action() {

        deck = mazziere.getTable().getDeck();
        deck.shuffle();
    }

    public void distrDeck() {
        List<Card>[] p = new List[5];
    }

    @Override
    public boolean done() {
        System.out.println("Done");
        block();
        return false;
    }

}
