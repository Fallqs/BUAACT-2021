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
    private ArrayList<Node> ind;

    public FuncFParam() {
        typ = NTyp.FuncFParam;
    }

    /* FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }] */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.INTTK)) return false;
        while (!cs.isTyp(Typ.IDENFR)) cs.nex();
        name = cs.cont();
        if (cs.nex().isTyp(Typ.LBRACK)) {
            ind.add(null);
            while (!cs.isTyp(Typ.RBRACK)) cs.nex();
            cs.nex();
            Node ch;
            while (cs.isTyp(Typ.LBRACK)) {
                if(!(ch = New.typ(NTyp.ConstExp)).fwd()) break;
                ind.add(ch);
                while(!cs.isTyp(Typ.RBRACK))cs.nex();
                cs.nex();
            }
        }
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
