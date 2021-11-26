package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import meta.Opr;
import meta.mcode.Bpi;
import meta.mcode.Brp;
import meta.mcode.Calc;
import meta.midt.MPin;
import word.Typ;

import java.util.ArrayList;

public class LOrExp extends Node {
    private final ArrayList<Node> and = new ArrayList<>();
//    private Node bl0;

    public LOrExp() {
        typ = NTyp.LOrExp;
        autoDisplay = false;
    }

    /* LOrExp → LAndExp { '||' LAndExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.LAndExp);
        if (!ch.fwd()) return false;
        and.add(ch);
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
//    @Override
//    public Meta translate() {
//        Meta ret = new Calc(Opr.not, bl0.translate());
//        for (Node o : and) ret = new Calc(Opr.and, ret, new Calc(Opr.not, o.translate()));
//        return new Calc(Opr.not, ret, Meta.Nop);
//    }

    public Meta translate(MPin pThen, MPin pEls) {
        if (and.size() <= 1) {
            Meta cond = ((LAndExp) and.get(0)).translate(pThen, pEls);
            if (cond != null) {
                new Brp(cond, null, pEls);
                new Brp(null, pThen, null);
            }
        } else {
            for (Node o : and) {
                MPin tEls = new MPin("");
                Meta tCalc = ((LAndExp) o).translate(pThen, tEls);
                if (tCalc != null) new Brp(tCalc, pThen, null);
                else new Bpi(tEls);
            }
            new Brp(null, pEls, null);
        }
        return null;
    }
}
