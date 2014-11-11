/*
 */
package briscola.common;

import java.util.Random;

/**
 *
 * @author mat
 */
public class Names {

    //public static final int WAIT_TO_PLAY = 10000;
    public static final int WAIT_FOR_CONFIRMATION = 10000;
    public static final long RETRY_EVERY = 5000;
    public static final int ACL_CHAT = 99;
    public static final int ACL_SEND_PLAYERS = 100;
    public static final int ACL_SEND_CHAT_ID = 101;
    public static final int ACL_MESSAGE_RECEIVED = 200;
    public static final int ACL_YOUR_HAND = 110;
    public static final String UNKNOWN = "intruso";
    public static final String MAZZIERE = "mazziere";
    private static Random random;

    public static final String CARD_IMG_EXTENSION = ".jpg";
    public static final String CARD_IMG_FOLDER = "/home/mat/school/Tesi/src/briscola/images/cards/";

    //  WORD BETWEEN RANK AND SUI
    public static final String CARD_OF = "";

    //  RANKS NAMES
    public static final String CARD_ACE = "A";
    public static final String CARD_DEUCE = "2";
    public static final String CARD_THREE = "3";
    public static final String CARD_FOUR = "4";
    public static final String CARD_FIVE = "5";
    public static final String CARD_SIX = "6";
    public static final String CARD_SEVEN = "7";
    public static final String CARD_JACK = "J";
    public static final String CARD_QUEEN = "Q";
    public static final String CARD_KING = "K";
    //  SUITS NAMES
    public static final String CARD_CLUBS = "♠";
    public static final String CARD_DIAMONDS = "♦";
    public static final String CARD_SPADES = "♣";
    public static final String CARD_HEARTS = "♥";

    public static final String[] playerNames
        = {
            "Abelardo",
            "Adelino",
            "Adriano",
            "Alfonso",
            "Alfredo",
            "Attilio",
            "Giacomo",
            "Gino"
        };

    public static String randomName() {
        if (random == null) {
            random = new Random();
        }
        return playerNames[random.nextInt(playerNames.length)];
    }

}
