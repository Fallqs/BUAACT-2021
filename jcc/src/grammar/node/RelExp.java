package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
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
        return null;
    }
}
