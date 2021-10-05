package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;

public class Decl extends Node {
    private Node decl;

    public Decl() {
        typ = NTyp.Decl;
    }

    /* Decl → ConstDecl | VarDecl */
    @Override
    public boolean forward() {
        decl = New.typ(NTyp.ConstDecl);
        if (decl.forward()) return true;
        decl = New.typ(NTyp.VarDecl);
        return decl.forward();
    }

    @Override
    public Meta compile() {
        return null;
    }
}
