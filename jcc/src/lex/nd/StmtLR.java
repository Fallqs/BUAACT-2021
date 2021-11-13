package lex.nd;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import word.Typ;

public class StmtLR extends Node {
    private Node calc;
    private Ref ref;

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
    public void logIdt() {
        if(ref != null)ref.logIdt();
        calc.logIdt();
        if(ref != null && ref.rets().cnst) cs.chkErr(Typ.CONSTTK, ref.name);
    }

    @Override
    public Meta translate() {
        Meta fr = calc.translate();
        if (ref != null) ref.translate(fr);
        return fr;
    }
}
