package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class ConstExp extends Node {
    public ConstExp() {
        typ = NTyp.ConstExp;
    }

    /* ConstExp → AddExp  MulExp { ('+' | '−') MulExp } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
