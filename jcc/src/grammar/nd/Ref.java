package grammar.nd;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import grammar.node.Block;
import meta.Meta;
import meta.ident.Var;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class Ref extends Node {
    public Result name;
    private final ArrayList<Node> params = new ArrayList<>();
    private Block body;
    private final NTyp ind;
    private final NTyp para;

    public Ref(boolean display, boolean def) {
        this.typ = NTyp.NULL;
        this.autoDisplay = display;
        ind = def ? NTyp.ConstExp : NTyp.Exp;
        para = def ? NTyp.FuncFParams : NTyp.FuncRParams;
    }

    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.IDENFR) && !cs.isTyp(Typ.MAINTK)) return false;
        name = cs.cont();
        switch (cs.nex().typ()) {
            case LBRACK: {//[
                this.typ = NTyp.LVal;
                cs.nex();
                Node ch;
                while ((ch = New.typ(ind)).fwd()) {
                    params.add(ch);
                    cs.chkErr(Typ.RBRACK).nex();
                    if (!cs.isTyp(Typ.LBRACK)) break;
                    cs.nex();
                }
                break;
            }
            case LPARENT: { //(
                this.typ = name.typ == Typ.MAINTK ? NTyp.MainFuncDef : NTyp.FuncDef;
                cs.nex();
                Node ch = New.typ(para);
                if (ch.fwd()) params.add(ch);
                cs.chkErr(Typ.RPARENT).nex();
                (body = new Block()).fwd();
                break;
            }
            default:
                this.typ = NTyp.LVal;
        }
        return true;
    }

    public void logIdt() {
        if (idt.cur.buf.onDecl) {
            idt.cur.buf.name = name.text;
            if (typ == NTyp.LVal) {
                for (Node i : params) i.logIdt();
                idt.cur.buf.dim = params.size();
            } else {
                if (!idt.merge()) cs.chkErr(Typ.IDENFR, name);
                if (!params.isEmpty()) params.get(0).logIdt();
                idt.cur.buf.onDecl = false;
                body.func = true;
                body.logIdt();
            }
        } else {
            if (typ == NTyp.LVal) {
                for (Node i : params) i.logIdt();
                Var var = idt.query(name.text);
                if (var == null) cs.chkErr(Typ.NULL, name);
                else if (idt.cur.buf.nonConst && !var.cnst) cs.chkErr(Typ.CONSTTK, name);
            } else {
                if (idt.func(name.text) == null) {
                    cs.chkErr(Typ.NULL, name);
                    if (!params.isEmpty()) params.get(0).logIdt();
                } else {
                    if (!params.isEmpty()) {
                        params.get(0).logIdt();
                        if (idt.fun.buf.paramCnt) cs.chkErr(Typ.PARAMCNT, name);
                        else if (idt.fun.buf.paramErr) cs.chkErr(Typ.PARAMTYP, name);
                    } else if (idt.fun.paramSiz() != 0) cs.chkErr(Typ.PARAMCNT, name);
                }
                idt.ofun();
            }
        }
    }

    @Override
    public Var rets() {
        if (typ != NTyp.LVal) return (idt.qfun(name.text) == Typ.VOIDTK)? new Var(Typ.VOIDTK): new Var(0);
        return new Var(idt.query(name.text), params.size());
    }

    @Override
    public Meta translate() {
        return null;
    }
}
