package grammar.nd;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class StmtLR extends Node {
    private Node ref, calc;

    public StmtLR() {
        autoDisplay = false;
    }

    @Override
    public boolean forward() {
        int p = cs.p;
        if ((ref = new Ref(false, false)).forward() && cs.isTyp(Typ.ASSIGN)) {
            dump(NTyp.LVal);
            cs.nex();
            (calc = New.typ(NTyp.Exp)).fwd();
            while (!cs.isTyp(Typ.SEMICN)) cs.nex();
            cs.nex();
            return true;
        }
        cs.p = p;
        ref = null;
        if ((calc = New.typ(NTyp.Exp)).fwd()) {
            while(!cs.isTyp(Typ.SEMICN))cs.nex();
            cs.nex();
            return true;
        }
        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
