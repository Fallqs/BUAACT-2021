package meta.mcode;

import engine.LgK;
import engine.instr.*;
import meta.Meta;
import meta.Opr;

public class Calc extends Meta {
    private Meta ma, mb;
    private Opr opr;

    public Calc(Opr opr, Meta ma, Meta mb) {
        this.opr = opr;
        asLegend(this.ma = ma);
        asLegend(this.mb = mb);
    }

    public Calc(Opr opr, Meta ma) {
        this.opr = opr;
        asLegend(this.ma = ma);
        this.mb = Nop;
    }

    public Calc(int v) {
        val = v;
        opr = Opr.cnst;
        ma = mb = Nop;
        cnst = true;
    }

    public LgK toLgk() {
        int aid = ma == null || ma.eqls == null ? -1 : ma.eqls.id;
        int bid = mb == null || mb.eqls == null ? -1 : mb.eqls.id;
        return new LgK(opr, aid, bid);
    }

    public boolean isCnst() {
        return cnst = cnst || (ma == null || ma.isCnst()) && (mb == null || mb.isCnst());
    }

    public int calc() {
        if (opr == Opr.cnst) return val;
        if (ma != null) ma.calc();
        else opr = Opr.lw;
        if (mb != null) mb.calc();
        else opr = Opr.not;
        switch (opr) {
            case add:
                val = ma.val + mb.val;
                break;
            case sub:
                val = ma.val - mb.val;
                break;
            case mult:
                val = ma.val * mb.val;
                break;
            case div:
                val = ma.val / mb.val;
                break;
            case mod:
                val = ma.val % mb.val;
                break;
            case and:
                val = ma.val != 0 && mb.val != 0 ? 1 : 0;
                break;
            case or:
                val = ma.val != 0 || mb.val != 0 ? 1 : 0;
                break;
            case not:
                val = ma == null || ma.val == 0 ? 1 : 0;
                break;
            default:
                val = 0;
        }
        return val;
    }

    @Override
    public void shrink() {
        if (isCnst()) {
            opr = Opr.cnst;
            return;
        }
        ma = ma.eqls;
        if (mb != null) mb = mb.eqls;
    }

    @Override
    public String toString() {
        if (opr == Opr.cnst) return "(" + val + " -> T" + id + ")";
        else if (opr == Opr.not) return "(" + opr + " T" + ma.id + " -> T" + id + ")";
        return "(T" + ma.id + " " + opr + " T" + mb.id + " -> T" + id + ")";
    }

    @Override
    public Meta[] prevs() {
        return mb == Nop ? (ma == Nop ? new Meta[0] : new Meta[]{ma}) : (ma == Nop ? new Meta[]{mb} : new Meta[]{ma, mb});
    }

    @Override
    public Instr translate() {
        ma = ma.eqls;
        mb = mb.eqls;
        int tar = gtag(Instr.V0);
        Instr ret = null;
        if (opr == Opr.cnst) ret = new InstrSI(Op.li, tar, val);
        else if (opr == Opr.add) ret = new InstrR(Op.add, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.sub) ret = new InstrR(Op.sub, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.and) ret = new InstrR(Op.and, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.or) ret = new InstrR(Op.or, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.not) ret = new InstrI(Op.sltiu, tar, ma.get(Instr.V0), 1);
        else if (opr == Opr.lt) ret = new InstrR(Op.slt, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.gt) ret = new InstrR(Op.slt, tar, mb.get(Instr.A0), ma.get(Instr.V0));
        else if (opr == Opr.neq) ret = new InstrR(Op.sltu, tar, mb.get(Instr.A0), ma.get(Instr.V0));
        else if (opr == Opr.eql) {
            new InstrR(Op.xor, tar, ma.get(Instr.V0), mb.get(Instr.A0));
            ret = new InstrI(Op.sltiu, tar, tar, 1);
        } else if (opr == Opr.mult) {
            new InstrDual(Op.mult, ma.get(Instr.V0), mb.get(Instr.A0));
            ret = new InstrS(Op.mflo, tar);
        } else if (opr == Opr.div) {
            new InstrDual(Op.div, ma.get(Instr.V0), mb.get(Instr.A0));
            ret = new InstrS(Op.mflo, tar);
        } else if (opr == Opr.mod) {
            new InstrDual(Op.div, ma.get(Instr.V0), mb.get(Instr.A0));
            ret = new InstrS(Op.mfhi, tar);
        }
        if (tar == Instr.V0) new InstrLS(Op.sw, Instr.V0, spx, Instr.SP);
        return ret;
    }
}
