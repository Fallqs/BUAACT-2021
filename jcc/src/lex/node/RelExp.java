package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import meta.Opr;
import meta.mcode.Calc;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class RelExp extends Node {
    private ArrayList<Node> exp = new ArrayList<>();
    private ArrayList<Result> opr = new ArrayList<>();

    public RelExp() {
        typ = NTyp.RelExp;
        autoDisplay = false;
    }

    /* RelExp → AddExp { ('<' | '>' | '<=' | '>=') AddExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.AddExp);
        if (!ch.fwd()) return false;
        opr.add(null);
        exp.add(ch);
        dump(typ);
        while (cs.isTyp(Typ.LSS) || cs.isTyp(Typ.LEQ) || cs.isTyp(Typ.GRE) || cs.isTyp(Typ.GEQ)) {
            opr.add(cs.cont());
            cs.nex();
            if (!(ch = New.typ(NTyp.AddExp)).fwd()) break;
            exp.add(ch);
            dump(typ);
        }
        return true;
    }

    @Override
    public void logIdt() {
        for (Node i : exp) i.logIdt();
    }

    @Override
    public Meta translate() {
        Meta ret = exp.get(0).translate();
        for (int i = 1; i < exp.size(); ++i) {
            Typ t = opr.get(i).typ;
            if (t == Typ.LSS) ret = new Calc(Opr.lt, ret, exp.get(i).translate());
            else if (t == Typ.LEQ) ret = new Calc(Opr.not, new Calc(Opr.gt, ret, exp.get(i).translate()));
            else if (t == Typ.GEQ) ret = new Calc(Opr.not, new Calc(Opr.lt, ret, exp.get(i).translate()));
            else ret = new Calc(Opr.gt, ret, exp.get(i).translate());
        }
        return ret;
    }
}
