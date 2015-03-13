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

    //  ROLES NAMES
    public static final String ROLE_GIAGUARO = "giaguaro";
    public static final String ROLE_VILLANO = "villano";
    public static final String ROLE_SOCIO = "socio";

    //  STRATEGIES
    public static final String STRATEGY_RANDOM_NAME = "Casuale";
    public static final String STRATEGY_RANDOM_FILE = "/home/mat/school/Tesi/src/briscola/reasoner/player.clp";

    public static final String STRATEGY_NORMAL_NAME = "Base";
    public static final String STRATEGY_NORMAL_FILE = "/home/mat/school/Tesi/src/briscola/reasoner/player.clp";

    public static final String[] playerNames
        = {
            "Abelardo",
            "Adelino",
            "Adriano",
            "Alfonso",
            "Alfredo",
            "Attilio",
            "Giacomo",
            "Gino",
            "Agata",
            "Agnese",
            "Adriana",
            "Antonella",
            "Diocleziano",
            "Dante",
            "Dionisio",
            "Dianora",
            "Druina",
            "Domitilla",
            "Omero",
            "Nazzareno",
            "Nicoletta",
            "Ombretta",
            "Noemi",
            "Osvaldo",
            "Odilia",
            "Ofelia",
            "Ottilia"
        };

    public static String randomName() {
        if (random == null) {
            random = new Random();
        }
        return playerNames[random.nextInt(playerNames.length)];
    }

}
