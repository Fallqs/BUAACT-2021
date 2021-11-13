package lex.nd;

import lex.NTyp;
import lex.New;
import lex.Node;
import lex.node.InitVal;
import meta.Meta;
import meta.mcode.Get;
import meta.midt.MTable;
import meta.midt.MVar;
import word.Typ;

public class Def extends Node {
    private Ref ref;
    private Node val;
    private final NTyp vtyp;

    public Def(boolean cnst) {
        this.typ = cnst ? NTyp.ConstDef : NTyp.VarDef;
        vtyp = cnst ? NTyp.ConstInitVal : NTyp.InitVal;
    }

    public Def(boolean cnst, Ref ref, Node val) {
        this(cnst);
        this.ref = ref;
        this.val = val;
    }

    @Override
    public boolean forward() {
        if (ref != null) return true;
        if (!(ref = new Ref(false, true)).forward()) return false;
        if (cs.isTyp(Typ.ASSIGN)) {
            cs.nex();
            (val = New.typ(vtyp)).fwd();
        }
        return true;
    }

    public void logIdt() {
        idt.cur.buf.onDecl = true;
        idt.cur.buf.cnst = (typ == NTyp.ConstDef);
        ref.logIdt();
        if (!idt.cur.addVar(false)) cs.chkErr(Typ.IDENFR, ref.name);
        idt.cur.buf.onDecl = false;
    }

    @Override
    public Meta translate() {
        MVar var = ((Get) ref.translate()).var;
        var.cnst = typ == NTyp.ConstDef;
        MTable.newIdt(var);
        if(!(val instanceof InitVal)) return null;
        return ((InitVal) val).translate(var);
    }
}
