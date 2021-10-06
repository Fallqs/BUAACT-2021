package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Result;
import word.Typ;

import java.util.ArrayList;
import java.util.List;

public class LVal extends Node {
    Result ident;
    ArrayList<Node> index = new ArrayList<>();
    private boolean cnst = false;

    public LVal() {
        typ = NTyp.LVal;
    }

    public LVal(boolean cnst) {
        typ = NTyp.NULL;
        this.cnst = cnst;
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
            if (!cs.isTyp(Typ.RBRACK)) break;
            cs.nex();
        }
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
