package briscola.objects;

import static briscola.common.Names.ROLE_GIAGUARO;
import static briscola.common.Names.ROLE_SOCIO;
import static briscola.common.Names.ROLE_VILLANO;
import java.io.Serializable;

public enum Role implements Serializable {

    GIAGUARO(ROLE_GIAGUARO),
    VILLANO(ROLE_VILLANO),
    SOCIO(ROLE_SOCIO);

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    private final String name;

}
