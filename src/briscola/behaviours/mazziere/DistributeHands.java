package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import briscola.Player;
import briscola.behaviours.SendAndWait;
import static briscola.common.ACLCodes.ACL_YOUR_HAND;
import briscola.objects.Deck;
import briscola.objects.Hand;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mat
 */
public class DistributeHands extends OneShotBehaviour {

    private static final long serialVersionUID = 1L;

    private final MazziereAgent mazziere;
    private final Deck deck;
    //private final boolean visto = false;

    public DistributeHands(MazziereAgent mazziere) {
        this.mazziere = mazziere;
        deck = mazziere.getTable().getDeck();
    }

    @Override
    public void action() {
        mazziere.say("distribuendo le carte");
        deck.shuffle();
        Hand[] h = new Hand[5];
        h[0] = new Hand();
        h[1] = new Hand();
        h[2] = new Hand();
        h[3] = new Hand();
        h[4] = new Hand();
        //  distribuiamo le carte come si deve
        for (int i = 0; i < deck.size(); ++i) {
            h[i % 5].addCard(deck.get(i));
        }
        List<Player> players = mazziere.getPlayers();
        ParallelBehaviour sendMessages = new ParallelBehaviour();
        for (int i = 0; i < 5; ++i) {
            List<Player> rcp = new LinkedList<>();
            rcp.add(players.get(i));
            mazziere.setHand(players.get(i), h[i]);
            SendAndWait b = new SendAndWait(rcp, ACL_YOUR_HAND, h[i]);
            sendMessages.addSubBehaviour(b);
            mazziere.say(rcp.get(0) + "\t" + h[i]);

        }
        SequentialBehaviour doAll = new SequentialBehaviour();
        OneShotBehaviour whatNext = new OneShotBehaviour() {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
                // HERE GOES WHAT TO DO NEXT!!!!
                myAgent.addBehaviour(new ManageBid(mazziere));
            }
        };
        doAll.addSubBehaviour(sendMessages);
        doAll.addSubBehaviour(whatNext);

        myAgent.addBehaviour(doAll);

    }

}
