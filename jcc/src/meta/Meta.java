package meta;

import engine.Dojo;
import engine.instr.Instr;
import engine.LgK;
import engine.instr.InstrLS;
import engine.instr.Nop;
import engine.instr.Op;

public class Meta implements Comparable<Meta> {
    public final int id;
    private int legend;
    public boolean cnst = false, valid = false;
    public Meta eqls;
    public static int cnt = 0;
    /**
     * val : deduced Value
     * reg : Register ID, -1 for No Register
     * spx : Index of Position on Stack
     */
    public int val = 0, reg = -1, spx = -1;

    public Meta() {
        legend = (id = ++cnt) << 1;
        eqls = this;
        Dojo.embed(this);
    }

    public Meta (boolean embed) {
        legend = (id = ++cnt) << 1;
        eqls = this;
    }

    public static final Meta Nop = new Meta();

    public String toString() {
        return "(=> " + id + ")";
    }

    public LgK toLgk() {
        return new LgK(Opr.lw, id, id);
    }

    public int calc() {
        return 0;
    }

    public boolean isCnst() {
        return false;
    }

    public void shrink() {
    }

    public void addLegend(Meta m) {
        legend = Integer.min(legend, m.legend);
        legend = Integer.min(legend, m.id * 2 - 1);
    }

    public Meta asLegend(Meta m) {
        m.addLegend(this);
        return this;
    }

    public int ref = 0;

    public void collect() {
        ++ref;
        valid = true;
    }

    @Override
    public int compareTo(Meta o) {
        return Integer.compare(legend, o.legend);
    }

    public Instr translate() {
        return new Nop();
    }

    public Meta[] prevs() {
        return new Meta[0];
    }

    public int get(int tmp) {
        if (reg >= 0) return reg;
        new InstrLS(Op.lw, tmp, spx, Instr.SP);
        return tmp;
    }

    public int gtag(int tmp) {
        return reg >= 0 ? reg : tmp;
    }
}
