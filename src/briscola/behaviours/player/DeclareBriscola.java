package briscola.behaviours.player;

import briscola.PlayerAgent;
import briscola.behaviours.SendMessage;
import static briscola.common.ACLCodes.ACL_TELL_BRISCOLA;
import briscola.objects.Suit;
import jade.core.behaviours.OneShotBehaviour;

public class DeclareBriscola extends OneShotBehaviour {

    private PlayerAgent player;

    public DeclareBriscola(PlayerAgent player) {
        this.player = player;
    }

    @Override
    public void action() {
        Suit s = player.getAuctionMemory().computeBriscola();
        SendMessage m = new SendMessage(player.getMazziereAID(),
                                        ACL_TELL_BRISCOLA,
                                        s);
        myAgent.addBehaviour(m);

    }

}
