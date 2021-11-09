package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class EqExp extends Node {
    private ArrayList<Node> rel = new ArrayList<>();
    private ArrayList<Result> opr = new ArrayList<>();

    public EqExp() {
        typ = NTyp.EqExp;
        autoDisplay = false;
    }

    /* EqExp → RelExp { ('==' | '!=') RelExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.RelExp);
        if (!ch.fwd()) return false;
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
        return null;
    }
}
