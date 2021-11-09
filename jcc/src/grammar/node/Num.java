package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;
import meta.ident.Var;
import word.Result;
import word.Typ;

public class Num extends Node {
    Result cont;
    public Num() {
        typ = NTyp.Number;
    }

    /* Number → IntConst */
    @Override
    public boolean forward() {
        if(!cs.isTyp(Typ.INTCON))return false;
        cont = cs.cont();
        cs.nex();
        return true;
    }

    @Override
    public Var rets() {
        return new Var(0).setCnst(true);
    }

    @Override
    public Meta translate() {
        return null;
    }
}
