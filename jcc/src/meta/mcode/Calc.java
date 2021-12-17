package meta.mcode;

import engine.LgK;
import engine.instr.*;
import meta.Meta;
import meta.Opr;
import meta.midt.MPin;

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
        int aid = ma == null || ma.eqls == null ? -1 : ma.eqls().id;
        int bid = mb == null || mb.eqls == null ? -1 : mb.eqls().id;
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
                val = mb.val == 0 ? ma.val : ma.val / mb.val;
                break;
            case mod:
                val = mb.val == 0 ? ma.val : ma.val % mb.val;
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
            case neq:
                val = ma.val ^ mb.val;
                break;
            case lt:
                val = ma.val < mb.val ? 1 : 0;
                break;
            case gt:
                val = ma.val > mb.val ? 1 : 0;
                break;
            case eql:
                val = ma.val == mb.val ? 1 : 0;
                break;
        }
        return val;
    }

    @Override
    public void shrink() {
        if (isCnst()) {
            opr = Opr.cnst;
            return;
        }
        ma = ma.eqls();
        if (mb != null) mb = mb.eqls();
    }

    @Override
    public String toString() {
        if (opr == Opr.cnst) return "(" + val + " -> T" + id + ")";
        else if (opr == Opr.not) return "(" + opr + " T" + ma.id + " -> T" + id + ")";
        return "(T" + ma.eqls().id + " " + opr + " T" + mb.eqls().id + " -> T" + id + ")";
    }

    @Override
    public Meta[] prevs() {
        return mb == Nop ? (ma == Nop ? new Meta[0] : new Meta[]{ma}) : (ma == Nop ? new Meta[]{mb} : new Meta[]{ma, mb});
    }

    private int madd(int tar, boolean save) {
        long b = mb.calc();
        int lg = 0;
        for (long x = Math.abs(b) - 1; x > 0; x >>= 1) ++lg;
        long magic = (1L << 31 + lg) / b + (b >= 0 ? 1 : -1); // b != 2^n, \forall n \in N
        int rega;
        if ((magic & 1) != 0) {
            rega = ma.get(Instr.A0);
            new InstrR(Op.slt, Instr.V0, rega, Instr.ZERO);
            new InstrR(Op.subu, Instr.V0, Instr.ZERO, Instr.V0);
            new InstrS(Op.mthi, Instr.V0);

            if (save && rega == tar) {
                new InstrDual(Op.move, Instr.A0, rega);
                rega = Instr.A0;
            }
            new InstrI(Op.sra, Instr.V0, rega, 1);
            new InstrS(Op.mtlo, Instr.V0);
            new InstrSI(Op.li, Instr.V0, (int) (magic / 2));
            new InstrDual(Op.madd, Instr.V0, rega);
        } else {
            new InstrSI(Op.li, Instr.V0, (int) (magic / 2));
            new InstrDual(Op.mult, Instr.V0, rega = ma.get(Instr.A0));
            if (save && rega == tar) {
                new InstrDual(Op.move, Instr.A0, rega);
                rega = Instr.A0;
            }
        }
        new InstrS(Op.mfhi, tar);
        if (lg > 2) new InstrI(Op.sra, tar, tar, lg - 2);
        MPin pin = new MPin("");
        new InstrBZ(Op.bgez, tar, pin);
        new InstrI(Op.addi, tar, tar, 1);
        pin.pin = new Nop().toString(true);
        return rega;
    }

    private Instr div(int tar) {
        if (!mb.isCnst()) {
            new InstrDual(Op.div, ma.get(Instr.V0), mb.get(Instr.A0));
            return new InstrS(Op.mflo, tar);
        }
        if (ma.isCnst()) return new InstrSI(Op.li, tar, ma.calc() / mb.calc());

        long b = mb.calc(), c = Math.abs(b);
        if (c == (c & -c)) {
            int rgx = ma.get(Instr.V0);
            if (b == 1) {
                if (ma.get(tar) != tar) new InstrDual(Op.move, tar, ma.get(tar));
                return null;
            } else if (b == -1) {
                return new InstrR(Op.subu, tar, Instr.ZERO, ma.get(tar));
            }
            if (b < 0) {
                new InstrR(Op.subu, Instr.V0, Instr.ZERO, rgx);
                rgx = Instr.V0;
            }
            int lg = 0;
            for (long x = c; (x >>= 1) > 0; ) ++lg;
            if (lg > 0) new InstrI(Op.sra, tar, rgx, lg);

            MPin pin1 = new MPin("");
            new InstrBZ(Op.bgez, rgx, pin1);
            new InstrI(Op.andi, Instr.A0, rgx, (int) (c - 1));
            new InstrR(Op.slt, Instr.A0, Instr.ZERO, Instr.A0);
            new InstrR(Op.addu, tar, tar, Instr.A0);
            pin1.pin = new Nop().toString(true);
            return null;
        }

        madd(tar, false);
        return null;
    }

    private Instr mod(int tar) {
        if (!mb.isCnst()) {
            new InstrDual(Op.div, ma.get(Instr.V0), mb.get(Instr.A0));
            return new InstrS(Op.mfhi, tar);
        }
        if (ma.isCnst()) return new InstrSI(Op.li, tar, ma.calc() % mb.calc());

        long b = mb.calc();
        b = Math.abs(b);
        if (b == (b & -b)) {
            if (b == 1) return new InstrDual(Op.move, tar, Instr.ZERO);
            int rgx = ma.get(Instr.V0);
            new InstrI(Op.andi, tar, rgx, (int) (b - 1));
            MPin pin = new MPin("");
            new InstrBZ(Op.bgez, rgx, pin);
            new InstrI(Op.addi, tar, tar, -(int) b);
            pin.pin = new Nop().toString(true);
            return null;
        }
        int rgx = madd(tar, true);
//        new InstrS(Op.mtlo, rgx);
        new InstrSI(Op.li, Instr.V0, mb.calc());
        new InstrDual(Op.mult, Instr.V0, tar);
        new InstrS(Op.mflo, tar);
        return new InstrR(Op.subu, tar, rgx, tar);
//        return new InstrS(Op.mflo, tar);
    }

    @Override
    public Instr translate() {
        ma = ma.eqls();
        mb = mb.eqls();
        int tar = gtag(Instr.V0);
        Instr ret = null;
        if (opr == Opr.cnst) ret = new InstrSI(Op.li, tar, val);
        else if (opr == Opr.add) ret = new InstrR(Op.addu, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.sub) ret = new InstrR(Op.subu, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.and) ret = new InstrR(Op.and, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.or) ret = new InstrR(Op.or, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.not) ret = new InstrI(Op.sltiu, tar, ma.get(Instr.V0), 1);
        else if (opr == Opr.lt) ret = new InstrR(Op.slt, tar, ma.get(Instr.V0), mb.get(Instr.A0));
        else if (opr == Opr.gt) ret = new InstrR(Op.slt, tar, mb.get(Instr.A0), ma.get(Instr.V0));
        else if (opr == Opr.neq) ret = new InstrR(Op.xor, tar, mb.get(Instr.A0), ma.get(Instr.V0));
        else if (opr == Opr.eql) {
            new InstrR(Op.xor, tar, ma.get(Instr.V0), mb.get(Instr.A0));
            ret = new InstrI(Op.sltiu, tar, tar, 1);
        } else if (opr == Opr.mult) {
            new InstrDual(Op.mult, ma.get(Instr.V0), mb.get(Instr.A0));
            ret = new InstrS(Op.mflo, tar);
        } else if (opr == Opr.div) {
//            new InstrDual(Op.div, ma.get(Instr.V0), mb.get(Instr.A0));
//            ret = new InstrS(Op.mflo, tar);
            ret = div(tar);
        } else if (opr == Opr.mod) {
//            new InstrDual(Op.div, ma.get(Instr.V0), mb.get(Instr.A0));
//            ret = new InstrS(Op.mfhi, tar);
            ret = mod(tar);
        }
        setSave(tar);
        if (tar == Instr.V0) new InstrLS(Op.sw, Instr.V0, spx, Instr.SP);
        return ret;
    }
}
