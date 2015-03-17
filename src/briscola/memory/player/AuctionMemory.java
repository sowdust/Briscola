package briscola.memory.player;

import briscola.Player;
import briscola.PlayerAgent;
import briscola.objects.Bid;
import briscola.objects.Card;
import briscola.objects.Hand;
import briscola.objects.Rank;
import briscola.objects.Suit;
import java.util.ArrayList;
import java.util.List;

public class AuctionMemory {

    private final PlayerAgent agent;
    private Bid bestBid;
    private final Player next;
    private int counter;
    private final SensibleOffer potentialOffer;
    private final List<Bid> offers;
    private final List<Suit> suitsOffers;
    private int internalCounter;

    public AuctionMemory(PlayerAgent agent) {
        this.agent = agent;
        this.counter = 0;
        this.internalCounter = 0;
        this.bestBid = null;
        this.next = null;
        this.potentialOffer = SensibleOffer.getOffer(
            agent.getHand().getDistribution(), agent.getHand().getPoints());
        this.offers = new ArrayList<>();
        this.suitsOffers = new ArrayList<>();
        computeOffers();

    }

    public void setBest(Bid justBid) {
        bestBid = justBid;
    }

    public Bid getBest() {
        return bestBid;
    }

    public void increaseCounter() {
        ++counter;
    }

    public final void computeOffers() {
        Hand hand = agent.getHand();

        //  se non siamo nelle condizioni di fare offerta, passiamo
        if (this.potentialOffer.howMany < 0) {
            offers.add(new Bid(agent.getPlayer(), true));

        } else {

            //  vediamo se ci sono le condizioni per offrire
            for (Suit s : agent.getHand().getSemeLungo()) {
                int iHave = 0;
                for (Rank r : potentialOffer.mustHave) {
                    Card temp = new Card(r, s);
                    if (hand.contains(temp)) {
                        ++iHave;
                    }
                }
                if (iHave >= potentialOffer.howMany) {
                    for (Rank r : Rank.getValues()) {

                        if (!hand.contains(new Card(r, s)) && r.getValue() >= potentialOffer.minimum.getValue()) {
                            offers.add(new Bid(agent.getPlayer(), r));
                            suitsOffers.add(s);
                        }
                    }
                }
            }
            offers.add(new Bid(agent.getPlayer(), true));
            suitsOffers.add(null);

        }

        agent.say("### Analisi asta");
        String st = "I miei semi lunghi sono";
        for (Suit s : agent.getHand().getSemeLungo()) {
            st += " " + s;
        }
        agent.say(st);
        agent.say("Il mio punteggio in mano è " + agent.getHand().getPoints());
        st = new String("La distribuzione dei semi è [");
        for (int i : agent.getHand().getDistribution()) {
            st += " " + i;
        }
        agent.say(st + " ]");
        if (potentialOffer.howMany == -1) {
            st = "Non mi conviene quindi chiamare";
        } else {
            st = new String(
                "Devo quindi avere almeno " + potentialOffer.howMany + " tra");
            for (Rank r : potentialOffer.mustHave) {
                st += " " + r;
            }
            st += " di seme lungo e chiamare fino al " + potentialOffer.minimum;
        }
        agent.say(st);
        if (offers.size() > 1) {
            st = "Quindi le offerte che posso fare sono, nell'ordine:";
            for (int i = 0; i < offers.size() - 1; ++i) {
                st += " " + offers.get(i).rank() + " " + suitsOffers.get(i);
            }
            agent.say(st);
        }

    }

    public Bid computeBid() {

        if (offers.get(internalCounter).passo()) {
            return offers.get(internalCounter);
        }

        if (bestBid == null) {
            return offers.get(internalCounter);
        }

        while (!offers.get(internalCounter).passo() && offers.get(
            internalCounter).getValue() >= bestBid.getValue()) {
            ++internalCounter;
        }

        return offers.get(internalCounter);

    }

    public Suit computeBriscola() {
        return suitsOffers.get(internalCounter);
    }

    public int counter() {
        return counter;
    }

}
