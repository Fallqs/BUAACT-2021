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

    public ConstDecl() {}

    @Override
    public Boolean forward() {
        if(!cs.isTyp(Typ.CONSTTK)) return false;
        cs.p++;
        btype = New.typ(NTyp.BType);
        if(!btype.forward()) cs.p++;
        Node def = New.typ(NTyp.ConstDef);
        if(!def.forward()){}
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
