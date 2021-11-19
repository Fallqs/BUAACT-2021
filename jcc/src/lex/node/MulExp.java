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

public class MulExp extends Node {
    private final ArrayList<Node> uny = new ArrayList<>();
    private final ArrayList<Result> opr = new ArrayList<>();

    public MulExp() {
        typ = NTyp.MulExp;
        autoDisplay = false;
    }

    /**
     * MulExp → UnaryExp { ([*%/]) UnaryExp }
     */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.UnaryExp);
        if (!ch.fwd()) return false;
        uny.add(ch);
        opr.add(null);
        dump(typ);
        while (cs.isTyp(Typ.MULT) || cs.isTyp(Typ.DIV) || cs.isTyp(Typ.MOD)) {
            opr.add(cs.cont());
            cs.nex();
            if (!(ch = New.typ(NTyp.UnaryExp)).fwd()) break;
            uny.add(ch);
            dump(typ);
        }
        return true;
    }

    @Override
    public void logIdt() {
        for (Node i : uny) i.logIdt();
    }

    @Override
    public Var rets() {
        if (uny.size() > 1) {
            for (Node i : uny) if (!i.rets().cnst) return new Var(0);
            return new Var(0).setCnst(true);
        }
        return uny.get(0).rets();
    }

    @Override
    public Meta translate() {
        Meta ret = uny.get(0).translate();
        for (int i = 1; i < uny.size(); ++i) {
            Typ t = opr.get(i).typ;
            ret = new Calc(t == Typ.MULT ? Opr.mult : t == Typ.DIV ? Opr.div : Opr.mod, ret, uny.get(i).translate());
        }
        return ret;
    }
}
