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
        int siz = output.size();
        if ((ref = new Ref(false, false)).forward() && cs.isTyp(Typ.ASSIGN)) {
            dump(NTyp.LVal);
            cs.nex();
            if (!(calc = New.typ(NTyp.Getint)).forward())
                (calc = New.typ(NTyp.Exp)).fwd();
            cs.chkErr(Typ.SEMICN).nex();
            return true;
        }
        cs.p = p;
        while (output.size() > siz) output.remove(output.size() - 1);
        ref = null;
        if ((calc = New.typ(NTyp.Exp)).fwd()) {
            cs.chkErr(Typ.SEMICN).nex();
            return true;
        }
        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
