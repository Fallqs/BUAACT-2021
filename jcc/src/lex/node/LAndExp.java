package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import meta.Opr;
import meta.mcode.Calc;
import word.Typ;

import java.util.ArrayList;

public class LAndExp extends Node {
    private ArrayList<Node> bl = new ArrayList<>();
    private Node bl0;

    public LAndExp() {
        typ = NTyp.LAndExp;
        autoDisplay = false;
    }

    /* LAndExp → EqExp { '&&' EqExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.EqExp);
        if (!ch.fwd()) return false;
        bl0 = ch;
        dump(typ);
        while (cs.isTyp(Typ.AND)) {
            cs.nex();
            if (!(ch = New.typ(NTyp.EqExp)).fwd()) break;
            bl.add(ch);
            dump(typ);
        }
        return true;
    }

    @Override
    public void logIdt() {
        for (Node i : bl) i.logIdt();
    }

    @Override
    public Meta translate() {
        Meta ret = new Calc(Opr.not, bl0.translate());
        for (Node o : bl) ret = new Calc(Opr.or, ret, new Calc(Opr.not, o.translate()));
        return new Calc(Opr.not, ret, Meta.Nop);
    }
}
