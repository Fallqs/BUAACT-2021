package meta.mcode;

import engine.LgK;
import engine.instr.Instr;
import engine.instr.InstrI;
import engine.instr.InstrLS;
import engine.instr.InstrDual;
import engine.instr.InstrR;
import engine.instr.InstrS;
import engine.instr.Op;
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
        this.mb = null;
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
    public void collect() {
        if (ref == 0) {
            if (ma != null) ma.collect();
            if (mb != null) mb.collect();
        }
        super.collect();
    }

    @Override
    public Meta[] prevs() {
        return mb == null ? new Meta[]{ma} : new Meta[]{ma, mb};
    }

    @Override
    public Instr translate() {
        int tar = reg >= 0 ? reg : Instr.V0;
        Instr ret = null;
        if (opr == Opr.add) ret = new InstrR(Op.add, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.sub) ret = new InstrR(Op.sub, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.not) ret = new InstrI(Op.sltiu, tar, ma.get(Instr.V0), 1);
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
