package meta.mcode;

import meta.Meta;

public class Cin extends Meta {
    public Cin() {
        valid = true;
    }

    @Override
    public boolean isCnst() {
        return cnst = false;
    }

    @Override
    public String toString() {
        return "cin >> " + this.id;
    }
}
