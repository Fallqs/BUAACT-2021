package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import meta.Opr;
import meta.mcode.Calc;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class EqExp extends Node {
    private final ArrayList<Node> rel = new ArrayList<>();
    private final ArrayList<Result> opr = new ArrayList<>();

    public EqExp() {
        typ = NTyp.EqExp;
        autoDisplay = false;
    }

    /* EqExp → RelExp { ('==' | '!=') RelExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.RelExp);
        if (!ch.fwd()) return false;
        opr.add(null);
        rel.add(ch);
        dump(typ);
        while (cs.isTyp(Typ.EQL) || cs.isTyp(Typ.NEQ)) {
            opr.add(cs.cont());
            cs.nex();
            if (!(ch = New.typ(NTyp.RelExp)).fwd()) break;
            rel.add(ch);
            dump(typ);
        }
        return true;
    }

    @Override
    public void logIdt() {
        for (Node i : rel) i.logIdt();
    }

    @Override
    public Meta translate() {
        Meta ret = rel.get(0).translate();
        for (int i = 1; i < rel.size(); ++i)
            ret = new Calc(opr.get(i).typ == Typ.EQL ? Opr.eql : Opr.neq, ret, rel.get(i).translate());
        return ret;
    }
}
