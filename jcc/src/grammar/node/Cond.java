package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class Cond extends Node {
    public Cond() {
        typ = NTyp.Cond;
    }

    /* Cond → LOrExp → LAndExp { '||' LAndExp } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
