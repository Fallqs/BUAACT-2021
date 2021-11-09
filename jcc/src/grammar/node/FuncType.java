package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class FuncType extends Node {
    private boolean ret; // ret === (Type == 'int')

    public FuncType() {
        typ = NTyp.FuncType;
    }

    /* FuncType → 'void' | 'int' */
    @Override
    public boolean forward() {
        ret = cs.isTyp(Typ.INTTK);
        if (ret || cs.isTyp(Typ.VOIDTK)) {
            cs.nex();
            return true;
        }
        return false;
    }

    @Override
    public Meta translate() {
        return null;
    }
}
