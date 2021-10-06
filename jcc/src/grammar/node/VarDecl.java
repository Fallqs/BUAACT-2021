package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

import java.util.ArrayList;

public class VarDecl extends Node {
    Node btype;
    ArrayList<Node> vardef = new ArrayList<>();

    public VarDecl() {
        typ = NTyp.VarDecl;
    }

    /**
     * VarDecl → BType VarDef { ',' VarDef } ';'
     */
    @Override
    public boolean forward() {
        if (!(btype = New.typ(NTyp.BType)).fwd()) return false;
        Node def = New.typ(NTyp.VarDef);
        while (def.fwd()) {
            vardef.add(def);
            if (!cs.isTyp(Typ.COMMA)) break;
            cs.nex();
            def = New.typ(NTyp.VarDef);
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
