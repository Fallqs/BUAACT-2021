package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import meta.Opr;
import meta.mcode.Calc;
import word.Typ;

import java.util.ArrayList;

public class LOrExp extends Node {
    private ArrayList<Node> and = new ArrayList<>();
    private Node bl0;

    public LOrExp() {
        typ = NTyp.LOrExp;
        autoDisplay = false;
    }

    /* LOrExp → LAndExp { '||' LAndExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.LAndExp);
        if (!ch.fwd()) return false;
        bl0 = ch;
        dump(typ);
        while (cs.isTyp(Typ.OR)) {
            cs.nex();
            if (!(ch = New.typ(NTyp.LAndExp)).fwd()) break;
            and.add(ch);
            dump(typ);
        }
        return true;
    }

    @Override
    public void logIdt() {
        for (Node i : and) i.logIdt();
    }

    /**
     * Short-cut Deducing Unimplemented
     * @return new Calc
     */
    @Override
    public Meta translate() {
        Meta ret = new Calc(Opr.not, bl0.translate());
        for (Node o : and) ret = new Calc(Opr.and, ret, new Calc(Opr.not, o.translate()));
        return new Calc(Opr.not, ret, Meta.Nop);
    }
}
