package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class BType extends Node {
    public BType() {
        typ = NTyp.BType;
    }

    /* BType → 'int' */
    @Override
    public boolean forward() {
        if(cs.isTyp(Typ.INTTK)){
            cs.nex();
            return true;
        }
        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
