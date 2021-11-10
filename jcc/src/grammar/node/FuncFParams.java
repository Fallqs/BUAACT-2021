package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import meta.midt.MFunc;
import word.Typ;

import java.util.ArrayList;

public class FuncFParams extends Node {
    private final ArrayList<FuncFParam> params = new ArrayList<>();

    public FuncFParams() {
        typ = NTyp.FuncFParams;
    }

    /* FuncFParams → FuncFParam { ',' FuncFParam } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.FuncFParam);
        if (!ch.fwd()) return false;
        params.add((FuncFParam) ch);
        while (cs.isTyp(Typ.COMMA)) {
            cs.nex();
            if (!(ch = New.typ(NTyp.FuncFParam)).fwd()) break;
            params.add((FuncFParam) ch);
        }
        return true;
    }

    public void logIdt() {
        for (Node i : params) i.logIdt();
    }

    @Override
    public Meta translate() {
        for (Node o : params) o.translate();
        return null;
    }

    public Meta translate(MFunc func) {
        for (FuncFParam p : params) func.params.add(p.parse());
        return null;
    }
}
