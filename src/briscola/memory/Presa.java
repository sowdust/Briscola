package briscola.memory;

import briscola.Player;
import briscola.objects.Role;

public class Presa {

    private final int mano;
    private final Player player;
    private final int score;
    private final Role role;

    public Presa(int mano, Player player, Role role, int score) {
        this.mano = mano;
        this.player = player;
        this.score = score;
        this.role = role;
    }

    public String[] toStringArray() {
        String[] s = new String[4];
        s[0] = Integer.toString(mano);
        s[1] = player.getName();
        if (role != null) {
            s[2] = role.toString();
        } else {
            s[2] = "";
        }
        s[3] = Integer.toString(score);
        return s;
    }

}
