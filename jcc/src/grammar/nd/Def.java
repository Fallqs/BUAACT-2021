package grammar.nd;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class Def extends Node {
    private Node ref, val;
    private NTyp vtyp;

    public Def(boolean cnst) {
        this.typ = cnst ? NTyp.ConstDef : NTyp.VarDef;
        vtyp = cnst ? NTyp.ConstInitVal : NTyp.InitVal;
    }

    public Def(boolean cnst, Node ref, Node val) {
        this(cnst);
        this.ref = ref;
        this.val = val;
    }

    @Override
    public boolean forward() {
        if (ref != null) return true;
        if (!(ref = new Ref(false, true)).forward()) return false;
        if (cs.isTyp(Typ.ASSIGN)) {
            cs.nex();
            (val = New.typ(vtyp)).fwd();
        }
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
