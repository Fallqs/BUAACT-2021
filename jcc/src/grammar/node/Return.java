package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import meta.ident.Var;
import word.Typ;

public class Return extends Node {
    private Node val;
    private int pos;

    public Return() {
        typ = NTyp.Return;
        autoDisplay = false;
    }

    /* 'return' [Exp] ';' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.RETURNTK)) return false;
        pos = cs.p;
        cs.nex();
        if (!(val = New.typ(NTyp.Exp)).fwd()) val = null;
        cs.chkErr(Typ.SEMICN).nex();
        return true;
    }

    @Override
    public void logIdt() {
        if (idt.cur.ret == Typ.VOIDTK && val != null) cs.chkErr(Typ.RETURNTK, pos);
        if (val != null) val.logIdt();
    }

    @Override
    public Var rets() {
        return val == null ? new Var(0) : val.rets();
    }

    @Override
    public Meta compile() {
        return null;
    }
}
