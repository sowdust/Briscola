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
    public static final String UNKNOWN = "intruso";
    public static final String MAZZIERE = "mazziere";
    private static Random random;

    public static final String[] playerNames = {
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
        return playerNames[random.nextInt((playerNames.length - 0) + 1)];
    }

}
