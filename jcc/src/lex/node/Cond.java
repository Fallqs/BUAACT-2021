package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;

public class Cond extends Node {
    private Node logi;

    public Cond() {
        typ = NTyp.Cond;
    }

    /* Cond → LOrExp → LAndExp { '||' LAndExp } */
    @Override
    public boolean forward() {
        return (logi = New.typ(NTyp.LOrExp)).fwd();
    }

    @Override
    public void logIdt() {
        logi.logIdt();
    }

    @Override
    public Meta translate() {
        return logi.translate();
    }
}
