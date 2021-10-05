package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class MulExp extends Node {
    public MulExp() {
        typ = NTyp.MulExp;
    }

    /* MulExp → UnaryExp { ('*' | '/' | '%') UnaryExp } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
