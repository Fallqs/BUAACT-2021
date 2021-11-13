package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;

public class ConstExp extends Node {
    private Node exp;

    public ConstExp() {
        typ = NTyp.ConstExp;
    }

    /* ConstExp → AddExp  MulExp { ('+' | '−') MulExp } */
    @Override
    public boolean forward() {
        return (exp = New.typ(NTyp.AddExp)).fwd();
    }

    @Override
    public void logIdt() {
        exp.logIdt();
    }

    @Override
    public Meta translate() {
        return exp.translate();
    }
}
