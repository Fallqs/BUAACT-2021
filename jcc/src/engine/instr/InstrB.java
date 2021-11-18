package engine.instr;

import engine.MetaAlloc;
import engine.sync.SyncR;

public class InstrB extends Instr {
    public int rs, rt;
    public String label;

    public InstrB(Op op, int rs, int rt, String tar) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.label = tar;
    }

    public InstrB(Op op, int rs, int rt, SyncR tar) {
        this(op, rs, rt, tar.toString());
    }

    public InstrB(Op op, int rs, int rt, Nop tar) {
        this(op, rs, rt, tar.toString());
    }

    @Override
    public String toString() {
        return op + " " + getReg(rs) + ", $" + getReg(rt) + ", " + label;
    }
}
