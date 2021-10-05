package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class LOrExp extends Node {
    public LOrExp() {
        typ = NTyp.LOrExp;
    }

    /* LOrExp → LAndExp { '||' LAndExp } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
