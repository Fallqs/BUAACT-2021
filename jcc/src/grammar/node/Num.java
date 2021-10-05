package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;
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
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
