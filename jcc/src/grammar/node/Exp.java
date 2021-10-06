package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;

public class Exp extends Node {
    private Node exp;

    public Exp() {
        typ = NTyp.Exp;
    }

    /* Exp → AddExp → MulExp { ('+' | '−') MulExp } */
    @Override
    public boolean forward() {
        return (exp = New.typ(NTyp.AddExp)).fwd();
    }

    @Override
    public Meta compile() {
        return null;
    }
}