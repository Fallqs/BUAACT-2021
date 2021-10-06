package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import grammar.nd.Ref;
import meta.Meta;
import word.Typ;

public class UnaryExp extends Node {
    private Node op, obj;

    public UnaryExp() {
        typ = NTyp.UnaryExp;
    }

    /* UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp */
    @Override
    public boolean forward() {
        if (!(op = New.typ(NTyp.UnaryOp)).fwd()) op = null;
        if (cs.isTyp(Typ.LPARENT)) {
            cs.nex();
            (obj = New.typ(NTyp.Exp)).fwd();
            while (!cs.isTyp(Typ.RPARENT)) cs.nex();
            cs.nex();
            return true;
        } else if ((obj = New.typ(NTyp.Number)).fwd()) {
            dump(NTyp.PrimaryExp);
            return true;
        } else if ((obj = new Ref(autoDisplay, false)).fwd()) {
            if (obj.gettyp() == NTyp.LVal) dump(NTyp.PrimaryExp);
            return true;
        }
        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
