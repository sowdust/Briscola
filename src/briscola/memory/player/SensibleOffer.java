package briscola.memory.player;

import briscola.objects.Rank;
import static briscola.objects.Rank.ACE;
import static briscola.objects.Rank.DEUCE;
import static briscola.objects.Rank.FIVE;
import static briscola.objects.Rank.FOUR;
import static briscola.objects.Rank.JACK;
import static briscola.objects.Rank.KING;
import static briscola.objects.Rank.QUEEN;
import static briscola.objects.Rank.SEVEN;
import static briscola.objects.Rank.SIX;
import static briscola.objects.Rank.THREE;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class SensibleOffer {

    public final int howMany;
    public final Rank minimum;
    public final List<Rank> mustHave;
    private static final Random rand = new Random();
    private static final int MAX_BLUFF = 10;

    SensibleOffer(int howMany, Rank minimum, List<Rank> mustHave) {
        this.howMany = howMany;
        this.minimum = minimum;
        this.mustHave = mustHave;
    }

    public static SensibleOffer getOffer(int[] distr, int punteggio) {

        List<Rank> mustHave = new LinkedList<>();
        int howMany = 0;
        Rank minimum = ACE;
        int probability;

        if (Arrays.equals(distr, new int[]{8, 0, 0, 0})) {

            if (punteggio > 50) {

            } else if (punteggio > 45) {

            } else if (punteggio > 40) {

            } else if (punteggio > 35) {

            } else if (punteggio > 30) {

            } else if (punteggio > 25) {
                minimum = DEUCE;
            } else if (punteggio > 20) {
                minimum = DEUCE;
            } else if (punteggio > 15) {
                minimum = FOUR;
            } else {
                probability = rand.nextInt(MAX_BLUFF);
                if (rand.nextInt(probability) == 1) {
                    minimum = Rank.values()[rand.nextInt(
                        Rank.getValues().size())];
                } else {
                    howMany = -1;
                }
            }

        } else if (Arrays.equals(distr, new int[]{7, 1, 0, 0})) {
            if (punteggio > 50) {

            } else if (punteggio > 45) {

            } else if (punteggio > 40) {
                minimum = DEUCE;
            } else if (punteggio > 35) {
                minimum = DEUCE;
            } else if (punteggio > 30) {
                minimum = DEUCE;
            } else if (punteggio > 25) {
                minimum = FOUR;
            } else if (punteggio > 20) {
                minimum = FIVE;
            } else if (punteggio > 15) {
                minimum = SIX;
            } else {
                howMany = -1;
            }
        } else if (Arrays.equals(distr, new int[]{6, 2, 0, 0})
            || Arrays.equals(distr, new int[]{6, 1, 1, 0})) {
            if (punteggio > 50) {
                minimum = DEUCE;
            } else if (punteggio > 45) {
                minimum = DEUCE;
            } else if (punteggio > 40) {
                minimum = DEUCE;
            } else if (punteggio > 35) {
                minimum = DEUCE;
            } else if (punteggio > 30) {
                minimum = FOUR;
            } else if (punteggio > 25) {
                minimum = FIVE;
            } else if (punteggio > 20) {
                minimum = SIX;
            } else if (punteggio > 15) {
                minimum = SEVEN;
            } else {
                probability = rand.nextInt(MAX_BLUFF);
                if (rand.nextInt(probability) == 1) {
                    minimum = Rank.values()[rand.nextInt(
                        Rank.getValues().size())];
                } else {
                    howMany = -1;
                }
            }
        } else if (Arrays.equals(distr, new int[]{5, 3, 0, 0})
            || Arrays.equals(distr, new int[]{5, 2, 1, 0})
            || Arrays.equals(distr, new int[]{5, 1, 1, 1})) {
            if (punteggio > 50) {
                minimum = DEUCE;
            } else if (punteggio > 45) {
                minimum = DEUCE;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 40) {
                minimum = DEUCE;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 35) {
                minimum = FOUR;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 30) {
                minimum = FIVE;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 25) {
                minimum = SIX;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 20) {
                minimum = SEVEN;
                mustHave.add(ACE);
                mustHave.add(THREE);
                howMany = 1;
            } else if (punteggio > 15) {
                minimum = JACK;
                mustHave.add(ACE);
                mustHave.add(THREE);
                howMany = 1;
            } else {
                probability = rand.nextInt(MAX_BLUFF);
                if (rand.nextInt(probability) == 1) {
                    minimum = Rank.values()[rand.nextInt(
                        Rank.getValues().size())];
                } else {
                    howMany = -1;
                }
            }
        } else if (Arrays.equals(distr, new int[]{4, 4, 0, 0})
            || Arrays.equals(distr, new int[]{4, 3, 1, 0})
            || Arrays.equals(distr, new int[]{4, 2, 2, 0})
            || Arrays.equals(distr, new int[]{4, 2, 1, 1})) {
            if (punteggio > 50) {
                minimum = DEUCE;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 45) {
                minimum = DEUCE;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 40) {
                minimum = FOUR;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 35) {
                minimum = FIVE;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 30) {
                minimum = SIX;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 25) {
                minimum = SEVEN;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 20) {
                minimum = JACK;
                mustHave.add(ACE);
                mustHave.add(THREE);
                howMany = 1;
            } else if (punteggio > 15) {
                minimum = QUEEN;
                mustHave.add(ACE);
                mustHave.add(THREE);
                howMany = 1;
            } else {
                probability = rand.nextInt(MAX_BLUFF);
                if (rand.nextInt(probability) == 1) {
                    minimum = Rank.values()[rand.nextInt(
                        Rank.getValues().size())];
                } else {
                    howMany = -1;
                }
            }
        } else if (Arrays.equals(distr, new int[]{3, 3, 2, 0})
            || Arrays.equals(distr, new int[]{3, 3, 1, 1})
            || Arrays.equals(distr, new int[]{3, 2, 2, 1})) {
            if (punteggio > 50) {
                minimum = DEUCE;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 45) {
                minimum = FOUR;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 40) {
                minimum = FIVE;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 35) {
                minimum = SIX;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 30) {
                minimum = SEVEN;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 25) {
                minimum = JACK;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 1;
            } else if (punteggio > 20) {
                minimum = QUEEN;
                mustHave.add(ACE);
                mustHave.add(THREE);
                howMany = 1;
            } else if (punteggio > 15) {
                minimum = KING;
                mustHave.add(ACE);
                mustHave.add(THREE);
                howMany = 1;
            } else {
                probability = rand.nextInt(MAX_BLUFF);
                if (rand.nextInt(probability) == 1) {
                    minimum = Rank.values()[rand.nextInt(
                        Rank.getValues().size())];
                } else {
                    howMany = -1;
                }
            }
        } else if (Arrays.equals(distr, new int[]{2, 2, 2, 2})) {
            if (punteggio > 50) {
                minimum = DEUCE;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 2;
            } else if (punteggio > 45) {
                minimum = FOUR;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 2;
            } else if (punteggio > 40) {
                minimum = FOUR;
                mustHave.add(ACE);
                mustHave.add(THREE);
                mustHave.add(KING);
                howMany = 2;
            } else if (punteggio > 35) {
                minimum = FIVE;
                mustHave.add(ACE);
                mustHave.add(THREE);
                howMany = 2;
            } else if (punteggio > 30) {
                minimum = SIX;
                mustHave.add(ACE);
                mustHave.add(THREE);
                howMany = 2;
            } else if (punteggio > 25) {
                minimum = SEVEN;
                mustHave.add(ACE);
                mustHave.add(THREE);
                howMany = 2;
            } else if (punteggio > 20) {
                minimum = JACK;
                mustHave.add(ACE);
                mustHave.add(THREE);
                howMany = 2;
            } else if (punteggio > 15) {
                howMany = -1;
            } else {
                probability = rand.nextInt(MAX_BLUFF);
                if (rand.nextInt(probability) == 1) {
                    minimum = Rank.values()[rand.nextInt(
                        Rank.getValues().size())];
                } else {
                    howMany = -1;
                }
            }
        }
        return new SensibleOffer(howMany, minimum, mustHave);
    }

}
