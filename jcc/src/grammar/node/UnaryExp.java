package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import grammar.nd.Ref;
import meta.Meta;
import meta.ident.Var;
import word.Typ;

import java.util.ArrayList;

public class UnaryExp extends Node {
    private final ArrayList<Node> ops = new ArrayList<>();
    private Node obj;

    public UnaryExp() {
        typ = NTyp.UnaryExp;
    }

    /* UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp */
    @Override
    public boolean forward() {
        Node op;
        while ((op = New.typ(NTyp.UnaryOp)).fwd()) {
            ops.add(op);
        }
        if (cs.isTyp(Typ.LPARENT)) {
            cs.nex();
            (obj = New.typ(NTyp.Exp)).fwd();
            cs.chkErr(Typ.RPARENT).nex();
            dump(NTyp.PrimaryExp);
        } else if ((obj = New.typ(NTyp.Number)).fwd()) {
            dump(NTyp.PrimaryExp);
        } else if ((obj = new Ref(false, false)).fwd()) {
            if (obj.gettyp() == NTyp.LVal) {
                dump(NTyp.LVal);
                dump(NTyp.PrimaryExp);
            }
        } else return false;
        for (Node i : ops) dump(typ);
        return true;
    }

    @Override
    public void logIdt() {
        obj.logIdt();
    }

    @Override
    public Var rets() {
        if (ops.size() != 0) {
            return obj.rets().cnst ? new Var(0).setCnst(true) : new Var(0);
        }
        return obj.rets();
    }

    @Override
    public Meta compile() {
        return null;
    }
}
