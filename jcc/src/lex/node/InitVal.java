package lex.node;

import engine.Dojo;
import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import meta.mcode.PArr;
import meta.midt.MVar;
import word.Typ;

import java.util.ArrayList;
import java.util.List;

public class InitVal extends Node {
    private Node exp;
    private final ArrayList<InitVal> init = new ArrayList<>();

    public InitVal() {
        typ = NTyp.InitVal;
    }

    public InitVal(boolean cnst) {
        this.typ = cnst ? NTyp.ConstInitVal : NTyp.InitVal;
    }

    /**
     * InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
     */
    @Override
    public boolean forward() {
        if ((exp = New.typ(typ == NTyp.InitVal ? NTyp.Exp : NTyp.ConstExp)).fwd()) return true;
        else if (cs.isTyp(Typ.LBRACE)) {
            cs.nex();
            exp = null;
            Node ch;
            while ((ch = New.typ(typ)).fwd()) {
                init.add((InitVal) ch);
                if (!cs.isTyp(Typ.COMMA)) break;
                cs.nex();
            }
            cs.chkTil(Typ.RBRACE).nex();
            return true;
        }
        return false;
    }

    @Override
    public void logIdt() {
        if (exp != null) exp.logIdt();
        for (Node i : init) i.logIdt();
    }

    public Meta translate(MVar var) {
        if (exp != null) {
            Meta v = exp.translate();
            Dojo.upd(var, v);
            var.initc(v.calc());
            return Meta.Nop;
        } else if (typ == NTyp.InitVal)
            return new PArr(var, values(new ArrayList<>()).toArray(new Meta[0]));
        else {
            List<Meta> ms = values(new ArrayList<>());
            int[] v = new int[ms.size()];
            for (int i = 0; i < ms.size(); ++i) v[i] = ms.get(i).calc();
            var.initc(v);
            return Meta.Nop;
        }
    }

    private List<Meta> values(List<Meta> v) {
        if (exp != null) v.add(exp.translate());
        else for (InitVal i : init) i.values(v);
        return v;
    }
}
