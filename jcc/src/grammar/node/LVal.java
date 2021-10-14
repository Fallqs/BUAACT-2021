package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class LVal extends Node {
    Result ident;
    ArrayList<Node> index = new ArrayList<>();

    public LVal() {
        typ = NTyp.LVal;
    }

    public LVal(NTyp t) {
        typ = t;
    }

    /**
     * LVal → Ident {'[' Exp ']'}
     * Ident: IDENFR; '[]': LBRACK, RBRACK;
     */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.IDENFR)) return false;
        ident = cs.cont();
        cs.nex();
        while (cs.isTyp(Typ.LBRACK)) {
            cs.nex();
            Node ind = New.typ(NTyp.Exp);
            if (!ind.fwd()) break;
            index.add(ind);
            cs.chkErr(Typ.RBRACK).nex();
        }
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
