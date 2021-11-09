package grammar.nd;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import grammar.node.BType;
import meta.Meta;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class Decl extends Node {
    private boolean cnst;
    private Ref ref;
    private Node init;
    private final ArrayList<Node> defs = new ArrayList<>();

    public Decl() {
        typ = NTyp.VarDecl;
    }

    @Override
    public boolean forward() {
        if (cs.isTyp(Typ.CONSTTK)) {
            cnst = true;
            cs.nex();
            this.typ = NTyp.ConstDecl;
        }
        if (!(new BType(this).fwd())) return false;  // value type stored in (Result)this.key
        cs.nex();
        (ref = new Ref(false, true)).forward();
        if (ref.gettyp() == NTyp.FuncDef) {
            typ = NTyp.FuncDef;
            dump(NTyp.FuncType, key.p + 1);
            return true;
        } else if (ref.gettyp() == NTyp.MainFuncDef) {
            typ = NTyp.MainFuncDef;
            return true;
        }
        if (cs.isTyp(Typ.ASSIGN)) {
            cs.nex();
            (init = New.typ(cnst ? NTyp.ConstInitVal : NTyp.InitVal)).fwd();
        }

        Node def = new Def(cnst, ref, init);
        def.fwd();
        defs.add(def);
        while (cs.isTyp(Typ.COMMA)) {
            cs.nex();
            if (!(def = new Def(cnst)).fwd()) break;
            defs.add(def);
        }
        cs.chkErr(Typ.SEMICN).nex();
        return true;
    }

    @Override
    public void logIdt() {
        if (typ == NTyp.ConstDecl || typ == NTyp.VarDecl) {
            idt.cur.buf.zero = (idt.cur == idt.sup);
            for (Node def : defs) def.logIdt();
        } else {
            idt.newEnv().buf.onDecl = true;
            idt.cur.setRet(key.typ);
            ref.logIdt();
            idt.cur.buf.onDecl = false;
            idt.cur = idt.sup;
        }
    }

    @Override
    public Meta translate() {
        return null;
    }
}
