package meta.mcode;

import engine.LgK;
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
}
