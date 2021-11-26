package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import meta.Opr;
import meta.mcode.Brp;
import meta.mcode.Calc;
import meta.midt.MPin;
import word.Typ;

import java.util.ArrayList;

public class LAndExp extends Node {
    private final ArrayList<Node> bl = new ArrayList<>();
//    private Node bl0;

    public LAndExp() {
        typ = NTyp.LAndExp;
        autoDisplay = false;
    }

    /* LAndExp → EqExp { '&&' EqExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.EqExp);
        if (!ch.fwd()) return false;
        bl.add(ch);
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

    /**
     * Short-cut Deducing Unimplemented
     * @return new Calc
     */
//    @Override
//    public Meta translate() {
//        Meta ret = new Calc(Opr.not, bl0.translate());
//        for (Node o : bl) ret = new Calc(Opr.or, ret, new Calc(Opr.not, o.translate()));
//        return new Calc(Opr.not, ret, Meta.Nop);
//    }

    public Meta translate(MPin pThen, MPin pEls) {
        if (bl.size() <= 1) return bl.get(0).translate();
        for (Node o : bl) new Brp(o.translate(), null, pEls);
        new Brp(null, pThen, null);
        return null;
    }
}
