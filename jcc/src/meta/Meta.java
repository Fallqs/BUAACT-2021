package meta;

import engine.Dojo;
import engine.instr.*;
import engine.LgK;
import meta.midt.MVar;

public class Meta implements Comparable<Meta> {
    public final int id;
    public int legend;
    public boolean cnst = false, valid = false, concrete = false;
    public Meta eqls;
    public static int cnt = 0;
    /**
     * val : deduced Value
     * reg : Register ID, -1 for No Register
     * spx : Index of Position on Stack
     */
    public int val = 0, reg = -1, spx = -1;
    public MVar save;

    public Meta() {
        legend = (id = ++cnt) << 1;
        eqls = this;
        Dojo.embed(this);
    }

    public Meta(boolean embed) {
        legend = (id = ++cnt) << 1;
        eqls = this;
    }

    private Meta(int x) {
        legend = (id = ++cnt) << 1;
        cnst = true;
        reg = Instr.ZERO;
        spx = 0;
        eqls = this;
    }

    public static final Meta Nop = new Meta(0);

    public String toString() {
        return "(=> " + id + ")";
    }

    public LgK toLgk() {
        return new LgK(Opr.lw, this, this);
    }

    public int calc() {
        return 0;
    }

    public boolean isCnst() {
        return false;
    }

    public void shrink() {
    }

    public Meta eqls() {
        return eqls = this.eqls == this ? this : this.eqls.eqls();
    }

    public void addLegend(Meta m) {
        legend = Integer.min(legend, m.legend);
        legend = Integer.min(legend, m.id * 2 - 1);
    }

    public void asLegend(Meta m) {
        m.addLegend(this);
    }

    @Override
    public int compareTo(Meta o) {
        return legend != o.legend ? Integer.compare(legend, o.legend) : Integer.compare(id, o.id);
    }

    public Instr translate() {
        return new Nop();
    }

    public Meta[] prevs() {
        return new Meta[0];
    }

    public int get(int tmp) {
        if (this == Nop) return Instr.ZERO;
//        if (this.isCnst()) {
//            new InstrSI(Op.li, tmp, calc());
//            return tmp;
//        }
        if (reg >= 0) return reg;
        new InstrLS(Op.lw, tmp, spx, Instr.SP);
        return tmp;
    }

    public int get(int tmp, int shift) {
        if (this == Nop) return Instr.ZERO;
//        if (this.isCnst()) {
//            new InstrSI(Op.li, tmp, calc());
//            return tmp;
//        }
        if (reg >= 0) return reg;
        new InstrLS(Op.lw, tmp, spx + shift, Instr.SP);
        return tmp;
    }

    public int gtag(int tmp) {
        if (this == Nop) return Instr.ZERO;
        return reg >= 0 ? reg : tmp;
    }

    public void setSave(int reg) {
        if (save != null && save.global)
            new InstrLS(Op.sw, reg, save.base, Instr.GP);
    }
}
