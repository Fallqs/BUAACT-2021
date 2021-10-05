package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class Exp extends Node {
    public Exp() {
        typ = NTyp.Exp;
    }

    /* Exp → AddExp → MulExp { ('+' | '−') MulExp } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
