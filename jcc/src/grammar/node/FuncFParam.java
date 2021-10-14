package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class FuncFParam extends Node {
    private Result name;
    private ArrayList<Node> ind = new ArrayList<>();

    public FuncFParam() {
        typ = NTyp.FuncFParam;
    }

    /* FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }] */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.INTTK)) return false;
        cs.chkTil(Typ.IDENFR);
        name = cs.cont();
        if (cs.nex().isTyp(Typ.LBRACK)) {
            ind.add(null);
            cs.nex().chkErr(Typ.RBRACK).nex();
            Node ch;
            while (cs.isTyp(Typ.LBRACK)) {
                cs.nex();
                if(!(ch = New.typ(NTyp.ConstExp)).fwd()) break;
                ind.add(ch);
                cs.chkErr(Typ.RBRACK).nex();
            }
        }
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
