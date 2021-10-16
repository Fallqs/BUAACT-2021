package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

import java.util.ArrayList;

public class FuncRParams extends Node {
    private final ArrayList<Node> params = new ArrayList<>();

    public FuncRParams() {
        typ = NTyp.FuncRParams;
    }

    /* FuncRParams → Exp { ',' Exp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.Exp);
        if (!ch.fwd()) return false;
        params.add(ch);
        while (cs.isTyp(Typ.COMMA)) {
            cs.nex();
            if (!(ch = New.typ(NTyp.Exp)).fwd()) break;
            params.add(ch);
        }
        return true;
    }

    public void logIdt() {
        idt.fun.buf.paramCnt = (params.size() == idt.fun.paramSiz());
        if(idt.fun.buf.paramCnt) return;
        boolean err = false;
        for (int i = 0; i < params.size(); ++i) err |= !idt.fun.chkParam(i, params.get(i).rets());
        idt.fun.buf.paramErr = err;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
