package grammar.nd;

import engine.Dojo;
import engine.SyncO;
import engine.SyncR;
import grammar.NTyp;
import grammar.New;
import grammar.Node;
import grammar.node.Block;
import grammar.node.FuncFParams;
import grammar.node.FuncRParams;
import meta.Meta;
import meta.ident.Var;
import meta.mcode.Call;
import meta.mcode.GVal;
import meta.mcode.Get;
import meta.midt.MFunc;
import meta.midt.MIdt;
import meta.midt.MTable;
import meta.midt.MTyp;
import meta.midt.MVar;
import word.Result;
import word.Typ;

import java.util.ArrayList;
import java.util.List;

public class Ref extends Node {
    public Result name;
    public MTyp rettyp;
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
        if (typ != NTyp.LVal) return (idt.qfun(name.text) == Typ.VOIDTK) ? new Var(Typ.VOIDTK) : new Var(0);
        return new Var(idt.query(name.text), params.size());
    }

    @Override
    public Meta translate() {
        if (typ == NTyp.LVal) {
            if (ind == NTyp.ConstExp) {
                int[] dim = new int[params.size()];
                for (int i = 0; i < params.size(); ++i) dim[i] = params.get(i).translate().calc();
                return new Get(new MVar(name.text, dim));
            } else {
                MIdt var = MTable.qryIdt(name.text);
                if (!(var instanceof MVar)) {
                    // cs.chkErr(Typ.NULL, name);
                    return null;
                }
                if (params.isEmpty()) return Dojo.qry((MVar) var);

                List<Meta> ix = new ArrayList<>();
                for (Node o : params) ix.add(o.translate());
                return new GVal((MVar) var, ix.toArray(new Meta[0]));
            }
        } else {
            if (para == NTyp.FuncFParams) {
                MFunc func = new MFunc(name.text, rettyp, new SyncR());
                if (!MTable.newIdt(func)) {
                    // cs.chkErr(Typ.NULL, name);
                    return null;
                }
                Dojo.curReq.fa.add(new SyncO());
                MTable.newBlock();
                if (!params.isEmpty()) ((FuncFParams) params.get(0)).translate(func);
                body.translate();
                MTable.popBlock();
                Dojo.cleanReq();
                Dojo.cleanOpr();
            } else {
                MIdt func = MTable.qryIdt(name.text);
                if (!(func instanceof MFunc)) {
                    // cs.chkErr(Typ.NULL, name);
                    return null;
                }
                Call ret;
                if(!params.isEmpty()) {
                    params.get(0).fa = this;
                    ret = ((FuncRParams) params.get(0)).translate((MFunc) func);
                } else {
                    ret = new Call((MFunc) func);
                }
                return ret;
            }
        }
        return null;
    }
}
