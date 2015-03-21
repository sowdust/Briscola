package briscola.objects;

import static briscola.common.Names.STRATEGY_MANUAL_FILE;
import static briscola.common.Names.STRATEGY_MANUAL_NAME;
import static briscola.common.Names.STRATEGY_NORMAL_FILE;
import static briscola.common.Names.STRATEGY_NORMAL_NAME;
import static briscola.common.Names.STRATEGY_RANDOM_FILE;
import static briscola.common.Names.STRATEGY_RANDOM_NAME;

public enum Strategy {

    RANDOM(STRATEGY_RANDOM_NAME, STRATEGY_RANDOM_FILE, true),
    NORMAL(STRATEGY_NORMAL_NAME, STRATEGY_NORMAL_FILE, false),
    MANUAL(STRATEGY_MANUAL_NAME, STRATEGY_MANUAL_FILE, false);

    private final boolean random;
    private final String name;
    private final String fileName;

    Strategy(String name, String fileName, boolean random) {
        this.name = name;
        this.fileName = fileName;
        this.random = random;
    }

    public String getFile() {
        return fileName;
    }

    public String getName() {
        return name;
    }

    public boolean random() {
        return random;
    }

    @Override
    public String toString() {
        return "Strategia " + name + " [file: " + fileName + " ]. Rand: " + random;
    }

}
