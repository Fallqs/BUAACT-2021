package grammar.nd;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import grammar.node.ConstDecl;
import meta.Meta;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class Decl extends Node {
    private Result vtyp;
    private boolean cnst;
    private Ref ref;
    private Node init;
    private ArrayList<Node> defs = new ArrayList<>();

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
        if (!cs.isTyp(Typ.INTTK) && !cs.isTyp(Typ.VOIDTK)) return false;
        vtyp = cs.cont();
        cs.nex();
        (ref = new Ref(false, true)).forward();
        if (ref.gettyp() == NTyp.FuncDef) {
            typ = NTyp.FuncDef;
            dump(NTyp.FuncType, vtyp.p + 1);
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
        while (!cs.isTyp(Typ.SEMICN)) cs.nex();
        cs.nex();
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
