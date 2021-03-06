package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import meta.ident.Var;

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
    public void logIdt() {
        exp.logIdt();
    }

    @Override
    public Var rets() {
        return exp.rets();
    }

    @Override
    public Meta translate() {
        return exp.translate();
    }
}
