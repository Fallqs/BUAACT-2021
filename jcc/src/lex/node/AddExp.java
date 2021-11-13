package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import meta.Opr;
import meta.ident.Var;
import meta.mcode.Calc;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class AddExp extends Node {
    private final ArrayList<Node> mult = new ArrayList<>();
    private final ArrayList<Result> opr = new ArrayList<>();

    public AddExp() {
        typ = NTyp.AddExp;
        autoDisplay = false;
    }

    /* AddExp → MulExp { ('+' | '−') MulExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.MulExp);
        if (!ch.fwd()) return false;
        opr.add(new Result(Typ.PLUS, 0));
        mult.add(ch);
        dump(typ);
        while (cs.isTyp(Typ.PLUS) || cs.isTyp(Typ.MINU)) {
            opr.add(cs.cont());
            cs.nex();
            if (!(ch = New.typ(NTyp.MulExp)).fwd()) break;
            mult.add(ch);
            dump(typ);
        }
        return true;
    }

    public void logIdt() {
        for (Node i : mult) i.logIdt();
    }

    @Override
    public Var rets() {
        if (mult.size() > 1) {
            for (Node i : mult) if (!i.rets().cnst) return new Var(0);
            return new Var(0).setCnst(true);
        }
        return mult.get(0).rets();
    }

    @Override
    public Meta translate() {
        if (mult.isEmpty()) return Meta.Nop;
        Meta ret = mult.get(0).translate();
        for (int i = 1; i < mult.size(); ++i) {
            ret = new Calc(
                    opr.get(i).typ == Typ.PLUS ? Opr.add : Opr.sub,
                    ret, mult.get(i).translate()
            );
        }
        return ret;
    }
}
