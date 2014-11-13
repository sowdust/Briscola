package briscola.memory.player;

import briscola.Player;
import briscola.PlayerAgent;
import briscola.objects.Bid;
import briscola.objects.Card;
import briscola.objects.Hand;

public class AuctionMemory {

    private PlayerAgent agent;
    private Bid bestBid;
    private Player next;
    private int counter;

    public AuctionMemory(PlayerAgent agent) {
        this.agent = agent;
        this.counter = 0;
        this.bestBid = null;
        this.next = null;
    }

    public void setBest(Bid justBid) {
        bestBid = justBid;
    }

    public void increaseCounter() {
        ++counter;
    }

    /*    public boolean checkCounter(int c) {
     System.out.println("checking counter..." + c + " == " + counter + 1);
     return c == ++counter;
     }*/
    //  QUI ANDRÀ LA LOGICA DEL REASONER SULLE ASTE
    public Bid computeBid(Hand hand) {
        int r = (int) (Math.random() * 12);
        Card c = hand.get(r % hand.size());
        if (bestBid != null && c.getValue() >= bestBid.getValue()) {
            return new Bid(agent.getPlayer(), true);
        }
        return new Bid(agent.getPlayer(), c.getRank());
    }

    public int counter() {
        return counter;
    }
}
