package engine.instr;

public class InstrLS extends Instr {
    public int rt, ix, base;

    public InstrLS(Op op, int rt, int base, int ix) {
        this.op = op;
        this.rt = rt;
        this.base = base;
        this.ix = ix;
    }

    @Override
    public String toString() {
        return op + " " + getReg(rt) + " " + base + "(" + getReg(ix) + ")";
    }
}
