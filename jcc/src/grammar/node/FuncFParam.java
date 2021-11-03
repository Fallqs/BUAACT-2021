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
    private final ArrayList<Node> ind = new ArrayList<>();

    public FuncFParam() {
        typ = NTyp.FuncFParam;
    }

    /* FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }] */
    @Override
    public boolean forward() {
        if (!(new BType(this).fwd())) return false;  // type stored in (Result)this.key
        cs.chkTil(Typ.IDENFR);
        name = cs.cont();
        if (cs.nex().isTyp(Typ.LBRACK)) {
            ind.add(null);
            cs.nex().chkErr(Typ.RBRACK).nex();
            Node ch;
            while (cs.isTyp(Typ.LBRACK)) {
                cs.nex();
                if (!(ch = New.typ(NTyp.ConstExp)).fwd()) break;
                ind.add(ch);
                cs.chkErr(Typ.RBRACK).nex();
            }
        }
        return true;
    }

    public void logIdt() {
        idt.cur.buf.name = name.text;
        idt.cur.buf.dim = ind.size();
        idt.cur.buf.cnst = idt.cur.buf.zero = false;
        if (!idt.cur.addVar(true)) cs.chkErr(Typ.IDENFR, name);
    }

    @Override
    public Meta compile() {
        return null;
    }
}
