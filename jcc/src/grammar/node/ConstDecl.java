package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

import java.util.ArrayList;

public class ConstDecl extends Node {
    private Node btype;
    private ArrayList<Node> constDef;

    public ConstDecl() {
        typ = NTyp.ConstDecl;
    }

    /* ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';' */
    /* 'const': CONSTTK; ',': COMMA; ';': SEMICN */
    @Override
    public boolean forward() {
        if(!cs.isTyp(Typ.CONSTTK)) return false;
        cs.nex();
        btype = New.typ(NTyp.BType);
        if(!btype.fwd()) cs.nex();
        Node def = New.typ(NTyp.ConstDef);
        while(def.fwd()){
            constDef.add(def);
            if(!cs.isTyp(Typ.COMMA))break;
            cs.nex();
            def = New.typ(NTyp.ConstDef);
        }
        while(!cs.isTyp(Typ.SEMICN))cs.nex();
        cs.nex();
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
