package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class LAndExp extends Node {
    public LAndExp() {
        typ = NTyp.LAndExp;
    }

    /* LAndExp → EqExp { '&&' EqExp } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
