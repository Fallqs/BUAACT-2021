package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import lex.nd.Ref;
import meta.Meta;
import meta.mcode.Call;
import meta.midt.MFunc;
import word.Typ;

import java.util.ArrayList;
import java.util.List;

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
        int size = idt.fun.paramSiz();
        for (Node i : params) i.logIdt();
        if (idt.fun == null) return;
        idt.fun.buf.paramCnt = (params.size() != size);
        if (idt.fun.buf.paramCnt) return;
        boolean err = false;
        for (int i = 0; i < params.size(); ++i) err |= !idt.fun.chkParam(i, params.get(i).rets());
        idt.fun.buf.paramErr = err;
    }

    public Call translate(MFunc func) {
        if (params.size() != func.params.size()) {
            cs.chkErr(Typ.PARAMCNT, ((Ref) fa).name);
        }
        List<Meta> para = new ArrayList<>();
        for(Node o: params)para.add(o.translate());
        return new Call(func, para.toArray(new Meta[0]));
    }
}
