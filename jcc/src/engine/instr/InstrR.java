package engine.instr;

import engine.MetaAlloc;

public class InstrR extends Instr {
    public int rd, rs, rt;

    public InstrR(Op op, int rd, int rs, int rt) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
    }

    @Override
    public String toString() {
        return op + " " + getReg(rd) + ", " + getReg(rs) + ", " + getReg(rt);
    }
}
