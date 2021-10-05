package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;
import word.Result;
import word.Typ;

public class UnaryOp extends Node {
    private Result opr;

    public UnaryOp() {
        typ = NTyp.UnaryOp;
    }

    /* UnaryOp → '+' | '−' | '!' */
    @Override
    public boolean forward() {
        if (cs.isTyp(Typ.PLUS) || cs.isTyp(Typ.MINU) || cs.isTyp(Typ.NOT)) {
            opr = cs.cont();
            cs.nex();
            return true;
        }
        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
