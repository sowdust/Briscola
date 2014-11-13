package briscola.objects;

import java.io.Serializable;

public enum BidOld implements Serializable {

    PASSO(-1),
    DEUCE(1),
    FOUR(2),
    FIVE(3),
    SIX(4),
    SEVEN(5),
    JACK(6),
    QUEEN(7),
    KING(8),
    THREE(9),
    ACE(10);

    private final Integer x;

    BidOld(Integer x) {
        this.x = x;
    }

    public Integer getValue() {
        return x;
    }

}
